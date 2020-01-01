package com.lineargs.watchnext.data;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.lineargs.watchnext.data.credits.Credits;
import com.lineargs.watchnext.data.credits.CreditsDao;
import com.lineargs.watchnext.data.episodes.Episodes;
import com.lineargs.watchnext.data.episodes.EpisodesDao;
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

@Database(entities = {Credits.class, Episodes.class, Favourites.class,
        Movies.class, Person.class, Reviews.class, Search.class,
        Seasons.class, Series.class, Videos.class}, version = WatchNextDatabase.DB_VERSION, exportSchema = false)
public abstract class WatchNextDatabase extends RoomDatabase {

    private static WatchNextDatabase INSTANCE;

    private static final int VERSION_41 = 41;

    private static final int VERSION_42_ROOM = 42;

    static final int DB_VERSION = VERSION_42_ROOM;

    public static WatchNextDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (WatchNextDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            WatchNextDatabase.class, "watchnext.db")
                            .addMigrations(
                                    MIGRATION_41_42
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

    public abstract MoviesDao moviesDao();

    public abstract SeriesDao seriesDao();

    public abstract FavouritesDao favouritesDao();

    public abstract VideosDao videosDao();

    public abstract ReviewsDao reviewsDao();

    public abstract CreditsDao creditsDao();

    public abstract PersonDao personDao();

    public abstract SeasonsDao seasonsDao();

    public abstract SearchDao searchDao();

    public abstract EpisodesDao episodesDao();
}
