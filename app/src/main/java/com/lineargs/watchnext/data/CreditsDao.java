package com.lineargs.watchnext.data;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface CreditsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertCredits(Credits credits);

    @Query("SELECT * FROM credits WHERE tmdb_id LIKE :tmdbId AND type LIKE 0")
    LiveData<List<Credits>> getAllCast(int tmdbId);

    @Query("SELECT * FROM credits WHERE tmdb_id LIKE :tmdbId AND type LIKE 1")
    LiveData<List<Credits>> getAllCrew(int tmdbId);
}
