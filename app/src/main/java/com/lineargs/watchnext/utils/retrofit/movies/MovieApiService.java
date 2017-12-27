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

    @GET("movie/{path}")
    Call<Movies> getMovies(
            @Path("path") String path,
            @Query("api_key") String apiKey,
            @Query("language") String language,
            @Query("region") String region);

    @GET("movie/{movie_id}")
    Call<MovieDetail> getMovieDetail(
            @Path("movie_id") String movieId,
            @Query("api_key") String apiKey,
            @Query("language") String language,
            @Query("append_to_response") String appendToResponse
    );
}
