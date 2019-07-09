package com.lineargs.watchnext.api.series.seasondetails;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.lineargs.watchnext.api.credits.Crew;

import java.util.List;

/**
 * Created by goranminov on 28/11/2017.
 * <p>
 * JSON POJO class for our {@link retrofit2.converter.gson.GsonConverterFactory}
 */

public class Episode {

    @SerializedName("air_date")
    @Expose
    private String airDate;

    @SerializedName("crew")
    @Expose
    private List<Crew> crew = null;

    @SerializedName("episode_number")
    @Expose
    private int episodeNumber;

    @SerializedName("guest_stars")
    @Expose
    private List<GuestStar> guestStars = null;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("overview")
    @Expose
    private String overview;

    @SerializedName("id")
    @Expose
    private int id;

    @SerializedName("production_code")
    @Expose
    private String productionCode;

    @SerializedName("season_number")
    @Expose
    private int seasonNumber;

    @SerializedName("still_path")
    @Expose
    private String stillPath;

    @SerializedName("vote_average")
    @Expose
    private double voteAverage;

    @SerializedName("vote_count")
    @Expose
    private int voteCount;

    public String getAirDate() {
        return airDate;
    }

    public List<Crew> getCrew() {
        return crew;
    }

    public int getEpisodeNumber() {
        return episodeNumber;
    }

    public List<GuestStar> getGuestStars() {
        return guestStars;
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

    public String getProductionCode() {
        return productionCode;
    }

    public int getSeasonNumber() {
        return seasonNumber;
    }

    public String getStillPath() {
        return stillPath;
    }

    public double getVoteAverage() {
        return voteAverage;
    }

    public int getVoteCount() {
        return voteCount;
    }
}
