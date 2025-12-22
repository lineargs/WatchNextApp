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

    // Getters and Setters
    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public int getTmdbId() { return tmdbId; }
    public void setTmdbId(int tmdbId) { this.tmdbId = tmdbId; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getPosterPath() { return posterPath; }
    public void setPosterPath(String posterPath) { this.posterPath = posterPath; }
}
