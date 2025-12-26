package com.lineargs.watchnext.utils.dbutils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.lineargs.watchnext.data.DataContract;
import com.lineargs.watchnext.utils.Utils;

/**
 * Created by goranminov on 08/11/2017.
 */

public class DbUtils {

    private static final String[] PROJECTION = {
            DataContract.PopularMovieEntry.COLUMN_MOVIE_ID,
            DataContract.PopularMovieEntry.COLUMN_BACKDROP_PATH,
            DataContract.PopularMovieEntry.COLUMN_ORIGINAL_TITLE,
            DataContract.PopularMovieEntry.COLUMN_POSTER_PATH,
            DataContract.PopularMovieEntry.COLUMN_RELEASE_DATE,
            DataContract.PopularMovieEntry.COLUMN_VOTE_AVERAGE,
            DataContract.PopularMovieEntry.COLUMN_OVERVIEW,
            DataContract.PopularMovieEntry.COLUMN_ORIGINAL_LANGUAGE,
            DataContract.PopularMovieEntry.COLUMN_TITLE,
            DataContract.PopularMovieEntry.COLUMN_IMDB_ID,
            DataContract.PopularMovieEntry.COLUMN_HOMEPAGE,
            DataContract.PopularMovieEntry.COLUMN_PRODUCTION_COMPANIES,
            DataContract.PopularMovieEntry.COLUMN_PRODUCTION_COUNTRIES,
            DataContract.PopularMovieEntry.COLUMN_STATUS,
            DataContract.PopularMovieEntry.COLUMN_GENRES,
            DataContract.PopularMovieEntry.COLUMN_RUNTIME
    };

    private static final int ID = 0;
    private static final int BACKDROP_PATH = 1;
    private static final int ORIGINAL_TITLE = 2;
    private static final int POSTER_PATH = 3;
    private static final int RELEASE_DATE = 4;
    private static final int VOTE_AVERAGE = 5;
    private static final int OVERVIEW = 6;
    private static final int ORIGINAL_LANGUAGE = 7;
    private static final int TITLE = 8;
    private static final int IMDB_ID = 9;
    private static final int HOMEPAGE = 10;
    private static final int PRODUCTION_COMPANIES = 11;
    private static final int PRODUCTION_COUNTRIES = 12;
    private static final int STATUS = 13;
    private static final int GENRES = 14;
    private static final int RUNTIME = 15;

