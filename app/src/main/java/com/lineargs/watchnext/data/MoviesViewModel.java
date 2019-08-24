package com.lineargs.watchnext.data;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class MoviesViewModel extends AndroidViewModel {

    private WatchNextRepository repository;

    private LiveData<List<Movies>> popularMovies;

    private LiveData<List<Movies>> topRatedMovies;

    private LiveData<List<Movies>> upcomingMovies;

    public MoviesViewModel(Application application) {
        super(application);
        repository = new WatchNextRepository(application);
        popularMovies = repository.getPopularMovies();
        topRatedMovies = repository.getTopRatedMovies();
        upcomingMovies = repository.getUpcomingMovies();
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

    public void insertMovie(Movies movies) {
        repository.insertMovie(movies);
    }
}
