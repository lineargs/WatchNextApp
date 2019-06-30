package com.lineargs.watchnext.data;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import java.util.List;

public class SeriesViewModel extends AndroidViewModel {

    private WatchNextRepository repository;

    private LiveData<List<Series>> popularSeries;

    private LiveData<List<Series>> topRatedSeries;

    private LiveData<List<Series>> onTheAirSeries;

    public SeriesViewModel(@NonNull Application application) {
        super(application);
        repository = new WatchNextRepository(application);
        popularSeries = repository.getPopularSeries();
        topRatedSeries = repository.getTopRatedSeries();
        onTheAirSeries = repository.getOnTheAirSeries();
    }

    public LiveData<List<Series>> getPopularSeries() {return popularSeries;}

    public LiveData<List<Series>> getTopRatedSeries() {return topRatedSeries;}

    public LiveData<List<Series>> getOnTheAirSeries() {return onTheAirSeries;}

}
