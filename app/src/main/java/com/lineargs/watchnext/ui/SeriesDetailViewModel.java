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

    private final MutableLiveData<android.net.Uri> seriesUri = new MutableLiveData<>();

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

        serie = Transformations.switchMap(seriesUri, uri -> {
            int id = Integer.parseInt(uri.getLastPathSegment());
            String path = uri.getPath();
            if (path.contains("favorites")) {
                return Transformations.map(favoritesRepository.getFavorite(id), this::mapFavoriteToSerie);
            } else if (path.contains("popular")) {
                return seriesRepository.getPopularSerieLiveData(id);
            } else if (path.contains("toprated")) {
                return Transformations.map(seriesRepository.getTopRatedSerieLiveData(id), this::mapTopRatedToPopular);
            } else if (path.contains("ontheair")) {
                return Transformations.map(seriesRepository.getOnTheAirSerieLiveData(id), this::mapOnTheAirToPopular);
            } else {
                 return seriesRepository.getPopularSerieLiveData(id);
            }
        });

        LiveData<Integer> seriesId = Transformations.map(seriesUri, uri -> Integer.parseInt(uri.getLastPathSegment()));

        cast = Transformations.switchMap(seriesId, creditsRepository::getCast);
        crew = Transformations.switchMap(seriesId, creditsRepository::getCrew);
        reviews = Transformations.switchMap(seriesId, reviewsRepository::getReviews);
        videos = Transformations.switchMap(seriesId, videosRepository::getVideos);
        seasons = Transformations.switchMap(seriesId, seasonsRepository::getSeasons);
        favorite = Transformations.switchMap(seriesId, favoritesRepository::getFavorite);
    }

    public void setSeriesUri(android.net.Uri uri) {
        seriesUri.setValue(uri);
    }

    private PopularSerie mapFavoriteToSerie(Favorites favorites) {
        if (favorites == null) return null;
        PopularSerie serie = new PopularSerie();
        serie.setTmdbId(favorites.getTmdbId());
        serie.setTitle(favorites.getTitle());
        serie.setOverview(favorites.getOverview());
        serie.setPosterPath(favorites.getPosterPath());
        serie.setBackdropPath(favorites.getBackdropPath());
        serie.setVoteAverage(favorites.getVoteAverage());
        serie.setReleaseDate(favorites.getReleaseDate());
        serie.setOriginalLanguage(favorites.getOriginalLanguage());
        serie.setProductionCompanies(favorites.getProductionCompanies());
        serie.setGenres(favorites.getGenres());
        serie.setStatus(favorites.getStatus());
        return serie;
    }

    private PopularSerie mapTopRatedToPopular(com.lineargs.watchnext.data.entity.TopRatedSerie topRated) {
        if (topRated == null) return null;
        PopularSerie serie = new PopularSerie();
        serie.setTmdbId(topRated.getTmdbId());
        serie.setTitle(topRated.getTitle());
        serie.setOverview(topRated.getOverview());
        serie.setPosterPath(topRated.getPosterPath());
        serie.setBackdropPath(topRated.getBackdropPath());
        serie.setVoteAverage(topRated.getVoteAverage());
        serie.setReleaseDate(topRated.getReleaseDate());
        serie.setOriginalLanguage(topRated.getOriginalLanguage());
        serie.setProductionCompanies(topRated.getProductionCompanies());
        serie.setGenres(topRated.getGenres());
        serie.setStatus(topRated.getStatus());
        return serie;
    }

    private PopularSerie mapOnTheAirToPopular(com.lineargs.watchnext.data.entity.OnTheAirSerie onTheAir) {
        if (onTheAir == null) return null;
        PopularSerie serie = new PopularSerie();
        serie.setTmdbId(onTheAir.getTmdbId());
        serie.setTitle(onTheAir.getTitle());
        serie.setOverview(onTheAir.getOverview());
        serie.setPosterPath(onTheAir.getPosterPath());
        serie.setBackdropPath(onTheAir.getBackdropPath());
        serie.setVoteAverage(onTheAir.getVoteAverage());
        serie.setReleaseDate(onTheAir.getReleaseDate());
        serie.setOriginalLanguage(onTheAir.getOriginalLanguage());
        serie.setProductionCompanies(onTheAir.getProductionCompanies());
        serie.setGenres(onTheAir.getGenres());
        serie.setStatus(onTheAir.getStatus());
        return serie;
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
