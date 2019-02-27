package com.example.android.bakingapp.network;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClientInstance {
    //Base URL for API request
    private static final String RECIPE_BASE_URL =
            "https://d17h27t6h515a5.cloudfront.net/";

    private static Retrofit getRetrofitInstance() {

        return new Retrofit.Builder()
                .baseUrl(RECIPE_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public static GetDataService getApiService() {
        return getRetrofitInstance().create(GetDataService.class);
    }
}
