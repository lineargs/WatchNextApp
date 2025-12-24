package com.lineargs.watchnext.ui;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.lineargs.watchnext.data.SearchRepository;
import com.lineargs.watchnext.data.entity.Search;
import com.lineargs.watchnext.data.entity.SearchTv;

import java.util.List;

public class SearchViewModel extends AndroidViewModel {

    private final SearchRepository repository;

    public SearchViewModel(@NonNull Application application) {
        super(application);
        repository = new SearchRepository(application);
    }

    public LiveData<List<Search>> getSearchMovies() {
        return repository.getSearchMovies();
    }

    public LiveData<List<SearchTv>> getSearchTvSeries() {
        return repository.getSearchTvSeries();
    }
}
