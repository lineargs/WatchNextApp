package com.lineargs.watchnext.utils.retrofit.search;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.List;

public class MultiSearchResult {
    @SerializedName("id")
    @Expose
    private int id;

    @SerializedName("media_type")
    @Expose
    private String mediaType;

    @SerializedName("title")
    @Expose
    private String title;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("poster_path")
    @Expose
    private String posterPath;

    @SerializedName("overview")
    @Expose
    private String overview;

    @SerializedName("backdrop_path")
    @Expose
    private String backdropPath;

    @SerializedName("vote_average")
    @Expose
    private double voteAverage;

    @SerializedName("release_date")
    @Expose
    private String releaseDate;

    @SerializedName("first_air_date")
    @Expose
    private String firstAirDate;
    
    // Getters
    public int getId() { return id; }
    public String getMediaType() { return mediaType; }
    // Helper to get title regardless of type
    public String getTitle() { return title != null ? title : name; }
    public String getPosterPath() { return posterPath; }
    public String getOverview() { return overview; }
    public String getBackdropPath() { return backdropPath; }
    public double getVoteAverage() { return voteAverage; }
    public String getReleaseDate() { return releaseDate != null ? releaseDate : firstAirDate; }
}
