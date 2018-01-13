package com.lineargs.watchnext.data;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by goranminov on 15/10/2017.
 * <p>
 * Handling the creation and upgrading our database
 */

public class DataDbHelper extends SQLiteOpenHelper {

    /*
     * Name of our database. Database names should be descriptive and end with the
     * .db extension.
     */
    private static final String DATABASE_NAME = "watchnext.db";


    private static final int DB_VER_37 = 37;

    private static final int DB_VER_38 = 38;
    /*
     * If we change the database schema, we must increment the database version or the onUpgrade
     * method will not be called.
     */
    public static final int DATABASE_VERSION = DB_VER_38;
    /*
     * Contains a simple SQL statement that will create a table that will
     * cache our popular movies data.
     */
    private static final String SQL_CREATE_POPULAR_MOVIE_TABLE =
            "CREATE TABLE " + DataContract.PopularMovieEntry.TABLE_NAME + " ( " +
                        /*
                         * PopularMovieEntry did not explicitly declare a column called "_ID". However,
                         * PopularMovieEntry implements the interface "BaseColumns", which does have a field
                         * named "_ID". We use that here to designate our table's primary key.
                         */
                    DataContract.PopularMovieEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    DataContract.PopularMovieEntry.COLUMN_MOVIE_ID + " INTEGER NOT NULL, " +
                    DataContract.PopularMovieEntry.COLUMN_TITLE + " TEXT NOT NULL, " +
                    DataContract.PopularMovieEntry.COLUMN_RELEASE_DATE + " TEXT NOT NULL, " +
                    DataContract.PopularMovieEntry.COLUMN_OVERVIEW + " TEXT NOT NULL, " +
                    DataContract.PopularMovieEntry.COLUMN_VOTE_AVERAGE + " TEXT NOT NULL, " +
                    DataContract.PopularMovieEntry.COLUMN_POSTER_PATH + " TEXT NOT NULL, " +
                    DataContract.PopularMovieEntry.COLUMN_BACKDROP_PATH + " TEXT NOT NULL, " +
                    DataContract.PopularMovieEntry.COLUMN_ORIGINAL_LANGUAGE + " TEXT NOT NULL, " +
                    DataContract.PopularMovieEntry.COLUMN_ORIGINAL_TITLE + " TEXT NOT NULL, " +
                    DataContract.PopularMovieEntry.COLUMN_IMDB_ID + " TEXT DEFAULT 0, " +
                    DataContract.PopularMovieEntry.COLUMN_HOMEPAGE + " TEXT DEFAULT 0, " +
                    DataContract.PopularMovieEntry.COLUMN_PRODUCTION_COMPANIES + " TEXT DEFAULT 0, " +
                    DataContract.PopularMovieEntry.COLUMN_PRODUCTION_COUNTRIES + " TEXT DEFAULT 0, " +
                    DataContract.PopularMovieEntry.COLUMN_RUNTIME + " INTEGER, " +
                    DataContract.PopularMovieEntry.COLUMN_STATUS + " TEXT, " +
                    DataContract.PopularMovieEntry.COLUMN_GENRES + " TEXT, " +

