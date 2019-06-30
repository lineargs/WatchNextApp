package com.lineargs.watchnext.data;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface MoviesDao {

    @Insert
    void insert(Movies movies);

    @Query("SELECT * FROM movies WHERE type = 0 ORDER BY title ASC")
    LiveData<List<Movies>> getPopularMovies();

    @Query("SELECT * FROM movies WHERE type = 1 ORDER BY title ASC")
    LiveData<List<Movies>> getTopRatedMovies();

    @Query("SELECT * FROM movies WHERE type = 2 ORDER BY title ASC")
    LiveData<List<Movies>> getUpcomingMovies();

    @Query("SELECT * FROM movies WHERE type = 3 ORDER BY title ASC")
    LiveData<List<Movies>> getTheaterMovies();

    @Query("SELECT * FROM movies WHERE tmdb_id LIKE :tmdbId")
    LiveData<Movies> getMovie(int tmdbId);
}
