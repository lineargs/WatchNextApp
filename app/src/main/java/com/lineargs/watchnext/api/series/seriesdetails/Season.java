package com.lineargs.watchnext.api.series.seriesdetails;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by goranminov on 27/11/2017.
 * <p>
 * JSON POJO class for our {@link retrofit2.converter.gson.GsonConverterFactory}
 */

public class Season {

    @SerializedName("air_date")
    @Expose
    private String airDate;

    @SerializedName("episode_count")
    @Expose
    private int episodeCount;

    @SerializedName("id")
    @Expose
    private int id;

    @SerializedName("poster_path")
    @Expose
    private String posterPath;

    @SerializedName("season_number")
    @Expose
    private int seasonNumber;

    public String getAirDate() {
        return airDate;
    }

    public int getEpisodeCount() {
        return episodeCount;
    }

    public int getId() {
        return id;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public int getSeasonNumber() {
        return seasonNumber;
    }
}
