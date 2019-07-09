package com.lineargs.watchnext.api.series;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by goranminov on 03/11/2017.
 * <p>
 * JSON POJO class for our {@link retrofit2.converter.gson.GsonConverterFactory}
 */

public class SeriesDetails {

    @SerializedName("original_name")
    @Expose
    private String originalName;

    @SerializedName("genre_ids")
    @Expose
    private List<Integer> genreIds = null;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("origin_country")
    @Expose
    private List<String> originCountry = null;

    @SerializedName("vote_count")
    @Expose
    private int voteCount;

    @SerializedName("first_air_date")
    @Expose
    private String firstAirDate;

    @SerializedName("backdrop_path")
    @Expose
    private String backdropPath;

    @SerializedName("original_language")
    @Expose
    private String originalLanguage;

    @SerializedName("id")
    @Expose
    private int id;

    @SerializedName("vote_average")
    @Expose
    private double voteAverage;

    @SerializedName("overview")
    @Expose
    private String overview;

    @SerializedName("poster_path")
    @Expose
    private String posterPath;

    public String getOriginalName() {
        return originalName;
    }

    public List<Integer> getGenreIds() {
        return genreIds;
    }

    public String getName() {
        return name;
    }

    public List<String> getOriginCountry() {
        return originCountry;
    }

    public int getVoteCount() {
        return voteCount;
    }

    public String getFirstAirDate() {
        return firstAirDate;
    }

    public String getBackdropPath() {
        return backdropPath;
    }

    public String getOriginalLanguage() {
        return originalLanguage;
    }

    public int getId() {
        return id;
    }

    public double getVoteAverage() {
        return voteAverage;
    }

    public String getOverview() {
        return overview;
    }

    public String getPosterPath() {
        return posterPath;
    }
}
