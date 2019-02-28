package com.example.android.bakingapp.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.bakingapp.R;
import com.example.android.bakingapp.model.Step;

/**
 * A simple {@link Fragment} subclass.
 */
public class StepFragment extends Fragment {

    private static String TAG = StepFragment.class.getSimpleName();
    private View rootView;
    private String STEP_KEY = "step-key";


    public StepFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_step, container, false);

        //Get the Step
        Bundle data = getArguments();
        Step step = (Step) data.getSerializable(STEP_KEY);
        Log.d(TAG,  step.getShortDescription());


        return rootView;
    }

}
