package com.lineargs.watchnext.data;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class ReviewsViewModel extends AndroidViewModel {

    private WatchNextRepository repository;

    public ReviewsViewModel(@NonNull Application application) {
        super(application);
        repository = new WatchNextRepository(application);
    }

    public LiveData<List<Reviews>> getReviews(int tmdbId) {
        return repository.getReviews(tmdbId);
    }
}
