package com.lineargs.watchnext.tools;

import android.content.Context;

import com.lineargs.watchnext.R;

/**
 * Created by goranminov on 01/03/2018.
 */

public class SeasonTools {

    public static String getSeasonString (Context context, int seasonNumber) {
        if (seasonNumber == 0) {
            return context.getString(R.string.season_specials);
        } else {
            return context.getString(R.string.season, seasonNumber);
        }
    }
}
