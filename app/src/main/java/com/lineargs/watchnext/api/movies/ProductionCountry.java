package com.lineargs.watchnext.api.movies;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by goranminov on 20/12/2017.
 * <p>
 * JSON POJO class for our {@link retrofit2.converter.gson.GsonConverterFactory}
 */

public class ProductionCountry {

    @SerializedName("iso_3166_1")
    @Expose
    private String iso31661;

    @SerializedName("name")
    @Expose
    private String name;

    public String getIso31661() {
        return iso31661;
    }

    public String getName() {
        return name;
    }
}