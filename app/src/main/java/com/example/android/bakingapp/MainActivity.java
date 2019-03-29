package com.example.android.bakingapp;

import android.app.Activity;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.databinding.DataBindingUtil;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.example.android.bakingapp.adapter.RecipeListAdapter;
import com.example.android.bakingapp.databinding.ActivityMainBinding;
import com.example.android.bakingapp.fragments.DetailsFragment;
import com.example.android.bakingapp.fragments.RecipeListFragment;
import com.example.android.bakingapp.fragments.StepFragment;
import com.example.android.bakingapp.model.Recipe;
import com.example.android.bakingapp.utils.DeviceUtils;
import com.example.android.bakingapp.utils.PreferenceUtils;
import com.example.android.bakingapp.viewmodel.ListViewModel;

import java.util.List;

public class MainActivity extends AppCompatActivity implements FragmentManager.OnBackStackChangedListener{

    private static String TAG = MainActivity.class.getSimpleName();
    ActivityMainBinding mBinding;
    public static Context context;
    public static Activity activity;
    public static AppBarLayout appBar;
    public static SharedPreferences preferences;

    private String WIDGET_INTENT = "widget-intent";
    private String RECIPE_KEY = "recipe-key";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //If in StepFragment, Phone and Landscape we want full screen and no status bar for video
        Fragment f = getSupportFragmentManager().findFragmentById(R.id.main_content);
        if(f instanceof StepFragment){
            if(DeviceUtils.isPhone && DeviceUtils.isLandscape()){
                requestWindowFeature(Window.FEATURE_NO_TITLE);
                getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
            }
        }

        setContentView(R.layout.activity_main);

        //Setup Context, Preferences and Activity to use in non activiy class
        context = this;
        activity = this;
        preferences = getSharedPreferences(getPackageName() + "_preferences", MODE_PRIVATE);

        //Setup Data Binding
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        //Setup the Toolbar
        setSupportActionBar(mBinding.toolbar);
        setAppBarHeight();

        //App Bar
        appBar = mBinding.appbar;

        //Listen for changes in the back stack
        getSupportFragmentManager().addOnBackStackChangedListener(this);
        shouldDisplayHomeUp();

        //Handle when activity is recreated like on orientation Change
        if(savedInstanceState == null){
            RecipeListFragment fragment = new RecipeListFragment();

            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .add(R.id.main_content, fragment)
                    .commit();
        }

        //Check for Intent coming from Widget
        checkForWidgetIntent();
    }

    //Fragment Navigation
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackStackChanged() {
        shouldDisplayHomeUp();
    }

    public void shouldDisplayHomeUp(){
        //Enable Up button only  if there are entries in the back stack
        boolean canGoBack = getSupportFragmentManager().getBackStackEntryCount()>0;
        getSupportActionBar().setDisplayHomeAsUpEnabled(canGoBack);
    }

    @Override
    public boolean onNavigateUp() {
        getSupportFragmentManager().popBackStack();
        return true;
    }

    @Override
    public void onBackPressed(){
        getSupportFragmentManager().popBackStack();
    }

    //Toolbar Sizing
    private void setAppBarHeight() {
        mBinding.appbar.setLayoutParams(new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, getStatusBarHeight() + dpToPx(56)));
    }

    private int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    private int dpToPx(int dp) {
        float density = getResources()
                .getDisplayMetrics()
                .density;
        return Math.round((float) dp * density);
    }

    private void checkForWidgetIntent() {
        Intent i = getIntent();

        if(i != null && i.hasExtra(WIDGET_INTENT)){
            //If Intent comes from Widget,
            //make Fragment Transaction to come to Details
            Recipe recipe = PreferenceUtils.getRecipeFromSharedPreferences();
            Bundle data = new Bundle();
            data.putSerializable(RECIPE_KEY, recipe);
            DetailsFragment fragment = new DetailsFragment();
            fragment.setArguments(data);
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.main_content, fragment);
            transaction.addToBackStack(null);
            transaction.commit();
        }
    }
}
