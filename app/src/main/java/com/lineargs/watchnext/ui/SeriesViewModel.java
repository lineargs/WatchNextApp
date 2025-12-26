package com.lineargs.watchnext.ui;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.lineargs.watchnext.data.SeriesRepository;
import com.lineargs.watchnext.data.entity.OnTheAirSerie;
import com.lineargs.watchnext.data.entity.PopularSerie;
import com.lineargs.watchnext.data.entity.TopRatedSerie;

import java.util.List;

public class SeriesViewModel extends AndroidViewModel {

    private final SeriesRepository repository;
    private final com.lineargs.watchnext.data.FavoritesRepository favoritesRepository;

    private final androidx.lifecycle.MutableLiveData<Boolean> isLoading = new androidx.lifecycle.MutableLiveData<>(false);
    private final androidx.lifecycle.MutableLiveData<String> errorMessage = new androidx.lifecycle.MutableLiveData<>(null);

    public SeriesViewModel(@NonNull Application application) {
        super(application);
        repository = new SeriesRepository(application);
        favoritesRepository = new com.lineargs.watchnext.data.FavoritesRepository(application);
    }

    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

    public LiveData<List<PopularSerie>> getPopularSeries() {
        return repository.getPopularSeries();
    }

    public LiveData<List<TopRatedSerie>> getTopRatedSeries() {
        return repository.getTopRatedSeries();
    }

    public LiveData<List<OnTheAirSerie>> getOnTheAirSeries() {
        return repository.getOnTheAirSeries();
    }

    public LiveData<List<Integer>> getFavoriteSeriesIds() {
        return favoritesRepository.getFavoriteSeriesIds();
    }

    public void toggleFavorite(android.net.Uri uri, boolean remove) {
        if (remove) {
            favoritesRepository.removeFromFavorites(uri);
        } else {
            favoritesRepository.addSeriesToFavorites(uri);
        }
    }

    public void loadNextPopularPage() {
        if (Boolean.TRUE.equals(isLoading.getValue())) return;

        repository.fetchNextPopularSeries(new com.lineargs.watchnext.utils.NetworkStateCallback() {
            @Override
            public void onLoading() {
                isLoading.postValue(true);
            }

            @Override
            public void onSuccess() {
                isLoading.postValue(false);
            }

            @Override
            public void onError(String message) {
                isLoading.postValue(false);
                errorMessage.postValue(message);
            }
        });
    }

    public void loadNextTopRatedPage() {
        if (Boolean.TRUE.equals(isLoading.getValue())) return;

        repository.fetchNextTopRatedSeries(new com.lineargs.watchnext.utils.NetworkStateCallback() {
            @Override
            public void onLoading() {
                isLoading.postValue(true);
            }

            @Override
            public void onSuccess() {
                isLoading.postValue(false);
            }

            @Override
            public void onError(String message) {
                isLoading.postValue(false);
                errorMessage.postValue(message);
            }
        });
    }

    public void loadNextOnTheAirPage() {
        if (Boolean.TRUE.equals(isLoading.getValue())) return;

        repository.fetchNextOnTheAirSeries(new com.lineargs.watchnext.utils.NetworkStateCallback() {
            @Override
            public void onLoading() {
                isLoading.postValue(true);
            }

            @Override
            public void onSuccess() {
                isLoading.postValue(false);
            }

            @Override
            public void onError(String message) {
                isLoading.postValue(false);
                errorMessage.postValue(message);
            }
        });
    }
}
