package com.lineargs.watchnext.data;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.lineargs.watchnext.api.credits.Cast;
import com.lineargs.watchnext.api.credits.Crew;
import com.lineargs.watchnext.api.movies.ReviewsResult;
import com.lineargs.watchnext.api.search.SearchResults;
import com.lineargs.watchnext.api.series.seriesdetails.Season;
import com.lineargs.watchnext.api.videos.VideoDetails;
import com.lineargs.watchnext.data.credits.Credits;
import com.lineargs.watchnext.data.credits.CreditsDao;
import com.lineargs.watchnext.data.favourites.Favourites;
import com.lineargs.watchnext.data.favourites.FavouritesDao;
import com.lineargs.watchnext.data.movies.Movies;
import com.lineargs.watchnext.data.movies.MoviesDao;
import com.lineargs.watchnext.data.person.Person;
import com.lineargs.watchnext.data.person.PersonDao;
import com.lineargs.watchnext.data.reviews.Reviews;
import com.lineargs.watchnext.data.reviews.ReviewsDao;
import com.lineargs.watchnext.data.search.Search;
import com.lineargs.watchnext.data.search.SearchDao;
import com.lineargs.watchnext.data.seasons.Seasons;
import com.lineargs.watchnext.data.seasons.SeasonsDao;
import com.lineargs.watchnext.data.series.Series;
import com.lineargs.watchnext.data.series.SeriesDao;
import com.lineargs.watchnext.data.videos.Videos;
import com.lineargs.watchnext.data.videos.VideosDao;
import com.lineargs.watchnext.utils.MovieUtils;

import java.util.List;

public class WatchNextRepository {

    /* Final variable for our poster and backdrop path*/
    private static final String IMAGE_SMALL_BASE = "https://image.tmdb.org/t/p/w500";

    private MoviesDao moviesDao;
    private SeriesDao seriesDao;
    private VideosDao videosDao;
    private ReviewsDao reviewsDao;
    private CreditsDao creditsDao;
    private PersonDao personDao;
    private SeasonsDao seasonsDao;
    private SearchDao searchDao;
    private FavouritesDao favouritesDao;
    private LiveData<List<Movies>> popularMovies;
    private LiveData<List<Movies>> topRatedMovies;
    private LiveData<List<Movies>> upcomingMovies;
    private LiveData<List<Movies>> theatreMovies;
    private LiveData<List<Series>> popularSeries;
    private LiveData<List<Series>> topRatedSeries;
    private LiveData<List<Series>> onTheAirSeries;
    private LiveData<List<Favourites>> favourites;
    private LiveData<List<Search>> searchResults;

    public WatchNextRepository(Application application) {
        WatchNextDatabase database = WatchNextDatabase.getDatabase(application);
        moviesDao = database.moviesDao();
        popularMovies = moviesDao.getPopularMovies();
        topRatedMovies = moviesDao.getTopRatedMovies();
        upcomingMovies = moviesDao.getUpcomingMovies();
        theatreMovies = moviesDao.getTheaterMovies();
        seriesDao = database.seriesDao();
        onTheAirSeries = seriesDao.getOnTheAirSeries();
        topRatedSeries = seriesDao.getTopRatedSeries();
        popularSeries = seriesDao.getPopularSeries();
        favouritesDao = database.favouritesDao();
        favourites = favouritesDao.getAllFavourites();
        videosDao = database.videosDao();
        reviewsDao = database.reviewsDao();
        creditsDao = database.creditsDao();
        personDao = database.personDao();
        seasonsDao = database.seasonsDao();
        searchDao = database.searchDao();
        searchResults = searchDao.getSearchResults();
    }

    //Movies
    public LiveData<Movies> getMovie(int tmdbId) {
        return moviesDao.getMovie(tmdbId);
    }

    public LiveData<List<Movies>> getPopularMovies() {
        return popularMovies;
    }

    public LiveData<List<Movies>> getTopRatedMovies() {
        return topRatedMovies;
    }

