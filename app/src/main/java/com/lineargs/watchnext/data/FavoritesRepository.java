package com.lineargs.watchnext.data;

import android.app.Application;
import androidx.lifecycle.LiveData;

import com.lineargs.watchnext.data.dao.FavoritesDao;
import com.lineargs.watchnext.data.entity.Favorites;

import java.util.List;

public class FavoritesRepository {

    private final FavoritesDao favoritesDao;

    private final Application application;

    public FavoritesRepository(Application application) {
        this.application = application;
        WatchNextDatabase database = WatchNextDatabase.getDatabase(application);
        favoritesDao = database.favoritesDao();
    }

    public LiveData<List<Favorites>> getFavorites() {
        return favoritesDao.getFavoritesLiveData();
    }

    public LiveData<Favorites> getFavorite(int id) {
        return favoritesDao.getFavoriteLiveData(id);
    }

    public LiveData<List<Integer>> getFavoriteMovieIds() {
        return favoritesDao.getFavoriteMovieIds();
    }

    public LiveData<List<Integer>> getFavoriteSeriesIds() {
        return favoritesDao.getFavoriteSeriesIds();
    }

    public LiveData<Integer> getMoviesCount() {
        return favoritesDao.getMoviesCountLiveData();
    }

    public LiveData<Integer> getSeriesCount() {
        return favoritesDao.getSeriesCountLiveData();
    }

    public void deleteFavorite(int id) {
        WatchNextDatabase.databaseWriteExecutor.execute(() -> favoritesDao.deleteFavorite(id));
    }

    public void addMovieToFavorites(android.net.Uri uri) {
        com.lineargs.watchnext.utils.dbutils.DbUtils.addMovieToFavorites(application, uri);
    }

    public void addMovieToFavorites(com.lineargs.watchnext.data.entity.Movie movie) {
        com.lineargs.watchnext.utils.dbutils.DbUtils.addMovieToFavorites(application, movie);
    }

    public void addSeriesToFavorites(android.net.Uri uri) {
        com.lineargs.watchnext.utils.dbutils.DbUtils.addTVToFavorites(application, uri);
    }

    public void addSeriesToFavorites(com.lineargs.watchnext.data.entity.PopularSerie serie) {
        com.lineargs.watchnext.utils.dbutils.DbUtils.addSerieToFavorites(application, serie);
    }

    public void removeFromFavorites(android.net.Uri uri) {
        com.lineargs.watchnext.utils.dbutils.DbUtils.removeFromFavorites(application, uri);
    }

    public void updateSubscription(long id, int notify) {
        com.lineargs.watchnext.utils.dbutils.DbUtils.updateSubscription(application, id, notify);
    }

    public boolean isFavorite(int id) {
        return favoritesDao.checkFavorite(id) > 0;
    }
}
