package com.lineargs.watchnext.utils;

import android.content.Context;
import android.database.Cursor;

import com.lineargs.watchnext.data.DataContract;

public class StatsUtils {

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
