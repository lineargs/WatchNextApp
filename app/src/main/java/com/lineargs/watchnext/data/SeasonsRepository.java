package com.lineargs.watchnext.data;

import android.app.Application;
import androidx.lifecycle.LiveData;

import com.lineargs.watchnext.data.dao.DetailsDao;
import com.lineargs.watchnext.data.entity.Episodes;
import com.lineargs.watchnext.data.entity.Seasons;

import java.util.List;

public class SeasonsRepository {

    private final DetailsDao detailsDao;

    public SeasonsRepository(Application application) {
        WatchNextDatabase database = WatchNextDatabase.getDatabase(application);
        detailsDao = database.detailsDao();
    }

    public LiveData<List<Seasons>> getSeasons(int serieId) {
        return detailsDao.getSeasonsLiveData(serieId);
    }

    public LiveData<Seasons> getSeason(String seasonId) {
        return detailsDao.getSeasonLiveData(seasonId);
    }

    public LiveData<Episodes> getEpisode(int episodeId) {
        return detailsDao.getEpisodeLiveData(episodeId);
    }

    public LiveData<List<Episodes>> getEpisodesForSeason(String seasonId) {
        return detailsDao.getEpisodesForSeasonLiveData(seasonId);
    }
}
