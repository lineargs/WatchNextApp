package com.lineargs.watchnext.data.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import com.lineargs.watchnext.data.DataContract;

@Entity(tableName = DataContract.Episodes.TABLE_NAME,
        indices = {@Index(value = {DataContract.Episodes.COLUMN_EPISODE_ID}, unique = true)})
public class Episodes {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = DataContract.Episodes._ID)
    private long id;

    @ColumnInfo(name = DataContract.Episodes.COLUMN_EPISODE_ID)
    private int episodeId;

    @ColumnInfo(name = DataContract.Episodes.COLUMN_SEASON_ID)
    private int seasonId;

    @ColumnInfo(name = DataContract.Episodes.COLUMN_EPISODE_NUMBER)
    private int episodeNumber;

    @ColumnInfo(name = DataContract.Episodes.COLUMN_RELEASE_DATE)
    private String releaseDate;

    @ColumnInfo(name = DataContract.Episodes.COLUMN_NAME)
    private String name;

    @ColumnInfo(name = DataContract.Episodes.COLUMN_OVERVIEW)
    private String overview;

    @ColumnInfo(name = DataContract.Episodes.COLUMN_SEASON_NUMBER)
    private int seasonNumber;

    @ColumnInfo(name = DataContract.Episodes.COLUMN_STILL_PATH)
    private String stillPath;

    @ColumnInfo(name = DataContract.Episodes.COLUMN_VOTE_AVERAGE)
    private String voteAverage;

    @ColumnInfo(name = DataContract.Episodes.COLUMN_DIRECTORS)
    private String directors;

    @ColumnInfo(name = DataContract.Episodes.COLUMN_WRITERS)
    private String writers;

    @ColumnInfo(name = DataContract.Episodes.COLUMN_GUEST_STARS)
    private String guestStars;

    @ColumnInfo(name = DataContract.Episodes.COLUMN_VOTE_COUNT, defaultValue = "0")
    private int voteCount;

    // Getters and Setters
    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public int getEpisodeId() { return episodeId; }
    public void setEpisodeId(int episodeId) { this.episodeId = episodeId; }

    public int getSeasonId() { return seasonId; }
    public void setSeasonId(int seasonId) { this.seasonId = seasonId; }

    public int getEpisodeNumber() { return episodeNumber; }
    public void setEpisodeNumber(int episodeNumber) { this.episodeNumber = episodeNumber; }

    public String getReleaseDate() { return releaseDate; }
    public void setReleaseDate(String releaseDate) { this.releaseDate = releaseDate; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getOverview() { return overview; }
    public void setOverview(String overview) { this.overview = overview; }

    public int getSeasonNumber() { return seasonNumber; }
    public void setSeasonNumber(int seasonNumber) { this.seasonNumber = seasonNumber; }

    public String getStillPath() { return stillPath; }
    public void setStillPath(String stillPath) { this.stillPath = stillPath; }

    public String getVoteAverage() { return voteAverage; }
    public void setVoteAverage(String voteAverage) { this.voteAverage = voteAverage; }

    public String getDirectors() { return directors; }
    public void setDirectors(String directors) { this.directors = directors; }

    public String getWriters() { return writers; }
    public void setWriters(String writers) { this.writers = writers; }

    public String getGuestStars() { return guestStars; }
    public void setGuestStars(String guestStars) { this.guestStars = guestStars; }

    public int getVoteCount() { return voteCount; }
    public void setVoteCount(int voteCount) { this.voteCount = voteCount; }
}