    public static void addMovieToFavorites(Context context, Uri uri) {
        Cursor cursor = context.getContentResolver().query(uri,
                PROJECTION,
                null,
                null,
                null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                ContentValues[] movieContentValues = new ContentValues[1];
                ContentValues contentValues = new ContentValues();
                contentValues.put(DataContract.PopularMovieEntry.COLUMN_MOVIE_ID, cursor.getInt(ID));
                contentValues.put(DataContract.PopularMovieEntry.COLUMN_BACKDROP_PATH, cursor.getString(BACKDROP_PATH));
                contentValues.put(DataContract.PopularMovieEntry.COLUMN_ORIGINAL_TITLE, cursor.getString(ORIGINAL_TITLE));
                contentValues.put(DataContract.PopularMovieEntry.COLUMN_POSTER_PATH, cursor.getString(POSTER_PATH));
                contentValues.put(DataContract.PopularMovieEntry.COLUMN_RELEASE_DATE, cursor.getString(RELEASE_DATE));
                contentValues.put(DataContract.PopularMovieEntry.COLUMN_VOTE_AVERAGE, cursor.getString(VOTE_AVERAGE));
                contentValues.put(DataContract.PopularMovieEntry.COLUMN_OVERVIEW, cursor.getString(OVERVIEW));
                contentValues.put(DataContract.PopularMovieEntry.COLUMN_ORIGINAL_LANGUAGE, cursor.getString(ORIGINAL_LANGUAGE));
                contentValues.put(DataContract.PopularMovieEntry.COLUMN_TITLE, cursor.getString(TITLE));
                contentValues.put(DataContract.PopularMovieEntry.COLUMN_IMDB_ID, cursor.getString(IMDB_ID));
                contentValues.put(DataContract.PopularMovieEntry.COLUMN_HOMEPAGE, cursor.getString(HOMEPAGE));
                contentValues.put(DataContract.PopularMovieEntry.COLUMN_PRODUCTION_COMPANIES, cursor.getString(PRODUCTION_COMPANIES));
                contentValues.put(DataContract.PopularMovieEntry.COLUMN_PRODUCTION_COUNTRIES, cursor.getString(PRODUCTION_COUNTRIES));
                contentValues.put(DataContract.PopularMovieEntry.COLUMN_STATUS, cursor.getString(STATUS));
                contentValues.put(DataContract.PopularMovieEntry.COLUMN_GENRES, cursor.getString(GENRES));
                contentValues.put(DataContract.PopularMovieEntry.COLUMN_RUNTIME, cursor.getInt(RUNTIME));
                movieContentValues[0] = contentValues;
                context.getContentResolver().bulkInsert(DataContract.Favorites.CONTENT_URI,
                        movieContentValues);
            }
            cursor.close();
        }
    }

    public static void addTVToFavorites(Context context, Uri uri) {
        Cursor cursor = context.getContentResolver().query(uri,
                PROJECTION,
                null,
                null,
                null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                ContentValues[] movieContentValues = new ContentValues[1];
                ContentValues contentValues = new ContentValues();
                contentValues.put(DataContract.PopularMovieEntry.COLUMN_MOVIE_ID, cursor.getInt(ID));
                contentValues.put(DataContract.PopularMovieEntry.COLUMN_BACKDROP_PATH, cursor.getString(BACKDROP_PATH));
                contentValues.put(DataContract.PopularMovieEntry.COLUMN_ORIGINAL_TITLE, cursor.getString(ORIGINAL_TITLE));
                contentValues.put(DataContract.PopularMovieEntry.COLUMN_POSTER_PATH, cursor.getString(POSTER_PATH));
                contentValues.put(DataContract.PopularMovieEntry.COLUMN_RELEASE_DATE, cursor.getString(RELEASE_DATE));
                contentValues.put(DataContract.Favorites.COLUMN_TYPE, 1);
                contentValues.put(DataContract.PopularMovieEntry.COLUMN_VOTE_AVERAGE, cursor.getString(VOTE_AVERAGE));
                contentValues.put(DataContract.PopularMovieEntry.COLUMN_OVERVIEW, cursor.getString(OVERVIEW));
                contentValues.put(DataContract.PopularMovieEntry.COLUMN_ORIGINAL_LANGUAGE, cursor.getString(ORIGINAL_LANGUAGE));
                contentValues.put(DataContract.PopularMovieEntry.COLUMN_TITLE, cursor.getString(TITLE));
                contentValues.put(DataContract.PopularMovieEntry.COLUMN_STATUS, cursor.getString(STATUS));
                contentValues.put(DataContract.PopularMovieEntry.COLUMN_PRODUCTION_COMPANIES, cursor.getString(PRODUCTION_COMPANIES));
                contentValues.put(DataContract.PopularMovieEntry.COLUMN_GENRES, cursor.getString(GENRES));
                movieContentValues[0] = contentValues;
                context.getContentResolver().bulkInsert(DataContract.Favorites.CONTENT_URI,
                        movieContentValues);
            }
            cursor.close();
        }
    }

    public static void removeFromFavorites(Context context, Uri uri) {
        String idMovie = uri.getLastPathSegment();
        String[] selectionArgs = new String[]{idMovie};
        context.getContentResolver().delete(DataContract.Favorites.buildFavoritesUriWithId(Long.parseLong(uri.getLastPathSegment())),
                null,
                selectionArgs);
    }

    public static void updateSubscription(Context context, long id, int notify) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DataContract.Favorites.COLUMN_NOTIFY, notify);
        context.getContentResolver().update(DataContract.Favorites.buildFavoritesUriWithId(id),
                contentValues, null, null);
        if (notify == 0) {
            com.lineargs.watchnext.data.WatchNextDatabase.databaseWriteExecutor.execute(() -> {
                com.lineargs.watchnext.data.WatchNextDatabase.getDatabase(context).upcomingEpisodesDao().deleteBySeriesId((int) id);
            });
        }
    }

    /* Helper methods for sync logic */
    public static boolean checkForCredits(Context context, String id) {
        Uri uri = DataContract.Credits.buildCastUriWithId(Long.parseLong(id));
        Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);
        if (cursor == null) return false;
        boolean contains = cursor.getCount() > 0;
        cursor.close();
        return contains;
    }

    public static boolean checkForExtras(Context context, Uri uri) {
        String id = uri.getLastPathSegment();
        uri = Utils.getBaseUri(uri);
        Cursor cursor = context.getContentResolver().query(uri, null,
                DataContract.PopularMovieEntry.COLUMN_MOVIE_ID + " = ? AND " + DataContract.PopularMovieEntry.COLUMN_IMDB_ID + " = ? ",
                new String[] {id, "0"}, null);
        if (cursor == null) return false;
        boolean contains = cursor.getCount() > 0;
        cursor.close();
        return contains;
    }

    public static boolean checkForId(Context context, String id, Uri uri) {
        Cursor cursor = context.getContentResolver().query(uri, null,
                DataContract.Review.COLUMN_MOVIE_ID + " = ? ", new String[] {id}, null);
        if (cursor == null) return false;
        boolean contains = cursor.getCount() > 0;
        cursor.close();
        return contains;
    }

    public static boolean isFavorite(Context context, long id) {
        Uri uri = DataContract.Favorites.buildFavoritesUriWithId(id);
        Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);
        if (cursor == null) return false;
        boolean favorite = cursor.getCount() > 0;
        cursor.close();
        return favorite;
    }

    public static boolean isSubscribed(Context context, long id) {
        Uri uri = DataContract.Favorites.buildFavoritesUriWithId(id);
        Cursor cursor = context.getContentResolver().query(uri, new String[]{DataContract.Favorites.COLUMN_NOTIFY}, null, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                boolean subscribed = cursor.getInt(0) == 1;
                cursor.close();
                return subscribed;
            }
            cursor.close();
        }
        return false;
    }
}
