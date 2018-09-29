package com.lineargs.watchnext.data;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

public interface SeriesDao {

    @Insert
    void insert(Series series);

    @Insert
    void insertAll(List<Series> series);

    @Query("DELETE FROM series")
    void deleteAll();

    @Query("SELECT * FROM series WHERE type = 0 ORDER BY title ASC")
    LiveData<List<Series>> getPopularSeries();

    @Query("SELECT * FROM series WHERE type = 1 ORDER BY title ASC")
    LiveData<List<Series>> getTopratedSeries();

    @Query("SELECT * FROM series WHERE type = 2 ORDER BY title ASC")
    LiveData<List<Series>> getOnTheAirSeries();

    @Query("SELECT * FROM series ORDER BY title ASC")
    LiveData<List<Series>> getAllSeries();
}
