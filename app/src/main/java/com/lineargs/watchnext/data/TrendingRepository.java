package com.lineargs.watchnext.data;

import androidx.lifecycle.MutableLiveData;
import com.lineargs.watchnext.BuildConfig;
import com.lineargs.watchnext.data.entity.PopularMovie;
import com.lineargs.watchnext.data.entity.PopularSerie;
import com.lineargs.watchnext.utils.retrofit.movies.Movies;
import com.lineargs.watchnext.utils.retrofit.movies.Result;
import com.lineargs.watchnext.utils.retrofit.people.Person;
import com.lineargs.watchnext.utils.retrofit.series.Series;
import com.lineargs.watchnext.utils.retrofit.series.SeriesResult;
import com.lineargs.watchnext.utils.retrofit.trending.TrendingApiService;
import com.lineargs.watchnext.utils.retrofit.trending.TrendingPeopleResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.util.ArrayList;
import java.util.List;

public class TrendingRepository {

    private final TrendingApiService apiService;
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>();
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();

    public TrendingRepository() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.themoviedb.org/3/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        apiService = retrofit.create(TrendingApiService.class);
    }

    public MutableLiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    public MutableLiveData<String> getErrorMessage() {
        return errorMessage;
    }

    public MutableLiveData<List<PopularMovie>> getTrendingMovies() {
        MutableLiveData<List<PopularMovie>> data = new MutableLiveData<>();
        isLoading.setValue(true);
        apiService.getTrendingMovies("day", BuildConfig.MOVIE_DATABASE_API_KEY).enqueue(new Callback<Movies>() {
            @Override
            public void onResponse(Call<Movies> call, Response<Movies> response) {
                isLoading.setValue(false);
                if (response.isSuccessful() && response.body() != null) {
                    List<PopularMovie> movies = new ArrayList<>();
                    for (Result result : response.body().getResults()) {
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
                        movies.add(movie);
                    }
                    data.setValue(movies);
                } else {
                    errorMessage.setValue("Failed to fetch trending movies");
                }
            }

            @Override
            public void onFailure(Call<Movies> call, Throwable t) {
                isLoading.setValue(false);
                errorMessage.setValue(t.getMessage());
            }
        });
        return data;
    }

    public MutableLiveData<List<PopularSerie>> getTrendingSeries() {
        MutableLiveData<List<PopularSerie>> data = new MutableLiveData<>();
        isLoading.setValue(true);
        apiService.getTrendingSeries("day", BuildConfig.MOVIE_DATABASE_API_KEY).enqueue(new Callback<Series>() {
            @Override
            public void onResponse(Call<Series> call, Response<Series> response) {
                isLoading.setValue(false);
                if (response.isSuccessful() && response.body() != null) {
                    List<PopularSerie> series = new ArrayList<>();
                    for (SeriesResult result : response.body().getResults()) {
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
                        series.add(serie);
                    }
                    data.setValue(series);
                } else {
                    errorMessage.setValue("Failed to fetch trending series");
                }
            }

            @Override
            public void onFailure(Call<Series> call, Throwable t) {
                isLoading.setValue(false);
                errorMessage.setValue(t.getMessage());
            }
        });
        return data;
    }

    public MutableLiveData<List<Person>> getTrendingPeople() {
        MutableLiveData<List<Person>> data = new MutableLiveData<>();
        isLoading.setValue(true);
        apiService.getTrendingPeople("day", BuildConfig.MOVIE_DATABASE_API_KEY).enqueue(new Callback<TrendingPeopleResponse>() {
            @Override
            public void onResponse(Call<TrendingPeopleResponse> call, Response<TrendingPeopleResponse> response) {
                isLoading.setValue(false);
                if (response.isSuccessful() && response.body() != null) {
                    data.setValue(response.body().getResults());
                } else {
                    errorMessage.setValue("Failed to fetch trending people");
                }
            }

            @Override
            public void onFailure(Call<TrendingPeopleResponse> call, Throwable t) {
                isLoading.setValue(false);
                errorMessage.setValue(t.getMessage());
            }
        });
        return data;
    }
}
