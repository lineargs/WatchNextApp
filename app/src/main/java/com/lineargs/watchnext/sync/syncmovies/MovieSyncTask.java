package com.lineargs.watchnext.sync.syncmovies;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;

import com.lineargs.watchnext.BuildConfig;
import com.lineargs.watchnext.data.DataContract;
import com.lineargs.watchnext.utils.dbutils.MovieDbUtils;
import com.lineargs.watchnext.utils.retrofit.movies.Movies;
import com.lineargs.watchnext.utils.retrofit.movies.MoviesAPI;
import com.lineargs.watchnext.utils.retrofit.movies.moviedetail.MovieDetail;

import java.lang.ref.WeakReference;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by goranminov on 10/11/2017.
 * <p>
 * See {@link com.lineargs.watchnext.sync.synccredits.CreditSyncTask}
 */

class MovieSyncTask {

    private static final String BASE_URL = "https://api.themoviedb.org/3/";
    private static final String APPEND_TO_RESPONSE = "videos,reviews,credits";
    private static String id;
    private static Uri mUri;

    private static final Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    private static final MoviesAPI moviesAPI = retrofit.create(MoviesAPI.class);

    static void syncMovieDetail(final Context context, final Uri uri) {
        String stringUri = uri.toString();
        stringUri = stringUri.substring(0, stringUri.lastIndexOf('/'));
        mUri = Uri.parse(stringUri);
        id = uri.getLastPathSegment();
        Call<MovieDetail> call = moviesAPI.getMovieDetail(id, BuildConfig.MOVIE_DATABASE_API_KEY, APPEND_TO_RESPONSE);
        call.enqueue(new Callback<MovieDetail>() {
            @Override
            public void onResponse(@NonNull Call<MovieDetail> call, @NonNull final Response<MovieDetail> response) {

                if (response.isSuccessful() && response.body() != null) {
                    ContentValues values = MovieDbUtils.updateMovie(response.body());
                    UpdateMovie updateMovie = new UpdateMovie(context);
                    updateMovie.execute(values);
                } else if (response.errorBody() != null) {
                    response.errorBody().close();
                }
            }

            @Override
            public void onFailure(@NonNull Call<MovieDetail> call, @NonNull Throwable t) {

            }
        });
    }

    static class UpdateMovie extends AsyncTask<ContentValues, Void, Void> {
        private final WeakReference<Context> weakReference;

        UpdateMovie(Context context) {
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
}
