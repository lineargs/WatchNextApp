package com.lineargs.watchnext.data;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "credits", indices = @Index(value = "credit_id", unique = true))
public class Credits {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "tmdb_id")
    private int tmdbId;

    @ColumnInfo(name = "person_id")
    private int personId;

    private String name;

    @ColumnInfo(name = "credit_id")
    private String creditId;

    @ColumnInfo(name = "character_name")
    private String characterName;

    private String job;

    private int type;

    @ColumnInfo(name = "profile_path")
    private String profilePath;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTmdbId() {
        return tmdbId;
    }

    public void setTmdbId(int tmdbId) {
        this.tmdbId = tmdbId;
    }

    public int getPersonId() {
        return personId;
    }

    public void setPersonId(int personId) {
        this.personId = personId;
    }

    public String getCreditId() { return creditId; }

    public void setCreditId(String creditId) { this.creditId = creditId; }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCharacterName() {
        return characterName;
    }

    public void setCharacterName(String characterName) {
        this.characterName = characterName;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getProfilePath() {
        return profilePath;
    }

    public void setProfilePath(String profilePath) {
        this.profilePath = profilePath;
    }
}
