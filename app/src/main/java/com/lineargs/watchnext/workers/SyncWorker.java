package com.lineargs.watchnext.workers;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.lineargs.watchnext.BuildConfig;
import com.lineargs.watchnext.api.movies.MovieApiService;
import com.lineargs.watchnext.api.movies.Movies;
import com.lineargs.watchnext.api.series.Series;
import com.lineargs.watchnext.api.series.SeriesApiService;
import com.lineargs.watchnext.data.WatchNextDatabase;
import com.lineargs.watchnext.utils.NotificationUtils;

import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.lineargs.watchnext.utils.Constants.SYNC_NOTIFICATION_ID;

public class SyncWorker extends Worker {

    private static final String BASE_URL = "https://api.themoviedb.org/3/";
    private static final String PATH_POPULAR = "popular";
    private static final String PATH_TOP_RATED = "top_rated";
    private static final String PATH_UPCOMING = "upcoming";
    private static final String PATH_THEATER = "now_playing";
    private static final String PATH_ON_THE_AIR = "on_the_air";

    private WatchNextDatabase database;

    private static final Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    /**
     * Creates an instance of the {@link Worker}.
     *
     * @param context      the application {@link Context}
     * @param workerParams the set of {@link WorkerParameters}
     */
    public SyncWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        Context applicationContext = getApplicationContext();
        database = WatchNextDatabase.getDatabase(applicationContext);
        NotificationUtils.syncProgress(SYNC_NOTIFICATION_ID, applicationContext);
        database.moviesDao().deleteAllMovies();
        database.seriesDao().deleteAllSeries();
        final MovieApiService movieApiService = retrofit.create(MovieApiService.class);

        String language = Locale.getDefault().toString();
        String region = language.substring(language.indexOf('_') + 1);

        Call<Movies> popularCall = movieApiService.getMovies(PATH_POPULAR, BuildConfig.MOVIE_DATABASE_API_KEY, region);
        popularCall.enqueue(new Callback<Movies>() {
            @SuppressWarnings("unchecked")
            @Override
            public void onResponse(@NonNull Call<Movies> call, @NonNull final Response<Movies> response) {

                if (response.isSuccessful() && response.body() != null) {
                    WorkerUtils.InsertPopularMovies popularMovies = new WorkerUtils.InsertPopularMovies(database);
                    popularMovies.execute(response.body().getResults());
                } else if (response.errorBody() != null) {
                    response.errorBody().close();
                }
            }

            @Override
            public void onFailure(@NonNull Call<Movies> call, @NonNull Throwable t) {
            }
        });

        Call<Movies> upcomingCall = movieApiService.getMovies(PATH_UPCOMING, BuildConfig.MOVIE_DATABASE_API_KEY, region);

        upcomingCall.enqueue(new Callback<Movies>() {
            @SuppressWarnings("unchecked")
            @Override
            public void onResponse(@NonNull Call<Movies> call, @NonNull final Response<Movies> response) {
                if (response.isSuccessful() && response.body() != null) {
                    WorkerUtils.InsertUpcomingMovies upcomingMovies = new WorkerUtils.InsertUpcomingMovies(database);
                    upcomingMovies.execute(response.body().getResults());
                } else if (response.errorBody() != null) {
                    response.errorBody().close();
                }
            }

            @Override
            public void onFailure(@NonNull Call<Movies> call, @NonNull Throwable t) {
            }
        });

        Call<Movies> topCall = movieApiService.getMovies(PATH_TOP_RATED, BuildConfig.MOVIE_DATABASE_API_KEY, region);

        topCall.enqueue(new Callback<Movies>() {
            @SuppressWarnings("unchecked")
            @Override
            public void onResponse(@NonNull Call<Movies> call, @NonNull final Response<Movies> response) {
                if (response.isSuccessful() && response.body() != null) {
                    WorkerUtils.InsertTopMovies topMovies = new WorkerUtils.InsertTopMovies(database);
                    topMovies.execute(response.body().getResults());
                } else if (response.errorBody() != null) {
                    response.errorBody().close();
                }
            }

            @Override
            public void onFailure(@NonNull Call<Movies> call, @NonNull Throwable t) {
            }
        });

        Call<Movies> theaterCall = movieApiService.getMovies(PATH_THEATER, BuildConfig.MOVIE_DATABASE_API_KEY, region);

        theaterCall.enqueue(new Callback<Movies>() {
            @SuppressWarnings("unchecked")
            @Override
            public void onResponse(@NonNull Call<Movies> call, @NonNull final Response<Movies> response) {
                if (response.isSuccessful() && response.body() != null) {
                    WorkerUtils.InsertTheaterMovies theaterMovies = new WorkerUtils.InsertTheaterMovies(database);
                    theaterMovies.execute(response.body().getResults());
                } else if (response.errorBody() != null) {
                    response.errorBody().close();
                }
            }

            @Override
            public void onFailure(@NonNull Call<Movies> call, @NonNull Throwable t) {
            }
        });
//
        final SeriesApiService seriesApiService = retrofit.create(SeriesApiService.class);

        Call<Series> popularSeriesCall = seriesApiService.getSeries(PATH_POPULAR, BuildConfig.MOVIE_DATABASE_API_KEY);

        popularSeriesCall.enqueue(new Callback<Series>() {
            @SuppressWarnings("unchecked")
            @Override
            public void onResponse(@NonNull Call<Series> call, @NonNull Response<Series> response) {
                if (response.isSuccessful() && response.body() != null) {
                    WorkerUtils.InsertPopularSeries popularSeries = new WorkerUtils.InsertPopularSeries(database);
                    popularSeries.execute(response.body().getResults());
                } else if (response.errorBody() != null) {
                    response.errorBody().close();
                }
            }

            @Override
            public void onFailure(@NonNull Call<Series> call, @NonNull Throwable t) {
            }
        });

        Call<Series> topSeriesCall = seriesApiService.getSeries(PATH_TOP_RATED, BuildConfig.MOVIE_DATABASE_API_KEY);

        topSeriesCall.enqueue(new Callback<Series>() {
            @SuppressWarnings("unchecked")
            @Override
            public void onResponse(@NonNull Call<Series> call, @NonNull Response<Series> response) {
                if (response.isSuccessful() && response.body() != null) {
                    WorkerUtils.InsertTopSeries topSeries = new WorkerUtils.InsertTopSeries(database);
                    topSeries.execute(response.body().getResults());
                } else if (response.errorBody() != null) {
                    response.errorBody().close();
                }
            }

            @Override
            public void onFailure(@NonNull Call<Series> call, @NonNull Throwable t) {
            }
        });

        Call<Series> onTheAirCall = seriesApiService.getSeries(PATH_ON_THE_AIR, BuildConfig.MOVIE_DATABASE_API_KEY);

        onTheAirCall.enqueue(new Callback<Series>() {
            @SuppressWarnings("unchecked")
            @Override
            public void onResponse(@NonNull Call<Series> call, @NonNull Response<Series> response) {
                if (response.isSuccessful() && response.body() != null) {
                    WorkerUtils.InsertOnTheAirSeries onTheAirSeries = new WorkerUtils.InsertOnTheAirSeries(database);
                    onTheAirSeries.execute(response.body().getResults());
                } else if (response.errorBody() != null) {
                    response.errorBody().close();
                }
            }

            @Override
            public void onFailure(@NonNull Call<Series> call, @NonNull Throwable t) {
            }
        });
        NotificationUtils.clearNotification(SYNC_NOTIFICATION_ID, applicationContext);
        return Result.success();
    }
}