package com.lineargs.watchnext.tools;

import android.content.Context;
import android.preference.PreferenceManager;

import com.lineargs.watchnext.R;

import java.text.NumberFormat;

/**
 * Created by goranminov on 01/03/2018.
 */

public class SeasonTools {

    public static String getSeasonString(Context context, int seasonNumber) {
        if (seasonNumber == 0) {
            return context.getString(R.string.season_specials);
        } else {
            return context.getString(R.string.season, seasonNumber);
        }
    }

    /* Will return the episode format according to the users preferences.
     * S1E1, S01E01, s1e1, s01e01
     */
    public static String getEpisodeFormat(Context context, int season, int episode) {
        NumberFormat numberFormat = NumberFormat.getIntegerInstance();
        numberFormat.setGroupingUsed(false);
        String format = getSeasonNumberFormat(context);
        String result = numberFormat.format(season);
        if (context.getString(R.string.pref_season_number_SO_key).equals(format)) {
            if (season < 10) {
                result = numberFormat.format(0) + result;
            }
            result = "S" + result + "E";
            if (episode != -1) {
                if (episode < 10) {
                    result += numberFormat.format(0);
                }
                result += numberFormat.format(episode);
            }
        } else if (context.getString(R.string.pref_season_number_S_key).equals(format)) {
            result = "S" + result + "E";
            result += numberFormat.format(episode);
        } else if (context.getString(R.string.pref_season_number_so_key).equals(format)) {
            if (season < 10) {
                result = numberFormat.format(0) + result;
            }
            result = "s" + result + "e";
            if (episode != -1) {
                if (episode < 10) {
                    result += numberFormat.format(0);
                }
                result += numberFormat.format(episode);
            }
        } else if (context.getString(R.string.pref_season_number_s_key).equals(format)) {
            result = "s" + result + "e";
            result += numberFormat.format(episode);
        }

        return result;
    }

    private static String getSeasonNumberFormat(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getString(context.getString(R.string.pref_season_number_key), context.getString(R.string.pref_season_number_SO_key));
    }
}
