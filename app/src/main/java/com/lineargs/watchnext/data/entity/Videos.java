package com.lineargs.watchnext.data.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import com.lineargs.watchnext.data.DataContract;

@Entity(tableName = DataContract.Videos.TABLE_NAME,
        indices = {@Index(value = {DataContract.Videos.COLUMN_VIDEO_ID}, unique = true)})
public class Videos {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = DataContract.Videos._ID)
    private long id;

    @ColumnInfo(name = DataContract.Videos.COLUMN_MOVIE_ID)
    private int tmdbId;

    @ColumnInfo(name = DataContract.Videos.COLUMN_VIDEO_ID)
    private String videoId;

    @ColumnInfo(name = DataContract.Videos.COLUMN_KEY)
    private String key;

    @ColumnInfo(name = DataContract.Videos.COLUMN_NAME)
    private String name;

    @ColumnInfo(name = DataContract.Videos.COLUMN_TYPE)
    private String type;

    @ColumnInfo(name = DataContract.Videos.COLUMN_IMG)
    private String image;

    @ColumnInfo(name = DataContract.Videos.COLUMN_SITE)
    private String site;

    // Getters and Setters
    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public int getTmdbId() { return tmdbId; }
    public void setTmdbId(int tmdbId) { this.tmdbId = tmdbId; }

    public String getVideoId() { return videoId; }
    public void setVideoId(String videoId) { this.videoId = videoId; }

    public String getKey() { return key; }
    public void setKey(String key) { this.key = key; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getImage() { return image; }
    public void setImage(String image) { this.image = image; }

    public String getSite() { return site; }
    public void setSite(String site) { this.site = site; }
}
