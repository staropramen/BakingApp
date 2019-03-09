package com.example.android.bakingapp.adapter;

import android.content.res.Resources;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.bakingapp.R;
import com.example.android.bakingapp.databinding.StepListItemBinding;
import com.example.android.bakingapp.model.Step;

import java.util.List;

public class StepListAdapter extends RecyclerView.Adapter<StepListAdapter.StepListAdapterViewHolder> {

    private List<Step> steps;

    private final StepOnClickHandler stepOnClickHandler;

    public interface StepOnClickHandler {
        void onClick(List<Step> steps, int position);
    }

    //Constructor
    public StepListAdapter(StepOnClickHandler clickHandler) {
        this.stepOnClickHandler = clickHandler;
    }

    public class StepListAdapterViewHolder extends RecyclerView.ViewHolder {
        private final StepListItemBinding mBinding;
        public StepListAdapterViewHolder(StepListItemBinding stepListItemBinding) {
            super(stepListItemBinding.getRoot());
            mBinding = stepListItemBinding;
        }
    }

    @NonNull
    @Override
    public StepListAdapter.StepListAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        StepListItemBinding binding = DataBindingUtil.inflate(
                LayoutInflater.from(viewGroup.getContext()),
                R.layout.step_list_item, viewGroup, false);
        StepListAdapterViewHolder viewHolder = new StepListAdapterViewHolder(binding);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull StepListAdapter.StepListAdapterViewHolder holder, final int pos) {
        Resources res = holder.itemView.getContext().getResources();
        final Step step = steps.get(pos);
        int stepNumber = step.getStepId();
        String stepNumberString;
        if(stepNumber == 0){
            stepNumberString = res.getString(R.string.intro);
        } else {
            stepNumberString = res.getString(R.string.step) + " " + String.valueOf(stepNumber);
        }
        holder.mBinding.tvStepName.setText(stepNumberString);
        holder.mBinding.tvStepDescription.setText(step.getShortDescription());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    stepOnClickHandler.onClick(steps, pos);
                }catch (ClassCastException e){
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        if (null == steps) return 0;
        return steps.size();
    }

    public void setSteps(List<Step> stepList) {
        steps = stepList;
        notifyDataSetChanged();
    }
}
