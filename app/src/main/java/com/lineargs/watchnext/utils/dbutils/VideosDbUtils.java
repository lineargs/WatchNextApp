package com.lineargs.watchnext.utils.dbutils;

import android.content.ContentValues;

import com.lineargs.watchnext.data.DataContract;
import com.lineargs.watchnext.utils.MovieUtils;
import com.lineargs.watchnext.utils.retrofit.videos.VideosResult;

import java.util.List;

/**
 * Created by goranminov on 26/11/2017.
 * <p>
 * Utilities Helper class used for the Videos API response
 */

public class VideosDbUtils {

    /**
     * Builds ContentValues[] used for our ContentResolver
     *
     * @param results List used to get the values from our API response
     * @param id      The movie / serie ID
     * @return The {@link ContentValues}
     */
    public static ContentValues[] getVideosContentValues(List<VideosResult> results, String id) {
        int i = 0;
        ContentValues[] values = new ContentValues[results.size()];
        for (VideosResult result : results) {
            ContentValues contentValues = new ContentValues();

            contentValues.put(DataContract.Videos.COLUMN_MOVIE_ID, id);
            contentValues.put(DataContract.Videos.COLUMN_VIDEO_ID, result.getId());
            contentValues.put(DataContract.Videos.COLUMN_KEY, result.getKey());
            contentValues.put(DataContract.Videos.COLUMN_NAME, result.getName());
            contentValues.put(DataContract.Videos.COLUMN_SITE, result.getSite());
            contentValues.put(DataContract.Videos.COLUMN_TYPE, result.getType());
            contentValues.put(DataContract.Videos.COLUMN_IMG, MovieUtils.getYouTubeImage(result.getKey()));

            values[i] = contentValues;
            i++;
        }

        return values;
    }
}
