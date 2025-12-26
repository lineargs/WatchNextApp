package com.lineargs.watchnext.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by goranminov on 31/10/2017.
 * <p>
 * Utilities class used for formatting Strings before entering them in our Database
 */

public class MovieUtils {

    /**
     * @param date The date received from the API
     * @return The date formatted using {@link SimpleDateFormat}
     * @throws ParseException If we get different date String from the API
     */
    public static String getNormalizedReleaseDate(String date) throws ParseException {
        //2017-10-31

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Date releaseDate = simpleDateFormat.parse(date);
        simpleDateFormat = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());
        date = simpleDateFormat.format(releaseDate);
        return date;
    }

    /**
     * @param vote The vote average String received from the API
     * @return The same String concatenated
     */
    public static String getNormalizedVoteAverage(String vote) {
        return vote + " / 10";
    }

    /**
     * Method used to get default video poster from YouTube
     * @param key Received from the MovieDb API
     * @return The URL used to download that image using Picasso
     */
    public static String getYouTubeImage(String key) {

        return "https://img.youtube.com/vi/" + key + "/0.jpg";
    }
}
