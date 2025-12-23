package com.lineargs.watchnext.data.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import com.lineargs.watchnext.data.DataContract;

@Entity(tableName = DataContract.Review.TABLE_NAME,
        indices = {@Index(value = {DataContract.Review.COLUMN_REVIEW_ID}, unique = true)})
public class Review {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = DataContract.Review._ID)
    private long id;

    @ColumnInfo(name = DataContract.Review.COLUMN_MOVIE_ID)
    private int tmdbId;

    @ColumnInfo(name = DataContract.Review.COLUMN_REVIEW_ID)
    private String reviewId;

    @ColumnInfo(name = DataContract.Review.COLUMN_AUTHOR)
    private String author;

    @ColumnInfo(name = DataContract.Review.COLUMN_CONTENT)
    private String content;

    @ColumnInfo(name = DataContract.Review.COLUMN_URL)
    private String url;

    // Getters and Setters
    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public int getTmdbId() { return tmdbId; }
    public void setTmdbId(int tmdbId) { this.tmdbId = tmdbId; }

    public String getReviewId() { return reviewId; }
    public void setReviewId(String reviewId) { this.reviewId = reviewId; }

    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public String getUrl() { return url; }
    public void setUrl(String url) { this.url = url; }
}
