package com.lineargs.watchnext.sync.syncreviews;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;

import com.lineargs.watchnext.BuildConfig;
import com.lineargs.watchnext.data.DataContract;
import com.lineargs.watchnext.utils.dbutils.ReviewsDbUtils;
import com.lineargs.watchnext.utils.retrofit.reviews.Reviews;
import com.lineargs.watchnext.utils.retrofit.reviews.ReviewsAPI;

import java.lang.ref.WeakReference;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by goranminov on 22/11/2017.
 * <p>
 * See {@link com.lineargs.watchnext.sync.synccredits.CreditSyncTask}
 */

class ReviewSyncTask {

    private static final String BASE_URL = "https://api.themoviedb.org/3/";

    static void syncReviews(final Context context, final String id) {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ReviewsAPI moviesAPI = retrofit.create(ReviewsAPI.class);

        Call<Reviews> call = moviesAPI.getReviews(id, BuildConfig.MOVIE_DATABASE_API_KEY);

        call.enqueue(new Callback<Reviews>() {
            @Override
            public void onResponse(@NonNull Call<Reviews> call, @NonNull Response<Reviews> response) {

                if (response.isSuccessful() && response.body() != null) {
                    ContentValues[] values = ReviewsDbUtils.getReviewsContentValues(response.body().getResults(), id);
                    InsertReviews insertReviews = new InsertReviews(context);
                    insertReviews.execute(values);
                } else if (response.errorBody() != null) {
                    response.errorBody().close();
                }
            }

            @Override
            public void onFailure(@NonNull Call<Reviews> call, @NonNull Throwable t) {
            }
        });
    }

    static class InsertReviews extends AsyncTask<ContentValues, Void, Void> {
        private final WeakReference<Context> weakReference;

        InsertReviews(Context context) {
            this.weakReference = new WeakReference<>(context);
        }

        @Override
        protected Void doInBackground(ContentValues... contentValues) {
            Context context = weakReference.get();
            if (context != null) {
                ContentResolver contentResolver = context.getContentResolver();
                if (contentValues != null && contentValues.length != 0) {
                    contentResolver.bulkInsert(DataContract.Review.CONTENT_URI, contentValues);
                }
            }
            return null;
        }
    }
}
