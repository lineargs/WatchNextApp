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

    public MoviesViewModel(@NonNull Application application) {
        super(application);
        repository = new MoviesRepository(application);
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
}
