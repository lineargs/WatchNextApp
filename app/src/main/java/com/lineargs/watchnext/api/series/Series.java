package com.lineargs.watchnext.api.series;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by goranminov on 03/11/2017.
 * <p>
 * JSON POJO class for our {@link retrofit2.converter.gson.GsonConverterFactory}
 */

public class Series {

    @SerializedName("page")
    @Expose
    private int page;

    @SerializedName("total_results")
    @Expose
    private int totalResults;

    @SerializedName("total_pages")
    @Expose
    private int totalPages;

    @SerializedName("results")
    @Expose
    private List<SeriesDetails> results = null;

    public int getPage() {
        return page;
    }

    public int getTotalResults() {
        return totalResults;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public List<SeriesDetails> getResults() {
        return results;
    }
}
