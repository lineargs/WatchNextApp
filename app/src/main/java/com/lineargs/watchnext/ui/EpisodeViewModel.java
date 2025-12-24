package com.lineargs.watchnext.ui;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.lineargs.watchnext.data.SeasonsRepository;
import com.lineargs.watchnext.data.entity.Episodes;

import java.util.List;

public class EpisodeViewModel extends AndroidViewModel {

    private final SeasonsRepository repository;
    private final MutableLiveData<String> seasonId = new MutableLiveData<>();
    private final LiveData<List<Episodes>> episodes;

    public EpisodeViewModel(@NonNull Application application) {
        super(application);
        repository = new SeasonsRepository(application);
        episodes = Transformations.switchMap(seasonId, repository::getEpisodesForSeason);
        season = Transformations.switchMap(seasonId, repository::getSeason);
    }
    private final LiveData<com.lineargs.watchnext.data.entity.Seasons> season;

    public void setSeasonId(String id) {
        seasonId.setValue(id);
    }

    public LiveData<List<Episodes>> getEpisodes() {
        return episodes;
    }

    public LiveData<com.lineargs.watchnext.data.entity.Seasons> getSeason() {
        return season;
    }
}
