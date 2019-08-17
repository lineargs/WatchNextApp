package com.lineargs.watchnext.data;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import java.util.List;

public class SearchViewModel extends AndroidViewModel {

    private WatchNextRepository repository;

    public SearchViewModel(@NonNull Application application) {
        super(application);
        repository = new WatchNextRepository(application);
    }

    public LiveData<List<Search>> getSearchResults() {
        return repository.getSearchResults();
    }
}
