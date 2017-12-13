package com.lineargs.watchnext.sync.syncseries;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;

import com.lineargs.watchnext.BuildConfig;
import com.lineargs.watchnext.data.DataContract;
import com.lineargs.watchnext.utils.dbutils.SerieDbUtils;
import com.lineargs.watchnext.utils.retrofit.series.Series;
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
 * See {@link com.lineargs.watchnext.sync.synccredits.CreditSyncTask}
 */

class SerieSyncTask {

    private static final String BASE_URL = "https://api.themoviedb.org/3/";
    private static final String PATH_POPULAR = "popular";
    private static final String PATH_TOP_RATED = "top_rated";
    private static final String PATH_ON_THE_AIR = "on_the_air";

    private static final Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    private static final SeriesAPI seriesAPI = retrofit.create(SeriesAPI.class);

    static void syncPopularSeries(final Context context) {

        Call<Series> call = seriesAPI.getSeries(PATH_POPULAR, BuildConfig.MOVIE_DATABASE_API_KEY);

        call.enqueue(new Callback<Series>() {
            @Override
            public void onResponse(@NonNull Call<Series> call, @NonNull Response<Series> response) {
                if (response.isSuccessful() && response.body() != null) {

                    ContentValues[] values = SerieDbUtils.getPopularContentValues(response.body().getResults());
                    InsertPopularSeries insertPopularSeries = new InsertPopularSeries(context);
                    insertPopularSeries.execute(values);
                } else if (response.errorBody() != null) {
                    response.errorBody().close();
                }
            }

            @Override
            public void onFailure(@NonNull Call<Series> call, @NonNull Throwable t) {
            }
        });
    }

    static void syncTopRatedSeries(final Context context) {

        Call<Series> call = seriesAPI.getSeries(PATH_TOP_RATED, BuildConfig.MOVIE_DATABASE_API_KEY);

        call.enqueue(new Callback<Series>() {
            @Override
            public void onResponse(@NonNull Call<Series> call, @NonNull Response<Series> response) {
                if (response.isSuccessful() && response.body() != null) {

                    ContentValues[] values = SerieDbUtils.getTopContentValues(response.body().getResults());
                    InsertTopSeries insertTopSeries = new InsertTopSeries(context);
                    insertTopSeries.execute(values);
                } else if (response.errorBody() != null) {
                    response.errorBody().close();
                }
            }

            @Override
            public void onFailure(@NonNull Call<Series> call, @NonNull Throwable t) {
            }
        });
    }

    static void syncOnTheAirSeries(final Context context) {

        Call<Series> call = seriesAPI.getSeries(PATH_ON_THE_AIR, BuildConfig.MOVIE_DATABASE_API_KEY);

        call.enqueue(new Callback<Series>() {
            @Override
            public void onResponse(@NonNull Call<Series> call, @NonNull Response<Series> response) {
                if (response.isSuccessful() && response.body() != null) {

                    ContentValues[] values = SerieDbUtils.getOnTheAirContentValues(response.body().getResults());
                    InsertOnTheAirSeries insertOnTheAirSeries = new InsertOnTheAirSeries(context);
                    insertOnTheAirSeries.execute(values);
                } else if (response.errorBody() != null) {
                    response.errorBody().close();
                }
            }

            @Override
            public void onFailure(@NonNull Call<Series> call, @NonNull Throwable t) {
            }
        });
    }

    static class InsertPopularSeries extends AsyncTask<ContentValues, Void, Void> {
        private final WeakReference<Context> weakReference;

        InsertPopularSeries(Context context) {
            this.weakReference = new WeakReference<>(context);
        }

        @Override
        protected Void doInBackground(ContentValues... contentValues) {
            Context context = weakReference.get();
            if (context != null) {
                ContentResolver contentResolver = context.getContentResolver();
                if (contentValues != null && contentValues.length != 0) {
                    contentResolver.bulkInsert(DataContract.PopularSerieEntry.CONTENT_URI, contentValues);
                }
            }

            return null;
        }
    }

    static class InsertTopSeries extends AsyncTask<ContentValues, Void, Void> {
        private final WeakReference<Context> weakReference;

        InsertTopSeries(Context context) {
            this.weakReference = new WeakReference<>(context);
        }

        @Override
        protected Void doInBackground(ContentValues... contentValues) {
            Context context = weakReference.get();
            if (context != null) {
                ContentResolver contentResolver = context.getContentResolver();
                if (contentValues != null && contentValues.length != 0) {
                    contentResolver.bulkInsert(DataContract.TopRatedSerieEntry.CONTENT_URI, contentValues);
                }
            }

            return null;
        }
    }

    static class InsertOnTheAirSeries extends AsyncTask<ContentValues, Void, Void> {
        private final WeakReference<Context> weakReference;

        InsertOnTheAirSeries(Context context) {
            this.weakReference = new WeakReference<>(context);
        }

        @Override
        protected Void doInBackground(ContentValues... contentValues) {
            Context context = weakReference.get();
            if (context != null) {
                ContentResolver contentResolver = context.getContentResolver();
                if (contentValues != null && contentValues.length != 0) {
                    contentResolver.bulkInsert(DataContract.OnTheAirSerieEntry.CONTENT_URI, contentValues);
                }
            }

            return null;
        }
    }
}
