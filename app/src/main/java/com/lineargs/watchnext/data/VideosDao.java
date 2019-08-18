package com.lineargs.watchnext.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

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
