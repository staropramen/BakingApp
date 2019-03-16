package com.example.android.bakingapp.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.bakingapp.R;
import com.example.android.bakingapp.adapter.IngredientsAdapter;
import com.example.android.bakingapp.adapter.StepListAdapter;
import com.example.android.bakingapp.model.Ingredient;
import com.example.android.bakingapp.model.Recipe;
import com.example.android.bakingapp.model.Step;
import com.example.android.bakingapp.utils.DeviceUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class DetailsFragment extends Fragment implements StepListAdapter.StepOnClickHandler {

    private static String TAG = DetailsFragment.class.getSimpleName();

    private View rootView;
    private RecyclerView ingredientsRecyclerView;
    private IngredientsAdapter ingredientsAdapter;
    private RecyclerView stepsRecyclerView;
    private StepListAdapter stepAdapter;
    private String RECIPE_KEY = "recipe-key";
    private String STEP_KEY = "step-key";
    private String POSITION_KEY = "position-key";
    private Recipe recipe;

    public DetailsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_details, container, false);

        //Prepare Adapter and RecyclerView
        ingredientsRecyclerView = rootView.findViewById(R.id.rv_ingredients);
        ingredientsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        this.ingredientsAdapter = new IngredientsAdapter();
        stepsRecyclerView = rootView.findViewById(R.id.rv_steps);
        stepsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        this.stepAdapter = new StepListAdapter(this);

        //Get the Recipe
        Bundle data = getArguments();
        recipe = (Recipe) data.getSerializable(RECIPE_KEY);

        //Set the title
        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle(recipe.getName());

        //Set up the Tablet layout (Check for Tablet is inside utils function)
        DeviceUtils.setUpTabletLayout(true);

        //Pop up recycler views
        setupRecyclerViews();

        //If Device is tablet, initial show Introduction step
        if(!DeviceUtils.isPhone){
            tabletFragmentTransaction(recipe.getSteps(), 0);
        }

        return rootView;
    }

    @Override
    public void onClick(List<Step> steps, int position) {
        phoneFragmentTransaction(steps, position);
    }

    //Setup recycler views
    private void setupRecyclerViews(){
        //Get Ingredients an Pop in Recycler View
        List<Ingredient> ingredients = recipe.getIngredients();
        ingredientsAdapter.setIngredients(ingredients);
        ingredientsRecyclerView.setAdapter(ingredientsAdapter);

        //Get Steps and Pop in RecyclerView
        List<Step> steps = recipe.getSteps();
        stepAdapter.setSteps(steps);
        stepsRecyclerView.setAdapter(stepAdapter);
    }

    //Fragment Transaction for Phone
    private void phoneFragmentTransaction(List<Step> steps, int position){
        Bundle data = new Bundle();
        data.putSerializable(STEP_KEY, (Serializable)steps);
        data.putInt(POSITION_KEY, position);
        StepFragment fragment = new StepFragment();
        fragment.setArguments(data);
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.main_content, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    //Fragment Transaction for Tablet
    private void tabletFragmentTransaction(List<Step> steps, int position){
        Bundle data = new Bundle();
        data.putSerializable(STEP_KEY, (Serializable)steps);
        data.putInt(POSITION_KEY, position);
        StepFragment fragment = new StepFragment();
        fragment.setArguments(data);
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.tab_content, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
