package com.lineargs.watchnext.data;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.lineargs.watchnext.BuildConfig;
import com.lineargs.watchnext.api.movies.MovieApiService;
import com.lineargs.watchnext.api.movies.MovieDetails;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.lineargs.watchnext.utils.RoomUtils.buildCompaniesString;
import static com.lineargs.watchnext.utils.RoomUtils.buildCountriesString;
import static com.lineargs.watchnext.utils.RoomUtils.buildGenresString;

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
        Call<MovieDetails> call = MOVIE_API_SERVICE.getMovieDetail(id, BuildConfig.MOVIE_DATABASE_API_KEY, APPEND_TO_RESPONSE);
        call.enqueue(new Callback<MovieDetails>() {
            @Override
            public void onResponse(@NonNull Call<MovieDetails> call, @NonNull final Response<MovieDetails> response) {
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
                    repository.insertReviews(response.body().getReviews(), response.body().getId());
                    repository.insertCast(response.body().getCredits(), response.body().getId());
                    repository.insertCrew(response.body().getCredits(), response.body().getId());

                } else if (response.errorBody() != null) {
                    response.errorBody().close();
                }
            }

            @Override
            public void onFailure(@NonNull Call<MovieDetails> call, @NonNull Throwable t) {

            }
        });
    }

    public LiveData<List<Credits>> getCast(int tmdbId) {return repository.getCast(tmdbId);}
    public LiveData<List<Credits>> getCrew(int tmdbId) {return repository.getCrew(tmdbId);}
}