    public LiveData<List<Movies>> getUpcomingMovies() {
        return upcomingMovies;
    }

    public LiveData<List<Movies>> getTheatreMovies() {
        return theatreMovies;
    }

    public void insertMovie(Movies movies) {
        new InsertMoviesTask(moviesDao).execute(movies);
    }

    public void updateMovie(Movies movies) {
        new UpdateMovieTask(moviesDao).execute(movies);
    }

    //Series
    public LiveData<List<Series>> getPopularSeries() {
        return popularSeries;
    }

    public LiveData<List<Series>> getTopRatedSeries() {
        return topRatedSeries;
    }

    public LiveData<List<Series>> getOnTheAirSeries() {
        return onTheAirSeries;
    }

    public void insertSeries(Series series) {
        new InsertSeriesTask(seriesDao).execute(series);
    }

    public void updateSeries(Series series) {
        new UpdateSeriesTask(seriesDao).execute(series);
    }

    public LiveData<Series> getSeries(int tmdbId) {
        return seriesDao.getSeries(tmdbId);
    }

    public void insertSeasons(List<Season> seasons, String name, int tmdbId) {
        //noinspection unchecked
        new InsertSeasonsTask(seasonsDao, name, tmdbId).execute(seasons);
    }

    public LiveData<List<Seasons>> getSeasons(int tmdbId) {
        return seasonsDao.getSeasons(tmdbId);
    }

    //Favourites
    public LiveData<List<Favourites>> getFavourites() {
        return favourites;
    }

    public void insertFavourites(Favourites favourites) {
        favouritesDao.insert(favourites);
    }

    //Videos
    public LiveData<List<Videos>> getVideos(int tmdbId) {
        return videosDao.getVideos(tmdbId);
    }

    public void insertVideos(com.lineargs.watchnext.api.videos.Videos videos, int tmdbId) {
        new InsertVideosTask(videosDao, tmdbId).execute(videos);
    }

    //Reviews
    public LiveData<List<Reviews>> getReviews(int tmdbId) {
        return reviewsDao.getReviews(tmdbId);
    }

    public void insertReviews(com.lineargs.watchnext.api.movies.Reviews reviews, int tmdbId) {
        new InsertReviewsTask(reviewsDao, tmdbId).execute(reviews);
    }

    //Credits
    public LiveData<List<Credits>> getCast(int tmdbId) {
        return creditsDao.getAllCast(tmdbId);
    }

    public LiveData<List<Credits>> getCrew(int tmdbId) {
        return creditsDao.getAllCrew(tmdbId);
    }

    public void insertCast(com.lineargs.watchnext.api.credits.Credits credits, int tmdbId) {
        new InsertCastTask(creditsDao, tmdbId).execute(credits);
    }

    public void insertCrew(com.lineargs.watchnext.api.credits.Credits credits, int tmdbId) {
        new InsertCrewTask(creditsDao, tmdbId).execute(credits);
    }

    //Person
    public void insertPerson(Person person) {
        new InsertPersonTask(personDao).execute(person);
    }

    public LiveData<Person> getPerson(int personId) {
        return personDao.getPerson(personId);
    }

    //Search
    public LiveData<List<Search>> getSearchResults() {
        return searchResults;
    }

    public void insertSearch(List<SearchResults> results) {//noinspection unchecked
        new InsertSearchTask(searchDao).execute(results);
    }

    //Movies Tasks
    private static class UpdateMovieTask extends AsyncTask<Movies, Void, Void> {

        private MoviesDao moviesDao;

        UpdateMovieTask(MoviesDao moviesDao) {
            this.moviesDao = moviesDao;
        }

        @Override
        protected Void doInBackground(final Movies... movies) {
            Movies movie = movies[0];
            moviesDao.updateMovie(movie.getTmdbId(), movie.getImdbId(), movie.getHomepage(), movie.getProductionCompanies(),
                    movie.getProductionCountries(), movie.getGenres(), movie.getRuntime(), movie.getStatus());
            return null;
        }
    }

