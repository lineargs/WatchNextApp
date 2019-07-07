package com.lineargs.watchnext.data;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Entity(tableName = "person", indices = @Index(value = "person_id", unique = true))
public class Person {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "person_id")
    private int personId;

    private String name;

    private String biography;

    @ColumnInfo(name = "place_of_birth")
    private String placeOfBirth;

    @ColumnInfo(name = "profile_path")
    private String profilePath;

    private String homepage;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPersonId() { return personId; }

    public void setPersonId(int personId) { this.personId = personId; }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBiography() {
        return biography;
    }

    public void setBiography(String biography) {
        this.biography = biography;
    }

    public String getPlaceOfBirth() {
        return placeOfBirth;
    }

    public void setPlaceOfBirth(String placeOfBirth) {
        this.placeOfBirth = placeOfBirth;
    }

    public String getProfilePath() {
        return profilePath;
    }

    public void setProfilePath(String profilePath) {
        this.profilePath = profilePath;
    }

    public String getHomepage() {
        return homepage;
    }

    public void setHomepage(String homepage) {
        this.homepage = homepage;
    }
}
