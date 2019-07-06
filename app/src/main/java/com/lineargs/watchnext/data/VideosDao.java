package com.lineargs.watchnext.data;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface VideosDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Videos videos);

    @Query("SELECT * FROM videos WHERE tmdb_id LIKE :tmdbId")
    LiveData<List<Videos>> getVideos(int tmdbId);

//    @Query("SELECT COUNT(tmdb_id) FROM videos WHERE tmdb_id LIKE :tmdbId")
//    int checkIfExists(int tmdbId);
}
