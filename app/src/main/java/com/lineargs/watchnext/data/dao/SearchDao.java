package com.lineargs.watchnext.data.dao;

import android.database.Cursor;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import com.lineargs.watchnext.data.entity.Search;
import com.lineargs.watchnext.data.entity.SearchTv;

@Dao
public interface SearchDao {

    // Search Movies
    @Query("SELECT * FROM search")
    Cursor getSearchMovies();

    @Query("SELECT * FROM search")
    androidx.lifecycle.LiveData<java.util.List<Search>> getSearchMoviesLiveData();

    @Query("SELECT * FROM search WHERE movie_id = :id")
    Cursor getSearchMovie(int id);

    @Query("SELECT * FROM search WHERE movie_id = :id")
    androidx.lifecycle.LiveData<Search> getSearchMovieLiveData(int id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertSearchMovie(Search movie);

    // Search TV
    @Query("SELECT * FROM searchtv")
    Cursor getSearchTvSeries();

    @Query("SELECT * FROM searchtv")
    androidx.lifecycle.LiveData<java.util.List<SearchTv>> getSearchTvSeriesLiveData();

    @Query("SELECT * FROM searchtv WHERE movie_id = :id")
    androidx.lifecycle.LiveData<SearchTv> getSearchTvLiveData(int id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertSearchTvSerie(SearchTv serie);
}
