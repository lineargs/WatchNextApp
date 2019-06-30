package com.lineargs.watchnext.data;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import java.util.List;

public class TheaterViewModel extends AndroidViewModel {

    private WatchNextRepository repository;

    private LiveData<List<Movies>> theaterMovies;

    public TheaterViewModel(@NonNull Application application) {
        super(application);
        repository = new WatchNextRepository(application);
        theaterMovies = repository.getTheatreMovies();
    }

    public LiveData<List<Movies>> getTheaterMovies() {
        return theaterMovies;
    }
}
