package com.lineargs.watchnext.workers;

import android.content.ContentValues;
import android.content.Context;
import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.lineargs.watchnext.BuildConfig;
import com.lineargs.watchnext.data.DataContract;
import com.lineargs.watchnext.utils.dbutils.SerieDbUtils;
import com.lineargs.watchnext.utils.retrofit.series.SeriesApiService;
import com.lineargs.watchnext.utils.retrofit.series.seasondetails.SeasonDetails;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SeasonWorker extends Worker {

    public static final String ARG_ID = "id";
    public static final String ARG_SEASON_NUMBER = "season_number";
    public static final String ARG_SEASON_ID = "season_id";

    private static final String BASE_URL = "https://api.themoviedb.org/3/";

    private static final Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    public SeasonWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        String id = getInputData().getString(ARG_ID); // Series ID
        int seasonNumber = getInputData().getInt(ARG_SEASON_NUMBER, -1);
        String seasonId = getInputData().getString(ARG_SEASON_ID); // Database Season ID

        if (id == null || seasonNumber == -1 || seasonId == null) {
            return Result.failure();
        }

        SeriesApiService service = retrofit.create(SeriesApiService.class);
        Call<SeasonDetails> call = service.getSeason(id, seasonNumber, BuildConfig.MOVIE_DATABASE_API_KEY);

        try {
            Response<SeasonDetails> response = call.execute();
            if (response.isSuccessful() && response.body() != null) {
                ContentValues[] values = SerieDbUtils.getEpisodes(response.body().getEpisodes(), seasonId);
                if (values != null && values.length > 0) {
                    getApplicationContext().getContentResolver().bulkInsert(DataContract.Episodes.CONTENT_URI, values);
                }
            }
            return Result.success();
        } catch (IOException e) {
            e.printStackTrace();
            return Result.retry();
        }
    }
}
