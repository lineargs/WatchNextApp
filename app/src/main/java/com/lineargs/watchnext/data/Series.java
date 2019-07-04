package com.lineargs.watchnext.data;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(tableName = "series")
public class Series {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    private int id;

    @NonNull
    @ColumnInfo(name = "tmdb_id")
    private int tmdbId;

    @ColumnInfo(name = "vote_average")
    private String voteAverage;

    @NonNull
    private String title;

    @ColumnInfo(name = "poster_path")
    private String posterPath;

    @ColumnInfo(name = "backdrop_path")
    private String backdropPath;

    @NonNull
    private String overview;

    @ColumnInfo(name = "release_date")
    private String releaseDate;

    private String homepage;

    @ColumnInfo(name = "production_companies")
    private String productionCompanies;

    private String networks;

    private String status;

    private String genres;

    @NonNull
    private int type;

    public Series(int tmdbId, String voteAverage, String title, String posterPath, String backdropPath,
                  String overview, String releaseDate, String homepage, String productionCompanies,
                  String networks, String status, String genres, int type) {
        this.tmdbId = tmdbId;
        this.voteAverage = voteAverage;
        this.title = title;
        this.posterPath = posterPath;
        this.backdropPath = backdropPath;
        this.overview = overview;
        this.releaseDate = releaseDate;
        this.homepage = homepage;
        this.productionCompanies = productionCompanies;
        this.networks = networks;
        this.status = status;
        this.genres = genres;
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTmdbId() {
        return tmdbId;
    }

    public void setTmdbId(int tmdbId) {
        this.tmdbId = tmdbId;
    }

    public String getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(String voteAverage) {
        this.voteAverage = voteAverage;
    }

    @NonNull
    public String getTitle() {
        return title;
    }

    public void setTitle(@NonNull String title) {
        this.title = title;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public String getBackdropPath() {
        return backdropPath;
    }

    public void setBackdropPath(String backdropPath) {
        this.backdropPath = backdropPath;
    }

    @NonNull
    public String getOverview() {
        return overview;
    }

    public void setOverview(@NonNull String overview) {
        this.overview = overview;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getHomepage() {
        return homepage;
    }

    public void setHomepage(String homepage) {
        this.homepage = homepage;
    }

    public String getProductionCompanies() {
        return productionCompanies;
    }

    public void setProductionCompanies(String productionCompanies) {
        this.productionCompanies = productionCompanies;
    }

    public String getNetworks() {
        return networks;
    }

    public void setNetworks(String networks) {
        this.networks = networks;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getGenres() {
        return genres;
    }

    public void setGenres(String genres) {
        this.genres = genres;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
