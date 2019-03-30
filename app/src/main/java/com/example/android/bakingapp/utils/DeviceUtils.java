package com.example.android.bakingapp.utils;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.view.View;
import android.widget.FrameLayout;

import com.example.android.bakingapp.MainActivity;
import com.example.android.bakingapp.R;

public class DeviceUtils {

    private static Context context = MainActivity.context;
    private static Activity activity = MainActivity.activity;
    private static Resources res = context.getResources();

    public static boolean isPhone = res.getBoolean(R.bool.isPhone);

    public static boolean isLandscape() {
        if(res.getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
            return true;
        }else {
            return false;
        }
    }

    public static void setUpTabletLayout(boolean isDetails){
        //Check if is a tablet here
        if(!isPhone){
            FrameLayout tabContent = (FrameLayout) activity.findViewById(R.id.tab_content);
            if(isDetails){
                tabContent.setVisibility(View.VISIBLE);
            } else {
                tabContent.setVisibility(View.GONE);
            }
        }
    }
}
