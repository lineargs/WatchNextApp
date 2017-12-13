package com.lineargs.watchnext.sync.syncvideos;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;

import com.lineargs.watchnext.BuildConfig;
import com.lineargs.watchnext.data.DataContract;
import com.lineargs.watchnext.utils.dbutils.VideosDbUtils;
import com.lineargs.watchnext.utils.retrofit.videos.Videos;
import com.lineargs.watchnext.utils.retrofit.videos.VideosAPI;

import java.lang.ref.WeakReference;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by goranminov on 26/11/2017.
 * <p>
 * See {@link com.lineargs.watchnext.sync.synccredits.CreditSyncTask}
 */

class VideosSyncTask {

    private static final String BASE_URL = "https://api.themoviedb.org/3/";

    static void syncVideos(final Context context, final String id) {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        VideosAPI moviesAPI = retrofit.create(VideosAPI.class);

        Call<Videos> call = moviesAPI.getReviews(id, BuildConfig.MOVIE_DATABASE_API_KEY);

        call.enqueue(new Callback<Videos>() {
            @Override
            public void onResponse(@NonNull Call<Videos> call, @NonNull final Response<Videos> response) {

                if (response.isSuccessful() && response.body() != null) {
                    ContentValues[] values = VideosDbUtils.getVideosContentValues(response.body().getResults(), id);
                    InsertVideos insertVideos = new InsertVideos(context);
                    insertVideos.execute(values);
                } else if (response.errorBody() != null) {
                    response.errorBody().close();
                }
            }

            @Override
            public void onFailure(@NonNull Call<Videos> call, @NonNull Throwable t) {
            }
        });
    }

    static void syncTVVideos(final Context context, final String id) {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        VideosAPI moviesAPI = retrofit.create(VideosAPI.class);

        Call<Videos> call = moviesAPI.getTVReviews(id, BuildConfig.MOVIE_DATABASE_API_KEY);

        call.enqueue(new Callback<Videos>() {
            @Override
            public void onResponse(@NonNull Call<Videos> call, @NonNull Response<Videos> response) {

                if (response.isSuccessful() && response.body() != null) {
                    ContentValues[] values = VideosDbUtils.getVideosContentValues(response.body().getResults(), id);
                    InsertTVVideos insertTVVideos = new InsertTVVideos(context);
                    insertTVVideos.execute(values);
                } else if (response.errorBody() != null) {
                    response.errorBody().close();
                }
            }

            @Override
            public void onFailure(@NonNull Call<Videos> call, @NonNull Throwable t) {
            }
        });
    }

    static class InsertVideos extends AsyncTask<ContentValues, Void, Void> {
        private final WeakReference<Context> weakReference;

        InsertVideos(Context context) {
            this.weakReference = new WeakReference<>(context);
        }

        @Override
        protected Void doInBackground(ContentValues... contentValues) {
            Context context = weakReference.get();
            if (context != null) {
                ContentResolver contentResolver = context.getContentResolver();
                if (contentValues != null && contentValues.length != 0) {
                    contentResolver.bulkInsert(DataContract.Videos.CONTENT_URI, contentValues);
                }
            }
            return null;
        }
    }

    static class InsertTVVideos extends AsyncTask<ContentValues, Void, Void> {
        private final WeakReference<Context> weakReference;

        InsertTVVideos(Context context) {
            this.weakReference = new WeakReference<>(context);
        }

        @Override
        protected Void doInBackground(ContentValues... contentValues) {
            Context context = weakReference.get();
            if (context != null) {
                ContentResolver contentResolver = context.getContentResolver();
                if (contentValues != null && contentValues.length != 0) {
                    contentResolver.bulkInsert(DataContract.Videos.CONTENT_URI, contentValues);
                }
            }
            return null;
        }
    }
}
