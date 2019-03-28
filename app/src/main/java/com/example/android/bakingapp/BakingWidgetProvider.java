package com.example.android.bakingapp;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.util.Log;
import android.widget.ListView;
import android.widget.RemoteViews;

import com.example.android.bakingapp.adapter.RecipeListAdapter;
import com.example.android.bakingapp.model.Recipe;
import com.example.android.bakingapp.utils.PreferenceUtils;

import service.IngredientsListService;

/**
 * Implementation of App Widget functionality.
 */
public class BakingWidgetProvider extends AppWidgetProvider {

    private static String TAG = BakingWidgetProvider.class.getSimpleName();
    private static String WIDGET_INTENT = "widget-intent";

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.baking_widget_provider);

        //Create Intent to launch the App
        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra(WIDGET_INTENT, true);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

        views.setOnClickPendingIntent(R.id.widget_tv_recipe_name, pendingIntent);

        //Get Recipe
        String recipeName;
        if (PreferenceUtils.hasSharedPrefecrences()){
            Recipe recipe = PreferenceUtils.getRecipeFromSharedPreferences();
            recipeName = recipe.getName();

            Intent serviceIntent = new Intent(context, IngredientsListService.class);
            views.setRemoteAdapter(R.id.lv_ingredients, serviceIntent);
        } else {
            recipeName = Resources.getSystem().getString(R.string.default_widget_text);
        }

        views.setTextViewText(R.id.widget_tv_recipe_name, recipeName);

        // Instruct the widget manager to update the widget
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId, R.id.lv_ingredients);
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

