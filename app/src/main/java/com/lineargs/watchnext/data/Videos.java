package com.lineargs.watchnext.data;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(tableName = "videos")
public class Videos {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    @ColumnInfo(name = "id")
    private int id;

    @ColumnInfo(name = "tmdb_id")
    private int tmdb_id;

    @ColumnInfo(name = "key")
    private String key;

    @ColumnInfo(name = "name")
    private String name;

    @ColumnInfo(name = "image")
    private String image;

    @NonNull
    public int getId() {
        return id;
    }

    public void setId(@NonNull int id) {
        this.id = id;
    }

    public int getTmdb_id() {
        return tmdb_id;
    }

    public void setTmdb_id(int tmdb_id) {
        this.tmdb_id = tmdb_id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
