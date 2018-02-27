package com.lineargs.watchnext.sync.syncsearch;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import com.lineargs.watchnext.BuildConfig;
import com.lineargs.watchnext.data.DataContract;
import com.lineargs.watchnext.utils.dbutils.MovieDbUtils;
import com.lineargs.watchnext.utils.dbutils.SearchDbUtils;
import com.lineargs.watchnext.utils.dbutils.SerieDbUtils;
import com.lineargs.watchnext.utils.retrofit.movies.Movies;
import com.lineargs.watchnext.utils.retrofit.movies.Result;
import com.lineargs.watchnext.utils.retrofit.search.SearchApiService;
import com.lineargs.watchnext.utils.retrofit.series.Series;
import com.lineargs.watchnext.utils.retrofit.series.SeriesResult;

import java.lang.ref.WeakReference;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

class SearchSyncTask {

    private static final String BASE_URL = "https://api.themoviedb.org/3/";
    private static final String PATH_MOVIE = "movie";
    private static final String PATH_TV = "tv";
    private static final Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    private static final SearchApiService SEARCH_API_SERVICE = retrofit.create(SearchApiService.class);

    static void syncSearchMovies(final Context context, String query, boolean adult) {


        Call<Movies> call = SEARCH_API_SERVICE.searchMovies(PATH_MOVIE, BuildConfig.MOVIE_DATABASE_API_KEY, query, adult);

        call.enqueue(new Callback<Movies>() {
            @Override
            public void onResponse(@NonNull Call<Movies> call, @NonNull Response<Movies> response) {

                if (response.isSuccessful() && response.body() != null) {
                    ContentValues[] values = SearchDbUtils.getMovieContentValues(response.body().getResults());
                    SearchMovies searchMovies = new SearchMovies(context);
                    searchMovies.execute(values);
                } else if (response.errorBody() != null) {
                    response.errorBody().close();
                }
            }

            @Override
            public void onFailure(@NonNull Call<Movies> call, @NonNull Throwable t) {
            }
        });
    }

    static void syncSearchTV(final Context context, String query, boolean adult) {


        Call<Series> call = SEARCH_API_SERVICE.searchTV(PATH_TV, BuildConfig.MOVIE_DATABASE_API_KEY, query, adult);

        call.enqueue(new Callback<Series>() {
            @Override
            public void onResponse(@NonNull Call<Series> call, @NonNull Response<Series> response) {

                if (response.isSuccessful() && response.body() != null) {
                    ContentValues[] values = SearchDbUtils.getTVContentValues(response.body().getResults());
                    SearchTVs searchTV = new SearchTVs(context);
                    searchTV.execute(values);
                } else if (response.errorBody() != null) {
                    response.errorBody().close();
                }
            }

            @Override
            public void onFailure(@NonNull Call<Series> call, @NonNull Throwable t) {
            }
        });
    }

    static void syncSearchMovie(final Context context, String id) {

        Call<Result> call = SEARCH_API_SERVICE.getMovie(id, BuildConfig.MOVIE_DATABASE_API_KEY);

        call.enqueue(new Callback<Result>() {
            @Override
            public void onResponse(@NonNull Call<Result> call, @NonNull Response<Result> response) {

                if (response.isSuccessful() && response.body() != null) {
                    ContentValues[] values = MovieDbUtils.getSyncMovie(response.body());
                    SearchMovie searchMovie = new SearchMovie(context);
                    searchMovie.execute(values);
                } else if (response.errorBody() != null) {
                    response.errorBody().close();
                }
            }

            @Override
            public void onFailure(@NonNull Call<Result> call, @NonNull Throwable t) {
            }
        });
    }

    static void syncSearchTV(final Context context, String id) {

        Call<SeriesResult> call = SEARCH_API_SERVICE.getTV(id, BuildConfig.MOVIE_DATABASE_API_KEY);

        call.enqueue(new Callback<SeriesResult>() {
            @Override
            public void onResponse(@NonNull Call<SeriesResult> call, @NonNull Response<SeriesResult> response) {

                if (response.isSuccessful() && response.body() != null) {
                    ContentValues[] values = SerieDbUtils.getSyncTV(response.body());
                    SearchTV searchTV = new SearchTV(context);
                    searchTV.execute(values);
                } else if (response.errorBody() != null) {
                    response.errorBody().close();
                }
            }

            @Override
            public void onFailure(@NonNull Call<SeriesResult> call, @NonNull Throwable t) {
            }
        });
    }

    static class SearchMovies extends AsyncTask<ContentValues, Void, Void> {
        private final WeakReference<Context> weakReference;

        SearchMovies(Context context) {
            this.weakReference = new WeakReference<>(context);
        }

        @Override
        protected Void doInBackground(ContentValues... contentValues) {
            Context context = weakReference.get();
            if (context != null) {
                ContentResolver contentResolver = context.getContentResolver();
                if (contentValues != null && contentValues.length != 0) {
                    contentResolver.delete(DataContract.Search.CONTENT_URI, null, null);
                    contentResolver.bulkInsert(DataContract.Search.CONTENT_URI, contentValues);
                }
            }
            return null;
        }
    }

    static class SearchTVs extends AsyncTask<ContentValues, Void, Void> {
        private final WeakReference<Context> weakReference;

        SearchTVs(Context context) {
            this.weakReference = new WeakReference<>(context);
        }

        @Override
        protected Void doInBackground(ContentValues... contentValues) {
            Context context = weakReference.get();
            if (context != null) {
                ContentResolver contentResolver = context.getContentResolver();
                if (contentValues != null && contentValues.length != 0) {
                    contentResolver.delete(DataContract.SearchTv.CONTENT_URI, null, null);
                    contentResolver.bulkInsert(DataContract.SearchTv.CONTENT_URI, contentValues);
                }
            }
            return null;
        }
    }

    static class SearchMovie extends AsyncTask<ContentValues, Void, Void> {
        private final WeakReference<Context> weakReference;

        SearchMovie(Context context) {
            this.weakReference = new WeakReference<>(context);
        }

        @Override
        protected Void doInBackground(ContentValues... contentValues) {
            Context context = weakReference.get();
            if (context != null) {
                ContentResolver contentResolver = context.getContentResolver();
                if (contentValues != null && contentValues.length != 0) {
                    contentResolver.bulkInsert(DataContract.Favorites.CONTENT_URI, contentValues);
                }
            }
            return null;
        }
    }

    static class SearchTV extends AsyncTask<ContentValues, Void, Void> {
        private final WeakReference<Context> weakReference;

        SearchTV(Context context) {
            this.weakReference = new WeakReference<>(context);
        }

        @Override
        protected Void doInBackground(ContentValues... contentValues) {
            Context context = weakReference.get();
            if (context != null) {
                ContentResolver contentResolver = context.getContentResolver();
                if (contentValues != null && contentValues.length != 0) {
                    contentResolver.bulkInsert(DataContract.Favorites.CONTENT_URI, contentValues);
                }
            }
            return null;
        }
    }
}
