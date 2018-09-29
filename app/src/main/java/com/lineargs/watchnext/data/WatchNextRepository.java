package com.lineargs.watchnext.data;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import java.util.List;

public class WatchNextRepository {

    private MoviesDao moviesDao;
    private SeriesDao seriesDao;
    private LiveData<List<Movies>> movies;
    private LiveData<List<Series>> series;

    WatchNextRepository(Application application) {
        WatchNextDatabase database = WatchNextDatabase.getDatabase(application);
        moviesDao = database.moviesDao();
        movies = moviesDao.getAllMovies();
        seriesDao = database.seriesDao();
        series = seriesDao.getAllSeries();
    }

    public LiveData<List<Movies>> getMovies() {
        return movies;
    }

    public LiveData<List<Series>> getSeries() {
        return series;
    }

    public void insertMovies(Movies movies) {
        new insertMoviesTask(moviesDao).execute(movies);
    }

    public void insertSeries(Series series) {
        new insertSeriesTask(seriesDao).execute(series);
    }

    private static class insertMoviesTask extends AsyncTask<Movies, Void, Void> {

        private MoviesDao moviesDao;

        insertMoviesTask(MoviesDao dao) {
            moviesDao = dao;
        }

        @Override
        protected Void doInBackground(final Movies... movies) {
            moviesDao.insert(movies[0]);
            return null;
        }
    }

    private static class insertSeriesTask extends AsyncTask<Series, Void, Void> {

        private SeriesDao seriesDao;

        insertSeriesTask(SeriesDao dao) {
            seriesDao = dao;
        }

        @Override
        protected Void doInBackground(Series... series) {
            seriesDao.insert(series[0]);
            return null;
        }
    }
}
