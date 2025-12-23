package com.lineargs.watchnext.data.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import com.lineargs.watchnext.data.DataContract;

@Entity(tableName = DataContract.Credits.TABLE_NAME)
public class Credits {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = DataContract.Credits._ID)
    private long id;

    @ColumnInfo(name = DataContract.Credits.COLUMN_MOVIE_ID)
    private int tmdbId;

    @ColumnInfo(name = DataContract.Credits.COLUMN_PERSON_ID)
    private int personId;

    @ColumnInfo(name = DataContract.Credits.COLUMN_NAME)
    private String name;

    @ColumnInfo(name = DataContract.Credits.COLUMN_CHARACTER_NAME)
    private String characterName;

    @ColumnInfo(name = DataContract.Credits.COLUMN_JOB)
    private String job;

    @ColumnInfo(name = DataContract.Credits.COLUMN_TYPE, defaultValue = "0")
    private int type;

    @ColumnInfo(name = DataContract.Credits.COLUMN_PROFILE_PATH)
    private String profilePath;

    // Getters and Setters
    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public int getTmdbId() { return tmdbId; }
    public void setTmdbId(int tmdbId) { this.tmdbId = tmdbId; }

    public int getPersonId() { return personId; }
    public void setPersonId(int personId) { this.personId = personId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getCharacterName() { return characterName; }
    public void setCharacterName(String characterName) { this.characterName = characterName; }

    public String getJob() { return job; }
    public void setJob(String job) { this.job = job; }

    public int getType() { return type; }
    public void setType(int type) { this.type = type; }

    public String getProfilePath() { return profilePath; }
    public void setProfilePath(String profilePath) { this.profilePath = profilePath; }
}
