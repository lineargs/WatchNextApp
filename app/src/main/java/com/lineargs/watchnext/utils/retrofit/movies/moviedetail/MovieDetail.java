package com.lineargs.watchnext.utils.retrofit.movies.moviedetail;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.lineargs.watchnext.utils.retrofit.credits.Credits;
import com.lineargs.watchnext.utils.retrofit.videos.Videos;

import java.util.List;

public class MovieDetail {

    @SerializedName("backdrop_path")
    @Expose
    private String backdropPath;

    @SerializedName("genres")
    @Expose
    private List<Genre> genres = null;

    @SerializedName("homepage")
    @Expose
    private String homepage;

    @SerializedName("id")
    @Expose
    private int id;

    @SerializedName("imdb_id")
    @Expose
    private String imdbId;

    @SerializedName("original_language")
    @Expose
    private String originalLanguage;

    @SerializedName("original_title")
    @Expose
    private String originalTitle;

    @SerializedName("overview")
    @Expose
    private String overview;

    @SerializedName("poster_path")
    @Expose
    private String posterPath;

    @SerializedName("production_companies")
    @Expose
    private List<ProductionCompany> productionCompanies = null;

    @SerializedName("production_countries")
    @Expose
    private List<ProductionCountry> productionCountries = null;

    @SerializedName("release_date")
    @Expose
    private String releaseDate;

    @SerializedName("runtime")
    @Expose
    private int runtime;

    @SerializedName("status")
    @Expose
    private String status;

    @SerializedName("tagline")
    @Expose
    private String tagline;

    @SerializedName("title")
    @Expose
    private String title;

    @SerializedName("vote_average")
    @Expose
    private double voteAverage;

    @SerializedName("vote_count")
    @Expose
    private int voteCount;

    @SerializedName("videos")
    @Expose
    private Videos videos;

    @SerializedName("reviews")
    @Expose
    private Reviews reviews;

    @SerializedName("images")
    @Expose
    private Images images;

    @SerializedName("credits")
    @Expose
    private Credits credits;

    public String getBackdropPath() {
        return backdropPath;
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

    public String getImdbId() {
        return imdbId;
    }

    public String getOriginalLanguage() {
        return originalLanguage;
    }

    public String getOriginalTitle() {
        return originalTitle;
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

    public List<ProductionCountry> getProductionCountries() {
        return productionCountries;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public int getRuntime() {
        return runtime;
    }

    public String getStatus() {
        return status;
    }

    public String getTagline() {
        return tagline;
    }

    public String getTitle() {
        return title;
    }

    public double getVoteAverage() {
        return voteAverage;
    }

    public int getVoteCount() {
        return voteCount;
    }

    public Videos getVideos() {
        return videos;
    }

    public Reviews getReviews() {
        return reviews;
    }

    public Images getImages() {
        return images;
    }

    public Credits getCredits() {
        return credits;
    }
}