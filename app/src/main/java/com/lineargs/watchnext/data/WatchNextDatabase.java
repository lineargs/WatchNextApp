package com.lineargs.watchnext.data;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;
import com.lineargs.watchnext.data.entity.*;
import com.lineargs.watchnext.data.dao.*;

@Database(entities = {
        PopularMovie.class,
        TopRatedMovie.class,
        UpcomingMovie.class,
        TheaterMovie.class,
        PopularSerie.class,
        TopRatedSerie.class,
        OnTheAirSerie.class,
        Favorites.class,
        Credits.class,
        Episodes.class,
        Seasons.class,
        Person.class,
        Review.class,
        Videos.class,
        Search.class,
        SearchTv.class,
        UpcomingEpisodes.class,
        AiringTodaySerie.class
}, version = 51, exportSchema = false)
public abstract class WatchNextDatabase extends RoomDatabase {

    private static final String DATABASE_NAME = "watchnext.db";
    private static volatile WatchNextDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    public static final java.util.concurrent.ExecutorService databaseWriteExecutor =
            java.util.concurrent.Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    static final Migration MIGRATION_48_49 = new Migration(48, 49) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE favorites ADD COLUMN notify INTEGER NOT NULL DEFAULT 0");
        }
    };

    static final Migration MIGRATION_49_50 = new Migration(49, 50) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL("CREATE TABLE IF NOT EXISTS `upcoming_episodes` (" +
                    "`series_id` INTEGER NOT NULL, " +
                    "`episode_id` INTEGER NOT NULL, " +
                    "`series_title` TEXT, " +
                    "`episode_name` TEXT, " +
                    "`air_date` TEXT, " +
                    "`season_number` INTEGER NOT NULL, " +
                    "`episode_number` INTEGER NOT NULL, " +
                    "`poster_path` TEXT, " +
                    "`season_id` INTEGER NOT NULL, " +
                    "`episode_count` INTEGER NOT NULL, " +
                    "PRIMARY KEY(`series_id`))");
        }
    };

    public abstract MoviesDao moviesDao();
    public abstract SeriesDao seriesDao();
    public abstract FavoritesDao favoritesDao();
    public abstract CreditsDao creditsDao();
    public abstract SearchDao searchDao();
    public abstract DetailsDao detailsDao();
    public abstract UpcomingEpisodesDao upcomingEpisodesDao();

    public static WatchNextDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (WatchNextDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    WatchNextDatabase.class, DATABASE_NAME)
                            .addMigrations(MIGRATION_48_49, MIGRATION_49_50)
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
