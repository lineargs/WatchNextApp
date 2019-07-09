package com.lineargs.watchnext.api.videos;

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
    private List<VideoDetails> results = null;

    public int getId() {
        return id;
    }

    public List<VideoDetails> getResults() {
        return results;
    }

}
