package com.lineargs.watchnext.utils.retrofit.movies.moviedetail;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by goranminov on 20/12/2017.
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

    public void setBackdrops(List<Object> backdrops) {
        this.backdrops = backdrops;
    }

    public Images withBackdrops(List<Object> backdrops) {
        this.backdrops = backdrops;
        return this;
    }

    public List<Object> getPosters() {
        return posters;
    }

    public void setPosters(List<Object> posters) {
        this.posters = posters;
    }

    public Images withPosters(List<Object> posters) {
        this.posters = posters;
        return this;
    }

}
