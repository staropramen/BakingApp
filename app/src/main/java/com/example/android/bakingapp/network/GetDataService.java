package com.example.android.bakingapp.network;

import com.example.android.bakingapp.model.Recipe;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface GetDataService {
    @GET
    Call<Recipe> getAllRecipes();
}
