package com.lineargs.watchnext.data.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import com.lineargs.watchnext.data.DataContract;

@Entity(tableName = DataContract.Person.TABLE_NAME,
        indices = {@Index(value = {DataContract.Person.COLUMN_PERSON_ID}, unique = true)})
public class Person {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = DataContract.Person._ID)
    private long id;

    @ColumnInfo(name = DataContract.Person.COLUMN_PERSON_ID)
    private int personId;

    @ColumnInfo(name = DataContract.Person.COLUMN_BIRTHDAY)
    private String birthday;

    @ColumnInfo(name = DataContract.Person.COLUMN_NAME)
    private String name;

    @ColumnInfo(name = DataContract.Person.COLUMN_BIOGRAPHY)
    private String biography;

    @ColumnInfo(name = DataContract.Person.COLUMN_PLACE_OF_BIRTH)
    private String placeOfBirth;

    @ColumnInfo(name = DataContract.Person.COLUMN_PROFILE_PATH)
    private String profilePath;

    @ColumnInfo(name = DataContract.Person.COLUMN_HOMEPAGE)
    private String homepage;

    // Getters and Setters
    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public int getPersonId() { return personId; }
    public void setPersonId(int personId) { this.personId = personId; }

    public String getBirthday() { return birthday; }
    public void setBirthday(String birthday) { this.birthday = birthday; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getBiography() { return biography; }
    public void setBiography(String biography) { this.biography = biography; }

    public String getPlaceOfBirth() { return placeOfBirth; }
    public void setPlaceOfBirth(String placeOfBirth) { this.placeOfBirth = placeOfBirth; }

    public String getProfilePath() { return profilePath; }
    public void setProfilePath(String profilePath) { this.profilePath = profilePath; }

    public String getHomepage() { return homepage; }
    public void setHomepage(String homepage) { this.homepage = homepage; }
}
