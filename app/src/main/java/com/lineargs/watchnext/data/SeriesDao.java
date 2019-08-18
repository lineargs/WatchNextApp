package com.lineargs.watchnext.data;



import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface SeriesDao {

    @Insert
    void insert(Series series);

    @Query("UPDATE series SET homepage = :homepage, production_companies = :productionCompanies, " +
            "networks = :networks, status = :status, genres = :genres WHERE tmdb_id LIKE :tmdbId")
    void updateSeries(int tmdbId, String homepage, String productionCompanies, String networks,
                      String status, String genres);

    @Query("SELECT * FROM series WHERE type = 0 ORDER BY title ASC")
    LiveData<List<Series>> getPopularSeries();

    @Query("SELECT * FROM series WHERE type = 1 ORDER BY title ASC")
    LiveData<List<Series>> getTopRatedSeries();

    @Query("SELECT * FROM series WHERE type = 2 ORDER BY title ASC")
    LiveData<List<Series>> getOnTheAirSeries();

    @Query("SELECT * FROM series WHERE tmdb_id LIKE :tmdbId")
    LiveData<Series> getSeries(int tmdbId);
}
