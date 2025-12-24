package com.lineargs.watchnext.data;

import android.app.Application;
import androidx.lifecycle.LiveData;

import com.lineargs.watchnext.data.dao.CreditsDao;
import com.lineargs.watchnext.data.entity.Credits;

import java.util.List;

public class CreditsRepository {

    private final CreditsDao creditsDao;

    public CreditsRepository(Application application) {
        WatchNextDatabase database = WatchNextDatabase.getDatabase(application);
        creditsDao = database.creditsDao();
    }

    public LiveData<List<Credits>> getCast(int movieId) {
        return creditsDao.getCastForMovieLiveData(movieId);
    }

    public LiveData<List<Credits>> getCrew(int movieId) {
        return creditsDao.getCrewForMovieLiveData(movieId);
    }
}
