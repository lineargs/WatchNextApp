package com.lineargs.watchnext.utils.retrofit.series;

import com.lineargs.watchnext.utils.retrofit.series.seasondetails.SeasonDetails;
import com.lineargs.watchnext.utils.retrofit.series.seriesdetails.SeriesDetails;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by goranminov on 03/11/2017.
 * <p>
 * {@link retrofit2.Retrofit} interface for our GET methods
 */

public interface SeriesApiService {

    @GET("tv/{path}")
    Call<Series> getSeries(
            @Path("path") String path,
            @Query("api_key") String apiKey);

    @GET("tv/{tv_id}")
    Call<SeriesDetails> getDetails(
            @Path("tv_id") String id,
            @Query("api_key") String apiKey,
            @Query ("append_to_response") String appendToResponse);

    @GET("tv/{tv_id}/season/{season_number}")
    Call<SeasonDetails> getSeason(
            @Path("tv_id") String id,
            @Path("season_number") String seasonNumber,
            @Query("api_key") String apiKey);
}
