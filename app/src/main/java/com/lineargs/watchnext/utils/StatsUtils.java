package com.lineargs.watchnext.utils;

import android.content.Context;
import android.database.Cursor;

import com.lineargs.watchnext.data.DataContract;

/**
 * Statistics helper utility class
 */
public class StatsUtils {

    /**
     * Performs a search using Cursor in favourites table
     * @param context Activity context
     * @return Movie number in favourites
     */
    public static int getMoviesCount(Context context) {
        Cursor cursor = context.getContentResolver().query(DataContract.Favorites.CONTENT_URI,
                null,
                DataContract.Favorites.COLUMN_TYPE + " = ?",
                new String[]{"0"},
                null);
        if (cursor == null) {
            return 0;
        }
        int contains = cursor.getCount();
        cursor.close();
        return contains;
    }

    /**
     * Performs a search using Cursor in favourites table
     * @param context Activity context
     * @return Series number in favourites
     */
    public static int getSeriesCount(Context context) {
        Cursor cursor = context.getContentResolver().query(DataContract.Favorites.CONTENT_URI,
                null,
                DataContract.Favorites.COLUMN_TYPE + " = ?",
                new String[]{"1"},
                null);
        if (cursor == null) {
            return 0;
        }
        int contains = cursor.getCount();
        cursor.close();
        return contains;
    }
}
