package com.lineargs.watchnext.data;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.migration.Migration;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;

@Database(entities = {Credits.class, Episodes.class, Favourites.class,
        Movies.class, Person.class, Reviews.class,
        Seasons.class, Series.class, Videos.class}, version = WatchNextDatabase.DB_VERSION)
public abstract class WatchNextDatabase extends RoomDatabase {

    private static WatchNextDatabase INSTANCE;

    private static final int VERSION_41 = 41;

    private static final int VERSION_42_ROOM = 42;

    static final int DB_VERSION = VERSION_42_ROOM;

    static WatchNextDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (WatchNextDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            WatchNextDatabase.class, "watchnext.db")
                            //TODO Remove callback implementation, here only for testing
                            .addCallback(callback)
                            .addMigrations(
                                    MIGRATION_41_42
//                                    MIGRATION_42_43
                                    //MIGRATION_43_44
                            )
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    private static final Migration MIGRATION_41_42 = new Migration(VERSION_41, VERSION_42_ROOM) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            /* If we do not want to lose data we need to create a new table for
             * example credits_new -> copy all the data from credits
             * Drop credits and then ALTER credits_new -> credits
             * We will recreate only the favourites table without loosing the data as any other
             * we can sync from the MovieDb.
             */
            //Credits table
            database.execSQL("DROP TABLE credits");
            database.execSQL("CREATE TABLE credits ('tmdb_id' INTEGER NOT NULL, 'character_name' TEXT," +
                    " 'name' TEXT, 'profile_path' TEXT, 'credit_id' TEXT, 'id' INTEGER NOT NULL, 'job' TEXT, 'type' INTEGER NOT NULL," +
                    " 'person_id' INTEGER NOT NULL, PRIMARY KEY ('id'))");

            //Episodes table
            database.execSQL("DROP TABLE episodes");
            database.execSQL("CREATE TABLE episodes('tmdb_id' INTEGER NOT NULL, 'overview' TEXT, 'directors' TEXT, " +
                    "'season_number' INTEGER NOT NULL, 'season_id' INTEGER NOT NULL, 'still_path' TEXT, 'writers' TEXT, " +
                    "'guest_stars' TEXT, 'episode_number' INTEGER NOT NULL, 'release_date' TEXT, 'vote_average' TEXT, " +
                    "'name' TEXT, 'id' INTEGER NOT NULL, 'vote_count' INTEGER NOT NULL, PRIMARY KEY ('id'))");

            //Favourites table
            database.execSQL("CREATE TABLE favourites ('tmdb_id' INTEGER NOT NULL, 'overview' TEXT NOT NULL, " +
                    "'favourite_type' INTEGER NOT NULL, 'imdb_id' TEXT, " +
                    "'runtime' INTEGER NOT NULL, 'title' TEXT NOT NULL, 'networks' TEXT, 'poster_path' TEXT, " +
                    "'backdrop_path' TEXT, 'release_date' TEXT, 'production_companies' TEXT, 'genres' TEXT, " +
                    "'vote_average' TEXT, 'production_countries' TEXT, 'id' INTEGER NOT NULL, 'homepage' TEXT, " +
                    "'status' TEXT, PRIMARY KEY ('id'))");
            /*
             * The reason having 0 for runtime is because Room cannot handle properly migration.
             * In our case for Series runtime is null but assigning Default value does not get picked up.
             * So we assign 0 just to have which are favourites, runtime will get synced again on next
             * Details Activity opening.
             */
            database.execSQL("INSERT INTO favourites (tmdb_id, overview, favourite_type, imdb_id, runtime, title, " +
                    "poster_path, backdrop_path, release_date, production_companies, genres, vote_average, " +
                    "production_countries, homepage, status) SELECT movie_id, overview, type, imdb_id, " +
                    "0, title, poster_path, backdrop_path, release_date, production_companies, " +
                    "genres, vote_average, production_countries, homepage, status FROM favorites");
            database.execSQL("DROP TABLE favorites");

            //Movies table
            database.execSQL("DROP TABLE popularmovies");
            database.execSQL("DROP TABLE topmovies");
            database.execSQL("DROP TABLE upcomingmovies");
            database.execSQL("DROP TABLE theatermovies");
            database.execSQL("CREATE TABLE movies ('tmdb_id' INTEGER NOT NULL, 'overview' TEXT NOT NULL, 'imdb_id' TEXT, " +
                    "'runtime' INTEGER NOT NULL, 'title' TEXT NOT NULL, 'type' INTEGER NOT NULL, 'poster_path' TEXT, " +
                    "'backdrop_path' TEXT, 'release_date' TEXT, 'production_companies' TEXT, 'genres' TEXT, " +
                    "'vote_average' TEXT, 'production_countries' TEXT, 'id' INTEGER NOT NULL, 'homepage' TEXT, " +
                    "'status' TEXT, PRIMARY KEY ('id'))");

