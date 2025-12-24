package com.lineargs.watchnext.data;

import android.app.Application;
import androidx.lifecycle.LiveData;

import com.lineargs.watchnext.data.dao.SearchDao;
import com.lineargs.watchnext.data.entity.Search;
import com.lineargs.watchnext.data.entity.SearchTv;

import java.util.List;

public class SearchRepository {

    private final SearchDao searchDao;

    public SearchRepository(Application application) {
        WatchNextDatabase database = WatchNextDatabase.getDatabase(application);
        searchDao = database.searchDao();
    }

    public LiveData<List<Search>> getSearchMovies() {
        return searchDao.getSearchMoviesLiveData();
    }

    public LiveData<Search> getSearchMovieLiveData(int id) {
        return searchDao.getSearchMovieLiveData(id);
    }

    public LiveData<List<SearchTv>> getSearchTvSeries() {
        return searchDao.getSearchTvSeriesLiveData();
    }
}
