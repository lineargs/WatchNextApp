package com.lineargs.watchnext.data;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class TheaterViewModel extends AndroidViewModel {

    private LiveData<List<Movies>> theaterMovies;

    public TheaterViewModel(@NonNull Application application) {
        super(application);
        WatchNextRepository repository = new WatchNextRepository(application);
        theaterMovies = repository.getTheatreMovies();
    }

    public LiveData<List<Movies>> getTheaterMovies() {
        return theaterMovies;
    }
}
