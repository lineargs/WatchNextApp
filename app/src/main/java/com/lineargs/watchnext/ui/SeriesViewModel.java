package com.lineargs.watchnext.ui;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.lineargs.watchnext.data.SeriesRepository;
import com.lineargs.watchnext.data.entity.OnTheAirSerie;
import com.lineargs.watchnext.data.entity.PopularSerie;
import com.lineargs.watchnext.data.entity.TopRatedSerie;

import java.util.List;

public class SeriesViewModel extends AndroidViewModel {

    private final SeriesRepository repository;

    public SeriesViewModel(@NonNull Application application) {
        super(application);
        repository = new SeriesRepository(application);
    }

    public LiveData<List<PopularSerie>> getPopularSeries() {
        return repository.getPopularSeries();
    }

    public LiveData<List<TopRatedSerie>> getTopRatedSeries() {
        return repository.getTopRatedSeries();
    }

    public LiveData<List<OnTheAirSerie>> getOnTheAirSeries() {
        return repository.getOnTheAirSeries();
    }
}
