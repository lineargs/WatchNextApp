package com.lineargs.watchnext.sync.syncseries;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import com.lineargs.watchnext.BuildConfig;
import com.lineargs.watchnext.data.DataContract;
import com.lineargs.watchnext.utils.Utils;
import com.lineargs.watchnext.utils.dbutils.CreditDbUtils;
import com.lineargs.watchnext.utils.dbutils.SerieDbUtils;
import com.lineargs.watchnext.utils.dbutils.VideosDbUtils;
import com.lineargs.watchnext.utils.retrofit.series.SeriesApiService;
import com.lineargs.watchnext.utils.retrofit.series.seriesdetails.SeriesDetails;

import java.lang.ref.WeakReference;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

class SerieDetailTask {

    private static final String BASE_URL = "https://api.themoviedb.org/3/";
    private static final String APPEND_TO_RESPONSE = "credits,videos,similar";
    private static String id;
    private static Uri mUri;

    static void syncSeasons(final Context context, final Uri uri) {

        mUri = Utils.getBaseUri(uri);
        id = uri.getLastPathSegment();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        SeriesApiService seriesApiService = retrofit.create(SeriesApiService.class);

        Call<SeriesDetails> call = seriesApiService.getDetails(id, BuildConfig.MOVIE_DATABASE_API_KEY, APPEND_TO_RESPONSE);

        call.enqueue(new Callback<SeriesDetails>() {
            @Override
            public void onResponse(@NonNull Call<SeriesDetails> call, @NonNull Response<SeriesDetails> response) {
                if (response.isSuccessful() && response.body() != null) {

                    ContentValues updateValues = SerieDbUtils.updateSeries(response.body());
                    UpdateSeries updateSeries = new UpdateSeries(context);
                    updateSeries.execute(updateValues);
                    ContentValues[] seasonValues = SerieDbUtils.getSeasons(context, response.body(), id);
                    InsertSeasons insertSeasons = new InsertSeasons(context);
                    insertSeasons.execute(seasonValues);
                    ContentValues[] castValues = CreditDbUtils.getCastContentValues(response.body().getCredits(), id);
                    InsertCast insertCast = new InsertCast(context);
                    insertCast.execute(castValues);
                    ContentValues[] videoValues = VideosDbUtils.getVideosContentValues(response.body().getVideos(), id);
                    InsertVideos insertVideos = new InsertVideos(context);
                    insertVideos.execute(videoValues);
                } else if (response.errorBody() != null) {
                    response.errorBody().close();
                }
            }

            @Override
            public void onFailure(@NonNull Call<SeriesDetails> call, @NonNull Throwable t) {
            }
        });
    }

    static class UpdateSeries extends AsyncTask<ContentValues, Void, Void> {
        private final WeakReference<Context> weakReference;

        UpdateSeries(Context context) {
            this.weakReference = new WeakReference<>(context);
        }

        @Override
        protected Void doInBackground(ContentValues... contentValues) {
            ContentValues values = contentValues[0];
            Context context = weakReference.get();
            if (context != null) {
                ContentResolver contentResolver = context.getContentResolver();
                if (values != null) {
                    contentResolver.update(mUri, values, DataContract.PopularMovieEntry.COLUMN_MOVIE_ID + " = ?",
                            new String[]{id});
                }
            }
            return null;
        }
    }

    static class InsertSeasons extends AsyncTask<ContentValues, Void, Void> {
        private final WeakReference<Context> weakReference;

        InsertSeasons(Context context) {
            this.weakReference = new WeakReference<>(context);
        }

        @Override
        protected Void doInBackground(ContentValues... contentValues) {
            Context context = weakReference.get();
            if (context != null) {
                ContentResolver contentResolver = context.getContentResolver();
                if (contentValues != null && contentValues.length != 0) {
                    contentResolver.bulkInsert(DataContract.Seasons.CONTENT_URI, contentValues);
                }
            }
            return null;
        }
    }

    static class InsertCast extends AsyncTask<ContentValues, Void, Void> {
        private final WeakReference<Context> weakReference;

        InsertCast(Context context) {
            this.weakReference = new WeakReference<>(context);
        }

        @Override
        protected Void doInBackground(ContentValues... contentValues) {
            Context context = weakReference.get();
            if (context != null) {
                ContentResolver contentResolver = context.getContentResolver();
                if (contentValues != null && contentValues.length != 0) {
                    contentResolver.bulkInsert(DataContract.Credits.CONTENT_URI_CAST, contentValues);
                }

            }
            return null;
        }
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
}
