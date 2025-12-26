package com.lineargs.watchnext.data;

import android.app.Application;
import androidx.lifecycle.LiveData;

import com.lineargs.watchnext.data.dao.SeriesDao;
import com.lineargs.watchnext.data.entity.OnTheAirSerie;
import com.lineargs.watchnext.data.entity.PopularSerie;
import com.lineargs.watchnext.data.entity.TopRatedSerie;

import com.lineargs.watchnext.BuildConfig;
import com.lineargs.watchnext.utils.retrofit.series.Series;
import com.lineargs.watchnext.utils.retrofit.series.SeriesApiService;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SeriesRepository {

    private final SeriesDao seriesDao;
    private final Application application;

    public SeriesRepository(Application application) {
        this.application = application;
        WatchNextDatabase database = WatchNextDatabase.getDatabase(application);
        seriesDao = database.seriesDao();
    }

    public LiveData<List<PopularSerie>> getPopularSeries() {
        return seriesDao.getPopularSeriesLiveData();
    }

    public LiveData<PopularSerie> getPopularSerieLiveData(int id) {
        return seriesDao.getPopularSerieLiveData(id);
    }

    public LiveData<List<TopRatedSerie>> getTopRatedSeries() {
        return seriesDao.getTopRatedSeriesLiveData();
    }

    public LiveData<List<OnTheAirSerie>> getOnTheAirSeries() {
        return seriesDao.getOnTheAirSeriesLiveData();
    }

    public LiveData<TopRatedSerie> getTopRatedSerieLiveData(int id) {
        return seriesDao.getTopRatedSerieLiveData(id);
    }

    public LiveData<OnTheAirSerie> getOnTheAirSerieLiveData(int id) {
        return seriesDao.getOnTheAirSerieLiveData(id);
    }

    public void fetchNextPopularSeries(com.lineargs.watchnext.utils.NetworkStateCallback callback) {
        if (callback != null) callback.onLoading();
        WatchNextDatabase.databaseWriteExecutor.execute(() -> {
            android.content.SharedPreferences preferences = android.preference.PreferenceManager.getDefaultSharedPreferences(application);
            int page = preferences.getInt("pref_series_popular_next_page", 2);

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("https://api.themoviedb.org/3/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            SeriesApiService service = retrofit.create(SeriesApiService.class);

            try {
                Call<Series> call = service.getSeries("popular", BuildConfig.MOVIE_DATABASE_API_KEY, page);
                Response<Series> response = call.execute();
                if (response.isSuccessful() && response.body() != null) {
                    List<com.lineargs.watchnext.utils.retrofit.series.SeriesResult> results = response.body().getResults();
                    if (results != null && !results.isEmpty()) {
                        for (com.lineargs.watchnext.utils.retrofit.series.SeriesResult result : results) {
                            PopularSerie serie = new PopularSerie();
                            serie.setTmdbId(result.getId());
                            serie.setTitle(result.getName());
                            serie.setOverview(result.getOverview());
                            serie.setVoteAverage(com.lineargs.watchnext.utils.MovieUtils.getNormalizedVoteAverage(String.valueOf(result.getVoteAverage())));
                            serie.setReleaseDate(result.getFirstAirDate());
                            serie.setPosterPath("https://image.tmdb.org/t/p/w500/" + result.getPosterPath());
                            serie.setBackdropPath("https://image.tmdb.org/t/p/w500/" + result.getBackdropPath());
                            serie.setOriginalLanguage(result.getOriginalLanguage());
                            serie.setOriginalTitle(result.getOriginalName());
                            seriesDao.insertPopularSerie(serie);
                        }
                        preferences.edit().putInt("pref_series_popular_next_page", page + 1).apply();
                        if (callback != null) callback.onSuccess();
                    } else {
                        if (callback != null) callback.onError("No more series found");
                    }
                } else {
                    if (callback != null) callback.onError("Failed to fetch series");
                }
            } catch (IOException e) {
                e.printStackTrace();
                if (callback != null) callback.onError(e.getMessage());
            }
        });
    }

    public void fetchNextTopRatedSeries(com.lineargs.watchnext.utils.NetworkStateCallback callback) {
        if (callback != null) callback.onLoading();
        WatchNextDatabase.databaseWriteExecutor.execute(() -> {
            android.content.SharedPreferences preferences = android.preference.PreferenceManager.getDefaultSharedPreferences(application);
            int page = preferences.getInt("pref_series_top_rated_next_page", 2);

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("https://api.themoviedb.org/3/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            SeriesApiService service = retrofit.create(SeriesApiService.class);

            try {
                Call<Series> call = service.getSeries("top_rated", BuildConfig.MOVIE_DATABASE_API_KEY, page);
                Response<Series> response = call.execute();
                if (response.isSuccessful() && response.body() != null) {
                    List<com.lineargs.watchnext.utils.retrofit.series.SeriesResult> results = response.body().getResults();
                    if (results != null && !results.isEmpty()) {
                        for (com.lineargs.watchnext.utils.retrofit.series.SeriesResult result : results) {
                            TopRatedSerie serie = new TopRatedSerie();
                            serie.setTmdbId(result.getId());
                            serie.setTitle(result.getName());
                            serie.setOverview(result.getOverview());
                            serie.setVoteAverage(com.lineargs.watchnext.utils.MovieUtils.getNormalizedVoteAverage(String.valueOf(result.getVoteAverage())));
                            serie.setReleaseDate(result.getFirstAirDate());
                            serie.setPosterPath("https://image.tmdb.org/t/p/w500/" + result.getPosterPath());
                            serie.setBackdropPath("https://image.tmdb.org/t/p/w500/" + result.getBackdropPath());
                            serie.setOriginalLanguage(result.getOriginalLanguage());
                            serie.setOriginalTitle(result.getOriginalName());
                            seriesDao.insertTopRatedSerie(serie);
                        }
                        preferences.edit().putInt("pref_series_top_rated_next_page", page + 1).apply();
                        if (callback != null) callback.onSuccess();
                    } else {
                        if (callback != null) callback.onError("No more series found");
                    }
                } else {
                    if (callback != null) callback.onError("Failed to fetch series");
                }
            } catch (IOException e) {
                e.printStackTrace();
                if (callback != null) callback.onError(e.getMessage());
            }
        });
    }

    public void fetchNextOnTheAirSeries(com.lineargs.watchnext.utils.NetworkStateCallback callback) {
        if (callback != null) callback.onLoading();
        WatchNextDatabase.databaseWriteExecutor.execute(() -> {
            android.content.SharedPreferences preferences = android.preference.PreferenceManager.getDefaultSharedPreferences(application);
            int page = preferences.getInt("pref_series_on_the_air_next_page", 2);

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("https://api.themoviedb.org/3/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            SeriesApiService service = retrofit.create(SeriesApiService.class);

            try {
                Call<Series> call = service.getSeries("on_the_air", BuildConfig.MOVIE_DATABASE_API_KEY, page);
                Response<Series> response = call.execute();
                if (response.isSuccessful() && response.body() != null) {
                    List<com.lineargs.watchnext.utils.retrofit.series.SeriesResult> results = response.body().getResults();
                    if (results != null && !results.isEmpty()) {
                        for (com.lineargs.watchnext.utils.retrofit.series.SeriesResult result : results) {
                            OnTheAirSerie serie = new OnTheAirSerie();
                            serie.setTmdbId(result.getId());
                            serie.setTitle(result.getName());
                            serie.setOverview(result.getOverview());
                            serie.setVoteAverage(com.lineargs.watchnext.utils.MovieUtils.getNormalizedVoteAverage(String.valueOf(result.getVoteAverage())));
                            serie.setReleaseDate(result.getFirstAirDate());
                            serie.setPosterPath("https://image.tmdb.org/t/p/w500/" + result.getPosterPath());
                            serie.setBackdropPath("https://image.tmdb.org/t/p/w500/" + result.getBackdropPath());
                            serie.setOriginalLanguage(result.getOriginalLanguage());
                            serie.setOriginalTitle(result.getOriginalName());
                            seriesDao.insertOnTheAirSerie(serie);
                        }
                        preferences.edit().putInt("pref_series_on_the_air_next_page", page + 1).apply();
                        if (callback != null) callback.onSuccess();
                    } else {
                        if (callback != null) callback.onError("No more series found");
                    }
                } else {
                    if (callback != null) callback.onError("Failed to fetch series");
                }
            } catch (IOException e) {
                e.printStackTrace();
                if (callback != null) callback.onError(e.getMessage());
            }
        });
    }
}
