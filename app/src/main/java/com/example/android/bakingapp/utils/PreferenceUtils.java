package com.example.android.bakingapp.utils;

import com.example.android.bakingapp.MainActivity;
import com.example.android.bakingapp.model.Recipe;
import com.google.gson.Gson;

public class PreferenceUtils {

    private static String RECIPE_KEY = "recipe-key";

    public static void saveRecipeToSharedPreferences(Recipe recipe){
        Gson gson = new Gson();
        String json = gson.toJson(recipe);
        MainActivity.preferences.edit().putString(RECIPE_KEY, json).apply();
    }

    public static Recipe getRecipeFromSharedPreferences(){
        Gson gson = new Gson();
        String json = MainActivity.preferences.getString(RECIPE_KEY, null);
        Recipe recipe = gson.fromJson(json, Recipe.class);
        return recipe;
    }

    public static boolean hasSharedPrefecrences(){
        boolean hasPrefs = MainActivity.preferences.contains(RECIPE_KEY);
        return hasPrefs;
    }
}