                        /*
                         * To ensure this table can only contain one movie entry per id, we declare
                         * the movie_id column to be unique. We also specify "ON CONFLICT REPLACE". This tells
                         * SQLite that if we have a movie entry with a certain movie_id and we attempt to
                         * insert another movie entry with that movie_id, we replace the old movie entry.
                         */
                    "UNIQUE (" + DataContract.PopularMovieEntry.COLUMN_MOVIE_ID + ") ON CONFLICT REPLACE);";
    /*
     * {@link SQL_CREATE_POPULAR_MOVIE_TABLE}
     */
    private static final String SQL_CREATE_TOP_RATED_MOVIE_TABLE =
            "CREATE TABLE " + DataContract.TopRatedMovieEntry.TABLE_NAME + " ( " +
                    DataContract.PopularMovieEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    DataContract.PopularMovieEntry.COLUMN_MOVIE_ID + " INTEGER NOT NULL, " +
                    DataContract.PopularMovieEntry.COLUMN_TITLE + " TEXT NOT NULL, " +
                    DataContract.PopularMovieEntry.COLUMN_RELEASE_DATE + " TEXT NOT NULL, " +
                    DataContract.PopularMovieEntry.COLUMN_OVERVIEW + " TEXT NOT NULL, " +
                    DataContract.PopularMovieEntry.COLUMN_VOTE_AVERAGE + " TEXT NOT NULL, " +
                    DataContract.PopularMovieEntry.COLUMN_POSTER_PATH + " TEXT NOT NULL, " +
                    DataContract.PopularMovieEntry.COLUMN_BACKDROP_PATH + " TEXT NOT NULL, " +
                    DataContract.PopularMovieEntry.COLUMN_ORIGINAL_LANGUAGE + " TEXT NOT NULL, " +
                    DataContract.PopularMovieEntry.COLUMN_ORIGINAL_TITLE + " TEXT NOT NULL, " +
                    DataContract.PopularMovieEntry.COLUMN_IMDB_ID + " TEXT DEFAULT 0, " +
                    DataContract.PopularMovieEntry.COLUMN_HOMEPAGE + " TEXT DEFAULT 0, " +
                    DataContract.PopularMovieEntry.COLUMN_PRODUCTION_COMPANIES + " TEXT DEFAULT 0, " +
                    DataContract.PopularMovieEntry.COLUMN_PRODUCTION_COUNTRIES + " TEXT DEFAULT 0, " +
                    DataContract.PopularMovieEntry.COLUMN_RUNTIME + " INTEGER, " +
                    DataContract.PopularMovieEntry.COLUMN_STATUS + " TEXT, " +
                    DataContract.PopularMovieEntry.COLUMN_GENRES + " TEXT, " +
                    "UNIQUE (" + DataContract.PopularMovieEntry.COLUMN_MOVIE_ID + ") ON CONFLICT REPLACE);";
    /*
     * {@link SQL_CREATE_POPULAR_MOVIE_TABLE}
     */
    private static final String SQL_CREATE_UPCOMING_MOVIE_TABLE =
            "CREATE TABLE " + DataContract.UpcomingMovieEntry.TABLE_NAME + " ( " +
                    DataContract.PopularMovieEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    DataContract.PopularMovieEntry.COLUMN_MOVIE_ID + " INTEGER NOT NULL, " +
                    DataContract.PopularMovieEntry.COLUMN_TITLE + " TEXT NOT NULL, " +
                    DataContract.PopularMovieEntry.COLUMN_RELEASE_DATE + " TEXT NOT NULL, " +
                    DataContract.PopularMovieEntry.COLUMN_OVERVIEW + " TEXT NOT NULL, " +
                    DataContract.PopularMovieEntry.COLUMN_VOTE_AVERAGE + " TEXT NOT NULL, " +
                    DataContract.PopularMovieEntry.COLUMN_POSTER_PATH + " TEXT NOT NULL, " +
                    DataContract.PopularMovieEntry.COLUMN_BACKDROP_PATH + " TEXT NOT NULL, " +
                    DataContract.PopularMovieEntry.COLUMN_ORIGINAL_LANGUAGE + " TEXT NOT NULL, " +
                    DataContract.PopularMovieEntry.COLUMN_ORIGINAL_TITLE + " TEXT NOT NULL, " +
                    DataContract.PopularMovieEntry.COLUMN_IMDB_ID + " TEXT DEFAULT 0, " +
                    DataContract.PopularMovieEntry.COLUMN_HOMEPAGE + " TEXT DEFAULT 0, " +
                    DataContract.PopularMovieEntry.COLUMN_PRODUCTION_COMPANIES + " TEXT DEFAULT 0, " +
                    DataContract.PopularMovieEntry.COLUMN_PRODUCTION_COUNTRIES + " TEXT DEFAULT 0, " +
                    DataContract.PopularMovieEntry.COLUMN_RUNTIME + " INTEGER, " +
                    DataContract.PopularMovieEntry.COLUMN_STATUS + " TEXT, " +
                    DataContract.PopularMovieEntry.COLUMN_GENRES + " TEXT, " +
                    "UNIQUE (" + DataContract.PopularMovieEntry.COLUMN_MOVIE_ID + ") ON CONFLICT REPLACE);";
    /*
     * {@link SQL_CREATE_POPULAR_MOVIE_TABLE}
     */
    private static final String SQL_CREATE_THEATER_MOVIE_TABLE =
            "CREATE TABLE " + DataContract.TheaterMovieEntry.TABLE_NAME + " ( " +
                    DataContract.PopularMovieEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    DataContract.PopularMovieEntry.COLUMN_MOVIE_ID + " INTEGER NOT NULL, " +
                    DataContract.PopularMovieEntry.COLUMN_TITLE + " TEXT NOT NULL, " +
                    DataContract.PopularMovieEntry.COLUMN_RELEASE_DATE + " TEXT NOT NULL, " +
                    DataContract.PopularMovieEntry.COLUMN_OVERVIEW + " TEXT NOT NULL, " +
                    DataContract.PopularMovieEntry.COLUMN_VOTE_AVERAGE + " TEXT NOT NULL, " +
                    DataContract.PopularMovieEntry.COLUMN_POSTER_PATH + " TEXT NOT NULL, " +
                    DataContract.PopularMovieEntry.COLUMN_BACKDROP_PATH + " TEXT NOT NULL, " +
                    DataContract.PopularMovieEntry.COLUMN_ORIGINAL_LANGUAGE + " TEXT NOT NULL, " +
                    DataContract.PopularMovieEntry.COLUMN_ORIGINAL_TITLE + " TEXT NOT NULL, " +
                    DataContract.PopularMovieEntry.COLUMN_IMDB_ID + " TEXT DEFAULT 0, " +
                    DataContract.PopularMovieEntry.COLUMN_HOMEPAGE + " TEXT DEFAULT 0, " +
                    DataContract.PopularMovieEntry.COLUMN_PRODUCTION_COMPANIES + " TEXT DEFAULT 0, " +
                    DataContract.PopularMovieEntry.COLUMN_PRODUCTION_COUNTRIES + " TEXT DEFAULT 0, " +
                    DataContract.PopularMovieEntry.COLUMN_RUNTIME + " INTEGER DEFAULT 0, " +
                    DataContract.PopularMovieEntry.COLUMN_STATUS + " TEXT, " +
                    DataContract.PopularMovieEntry.COLUMN_GENRES + " TEXT, " +
                    "UNIQUE (" + DataContract.PopularMovieEntry.COLUMN_MOVIE_ID + ") ON CONFLICT REPLACE);";
    /*
     * {@link SQL_CREATE_POPULAR_MOVIE_TABLE}
     */
    private static final String SQL_CREATE_MOVIE_CAST_TABLE =
            "CREATE TABLE " + DataContract.CreditCast.TABLE_NAME + " ( " +
                    DataContract.CreditCast._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    DataContract.CreditCast.COLUMN_MOVIE_ID + " INTEGER NOT NULL, " +
                    DataContract.CreditCast.COLUMN_PERSON_ID + " INTEGER NOT NULL, " +
                    DataContract.CreditCast.COLUMN_NAME + " TEXT NOT NULL, " +
                    DataContract.CreditCast.COLUMN_CHARACTER_NAME + " TEXT NOT NULL, " +
                    DataContract.CreditCast.COLUMN_PROFILE_PATH + " TEXT NOT NULL);";
    /*
     * {@link SQL_CREATE_POPULAR_MOVIE_TABLE}
     */
    private static final String SQL_CREATE_MOVIE_CREW_TABLE =
            "CREATE TABLE " + DataContract.CreditCrew.TABLE_NAME + " ( " +
                    DataContract.CreditCrew._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    DataContract.CreditCrew.COLUMN_MOVIE_ID + " INTEGER NOT NULL, " +
                    DataContract.CreditCrew.COLUMN_CREW_ID + " INTEGER NOT NULL, " +
                    DataContract.CreditCrew.COLUMN_NAME + " TEXT NOT NULL, " +
                    DataContract.CreditCrew.COLUMN_CREDIT_ID + " TEXT NOT NULL, " +
                    DataContract.CreditCrew.COLUMN_DEPARTMENT + " TEXT NOT NULL, " +
                    DataContract.CreditCrew.COLUMN_JOB + " TEXT NOT NULL, " +
                    DataContract.CreditCrew.COLUMN_PROFILE_PATH + " TEXT NOT NULL);";
    /*
     * {@link SQL_CREATE_POPULAR_MOVIE_TABLE}
     */
    private static final String SQL_CREATE_POPULAR_SERIE_TABLE =
            "CREATE TABLE " + DataContract.PopularSerieEntry.TABLE_NAME + " ( " +
                    DataContract.PopularSerieEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    DataContract.PopularMovieEntry.COLUMN_MOVIE_ID + " INTEGER NOT NULL, " +
                    DataContract.PopularMovieEntry.COLUMN_TITLE + " TEXT NOT NULL, " +
                    DataContract.PopularMovieEntry.COLUMN_RELEASE_DATE + " TEXT NOT NULL, " +
                    DataContract.PopularMovieEntry.COLUMN_OVERVIEW + " TEXT NOT NULL, " +
                    DataContract.PopularMovieEntry.COLUMN_VOTE_AVERAGE + " TEXT NOT NULL, " +
                    DataContract.PopularMovieEntry.COLUMN_POSTER_PATH + " TEXT NOT NULL, " +
                    DataContract.PopularMovieEntry.COLUMN_BACKDROP_PATH + " TEXT NOT NULL, " +
                    DataContract.PopularMovieEntry.COLUMN_ORIGINAL_LANGUAGE + " TEXT NOT NULL, " +
                    DataContract.PopularMovieEntry.COLUMN_ORIGINAL_TITLE + " TEXT NOT NULL, " +
                    DataContract.PopularMovieEntry.COLUMN_IMDB_ID + " TEXT DEFAULT 0, " +
                    DataContract.PopularMovieEntry.COLUMN_HOMEPAGE + " TEXT DEFAULT 0, " +
                    DataContract.PopularMovieEntry.COLUMN_PRODUCTION_COMPANIES + " TEXT DEFAULT 0, " +
                    DataContract.PopularMovieEntry.COLUMN_PRODUCTION_COUNTRIES + " TEXT DEFAULT 0, " +
                    DataContract.PopularMovieEntry.COLUMN_RUNTIME + " INTEGER, " +
                    DataContract.PopularMovieEntry.COLUMN_STATUS + " TEXT, " +
                    DataContract.PopularMovieEntry.COLUMN_GENRES + " TEXT, " +
                    "UNIQUE (" + DataContract.PopularMovieEntry.COLUMN_MOVIE_ID + ") ON CONFLICT REPLACE);";
    /*
     * {@link SQL_CREATE_POPULAR_MOVIE_TABLE}
     */
    private static final String SQL_CREATE_TOP_SERIE_TABLE =
            "CREATE TABLE " + DataContract.TopRatedSerieEntry.TABLE_NAME + " ( " +
                    DataContract.TopRatedSerieEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    DataContract.PopularMovieEntry.COLUMN_MOVIE_ID + " INTEGER NOT NULL, " +
                    DataContract.PopularMovieEntry.COLUMN_TITLE + " TEXT NOT NULL, " +
                    DataContract.PopularMovieEntry.COLUMN_RELEASE_DATE + " TEXT NOT NULL, " +
                    DataContract.PopularMovieEntry.COLUMN_OVERVIEW + " TEXT NOT NULL, " +
                    DataContract.PopularMovieEntry.COLUMN_VOTE_AVERAGE + " TEXT NOT NULL, " +
                    DataContract.PopularMovieEntry.COLUMN_POSTER_PATH + " TEXT NOT NULL, " +
                    DataContract.PopularMovieEntry.COLUMN_BACKDROP_PATH + " TEXT NOT NULL, " +
                    DataContract.PopularMovieEntry.COLUMN_ORIGINAL_LANGUAGE + " TEXT NOT NULL, " +
                    DataContract.PopularMovieEntry.COLUMN_ORIGINAL_TITLE + " TEXT NOT NULL, " +
                    DataContract.PopularMovieEntry.COLUMN_IMDB_ID + " TEXT DEFAULT 0, " +
                    DataContract.PopularMovieEntry.COLUMN_HOMEPAGE + " TEXT DEFAULT 0, " +
                    DataContract.PopularMovieEntry.COLUMN_PRODUCTION_COMPANIES + " TEXT DEFAULT 0, " +
                    DataContract.PopularMovieEntry.COLUMN_PRODUCTION_COUNTRIES + " TEXT DEFAULT 0, " +
                    DataContract.PopularMovieEntry.COLUMN_RUNTIME + " INTEGER, " +
                    DataContract.PopularMovieEntry.COLUMN_STATUS + " TEXT, " +
                    DataContract.PopularMovieEntry.COLUMN_GENRES + " TEXT, " +
                    "UNIQUE (" + DataContract.PopularMovieEntry.COLUMN_MOVIE_ID + ") ON CONFLICT REPLACE);";
    /*
     * {@link SQL_CREATE_POPULAR_MOVIE_TABLE}
     */
    private static final String SQL_CREATE_ON_THE_AIR_SERIE_TABLE =
            "CREATE TABLE " + DataContract.OnTheAirSerieEntry.TABLE_NAME + " ( " +
                    DataContract.OnTheAirSerieEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    DataContract.PopularMovieEntry.COLUMN_MOVIE_ID + " INTEGER NOT NULL, " +
                    DataContract.PopularMovieEntry.COLUMN_TITLE + " TEXT NOT NULL, " +
                    DataContract.PopularMovieEntry.COLUMN_RELEASE_DATE + " TEXT NOT NULL, " +
                    DataContract.PopularMovieEntry.COLUMN_OVERVIEW + " TEXT NOT NULL, " +
                    DataContract.PopularMovieEntry.COLUMN_VOTE_AVERAGE + " TEXT NOT NULL, " +
                    DataContract.PopularMovieEntry.COLUMN_POSTER_PATH + " TEXT NOT NULL, " +
                    DataContract.PopularMovieEntry.COLUMN_BACKDROP_PATH + " TEXT NOT NULL, " +
                    DataContract.PopularMovieEntry.COLUMN_ORIGINAL_LANGUAGE + " TEXT NOT NULL, " +
                    DataContract.PopularMovieEntry.COLUMN_ORIGINAL_TITLE + " TEXT NOT NULL, " +
                    DataContract.PopularMovieEntry.COLUMN_IMDB_ID + " TEXT DEFAULT 0, " +
                    DataContract.PopularMovieEntry.COLUMN_HOMEPAGE + " TEXT DEFAULT 0, " +
                    DataContract.PopularMovieEntry.COLUMN_PRODUCTION_COMPANIES + " TEXT DEFAULT 0, " +
                    DataContract.PopularMovieEntry.COLUMN_PRODUCTION_COUNTRIES + " TEXT DEFAULT 0, " +
                    DataContract.PopularMovieEntry.COLUMN_RUNTIME + " INTEGER, " +
                    DataContract.PopularMovieEntry.COLUMN_STATUS + " TEXT, " +
                    DataContract.PopularMovieEntry.COLUMN_GENRES + " TEXT, " +
                    "UNIQUE (" + DataContract.PopularMovieEntry.COLUMN_MOVIE_ID + ") ON CONFLICT REPLACE);";
    /*
     * {@link SQL_CREATE_POPULAR_MOVIE_TABLE}
     */
    private static final String SQL_CREATE_FAVORITES_TABLE =
            "CREATE TABLE " + DataContract.Favorites.TABLE_NAME + " ( " +
                    DataContract.Favorites._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    DataContract.PopularMovieEntry.COLUMN_MOVIE_ID + " INTEGER NOT NULL, " +
                    DataContract.PopularMovieEntry.COLUMN_TITLE + " TEXT NOT NULL, " +
                    DataContract.PopularMovieEntry.COLUMN_RELEASE_DATE + " TEXT NOT NULL, " +
                    DataContract.PopularMovieEntry.COLUMN_OVERVIEW + " TEXT NOT NULL, " +
                    DataContract.Favorites.COLUMN_TYPE + " INTEGER DEFAULT 0, " +
                    DataContract.PopularMovieEntry.COLUMN_VOTE_AVERAGE + " TEXT NOT NULL, " +
                    DataContract.PopularMovieEntry.COLUMN_POSTER_PATH + " TEXT NOT NULL, " +
                    DataContract.PopularMovieEntry.COLUMN_BACKDROP_PATH + " TEXT NOT NULL, " +
                    DataContract.PopularMovieEntry.COLUMN_ORIGINAL_LANGUAGE + " TEXT NOT NULL, " +
                    DataContract.PopularMovieEntry.COLUMN_IMDB_ID + " TEXT DEFAULT 0, " +
                    DataContract.PopularMovieEntry.COLUMN_HOMEPAGE + " TEXT DEFAULT 0, " +
                    DataContract.PopularMovieEntry.COLUMN_PRODUCTION_COMPANIES + " TEXT DEFAULT 0, " +
                    DataContract.PopularMovieEntry.COLUMN_PRODUCTION_COUNTRIES + " TEXT DEFAULT 0, " +
                    DataContract.PopularMovieEntry.COLUMN_RUNTIME + " INTEGER, " +
                    DataContract.PopularMovieEntry.COLUMN_STATUS + " TEXT, " +
                    DataContract.PopularMovieEntry.COLUMN_GENRES + " TEXT, " +
                    DataContract.PopularMovieEntry.COLUMN_ORIGINAL_TITLE + " TEXT NOT NULL);";
    /*
     * {@link SQL_CREATE_POPULAR_MOVIE_TABLE}
     */
    private static final String SQL_CREATE_SEARCH_TABLE =
            "CREATE TABLE " + DataContract.Search.TABLE_NAME + " ( " +
                    DataContract.Search._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    DataContract.Search.COLUMN_MOVIE_ID + " INTEGER NOT NULL, " +
                    DataContract.Search.COLUMN_TITLE + " TEXT NOT NULL, " +
                    DataContract.Search.COLUMN_POSTER_PATH + " TEXT NOT NULL);";
    /*
     * {@link SQL_CREATE_POPULAR_MOVIE_TABLE}
     */
    private static final String SQL_CREATE_SEARCH_TV_TABLE =
            "CREATE TABLE " + DataContract.SearchTv.TABLE_NAME + " ( " +
                    DataContract.SearchTv._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    DataContract.Search.COLUMN_MOVIE_ID + " INTEGER NOT NULL, " +
                    DataContract.Search.COLUMN_TITLE + " TEXT NOT NULL, " +
                    DataContract.Search.COLUMN_POSTER_PATH + " TEXT NOT NULL);";
    /*
     * {@link SQL_CREATE_POPULAR_MOVIE_TABLE}
     */
    private static final String SQL_CREATE_REVIEW_TABLE =
            "CREATE TABLE " + DataContract.Review.TABLE_NAME + " ( " +
                    DataContract.Review._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    DataContract.Review.COLUMN_MOVIE_ID + " INTEGER NOT NULL, " +
                    DataContract.Review.COLUMN_REVIEW_ID + " TEXT NOT NULL, " +
                    DataContract.Review.COLUMN_AUTHOR + " TEXT NOT NULL, " +
                    DataContract.Review.COLUMN_CONTENT + " TEXT NOT NULL, " +
                    DataContract.Review.COLUMN_URL + " TEXT NOT NULL, " +
                    "UNIQUE (" + DataContract.Review.COLUMN_REVIEW_ID + ") ON CONFLICT REPLACE);";
    /*
     * {@link SQL_CREATE_POPULAR_MOVIE_TABLE}
     */
    private static final String SQL_CREATE_VIDEOS_TABLE =
            "CREATE TABLE " + DataContract.Videos.TABLE_NAME + " ( " +
                    DataContract.Videos._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    DataContract.Videos.COLUMN_MOVIE_ID + " INTEGER NOT NULL, " +
                    DataContract.Videos.COLUMN_VIDEO_ID + " TEXT NOT NULL, " +
                    DataContract.Videos.COLUMN_KEY + " TEXT NOT NULL, " +
                    DataContract.Videos.COLUMN_NAME + " TEXT NOT NULL, " +
                    DataContract.Videos.COLUMN_TYPE + " TEXT NOT NULL, " +
                    DataContract.Videos.COLUMN_IMG + " TEXT NOT NULL, " +
                    DataContract.Videos.COLUMN_SITE + " TEXT NOT NULL, " +
                    "UNIQUE (" + DataContract.Videos.COLUMN_VIDEO_ID + ") ON CONFLICT REPLACE);";
    /*
     * {@link SQL_CREATE_POPULAR_MOVIE_TABLE}
     */
    private static final String SQL_CREATE_SEASONS_TABLE =
            "CREATE TABLE " + DataContract.Seasons.TABLE_NAME + " ( " +
                    DataContract.Seasons._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    DataContract.Seasons.COLUMN_SERIE_ID + " INTEGER NOT NULL, " +
                    DataContract.Seasons.COLUMN_SEASON_ID + " INTEGER NOT NULL, " +
                    DataContract.Seasons.COLUMN_EPISODE_COUNT + " INTEGER NOT NULL, " +
                    DataContract.Seasons.COLUMN_RELEASE_DATE + " TEXT, " +
                    DataContract.Seasons.COLUMN_POSTER_PATH + " TEXT NOT NULL, " +
                    DataContract.Seasons.COLUMN_SEASON_NUMBER + " INTEGER NOT NULL, " +
                    DataContract.Seasons.COLUMN_SHOW_NAME + " TEXT NOT NULL, " +
                    "UNIQUE (" + DataContract.Seasons.COLUMN_SEASON_ID + ") ON CONFLICT REPLACE);";
    /*
     * {@link SQL_CREATE_POPULAR_MOVIE_TABLE}
     */
    private static final String SQL_CREATE_EPISODES_TABLE =
            "CREATE TABLE " + DataContract.Episodes.TABLE_NAME + " ( " +
                    DataContract.Episodes._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    DataContract.Episodes.COLUMN_EPISODE_ID + " INTEGER NOT NULL, " +
                    DataContract.Episodes.COLUMN_SEASON_ID + " INTEGER NOT NULL, " +
                    DataContract.Episodes.COLUMN_EPISODE_NUMBER + " INTEGER NOT NULL, " +
                    DataContract.Episodes.COLUMN_RELEASE_DATE + " TEXT, " +
                    DataContract.Episodes.COLUMN_NAME + " TEXT NOT NULL, " +
                    DataContract.Episodes.COLUMN_OVERVIEW + " TEXT NOT NULL, " +
                    DataContract.Episodes.COLUMN_SEASON_NUMBER + " INTEGER NOT NULL, " +
                    DataContract.Episodes.COLUMN_STILL_PATH + " TEXT NOT NULL, " +
                    DataContract.Episodes.COLUMN_VOTE_AVERAGE + " TEXT NOT NULL, " +
                    DataContract.Episodes.COLUMN_VOTE_COUNT + " INTEGER DEFAULT 0, " +
                    "UNIQUE (" + DataContract.Episodes.COLUMN_EPISODE_ID + ") ON CONFLICT REPLACE);";
    /*
     * {@link SQL_CREATE_POPULAR_MOVIE_TABLE}
     */
    private static final String SQL_CREATE_PERSON_TABLE =
            "CREATE TABLE " + DataContract.Person.TABLE_NAME + " ( " +
                    DataContract.Person._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    DataContract.Person.COLUMN_PERSON_ID + " INTEGER NOT NULL, " +
                    DataContract.Person.COLUMN_BIRTHDAY + " TEXT, " +
                    DataContract.Person.COLUMN_NAME + " TEXT NOT NULL, " +
                    DataContract.Person.COLUMN_BIOGRAPHY + " TEXT, " +
                    DataContract.Person.COLUMN_PLACE_OF_BIRTH + " TEXT, " +
                    DataContract.Person.COLUMN_PROFILE_PATH + " TEXT NOT NULL, " +
                    DataContract.Person.COLUMN_HOMEPAGE + " TEXT, " +
                    "UNIQUE (" + DataContract.Person.COLUMN_PERSON_ID + ") ON CONFLICT REPLACE);";

