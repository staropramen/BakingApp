package com.example.android.bakingapp.utils;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;

import com.example.android.bakingapp.MainActivity;
import com.example.android.bakingapp.R;

public class DeviceUtils {

    private static Context context = MainActivity.context;
    private static Resources res = context.getResources();

    public static boolean isPhone = res.getBoolean(R.bool.isPhone);

    public static boolean isLandscape() {
        if(res.getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
            return true;
        }else {
            return false;
        }
    }

}
