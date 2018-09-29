package com.lineargs.watchnext.data;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

@Database(entities = {Credits.class, Episodes.class, Favourites.class,
        Movies.class, Person.class, Reviews.class,
        Seasons.class, Series.class, Videos.class}, version = 1)
public abstract class WatchNextDatabase extends RoomDatabase {

    private static volatile WatchNextDatabase INSTANCE;

    static WatchNextDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (WatchNextDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            WatchNextDatabase.class, "watchnext.db")
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    public abstract MoviesDao moviesDao();

    public abstract SeriesDao seriesDao();
}
