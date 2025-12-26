package com.lineargs.watchnext.workers;

import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.lineargs.watchnext.BuildConfig;
import com.lineargs.watchnext.data.DataContract;
import com.lineargs.watchnext.utils.Utils;
import com.lineargs.watchnext.utils.dbutils.CreditDbUtils;
import com.lineargs.watchnext.utils.dbutils.SerieDbUtils;
import com.lineargs.watchnext.utils.dbutils.VideosDbUtils;
import com.lineargs.watchnext.utils.retrofit.series.SeriesApiService;
import com.lineargs.watchnext.utils.retrofit.series.seriesdetails.SeriesDetails;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SerieDetailWorker extends Worker {

    public static final String ARG_URI = "arg_uri";
    public static final String ARG_UPDATE_ONLY = "arg_update_only";

    private static final String BASE_URL = "https://api.themoviedb.org/3/";
    private static final String APPEND_TO_RESPONSE = "credits,videos,similar";

    private static final Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    public SerieDetailWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        String uriString = getInputData().getString(ARG_URI);
        boolean updateOnly = getInputData().getBoolean(ARG_UPDATE_ONLY, false);

        if (uriString == null) {
            return Result.failure();
        }

        Uri uri = Uri.parse(uriString);
        String id = uri.getLastPathSegment();
        SeriesApiService service = retrofit.create(SeriesApiService.class);

        try {
            if (updateOnly) {
                updateSeriesDetail(service, id, uri);
            } else {
                fetchFullSeriesDetail(service, id, uri);
            }
            return Result.success();
        } catch (IOException e) {
            e.printStackTrace();
            return Result.retry();
        }
    }

    private void fetchFullSeriesDetail(SeriesApiService service, String id, Uri uri) throws IOException {
        Call<SeriesDetails> call = service.getDetails(id, BuildConfig.MOVIE_DATABASE_API_KEY, APPEND_TO_RESPONSE);
        Response<SeriesDetails> response = call.execute();

        if (response.isSuccessful() && response.body() != null) {
            SeriesDetails series = response.body();
            Context context = getApplicationContext();

            // Update Series
            ContentValues updateValues = SerieDbUtils.updateSeries(series);
            int rows = context.getContentResolver().update(Utils.getBaseUri(uri), updateValues, DataContract.PopularMovieEntry.COLUMN_MOVIE_ID + " = ?", new String[]{id});
            if (rows == 0) {
                ContentValues[] insertValues = SerieDbUtils.getSyncSeries(series);
                if (insertValues != null && insertValues.length > 0) {
                    context.getContentResolver().insert(Utils.getBaseUri(uri), insertValues[0]);
                }
            }

            // Insert Seasons
            ContentValues[] seasonValues = SerieDbUtils.getSeasons(context, series, id);
            bulkInsert(DataContract.Seasons.CONTENT_URI, seasonValues);

            // Insert Cast
            ContentValues[] castValues = CreditDbUtils.getCastContentValues(series.getCredits(), id);
            bulkInsert(DataContract.Credits.CONTENT_URI_CAST, castValues);

            // Insert Videos
            ContentValues[] videoValues = VideosDbUtils.getVideosContentValues(series.getVideos(), id);
            bulkInsert(DataContract.Videos.CONTENT_URI, videoValues);
        }
    }

    private void updateSeriesDetail(SeriesApiService service, String id, Uri uri) throws IOException {
        Call<SeriesDetails> call = service.updateDetails(id, BuildConfig.MOVIE_DATABASE_API_KEY);
        Response<SeriesDetails> response = call.execute();

        if (response.isSuccessful() && response.body() != null) {
            ContentValues updateValues = SerieDbUtils.updateSeries(response.body());
            int rows = getApplicationContext().getContentResolver().update(Utils.getBaseUri(uri), updateValues, DataContract.PopularMovieEntry.COLUMN_MOVIE_ID + " = ?", new String[]{id});
            if (rows == 0) {
                ContentValues[] insertValues = SerieDbUtils.getSyncSeries(response.body());
                if (insertValues != null && insertValues.length > 0) {
                    getApplicationContext().getContentResolver().insert(Utils.getBaseUri(uri), insertValues[0]);
                }
            }
        }
    }

    private void bulkInsert(Uri uri, ContentValues[] values) {
        if (values != null && values.length > 0) {
            getApplicationContext().getContentResolver().bulkInsert(uri, values);
        }
    }
}
