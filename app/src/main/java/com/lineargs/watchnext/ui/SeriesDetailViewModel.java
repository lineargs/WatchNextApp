package com.lineargs.watchnext.ui;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.lineargs.watchnext.data.CreditsRepository;
import com.lineargs.watchnext.data.FavoritesRepository;
import com.lineargs.watchnext.data.ReviewsRepository;
import com.lineargs.watchnext.data.SeasonsRepository;
import com.lineargs.watchnext.data.SeriesRepository;
import com.lineargs.watchnext.data.VideosRepository;
import com.lineargs.watchnext.data.entity.Credits;
import com.lineargs.watchnext.data.entity.Favorites;
import com.lineargs.watchnext.data.entity.PopularSerie;
import com.lineargs.watchnext.data.entity.Review;
import com.lineargs.watchnext.data.entity.Seasons;
import com.lineargs.watchnext.data.entity.Videos;

import java.util.List;

public class SeriesDetailViewModel extends AndroidViewModel {

    private final SeriesRepository seriesRepository;
    private final CreditsRepository creditsRepository;
    private final ReviewsRepository reviewsRepository;
    private final VideosRepository videosRepository;
    private final SeasonsRepository seasonsRepository;
    private final FavoritesRepository favoritesRepository;

    private final MutableLiveData<Integer> seriesId = new MutableLiveData<>();

    private final LiveData<PopularSerie> serie;
    private final LiveData<List<Credits>> cast;
    private final LiveData<List<Credits>> crew;
    private final LiveData<List<Review>> reviews;
    private final LiveData<List<Videos>> videos;
    private final LiveData<List<Seasons>> seasons;
    private final LiveData<Favorites> favorite;

    public SeriesDetailViewModel(@NonNull Application application) {
        super(application);
        seriesRepository = new SeriesRepository(application);
        creditsRepository = new CreditsRepository(application);
        reviewsRepository = new ReviewsRepository(application);
        videosRepository = new VideosRepository(application);
        seasonsRepository = new SeasonsRepository(application);
        favoritesRepository = new FavoritesRepository(application);

        serie = Transformations.switchMap(seriesId, seriesRepository::getPopularSerieLiveData);
        cast = Transformations.switchMap(seriesId, creditsRepository::getCast);
        crew = Transformations.switchMap(seriesId, creditsRepository::getCrew);
        reviews = Transformations.switchMap(seriesId, reviewsRepository::getReviews);
        videos = Transformations.switchMap(seriesId, videosRepository::getVideos);
        seasons = Transformations.switchMap(seriesId, seasonsRepository::getSeasons);
        favorite = Transformations.switchMap(seriesId, favoritesRepository::getFavorite);
    }

    public void setSeriesId(int id) {
        seriesId.setValue(id);
    }

    public LiveData<PopularSerie> getSerie() {
        return serie;
    }

    public LiveData<List<Credits>> getCast() {
        return cast;
    }

    public LiveData<List<Credits>> getCrew() {
        return crew;
    }

    public LiveData<List<Review>> getReviews() {
        return reviews;
    }

    public LiveData<List<Videos>> getVideos() {
        return videos;
    }

    public LiveData<List<Seasons>> getSeasons() {
        return seasons;
    }

    public LiveData<Favorites> getFavorite() {
        return favorite;
    }
}
