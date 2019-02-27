package com.example.android.bakingapp.adapter;

import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.bakingapp.R;
import com.example.android.bakingapp.databinding.RecipeListItemBinding;
import com.example.android.bakingapp.model.Recipe;
import com.example.android.bakingapp.utils.ImageChooser;
import com.squareup.picasso.Picasso;

import java.util.List;

public class RecipeListAdapter extends RecyclerView.Adapter<RecipeListAdapter.RecipeListAdapterViewHolder> {

    private static String TAG = RecipeListAdapter.class.getSimpleName();

    private List<Recipe> recipes;

    //Constructor
    public RecipeListAdapter() {
        Log.d(TAG, "Adapter Created");
    }

    public class RecipeListAdapterViewHolder extends RecyclerView.ViewHolder {

        private final RecipeListItemBinding mBinding;

        public RecipeListAdapterViewHolder(RecipeListItemBinding recipeListItemBinding) {
            super(recipeListItemBinding.getRoot());
            mBinding = recipeListItemBinding;
        }
    }

    @NonNull
    @Override
    public RecipeListAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        RecipeListItemBinding binding = DataBindingUtil.inflate(
                LayoutInflater.from(viewGroup.getContext()),
                R.layout.recipe_list_item, viewGroup, false);

        RecipeListAdapterViewHolder viewHolder = new RecipeListAdapterViewHolder(binding);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeListAdapterViewHolder holder, int pos) {
        Recipe recipe = recipes.get(pos);
        Log.d(TAG, recipe.getName());
        holder.mBinding.tvRecipeName.setText(recipe.getName());
        if(TextUtils.isEmpty(recipe.getImage())){
            Picasso.get().load(ImageChooser.getImageResId(recipe.getName())).into(holder.mBinding.ivRecipeImage);
        } else {
            Picasso.get().load(recipe.getImage()).into(holder.mBinding.ivRecipeImage);
        }
    }

    @Override
    public int getItemCount() {
        if (null == recipes) return 0;
        return recipes.size();
    }

    public void setRecipes(List<Recipe> recipeList) {
        recipes = recipeList;
        notifyDataSetChanged();
    }

}
