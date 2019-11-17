package com.lineargs.watchnext.data.seasons;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.lineargs.watchnext.data.WatchNextRepository;

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
