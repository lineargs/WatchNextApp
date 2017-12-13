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
import com.lineargs.watchnext.utils.retrofit.series.SeriesAPI;
import com.lineargs.watchnext.utils.retrofit.series.seriesdetails.SeriesDetails;

import java.lang.ref.WeakReference;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by goranminov on 27/11/2017.
 * <p>
 * See {@link com.lineargs.watchnext.sync.synccredits.CreditSyncTask}
 */

class SerieDetailTask {

    private static final String BASE_URL = "https://api.themoviedb.org/3/";

    static void syncSeasons(final Context context, final String id) {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        SeriesAPI seriesAPI = retrofit.create(SeriesAPI.class);

        Call<SeriesDetails> call = seriesAPI.getDetails(id, BuildConfig.MOVIE_DATABASE_API_KEY);

        call.enqueue(new Callback<SeriesDetails>() {
            @Override
            public void onResponse(@NonNull Call<SeriesDetails> call, @NonNull Response<SeriesDetails> response) {
                if (response.isSuccessful() && response.body() != null) {

                    ContentValues[] values = SerieDbUtils.getSeasons(response.body(), id);
                    InsertSeasons insertSeasons = new InsertSeasons(context);
                    insertSeasons.execute(values);
                } else if (response.errorBody() != null) {
                    response.errorBody().close();
                }
            }

            @Override
            public void onFailure(@NonNull Call<SeriesDetails> call, @NonNull Throwable t) {
            }
        });
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
}
