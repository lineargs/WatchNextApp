package com.lineargs.watchnext.data;

import android.app.Application;
import androidx.lifecycle.LiveData;

import com.lineargs.watchnext.data.dao.MoviesDao;
import com.lineargs.watchnext.data.entity.PopularMovie;
import com.lineargs.watchnext.data.entity.TheaterMovie;
import com.lineargs.watchnext.data.entity.TopRatedMovie;
import com.lineargs.watchnext.data.entity.UpcomingMovie;

import java.util.List;

public class MoviesRepository {

    private final MoviesDao moviesDao;

    public MoviesRepository(Application application) {
        WatchNextDatabase database = WatchNextDatabase.getDatabase(application);
        moviesDao = database.moviesDao();
    }

    public LiveData<List<PopularMovie>> getPopularMovies() {
        return moviesDao.getPopularMoviesLiveData();
    }

    public LiveData<PopularMovie> getPopularMovieLiveData(int id) {
        return moviesDao.getPopularMovieLiveData(id);
    }

    public LiveData<List<TopRatedMovie>> getTopRatedMovies() {
        return moviesDao.getTopRatedMoviesLiveData();
    }

    public LiveData<TopRatedMovie> getTopRatedMovieLiveData(int id) {
        return moviesDao.getTopRatedMovieLiveData(id);
    }

    public LiveData<List<UpcomingMovie>> getUpcomingMovies() {
        return moviesDao.getUpcomingMoviesLiveData();
    }

    public LiveData<UpcomingMovie> getUpcomingMovieLiveData(int id) {
        return moviesDao.getUpcomingMovieLiveData(id);
    }

    public LiveData<List<TheaterMovie>> getTheaterMovies() {
        return moviesDao.getTheaterMoviesLiveData();
    }

    public LiveData<TheaterMovie> getTheaterMovieLiveData(int id) {
        return moviesDao.getTheaterMovieLiveData(id);
    }
}
