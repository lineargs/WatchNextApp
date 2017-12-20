package com.lineargs.watchnext.utils.retrofit.movies.moviedetail;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by goranminov on 20/12/2017.
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

    public void setName(String name) {
        this.name = name;
    }

    public ProductionCompany withName(String name) {
        this.name = name;
        return this;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ProductionCompany withId(int id) {
        this.id = id;
        return this;
    }
}
