package com.lineargs.watchnext.data.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import com.lineargs.watchnext.data.DataContract;

@Entity(tableName = DataContract.Search.TABLE_NAME)
public class Search implements Movie {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = DataContract.Search._ID)
    private long id;

    @ColumnInfo(name = DataContract.Search.COLUMN_MOVIE_ID)
    private int tmdbId;

    @ColumnInfo(name = DataContract.Search.COLUMN_TITLE)
    private String title;

    @ColumnInfo(name = DataContract.Search.COLUMN_POSTER_PATH)
    private String posterPath;

    @ColumnInfo(name = DataContract.Search.COLUMN_TYPE)
    private int mediaType;

    @ColumnInfo(name = DataContract.PopularMovieEntry.COLUMN_BACKDROP_PATH)
    private String backdropPath;

    @ColumnInfo(name = DataContract.PopularMovieEntry.COLUMN_OVERVIEW)
    private String overview;

    @ColumnInfo(name = DataContract.PopularMovieEntry.COLUMN_VOTE_AVERAGE)
    private String voteAverage;

    @ColumnInfo(name = DataContract.PopularMovieEntry.COLUMN_RELEASE_DATE)
    private String releaseDate;

    @ColumnInfo(name = DataContract.PopularMovieEntry.COLUMN_IMDB_ID)
    private String imdbId;

    @ColumnInfo(name = DataContract.PopularMovieEntry.COLUMN_RUNTIME)
    private Integer runtime;

    @ColumnInfo(name = DataContract.PopularMovieEntry.COLUMN_PRODUCTION_COMPANIES)
    private String productionCompanies;

    @ColumnInfo(name = DataContract.PopularMovieEntry.COLUMN_PRODUCTION_COUNTRIES)
    private String productionCountries;

    @ColumnInfo(name = DataContract.PopularMovieEntry.COLUMN_GENRES)
    private String genres;

    @ColumnInfo(name = DataContract.PopularMovieEntry.COLUMN_STATUS)
    private String status;

    // Getters and Setters
    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public int getTmdbId() { return tmdbId; }
    public void setTmdbId(int tmdbId) { this.tmdbId = tmdbId; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getPosterPath() { return posterPath; }
    public void setPosterPath(String posterPath) { this.posterPath = posterPath; }

    public int getMediaType() { return mediaType; }
    public void setMediaType(int mediaType) { this.mediaType = mediaType; }

    public String getBackdropPath() { return backdropPath; }
    public void setBackdropPath(String backdropPath) { this.backdropPath = backdropPath; }

    public String getOverview() { return overview; }
    public void setOverview(String overview) { this.overview = overview; }

    public String getVoteAverage() { return voteAverage; }
    public void setVoteAverage(String voteAverage) { this.voteAverage = voteAverage; }

    public String getReleaseDate() { return releaseDate; }
    public void setReleaseDate(String releaseDate) { this.releaseDate = releaseDate; }

    public String getImdbId() { return imdbId; }
    public void setImdbId(String imdbId) { this.imdbId = imdbId; }

    public Integer getRuntime() { return runtime; }
    public void setRuntime(Integer runtime) { this.runtime = runtime; }

    public String getProductionCompanies() { return productionCompanies; }
    public void setProductionCompanies(String productionCompanies) { this.productionCompanies = productionCompanies; }

    public String getProductionCountries() { return productionCountries; }
    public void setProductionCountries(String productionCountries) { this.productionCountries = productionCountries; }

    public String getGenres() { return genres; }
    public void setGenres(String genres) { this.genres = genres; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    @Override public String getOriginalTitle() { return null; }
    @Override public String getOriginalLanguage() { return null; }
    @Override public String getHomepage() { return null; }
}
