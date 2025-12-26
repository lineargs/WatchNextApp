package com.lineargs.watchnext.data.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import com.lineargs.watchnext.data.DataContract;

@Entity(tableName = DataContract.SearchTv.TABLE_NAME)
public class SearchTv {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = DataContract.SearchTv._ID)
    private long id;

    @ColumnInfo(name = DataContract.Search.COLUMN_MOVIE_ID)
    private int tmdbId;

    @ColumnInfo(name = DataContract.Search.COLUMN_TITLE)
    private String title;

    @ColumnInfo(name = DataContract.Search.COLUMN_POSTER_PATH)
    private String posterPath;

    @ColumnInfo(name = DataContract.PopularMovieEntry.COLUMN_OVERVIEW)
    private String overview;

    @ColumnInfo(name = DataContract.PopularMovieEntry.COLUMN_BACKDROP_PATH)
    private String backdropPath;

    @ColumnInfo(name = DataContract.PopularMovieEntry.COLUMN_VOTE_AVERAGE)
    private String voteAverage;

    @ColumnInfo(name = DataContract.PopularMovieEntry.COLUMN_RELEASE_DATE)
    private String releaseDate;

    @ColumnInfo(name = DataContract.PopularMovieEntry.COLUMN_ORIGINAL_LANGUAGE)
    private String originalLanguage;

    // Getters and Setters
    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public int getTmdbId() { return tmdbId; }
    public void setTmdbId(int tmdbId) { this.tmdbId = tmdbId; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getPosterPath() { return posterPath; }
    public void setPosterPath(String posterPath) { this.posterPath = posterPath; }

    public String getOverview() { return overview; }
    public void setOverview(String overview) { this.overview = overview; }

    public String getBackdropPath() { return backdropPath; }
    public void setBackdropPath(String backdropPath) { this.backdropPath = backdropPath; }

    public String getVoteAverage() { return voteAverage; }
    public void setVoteAverage(String voteAverage) { this.voteAverage = voteAverage; }

    public String getReleaseDate() { return releaseDate; }
    public void setReleaseDate(String releaseDate) { this.releaseDate = releaseDate; }

    public String getOriginalLanguage() { return originalLanguage; }
    public void setOriginalLanguage(String originalLanguage) { this.originalLanguage = originalLanguage; }
}
