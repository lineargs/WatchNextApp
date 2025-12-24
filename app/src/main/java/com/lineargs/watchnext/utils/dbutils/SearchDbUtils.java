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
    private static final String IMAGE_SMALL_BASE = "https://image.tmdb.org/t/p/w500/";

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

    public static ContentValues[] getMultiSearchContentValues(List<com.lineargs.watchnext.utils.retrofit.search.MultiSearchResult> results) {
        // Filter out people or other media types if necessary, though list generic type is bound.
        // We will do filtering in the Worker or here. Let's do it here by checking list size and only adding valid cursors,
        // but array size must match. Actually, easier to filter in Worker before calling this, OR
        // just handle list iteration and dynamic resizing.
        // For now, let's assume the list passed here is already filtered or we handle nulls.
        // Better: Use a List<ContentValues> and convert to array.
        
        java.util.List<ContentValues> valuesList = new java.util.ArrayList<>();
        
        for (com.lineargs.watchnext.utils.retrofit.search.MultiSearchResult result : results) {
            if (result.getMediaType() != null && (result.getMediaType().equals("movie") || result.getMediaType().equals("tv"))) {
                ContentValues contentValues = new ContentValues();
                contentValues.put(DataContract.Search.COLUMN_MOVIE_ID, result.getId());
                contentValues.put(DataContract.Search.COLUMN_TITLE, result.getTitle());
                contentValues.put(DataContract.Search.COLUMN_POSTER_PATH, IMAGE_SMALL_BASE + result.getPosterPath());
                // Set type: 0 for Movie, 1 for TV
                int type = result.getMediaType().equals("movie") ? 0 : 1;
                contentValues.put(DataContract.Search.COLUMN_TYPE, type);
                valuesList.add(contentValues);
            }
        }
        
        return valuesList.toArray(new ContentValues[0]);
    }
}
