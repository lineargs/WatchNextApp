package com.lineargs.watchnext.data;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface MoviesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Movies movies);

    @Query("UPDATE movies SET imdb_id = :imdbId, homepage = :homepage, production_companies = :companies, " +
            "production_countries = :countries, genres = :genres, runtime = :runtime, status = :status  WHERE tmdb_id LIKE :id")
    void updateMovie(int id, String imdbId, String homepage, String companies, String countries,
                     String genres, int runtime, String status);

    @Query("SELECT * FROM movies WHERE type LIKE 0 ORDER BY title ASC")
    LiveData<List<Movies>> getPopularMovies();

    @Query("SELECT * FROM movies WHERE type LIKE 1 ORDER BY title ASC")
    LiveData<List<Movies>> getTopRatedMovies();

    @Query("SELECT * FROM movies WHERE type LIKE 2 ORDER BY title ASC")
    LiveData<List<Movies>> getUpcomingMovies();

    @Query("SELECT * FROM movies WHERE type LIKE 3 ORDER BY title ASC")
    LiveData<List<Movies>> getTheaterMovies();

    @Query("SELECT * FROM movies WHERE tmdb_id LIKE :tmdbId")
    LiveData<Movies> getMovie(int tmdbId);
}
