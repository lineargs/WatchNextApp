package com.lineargs.watchnext.ui;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.lineargs.watchnext.data.FavoritesRepository;

public class StatisticsViewModel extends AndroidViewModel {

    private final FavoritesRepository repository;

    public StatisticsViewModel(@NonNull Application application) {
        super(application);
        repository = new FavoritesRepository(application);
    }

    public LiveData<Integer> getMoviesCount() {
        return repository.getMoviesCount();
    }

    public LiveData<Integer> getSeriesCount() {
        return repository.getSeriesCount();
    }
}
