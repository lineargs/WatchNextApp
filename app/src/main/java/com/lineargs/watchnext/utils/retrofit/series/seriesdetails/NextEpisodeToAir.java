package com.lineargs.watchnext.utils.retrofit.series.seriesdetails;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class NextEpisodeToAir {

    @SerializedName("air_date")
    @Expose
    private String airDate;

    @SerializedName("episode_number")
    @Expose
    private int episodeNumber;

    @SerializedName("id")
    @Expose
    private int id;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("season_number")
    @Expose
    private int seasonNumber;

    public String getAirDate() {
        return airDate;
    }

    public int getEpisodeNumber() {
        return episodeNumber;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getSeasonNumber() {
        return seasonNumber;
    }
}