    private static class InsertMoviesTask extends AsyncTask<Movies, Void, Void> {

        private MoviesDao moviesDao;

        InsertMoviesTask(MoviesDao moviesDao) {
            this.moviesDao = moviesDao;
        }

        @Override
        protected Void doInBackground(final Movies... movies) {
            moviesDao.insert(movies[0]);
            return null;
        }
    }

    //Series Tasks
    private static class InsertSeriesTask extends AsyncTask<Series, Void, Void> {

        private SeriesDao seriesDao;

        InsertSeriesTask(SeriesDao seriesDao) {
            this.seriesDao = seriesDao;
        }

        @Override
        protected Void doInBackground(Series... series) {
            seriesDao.insert(series[0]);
            return null;
        }
    }

    private static class UpdateSeriesTask extends AsyncTask<Series, Void, Void> {

        private SeriesDao seriesDao;

        UpdateSeriesTask(SeriesDao seriesDao) {
            this.seriesDao = seriesDao;
        }

        @Override
        protected Void doInBackground(Series... series) {
            Series serie = series[0];
            seriesDao.updateSeries(serie.getTmdbId(), serie.getHomepage(),
                    serie.getProductionCompanies(), serie.getNetworks(),
                    serie.getStatus(), serie.getGenres());
            return null;
        }
    }

    private static class InsertSeasonsTask extends AsyncTask<List<Season>, Void, Void> {

        private SeasonsDao seasonsDao;
        private String name;
        private int tmdbId;

        InsertSeasonsTask(SeasonsDao seasonsDao, String name, int tmdbId) {
            this.seasonsDao = seasonsDao;
            this.name = name;
            this.tmdbId = tmdbId;
        }

        @SafeVarargs
        @Override
        protected final Void doInBackground(List<Season>... seasonList) {
            List<Season> seasons = seasonList[0];
            for (Season season : seasons) {
                Seasons seasonRoom = new Seasons();
                seasonRoom.setEpisodeCount(season.getEpisodeCount());
                seasonRoom.setPosterPath(IMAGE_SMALL_BASE + season.getPosterPath());
                seasonRoom.setSeasonId(season.getId());
                seasonRoom.setSeasonNumber(String.valueOf(season.getSeasonNumber()));
                seasonRoom.setShowName(name);
                seasonRoom.setTmdbId(tmdbId);
                seasonsDao.insert(seasonRoom);
            }
            return null;
        }
    }

    //Videos Tasks
    private static class InsertVideosTask extends AsyncTask<com.lineargs.watchnext.api.videos.Videos, Void, Void> {

        private VideosDao videosDao;
        private int tmdbId;

        InsertVideosTask(VideosDao videosDao, int tmdbId) {
            this.videosDao = videosDao;
            this.tmdbId = tmdbId;
        }

        @Override
        protected Void doInBackground(com.lineargs.watchnext.api.videos.Videos... videos) {
            com.lineargs.watchnext.api.videos.Videos tmdbVideos = videos[0];
            List<VideoDetails> resultList = tmdbVideos.getResults();
            for (VideoDetails result : resultList) {
                Videos video = new Videos();
                video.setTmdbId(tmdbId);
                video.setName(result.getName());
                video.setKey(result.getKey());
                video.setImage(MovieUtils.getYouTubeImage(result.getKey()));
                videosDao.insert(video);
            }
            return null;
        }
    }

    //Reviews Tasks
    private static class InsertReviewsTask extends AsyncTask<com.lineargs.watchnext.api.movies.Reviews, Void, Void> {

        private ReviewsDao reviewsDao;
        private int tmdbId;

        InsertReviewsTask(ReviewsDao reviewsDao, int tmdbId) {
            this.reviewsDao = reviewsDao;
            this.tmdbId = tmdbId;
        }

