package com.lineargs.watchnext.data;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class FavouritesViewModel extends AndroidViewModel {

    private WatchNextRepository repository;
    private LiveData<List<Favourites>> favourites;

    public FavouritesViewModel(@NonNull Application application) {
        super(application);
        repository = new WatchNextRepository(application);
        favourites = repository.getFavourites();
    }

    public LiveData<List<Favourites>> getFavourites() {
        return favourites;
    }
}
