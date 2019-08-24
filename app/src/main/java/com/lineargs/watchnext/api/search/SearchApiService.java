package com.lineargs.watchnext.api.search;

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
    Call<Search> multiSearch(
            @Path("path") String path,
            @Query("api_key") String apiKey,
            @Query("query") String query,
            @Query("include_adult") boolean adult);
}
