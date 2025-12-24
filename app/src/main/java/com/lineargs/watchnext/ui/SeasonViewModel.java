package com.lineargs.watchnext.ui;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.lineargs.watchnext.data.SeasonsRepository;
import com.lineargs.watchnext.data.entity.Seasons;

import java.util.List;

public class SeasonViewModel extends AndroidViewModel {

    private final SeasonsRepository repository;
    private final MutableLiveData<Integer> serieId = new MutableLiveData<>();
    private final LiveData<List<Seasons>> seasons;

    public SeasonViewModel(@NonNull Application application) {
        super(application);
        repository = new SeasonsRepository(application);
        seasons = Transformations.switchMap(serieId, repository::getSeasons);
    }

    public void setSerieId(int id) {
        serieId.setValue(id);
    }

    public LiveData<List<Seasons>> getSeasons() {
        return seasons;
    }
}
