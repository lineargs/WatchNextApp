package com.lineargs.watchnext.utils.dbutils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.net.Uri;
import android.util.Log;

import com.lineargs.watchnext.data.DataContract;
import com.lineargs.watchnext.data.Query;
import com.lineargs.watchnext.utils.Utils;

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
        if (cursor != null) {
            if (cursor.moveToFirst()) {
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
        contentValues.put(DataContract.PopularMovieEntry.COLUMN_IMDB_ID, cursor.getString(Query.IMDB_ID));
        contentValues.put(DataContract.PopularMovieEntry.COLUMN_HOMEPAGE, cursor.getString(Query.HOMEPAGE));
        contentValues.put(DataContract.PopularMovieEntry.COLUMN_PRODUCTION_COMPANIES, cursor.getString(Query.PRODUCTION_COMPANIES));
        contentValues.put(DataContract.PopularMovieEntry.COLUMN_PRODUCTION_COUNTRIES, cursor.getString(Query.PRODUCTION_COUNTRIES));
        contentValues.put(DataContract.PopularMovieEntry.COLUMN_STATUS, cursor.getString(Query.STATUS));
        contentValues.put(DataContract.PopularMovieEntry.COLUMN_GENRES, cursor.getString(Query.GENRES));
        contentValues.put(DataContract.PopularMovieEntry.COLUMN_RUNTIME, cursor.getInt(Query.RUNTIME));
        movieContentValues[0] = contentValues;
                context.getContentResolver().bulkInsert(DataContract.Favorites.CONTENT_URI,
                        movieContentValues);
            }
            cursor.close();
        }
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
        if (cursor != null) {
            if (cursor.moveToFirst()) {
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
        contentValues.put(DataContract.PopularMovieEntry.COLUMN_STATUS, cursor.getString(Query.STATUS));
        contentValues.put(DataContract.PopularMovieEntry.COLUMN_PRODUCTION_COMPANIES, cursor.getString(Query.PRODUCTION_COMPANIES));
        contentValues.put(DataContract.PopularMovieEntry.COLUMN_GENRES, cursor.getString(Query.GENRES));
        movieContentValues[0] = contentValues;
                context.getContentResolver().bulkInsert(DataContract.Favorites.CONTENT_URI,
                        movieContentValues);
            }
            cursor.close();
        }
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

    /**
     * Used to check whether the Movie in the table contains an IMDB ID. If contains that means that we have
     * all the informations sync from the MovieDb API and we do not need to make a call so we can update the
     * Movie information
     * @param context The app context
     * @param uri URI used to check in the db
     * @return true / false
     */
    public static boolean checkForExtras(Context context, Uri uri) {
        String id = uri.getLastPathSegment();
        uri = Utils.getBaseUri(uri);
        Cursor cursor = context.getContentResolver().query(uri,
                null,
                DataContract.PopularMovieEntry.COLUMN_MOVIE_ID + " = ? AND " + DataContract.PopularMovieEntry.COLUMN_IMDB_ID + " = ? ",
                new String[] {id, "0"},
                null);
        if (cursor == null) {
            return false;
        }
        boolean contains = cursor.getCount() > 0;
        cursor.close();
        return contains;
    }

    /**
     * Used to check whether that particular MovieID contains inside the table. If contains that means that we have
     * all the information sync from the MovieDb API and we can enable the button
     * @param context The app context
     * @param id MovieID used to check in the db
     * @return true / false
     */
    public static boolean checkForId(Context context, String id, Uri uri) {
        Cursor cursor = context.getContentResolver().query(uri,
                null,
                //Does not matter from where we will take the MovieId column as it is
                //declared everywhere the same
                DataContract.Review.COLUMN_MOVIE_ID + " = ? ",
                new String[] {id},
                null);
        if (cursor == null) {
            return false;
        }
        boolean contains = cursor.getCount() > 0;
        cursor.close();
        return contains;
    }

    /**
     * Checks whether the Movie / Serie is added in the favourites db. Used so we can display the proper
     * Drawable. Whether is star or borderStar image.
     * @param context The app context
     * @param id The Movie / Serie ID
     * @return true / false
     */
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

    public static boolean isSubscribed(Context context, long id) {
        Uri uri = DataContract.Favorites.buildFavoritesUriWithId(id);
        Cursor cursor = context.getContentResolver().query(uri,
                new String[]{DataContract.Favorites.COLUMN_NOTIFY},
                null,
                null,
                null);
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
}
