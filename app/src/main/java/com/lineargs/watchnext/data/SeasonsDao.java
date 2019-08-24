package com.lineargs.watchnext.data;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface SeasonsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Seasons seasons);

    @Query("SELECT * FROM seasons WHERE tmdb_id LIKE :tmdbId")
    LiveData<List<Seasons>> getSeasons(int tmdbId);
}
