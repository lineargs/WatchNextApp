package com.lineargs.watchnext.data.dao;

import android.database.Cursor;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import com.lineargs.watchnext.data.entity.OnTheAirSerie;
import com.lineargs.watchnext.data.entity.PopularSerie;
import com.lineargs.watchnext.data.entity.TopRatedSerie;

@Dao
public interface SeriesDao {

    // Popular Series
    @Query("SELECT * FROM popularseries")
    Cursor getPopularSeries();

    @Query("SELECT * FROM popularseries")
    androidx.lifecycle.LiveData<java.util.List<PopularSerie>> getPopularSeriesLiveData();

    @Query("SELECT * FROM popularseries WHERE movie_id = :id")
    Cursor getPopularSerie(int id);

    @Query("SELECT * FROM popularseries WHERE movie_id = :id")
    PopularSerie getPopularSerieSync(int id);

    @Query("SELECT * FROM popularseries WHERE movie_id = :id")
    androidx.lifecycle.LiveData<PopularSerie> getPopularSerieLiveData(int id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertPopularSerie(PopularSerie serie);

    @Query("DELETE FROM popularseries WHERE movie_id = :id")
    int deletePopularSerie(int id);

    // Top Rated Series
    @Query("SELECT * FROM topseries")
    Cursor getTopRatedSeries();

    @Query("SELECT * FROM topseries")
    androidx.lifecycle.LiveData<java.util.List<TopRatedSerie>> getTopRatedSeriesLiveData();

    @Query("SELECT * FROM topseries WHERE movie_id = :id")
    Cursor getTopRatedSerie(int id);

    @Query("SELECT * FROM topseries WHERE movie_id = :id")
    TopRatedSerie getTopRatedSerieSync(int id);

    @Query("SELECT * FROM topseries WHERE movie_id = :id")
    androidx.lifecycle.LiveData<TopRatedSerie> getTopRatedSerieLiveData(int id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertTopRatedSerie(TopRatedSerie serie);

    @Query("DELETE FROM topseries WHERE movie_id = :id")
    int deleteTopRatedSerie(int id);

    // On The Air Series
    @Query("SELECT * FROM ontheairseries")
    Cursor getOnTheAirSeries();

    @Query("SELECT * FROM ontheairseries")
    androidx.lifecycle.LiveData<java.util.List<OnTheAirSerie>> getOnTheAirSeriesLiveData();

    @Query("SELECT * FROM ontheairseries WHERE movie_id = :id")
    Cursor getOnTheAirSerie(int id);

    @Query("SELECT * FROM ontheairseries WHERE movie_id = :id")
    OnTheAirSerie getOnTheAirSerieSync(int id);

    @Query("SELECT * FROM ontheairseries WHERE movie_id = :id")
    androidx.lifecycle.LiveData<OnTheAirSerie> getOnTheAirSerieLiveData(int id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertOnTheAirSerie(OnTheAirSerie serie);

    @Query("DELETE FROM ontheairseries WHERE movie_id = :id")
    int deleteOnTheAirSerie(int id);
}
