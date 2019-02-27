package com.example.android.bakingapp.network;

import com.example.android.bakingapp.model.Recipe;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface GetDataService {
    @GET("baking.json/")
    Call<List<Recipe>> getAllRecipes();
}
