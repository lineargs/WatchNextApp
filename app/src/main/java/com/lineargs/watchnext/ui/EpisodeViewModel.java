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
    private final com.lineargs.watchnext.data.FavoritesRepository favoritesRepository;
    private final MutableLiveData<String> seasonId = new MutableLiveData<>();
    private final MutableLiveData<Integer> serieId = new MutableLiveData<>();
    private final LiveData<List<Episodes>> episodes;
    private final LiveData<com.lineargs.watchnext.data.entity.Seasons> season;
    private final LiveData<com.lineargs.watchnext.data.entity.Favorites> seriesFavorite;

    public EpisodeViewModel(@NonNull Application application) {
        super(application);
        repository = new SeasonsRepository(application);
        favoritesRepository = new com.lineargs.watchnext.data.FavoritesRepository(application);
        episodes = Transformations.switchMap(seasonId, repository::getEpisodesForSeason);
        season = Transformations.switchMap(seasonId, repository::getSeason);
        seriesFavorite = Transformations.switchMap(serieId, favoritesRepository::getFavorite);
    }

    public void setSeasonId(String id) {
        seasonId.setValue(id);
    }

    public void setSerieId(int id) {
        serieId.setValue(id);
    }

    public LiveData<com.lineargs.watchnext.data.entity.Favorites> getSeriesFavorite() {
        return seriesFavorite;
    }

    public LiveData<List<Episodes>> getEpisodes() {
        return episodes;
    }

    public LiveData<com.lineargs.watchnext.data.entity.Seasons> getSeason() {
        return season;
    }

    public LiveData<Episodes> getEpisodeById(int id) {
        return repository.getEpisode(id);
    }
}
