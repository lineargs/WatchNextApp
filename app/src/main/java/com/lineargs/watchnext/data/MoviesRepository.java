package com.lineargs.watchnext.data;

import android.app.Application;
import androidx.lifecycle.LiveData;

import com.lineargs.watchnext.data.dao.MoviesDao;
import com.lineargs.watchnext.data.entity.PopularMovie;
import com.lineargs.watchnext.data.entity.TheaterMovie;
import com.lineargs.watchnext.data.entity.TopRatedMovie;
import com.lineargs.watchnext.data.entity.UpcomingMovie;
import com.lineargs.watchnext.utils.dbutils.MovieDbUtils;
import com.lineargs.watchnext.utils.retrofit.movies.MovieApiService;
import com.lineargs.watchnext.utils.retrofit.movies.Movies;
import com.lineargs.watchnext.BuildConfig;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MoviesRepository {

    private final MoviesDao moviesDao;

    private final Application application;

    public MoviesRepository(Application application) {
        this.application = application;
        WatchNextDatabase database = WatchNextDatabase.getDatabase(application);
        moviesDao = database.moviesDao();
    }

    public LiveData<List<PopularMovie>> getPopularMovies() {
        return moviesDao.getPopularMoviesLiveData();
    }

    public LiveData<PopularMovie> getPopularMovieLiveData(int id) {
        return moviesDao.getPopularMovieLiveData(id);
    }

    public LiveData<List<TopRatedMovie>> getTopRatedMovies() {
        return moviesDao.getTopRatedMoviesLiveData();
    }

    public LiveData<TopRatedMovie> getTopRatedMovieLiveData(int id) {
        return moviesDao.getTopRatedMovieLiveData(id);
    }

    public LiveData<List<UpcomingMovie>> getUpcomingMovies() {
        return moviesDao.getUpcomingMoviesLiveData();
    }

    public LiveData<UpcomingMovie> getUpcomingMovieLiveData(int id) {
        return moviesDao.getUpcomingMovieLiveData(id);
    }

    public LiveData<List<TheaterMovie>> getTheaterMovies() {
        return moviesDao.getTheaterMoviesLiveData();
    }

    public LiveData<TheaterMovie> getTheaterMovieLiveData(int id) {
        return moviesDao.getTheaterMovieLiveData(id);
    }
    public int getPopularMoviesCount() {
        return moviesDao.getPopularMoviesCount();
    }

    public void fetchNextPopularMovies(com.lineargs.watchnext.utils.NetworkStateCallback callback) {
        if (callback != null) callback.onLoading();
        WatchNextDatabase.databaseWriteExecutor.execute(() -> {
            android.content.SharedPreferences preferences = android.preference.PreferenceManager.getDefaultSharedPreferences(application);
            int page = preferences.getInt("pref_popular_next_page", 2);

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("https://api.themoviedb.org/3/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            MovieApiService service = retrofit.create(MovieApiService.class);
            String region = Locale.getDefault().getCountry();
            if (region.isEmpty()) {
                region = "US";
            }

            try {
                Call<Movies> call = service.getMovies("popular", BuildConfig.MOVIE_DATABASE_API_KEY, region, page);
                Response<Movies> response = call.execute();
                if (response.isSuccessful() && response.body() != null) {
                    List<com.lineargs.watchnext.utils.retrofit.movies.Result> results = response.body().getResults();
                    if (results != null && !results.isEmpty()) {
                        for (com.lineargs.watchnext.utils.retrofit.movies.Result result : results) {
                            PopularMovie movie = new PopularMovie();
                            movie.setTmdbId(result.getId());
                            movie.setTitle(result.getTitle());
                            movie.setOverview(result.getOverview());
                            movie.setVoteAverage(com.lineargs.watchnext.utils.MovieUtils.getNormalizedVoteAverage(String.valueOf(result.getVoteAverage())));
                            movie.setReleaseDate(result.getReleaseDate()); 
                            movie.setPosterPath("https://image.tmdb.org/t/p/w500/" + result.getPosterPath());
                            movie.setBackdropPath("https://image.tmdb.org/t/p/w500/" + result.getBackdropPath());
                            movie.setOriginalLanguage(result.getOriginalLanguage());
                            movie.setOriginalTitle(result.getOriginalTitle());
                            moviesDao.insertPopularMovie(movie);
                        }
                        preferences.edit().putInt("pref_popular_next_page", page + 1).apply();
                        if (callback != null) callback.onSuccess();
                    } else {
                        if (callback != null) callback.onError("No more movies found");
                    }
                } else {
                    if (callback != null) callback.onError("Failed to fetch movies");
                }
            } catch (IOException e) {
                e.printStackTrace();
                if (callback != null) callback.onError(e.getMessage());
            }
        });
    }

    public void fetchNextTopRatedMovies(com.lineargs.watchnext.utils.NetworkStateCallback callback) {
        if (callback != null) callback.onLoading();
        WatchNextDatabase.databaseWriteExecutor.execute(() -> {
            android.content.SharedPreferences preferences = android.preference.PreferenceManager.getDefaultSharedPreferences(application);
            int page = preferences.getInt("pref_top_rated_next_page", 2);

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("https://api.themoviedb.org/3/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            MovieApiService service = retrofit.create(MovieApiService.class);
            String region = Locale.getDefault().getCountry();
            if (region.isEmpty()) {
                region = "US";
            }

            try {
                Call<Movies> call = service.getMovies("top_rated", BuildConfig.MOVIE_DATABASE_API_KEY, region, page);
                Response<Movies> response = call.execute();
                if (response.isSuccessful() && response.body() != null) {
                    List<com.lineargs.watchnext.utils.retrofit.movies.Result> results = response.body().getResults();
                    if (results != null && !results.isEmpty()) {
                        for (com.lineargs.watchnext.utils.retrofit.movies.Result result : results) {
                            com.lineargs.watchnext.data.entity.TopRatedMovie movie = new com.lineargs.watchnext.data.entity.TopRatedMovie();
                            movie.setTmdbId(result.getId());
                            movie.setTitle(result.getTitle());
                            movie.setOverview(result.getOverview());
                            movie.setVoteAverage(com.lineargs.watchnext.utils.MovieUtils.getNormalizedVoteAverage(String.valueOf(result.getVoteAverage())));
                            movie.setReleaseDate(result.getReleaseDate());
                            movie.setPosterPath("https://image.tmdb.org/t/p/w500/" + result.getPosterPath());
                            movie.setBackdropPath("https://image.tmdb.org/t/p/w500/" + result.getBackdropPath());
                            movie.setOriginalLanguage(result.getOriginalLanguage());
                            movie.setOriginalTitle(result.getOriginalTitle());
                            moviesDao.insertTopRatedMovie(movie);
                        }
                        preferences.edit().putInt("pref_top_rated_next_page", page + 1).apply();
                        if (callback != null) callback.onSuccess();
                    } else {
                        if (callback != null) callback.onError("No more movies found");
                    }
                } else {
                    if (callback != null) callback.onError("Failed to fetch movies");
                }
            } catch (IOException e) {
                e.printStackTrace();
                if (callback != null) callback.onError(e.getMessage());
            }
        });
    }

    public void fetchNextUpcomingMovies(com.lineargs.watchnext.utils.NetworkStateCallback callback) {
        if (callback != null) callback.onLoading();
        WatchNextDatabase.databaseWriteExecutor.execute(() -> {
            android.content.SharedPreferences preferences = android.preference.PreferenceManager.getDefaultSharedPreferences(application);
            int page = preferences.getInt("pref_upcoming_next_page", 2);

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("https://api.themoviedb.org/3/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            MovieApiService service = retrofit.create(MovieApiService.class);
            String region = Locale.getDefault().getCountry();
            if (region.isEmpty()) {
                region = "US";
            }

            try {
                Call<Movies> call = service.getMovies("upcoming", BuildConfig.MOVIE_DATABASE_API_KEY, region, page);
                Response<Movies> response = call.execute();
                if (response.isSuccessful() && response.body() != null) {
                    List<com.lineargs.watchnext.utils.retrofit.movies.Result> results = response.body().getResults();
                    if (results != null && !results.isEmpty()) {
                        for (com.lineargs.watchnext.utils.retrofit.movies.Result result : results) {
                            com.lineargs.watchnext.data.entity.UpcomingMovie movie = new com.lineargs.watchnext.data.entity.UpcomingMovie();
                            movie.setTmdbId(result.getId());
                            movie.setTitle(result.getTitle());
                            movie.setOverview(result.getOverview());
                            movie.setVoteAverage(com.lineargs.watchnext.utils.MovieUtils.getNormalizedVoteAverage(String.valueOf(result.getVoteAverage())));
                            movie.setReleaseDate(result.getReleaseDate());
                            movie.setPosterPath("https://image.tmdb.org/t/p/w500/" + result.getPosterPath());
                            movie.setBackdropPath("https://image.tmdb.org/t/p/w500/" + result.getBackdropPath());
                            movie.setOriginalLanguage(result.getOriginalLanguage());
                            movie.setOriginalTitle(result.getOriginalTitle());
                            moviesDao.insertUpcomingMovie(movie);
                        }
                        preferences.edit().putInt("pref_upcoming_next_page", page + 1).apply();
                        if (callback != null) callback.onSuccess();
                    } else {
                        if (callback != null) callback.onError("No more movies found");
                    }
                } else {
                    if (callback != null) callback.onError("Failed to fetch movies");
                }
            } catch (IOException e) {
                e.printStackTrace();
                if (callback != null) callback.onError(e.getMessage());
            }
        });
    }
}
