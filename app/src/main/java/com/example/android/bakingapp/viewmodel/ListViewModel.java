package com.example.android.bakingapp.viewmodel;


import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.util.Log;

import com.example.android.bakingapp.model.Recipe;
import com.example.android.bakingapp.network.GetDataService;
import com.example.android.bakingapp.network.RetrofitClientInstance;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListViewModel extends ViewModel {

    private static String TAG = ListViewModel.class.getSimpleName();

    private MutableLiveData<List<Recipe>> recipes;

    public LiveData<List<Recipe>> getRecipes(){
        if(recipes == null){
            recipes = new MutableLiveData<>();
            loadAllRecipes();
        }
        return recipes;
    }

    private void loadAllRecipes(){
        GetDataService api = RetrofitClientInstance.getApiService();

        Call<List<Recipe>> call = api.getAllRecipes();

        call.enqueue(new Callback<List<Recipe>>() {
            @Override
            public void onResponse(Call<List<Recipe>> call, Response<List<Recipe>> response) {
                Log.d(TAG, "Successful api call");
                recipes.setValue(response.body());
            }

            @Override
            public void onFailure(Call<List<Recipe>> call, Throwable t) {

            }
        });
    }
}
