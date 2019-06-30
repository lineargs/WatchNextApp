package com.lineargs.watchnext.data;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import java.util.List;

public class MoviesViewModel extends AndroidViewModel {

    private WatchNextRepository repository;

    private LiveData<List<Movies>> popularMovies;

    private LiveData<List<Movies>> topRatedMovies;

    private LiveData<List<Movies>> upcomingMovies;

    private LiveData<List<Favourites>> favourites;

    public MoviesViewModel(Application application) {
        super(application);
        repository = new WatchNextRepository(application);
        popularMovies = repository.getPopularMovies();
        topRatedMovies = repository.getTopRatedMovies();
        upcomingMovies = repository.getUpcomingMovies();
        favourites = repository.getFavourites();

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

    public LiveData<List<Favourites>> getFavourites() {
        return favourites;
    }

    public void insertMovie(Movies movies) {
        repository.insertMovie(movies);
    }
}
