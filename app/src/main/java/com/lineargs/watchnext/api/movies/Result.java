package com.lineargs.watchnext.api.movies;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by goranminov on 14/10/2017.
 * <p>
 * JSON POJO class for our {@link retrofit2.converter.gson.GsonConverterFactory}
 */

public class Result {

    @SerializedName("vote_count")
    @Expose
    private int voteCount;

    @SerializedName("id")
    @Expose
    private int id;

    @SerializedName("vote_average")
    @Expose
    private double voteAverage;

    @SerializedName("title")
    @Expose
    private String title;

    @SerializedName("poster_path")
    @Expose
    private String posterPath;

    @SerializedName("original_language")
    @Expose
    private String originalLanguage;

    @SerializedName("original_title")
    @Expose
    private String originalTitle;

    @SerializedName("genre_ids")
    @Expose
    private List<Integer> genreIds = null;

    @SerializedName("backdrop_path")
    @Expose
    private String backdropPath;

    @SerializedName("overview")
    @Expose
    private String overview;

    @SerializedName("release_date")
    @Expose
    private String releaseDate;

    public int getVoteCount() {
        return voteCount;
    }

    public int getId() {
        return id;
    }

    public double getVoteAverage() {
        return voteAverage;
    }

    public String getTitle() {
        return title;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public String getOriginalLanguage() {
        return originalLanguage;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public List<Integer> getGenreIds() {
        return genreIds;
    }

    public String getBackdropPath() {
        return backdropPath;
    }

    public String getOverview() {
        return overview;
    }

    public String getReleaseDate() {
        return releaseDate;
    }
}
