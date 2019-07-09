package com.lineargs.watchnext.api.movies;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by goranminov on 20/12/2017.
 * <p>
 * JSON POJO class for our {@link retrofit2.converter.gson.GsonConverterFactory}
 */

public class ProductionCompany {

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("id")
    @Expose
    private int id;

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }
}
