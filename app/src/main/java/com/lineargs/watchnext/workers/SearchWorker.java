package com.lineargs.watchnext.workers;

import android.content.ContentValues;
import android.content.Context;
import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.lineargs.watchnext.BuildConfig;
import com.lineargs.watchnext.data.DataContract;
import com.lineargs.watchnext.utils.dbutils.MovieDbUtils;
import com.lineargs.watchnext.utils.dbutils.SearchDbUtils;
import com.lineargs.watchnext.utils.dbutils.SerieDbUtils;
import com.lineargs.watchnext.utils.retrofit.movies.Movies;

import com.lineargs.watchnext.utils.retrofit.search.SearchApiService;
import com.lineargs.watchnext.utils.retrofit.series.Series;
import com.lineargs.watchnext.utils.retrofit.series.SeriesResult;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SearchWorker extends Worker {

    public static final String ARG_QUERY = "query";
    public static final String ARG_ADULT = "adult";
    public static final String ARG_TV_QUERY = "tv_query";
    public static final String ARG_MOVIE_ID = "movie_id";
    public static final String ARG_TV_ID = "tv_id";

    private static final String BASE_URL = "https://api.themoviedb.org/3/";
    private static final String PATH_MOVIE = "movie";
    private static final String PATH_TV = "tv";

    private static final Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    public SearchWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        String query = getInputData().getString(ARG_QUERY);
        boolean adult = getInputData().getBoolean(ARG_ADULT, false);
        String tvQuery = getInputData().getString(ARG_TV_QUERY);
        String movieId = getInputData().getString(ARG_MOVIE_ID);
        String tvId = getInputData().getString(ARG_TV_ID);

        SearchApiService service = retrofit.create(SearchApiService.class);

        try {
            if (query != null) {
                searchMovies(service, query, adult);
            }
            if (tvQuery != null) {
                searchTV(service, tvQuery, adult);
            }
            if (movieId != null) {
                getMovie(service, movieId);
            }
            if (tvId != null) {
                getTV(service, tvId);
            }
            return Result.success();
        } catch (IOException e) {
            e.printStackTrace();
            return Result.retry();
        }
    }

    private void searchMovies(SearchApiService service, String query, boolean adult) throws IOException {
        Call<Movies> call = service.searchMovies(PATH_MOVIE, BuildConfig.MOVIE_DATABASE_API_KEY, query, adult);
        Response<Movies> response = call.execute();
        if (response.isSuccessful() && response.body() != null) {
            ContentValues[] values = SearchDbUtils.getMovieContentValues(response.body().getResults());
            if (values != null && values.length > 0) {
                getApplicationContext().getContentResolver().delete(DataContract.Search.CONTENT_URI, null, null);
                getApplicationContext().getContentResolver().bulkInsert(DataContract.Search.CONTENT_URI, values);
            }
        }
    }

    private void searchTV(SearchApiService service, String query, boolean adult) throws IOException {
        Call<Series> call = service.searchTV(PATH_TV, BuildConfig.MOVIE_DATABASE_API_KEY, query, adult);
        Response<Series> response = call.execute();
        if (response.isSuccessful() && response.body() != null) {
            ContentValues[] values = SearchDbUtils.getTVContentValues(response.body().getResults());
            if (values != null && values.length > 0) {
                getApplicationContext().getContentResolver().delete(DataContract.SearchTv.CONTENT_URI, null, null);
                getApplicationContext().getContentResolver().bulkInsert(DataContract.SearchTv.CONTENT_URI, values);
            }
        }
    }

    private void getMovie(SearchApiService service, String id) throws IOException {
        Call<com.lineargs.watchnext.utils.retrofit.movies.Result> call = service.getMovie(id, BuildConfig.MOVIE_DATABASE_API_KEY);
        Response<com.lineargs.watchnext.utils.retrofit.movies.Result> response = call.execute();
        if (response.isSuccessful() && response.body() != null) {
            ContentValues[] values = MovieDbUtils.getSyncMovie(response.body());
            if (values != null && values.length > 0) {
                getApplicationContext().getContentResolver().bulkInsert(DataContract.Favorites.CONTENT_URI, values);
            }
        }
    }

    private void getTV(SearchApiService service, String id) throws IOException {
        Call<SeriesResult> call = service.getTV(id, BuildConfig.MOVIE_DATABASE_API_KEY);
        Response<SeriesResult> response = call.execute();
        if (response.isSuccessful() && response.body() != null) {
            ContentValues[] values = SerieDbUtils.getSyncTV(response.body());
            if (values != null && values.length > 0) {
                getApplicationContext().getContentResolver().bulkInsert(DataContract.Favorites.CONTENT_URI, values);
            }
        }
    }
}
