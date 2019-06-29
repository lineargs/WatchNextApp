package com.lineargs.watchnext.data;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import java.util.List;

public class WatchNextRepository {

    private MoviesDao moviesDao;
    private SeriesDao seriesDao;
    private FavouritesDao favouritesDao;
    private LiveData<List<Movies>> allMovies;
    private LiveData<List<Movies>> popularMovies;
    private LiveData<List<Movies>> topRatedMovies;
    private LiveData<List<Movies>> upcomingMovies;
    private LiveData<List<Movies>> theatreMovies;
    private LiveData<List<Series>> allSeries;
    private LiveData<List<Favourites>> favourites;

    WatchNextRepository(Application application) {
        WatchNextDatabase database = WatchNextDatabase.getDatabase(application);
        moviesDao = database.moviesDao();
        allMovies = moviesDao.getAllMovies();
        popularMovies = moviesDao.getPopularMovies();
        topRatedMovies = moviesDao.getTopRatedMovies();
        upcomingMovies = moviesDao.getUpcomingMovies();
        theatreMovies = moviesDao.getTheatreMovies();
        seriesDao = database.seriesDao();
        allSeries = seriesDao.getAllSeries();
        favouritesDao = database.favouritesDao();
        favourites = favouritesDao.getAllFavourites();
    }

    public LiveData<List<Movies>> getAllMovies() {
        return allMovies;
    }

    public LiveData<List<Movies>> getPopularMovies() {
        return popularMovies;
    }

    public LiveData<List<Movies>> getTopRatedMovies() {
        return topRatedMovies;
    }

    public LiveData<List<Movies>> getUpcomingMovies() {
        return upcomingMovies;
    }

    public LiveData<List<Movies>> getTheatreMovies() {
        return theatreMovies;
    }

    public LiveData<List<Series>> getAllSeries() {
        return allSeries;
    }

    public LiveData<List<Favourites>> getFavourites() {
        return favourites;
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
