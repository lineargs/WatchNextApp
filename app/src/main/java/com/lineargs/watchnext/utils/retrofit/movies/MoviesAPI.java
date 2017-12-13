package com.lineargs.watchnext.utils.retrofit.movies;

import com.lineargs.watchnext.utils.retrofit.credits.MovieCredits;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by goranminov on 14/10/2017.
 * <p>
 * {@link retrofit2.Retrofit} interface for our GET methods
 */

public interface MoviesAPI {

    @GET("movie/{path}")
    Call<Movies> getMovies(
            @Path("path") String path,
            @Query("api_key") String apiKey);

    @GET("movie/{movie_id}/credits")
    Call<MovieCredits> getMovieCredits(
            @Path("movie_id") String movieId,
            @Query("api_key") String apiKey);
}
