package com.lineargs.watchnext.data;

import android.content.Context;
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

    /*
     * If we change the database schema, we must increment the database version or the onUpgrade
     * method will not be called.
     */
    private static final int DATABASE_VERSION = 37;
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
    @SuppressWarnings("unused")
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
                    DataContract.PopularSerieEntry.COLUMN_SERIE_ID + " INTEGER NOT NULL, " +
                    DataContract.PopularSerieEntry.COLUMN_NAME + " TEXT NOT NULL, " +
                    DataContract.PopularSerieEntry.COLUMN_RELEASE_DATE + " TEXT NOT NULL, " +
                    DataContract.PopularSerieEntry.COLUMN_OVERVIEW + " TEXT NOT NULL, " +
                    DataContract.PopularSerieEntry.COLUMN_VOTE_AVERAGE + " TEXT NOT NULL, " +
                    DataContract.PopularSerieEntry.COLUMN_POSTER_PATH + " TEXT NOT NULL, " +
                    DataContract.PopularSerieEntry.COLUMN_BACKDROP_PATH + " TEXT NOT NULL, " +
                    DataContract.PopularSerieEntry.COLUMN_ORIGINAL_LANGUAGE + " TEXT NOT NULL, " +
                    DataContract.PopularSerieEntry.COLUMN_ORIGINAL_NAME + " TEXT NOT NULL, " +
                    "UNIQUE (" + DataContract.PopularSerieEntry.COLUMN_SERIE_ID + ") ON CONFLICT REPLACE);";
    /*
     * {@link SQL_CREATE_POPULAR_MOVIE_TABLE}
     */
    private static final String SQL_CREATE_TOP_SERIE_TABLE =
            "CREATE TABLE " + DataContract.TopRatedSerieEntry.TABLE_NAME + " ( " +
                    DataContract.TopRatedSerieEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    DataContract.TopRatedSerieEntry.COLUMN_SERIE_ID + " INTEGER NOT NULL, " +
                    DataContract.TopRatedSerieEntry.COLUMN_NAME + " TEXT NOT NULL, " +
                    DataContract.TopRatedSerieEntry.COLUMN_RELEASE_DATE + " TEXT NOT NULL, " +
                    DataContract.TopRatedSerieEntry.COLUMN_OVERVIEW + " TEXT NOT NULL, " +
                    DataContract.TopRatedSerieEntry.COLUMN_VOTE_AVERAGE + " TEXT NOT NULL, " +
                    DataContract.TopRatedSerieEntry.COLUMN_POSTER_PATH + " TEXT NOT NULL, " +
                    DataContract.TopRatedSerieEntry.COLUMN_BACKDROP_PATH + " TEXT NOT NULL, " +
                    DataContract.TopRatedSerieEntry.COLUMN_ORIGINAL_LANGUAGE + " TEXT NOT NULL, " +
                    DataContract.TopRatedSerieEntry.COLUMN_ORIGINAL_NAME + " TEXT NOT NULL, " +
                    "UNIQUE (" + DataContract.TopRatedSerieEntry.COLUMN_SERIE_ID + ") ON CONFLICT REPLACE);";
    /*
     * {@link SQL_CREATE_POPULAR_MOVIE_TABLE}
     */
    private static final String SQL_CREATE_ON_THE_AIR_SERIE_TABLE =
            "CREATE TABLE " + DataContract.OnTheAirSerieEntry.TABLE_NAME + " ( " +
                    DataContract.OnTheAirSerieEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    DataContract.OnTheAirSerieEntry.COLUMN_SERIE_ID + " INTEGER NOT NULL, " +
                    DataContract.OnTheAirSerieEntry.COLUMN_NAME + " TEXT NOT NULL, " +
                    DataContract.OnTheAirSerieEntry.COLUMN_RELEASE_DATE + " TEXT NOT NULL, " +
                    DataContract.OnTheAirSerieEntry.COLUMN_OVERVIEW + " TEXT NOT NULL, " +
                    DataContract.OnTheAirSerieEntry.COLUMN_VOTE_AVERAGE + " TEXT NOT NULL, " +
                    DataContract.OnTheAirSerieEntry.COLUMN_POSTER_PATH + " TEXT NOT NULL, " +
                    DataContract.OnTheAirSerieEntry.COLUMN_BACKDROP_PATH + " TEXT NOT NULL, " +
                    DataContract.OnTheAirSerieEntry.COLUMN_ORIGINAL_LANGUAGE + " TEXT NOT NULL, " +
                    DataContract.OnTheAirSerieEntry.COLUMN_ORIGINAL_NAME + " TEXT NOT NULL, " +
                    "UNIQUE (" + DataContract.OnTheAirSerieEntry.COLUMN_SERIE_ID + ") ON CONFLICT REPLACE);";
    /*
     * {@link SQL_CREATE_POPULAR_MOVIE_TABLE}
     */
    private static final String SQL_CREATE_FAVORITES_TABLE =
            "CREATE TABLE " + DataContract.Favorites.TABLE_NAME + " ( " +
                    DataContract.Favorites._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    DataContract.Favorites.COLUMN_FAV_ID + " INTEGER NOT NULL, " +
                    DataContract.Favorites.COLUMN_TITLE + " TEXT NOT NULL, " +
                    DataContract.Favorites.COLUMN_RELEASE_DATE + " TEXT NOT NULL, " +
                    DataContract.Favorites.COLUMN_OVERVIEW + " TEXT NOT NULL, " +
                    DataContract.Favorites.COLUMN_TYPE + " INTEGER DEFAULT 0, " +
                    DataContract.Favorites.COLUMN_VOTE_AVERAGE + " TEXT NOT NULL, " +
                    DataContract.Favorites.COLUMN_POSTER_PATH + " TEXT NOT NULL, " +
                    DataContract.Favorites.COLUMN_BACKDROP_PATH + " TEXT NOT NULL, " +
                    DataContract.Favorites.COLUMN_ORIGINAL_LANGUAGE + " TEXT NOT NULL, " +
                    DataContract.Favorites.COLUMN_ORIGINAL_TITLE + " TEXT NOT NULL);";
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
                    DataContract.SearchTv.COLUMN_MOVIE_ID + " INTEGER NOT NULL, " +
                    DataContract.SearchTv.COLUMN_TITLE + " TEXT NOT NULL, " +
                    DataContract.SearchTv.COLUMN_POSTER_PATH + " TEXT NOT NULL);";
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

    private static void upgradeToTwentyThree(SQLiteDatabase db) {
        db.execSQL("ALTER TABLE " + DataContract.Favorites.TABLE_NAME +
                " ADD COLUMN " + DataContract.Favorites.COLUMN_TYPE +
                " INTEGER DEFAULT 0;");
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
     * depend on the version number for our application found in our app/build.gradle file. If
     * we want to update the schema without wiping data, commenting out the current body of this
     * method should be our top priority before modifying this method.
     *
     * @param db         Database that is being upgraded
     * @param oldVersion The old database version
     * @param newVersion The new database version
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        Log.d("DataDbHelper", "Upgrading from " + oldVersion + " to " + newVersion);
    }
}
