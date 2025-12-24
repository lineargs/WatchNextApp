package com.lineargs.watchnext.data;

import android.app.Application;
import androidx.lifecycle.LiveData;

import com.lineargs.watchnext.data.dao.FavoritesDao;
import com.lineargs.watchnext.data.entity.Favorites;

import java.util.List;

public class FavoritesRepository {

    private final FavoritesDao favoritesDao;

    public FavoritesRepository(Application application) {
        WatchNextDatabase database = WatchNextDatabase.getDatabase(application);
        favoritesDao = database.favoritesDao();
    }

    public LiveData<List<Favorites>> getFavorites() {
        return favoritesDao.getFavoritesLiveData();
    }

    public LiveData<Favorites> getFavorite(int id) {
        return favoritesDao.getFavoriteLiveData(id);
    }

    public void deleteFavorite(int id) {
        WatchNextDatabase.databaseWriteExecutor.execute(() -> favoritesDao.deleteFavorite(id));
    }
}
