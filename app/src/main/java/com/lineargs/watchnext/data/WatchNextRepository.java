package com.lineargs.watchnext.data;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import java.util.List;

public class WatchNextRepository {

    private MoviesDao moviesDao;
    private SeriesDao seriesDao;
    private FavouritesDao favouritesDao;
    private LiveData<List<Movies>> popularMovies;
    private LiveData<List<Movies>> topRatedMovies;
    private LiveData<List<Movies>> upcomingMovies;
    private LiveData<List<Movies>> theatreMovies;
    private LiveData<List<Series>> popularSeries;
    private LiveData<List<Series>> topRatedSeries;
    private LiveData<List<Series>> onTheAirSeries;
    private LiveData<List<Favourites>> favourites;

    WatchNextRepository(Application application) {
        WatchNextDatabase database = WatchNextDatabase.getDatabase(application);
        moviesDao = database.moviesDao();
        popularMovies = moviesDao.getPopularMovies();
        topRatedMovies = moviesDao.getTopRatedMovies();
        upcomingMovies = moviesDao.getUpcomingMovies();
        theatreMovies = moviesDao.getTheaterMovies();
        seriesDao = database.seriesDao();
        onTheAirSeries = seriesDao.getOnTheAirSeries();
        topRatedSeries = seriesDao.getTopRatedSeries();
        popularSeries = seriesDao.getPopularSeries();
        favouritesDao = database.favouritesDao();
        favourites = favouritesDao.getAllFavourites();
    }

    public LiveData<Movies> getMovie(int tmdbId) {
        return moviesDao.getMovie(tmdbId);
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

    public LiveData<List<Series>> getPopularSeries() {
        return popularSeries;
    }

    public LiveData<List<Series>> getTopRatedSeries() {
        return topRatedSeries;
    }

    public LiveData<List<Series>> getOnTheAirSeries() {
        return onTheAirSeries;
    }

    public LiveData<List<Favourites>> getFavourites() {
        return favourites;
    }

    public void insertMovie(Movies movies) {
        new insertMoviesTask(moviesDao).execute(movies);
    }

    public void updateMovie(Movies movies) {
        new updateMovieTask(moviesDao).execute(movies);
    }

    public void insertSeries(Series series) {
        new insertSeriesTask(seriesDao).execute(series);
    }

    private static class updateMovieTask extends AsyncTask<Movies, Void, Void> {

        private MoviesDao moviesDao;

        updateMovieTask(MoviesDao dao) {
            moviesDao = dao;
        }

        @Override
        protected Void doInBackground(final Movies... movies) {
            Movies movie = movies[0];
            moviesDao.updateMovie(movie.getTmdbId(), movie.getImdbId(), movie.getHomepage(), movie.getProductionCompanies(),
                    movie.getProductionCountries(), movie.getGenres(), movie.getRuntime(), movie.getStatus());
            return null;
        }
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
