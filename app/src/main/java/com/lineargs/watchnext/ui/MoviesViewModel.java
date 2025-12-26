package com.lineargs.watchnext.ui;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.lineargs.watchnext.data.MoviesRepository;
import com.lineargs.watchnext.data.entity.PopularMovie;
import com.lineargs.watchnext.data.entity.TheaterMovie;
import com.lineargs.watchnext.data.entity.TopRatedMovie;
import com.lineargs.watchnext.data.entity.UpcomingMovie;

import java.util.List;

public class MoviesViewModel extends AndroidViewModel {

    private final MoviesRepository repository;
    private final com.lineargs.watchnext.data.FavoritesRepository favoritesRepository;

    public MoviesViewModel(@NonNull Application application) {
        super(application);
        repository = new MoviesRepository(application);
        favoritesRepository = new com.lineargs.watchnext.data.FavoritesRepository(application);
    }

    public LiveData<List<PopularMovie>> getPopularMovies() {
        return repository.getPopularMovies();
    }

    public LiveData<List<TopRatedMovie>> getTopRatedMovies() {
        return repository.getTopRatedMovies();
    }

    public LiveData<List<UpcomingMovie>> getUpcomingMovies() {
        return repository.getUpcomingMovies();
    }

    public LiveData<List<TheaterMovie>> getTheaterMovies() {
        return repository.getTheaterMovies();
    }

    public LiveData<List<Integer>> getFavoriteMovieIds() {
        return favoritesRepository.getFavoriteMovieIds();
    }

    private final androidx.lifecycle.MutableLiveData<Boolean> isLoading = new androidx.lifecycle.MutableLiveData<>();
    private final androidx.lifecycle.MutableLiveData<String> errorMessage = new androidx.lifecycle.MutableLiveData<>();

    public void toggleFavorite(android.net.Uri uri, boolean remove) {
        if (remove) {
            favoritesRepository.removeFromFavorites(uri);
        } else {
            favoritesRepository.addMovieToFavorites(uri);
        }
    }

    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

    public void loadNextPopularPage() {
        if (Boolean.TRUE.equals(isLoading.getValue())) return;

        repository.fetchNextPopularMovies(new com.lineargs.watchnext.utils.NetworkStateCallback() {
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

        repository.fetchNextTopRatedMovies(new com.lineargs.watchnext.utils.NetworkStateCallback() {
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

    public void loadNextUpcomingPage() {
        if (Boolean.TRUE.equals(isLoading.getValue())) return;

        repository.fetchNextUpcomingMovies(new com.lineargs.watchnext.utils.NetworkStateCallback() {
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
