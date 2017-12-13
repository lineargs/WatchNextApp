package com.lineargs.watchnext.sync.synccredits;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;

import com.lineargs.watchnext.BuildConfig;
import com.lineargs.watchnext.data.DataContract;
import com.lineargs.watchnext.utils.dbutils.CreditDbUtils;
import com.lineargs.watchnext.utils.retrofit.credits.MovieCredits;
import com.lineargs.watchnext.utils.retrofit.credits.TVCredits;
import com.lineargs.watchnext.utils.retrofit.movies.MoviesAPI;
import com.lineargs.watchnext.utils.retrofit.series.SeriesAPI;

import java.lang.ref.WeakReference;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by goranminov on 11/11/2017.
 * <p>
 * Performs the network request, parses the JSON from that request, and
 * inserts the new data information into our ContentProvider.
 */

class CreditSyncTask {

    private static final String BASE_URL = "https://api.themoviedb.org/3/";

    /**
     * Performs the network request using {@link Retrofit} and {@link GsonConverterFactory}
     * as a converter
     *
     * @param context Used to access utility methods and the ContentResolver
     * @param id      The ID passed
     */
    static void syncMovieCredits(final Context context, final String id) {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        MoviesAPI moviesAPI = retrofit.create(MoviesAPI.class);

        /*
         * The moviesAPI will return the URL that we need to get the JSON for the
         * movies.
         */
        Call<MovieCredits> call = moviesAPI.getMovieCredits(id, BuildConfig.MOVIE_DATABASE_API_KEY);

        call.enqueue(new Callback<MovieCredits>() {
            @Override
            public void onResponse(@NonNull Call<MovieCredits> call, @NonNull Response<MovieCredits> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ContentValues[] values = CreditDbUtils.getCastContentValues(response.body().getCast(), id);
                    InsertMovieCredits insertMovieCredits = new InsertMovieCredits(context);
                    insertMovieCredits.execute(values);
                } else if (response.errorBody() != null) {
                    response.errorBody().close();
                }
            }

            @Override
            public void onFailure(@NonNull Call<MovieCredits> call, @NonNull Throwable t) {
            }
        });
    }

    /**
     * Performs the network request using {@link Retrofit} and {@link GsonConverterFactory}
     * as a converter
     *
     * @param context Used to access utility methods and the ContentResolver
     * @param id      The ID passed
     */
    synchronized static void syncTVCredits(final Context context, final String id) {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        SeriesAPI seriesAPI = retrofit.create(SeriesAPI.class);

        Call<TVCredits> call = seriesAPI.getCredits(id, BuildConfig.MOVIE_DATABASE_API_KEY);

        call.enqueue(new Callback<TVCredits>() {
            @Override
            public void onResponse(@NonNull Call<TVCredits> call, @NonNull Response<TVCredits> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ContentValues[] values = CreditDbUtils.getCastContentValues(response.body().getCast(), id);
                    InsertTVCredits insertTVCredits = new InsertTVCredits(context);
                    insertTVCredits.execute(values);
                } else if (response.errorBody() != null) {
                    response.errorBody().close();
                }
            }

            @Override
            public void onFailure(@NonNull Call<TVCredits> call, @NonNull Throwable t) {
            }
        });
    }

    static class InsertMovieCredits extends AsyncTask<ContentValues, Void, Void> {
        private final WeakReference<Context> weakReference;

        InsertMovieCredits(Context context) {
            this.weakReference = new WeakReference<>(context);
        }

        @Override
        protected Void doInBackground(ContentValues... contentValues) {
            Context context = weakReference.get();
            if (context != null) {
                ContentResolver contentResolver = context.getContentResolver();
                if (contentValues != null && contentValues.length != 0) {
                    contentResolver.bulkInsert(DataContract.CreditCast.CONTENT_URI, contentValues);
                }

            }
            return null;
        }
    }

    static class InsertTVCredits extends AsyncTask<ContentValues, Void, Void> {
        private final WeakReference<Context> weakReference;

        InsertTVCredits(Context context) {
            this.weakReference = new WeakReference<>(context);
        }

        @Override
        protected Void doInBackground(ContentValues... contentValues) {
            Context context = weakReference.get();
            if (context != null) {
                ContentResolver contentResolver = context.getContentResolver();
                if (contentValues != null && contentValues.length != 0) {
                    contentResolver.bulkInsert(DataContract.CreditCast.CONTENT_URI, contentValues);
                }
            }
            return null;
        }
    }
}
