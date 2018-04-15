package com.lineargs.watchnext.utils.dbutils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.lineargs.watchnext.data.DataContract;
import com.lineargs.watchnext.data.Query;

/**
 * Created by goranminov on 08/11/2017.
 * <p>
 * Helper class for our Favorites OnClick
 */

public class DbUtils {

    /**
     * Adds a movie in the favorites table on a request
     *
     * @param context used to access our Cursor
     * @param uri     The Uri of the data to be added
     */
    public static void addMovieToFavorites(Context context, Uri uri) {

        Cursor cursor = context.getContentResolver().query(uri,
                Query.PROJECTION,
                null,
                null,
                null);
        cursor.moveToFirst();
        ContentValues[] movieContentValues = new ContentValues[1];
        ContentValues contentValues = new ContentValues();
        contentValues.put(DataContract.PopularMovieEntry.COLUMN_MOVIE_ID, cursor.getInt(Query.ID));
        contentValues.put(DataContract.PopularMovieEntry.COLUMN_BACKDROP_PATH, cursor.getString(Query.BACKDROP_PATH));
        contentValues.put(DataContract.PopularMovieEntry.COLUMN_ORIGINAL_TITLE, cursor.getString(Query.ORIGINAL_TITLE));
        contentValues.put(DataContract.PopularMovieEntry.COLUMN_POSTER_PATH, cursor.getString(Query.POSTER_PATH));
        contentValues.put(DataContract.PopularMovieEntry.COLUMN_RELEASE_DATE, cursor.getString(Query.RELEASE_DATE));
        contentValues.put(DataContract.PopularMovieEntry.COLUMN_VOTE_AVERAGE, cursor.getString(Query.VOTE_AVERAGE));
        contentValues.put(DataContract.PopularMovieEntry.COLUMN_OVERVIEW, cursor.getString(Query.OVERVIEW));
        contentValues.put(DataContract.PopularMovieEntry.COLUMN_ORIGINAL_LANGUAGE, cursor.getString(Query.ORIGINAL_LANGUAGE));
        contentValues.put(DataContract.PopularMovieEntry.COLUMN_TITLE, cursor.getString(Query.TITLE));
        movieContentValues[0] = contentValues;
        context.getContentResolver().bulkInsert(DataContract.Favorites.CONTENT_URI,
                movieContentValues);
        cursor.close();
    }

    /**
     * Adds a serie in the favorites table on a request
     *
     * @param context used to access our Cursor
     * @param uri     The Uri of the data to be added
     */
    public static void addTVToFavorites(Context context, Uri uri) {

        Cursor cursor = context.getContentResolver().query(uri,
                Query.PROJECTION,
                null,
                null,
                null);
        cursor.moveToFirst();
        ContentValues[] movieContentValues = new ContentValues[1];
        ContentValues contentValues = new ContentValues();
        contentValues.put(DataContract.PopularMovieEntry.COLUMN_MOVIE_ID, cursor.getInt(Query.ID));
        contentValues.put(DataContract.PopularMovieEntry.COLUMN_BACKDROP_PATH, cursor.getString(Query.BACKDROP_PATH));
        contentValues.put(DataContract.PopularMovieEntry.COLUMN_ORIGINAL_TITLE, cursor.getString(Query.ORIGINAL_TITLE));
        contentValues.put(DataContract.PopularMovieEntry.COLUMN_POSTER_PATH, cursor.getString(Query.POSTER_PATH));
        contentValues.put(DataContract.PopularMovieEntry.COLUMN_RELEASE_DATE, cursor.getString(Query.RELEASE_DATE));
        contentValues.put(DataContract.Favorites.COLUMN_TYPE, 1);
        contentValues.put(DataContract.PopularMovieEntry.COLUMN_VOTE_AVERAGE, cursor.getString(Query.VOTE_AVERAGE));
        contentValues.put(DataContract.PopularMovieEntry.COLUMN_OVERVIEW, cursor.getString(Query.OVERVIEW));
        contentValues.put(DataContract.PopularMovieEntry.COLUMN_ORIGINAL_LANGUAGE, cursor.getString(Query.ORIGINAL_LANGUAGE));
        contentValues.put(DataContract.PopularMovieEntry.COLUMN_TITLE, cursor.getString(Query.TITLE));
        movieContentValues[0] = contentValues;
        context.getContentResolver().bulkInsert(DataContract.Favorites.CONTENT_URI,
                movieContentValues);
        cursor.close();
    }

    /**
     * Removes data from the favorites table on a request
     *
     * @param context used for our ContentResolver
     * @param uri     Uri of the data to be removed
     */
    public static void removeFromFavorites(Context context, Uri uri) {
        String idMovie = uri.getLastPathSegment();
        String[] selectionArgs = new String[]{idMovie};
        context.getContentResolver().delete(DataContract.Favorites.buildFavoritesUriWithId(Long.parseLong(uri.getLastPathSegment())),
                null,
                selectionArgs);
    }

    /* Used so we can save on bandwidth and network calls */
    public static boolean checkForCredits(Context context, String id) {
        Uri uri = DataContract.Credits.buildCastUriWithId(Long.parseLong(id));
        Cursor cursor = context.getContentResolver().query(uri,
                null,
                null,
                null,
                null);
        if (cursor == null) {
            return false;
        }
        boolean contains = cursor.getCount() > 0;
        cursor.close();
        return contains;
    }

    public static boolean isFavorite(Context context, long id) {
        Uri uri = DataContract.Favorites.buildFavoritesUriWithId(id);
        Cursor cursor = context.getContentResolver().query(uri,
                null,
                null,
                null,
                null);
        if (cursor == null) {
            return false;
        }
        boolean favorite = cursor.getCount() > 0;
        cursor.close();
        return favorite;
    }
}
