package com.lineargs.watchnext.api.series.seriesdetails;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.lineargs.watchnext.api.credits.Credits;
import com.lineargs.watchnext.api.movies.Genre;
import com.lineargs.watchnext.api.movies.ProductionCompany;
import com.lineargs.watchnext.api.videos.Videos;

import java.util.List;

/**
 * Created by goranminov on 27/11/2017.
 * <p>
 * JSON POJO class for our {@link retrofit2.converter.gson.GsonConverterFactory}
 */

public class SeriesDetails {


    @SerializedName("backdrop_path")
    @Expose
    private String backdropPath;

    @SerializedName("created_by")
    @Expose
    private List<CreatedBy> createdBy = null;

    @SerializedName("first_air_date")
    @Expose
    private String firstAirDate;

    @SerializedName("genres")
    @Expose
    private List<Genre> genres = null;

    @SerializedName("homepage")
    @Expose
    private String homepage;

    @SerializedName("id")
    @Expose
    private int id;

    @SerializedName("in_production")
    @Expose
    private boolean inProduction;

    @SerializedName("languages")
    @Expose
    private List<String> languages = null;

    @SerializedName("last_air_date")
    @Expose
    private String lastAirDate;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("networks")
    @Expose
    private List<Network> networks = null;

    @SerializedName("number_of_episodes")
    @Expose
    private int numberOfEpisodes;

    @SerializedName("number_of_seasons")
    @Expose
    private int numberOfSeasons;

    @SerializedName("origin_country")
    @Expose
    private List<String> originCountry = null;

    @SerializedName("original_language")
    @Expose
    private String originalLanguage;

    @SerializedName("original_name")
    @Expose
    private String originalName;

    @SerializedName("overview")
    @Expose
    private String overview;

    @SerializedName("poster_path")
    @Expose
    private String posterPath;

    @SerializedName("production_companies")
    @Expose
    private List<ProductionCompany> productionCompanies = null;

    @SerializedName("seasons")
    @Expose
    private List<Season> seasons = null;

    @SerializedName("status")
    @Expose
    private String status;

    @SerializedName("type")
    @Expose
    private String type;

    @SerializedName("vote_average")
    @Expose
    private double voteAverage;

    @SerializedName("vote_count")
    @Expose
    private int voteCount;

    @SerializedName("credits")
    @Expose
    private Credits credits;

    @SerializedName("videos")
    @Expose
    private Videos videos;

    public String getBackdropPath() {
        return backdropPath;
    }

    public List<CreatedBy> getCreatedBy() {
        return createdBy;
    }

    public String getFirstAirDate() {
        return firstAirDate;
    }

    public List<Genre> getGenres() {
        return genres;
    }

    public String getHomepage() {
        return homepage;
    }

    public int getId() {
        return id;
    }

    public boolean isInProduction() {
        return inProduction;
    }

    public List<String> getLanguages() {
        return languages;
    }

    public String getLastAirDate() {
        return lastAirDate;
    }

    public String getName() {
        return name;
    }

    public List<Network> getNetworks() {
        return networks;
    }

    public int getNumberOfSeasons() {
        return numberOfSeasons;
    }

    public List<String> getOriginCountry() {
        return originCountry;
    }

    public String getOriginalLanguage() {
        return originalLanguage;
    }

    public String getOriginalName() {
        return originalName;
    }

    public String getOverview() {
        return overview;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public List<ProductionCompany> getProductionCompanies() {
        return productionCompanies;
    }

    public List<Season> getSeasons() {
        return seasons;
    }

    public String getStatus() {
        return status;
    }

    public String getType() {
        return type;
    }

    public double getVoteAverage() {
        return voteAverage;
    }

    public int getVoteCount() {
        return voteCount;
    }

    public Credits getCredits() {
        return credits;
    }

    public Videos getVideos() {
        return videos;
    }
}
