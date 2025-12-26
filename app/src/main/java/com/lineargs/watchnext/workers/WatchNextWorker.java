package com.lineargs.watchnext.workers;

import android.content.ContentValues;
import android.content.Context;
import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.lineargs.watchnext.BuildConfig;
import com.lineargs.watchnext.data.DataContract;
import com.lineargs.watchnext.utils.NotificationUtils;
import com.lineargs.watchnext.utils.dbutils.MovieDbUtils;
import com.lineargs.watchnext.utils.dbutils.SerieDbUtils;
import com.lineargs.watchnext.utils.retrofit.movies.MovieApiService;
import com.lineargs.watchnext.utils.retrofit.movies.Movies;
import com.lineargs.watchnext.utils.retrofit.series.Series;
import com.lineargs.watchnext.utils.retrofit.series.SeriesApiService;

import java.io.IOException;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class WatchNextWorker extends Worker {

    private static final String BASE_URL = "https://api.themoviedb.org/3/";
    private static final String PATH_POPULAR = "popular";
    private static final String PATH_TOP_RATED = "top_rated";
    private static final String PATH_UPCOMING = "upcoming";
    private static final String PATH_THEATER = "now_playing";
    private static final String PATH_ON_THE_AIR = "on_the_air";
    private static final int SYNC_NOTIFICATION_ID = 29101988;

    private static final Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    public WatchNextWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        NotificationUtils.syncProgress(SYNC_NOTIFICATION_ID, getApplicationContext());

        String region = Locale.getDefault().getCountry();
        if (region.isEmpty()) {
            region = "US";
        }

        MovieApiService movieApiService = retrofit.create(MovieApiService.class);
        SeriesApiService seriesApiService = retrofit.create(SeriesApiService.class);

        try {
            // Movies
            fetchPopularMovies(movieApiService, region);
            fetchUpcomingMovies(movieApiService, region);
            fetchTopRatedMovies(movieApiService, region);
            fetchTheaterMovies(movieApiService, region);

            // Series
            fetchPopularSeries(seriesApiService);
            fetchTopRatedSeries(seriesApiService);
            fetchOnTheAirSeries(seriesApiService);

            NotificationUtils.syncComplete(SYNC_NOTIFICATION_ID, getApplicationContext());
            return Result.success();
        } catch (IOException e) {
            e.printStackTrace();
            return Result.retry();
        }
    }

    private void fetchPopularMovies(MovieApiService service, String region) throws IOException {
        Call<Movies> call = service.getMovies(PATH_POPULAR, BuildConfig.MOVIE_DATABASE_API_KEY, region, 1);
        Response<Movies> response = call.execute();
        if (response.isSuccessful() && response.body() != null) {
            ContentValues[] values = MovieDbUtils.getPopularContentValues(response.body().getResults());
            insertData(DataContract.PopularMovieEntry.CONTENT_URI, values);
            android.preference.PreferenceManager.getDefaultSharedPreferences(getApplicationContext())
                    .edit()
                    .putInt("pref_popular_next_page", 2)
                    .apply();
        }
    }

    private void fetchUpcomingMovies(MovieApiService service, String region) throws IOException {
        Call<Movies> call = service.getMovies(PATH_UPCOMING, BuildConfig.MOVIE_DATABASE_API_KEY, region, 1);
        Response<Movies> response = call.execute();
        if (response.isSuccessful() && response.body() != null) {
            ContentValues[] values = MovieDbUtils.getUpcomingContentValues(response.body().getResults());
            insertData(DataContract.UpcomingMovieEntry.CONTENT_URI, values);
            android.preference.PreferenceManager.getDefaultSharedPreferences(getApplicationContext())
                    .edit()
                    .putInt("pref_upcoming_next_page", 2)
                    .apply();
        }
    }

    private void fetchTopRatedMovies(MovieApiService service, String region) throws IOException {
        Call<Movies> call = service.getMovies(PATH_TOP_RATED, BuildConfig.MOVIE_DATABASE_API_KEY, region, 1);
        Response<Movies> response = call.execute();
        if (response.isSuccessful() && response.body() != null) {
            ContentValues[] values = MovieDbUtils.getTopContentValues(response.body().getResults());
            insertData(DataContract.TopRatedMovieEntry.CONTENT_URI, values);
            android.preference.PreferenceManager.getDefaultSharedPreferences(getApplicationContext())
                    .edit()
                    .putInt("pref_top_rated_next_page", 2)
                    .apply();
        }
    }

    private void fetchTheaterMovies(MovieApiService service, String region) throws IOException {
        Call<Movies> call = service.getMovies(PATH_THEATER, BuildConfig.MOVIE_DATABASE_API_KEY, region, 1);
        Response<Movies> response = call.execute();
        if (response.isSuccessful() && response.body() != null) {
            ContentValues[] values = MovieDbUtils.getTheaterContentValues(response.body().getResults());
            insertData(DataContract.TheaterMovieEntry.CONTENT_URI, values);
            android.preference.PreferenceManager.getDefaultSharedPreferences(getApplicationContext())
                    .edit()
                    .putInt("pref_theater_next_page", 2)
                    .apply();
        }
    }

    private void fetchPopularSeries(SeriesApiService service) throws IOException {
        Call<Series> call = service.getSeries(PATH_POPULAR, BuildConfig.MOVIE_DATABASE_API_KEY, 1);
        Response<Series> response = call.execute();
        if (response.isSuccessful() && response.body() != null) {
            ContentValues[] values = SerieDbUtils.getPopularContentValues(response.body().getResults());
            insertData(DataContract.PopularSerieEntry.CONTENT_URI, values);
            android.preference.PreferenceManager.getDefaultSharedPreferences(getApplicationContext())
                    .edit()
                    .putInt("pref_series_popular_next_page", 2)
                    .apply();
        }
    }

    private void fetchTopRatedSeries(SeriesApiService service) throws IOException {
        Call<Series> call = service.getSeries(PATH_TOP_RATED, BuildConfig.MOVIE_DATABASE_API_KEY, 1);
        Response<Series> response = call.execute();
        if (response.isSuccessful() && response.body() != null) {
            ContentValues[] values = SerieDbUtils.getTopContentValues(response.body().getResults());
            insertData(DataContract.TopRatedSerieEntry.CONTENT_URI, values);
            android.preference.PreferenceManager.getDefaultSharedPreferences(getApplicationContext())
                    .edit()
                    .putInt("pref_series_top_rated_next_page", 2)
                    .apply();
        }
    }

    private void fetchOnTheAirSeries(SeriesApiService service) throws IOException {
        Call<Series> call = service.getSeries(PATH_ON_THE_AIR, BuildConfig.MOVIE_DATABASE_API_KEY, 1);
        Response<Series> response = call.execute();
        if (response.isSuccessful() && response.body() != null) {
            ContentValues[] values = SerieDbUtils.getOnTheAirContentValues(response.body().getResults());
            insertData(DataContract.OnTheAirSerieEntry.CONTENT_URI, values);
            android.preference.PreferenceManager.getDefaultSharedPreferences(getApplicationContext())
                    .edit()
                    .putInt("pref_series_on_the_air_next_page", 2)
                    .apply();
        }
    }

    private void insertData(android.net.Uri uri, ContentValues[] values) {
        if (values != null && values.length > 0) {
            getApplicationContext().getContentResolver().delete(uri, null, null);
            getApplicationContext().getContentResolver().bulkInsert(uri, values);
        }
    }
}
