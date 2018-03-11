package com.lineargs.watchnext.utils.retrofit.people;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by goranminov on 29/11/2017.
 * <p>
 * JSON POJO class for our {@link retrofit2.converter.gson.GsonConverterFactory}
 */

public class Person {

    @SerializedName("id")
    @Expose
    private int id;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("biography")
    @Expose
    private String biography;

    @SerializedName("place_of_birth")
    @Expose
    private String placeOfBirth;

    @SerializedName("birthday")
    @Expose
    private String birthday;

    @SerializedName("profile_path")
    @Expose
    private String profilePath;

    @SerializedName("imdb_id")
    @Expose
    private String imdbId;

    @SerializedName("homepage")
    @Expose
    private String homepage;

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getBiography() {
        return biography;
    }

    public String getPlaceOfBirth() {
        return placeOfBirth;
    }

    public String getBirthday() {
        return birthday;
    }

    public String getProfilePath() {
        return profilePath;
    }

    public String getImdbId() {
        return imdbId;
    }

    public String getHomepage() {
        return homepage;
    }
}
