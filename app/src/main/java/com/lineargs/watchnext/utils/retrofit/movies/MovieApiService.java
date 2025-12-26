package com.lineargs.watchnext.utils.retrofit.movies;

import com.lineargs.watchnext.utils.retrofit.movies.moviedetail.MovieDetail;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by goranminov on 14/10/2017.
 * <p>
 * {@link retrofit2.Retrofit} interface for our GET methods
 */

public interface MovieApiService {

    /**
     * Method of type retrofit GET
     * @param path The movie path
     * @param apiKey The API KEY
     * @param region The region from where we access the app
     * @return The URL for the network call
     */
    @GET("movie/{path}")
    Call<Movies> getMovies(
            @Path("path") String path,
            @Query("api_key") String apiKey,
            @Query("region") String region,
            @Query("page") int page);

    /**
     * Method of type retrofit GET
     * @param movieId The ID
     * @param apiKey The API KEY
     * @param appendToResponse The appendToResponse parameters
     * @return The URL for the network Call
     */
    @GET("movie/{movie_id}")
    Call<MovieDetail> getMovieDetail(
            @Path("movie_id") String movieId,
            @Query("api_key") String apiKey,
            @Query("append_to_response") String appendToResponse
    );

    /**
     * Method of type retrofit GET
     * @param movieId The ID
     * @param apiKey The API KEY
     * @return The URL for the network Call
     */
    @GET("movie/{movie_id}")
    Call<MovieDetail> updateMovie(
            @Path("movie_id") String movieId,
            @Query("api_key") String apiKey);
}
