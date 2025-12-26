package com.lineargs.watchnext.data.dao;

import android.database.Cursor;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import com.lineargs.watchnext.data.entity.PopularMovie;
import com.lineargs.watchnext.data.entity.TheaterMovie;
import com.lineargs.watchnext.data.entity.TopRatedMovie;
import com.lineargs.watchnext.data.entity.UpcomingMovie;

@Dao
public interface MoviesDao {

    // Popular Movies
    @Query("SELECT * FROM popularmovies")
    Cursor getPopularMovies();

    @Query("SELECT * FROM popularmovies")
    androidx.lifecycle.LiveData<java.util.List<PopularMovie>> getPopularMoviesLiveData();

    @Query("SELECT * FROM popularmovies WHERE movie_id = :id")
    Cursor getPopularMovie(int id);

    @Query("SELECT * FROM popularmovies WHERE movie_id = :id")
    PopularMovie getPopularMovieSync(int id);

    @Query("SELECT * FROM popularmovies WHERE movie_id = :id")
    androidx.lifecycle.LiveData<PopularMovie> getPopularMovieLiveData(int id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertPopularMovie(PopularMovie movie);

    @Query("DELETE FROM popularmovies WHERE movie_id = :id")
    int deletePopularMovie(int id);

    // Top Rated Movies
    @Query("SELECT * FROM topmovies")
    Cursor getTopRatedMovies();

    @Query("SELECT * FROM topmovies")
    androidx.lifecycle.LiveData<java.util.List<TopRatedMovie>> getTopRatedMoviesLiveData();

    @Query("SELECT * FROM topmovies WHERE movie_id = :id")
    Cursor getTopRatedMovie(int id);

    @Query("SELECT * FROM topmovies WHERE movie_id = :id")
    TopRatedMovie getTopRatedMovieSync(int id);

    @Query("SELECT * FROM topmovies WHERE movie_id = :id")
    androidx.lifecycle.LiveData<TopRatedMovie> getTopRatedMovieLiveData(int id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertTopRatedMovie(TopRatedMovie movie);

    @Query("DELETE FROM topmovies WHERE movie_id = :id")
    int deleteTopRatedMovie(int id);

    // Upcoming Movies
    @Query("SELECT * FROM upcomingmovies")
    Cursor getUpcomingMovies();

    @Query("SELECT * FROM upcomingmovies")
    androidx.lifecycle.LiveData<java.util.List<UpcomingMovie>> getUpcomingMoviesLiveData();

    @Query("SELECT * FROM upcomingmovies WHERE movie_id = :id")
    Cursor getUpcomingMovie(int id);

    @Query("SELECT * FROM upcomingmovies WHERE movie_id = :id")
    UpcomingMovie getUpcomingMovieSync(int id);

    @Query("SELECT * FROM upcomingmovies WHERE movie_id = :id")
    androidx.lifecycle.LiveData<UpcomingMovie> getUpcomingMovieLiveData(int id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertUpcomingMovie(UpcomingMovie movie);

    @Query("DELETE FROM upcomingmovies WHERE movie_id = :id")
    int deleteUpcomingMovie(int id);

    // Theater Movies
    @Query("SELECT * FROM theatermovies")
    Cursor getTheaterMovies();

    @Query("SELECT * FROM theatermovies")
    androidx.lifecycle.LiveData<java.util.List<TheaterMovie>> getTheaterMoviesLiveData();

    @Query("SELECT * FROM theatermovies WHERE movie_id = :id")
    Cursor getTheaterMovie(int id);

    @Query("SELECT * FROM theatermovies WHERE movie_id = :id")
    androidx.lifecycle.LiveData<TheaterMovie> getTheaterMovieLiveData(int id);

    @Query("SELECT * FROM theatermovies WHERE movie_id = :id")
    TheaterMovie getTheaterMovieSync(int id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertTheaterMovie(TheaterMovie movie);

    @Query("DELETE FROM theatermovies WHERE movie_id = :id")
    int deleteTheaterMovie(int id);
}
