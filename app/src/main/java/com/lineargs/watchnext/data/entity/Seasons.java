package com.lineargs.watchnext.data.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import com.lineargs.watchnext.data.DataContract;

@Entity(tableName = DataContract.Seasons.TABLE_NAME,
        indices = {@Index(value = {DataContract.Seasons.COLUMN_SEASON_ID}, unique = true)})
public class Seasons {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = DataContract.Seasons._ID)
    private long id;

    @ColumnInfo(name = DataContract.Seasons.COLUMN_SERIE_ID)
    private int tmdbId;

    @ColumnInfo(name = DataContract.Seasons.COLUMN_SEASON_ID)
    private int seasonId;

    @ColumnInfo(name = DataContract.Seasons.COLUMN_EPISODE_COUNT)
    private int episodeCount;

    @ColumnInfo(name = DataContract.Seasons.COLUMN_RELEASE_DATE)
    private String releaseDate;

    @ColumnInfo(name = DataContract.Seasons.COLUMN_POSTER_PATH)
    private String posterPath;

    @ColumnInfo(name = DataContract.Seasons.COLUMN_SEASON_NUMBER)
    private String seasonNumber;

    @ColumnInfo(name = DataContract.Seasons.COLUMN_SHOW_NAME)
    private String showName;

    // Getters and Setters
    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public int getTmdbId() { return tmdbId; }
    public void setTmdbId(int tmdbId) { this.tmdbId = tmdbId; }

    public int getSeasonId() { return seasonId; }
    public void setSeasonId(int seasonId) { this.seasonId = seasonId; }

    public int getEpisodeCount() { return episodeCount; }
    public void setEpisodeCount(int episodeCount) { this.episodeCount = episodeCount; }

    public String getReleaseDate() { return releaseDate; }
    public void setReleaseDate(String releaseDate) { this.releaseDate = releaseDate; }

    public String getPosterPath() { return posterPath; }
    public void setPosterPath(String posterPath) { this.posterPath = posterPath; }

    public String getSeasonNumber() { return seasonNumber; }
    public void setSeasonNumber(String seasonNumber) { this.seasonNumber = seasonNumber; }

    public String getShowName() { return showName; }
    public void setShowName(String showName) { this.showName = showName; }
}
