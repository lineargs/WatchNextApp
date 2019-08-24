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

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/*
 * Turn off the animations and transitions in your device before running the tests.
 * Most of the times they do not fail, but Espresso is complaining when they are on.
 */
@RunWith(AndroidJUnit4.class)
public class SerieDetailTest {

    private static final Uri URI = DataContract.PopularSerieEntry.buildSerieUriWithId(1418);

    @Rule
    public IntentsTestRule<SeriesDetailsActivity> tvDetailsActivityIntentsTestRule =
            new IntentsTestRule<>(SeriesDetailsActivity.class, false, false);

    @Test
    public void checkSerieName() {
        Intent intent = new Intent();
        intent.setData(URI);
        tvDetailsActivityIntentsTestRule.launchActivity(intent);

        Espresso.onView(ViewMatchers.withId(R.id.title))
                .check(ViewAssertions.matches
                        (ViewMatchers.withText("The Big Bang Theory")));
    }
}
