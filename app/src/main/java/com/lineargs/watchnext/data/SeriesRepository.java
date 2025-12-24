package com.lineargs.watchnext.data;

import android.app.Application;
import androidx.lifecycle.LiveData;

import com.lineargs.watchnext.data.dao.SeriesDao;
import com.lineargs.watchnext.data.entity.OnTheAirSerie;
import com.lineargs.watchnext.data.entity.PopularSerie;
import com.lineargs.watchnext.data.entity.TopRatedSerie;

import java.util.List;

public class SeriesRepository {

    private final SeriesDao seriesDao;

    public SeriesRepository(Application application) {
        WatchNextDatabase database = WatchNextDatabase.getDatabase(application);
        seriesDao = database.seriesDao();
    }

    public LiveData<List<PopularSerie>> getPopularSeries() {
        return seriesDao.getPopularSeriesLiveData();
    }

    public LiveData<PopularSerie> getPopularSerieLiveData(int id) {
        return seriesDao.getPopularSerieLiveData(id);
    }

    public LiveData<List<TopRatedSerie>> getTopRatedSeries() {
        return seriesDao.getTopRatedSeriesLiveData();
    }

    public LiveData<List<OnTheAirSerie>> getOnTheAirSeries() {
        return seriesDao.getOnTheAirSeriesLiveData();
    }
}
