package com.lineargs.watchnext.data;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

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
