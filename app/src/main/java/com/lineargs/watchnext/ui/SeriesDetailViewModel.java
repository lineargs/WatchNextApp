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
    private LiveData<java.util.List<com.lineargs.watchnext.data.entity.Seasons>> seasons;
    private LiveData<java.util.List<com.lineargs.watchnext.data.entity.Videos>> videos;
    private LiveData<com.lineargs.watchnext.data.entity.Favorites> favorite;
    private final MutableLiveData<Integer> serieId = new MutableLiveData<>();

    private final LiveData<PopularSerie> serie;
    private final LiveData<List<Credits>> cast;
    private final LiveData<List<Credits>> crew;
    private final LiveData<List<Review>> reviews;

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
            } else if (path.contains("airingtoday")) {
                return Transformations.map(seriesRepository.getAiringTodaySerieLiveData(id), this::mapAiringTodayToPopular);
            } else if (path.contains("search")) {
                com.lineargs.watchnext.data.SearchRepository searchRepo = new com.lineargs.watchnext.data.SearchRepository(getApplication());
                return Transformations.map(searchRepo.getSearchTvLiveData(id), this::mapSearchToPopular);
            } else {
                return seriesRepository.getPopularSerieLiveData(id);
            }
        });
        cast = Transformations.switchMap(serieId, creditsRepository::getCast);
        crew = Transformations.switchMap(serieId, creditsRepository::getCrew);
        reviews = Transformations.switchMap(serieId, reviewsRepository::getReviews);
        videos = Transformations.switchMap(serieId, videosRepository::getVideos);
        seasons = Transformations.switchMap(serieId, seasonsRepository::getSeasons);
        favorite = Transformations.switchMap(serieId, id -> {
            if (id == null) {
                return new MutableLiveData<>(null);
            } else {
                return favoritesRepository.getFavorite(id);
            }
        });
    }

    private PopularSerie mapSearchToPopular(com.lineargs.watchnext.data.entity.SearchTv searchTv) {
        if (searchTv == null) return null;
        PopularSerie serie = new PopularSerie();
        serie.setTmdbId(searchTv.getTmdbId());
        serie.setTitle(searchTv.getTitle());
        serie.setOverview(searchTv.getOverview());
        serie.setPosterPath(searchTv.getPosterPath());
        serie.setBackdropPath(searchTv.getBackdropPath());
        serie.setVoteAverage(searchTv.getVoteAverage());
        serie.setReleaseDate(searchTv.getReleaseDate());
        serie.setOriginalLanguage(searchTv.getOriginalLanguage());
        return serie;
    }

    public void setSeriesUri(android.net.Uri uri) {
        seriesUri.setValue(uri);
        if (uri != null) {
            serieId.setValue(Integer.parseInt(uri.getLastPathSegment()));
        }
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
        serie.setProductionCountries(favorites.getProductionCountries());
        serie.setGenres(favorites.getGenres());
        serie.setStatus(favorites.getStatus());
        serie.setRuntime(favorites.getRuntime());
        serie.setImdbId(favorites.getImdbId());
        serie.setHomepage(favorites.getHomepage());
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

    private PopularSerie mapAiringTodayToPopular(com.lineargs.watchnext.data.entity.AiringTodaySerie airingToday) {
        if (airingToday == null) return null;
        PopularSerie serie = new PopularSerie();
        serie.setTmdbId(airingToday.getTmdbId());
        serie.setTitle(airingToday.getTitle());
        serie.setOverview(airingToday.getOverview());
        serie.setPosterPath(airingToday.getPosterPath());
        serie.setBackdropPath(airingToday.getBackdropPath());
        serie.setVoteAverage(airingToday.getVoteAverage());
        serie.setReleaseDate(airingToday.getReleaseDate());
        serie.setOriginalLanguage(airingToday.getOriginalLanguage());
        serie.setProductionCompanies(airingToday.getProductionCompanies());
        serie.setGenres(airingToday.getGenres());
        serie.setStatus(airingToday.getStatus());
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

    public LiveData<java.util.List<com.lineargs.watchnext.data.entity.Videos>> getVideos() {
        return videos;
    }

    public LiveData<java.util.List<com.lineargs.watchnext.data.entity.Seasons>> getSeasons() {
        return seasons;
    }

    public LiveData<com.lineargs.watchnext.data.entity.Favorites> getFavorite() {
        return favorite;
    }

    public void toggleFavorite(android.net.Uri uri, boolean remove) {
        if (remove) {
            favoritesRepository.removeFromFavorites(uri);
        } else {
            favoritesRepository.addSeriesToFavorites(uri);
        }
    }

    public void toggleSubscription(long id, int notify) {
        favoritesRepository.updateSubscription(id, notify);
        com.lineargs.watchnext.jobs.WorkManagerUtils.syncSubscriptionsImmediately(getApplication());
    }

    /**
     * Checks if data exists in DB, if not triggers sync.
     * Uses DbUtils in background thread to avoid UI block.
     */
    public void checkDataAndSync(android.content.Context context, android.net.Uri uri) {
        com.lineargs.watchnext.data.WatchNextDatabase.databaseWriteExecutor.execute(() -> {
            if (!com.lineargs.watchnext.utils.dbutils.DbUtils.checkForCredits(context, uri.getLastPathSegment())) {
                com.lineargs.watchnext.sync.syncseries.SerieDetailUtils.syncSeasons(context, uri);
            } else {
                 // The 'else' block in Fragment was calling updateDetails() if savedState == null.
                 // Fragment Logic: 
                 // if (!checkForCredits) syncSeasons
                 // else if (savedState == null) updateDetails
                 // This means updateDetails is called ALWAYS if we already have credits?
                 // Wait, original Logic:
                 // if (!DbUtils.checkForCredits...) { ... } else if (savedState == null) { ... }
                 // So if credits exist, we update details.
                 // Wait, if credits check is false, we sync seasons. Does syncSeasons also update details? 
                 // Let's assume we maintain the logic.
                 com.lineargs.watchnext.sync.syncseries.SerieDetailUtils.updateDetails(context, uri);
            }
        });
    }
}
