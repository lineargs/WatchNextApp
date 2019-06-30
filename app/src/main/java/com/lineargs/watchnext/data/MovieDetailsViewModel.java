package com.lineargs.watchnext.data;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

public class MovieDetailsViewModel extends AndroidViewModel {

    private WatchNextRepository repository;

    public MovieDetailsViewModel(@NonNull Application application) {
        super(application);
        repository = new WatchNextRepository(application);
    }

    public LiveData<Movies> getMovie(int tmdbId) {
        return repository.getMovie(tmdbId);
    }
}
