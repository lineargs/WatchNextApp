package com.lineargs.watchnext.ui;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.lineargs.watchnext.data.SearchRepository;
import com.lineargs.watchnext.data.entity.Search;
import com.lineargs.watchnext.data.entity.SearchTv;

import java.util.List;

public class SearchViewModel extends AndroidViewModel {

    private final SearchRepository repository;
    private final com.lineargs.watchnext.data.FavoritesRepository favoritesRepository;
    private final LiveData<List<Integer>> favoriteMovieIds;
    private final LiveData<List<Integer>> favoriteSeriesIds;

    public SearchViewModel(@NonNull Application application) {
        super(application);
        repository = new SearchRepository(application);
        favoritesRepository = new com.lineargs.watchnext.data.FavoritesRepository(application);
        favoriteMovieIds = favoritesRepository.getFavoriteMovieIds();
        favoriteSeriesIds = favoritesRepository.getFavoriteSeriesIds();
    }

    public LiveData<List<Search>> getSearchMovies() {
        return repository.getSearchMovies();
    }

    public LiveData<List<SearchTv>> getSearchTvSeries() {
        return repository.getSearchTvSeries();
    }

    public LiveData<List<Integer>> getFavoriteMovieIds() {
        return favoriteMovieIds;
    }

    public LiveData<List<Integer>> getFavoriteSeriesIds() {
        return favoriteSeriesIds;
    }

    public void removeFavorite(android.net.Uri uri) {
        favoritesRepository.removeFromFavorites(uri);
    }
}
