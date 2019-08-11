package com.lineargs.watchnext.data;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface SeasonsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Seasons seasons);

    @Query("SELECT * FROM seasons WHERE tmdb_id LIKE :tmdbId")
    LiveData<List<Seasons>> getSeasons(int tmdbId);
}
