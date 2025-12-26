package com.lineargs.watchnext.utils.retrofit.trending;

import com.lineargs.watchnext.utils.retrofit.movies.Movies;
import com.lineargs.watchnext.utils.retrofit.series.Series;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface TrendingApiService {

    @GET("trending/movie/{time_window}")
    Call<Movies> getTrendingMovies(
            @Path("time_window") String timeWindow,
            @Query("api_key") String apiKey,
            @Query("page") int page);

    @GET("trending/tv/{time_window}")
    Call<Series> getTrendingSeries(
            @Path("time_window") String timeWindow,
            @Query("api_key") String apiKey,
            @Query("page") int page);

    @GET("trending/person/{time_window}")
    Call<TrendingPeopleResponse> getTrendingPeople(
            @Path("time_window") String timeWindow,
            @Query("api_key") String apiKey,
            @Query("page") int page);
}
