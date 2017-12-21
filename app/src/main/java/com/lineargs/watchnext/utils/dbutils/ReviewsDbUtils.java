package com.lineargs.watchnext.utils.dbutils;

import android.content.ContentValues;

import com.lineargs.watchnext.data.DataContract;
import com.lineargs.watchnext.utils.retrofit.reviews.Reviews;
import com.lineargs.watchnext.utils.retrofit.reviews.ReviewsResult;

import java.util.List;

/**
 * Created by goranminov on 22/11/2017.
 * <p>
 * Utilities Helper class used for the Reviews API response
 */

public class ReviewsDbUtils {

    /**
     * Builds ContentValues[] used for our ContentResolver
     *
     * @param results List used to get the values from our API response
     * @param id      The movie / serie ID
     * @return The {@link ContentValues}
     */
    public static ContentValues[] getReviewsContentValues(List<ReviewsResult> results, String id) {
        int i = 0;
        ContentValues[] values = new ContentValues[results.size()];
        for (ReviewsResult result : results) {
            ContentValues contentValues = new ContentValues();

            contentValues.put(DataContract.Review.COLUMN_MOVIE_ID, id);
            contentValues.put(DataContract.Review.COLUMN_REVIEW_ID, result.getId());
            contentValues.put(DataContract.Review.COLUMN_AUTHOR, result.getAuthor());
            contentValues.put(DataContract.Review.COLUMN_CONTENT, result.getContent());
            contentValues.put(DataContract.Review.COLUMN_URL, result.getUrl());

            values[i] = contentValues;
            i++;
        }

        return values;
    }

    /**
     * Builds ContentValues[] used for our ContentResolver
     *
     * @param reviews List used to get the values from our API response
     * @param id      The movie / serie ID
     * @return The {@link ContentValues}
     */
    public static ContentValues[] getReviewsContentValues(Reviews reviews, String id) {
        int i = 0;
        List<ReviewsResult> results = reviews.getResults();
        ContentValues[] values = new ContentValues[results.size()];
        for (ReviewsResult result : results) {
            ContentValues contentValues = new ContentValues();

            contentValues.put(DataContract.Review.COLUMN_MOVIE_ID, id);
            contentValues.put(DataContract.Review.COLUMN_REVIEW_ID, result.getId());
            contentValues.put(DataContract.Review.COLUMN_AUTHOR, result.getAuthor());
            contentValues.put(DataContract.Review.COLUMN_CONTENT, result.getContent());
            contentValues.put(DataContract.Review.COLUMN_URL, result.getUrl());

            values[i] = contentValues;
            i++;
        }

        return values;
    }
}
