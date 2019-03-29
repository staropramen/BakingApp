package com.example.android.bakingapp.utils;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class ConnectivityUtils {

    private static ConnectivityManager connectivityManager;

    public static boolean isOnline(Context context){
        //Check if there is an internet connection
        connectivityManager = (ConnectivityManager)  context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();

        if(activeNetwork != null && activeNetwork.isConnected()){
            return true;
        }
        return false;
    }
}
