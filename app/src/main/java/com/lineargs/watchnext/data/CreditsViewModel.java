package com.lineargs.watchnext.data;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import java.util.List;

public class CreditsViewModel extends AndroidViewModel {

    private WatchNextRepository repository;

    public CreditsViewModel(@NonNull Application application) {
        super(application);
        repository = new WatchNextRepository(application);
    }

    public LiveData<List<Credits>> getCast (int tmdbId) {return repository.getCast(tmdbId);}

    public LiveData<List<Credits>> getCrew (int tmdbId) {return repository.getCrew(tmdbId);}
}
