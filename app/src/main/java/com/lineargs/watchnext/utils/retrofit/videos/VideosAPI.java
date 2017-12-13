package com.lineargs.watchnext.utils.retrofit.videos;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by goranminov on 26/11/2017.
 * <p>
 * {@link retrofit2.Retrofit} interface for our GET methods
 */

public interface VideosAPI {

    @GET("movie/{movie_id}/videos")
    Call<Videos> getReviews(
            @Path("movie_id") String id,
            @Query("api_key") String apiKey);

    @GET("tv/{tv_id}/videos")
    Call<Videos> getTVReviews(
            @Path("tv_id") String id,
            @Query("api_key") String apiKey);
}
