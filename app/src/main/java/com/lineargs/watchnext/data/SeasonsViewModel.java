package com.lineargs.watchnext.data;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import java.util.List;

public class SeasonsViewModel extends AndroidViewModel {

    private WatchNextRepository repository;

    public SeasonsViewModel(@NonNull Application application) {
        super(application);
        repository = new WatchNextRepository(application);
    }

    public LiveData<List<Seasons>> getSeasons(int tmdbId) {
        return repository.getSeasons(tmdbId);
    }
}
