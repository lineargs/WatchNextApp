package com.lineargs.watchnext.api.series.seasondetails;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by goranminov on 28/11/2017.
 * <p>
 * JSON POJO class for our {@link retrofit2.converter.gson.GsonConverterFactory}
 */

public class SeasonDetails {

    @SerializedName("_id")
    @Expose
    private String _id;

    @SerializedName("air_date")
    @Expose
    private String airDate;

    @SerializedName("episodes")
    @Expose
    private List<Episode> episodes = null;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("overview")
    @Expose
    private String overview;

    @SerializedName("id")
    @Expose
    private int id;

    @SerializedName("poster_path")
    @Expose
    private String posterPath;

    @SerializedName("season_number")
    @Expose
    private int seasonNumber;

    public String get_id() {
        return _id;
    }

    public String getAirDate() {
        return airDate;
    }

    public List<Episode> getEpisodes() {
        return episodes;
    }

    public String getName() {
        return name;
    }

    public String getOverview() {
        return overview;
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
