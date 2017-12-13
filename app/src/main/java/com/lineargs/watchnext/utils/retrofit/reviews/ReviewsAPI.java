package com.lineargs.watchnext.utils.retrofit.reviews;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by goranminov on 22/11/2017.
 * <p>
 * {@link retrofit2.Retrofit} interface for our GET methods
 */

public interface ReviewsAPI {

    @GET("movie/{movie_id}/reviews")
    Call<Reviews> getReviews(
            @Path("movie_id") String id,
            @Query("api_key") String apiKey);
}
