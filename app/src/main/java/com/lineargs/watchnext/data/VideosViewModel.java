package com.lineargs.watchnext.data;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import java.util.List;

public class VideosViewModel extends AndroidViewModel {

    private WatchNextRepository repository;

    public VideosViewModel(@NonNull Application application) {
        super(application);
        repository = new WatchNextRepository(application);
    }

    public LiveData<List<Videos>> getVideos(int tmdbId) {
        return repository.getVideos(tmdbId);
    }
}
