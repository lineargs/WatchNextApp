package com.lineargs.watchnext.data;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
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
        SearchTv.class
}, version = 45, exportSchema = false)
public abstract class WatchNextDatabase extends RoomDatabase {

    private static final String DATABASE_NAME = "watchnext.db";
    private static volatile WatchNextDatabase INSTANCE;

    public abstract MoviesDao moviesDao();
    public abstract SeriesDao seriesDao();
    public abstract FavoritesDao favoritesDao();
    public abstract CreditsDao creditsDao();
    public abstract SearchDao searchDao();
    public abstract DetailsDao detailsDao();

    public static WatchNextDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (WatchNextDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    WatchNextDatabase.class, DATABASE_NAME)
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
