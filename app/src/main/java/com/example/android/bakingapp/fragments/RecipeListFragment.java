package com.example.android.bakingapp.fragments;


import android.appwidget.AppWidgetManager;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.bakingapp.BakingWidgetProvider;
import com.example.android.bakingapp.R;
import com.example.android.bakingapp.adapter.RecipeListAdapter;
import com.example.android.bakingapp.model.Recipe;
import com.example.android.bakingapp.utils.ConnectivityUtils;
import com.example.android.bakingapp.utils.DeviceUtils;
import com.example.android.bakingapp.utils.PreferenceUtils;
import com.example.android.bakingapp.viewmodel.ListViewModel;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class RecipeListFragment extends Fragment implements RecipeListAdapter.RecipeOnClickHandler {

    private static String TAG = RecipeListFragment.class.getSimpleName();

    private View rootView;
    private RecyclerView recyclerView;
    private TextView noConnectionText;
    private RecipeListAdapter adapter;
    private GridLayoutManager gridLayoutManager;

    private String RECIPE_KEY = "recipe-key";

    public RecipeListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_recipe_list, container, false);

        noConnectionText = rootView.findViewById(R.id.tv_no_connection);

        //Setup RecyclerView, GridLayoutManager and Adapter
        recyclerView = rootView.findViewById(R.id.rv_all_recipes);
        int columnCount = getColumns();
        gridLayoutManager = new GridLayoutManager(getActivity(), columnCount);
        recyclerView.setLayoutManager(gridLayoutManager);
        adapter = new RecipeListAdapter(this);
        recyclerView.setAdapter(adapter);

        //Set the title
        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.app_name);

        if(ConnectivityUtils.isOnline(getActivity().getApplicationContext())){
            //Kick off ViewModel if Device is connected to internet
            noConnectionText.setVisibility(View.INVISIBLE);
            setupViewModel();
        }else {
            recyclerView.setVisibility(View.INVISIBLE);
            noConnectionText.setVisibility(View.VISIBLE);
        }
        return rootView;
    }

    //Setup onClick
    @Override
    public void onClick(Recipe recipe) {
        //Save clicked Recipe to Shared Prefs
        PreferenceUtils.saveRecipeToSharedPreferences(recipe);

        updateWidget();

        //Make fragment transaction
        Bundle data = new Bundle();
        data.putSerializable(RECIPE_KEY, recipe);
        DetailsFragment fragment = new DetailsFragment();
        fragment.setArguments(data);
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.main_content, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    //Update the widget
    private void updateWidget(){
        Intent intent = new Intent(getActivity(), BakingWidgetProvider.class);
        intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        int[] ids = AppWidgetManager.getInstance(getActivity().getApplication())
                .getAppWidgetIds(new ComponentName(getActivity().getApplication(), BakingWidgetProvider.class));
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids);
        getActivity().sendBroadcast(intent);
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

        if(DeviceUtils.isPhone && DeviceUtils.isLandscape()){
            columns = 2;
        }else if(!DeviceUtils.isPhone){
            columns = 3;
        }

        return columns;
    }
}
