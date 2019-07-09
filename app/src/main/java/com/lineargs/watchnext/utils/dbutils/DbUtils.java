package com.lineargs.watchnext.utils.dbutils;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.lineargs.watchnext.data.DataContract;

/**
 * Created by goranminov on 08/11/2017.
 * <p>
 * Helper class for our Favorites OnClick
 */

public class DbUtils {

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

    /**
     * Used to check whether that particular MovieID contains inside the table. If contains that means that we have
     * all the information sync from the MovieDb API and we can enable the button
     *
     * @param context The app context
     * @param id      MovieID used to check in the db
     * @return true / false
     */
    public static boolean checkForId(Context context, String id, Uri uri) {
        Cursor cursor = context.getContentResolver().query(uri,
                null,
                //Does not matter from where we will take the MovieId column as it is
                //declared everywhere the same
                DataContract.Review.COLUMN_MOVIE_ID + " = ? ",
                new String[]{id},
                null);
        if (cursor == null) {
            return false;
        }
        boolean contains = cursor.getCount() > 0;
        cursor.close();
        return contains;
    }
}
