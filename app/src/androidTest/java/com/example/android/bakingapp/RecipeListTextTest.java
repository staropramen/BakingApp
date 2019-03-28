package com.example.android.bakingapp;

import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.example.android.bakingapp.fragments.RecipeListFragment;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runner.RunWith;
import org.junit.runners.model.Statement;

@RunWith(AndroidJUnit4.class)
public class RecipeListTextTest {
    private String RECIPE_NAME = "Yellow Cake";

    @Rule public ActivityTestRule<MainActivity> recipeListTestRule =
            new ActivityTestRule<>(MainActivity.class);

    @Test
    public void checkRecipeName(){
        onView(withId(R.id.rv_all_recipes)).perform(RecyclerViewActions.scrollToPosition(2));
        onView(withText(RECIPE_NAME)).check(matches(isDisplayed()));
    }
}
