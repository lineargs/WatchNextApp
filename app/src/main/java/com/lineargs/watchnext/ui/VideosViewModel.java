package com.lineargs.watchnext.ui;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.lineargs.watchnext.data.VideosRepository;
import com.lineargs.watchnext.data.entity.Videos;

import java.util.List;

public class VideosViewModel extends AndroidViewModel {

    private final VideosRepository repository;
    private final MutableLiveData<Integer> movieId = new MutableLiveData<>();
    private final LiveData<List<Videos>> videos;

    public VideosViewModel(@NonNull Application application) {
        super(application);
        repository = new VideosRepository(application);
        videos = Transformations.switchMap(movieId, repository::getVideos);
    }

    public void setMovieId(int id) {
        movieId.setValue(id);
    }

    public LiveData<List<Videos>> getVideos() {
        return videos;
    }
}
