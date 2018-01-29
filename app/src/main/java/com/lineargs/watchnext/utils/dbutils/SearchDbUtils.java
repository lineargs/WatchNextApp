package com.lineargs.watchnext.utils.dbutils;

import android.content.ContentValues;

import com.lineargs.watchnext.data.DataContract;
import com.lineargs.watchnext.utils.retrofit.movies.Result;
import com.lineargs.watchnext.utils.retrofit.series.SeriesResult;

import java.util.List;

/**
 * Created by goranminov on 11/11/2017.
 * <p>
 * Utilities Helper class used for the Search API response
 */

public class SearchDbUtils {

    /* Static variable for our poster path*/
    private static final String IMAGE_SMALL_BASE = "http://image.tmdb.org/t/p/w500/";

    /**
     * Builds ContentValues[] used for our ContentResolver
     *
     * @param results List used to get the values from our API response
     * @return The {@link ContentValues}
     */
    public static ContentValues[] getMovieContentValues(List<Result> results) {
        int i = 0;
        ContentValues[] values = new ContentValues[results.size()];
        for (Result result : results) {
            ContentValues contentValues = new ContentValues();

            contentValues.put(DataContract.Search.COLUMN_MOVIE_ID, result.getId());
            contentValues.put(DataContract.Search.COLUMN_TITLE, result.getTitle());
            contentValues.put(DataContract.Search.COLUMN_POSTER_PATH, IMAGE_SMALL_BASE + result.getPosterPath());

            values[i] = contentValues;
            i++;
        }

        return values;
    }

    /**
     * Builds ContentValues[] used for our ContentResolver
     *
     * @param results List used to get the values from our API response
     * @return The {@link ContentValues}
     */
    public static ContentValues[] getTVContentValues(List<SeriesResult> results) {
        int i = 0;
        ContentValues[] values = new ContentValues[results.size()];
        for (SeriesResult result : results) {
            ContentValues contentValues = new ContentValues();

            contentValues.put(DataContract.Search.COLUMN_MOVIE_ID, result.getId());
            contentValues.put(DataContract.Search.COLUMN_TITLE, result.getName());
            contentValues.put(DataContract.Search.COLUMN_POSTER_PATH, IMAGE_SMALL_BASE + result.getPosterPath());

            values[i] = contentValues;
            i++;
        }

        return values;
    }
}
