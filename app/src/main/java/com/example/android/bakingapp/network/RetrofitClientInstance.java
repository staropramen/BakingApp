package com.example.android.bakingapp.network;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClientInstance {
    //Base URL for API request
    private static final String MOVIE_DATABASE_URL_POPULAR =
            "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/baking.json";

    private static Retrofit getRetrofitInstance() {

        return new Retrofit.Builder()
                .baseUrl(MOVIE_DATABASE_URL_POPULAR)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public static GetDataService getApiService() {
        return getRetrofitInstance().create(GetDataService.class);
    }
}