    /**
     * Create a helper object to create, open, and/or manage a database.
     *
     * @param context To use to open or create the database
     */
    DataDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * Called when the database is created for the first time. This is where the creation of
     * tables and the initial population of the tables should happen.
     *
     * @param db The database.
     */
    @Override
    public void onCreate(SQLiteDatabase db) {

        /*
         * After we've spelled out our SQLite table creation statements above, we actually execute
         * that SQLs with the execSQL method of our SQLite database object.
         */
        db.execSQL(SQL_CREATE_POPULAR_MOVIE_TABLE);
        db.execSQL(SQL_CREATE_TOP_RATED_MOVIE_TABLE);
        db.execSQL(SQL_CREATE_UPCOMING_MOVIE_TABLE);
        db.execSQL(SQL_CREATE_THEATER_MOVIE_TABLE);
        db.execSQL(SQL_CREATE_MOVIE_CAST_TABLE);
        db.execSQL(SQL_CREATE_MOVIE_CREW_TABLE);
        db.execSQL(SQL_CREATE_POPULAR_SERIE_TABLE);
        db.execSQL(SQL_CREATE_TOP_SERIE_TABLE);
        db.execSQL(SQL_CREATE_ON_THE_AIR_SERIE_TABLE);
        db.execSQL(SQL_CREATE_FAVORITES_TABLE);
        db.execSQL(SQL_CREATE_SEARCH_TABLE);
        db.execSQL(SQL_CREATE_REVIEW_TABLE);
        db.execSQL(SQL_CREATE_VIDEOS_TABLE);
        db.execSQL(SQL_CREATE_SEASONS_TABLE);
        db.execSQL(SQL_CREATE_EPISODES_TABLE);
        db.execSQL(SQL_CREATE_PERSON_TABLE);
        db.execSQL(SQL_CREATE_SEARCH_TV_TABLE);
    }

