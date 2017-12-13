package com.lineargs.watchnext.sync.syncmovies;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;

import com.lineargs.watchnext.BuildConfig;
import com.lineargs.watchnext.data.DataContract;
import com.lineargs.watchnext.utils.dbutils.MovieDbUtils;
import com.lineargs.watchnext.utils.retrofit.movies.Movies;
import com.lineargs.watchnext.utils.retrofit.movies.MoviesAPI;

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
    private static final String PATH_POPULAR = "popular";
    private static final String PATH_TOP_RATED = "top_rated";
    private static final String PATH_UPCOMING = "upcoming";
    private static final String PATH_THEATER = "now_playing";

    private static final Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    private static final MoviesAPI moviesAPI = retrofit.create(MoviesAPI.class);

    static void syncPopularMovies(final Context context) {

        Call<Movies> call = moviesAPI.getMovies(PATH_POPULAR, BuildConfig.MOVIE_DATABASE_API_KEY);
        call.enqueue(new Callback<Movies>() {
            @Override
            public void onResponse(@NonNull Call<Movies> call, @NonNull final Response<Movies> response) {

                if (response.isSuccessful() && response.body() != null) {
                    ContentValues[] values = MovieDbUtils.getPopularContentValues(response.body().getResults());
                    InsertPopularMovies insertPopularMovies = new InsertPopularMovies(context);
                    insertPopularMovies.execute(values);
                } else if (response.errorBody() != null) {
                    response.errorBody().close();
                }
            }

            @Override
            public void onFailure(@NonNull Call<Movies> call, @NonNull Throwable t) {
            }
        });
    }

    static void syncTopMovies(final Context context) {

        Call<Movies> call = moviesAPI.getMovies(PATH_TOP_RATED, BuildConfig.MOVIE_DATABASE_API_KEY);

        call.enqueue(new Callback<Movies>() {
            @Override
            public void onResponse(@NonNull Call<Movies> call, @NonNull final Response<Movies> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ContentValues[] values = MovieDbUtils.getTopContentValues(response.body().getResults());
                    InsertTopMovies insertTopMovies = new InsertTopMovies(context);
                    insertTopMovies.execute(values);
                } else if (response.errorBody() != null) {
                    response.errorBody().close();
                }
            }

            @Override
            public void onFailure(@NonNull Call<Movies> call, @NonNull Throwable t) {
            }
        });
    }

    static void syncUpcomingMovies(final Context context) {

        Call<Movies> call = moviesAPI.getMovies(PATH_UPCOMING, BuildConfig.MOVIE_DATABASE_API_KEY);

        call.enqueue(new Callback<Movies>() {
            @Override
            public void onResponse(@NonNull Call<Movies> call, @NonNull final Response<Movies> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ContentValues[] values = MovieDbUtils.getUpcomingContentValues(response.body().getResults());
                    InsertUpcomingMovies insertUpcomingMovies = new InsertUpcomingMovies(context);
                    insertUpcomingMovies.execute(values);
                } else if (response.errorBody() != null) {
                    response.errorBody().close();
                }
            }

            @Override
            public void onFailure(@NonNull Call<Movies> call, @NonNull Throwable t) {
            }
        });
    }

    static void syncTheaterMovies(final Context context) {

        Call<Movies> call = moviesAPI.getMovies(PATH_THEATER, BuildConfig.MOVIE_DATABASE_API_KEY);

        call.enqueue(new Callback<Movies>() {
            @Override
            public void onResponse(@NonNull Call<Movies> call, @NonNull final Response<Movies> response) {

                if (response.isSuccessful() && response.body() != null) {
                    Log.v("Link", String.valueOf(call.request().url()));
                    ContentValues[] values = MovieDbUtils.getTheaterContentValues(response.body().getResults());
                    InsertTheaterMovies insertTheaterMovies = new InsertTheaterMovies(context);
                    insertTheaterMovies.execute(values);
                } else if (response.errorBody() != null) {
                    response.errorBody().close();
                }
            }

            @Override
            public void onFailure(@NonNull Call<Movies> call, @NonNull Throwable t) {
            }
        });
    }

    static class InsertPopularMovies extends AsyncTask<ContentValues, Void, Void> {
        private final WeakReference<Context> weakReference;

        InsertPopularMovies(Context context) {
            this.weakReference = new WeakReference<>(context);
        }

        @Override
        protected Void doInBackground(ContentValues... contentValues) {
            Context context = weakReference.get();
            if (context != null) {
                ContentResolver contentResolver = context.getContentResolver();
                if (contentValues != null && contentValues.length != 0) {
                    contentResolver.bulkInsert(DataContract.PopularMovieEntry.CONTENT_URI, contentValues);
                }
            }
            return null;
        }
    }

    static class InsertTopMovies extends AsyncTask<ContentValues, Void, Void> {
        private final WeakReference<Context> weakReference;

        InsertTopMovies(Context context) {
            this.weakReference = new WeakReference<>(context);
        }

        @Override
        protected Void doInBackground(ContentValues... contentValues) {
            Context context = weakReference.get();
            if (context != null) {
                ContentResolver contentResolver = context.getContentResolver();
                if (contentValues != null && contentValues.length != 0) {
                    contentResolver.bulkInsert(DataContract.TopRatedMovieEntry.CONTENT_URI, contentValues);
                }
            }
            return null;
        }
    }

    static class InsertUpcomingMovies extends AsyncTask<ContentValues, Void, Void> {
        private final WeakReference<Context> weakReference;

        InsertUpcomingMovies(Context context) {
            this.weakReference = new WeakReference<>(context);
        }

        @Override
        protected Void doInBackground(ContentValues... contentValues) {
            Context context = weakReference.get();
            if (context != null) {
                ContentResolver contentResolver = context.getContentResolver();
                if (contentValues != null && contentValues.length != 0) {
                    contentResolver.bulkInsert(DataContract.UpcomingMovieEntry.CONTENT_URI, contentValues);
                }
            }
            return null;
        }
    }

    static class InsertTheaterMovies extends AsyncTask<ContentValues, Void, Void> {
        private final WeakReference<Context> weakReference;

        InsertTheaterMovies(Context context) {
            this.weakReference = new WeakReference<>(context);
        }

        @Override
        protected Void doInBackground(ContentValues... contentValues) {
            Context context = weakReference.get();
            if (context != null) {
                ContentResolver contentResolver = context.getContentResolver();
                if (contentValues != null && contentValues.length != 0) {
                    contentResolver.bulkInsert(DataContract.TheaterMovieEntry.CONTENT_URI, contentValues);
                }
            }
            return null;
        }
    }
}