        @Override
        protected Void doInBackground(com.lineargs.watchnext.api.movies.Reviews... reviews) {
            com.lineargs.watchnext.api.movies.Reviews tmdbReviews = reviews[0];
            List<ReviewsResult> resultList = tmdbReviews.getResults();
            for (ReviewsResult result : resultList) {
                Reviews review = new Reviews();
                review.setTmdbId(tmdbId);
                review.setAuthor(result.getAuthor());
                review.setContent(result.getContent());
                review.setUrl(result.getUrl());
                reviewsDao.insert(review);
            }
            return null;
        }
    }

    //Credits Tasks
    private static class InsertCastTask extends AsyncTask<com.lineargs.watchnext.api.credits.Credits, Void, Void> {

        private CreditsDao creditsDao;
        private int tmdbId;

        InsertCastTask(CreditsDao creditsDao, int tmdbId) {
            this.creditsDao = creditsDao;
            this.tmdbId = tmdbId;
        }

        @Override
        protected Void doInBackground(com.lineargs.watchnext.api.credits.Credits... credits) {
            com.lineargs.watchnext.api.credits.Credits tmdbCredits = credits[0];
            List<Cast> castList = tmdbCredits.getCast();
            for (Cast cast : castList) {
                Credits credit = new Credits();
                credit.setTmdbId(tmdbId);
                credit.setName(cast.getName());
                credit.setCharacterName(cast.getCharacter());
                credit.setPersonId(cast.getId());
                credit.setCreditId(cast.getCreditId());
                credit.setProfilePath(IMAGE_SMALL_BASE + cast.getProfilePath());
                credit.setType(0);
                creditsDao.insertCredits(credit);
            }
            return null;
        }
    }

    private static class InsertCrewTask extends AsyncTask<com.lineargs.watchnext.api.credits.Credits, Void, Void> {

        private CreditsDao creditsDao;
        private int tmdbId;

        InsertCrewTask(CreditsDao creditsDao, int tmdbId) {
            this.creditsDao = creditsDao;
            this.tmdbId = tmdbId;
        }

        @Override
        protected Void doInBackground(com.lineargs.watchnext.api.credits.Credits... credits) {
            com.lineargs.watchnext.api.credits.Credits tmdbCredits = credits[0];
            List<Crew> crewList = tmdbCredits.getCrew();
            for (Crew crew : crewList) {
                Credits credit = new Credits();
                credit.setTmdbId(tmdbId);
                credit.setName(crew.getName());
                credit.setJob(crew.getJob());
                credit.setCreditId(crew.getCreditId());
                credit.setPersonId(crew.getId());
                credit.setProfilePath(IMAGE_SMALL_BASE + crew.getProfilePath());
                credit.setType(1);
                creditsDao.insertCredits(credit);
            }
            return null;
        }
    }

    //Person Tasks
    private static class InsertPersonTask extends AsyncTask<Person, Void, Void> {

        private PersonDao personDao;

        InsertPersonTask(PersonDao personDao) {
            this.personDao = personDao;
        }

        @Override
        protected Void doInBackground(Person... people) {
            personDao.insert(people[0]);
            return null;
        }
    }

    private static class InsertSearchTask extends AsyncTask<List<SearchResults>, Void, Void> {

        private SearchDao searchDao;

        InsertSearchTask(SearchDao searchDao) {
            this.searchDao = searchDao;
        }

        @SafeVarargs
        @Override
        protected final Void doInBackground(List<SearchResults>... lists) {
            searchDao.deleteAll();
            List<SearchResults> searchResults = lists[0];
            Search search = new Search();
            for (SearchResults result : searchResults) {
                if (result.getName() != null) {
                    search.setTmdbId(result.getId());
                    search.setTitle(result.getName());
                    search.setPosterPath(IMAGE_SMALL_BASE + result.getPosterPath());
                } else if (result.getTitle() != null) {
                    search.setTmdbId(result.getId());
                    search.setTitle(result.getTitle());
                    search.setPosterPath(IMAGE_SMALL_BASE + result.getPosterPath());
                }
                searchDao.insert(search);
            }
            return null;
        }
    }
}
