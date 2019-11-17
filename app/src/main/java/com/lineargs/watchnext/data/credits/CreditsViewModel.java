package com.lineargs.watchnext.data.credits;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.lineargs.watchnext.data.WatchNextRepository;

import java.util.List;

public class CreditsViewModel extends AndroidViewModel {

    private WatchNextRepository repository;

    public CreditsViewModel(@NonNull Application application) {
        super(application);
        repository = new WatchNextRepository(application);
    }

    public LiveData<List<Credits>> getCast(int tmdbId) {
        return repository.getCast(tmdbId);
    }

    public LiveData<List<Credits>> getCrew(int tmdbId) {
        return repository.getCrew(tmdbId);
    }
}
