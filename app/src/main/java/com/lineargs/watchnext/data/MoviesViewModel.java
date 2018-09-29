package com.lineargs.watchnext.data;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import java.util.List;

public class MoviesViewModel extends AndroidViewModel {

    private WatchNextRepository repository;

    private LiveData<List<Movies>> movies;

    public MoviesViewModel (Application application) {
        super(application);
        repository = new WatchNextRepository(application);
        movies = repository.getMovies();
    }

    public LiveData<List<Movies>> getMovies() {
        return movies;
    }

    public void insert(Movies movies) {repository.insertMovies(movies);}
}
