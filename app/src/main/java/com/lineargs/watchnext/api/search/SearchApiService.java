package com.lineargs.watchnext.api.search;

import com.lineargs.watchnext.api.movies.Movies;
import com.lineargs.watchnext.api.movies.Result;
import com.lineargs.watchnext.api.series.Series;
import com.lineargs.watchnext.api.series.SeriesDetails;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by goranminov on 06/11/2017.
 * <p>
 * {@link retrofit2.Retrofit} interface for our GET methods
 */

public interface SearchApiService {

    @GET("search/{path}")
    Call<Search> multiSearch (
      @Path("path") String path,
      @Query("api_key") String apiKey,
      @Query("query") String query,
      @Query("include_adult") boolean adult);

    @GET("search/{path}")
    Call<Movies> searchMovies(
            @Path("path") String path,
            @Query("api_key") String apiKey,
            @Query("query") String query,
            @Query("include_adult") boolean adult);

    @GET("search/{path}")
    Call<Series> searchTV(
            @Path("path") String path,
            @Query("api_key") String apiKey,
            @Query("query") String query,
            @Query("include_adult") boolean adult);

    @GET("movie/{movie_id}")
    Call<Result> getMovie(
            @Path("movie_id") String id,
            @Query("api_key") String apiKey);

    @GET("tv/{tv_id}")
    Call<SeriesDetails> getTV(
            @Path("tv_id") String id,
            @Query("api_key") String apiKey);
}
