package com.lineargs.watchnext.utils.retrofit.videos;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by goranminov on 26/11/2017.
 * <p>
 * JSON POJO class for our {@link retrofit2.converter.gson.GsonConverterFactory}
 */

public class Videos {

    @SerializedName("id")
    @Expose
    private int id;

    @SerializedName("results")
    @Expose
    private List<VideosResult> results = null;

    public int getId() {
        return id;
    }

    public List<VideosResult> getResults() {
        return results;
    }

}
