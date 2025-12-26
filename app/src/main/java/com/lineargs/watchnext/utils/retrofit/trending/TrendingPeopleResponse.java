package com.lineargs.watchnext.utils.retrofit.trending;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.lineargs.watchnext.utils.retrofit.people.Person;

import java.util.List;

public class TrendingPeopleResponse {

    @SerializedName("page")
    @Expose
    private int page;

    @SerializedName("results")
    @Expose
    private List<Person> results = null;

    @SerializedName("total_pages")
    @Expose
    private int totalPages;

    @SerializedName("total_results")
    @Expose
    private int totalResults;

    public int getPage() {
        return page;
    }

    public List<Person> getResults() {
        return results;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public int getTotalResults() {
        return totalResults;
    }
}
