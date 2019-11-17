package com.lineargs.watchnext.data.videos;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.lineargs.watchnext.data.WatchNextRepository;

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
