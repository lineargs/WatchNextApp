package com.lineargs.watchnext.data;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.migration.Migration;
import android.content.Context;
import android.support.annotation.NonNull;

@Database(entities = {Credits.class, Episodes.class, Favourites.class,
        Movies.class, Person.class, Reviews.class,
        Seasons.class, Series.class, Videos.class}, version = WatchNextDatabase.DB_VERSION)
public abstract class WatchNextDatabase extends RoomDatabase {

    private static volatile WatchNextDatabase INSTANCE;

    private static final int VERSION_41 = 41;

    private static final int VERSION_42_ROOM = 42;

    static final int DB_VERSION = VERSION_42_ROOM;

    static WatchNextDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (WatchNextDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            WatchNextDatabase.class, "watchnext.db")
                            .addMigrations(MIGRATION_41_42)
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    static final Migration MIGRATION_41_42 = new Migration(VERSION_41, VERSION_42_ROOM) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            /* Successfully migrated Credits table however by loosing data
             * If we do not want to lose data we need to create a new table for
             * example credits_new -> copy all the data from credits
             * Drop credits and then ALTER credits_new -> credits
             * Well done so far
             */
            //TODO Create migration without  loosing the data
            database.execSQL("DROP TABLE credits");
            database.execSQL("CREATE TABLE credits ('tmdb_id' INTEGER NOT NULL, 'character_name' TEXT, 'name' TEXT, 'profile_path' TEXT, 'id' INTEGER NOT NULL, 'job' TEXT, 'type' INTEGER NOT NULL, 'person_id' INTEGER NOT NULL, PRIMARY KEY ('id'))");
        }
    };

    public abstract MoviesDao moviesDao();

    public abstract SeriesDao seriesDao();
}
