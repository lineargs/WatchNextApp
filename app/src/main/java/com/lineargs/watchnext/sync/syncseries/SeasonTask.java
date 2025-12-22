package com.lineargs.watchnext.sync.syncseries;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.os.AsyncTask;
import androidx.annotation.NonNull;

import com.lineargs.watchnext.BuildConfig;
import com.lineargs.watchnext.data.DataContract;
import com.lineargs.watchnext.utils.dbutils.SerieDbUtils;
import com.lineargs.watchnext.utils.retrofit.series.SeriesApiService;
import com.lineargs.watchnext.utils.retrofit.series.seasondetails.SeasonDetails;

import java.lang.ref.WeakReference;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

class SeasonTask {

    private static final String BASE_URL = "https://api.themoviedb.org/3/";

    static void syncEpisodes(final Context context, final String id, int seasonNumber, final String seasonId) {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        SeriesApiService seriesApiService = retrofit.create(SeriesApiService.class);

        Call<SeasonDetails> call = seriesApiService.getSeason(id, seasonNumber, BuildConfig.MOVIE_DATABASE_API_KEY);

        call.enqueue(new Callback<SeasonDetails>() {
            @Override
            public void onResponse(@NonNull Call<SeasonDetails> call, @NonNull Response<SeasonDetails> response) {

                if (response.isSuccessful() && response.body() != null) {

                    ContentValues[] values = SerieDbUtils.getEpisodes(response.body().getEpisodes(), seasonId);
                    InsertEpisodes insertEpisodes = new InsertEpisodes(context);
                    insertEpisodes.execute(values);
                } else if (response.errorBody() != null) {
                    response.errorBody().close();
                }
            }

            @Override
            public void onFailure(@NonNull Call<SeasonDetails> call, @NonNull Throwable t) {
            }
        });
    }

    static class InsertEpisodes extends AsyncTask<ContentValues, Void, Void> {
        private final WeakReference<Context> weakReference;

        InsertEpisodes(Context context) {
            this.weakReference = new WeakReference<>(context);
        }

        @Override
        protected Void doInBackground(ContentValues... contentValues) {
            Context context = weakReference.get();
            if (context != null) {
                ContentResolver contentResolver = context.getContentResolver();
                if (contentValues != null && contentValues.length != 0) {
                    contentResolver.bulkInsert(DataContract.Episodes.CONTENT_URI, contentValues);
                }
            }
            return null;
        }
    }
}
