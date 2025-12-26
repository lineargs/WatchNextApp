package com.lineargs.watchnext.ui;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import com.lineargs.watchnext.data.TrendingRepository;
import com.lineargs.watchnext.data.entity.PopularMovie;
import com.lineargs.watchnext.data.entity.PopularSerie;
import com.lineargs.watchnext.utils.retrofit.people.Person;

import java.util.List;

public class TrendingViewModel extends AndroidViewModel {

    private final TrendingRepository repository;
    private final LiveData<List<PopularMovie>> trendingMovies;
    private final LiveData<List<PopularSerie>> trendingSeries;
    private final LiveData<List<Person>> trendingPeople;

    public TrendingViewModel(@NonNull Application application) {
        super(application);
        repository = new TrendingRepository();
        trendingMovies = repository.getTrendingMovies();
        trendingSeries = repository.getTrendingSeries();
        trendingPeople = repository.getTrendingPeople();
    }

    public LiveData<List<PopularMovie>> getTrendingMovies() {
        return trendingMovies;
    }

    public LiveData<List<PopularSerie>> getTrendingSeries() {
        return trendingSeries;
    }

    public LiveData<List<Person>> getTrendingPeople() {
        return trendingPeople;
    }

    public LiveData<Boolean> getIsLoading() {
        return repository.getIsLoading();
    }

    public LiveData<String> getErrorMessage() {
        return repository.getErrorMessage();
    }
}
