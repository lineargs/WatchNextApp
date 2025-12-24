package com.lineargs.watchnext.ui;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.lineargs.watchnext.data.FavoritesRepository;
import com.lineargs.watchnext.data.entity.Favorites;

import java.util.List;

public class FavoritesViewModel extends AndroidViewModel {

    private final FavoritesRepository repository;

    public FavoritesViewModel(@NonNull Application application) {
        super(application);
        repository = new FavoritesRepository(application);
    }

    public LiveData<List<Favorites>> getFavorites() {
        return repository.getFavorites();
    }

    public void removeFavorite(int id) {
        repository.deleteFavorite(id);
    }
}
