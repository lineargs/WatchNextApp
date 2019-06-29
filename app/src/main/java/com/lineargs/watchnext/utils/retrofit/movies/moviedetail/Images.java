package com.lineargs.watchnext.utils.retrofit.movies.moviedetail;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by goranminov on 20/12/2017.
 * <p>
 * JSON POJO class for our {@link retrofit2.converter.gson.GsonConverterFactory}
 */

public class Images {

    @SerializedName("backdrops")
    @Expose
    private List<Object> backdrops = null;

    @SerializedName("posters")
    @Expose
    private List<Object> posters = null;

    public List<Object> getBackdrops() {
        return backdrops;
    }

    public List<Object> getPosters() {
        return posters;
    }
}
