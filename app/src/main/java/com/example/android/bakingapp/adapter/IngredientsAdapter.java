package com.example.android.bakingapp.adapter;

import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.example.android.bakingapp.R;
import com.example.android.bakingapp.databinding.IngredientListItemBinding;
import com.example.android.bakingapp.model.Ingredient;

import java.util.List;

public class IngredientsAdapter extends RecyclerView.Adapter<IngredientsAdapter.IngredientsAdapterViewHolder> {

    private List<Ingredient> ingredients;

    public class IngredientsAdapterViewHolder extends RecyclerView.ViewHolder {

        private final IngredientListItemBinding mBinding;

        public IngredientsAdapterViewHolder(IngredientListItemBinding ingredientListItemBinding) {
            super(ingredientListItemBinding.getRoot());
            mBinding = ingredientListItemBinding;
        }
    }

    @NonNull
    @Override
    public IngredientsAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        IngredientListItemBinding binding = DataBindingUtil.inflate(
                LayoutInflater.from(viewGroup.getContext()),
                R.layout.ingredient_list_item, viewGroup, false);

        IngredientsAdapterViewHolder viewHolder = new IngredientsAdapterViewHolder(binding);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull IngredientsAdapterViewHolder holder, int pos) {
        Ingredient ingredient = ingredients.get(pos);
        String quantity = String.valueOf(ingredient.getQuantity()) + " " + ingredient.getMeasure();
        holder.mBinding.tvQuantity.setText(quantity);
        holder.mBinding.tvIngredient.setText(ingredient.getIngredient());
    }

    @Override
    public int getItemCount() {
        if (null == ingredients) return 0;
        return ingredients.size();
    }

    public void setIngredients(List<Ingredient> ingredientList) {
        ingredients = ingredientList;
        notifyDataSetChanged();
    }
}
