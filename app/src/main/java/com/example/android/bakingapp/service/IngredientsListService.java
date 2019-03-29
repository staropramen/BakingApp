package com.example.android.bakingapp.service;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.android.bakingapp.R;
import com.example.android.bakingapp.model.Ingredient;
import com.example.android.bakingapp.model.Recipe;
import com.example.android.bakingapp.utils.PreferenceUtils;

public class IngredientsListService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new IngredientsListViewsFactory(getApplicationContext());
    }
}

class IngredientsListViewsFactory implements RemoteViewsService.RemoteViewsFactory {

    private static String TAG = IngredientsListViewsFactory.class.getSimpleName();
    private Context context;
    private Recipe recipe;
    private String RECIPE_KEY = "recipe-key";

    public IngredientsListViewsFactory(Context applicationContext) {
        context = applicationContext;
    }

    @Override
    public void onCreate() {
        Log.d(TAG, "ON CREATE SERVICE");
    }

    @Override
    public void onDataSetChanged() {
        recipe = PreferenceUtils.getRecipeFromSharedPreferences();
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        if(recipe.getIngredients() != null){
            return recipe.getIngredients().size();
        }
        return 0;
    }

    @Override
    public RemoteViews getViewAt(int pos) {
        Log.d(TAG, "REMOTE VIEWS");
        Ingredient ingredient = recipe.getIngredients().get(pos);

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_list_item);
        String ingredientString = ingredient.getQuantity() + " " + ingredient.getMeasure() +
                " " + ingredient.getIngredient();
        views.setTextViewText(R.id.tv_widget_ingredient, ingredientString);

        Intent intent = new Intent();
        intent.putExtra(RECIPE_KEY, ingredientString);
        views.setOnClickFillInIntent(R.id.tv_widget_ingredient, intent);

        return views;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }
}