package com.lineargs.watchnext.ui;

import android.app.Activity;
import android.app.Instrumentation;
import android.net.Uri;
import androidx.test.espresso.ViewInteraction;
import androidx.test.espresso.intent.Intents;
import androidx.test.espresso.intent.matcher.IntentMatchers;
import androidx.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import com.lineargs.watchnext.R;
import com.lineargs.watchnext.data.DataContract;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.hamcrest.core.IsNot;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.core.AllOf.allOf;

/*
 * Turn off the animations and transitions in your device before running the tests.
 * Most of the times they do not fail, but Espresso is complaining when they are on.
 */
@RunWith(AndroidJUnit4.class)
public class MainIntentTest {

    private static final Uri URI = DataContract.Favorites.buildFavoritesUriWithId(284053);

    @Rule
    public IntentsTestRule<MainActivity> mainActivityIntentsTestRule =
            new IntentsTestRule<>(MainActivity.class);

    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }

    @Before
    public void stubAllExternalIntents() {
        Intents.intending(IsNot.not(IntentMatchers.isInternal())).respondWith
                (new Instrumentation.ActivityResult(Activity.RESULT_OK, null));
    }

    @Test
    public void clickOnMovie() {
        ViewInteraction recyclerView = onView(
                allOf(withId(R.id.main_recycler_view),
                        childAtPosition(
                                withId(R.id.main_fragment),
                                1)));
        recyclerView.perform(actionOnItemAtPosition(0, click()));

        Intents.intended(allOf(IntentMatchers.hasData(URI)));

    }
}
