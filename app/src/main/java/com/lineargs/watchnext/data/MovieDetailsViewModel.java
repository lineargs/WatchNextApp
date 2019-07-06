package com.lineargs.watchnext.data;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.lineargs.watchnext.BuildConfig;
import com.lineargs.watchnext.utils.retrofit.movies.MovieApiService;
import com.lineargs.watchnext.utils.retrofit.movies.moviedetail.Genre;
import com.lineargs.watchnext.utils.retrofit.movies.moviedetail.MovieDetail;
import com.lineargs.watchnext.utils.retrofit.movies.moviedetail.ProductionCompany;
import com.lineargs.watchnext.utils.retrofit.movies.moviedetail.ProductionCountry;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MovieDetailsViewModel extends AndroidViewModel {

    private static final String BASE_URL = "https://api.themoviedb.org/3/";
    private static final String APPEND_TO_RESPONSE = "videos,reviews,credits";
    private static final Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    private WatchNextRepository repository;
    private MutableLiveData<Movies> movieDetails;

    public MovieDetailsViewModel(@NonNull Application application) {
        super(application);
        repository = new WatchNextRepository(application);
    }

    public LiveData<Movies> getMovie(int tmdbId) {
        return repository.getMovie(tmdbId);
    }

    public MutableLiveData<Movies> getMovieDetails(String id) {
        if (movieDetails == null) {
            movieDetails = new MutableLiveData<>();
            syncMovieDetails(id);
        }
        return movieDetails;
    }

    private void syncMovieDetails(String id) {
        MovieApiService MOVIE_API_SERVICE = retrofit.create(MovieApiService.class);
        Call<MovieDetail> call = MOVIE_API_SERVICE.getMovieDetail(id, BuildConfig.MOVIE_DATABASE_API_KEY, APPEND_TO_RESPONSE);
        call.enqueue(new Callback<MovieDetail>() {
            @Override
            public void onResponse(@NonNull Call<MovieDetail> call, @NonNull final Response<MovieDetail> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Movies movie = new Movies();
                    movie.setTmdbId(response.body().getId());
                    movie.setImdbId(response.body().getImdbId());
                    movie.setHomepage(response.body().getHomepage());
                    movie.setProductionCompanies(buildCompaniesString(response.body().getProductionCompanies()));
                    movie.setProductionCountries(buildCountriesString(response.body().getProductionCountries()));
                    movie.setGenres(buildGenresString(response.body().getGenres()));
                    movie.setRuntime(response.body().getRuntime());
                    movie.setStatus(response.body().getStatus());
                    repository.updateMovie(movie);
                    repository.insertVideos(response.body().getVideos(), response.body().getId());

                } else if (response.errorBody() != null) {
                    response.errorBody().close();
                }
            }

            @Override
            public void onFailure(@NonNull Call<MovieDetail> call, @NonNull Throwable t) {

            }
        });
    }

    /**
     * Helper method used for building Companies String
     *
     * @param companies List of type ProductionCompany
     * @return String in following format: Google, Google, Google
     */
    private static String buildCompaniesString(List<ProductionCompany> companies) {

        if (companies == null || companies.isEmpty()) {
            return null;
        }

        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < companies.size(); i++) {
            ProductionCompany company = companies.get(i);
            stringBuilder.append(company.getName());
            if (i + 1 < companies.size()) {
                stringBuilder.append(", ");
            }
        }
        return stringBuilder.toString();
    }

    /**
     * Helper method used for building Countries String
     *
     * @param countries List of type ProductionCountry
     * @return String in following format: Macedonia, England, United States
     */
    private static String buildCountriesString(List<ProductionCountry> countries) {

        if (countries == null || countries.isEmpty()) {
            return null;
        }

        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < countries.size(); i++) {
            ProductionCountry country = countries.get(i);
            stringBuilder.append(country.getName());
            if (i + 1 < countries.size()) {
                stringBuilder.append(", ");
            }
        }
        return stringBuilder.toString();
    }

    /**
     * Helper method used for building Genres String
     *
     * @param genres List of type Genre
     * @return String in following format: Comedy, Horror, Fantasy
     */
    private static String buildGenresString(List<Genre> genres) {

        if (genres == null || genres.isEmpty()) {
            return null;
        }

        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < genres.size(); i++) {
            Genre genre = genres.get(i);
            stringBuilder.append(genre.getName());
            if (i + 1 < genres.size()) {
                stringBuilder.append(", ");
            }
        }
        return stringBuilder.toString();
    }
}
