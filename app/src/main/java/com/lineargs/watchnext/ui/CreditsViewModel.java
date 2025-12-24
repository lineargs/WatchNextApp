package com.lineargs.watchnext.ui;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.lineargs.watchnext.data.CreditsRepository;
import com.lineargs.watchnext.data.entity.Credits;

import java.util.List;

public class CreditsViewModel extends AndroidViewModel {

    private final CreditsRepository repository;
    private final MutableLiveData<Integer> movieId = new MutableLiveData<>();
    private final LiveData<List<Credits>> cast;
    private final LiveData<List<Credits>> crew;

    public CreditsViewModel(@NonNull Application application) {
        super(application);
        repository = new CreditsRepository(application);
        cast = Transformations.switchMap(movieId, repository::getCast);
        crew = Transformations.switchMap(movieId, repository::getCrew);
    }

    public void setMovieId(int id) {
        movieId.setValue(id);
    }

    public LiveData<List<Credits>> getCast() {
        return cast;
    }

    public LiveData<List<Credits>> getCrew() {
        return crew;
    }
}