    /**
     * All the tables except favorites one are only a cache for online data, so its upgrade policy is simply to discard
     * the data and call through to onCreate to recreate the table. Note that this only fires if
     * we change the version number for our database (in our case, DATABASE_VERSION). It does NOT
     * depend on the version number for our application found in our app/build.gradle file.
     *
     * @param db         Database that is being upgraded
     * @param oldVersion The old database version
     * @param newVersion The new database version
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        Log.d("DataDbHelper", "Upgrading from " + oldVersion + " to " + newVersion);

        int dbVersion = oldVersion;
        switch (dbVersion) {
            case DB_VER_37:
                upgradeToThirtyEight(db);
                dbVersion = DB_VER_38;
        }

        if (dbVersion != DATABASE_VERSION) {
            recreateDatabase(db);
        }
    }

    /**
     * NOTE: We do not support downgrading the database version, so its downgrade policy is simply to discard
     * the data and call through to onCreate to recreate the table.
     * @param db Database that is being downgraded
     * @param oldVersion The old database version
     * @param newVersion The new database version
     */
    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        recreateDatabase(db);
    }

    private void recreateDatabase(SQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS " + DataContract.PopularMovieEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + DataContract.TopRatedMovieEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + DataContract.UpcomingMovieEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + DataContract.TheaterMovieEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + DataContract.CreditCast.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + DataContract.CreditCrew.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + DataContract.PopularSerieEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + DataContract.TopRatedSerieEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + DataContract.OnTheAirSerieEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + DataContract.Favorites.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + DataContract.Search.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + DataContract.SearchTv.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + DataContract.Review.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + DataContract.Videos.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + DataContract.Seasons.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + DataContract.Episodes.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + DataContract.Person.TABLE_NAME);

        onCreate(db);
    }

    private static boolean isColumnMissing(SQLiteDatabase db, String table, String column) {
        Cursor cursor = db.query(table, null, null, null, null, null, null, "1");
        if (cursor == null) {
            return true;
        }
        boolean columnExists = cursor.getColumnIndex(column) != -1;
        cursor.close();
        return !columnExists;
    }

    private static boolean isTableMissing(SQLiteDatabase db, String table) {
        Cursor cursor = db.query("sqlite_master", new String[]{"name"}, "type='table' AND name=?",
                new String[]{table}, null, null, null, "1");
        if (cursor == null) {
            return true;
        }
        boolean tableExists = cursor.getCount() > 0;
        cursor.close();
        return !tableExists;
    }

    private static void upgradeToThirtyEight(SQLiteDatabase db) {

        /* Create the Crew table*/
        if (isTableMissing(db, DataContract.CreditCrew.TABLE_NAME)) {
            db.execSQL(SQL_CREATE_MOVIE_CREW_TABLE);
        }

        /* Check if the columns are missing then alter the  Popular Movie table*/
        if (isColumnMissing(db, DataContract.PopularMovieEntry.TABLE_NAME, DataContract.PopularMovieEntry.COLUMN_IMDB_ID)) {
            db.execSQL("ALTER TABLE " + DataContract.PopularMovieEntry.TABLE_NAME +
                    " ADD COLUMN " + DataContract.PopularMovieEntry.COLUMN_IMDB_ID +
                    " TEXT DEFAULT 0;");
        }
        if (isColumnMissing(db, DataContract.PopularMovieEntry.TABLE_NAME, DataContract.PopularMovieEntry.COLUMN_HOMEPAGE)) {
            db.execSQL("ALTER TABLE " + DataContract.PopularMovieEntry.TABLE_NAME +
                    " ADD COLUMN " + DataContract.PopularMovieEntry.COLUMN_HOMEPAGE +
                    " TEXT DEFAULT 0;");
        }
        if (isColumnMissing(db, DataContract.PopularMovieEntry.TABLE_NAME, DataContract.PopularMovieEntry.COLUMN_PRODUCTION_COMPANIES)) {
            db.execSQL("ALTER TABLE " + DataContract.PopularMovieEntry.TABLE_NAME +
                    " ADD COLUMN " + DataContract.PopularMovieEntry.COLUMN_PRODUCTION_COMPANIES +
                    " TEXT DEFAULT 0;");
        }
        if (isColumnMissing(db, DataContract.PopularMovieEntry.TABLE_NAME, DataContract.PopularMovieEntry.COLUMN_PRODUCTION_COUNTRIES)) {
            db.execSQL("ALTER TABLE " + DataContract.PopularMovieEntry.TABLE_NAME +
                    " ADD COLUMN " + DataContract.PopularMovieEntry.COLUMN_PRODUCTION_COUNTRIES +
                    " TEXT DEFAULT 0;");
        }
        if (isColumnMissing(db, DataContract.PopularMovieEntry.TABLE_NAME, DataContract.PopularMovieEntry.COLUMN_RUNTIME)) {
            db.execSQL("ALTER TABLE " + DataContract.PopularMovieEntry.TABLE_NAME +
                    " ADD COLUMN " + DataContract.PopularMovieEntry.COLUMN_RUNTIME +
                    " INTEGER DEFAULT 0;");
        }
        if (isColumnMissing(db, DataContract.PopularMovieEntry.TABLE_NAME, DataContract.PopularMovieEntry.COLUMN_STATUS)) {
            db.execSQL("ALTER TABLE " + DataContract.PopularMovieEntry.TABLE_NAME +
                    " ADD COLUMN " + DataContract.PopularMovieEntry.COLUMN_STATUS +
                    " TEXT;");
        }
        if (isColumnMissing(db, DataContract.PopularMovieEntry.TABLE_NAME, DataContract.PopularMovieEntry.COLUMN_GENRES)) {
            db.execSQL("ALTER TABLE " + DataContract.PopularMovieEntry.TABLE_NAME +
                    " ADD COLUMN " + DataContract.PopularMovieEntry.COLUMN_GENRES +
                    " TEXT;");
        }

        /* Check if the columns are missing then alter the  Top Rated Movie table*/
        if (isColumnMissing(db, DataContract.TopRatedMovieEntry.TABLE_NAME, DataContract.PopularMovieEntry.COLUMN_IMDB_ID)) {
            db.execSQL("ALTER TABLE " + DataContract.TopRatedMovieEntry.TABLE_NAME +
                    " ADD COLUMN " + DataContract.PopularMovieEntry.COLUMN_IMDB_ID +
                    " TEXT DEFAULT 0;");
        }
        if (isColumnMissing(db, DataContract.TopRatedMovieEntry.TABLE_NAME, DataContract.PopularMovieEntry.COLUMN_HOMEPAGE)) {
            db.execSQL("ALTER TABLE " + DataContract.TopRatedMovieEntry.TABLE_NAME +
                    " ADD COLUMN " + DataContract.PopularMovieEntry.COLUMN_HOMEPAGE +
                    " TEXT DEFAULT 0;");
        }
        if (isColumnMissing(db, DataContract.TopRatedMovieEntry.TABLE_NAME, DataContract.PopularMovieEntry.COLUMN_PRODUCTION_COMPANIES)) {
            db.execSQL("ALTER TABLE " + DataContract.TopRatedMovieEntry.TABLE_NAME +
                    " ADD COLUMN " + DataContract.PopularMovieEntry.COLUMN_PRODUCTION_COMPANIES +
                    " TEXT DEFAULT 0;");
        }
        if (isColumnMissing(db, DataContract.TopRatedMovieEntry.TABLE_NAME, DataContract.PopularMovieEntry.COLUMN_PRODUCTION_COUNTRIES)) {
            db.execSQL("ALTER TABLE " + DataContract.TopRatedMovieEntry.TABLE_NAME +
                    " ADD COLUMN " + DataContract.PopularMovieEntry.COLUMN_PRODUCTION_COUNTRIES +
                    " TEXT DEFAULT 0;");
        }
        if (isColumnMissing(db, DataContract.TopRatedMovieEntry.TABLE_NAME, DataContract.PopularMovieEntry.COLUMN_RUNTIME)) {
            db.execSQL("ALTER TABLE " + DataContract.TopRatedMovieEntry.TABLE_NAME +
                    " ADD COLUMN " + DataContract.PopularMovieEntry.COLUMN_RUNTIME +
                    " INTEGER DEFAULT 0;");
        }
        if (isColumnMissing(db, DataContract.TopRatedMovieEntry.TABLE_NAME, DataContract.PopularMovieEntry.COLUMN_STATUS)) {
            db.execSQL("ALTER TABLE " + DataContract.TopRatedMovieEntry.TABLE_NAME +
                    " ADD COLUMN " + DataContract.PopularMovieEntry.COLUMN_STATUS +
                    " TEXT;");
        }
        if (isColumnMissing(db, DataContract.TopRatedMovieEntry.TABLE_NAME, DataContract.PopularMovieEntry.COLUMN_GENRES)) {
            db.execSQL("ALTER TABLE " + DataContract.TopRatedMovieEntry.TABLE_NAME +
                    " ADD COLUMN " + DataContract.PopularMovieEntry.COLUMN_GENRES +
                    " TEXT;");
        }

        /* Check if the columns are missing then alter the  Upcoming table*/
        if (isColumnMissing(db, DataContract.UpcomingMovieEntry.TABLE_NAME, DataContract.PopularMovieEntry.COLUMN_IMDB_ID)) {
            db.execSQL("ALTER TABLE " + DataContract.UpcomingMovieEntry.TABLE_NAME +
                    " ADD COLUMN " + DataContract.PopularMovieEntry.COLUMN_IMDB_ID +
                    " TEXT DEFAULT 0;");
        }
        if (isColumnMissing(db, DataContract.UpcomingMovieEntry.TABLE_NAME, DataContract.PopularMovieEntry.COLUMN_HOMEPAGE)) {
            db.execSQL("ALTER TABLE " + DataContract.UpcomingMovieEntry.TABLE_NAME +
                    " ADD COLUMN " + DataContract.PopularMovieEntry.COLUMN_HOMEPAGE +
                    " TEXT DEFAULT 0;");
        }
        if (isColumnMissing(db, DataContract.UpcomingMovieEntry.TABLE_NAME, DataContract.PopularMovieEntry.COLUMN_PRODUCTION_COMPANIES)) {
            db.execSQL("ALTER TABLE " + DataContract.UpcomingMovieEntry.TABLE_NAME +
                    " ADD COLUMN " + DataContract.PopularMovieEntry.COLUMN_PRODUCTION_COMPANIES +
                    " TEXT DEFAULT 0;");
        }
        if (isColumnMissing(db, DataContract.UpcomingMovieEntry.TABLE_NAME, DataContract.PopularMovieEntry.COLUMN_PRODUCTION_COUNTRIES)) {
            db.execSQL("ALTER TABLE " + DataContract.UpcomingMovieEntry.TABLE_NAME +
                    " ADD COLUMN " + DataContract.PopularMovieEntry.COLUMN_PRODUCTION_COUNTRIES +
                    " TEXT DEFAULT 0;");
        }
        if (isColumnMissing(db, DataContract.UpcomingMovieEntry.TABLE_NAME, DataContract.PopularMovieEntry.COLUMN_RUNTIME)) {
            db.execSQL("ALTER TABLE " + DataContract.UpcomingMovieEntry.TABLE_NAME +
                    " ADD COLUMN " + DataContract.PopularMovieEntry.COLUMN_RUNTIME +
                    " INTEGER DEFAULT 0;");
        }
        if (isColumnMissing(db, DataContract.UpcomingMovieEntry.TABLE_NAME, DataContract.PopularMovieEntry.COLUMN_STATUS)) {
            db.execSQL("ALTER TABLE " + DataContract.UpcomingMovieEntry.TABLE_NAME +
                    " ADD COLUMN " + DataContract.PopularMovieEntry.COLUMN_STATUS +
                    " TEXT;");
        }
        if (isColumnMissing(db, DataContract.UpcomingMovieEntry.TABLE_NAME, DataContract.PopularMovieEntry.COLUMN_GENRES)) {
            db.execSQL("ALTER TABLE " + DataContract.UpcomingMovieEntry.TABLE_NAME +
                    " ADD COLUMN " + DataContract.PopularMovieEntry.COLUMN_GENRES +
                    " TEXT;");
        }

        /* Check if the columns are missing then alter the  Theater Movie table*/
        if (isColumnMissing(db, DataContract.TheaterMovieEntry.TABLE_NAME, DataContract.PopularMovieEntry.COLUMN_IMDB_ID)) {
            db.execSQL("ALTER TABLE " + DataContract.TheaterMovieEntry.TABLE_NAME +
                    " ADD COLUMN " + DataContract.PopularMovieEntry.COLUMN_IMDB_ID +
                    " TEXT DEFAULT 0;");
        }
        if (isColumnMissing(db, DataContract.TheaterMovieEntry.TABLE_NAME, DataContract.PopularMovieEntry.COLUMN_HOMEPAGE)) {
            db.execSQL("ALTER TABLE " + DataContract.TheaterMovieEntry.TABLE_NAME +
                    " ADD COLUMN " + DataContract.PopularMovieEntry.COLUMN_HOMEPAGE +
                    " TEXT DEFAULT 0;");
        }
        if (isColumnMissing(db, DataContract.TheaterMovieEntry.TABLE_NAME, DataContract.PopularMovieEntry.COLUMN_PRODUCTION_COMPANIES)) {
            db.execSQL("ALTER TABLE " + DataContract.TheaterMovieEntry.TABLE_NAME +
                    " ADD COLUMN " + DataContract.PopularMovieEntry.COLUMN_PRODUCTION_COMPANIES +
                    " TEXT DEFAULT 0;");
        }
        if (isColumnMissing(db, DataContract.TheaterMovieEntry.TABLE_NAME, DataContract.PopularMovieEntry.COLUMN_PRODUCTION_COUNTRIES)) {
            db.execSQL("ALTER TABLE " + DataContract.TheaterMovieEntry.TABLE_NAME +
                    " ADD COLUMN " + DataContract.PopularMovieEntry.COLUMN_PRODUCTION_COUNTRIES +
                    " TEXT DEFAULT 0;");
        }
        if (isColumnMissing(db, DataContract.TheaterMovieEntry.TABLE_NAME, DataContract.PopularMovieEntry.COLUMN_RUNTIME)) {
            db.execSQL("ALTER TABLE " + DataContract.TheaterMovieEntry.TABLE_NAME +
                    " ADD COLUMN " + DataContract.PopularMovieEntry.COLUMN_RUNTIME +
                    " INTEGER DEFAULT 0;");
        }
        if (isColumnMissing(db, DataContract.TheaterMovieEntry.TABLE_NAME, DataContract.PopularMovieEntry.COLUMN_STATUS)) {
            db.execSQL("ALTER TABLE " + DataContract.TheaterMovieEntry.TABLE_NAME +
                    " ADD COLUMN " + DataContract.PopularMovieEntry.COLUMN_STATUS +
                    " TEXT;");
        }
        if (isColumnMissing(db, DataContract.TheaterMovieEntry.TABLE_NAME, DataContract.PopularMovieEntry.COLUMN_GENRES)) {
            db.execSQL("ALTER TABLE " + DataContract.TheaterMovieEntry.TABLE_NAME +
                    " ADD COLUMN " + DataContract.PopularMovieEntry.COLUMN_GENRES +
                    " TEXT;");
        }

        /* Check if the columns are missing then alter the  Favorites Movie table*/
        if (isColumnMissing(db, DataContract.Favorites.TABLE_NAME, DataContract.PopularMovieEntry.COLUMN_IMDB_ID)) {
            db.execSQL("ALTER TABLE " + DataContract.Favorites.TABLE_NAME +
                    " ADD COLUMN " + DataContract.PopularMovieEntry.COLUMN_IMDB_ID +
                    " TEXT DEFAULT 0;");
        }
        if (isColumnMissing(db, DataContract.Favorites.TABLE_NAME, DataContract.PopularMovieEntry.COLUMN_HOMEPAGE)) {
            db.execSQL("ALTER TABLE " + DataContract.Favorites.TABLE_NAME +
                    " ADD COLUMN " + DataContract.PopularMovieEntry.COLUMN_HOMEPAGE +
                    " TEXT DEFAULT 0;");
        }
        if (isColumnMissing(db, DataContract.Favorites.TABLE_NAME, DataContract.PopularMovieEntry.COLUMN_PRODUCTION_COMPANIES)) {
            db.execSQL("ALTER TABLE " + DataContract.Favorites.TABLE_NAME +
                    " ADD COLUMN " + DataContract.PopularMovieEntry.COLUMN_PRODUCTION_COMPANIES +
                    " TEXT DEFAULT 0;");
        }
        if (isColumnMissing(db, DataContract.Favorites.TABLE_NAME, DataContract.PopularMovieEntry.COLUMN_PRODUCTION_COUNTRIES)) {
            db.execSQL("ALTER TABLE " + DataContract.Favorites.TABLE_NAME +
                    " ADD COLUMN " + DataContract.PopularMovieEntry.COLUMN_PRODUCTION_COUNTRIES +
                    " TEXT DEFAULT 0;");
        }
        if (isColumnMissing(db, DataContract.Favorites.TABLE_NAME, DataContract.PopularMovieEntry.COLUMN_RUNTIME)) {
            db.execSQL("ALTER TABLE " + DataContract.Favorites.TABLE_NAME +
                    " ADD COLUMN " + DataContract.PopularMovieEntry.COLUMN_RUNTIME +
                    " INTEGER DEFAULT 0;");
        }
        if (isColumnMissing(db, DataContract.Favorites.TABLE_NAME, DataContract.PopularMovieEntry.COLUMN_STATUS)) {
            db.execSQL("ALTER TABLE " + DataContract.Favorites.TABLE_NAME +
                    " ADD COLUMN " + DataContract.PopularMovieEntry.COLUMN_STATUS +
                    " TEXT;");
        }
        if (isColumnMissing(db, DataContract.Favorites.TABLE_NAME, DataContract.PopularMovieEntry.COLUMN_GENRES)) {
            db.execSQL("ALTER TABLE " + DataContract.Favorites.TABLE_NAME +
                    " ADD COLUMN " + DataContract.PopularMovieEntry.COLUMN_GENRES +
                    " TEXT;");
        }

        /* Check if the columns are missing then alter the  Favorites Series table*/
        if (isColumnMissing(db, DataContract.PopularSerieEntry.TABLE_NAME, DataContract.PopularMovieEntry.COLUMN_IMDB_ID)) {
            db.execSQL("ALTER TABLE " + DataContract.PopularSerieEntry.TABLE_NAME +
                    " ADD COLUMN " + DataContract.PopularMovieEntry.COLUMN_IMDB_ID +
                    " TEXT DEFAULT 0;");
        }
        if (isColumnMissing(db, DataContract.PopularSerieEntry.TABLE_NAME, DataContract.PopularMovieEntry.COLUMN_HOMEPAGE)) {
            db.execSQL("ALTER TABLE " + DataContract.PopularSerieEntry.TABLE_NAME +
                    " ADD COLUMN " + DataContract.PopularMovieEntry.COLUMN_HOMEPAGE +
                    " TEXT DEFAULT 0;");
        }
        if (isColumnMissing(db, DataContract.PopularSerieEntry.TABLE_NAME, DataContract.PopularMovieEntry.COLUMN_PRODUCTION_COMPANIES)) {
            db.execSQL("ALTER TABLE " + DataContract.PopularSerieEntry.TABLE_NAME +
                    " ADD COLUMN " + DataContract.PopularMovieEntry.COLUMN_PRODUCTION_COMPANIES +
                    " TEXT DEFAULT 0;");
        }
        if (isColumnMissing(db, DataContract.PopularSerieEntry.TABLE_NAME, DataContract.PopularMovieEntry.COLUMN_PRODUCTION_COUNTRIES)) {
            db.execSQL("ALTER TABLE " + DataContract.PopularSerieEntry.TABLE_NAME +
                    " ADD COLUMN " + DataContract.PopularMovieEntry.COLUMN_PRODUCTION_COUNTRIES +
                    " TEXT DEFAULT 0;");
        }
        if (isColumnMissing(db, DataContract.PopularSerieEntry.TABLE_NAME, DataContract.PopularMovieEntry.COLUMN_RUNTIME)) {
            db.execSQL("ALTER TABLE " + DataContract.PopularSerieEntry.TABLE_NAME +
                    " ADD COLUMN " + DataContract.PopularMovieEntry.COLUMN_RUNTIME +
                    " INTEGER DEFAULT 0;");
        }
        if (isColumnMissing(db, DataContract.PopularSerieEntry.TABLE_NAME, DataContract.PopularMovieEntry.COLUMN_STATUS)) {
            db.execSQL("ALTER TABLE " + DataContract.PopularSerieEntry.TABLE_NAME +
                    " ADD COLUMN " + DataContract.PopularMovieEntry.COLUMN_STATUS +
                    " TEXT;");
        }
        if (isColumnMissing(db, DataContract.PopularSerieEntry.TABLE_NAME, DataContract.PopularMovieEntry.COLUMN_GENRES)) {
            db.execSQL("ALTER TABLE " + DataContract.PopularSerieEntry.TABLE_NAME +
                    " ADD COLUMN " + DataContract.PopularMovieEntry.COLUMN_GENRES +
                    " TEXT;");
        }

        /* Check if the columns are missing then alter the  TopRated Series table*/
        if (isColumnMissing(db, DataContract.TopRatedSerieEntry.TABLE_NAME, DataContract.PopularMovieEntry.COLUMN_IMDB_ID)) {
            db.execSQL("ALTER TABLE " + DataContract.TopRatedSerieEntry.TABLE_NAME +
                    " ADD COLUMN " + DataContract.PopularMovieEntry.COLUMN_IMDB_ID +
                    " TEXT DEFAULT 0;");
        }
        if (isColumnMissing(db, DataContract.TopRatedSerieEntry.TABLE_NAME, DataContract.PopularMovieEntry.COLUMN_HOMEPAGE)) {
            db.execSQL("ALTER TABLE " + DataContract.TopRatedSerieEntry.TABLE_NAME +
                    " ADD COLUMN " + DataContract.PopularMovieEntry.COLUMN_HOMEPAGE +
                    " TEXT DEFAULT 0;");
        }
        if (isColumnMissing(db, DataContract.TopRatedSerieEntry.TABLE_NAME, DataContract.PopularMovieEntry.COLUMN_PRODUCTION_COMPANIES)) {
            db.execSQL("ALTER TABLE " + DataContract.TopRatedSerieEntry.TABLE_NAME +
                    " ADD COLUMN " + DataContract.PopularMovieEntry.COLUMN_PRODUCTION_COMPANIES +
                    " TEXT DEFAULT 0;");
        }
        if (isColumnMissing(db, DataContract.TopRatedSerieEntry.TABLE_NAME, DataContract.PopularMovieEntry.COLUMN_PRODUCTION_COUNTRIES)) {
            db.execSQL("ALTER TABLE " + DataContract.TopRatedSerieEntry.TABLE_NAME +
                    " ADD COLUMN " + DataContract.PopularMovieEntry.COLUMN_PRODUCTION_COUNTRIES +
                    " TEXT DEFAULT 0;");
        }
        if (isColumnMissing(db, DataContract.TopRatedSerieEntry.TABLE_NAME, DataContract.PopularMovieEntry.COLUMN_RUNTIME)) {
            db.execSQL("ALTER TABLE " + DataContract.TopRatedSerieEntry.TABLE_NAME +
                    " ADD COLUMN " + DataContract.PopularMovieEntry.COLUMN_RUNTIME +
                    " INTEGER DEFAULT 0;");
        }
        if (isColumnMissing(db, DataContract.TopRatedSerieEntry.TABLE_NAME, DataContract.PopularMovieEntry.COLUMN_STATUS)) {
            db.execSQL("ALTER TABLE " + DataContract.TopRatedSerieEntry.TABLE_NAME +
                    " ADD COLUMN " + DataContract.PopularMovieEntry.COLUMN_STATUS +
                    " TEXT;");
        }
        if (isColumnMissing(db, DataContract.TopRatedSerieEntry.TABLE_NAME, DataContract.PopularMovieEntry.COLUMN_GENRES)) {
            db.execSQL("ALTER TABLE " + DataContract.TopRatedSerieEntry.TABLE_NAME +
                    " ADD COLUMN " + DataContract.PopularMovieEntry.COLUMN_GENRES +
                    " TEXT;");
        }

        /* Check if the columns are missing then alter the  OnTheAir Series table*/
        if (isColumnMissing(db, DataContract.OnTheAirSerieEntry.TABLE_NAME, DataContract.PopularMovieEntry.COLUMN_IMDB_ID)) {
            db.execSQL("ALTER TABLE " + DataContract.OnTheAirSerieEntry.TABLE_NAME +
                    " ADD COLUMN " + DataContract.PopularMovieEntry.COLUMN_IMDB_ID +
                    " TEXT DEFAULT 0;");
        }
        if (isColumnMissing(db, DataContract.OnTheAirSerieEntry.TABLE_NAME, DataContract.PopularMovieEntry.COLUMN_HOMEPAGE)) {
            db.execSQL("ALTER TABLE " + DataContract.OnTheAirSerieEntry.TABLE_NAME +
                    " ADD COLUMN " + DataContract.PopularMovieEntry.COLUMN_HOMEPAGE +
                    " TEXT DEFAULT 0;");
        }
        if (isColumnMissing(db, DataContract.OnTheAirSerieEntry.TABLE_NAME, DataContract.PopularMovieEntry.COLUMN_PRODUCTION_COMPANIES)) {
            db.execSQL("ALTER TABLE " + DataContract.OnTheAirSerieEntry.TABLE_NAME +
                    " ADD COLUMN " + DataContract.PopularMovieEntry.COLUMN_PRODUCTION_COMPANIES +
                    " TEXT DEFAULT 0;");
        }
        if (isColumnMissing(db, DataContract.OnTheAirSerieEntry.TABLE_NAME, DataContract.PopularMovieEntry.COLUMN_PRODUCTION_COUNTRIES)) {
            db.execSQL("ALTER TABLE " + DataContract.OnTheAirSerieEntry.TABLE_NAME +
                    " ADD COLUMN " + DataContract.PopularMovieEntry.COLUMN_PRODUCTION_COUNTRIES +
                    " TEXT DEFAULT 0;");
        }
        if (isColumnMissing(db, DataContract.OnTheAirSerieEntry.TABLE_NAME, DataContract.PopularMovieEntry.COLUMN_RUNTIME)) {
            db.execSQL("ALTER TABLE " + DataContract.OnTheAirSerieEntry.TABLE_NAME +
                    " ADD COLUMN " + DataContract.PopularMovieEntry.COLUMN_RUNTIME +
                    " INTEGER DEFAULT 0;");
        }
        if (isColumnMissing(db, DataContract.OnTheAirSerieEntry.TABLE_NAME, DataContract.PopularMovieEntry.COLUMN_STATUS)) {
            db.execSQL("ALTER TABLE " + DataContract.OnTheAirSerieEntry.TABLE_NAME +
                    " ADD COLUMN " + DataContract.PopularMovieEntry.COLUMN_STATUS +
                    " TEXT;");
        }
        if (isColumnMissing(db, DataContract.OnTheAirSerieEntry.TABLE_NAME, DataContract.PopularMovieEntry.COLUMN_GENRES)) {
            db.execSQL("ALTER TABLE " + DataContract.OnTheAirSerieEntry.TABLE_NAME +
                    " ADD COLUMN " + DataContract.PopularMovieEntry.COLUMN_GENRES +
                    " TEXT;");
        }
    }
}
