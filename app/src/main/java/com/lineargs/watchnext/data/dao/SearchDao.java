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

    @Query("SELECT * FROM search WHERE movie_id = :id")
    Cursor getSearchMovie(int id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertSearchMovie(Search movie);

    // Search TV
    @Query("SELECT * FROM searchtv")
    Cursor getSearchTvSeries();

    @Query("SELECT * FROM searchtv WHERE movie_id = :id")
    Cursor getSearchTvSerie(int id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertSearchTvSerie(SearchTv serie);
}
