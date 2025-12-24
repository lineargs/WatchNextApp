package com.lineargs.watchnext.data.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import com.lineargs.watchnext.data.DataContract;

@Entity(tableName = DataContract.PopularMovieEntry.TABLE_NAME,
        indices = {@Index(value = {DataContract.PopularMovieEntry.COLUMN_MOVIE_ID}, unique = true)})
public class PopularMovie implements Movie {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = DataContract.PopularMovieEntry._ID)
    private long id;

    @ColumnInfo(name = DataContract.PopularMovieEntry.COLUMN_MOVIE_ID)
    private int tmdbId;

    @ColumnInfo(name = DataContract.PopularMovieEntry.COLUMN_TITLE)
    private String title;

    @ColumnInfo(name = DataContract.PopularMovieEntry.COLUMN_RELEASE_DATE)
    private String releaseDate;

    @ColumnInfo(name = DataContract.PopularMovieEntry.COLUMN_OVERVIEW)
    private String overview;

    @ColumnInfo(name = DataContract.PopularMovieEntry.COLUMN_VOTE_AVERAGE)
    private String voteAverage;

    @ColumnInfo(name = DataContract.PopularMovieEntry.COLUMN_POSTER_PATH)
    private String posterPath;

    @ColumnInfo(name = DataContract.PopularMovieEntry.COLUMN_BACKDROP_PATH)
    private String backdropPath;

    @ColumnInfo(name = DataContract.PopularMovieEntry.COLUMN_ORIGINAL_LANGUAGE)
    private String originalLanguage;

    @ColumnInfo(name = DataContract.PopularMovieEntry.COLUMN_ORIGINAL_TITLE)
    private String originalTitle;

    @ColumnInfo(name = DataContract.PopularMovieEntry.COLUMN_IMDB_ID, defaultValue = "0")
    private String imdbId;

    @ColumnInfo(name = DataContract.PopularMovieEntry.COLUMN_HOMEPAGE, defaultValue = "0")
    private String homepage;

    @ColumnInfo(name = DataContract.PopularMovieEntry.COLUMN_PRODUCTION_COMPANIES, defaultValue = "0")
    private String productionCompanies;

    @ColumnInfo(name = DataContract.PopularMovieEntry.COLUMN_PRODUCTION_COUNTRIES, defaultValue = "0")
    private String productionCountries;

    @ColumnInfo(name = DataContract.PopularMovieEntry.COLUMN_RUNTIME)
    private Integer runtime;

    @ColumnInfo(name = DataContract.PopularMovieEntry.COLUMN_STATUS)
    private String status;

    @ColumnInfo(name = DataContract.PopularMovieEntry.COLUMN_GENRES)
    private String genres;

    // Getters and Setters
    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public int getTmdbId() { return tmdbId; }
    public void setTmdbId(int tmdbId) { this.tmdbId = tmdbId; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getReleaseDate() { return releaseDate; }
    public void setReleaseDate(String releaseDate) { this.releaseDate = releaseDate; }

    public String getOverview() { return overview; }
    public void setOverview(String overview) { this.overview = overview; }

    public String getVoteAverage() { return voteAverage; }
    public void setVoteAverage(String voteAverage) { this.voteAverage = voteAverage; }

    public String getPosterPath() { return posterPath; }
    public void setPosterPath(String posterPath) { this.posterPath = posterPath; }

    public String getBackdropPath() { return backdropPath; }
    public void setBackdropPath(String backdropPath) { this.backdropPath = backdropPath; }

    public String getOriginalLanguage() { return originalLanguage; }
    public void setOriginalLanguage(String originalLanguage) { this.originalLanguage = originalLanguage; }

    public String getOriginalTitle() { return originalTitle; }
    public void setOriginalTitle(String originalTitle) { this.originalTitle = originalTitle; }

    public String getImdbId() { return imdbId; }
    public void setImdbId(String imdbId) { this.imdbId = imdbId; }

    public String getHomepage() { return homepage; }
    public void setHomepage(String homepage) { this.homepage = homepage; }

    public String getProductionCompanies() { return productionCompanies; }
    public void setProductionCompanies(String productionCompanies) { this.productionCompanies = productionCompanies; }

    public String getProductionCountries() { return productionCountries; }
    public void setProductionCountries(String productionCountries) { this.productionCountries = productionCountries; }

    public Integer getRuntime() { return runtime; }
    public void setRuntime(Integer runtime) { this.runtime = runtime; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getGenres() { return genres; }
    public void setGenres(String genres) { this.genres = genres; }
}