            //Person table
            database.execSQL("DROP TABLE person");
            database.execSQL("CREATE TABLE person ('place_of_birth' TEXT, 'person_id' INTEGER NOT NULL, 'name' TEXT, " +
                    "'profile_path' TEXT, 'id' INTEGER NOT NULL, 'biography' TEXT, 'homepage' TEXT, PRIMARY KEY ('id'))");

            //Reviews table
            database.execSQL("DROP TABLE review");
            database.execSQL("CREATE TABLE reviews ('tmdb_id' INTEGER NOT NULL, 'id' INTEGER NOT NULL, 'author' TEXT, 'content' TEXT, " +
                    "'url' TEXT, PRIMARY KEY ('id'))");

            //Search tables
            database.execSQL("DROP TABLE search");
            database.execSQL("DROP TABLE searchtv");
            database.execSQL("CREATE TABLE search ('tmdb_id' INTEGER NOT NULL, 'id' INTEGER NOT NULL, 'title' TEXT NOT NULL, " +
                    "'poster_path' TEXT NOT NULL, PRIMARY KEY ('id'))");

            //Seasons table
            database.execSQL("DROP TABLE seasons");
            database.execSQL("CREATE TABLE seasons ('tmdb_id' INTEGER NOT NULL, 'episode_count' INTEGER NOT NULL, 'show_name' TEXT, " +
                    "'season_number' TEXT, 'season_id' INTEGER NOT NULL, 'id' INTEGER NOT NULL, " +
                    "'poster_path' TEXT, PRIMARY KEY ('id'))");

            //Series table
            database.execSQL("DROP TABLE ontheairseries");
            database.execSQL("DROP TABLE popularseries");
            database.execSQL("DROP TABLE topseries");
            database.execSQL("CREATE TABLE series ('tmdb_id' INTEGER NOT NULL, 'overview' TEXT NOT NULL, " +
                    "'title' TEXT NOT NULL, 'networks' TEXT, 'type' INTEGER NOT NULL, 'poster_path' TEXT, " +
                    "'backdrop_path' TEXT, 'release_date' TEXT, 'production_companies' TEXT, 'genres' TEXT, " +
                    "'vote_average' TEXT, 'id' INTEGER NOT NULL, 'homepage' TEXT, " +
                    "'status' TEXT, PRIMARY KEY ('id'))");

            //Videos table
            database.execSQL("DROP TABLE videos");
            database.execSQL("CREATE TABLE videos ('name' TEXT, 'tmdb_id' INTEGER NOT NULL, 'image' TEXT, " +
                    "'id' INTEGER NOT NULL, 'key' TEXT, PRIMARY KEY ('id'))");

