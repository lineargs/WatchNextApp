package com.lineargs.watchnext.ui;

import android.content.Intent;
import android.net.Uri;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.assertion.ViewAssertions;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.runner.AndroidJUnit4;

import com.lineargs.watchnext.R;
import com.lineargs.watchnext.data.DataContract;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/*
 * Turn off the animations and transitions in your device before running the tests.
 * Most of the times they do not fail, but Espresso is complaining when they are on.
 */
@RunWith(AndroidJUnit4.class)
public class MovieDetailTest {

    private static final Uri URI = DataContract.PopularMovieEntry.buildMovieUriWithId(284053);

    @Rule
    public IntentsTestRule<MovieDetailsActivity> detailsActivityIntentsTestRule =
            new IntentsTestRule<>(MovieDetailsActivity.class, false, false);

    @Test
    public void checkMovieTitle() {
        Intent intent = new Intent();
        intent.setData(URI);
        detailsActivityIntentsTestRule.launchActivity(intent);

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Espresso.onView(ViewMatchers.withId(R.id.title)).check(ViewAssertions.matches
                (ViewMatchers.withText("Thor: Ragnarok")));
    }
}
