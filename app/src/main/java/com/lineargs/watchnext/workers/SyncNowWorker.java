package com.lineargs.watchnext.workers;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.lineargs.watchnext.BuildConfig;
import com.lineargs.watchnext.api.movies.MovieApiService;
import com.lineargs.watchnext.api.movies.Movies;
import com.lineargs.watchnext.data.WatchNextDatabase;
import com.lineargs.watchnext.utils.MovieUtils;

import java.lang.ref.WeakReference;
import java.text.ParseException;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.lineargs.watchnext.utils.Constants.IMAGE_SMALL_BASE;

public class SyncNowWorker extends Worker {

    private static final String TAG = SyncNowWorker.class.getSimpleName();
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
    public SyncNowWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        Context applicationContext = getApplicationContext();
        database = WatchNextDatabase.getDatabase(applicationContext);
        final MovieApiService movieApiService = retrofit.create(MovieApiService.class);

        String language = Locale.getDefault().toString();
        String region = language.substring(language.indexOf('_') + 1);
        Log.w(TAG + "Region", region);
        Log.w("Locale", Locale.getDefault().getLanguage());

        Call<Movies> popularCall = movieApiService.getMovies(PATH_POPULAR, BuildConfig.MOVIE_DATABASE_API_KEY, region);
        popularCall.enqueue(new Callback<Movies>() {
            @Override
            public void onResponse(@NonNull Call<Movies> call, @NonNull final Response<Movies> response) {

                if (response.isSuccessful() && response.body() != null) {
                    InsertPopularMovies popularMovies = new InsertPopularMovies(applicationContext, database);
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
            @Override
            public void onResponse(@NonNull Call<Movies> call, @NonNull final Response<Movies> response) {
                if (response.isSuccessful() && response.body() != null) {
                    InsertUpcomingMovies upcomingMovies = new InsertUpcomingMovies(applicationContext, database);
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
            @Override
            public void onResponse(@NonNull Call<Movies> call, @NonNull final Response<Movies> response) {
                if (response.isSuccessful() && response.body() != null) {
                    InsertTopMovies topMovies = new InsertTopMovies(applicationContext, database);
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
            @Override
            public void onResponse(@NonNull Call<Movies> call, @NonNull final Response<Movies> response) {

                if (response.isSuccessful() && response.body() != null) {
                    InsertTheaterMovies theaterMovies = new InsertTheaterMovies(applicationContext, database);
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
//        final SeriesApiService seriesApiService = retrofit.create(SeriesApiService.class);
//
//        Call<Series> popularSeriesCall = seriesApiService.getSeries(PATH_POPULAR, BuildConfig.MOVIE_DATABASE_API_KEY);
//
//        popularSeriesCall.enqueue(new Callback<Series>() {
//            @Override
//            public void onResponse(@NonNull Call<Series> call, @NonNull Response<Series> response) {
//                if (response.isSuccessful() && response.body() != null) {
////                    ContentValues[] values = SerieDbUtils.getPopularContentValues(response.body().getResults());
////                    InsertPopularSeries insertPopularSeries = new InsertPopularSeries(getContext());
////                    insertPopularSeries.execute(values);
//                } else if (response.errorBody() != null) {
//                    response.errorBody().close();
//                }
//            }
//
//            @Override
//            public void onFailure(@NonNull Call<Series> call, @NonNull Throwable t) {
//            }
//        });
//
//        Call<Series> topSeriesCall = seriesApiService.getSeries(PATH_TOP_RATED, BuildConfig.MOVIE_DATABASE_API_KEY);
//
//        topSeriesCall.enqueue(new Callback<Series>() {
//            @Override
//            public void onResponse(@NonNull Call<Series> call, @NonNull Response<Series> response) {
//                if (response.isSuccessful() && response.body() != null) {
////                    ContentValues[] values = SerieDbUtils.getTopContentValues(response.body().getResults());
////                    InsertTopSeries insertTopSeries = new InsertTopSeries(getContext());
////                    insertTopSeries.execute(values);
//                } else if (response.errorBody() != null) {
//                    response.errorBody().close();
//                }
//            }
//
//            @Override
//            public void onFailure(@NonNull Call<Series> call, @NonNull Throwable t) {
//            }
//        });
//
//        Call<Series> onTheAirCall = seriesApiService.getSeries(PATH_ON_THE_AIR, BuildConfig.MOVIE_DATABASE_API_KEY);
//
//        onTheAirCall.enqueue(new Callback<Series>() {
//            @Override
//            public void onResponse(@NonNull Call<Series> call, @NonNull Response<Series> response) {
//                if (response.isSuccessful() && response.body() != null) {
////                    ContentValues[] values = SerieDbUtils.getOnTheAirContentValues(response.body().getResults());
////                    InsertOnTheAirSeries insertOnTheAirSeries = new InsertOnTheAirSeries(getContext());
////                    insertOnTheAirSeries.execute(values);
//                } else if (response.errorBody() != null) {
//                    response.errorBody().close();
//                }
//            }
//
//            @Override
//            public void onFailure(@NonNull Call<Series> call, @NonNull Throwable t) {
//            }
//        });
        return Result.success();
    }

    private static class InsertPopularMovies extends AsyncTask<List<com.lineargs.watchnext.api.movies.Result>, Void, Void> {

        private final WeakReference<Context> context;
        private final WatchNextDatabase database;

        InsertPopularMovies(Context context, WatchNextDatabase database) {
            this.context = new WeakReference<>(context);
            this.database = database;
        }

        @Override
        protected Void doInBackground(List<com.lineargs.watchnext.api.movies.Result>... lists) {
            List<com.lineargs.watchnext.api.movies.Result> resultList = lists[0];
            com.lineargs.watchnext.data.Movies movies = new com.lineargs.watchnext.data.Movies();
            for (com.lineargs.watchnext.api.movies.Result result : resultList) {
                movies.setTmdbId(result.getId());
                movies.setBackdropPath(IMAGE_SMALL_BASE + result.getBackdropPath());
                movies.setPosterPath(IMAGE_SMALL_BASE + result.getPosterPath());
                movies.setVoteAverage(MovieUtils.getNormalizedVoteAverage(String.valueOf(result.getVoteAverage())));
                movies.setTitle(result.getTitle());
                movies.setOverview(result.getOverview());
                try {
                    movies.setReleaseDate(MovieUtils.getNormalizedReleaseDate(result.getReleaseDate()));
                } catch (ParseException e) {
                    e.printStackTrace();
                    Log.e(TAG, e.getMessage());
                }
                movies.setType(0);
                database.moviesDao().insert(movies);
            }
            return null;
        }
    }

    private static class InsertTopMovies extends AsyncTask<List<com.lineargs.watchnext.api.movies.Result>, Void, Void> {

        private final WeakReference<Context> context;
        private final WatchNextDatabase database;

        InsertTopMovies(Context context, WatchNextDatabase database) {
            this.context = new WeakReference<>(context);
            this.database = database;
        }

        @Override
        protected Void doInBackground(List<com.lineargs.watchnext.api.movies.Result>... lists) {
            List<com.lineargs.watchnext.api.movies.Result> resultList = lists[0];
            com.lineargs.watchnext.data.Movies movies = new com.lineargs.watchnext.data.Movies();
            for (com.lineargs.watchnext.api.movies.Result result : resultList) {
                movies.setTmdbId(result.getId());
                movies.setBackdropPath(IMAGE_SMALL_BASE + result.getBackdropPath());
                movies.setPosterPath(IMAGE_SMALL_BASE + result.getPosterPath());
                movies.setVoteAverage(MovieUtils.getNormalizedVoteAverage(String.valueOf(result.getVoteAverage())));
                movies.setTitle(result.getTitle());
                movies.setOverview(result.getOverview());
                try {
                    movies.setReleaseDate(MovieUtils.getNormalizedReleaseDate(result.getReleaseDate()));
                } catch (ParseException e) {
                    e.printStackTrace();
                    Log.e(TAG, e.getMessage());
                }
                movies.setType(1);
                database.moviesDao().insert(movies);
            }
            return null;
        }
    }

    private static class InsertUpcomingMovies extends AsyncTask<List<com.lineargs.watchnext.api.movies.Result>, Void, Void> {

        private final WeakReference<Context> context;
        private final WatchNextDatabase database;

        InsertUpcomingMovies(Context context, WatchNextDatabase database) {
            this.context = new WeakReference<>(context);
            this.database = database;
        }

        @Override
        protected Void doInBackground(List<com.lineargs.watchnext.api.movies.Result>... lists) {
            List<com.lineargs.watchnext.api.movies.Result> resultList = lists[0];
            com.lineargs.watchnext.data.Movies movies = new com.lineargs.watchnext.data.Movies();
            for (com.lineargs.watchnext.api.movies.Result result : resultList) {
                movies.setTmdbId(result.getId());
                movies.setBackdropPath(IMAGE_SMALL_BASE + result.getBackdropPath());
                movies.setPosterPath(IMAGE_SMALL_BASE + result.getPosterPath());
                movies.setVoteAverage(MovieUtils.getNormalizedVoteAverage(String.valueOf(result.getVoteAverage())));
                movies.setTitle(result.getTitle());
                movies.setOverview(result.getOverview());
                try {
                    movies.setReleaseDate(MovieUtils.getNormalizedReleaseDate(result.getReleaseDate()));
                } catch (ParseException e) {
                    e.printStackTrace();
                    Log.e(TAG, e.getMessage());
                }
                movies.setType(2);
                database.moviesDao().insert(movies);
            }
            return null;
        }
    }

    private static class InsertTheaterMovies extends AsyncTask<List<com.lineargs.watchnext.api.movies.Result>, Void, Void> {

        private final WeakReference<Context> context;
        private final WatchNextDatabase database;

        InsertTheaterMovies(Context context, WatchNextDatabase database) {
            this.context = new WeakReference<>(context);
            this.database = database;
        }

        @Override
        protected Void doInBackground(List<com.lineargs.watchnext.api.movies.Result>... lists) {
            List<com.lineargs.watchnext.api.movies.Result> resultList = lists[0];
            com.lineargs.watchnext.data.Movies movies = new com.lineargs.watchnext.data.Movies();
            for (com.lineargs.watchnext.api.movies.Result result : resultList) {
                movies.setTmdbId(result.getId());
                movies.setBackdropPath(IMAGE_SMALL_BASE + result.getBackdropPath());
                movies.setPosterPath(IMAGE_SMALL_BASE + result.getPosterPath());
                movies.setVoteAverage(MovieUtils.getNormalizedVoteAverage(String.valueOf(result.getVoteAverage())));
                movies.setTitle(result.getTitle());
                movies.setOverview(result.getOverview());
                try {
                    movies.setReleaseDate(MovieUtils.getNormalizedReleaseDate(result.getReleaseDate()));
                } catch (ParseException e) {
                    e.printStackTrace();
                    Log.e(TAG, e.getMessage());
                }
                movies.setType(3);
                database.moviesDao().insert(movies);
            }
            return null;
        }
    }
}
