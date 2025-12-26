package com.lineargs.watchnext.data.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "upcoming_episodes")
public class UpcomingEpisodes {

    @ColumnInfo(name = "episode_id")
    private int episodeId;

    @PrimaryKey
    @ColumnInfo(name = "series_id")
    private int seriesId;

    @ColumnInfo(name = "series_title")
    private String seriesTitle;

    @ColumnInfo(name = "episode_name")
    private String episodeName;

    @ColumnInfo(name = "air_date")
    private String airDate;

    @ColumnInfo(name = "season_number")
    private int seasonNumber;

    @ColumnInfo(name = "episode_number")
    private int episodeNumber;

    @ColumnInfo(name = "poster_path")
    private String posterPath;

    @ColumnInfo(name = "season_id")
    private int seasonId;

    @ColumnInfo(name = "episode_count")
    private int episodeCount;

    // Getters and Setters
    public int getEpisodeId() { return episodeId; }
    public void setEpisodeId(int episodeId) { this.episodeId = episodeId; }

    public int getSeriesId() { return seriesId; }
    public void setSeriesId(int seriesId) { this.seriesId = seriesId; }

    public String getSeriesTitle() { return seriesTitle; }
    public void setSeriesTitle(String seriesTitle) { this.seriesTitle = seriesTitle; }

    public String getEpisodeName() { return episodeName; }
    public void setEpisodeName(String episodeName) { this.episodeName = episodeName; }

    public String getAirDate() { return airDate; }
    public void setAirDate(String airDate) { this.airDate = airDate; }

    public int getSeasonNumber() { return seasonNumber; }
    public void setSeasonNumber(int seasonNumber) { this.seasonNumber = seasonNumber; }

    public int getEpisodeNumber() { return episodeNumber; }
    public void setEpisodeNumber(int episodeNumber) { this.episodeNumber = episodeNumber; }

    public String getPosterPath() { return posterPath; }
    public void setPosterPath(String posterPath) { this.posterPath = posterPath; }

    public int getSeasonId() { return seasonId; }
    public void setSeasonId(int seasonId) { this.seasonId = seasonId; }

    public int getEpisodeCount() { return episodeCount; }
    public void setEpisodeCount(int episodeCount) { this.episodeCount = episodeCount; }
}
