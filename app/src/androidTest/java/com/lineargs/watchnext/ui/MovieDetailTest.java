package com.lineargs.watchnext.ui;

import android.content.Intent;
import android.net.Uri;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.espresso.intent.rule.IntentsTestRule;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.runner.AndroidJUnit4;

import com.lineargs.watchnext.R;
import com.lineargs.watchnext.data.DataContract;
import com.lineargs.watchnext.ui.movies.MovieDetailsActivity;

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
