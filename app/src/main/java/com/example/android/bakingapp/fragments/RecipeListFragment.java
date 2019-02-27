package com.example.android.bakingapp.fragments;


import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.bakingapp.R;
import com.example.android.bakingapp.adapter.RecipeListAdapter;
import com.example.android.bakingapp.model.Recipe;
import com.example.android.bakingapp.viewmodel.ListViewModel;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class RecipeListFragment extends Fragment {

    private static String TAG = RecipeListFragment.class.getSimpleName();

    private View rootView;
    private RecyclerView recyclerView;
    private RecipeListAdapter adapter;
    private GridLayoutManager gridLayoutManager;


    public RecipeListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_recipe_list, container, false);

        //Setup RecyclerView, GridLayoutManager and Adapter
        recyclerView = rootView.findViewById(R.id.rv_all_recipes);
        int columnCount = getColumns();
        gridLayoutManager = new GridLayoutManager(getActivity(), columnCount);
        recyclerView.setLayoutManager(gridLayoutManager);
        adapter = new RecipeListAdapter();
        recyclerView.setAdapter(adapter);

        //Kick off ViewModel
        setupViewModel();

        return rootView;
    }

    //Setup the ViewModel
    private void setupViewModel(){
        ListViewModel viewModel = ViewModelProviders.of(this).get(ListViewModel.class);
        viewModel.getRecipes().observe(this, new Observer<List<Recipe>>() {
            @Override
            public void onChanged(@Nullable List<Recipe> recipes) {
                adapter.setRecipes(recipes);
            }
        });
    }

    //Get Screen Size to set number of columns for GridLayout
    private int getColumns(){
        // DefaultColumn Count is 1
        int columns = 1;
        boolean isPhone = getResources().getBoolean(R.bool.isPhone);
        boolean isLandscape = getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;

        if(isPhone && isLandscape){
            columns = 2;
        }else if(!isPhone){
            columns = 3;
        }

        return columns;
    }


}