            //UNIQUE INDICES
            database.execSQL("CREATE UNIQUE INDEX 'index_videos_key' ON videos ('key')");
            database.execSQL("CREATE UNIQUE INDEX 'index_reviews_key' ON reviews ('url')");
            database.execSQL("CREATE UNIQUE INDEX 'index_person_key' ON person ('person_id')");
            database.execSQL("CREATE UNIQUE INDEX 'index_credits_key' ON credits ('credit_id')");
            database.execSQL("CREATE UNIQUE INDEX 'index_seasons_key' ON seasons ('season_id')");
            Log.e("SUCCESS", "MIGRATION SUCCESS");
        }
    };

    /**
     * Testing purposes only
     */
    private static RoomDatabase.Callback callback = new RoomDatabase.Callback() {
        @Override
        public void onOpen(@NonNull SupportSQLiteDatabase db) {
            super.onOpen(db);
            new PopulateDbAsync(INSTANCE).execute();
        }
    };

    /**
     * Testing purposes only
     * Populate the database in the background.
     */
    private static class PopulateDbAsync extends AsyncTask<Void, Void, Void> {

        private final MoviesDao dao;
        private final SeriesDao seriesDao;
        Movies popularMovie = new Movies();
        Movies topMovie = new Movies();
        Movies upcomingMovie = new Movies();
        Movies theatreMovie = new Movies();
        Series popularSeries = new Series();
        Series topSeries = new Series();

        PopulateDbAsync(WatchNextDatabase db) {
            dao = db.moviesDao();
            seriesDao = db.seriesDao();
        }

        @Override
        protected Void doInBackground(final Void... params) {
            popularMovie.setTmdbId(301527);
            popularMovie.setVoteAverage("");
            popularMovie.setTitle("Popular");
            popularMovie.setPosterPath("http://image.tmdb.org/t/p/w500//w9kR8qbmQ01HwnvK4alvnQ2ca0L.jpg");
            popularMovie.setBackdropPath("http://image.tmdb.org/t/p/w500//w9kR8qbmQ01HwnvK4alvnQ2ca0L.jpg");
            popularMovie.setOverview("Overview");
            popularMovie.setReleaseDate("");
            popularMovie.setType(0);
            dao.insert(popularMovie);
            topMovie.setTmdbId(301528);
            topMovie.setVoteAverage("");
            topMovie.setTitle("Top Rated");
            topMovie.setPosterPath("http://image.tmdb.org/t/p/w500//w9kR8qbmQ01HwnvK4alvnQ2ca0L.jpg");
            topMovie.setBackdropPath("http://image.tmdb.org/t/p/w500//w9kR8qbmQ01HwnvK4alvnQ2ca0L.jpg");
            topMovie.setOverview("Overview");
            topMovie.setReleaseDate("");
            topMovie.setType(1);
            dao.insert(topMovie);
            upcomingMovie.setTmdbId(301529);
            upcomingMovie.setVoteAverage("");
            upcomingMovie.setTitle("Upcoming");
            upcomingMovie.setPosterPath("http://image.tmdb.org/t/p/w500//w9kR8qbmQ01HwnvK4alvnQ2ca0L.jpg");
            upcomingMovie.setBackdropPath("http://image.tmdb.org/t/p/w500//w9kR8qbmQ01HwnvK4alvnQ2ca0L.jpg");
            upcomingMovie.setOverview("Overview");
            upcomingMovie.setReleaseDate("");
            upcomingMovie.setType(2);
            dao.insert(upcomingMovie);
            theatreMovie.setTmdbId(301526);
            theatreMovie.setVoteAverage("");
            theatreMovie.setTitle("Theater");
            theatreMovie.setPosterPath("http://image.tmdb.org/t/p/w500//w9kR8qbmQ01HwnvK4alvnQ2ca0L.jpg");
            theatreMovie.setBackdropPath("http://image.tmdb.org/t/p/w500//w9kR8qbmQ01HwnvK4alvnQ2ca0L.jpg");
            theatreMovie.setOverview("Overview");
            theatreMovie.setReleaseDate("");
            theatreMovie.setType(3);
            dao.insert(theatreMovie);
            popularSeries.setTitle("Popular");
            popularSeries.setReleaseDate("");
            popularSeries.setPosterPath("http://image.tmdb.org/t/p/w500//fki3kBlwJzFp8QohL43g9ReV455.jpg");
            popularSeries.setBackdropPath("http://image.tmdb.org/t/p/w500//fki3kBlwJzFp8QohL43g9ReV455.jpg");
            popularSeries.setTmdbId(60735);
            popularSeries.setVoteAverage("7.2");
            popularSeries.setOverview("When a young boy vanishes, a small town uncovers a mystery involving secret experiments, terrifying supernatural forces, and one strange little girl.");
            popularSeries.setType(0);
            topSeries.setTitle("Popular");
            topSeries.setReleaseDate("");
            topSeries.setPosterPath("http://image.tmdb.org/t/p/w500//x2LSRK2Cm7MZhjluni1msVJ3wDF.jpg");
            topSeries.setBackdropPath("http://image.tmdb.org/t/p/w500//x2LSRK2Cm7MZhjluni1msVJ3wDF.jpg");
            topSeries.setTmdbId(66732);
            topSeries.setVoteAverage("10.0");
            topSeries.setOverview("When a young boy vanishes, a small town uncovers a mystery involving secret experiments, terrifying supernatural forces, and one strange little girl.");
            topSeries.setType(1);
            seriesDao.insert(popularSeries);
            seriesDao.insert(topSeries);
            return null;
        }
    }

    public abstract MoviesDao moviesDao();

    public abstract SeriesDao seriesDao();

    public abstract FavouritesDao favouritesDao();

    public abstract VideosDao videosDao();

    public abstract ReviewsDao reviewsDao();

    public abstract CreditsDao creditsDao();

    public abstract PersonDao personDao();

    public abstract SeasonsDao seasonsDao();
}
