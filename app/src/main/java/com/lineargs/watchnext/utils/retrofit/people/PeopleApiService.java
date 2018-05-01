package com.lineargs.watchnext.utils.retrofit.people;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by goranminov on 29/11/2017.
 * <p>
 * {@link retrofit2.Retrofit} interface for our GET methods
 */

public interface PeopleApiService {

    /**
     * Method of type retrofit GET
     * @param id The ID
     * @param apiKey The API KEY
     * @return The URL for the network call
     */
    @GET("person/{person_id}")
    Call<Person> getPerson(
            @Path("person_id") String id,
            @Query("api_key") String apiKey);
}
