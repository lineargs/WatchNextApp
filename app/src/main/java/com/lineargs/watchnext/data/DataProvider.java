package com.lineargs.watchnext.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * Created by goranminov on 15/10/2017.
 * <p>
 * This class serves as the ContentProvider for all of WatchNext's data. This class allows us to
 * bulkInsert, insert, query, update and delete data.
 * <p>
 */

public class DataProvider extends ContentProvider {

    /*
     * These constants will be used to match URIs with the data they are looking for. We will take
     * advantage of the UriMatcher class to make that matching MUCH easier than doing something
     * ourselves, such as using regular expressions.
     */
    public static final int CODE_POPULAR_MOVIES = 100;
    public static final int CODE_POPULAR_MOVIES_WITH_ID = 101;
    public static final int CODE_POPULAR_MOVIES_WITH_TITLE = 102;
    public static final int CODE_TOP_MOVIES = 200;
    public static final int CODE_TOP_MOVIES_WITH_ID = 201;
    public static final int CODE_UPCOMING_MOVIES = 300;
    public static final int CODE_UPCOMING_MOVIES_WITH_ID = 301;
    public static final int CODE_THEATER_MOVIES = 400;
    public static final int CODE_THEATER_MOVIES_WITH_ID = 401;
    public static final int CODE_CAST = 500;
    public static final int CODE_CAST_WITH_ID = 501;
    public static final int CODE_CREW = 600;
    public static final int CODE_CREW_WITH_ID = 601;
    public static final int CODE_POPULAR_SERIES = 700;
    public static final int CODE_POPULAR_SERIES_WITH_ID = 701;
    public static final int CODE_TOP_SERIES = 800;
    public static final int CODE_TOP_SERIES_WITH_ID = 801;
    public static final int CODE_ON_THE_AIR_SERIES = 900;
    public static final int CODE_ON_THE_AIR_SERIES_WITH_ID = 901;
    public static final int CODE_FAVORITES = 1000;
    public static final int CODE_FAVORITES_WITH_ID = 1001;
    public static final int CODE_SEARCH = 2000;
    public static final int CODE_SEARCH_WITH_ID = 2001;
    public static final int CODE_REVIEW = 3000;
    public static final int CODE_REVIEW_WITH_ID = 3001;
    public static final int CODE_VIDEOS = 4000;
    public static final int CODE_VIDEOS_WITH_ID = 4001;
    public static final int CODE_SEASONS = 5000;
    public static final int CODE_SEASONS_WITH_ID = 5001;
    public static final int CODE_EPISODES = 6000;
    public static final int CODE_EPISODES_WITH_ID = 6001;
    public static final int CODE_PERSON = 7000;
    public static final int CODE_PERSON_WITH_ID = 7001;
    public static final int CODE_SEARCH_TV = 8000;
    public static final int CODE_SEARCH_TV_WITH_ID = 8001;

    /*
     * The URI Matcher used by this content provider. The leading "s" in this variable name
     * signifies that this UriMatcher is a static member variable of DataProvider.
     */
    private static final UriMatcher uriMatcher = buildUriMatcher();
    private WatchNextDatabase database;

    /**
     * Creates the UriMatcher that will match each URI to the constants defined above.
     * UriMatcher does all the hard work for us. We just have to tell it which code to match
     * with which URI, and it does the rest automatically.
     *
     * @return A UriMatcher that correctly matches the constants above
     */
    public static UriMatcher buildUriMatcher() {

        /*
         * All paths added to the UriMatcher have a corresponding code to return when a match is
         * found. The code passed into the constructor of UriMatcher here represents the code to
         * return for the root URI. It's common to use NO_MATCH as the code for this case.
         */
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = DataContract.CONTENT_AUTHORITY;

        /*
         * For each type of URI we want to add, create a corresponding code. Preferably, these are
         * constant fields in our class so that we can use them throughout the class and we know
         * they aren't going to change.
         * This URI is content://com.lineargs.watchnext/popularmovie/
         */
        matcher.addURI(authority, DataContract.PATH_POPULAR_MOVIE, CODE_POPULAR_MOVIES);
        /*
         * This URI would look something like content://com.lineargs.watchnext/popularmovie/123567
         * The "/#" signifies to the UriMatcher that if PATH_POPULAR_MOVIE is followed by ANY number,
         * that it should return the CODE_POPULAR_MOVIES_WITH_ID code
         */
        matcher.addURI(authority, DataContract.PATH_POPULAR_MOVIE + "/#", CODE_POPULAR_MOVIES_WITH_ID);
        /*
         * This URI would look something like content://com.lineargs.watchnext/popularmovie/thor
         * The "/*" signifies to the UriMatcher that if PATH_POPULAR_MOVIE is followed by ANY letter,
         * that it should return the CODE_POPULAR_MOVIES_WITH_TITLE code
         */
        matcher.addURI(authority, DataContract.PATH_POPULAR_MOVIE + "/*", CODE_POPULAR_MOVIES_WITH_TITLE);
        matcher.addURI(authority, DataContract.PATH_TOP_RATED_MOVIE, CODE_TOP_MOVIES);
        matcher.addURI(authority, DataContract.PATH_TOP_RATED_MOVIE + "/#", CODE_TOP_MOVIES_WITH_ID);
        matcher.addURI(authority, DataContract.PATH_UPCOMING_MOVIE, CODE_UPCOMING_MOVIES);
        matcher.addURI(authority, DataContract.PATH_UPCOMING_MOVIE + "/#", CODE_UPCOMING_MOVIES_WITH_ID);
        matcher.addURI(authority, DataContract.PATH_THEATER_MOVIE, CODE_THEATER_MOVIES);
        matcher.addURI(authority, DataContract.PATH_THEATER_MOVIE + "/#", CODE_THEATER_MOVIES_WITH_ID);
        matcher.addURI(authority, DataContract.PATH_CREDIT_CAST, CODE_CAST);
        matcher.addURI(authority, DataContract.PATH_CREDIT_CAST + "/#", CODE_CAST_WITH_ID);
        matcher.addURI(authority, DataContract.PATH_CREDIT_CREW, CODE_CREW);
        matcher.addURI(authority, DataContract.PATH_CREDIT_CREW + "/#", CODE_CREW_WITH_ID);
        matcher.addURI(authority, DataContract.PATH_POPULAR_SERIE, CODE_POPULAR_SERIES);
        matcher.addURI(authority, DataContract.PATH_POPULAR_SERIE + "/#", CODE_POPULAR_SERIES_WITH_ID);
        matcher.addURI(authority, DataContract.PATH_TOP_SERIE, CODE_TOP_SERIES);
        matcher.addURI(authority, DataContract.PATH_TOP_SERIE + "/#", CODE_TOP_SERIES_WITH_ID);
        matcher.addURI(authority, DataContract.PATH_ON_THE_AIR_SERIE, CODE_ON_THE_AIR_SERIES);
        matcher.addURI(authority, DataContract.PATH_ON_THE_AIR_SERIE + "/#", CODE_ON_THE_AIR_SERIES_WITH_ID);
        matcher.addURI(authority, DataContract.PATH_FAVORITES, CODE_FAVORITES);
        matcher.addURI(authority, DataContract.PATH_FAVORITES + "/#", CODE_FAVORITES_WITH_ID);
        matcher.addURI(authority, DataContract.PATH_SEARCH, CODE_SEARCH);
        matcher.addURI(authority, DataContract.PATH_SEARCH + "/#", CODE_SEARCH_WITH_ID);
        matcher.addURI(authority, DataContract.PATH_REVIEW, CODE_REVIEW);
        matcher.addURI(authority, DataContract.PATH_REVIEW + "/#", CODE_REVIEW_WITH_ID);
        matcher.addURI(authority, DataContract.PATH_VIDEOS, CODE_VIDEOS);
        matcher.addURI(authority, DataContract.PATH_VIDEOS + "/#", CODE_VIDEOS_WITH_ID);
        matcher.addURI(authority, DataContract.PATH_SEASONS, CODE_SEASONS);
        matcher.addURI(authority, DataContract.PATH_SEASONS + "/#", CODE_SEASONS_WITH_ID);
        matcher.addURI(authority, DataContract.PATH_EPISODES, CODE_EPISODES);
        matcher.addURI(authority, DataContract.PATH_EPISODES + "/#", CODE_EPISODES_WITH_ID);
        matcher.addURI(authority, DataContract.PATH_PERSON, CODE_PERSON);
        matcher.addURI(authority, DataContract.PATH_PERSON + "/#", CODE_PERSON_WITH_ID);
        matcher.addURI(authority, DataContract.PATH_SEARCH_TV, CODE_SEARCH_TV);
        matcher.addURI(authority, DataContract.PATH_SEARCH_TV + "/#", CODE_SEARCH_TV_WITH_ID);

        return matcher;
    }

    /**
     * In onCreate, we initialize our content provider on startup. This method is called for all
     * registered content providers on the application main thread at application launch time.
     * It must not perform lengthy operations, or application startup will be delayed.
     * <p>
     * Nontrivial initialization (such as opening, upgrading, and scanning
     * databases) should be deferred until the content provider is used (via {@link #query},
     * {@link #bulkInsert(Uri, ContentValues[])}, etc).
     * <p>
     * Deferred initialization keeps application startup fast, avoids unnecessary work if the
     * provider turns out not to be needed, and stops database errors (such as a full disk) from
     * halting application launch.
     *
     * @return true if the provider was successfully loaded, false otherwise
     */
    @Override
    public boolean onCreate() {

        /*
         * As noted in the comment above, onCreate is run on the main thread, so performing any
         * lengthy operations will cause lag in your app. Since WatchNextDatabase.getDatabase is
         * very lightweight, we are safe to perform that initialization here.
         */
        database = WatchNextDatabase.getDatabase(getContext());
        return true;
    }

    /**
     * Handles query requests from clients. We will use this method to query for all
     * of our data as well as to query for a particular ID.
     *
     * @param uri           The URI to query
     * @param projection    The list of columns to put into the cursor. If null, all columns are
     *                      included.
     * @param selection     A selection criteria to apply when filtering rows. If null, then all
     *                      rows are included.
     * @param selectionArgs You may include ?s in selection, which will be replaced by
     *                      the values from selectionArgs, in order that they appear in the
     *                      selection.
     * @param sortOrder     How the rows in the cursor should be sorted.
     * @return A Cursor containing the results of the query.
     */
    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        final androidx.sqlite.db.SupportSQLiteDatabase db = database.getOpenHelper().getReadableDatabase();
        Cursor cursor;
        androidx.sqlite.db.SupportSQLiteQueryBuilder builder;

        switch (uriMatcher.match(uri)) {
            case CODE_POPULAR_MOVIES:
                builder = androidx.sqlite.db.SupportSQLiteQueryBuilder.builder(DataContract.PopularMovieEntry.TABLE_NAME)
                        .columns(projection)
                        .selection(selection, selectionArgs)
                        .orderBy(sortOrder);
                cursor = db.query(builder.create());
                break;
            case CODE_POPULAR_MOVIES_WITH_ID:
                builder = androidx.sqlite.db.SupportSQLiteQueryBuilder.builder(DataContract.PopularMovieEntry.TABLE_NAME)
                        .columns(projection)
                        .selection(DataContract.PopularMovieEntry.COLUMN_MOVIE_ID + " = ? ", new String[]{uri.getLastPathSegment()})
                        .orderBy(sortOrder);
                cursor = db.query(builder.create());
                break;
            case CODE_POPULAR_MOVIES_WITH_TITLE:
                builder = androidx.sqlite.db.SupportSQLiteQueryBuilder.builder(DataContract.PopularMovieEntry.TABLE_NAME)
                        .columns(projection)
                        .selection(DataContract.PopularMovieEntry.COLUMN_TITLE + " = ? ", new String[]{uri.getLastPathSegment()})
                        .orderBy(sortOrder);
                cursor = db.query(builder.create());
                break;
            case CODE_TOP_MOVIES:
                builder = androidx.sqlite.db.SupportSQLiteQueryBuilder.builder(DataContract.TopRatedMovieEntry.TABLE_NAME)
                        .columns(projection)
                        .selection(selection, selectionArgs)
                        .orderBy(sortOrder);
                cursor = db.query(builder.create());
                break;
            case CODE_TOP_MOVIES_WITH_ID:
                builder = androidx.sqlite.db.SupportSQLiteQueryBuilder.builder(DataContract.TopRatedMovieEntry.TABLE_NAME)
                        .columns(projection)
                        .selection(DataContract.PopularMovieEntry.COLUMN_MOVIE_ID + " = ? ", new String[]{uri.getLastPathSegment()})
                        .orderBy(sortOrder);
                cursor = db.query(builder.create());
                break;
            case CODE_UPCOMING_MOVIES:
                builder = androidx.sqlite.db.SupportSQLiteQueryBuilder.builder(DataContract.UpcomingMovieEntry.TABLE_NAME)
                        .columns(projection)
                        .selection(selection, selectionArgs)
                        .orderBy(sortOrder);
                cursor = db.query(builder.create());
                break;
            case CODE_UPCOMING_MOVIES_WITH_ID:
                builder = androidx.sqlite.db.SupportSQLiteQueryBuilder.builder(DataContract.UpcomingMovieEntry.TABLE_NAME)
                        .columns(projection)
                        .selection(DataContract.PopularMovieEntry.COLUMN_MOVIE_ID + " = ? ", new String[]{uri.getLastPathSegment()})
                        .orderBy(sortOrder);
                cursor = db.query(builder.create());
                break;
            case CODE_THEATER_MOVIES:
                builder = androidx.sqlite.db.SupportSQLiteQueryBuilder.builder(DataContract.TheaterMovieEntry.TABLE_NAME)
                        .columns(projection)
                        .selection(selection, selectionArgs)
                        .orderBy(sortOrder);
                cursor = db.query(builder.create());
                break;
            case CODE_THEATER_MOVIES_WITH_ID:
                builder = androidx.sqlite.db.SupportSQLiteQueryBuilder.builder(DataContract.TheaterMovieEntry.TABLE_NAME)
                        .columns(projection)
                        .selection(DataContract.PopularMovieEntry.COLUMN_MOVIE_ID + " = ? ", new String[]{uri.getLastPathSegment()})
                        .orderBy(sortOrder);
                cursor = db.query(builder.create());
                break;
            case CODE_CAST:
                builder = androidx.sqlite.db.SupportSQLiteQueryBuilder.builder(DataContract.Credits.TABLE_NAME)
                        .columns(projection)
                        .selection(DataContract.Credits.COLUMN_TYPE + " = ? ", new String[]{"0"})
                        .orderBy(sortOrder);
                cursor = db.query(builder.create());
                break;
            case CODE_CAST_WITH_ID:
                builder = androidx.sqlite.db.SupportSQLiteQueryBuilder.builder(DataContract.Credits.TABLE_NAME)
                        .columns(projection)
                        .selection(DataContract.Credits.COLUMN_MOVIE_ID + " = ? AND "
                                + DataContract.Credits.COLUMN_TYPE + " = ? ", new String[]{uri.getLastPathSegment(), "0"})
                        .orderBy(sortOrder);
                cursor = db.query(builder.create());
                break;
            case CODE_CREW:
                builder = androidx.sqlite.db.SupportSQLiteQueryBuilder.builder(DataContract.Credits.TABLE_NAME)
                        .columns(projection)
                        .selection(DataContract.Credits.COLUMN_TYPE + " = ? ", new String[]{"1"})
                        .orderBy(sortOrder);
                cursor = db.query(builder.create());
                break;
            case CODE_CREW_WITH_ID:
                builder = androidx.sqlite.db.SupportSQLiteQueryBuilder.builder(DataContract.Credits.TABLE_NAME)
                        .columns(projection)
                        .selection(DataContract.Credits.COLUMN_MOVIE_ID + " = ? AND "
                                + DataContract.Credits.COLUMN_TYPE + " = ? ", new String[]{uri.getLastPathSegment(), "1"})
                        .orderBy(sortOrder);
                cursor = db.query(builder.create());
                break;
            case CODE_POPULAR_SERIES:
                builder = androidx.sqlite.db.SupportSQLiteQueryBuilder.builder(DataContract.PopularSerieEntry.TABLE_NAME)
                        .columns(projection)
                        .selection(selection, selectionArgs)
                        .orderBy(sortOrder);
                cursor = db.query(builder.create());
                break;
            case CODE_POPULAR_SERIES_WITH_ID:
                builder = androidx.sqlite.db.SupportSQLiteQueryBuilder.builder(DataContract.PopularSerieEntry.TABLE_NAME)
                        .columns(projection)
                        .selection(DataContract.PopularMovieEntry.COLUMN_MOVIE_ID + " = ? ", new String[]{uri.getLastPathSegment()})
                        .orderBy(sortOrder);
                cursor = db.query(builder.create());
                break;
            case CODE_TOP_SERIES:
                builder = androidx.sqlite.db.SupportSQLiteQueryBuilder.builder(DataContract.TopRatedSerieEntry.TABLE_NAME)
                        .columns(projection)
                        .selection(selection, selectionArgs)
                        .orderBy(sortOrder);
                cursor = db.query(builder.create());
                break;
            case CODE_TOP_SERIES_WITH_ID:
                builder = androidx.sqlite.db.SupportSQLiteQueryBuilder.builder(DataContract.TopRatedSerieEntry.TABLE_NAME)
                        .columns(projection)
                        .selection(DataContract.PopularMovieEntry.COLUMN_MOVIE_ID + " = ? ", new String[]{uri.getLastPathSegment()})
                        .orderBy(sortOrder);
                cursor = db.query(builder.create());
                break;
            case CODE_ON_THE_AIR_SERIES:
                builder = androidx.sqlite.db.SupportSQLiteQueryBuilder.builder(DataContract.OnTheAirSerieEntry.TABLE_NAME)
                        .columns(projection)
                        .selection(selection, selectionArgs)
                        .orderBy(sortOrder);
                cursor = db.query(builder.create());
                break;
            case CODE_ON_THE_AIR_SERIES_WITH_ID:
                builder = androidx.sqlite.db.SupportSQLiteQueryBuilder.builder(DataContract.OnTheAirSerieEntry.TABLE_NAME)
                        .columns(projection)
                        .selection(DataContract.PopularMovieEntry.COLUMN_MOVIE_ID + " = ? ", new String[]{uri.getLastPathSegment()})
                        .orderBy(sortOrder);
                cursor = db.query(builder.create());
                break;
            case CODE_FAVORITES:
                builder = androidx.sqlite.db.SupportSQLiteQueryBuilder.builder(DataContract.Favorites.TABLE_NAME)
                        .columns(projection)
                        .selection(selection, selectionArgs)
                        .orderBy(sortOrder);
                cursor = db.query(builder.create());
                break;
            case CODE_FAVORITES_WITH_ID:
                builder = androidx.sqlite.db.SupportSQLiteQueryBuilder.builder(DataContract.Favorites.TABLE_NAME)
                        .columns(projection)
                        .selection(DataContract.PopularMovieEntry.COLUMN_MOVIE_ID + " = ? ", new String[]{uri.getLastPathSegment()})
                        .orderBy(sortOrder);
                cursor = db.query(builder.create());
                break;
            case CODE_SEARCH:
                builder = androidx.sqlite.db.SupportSQLiteQueryBuilder.builder(DataContract.Search.TABLE_NAME)
                        .columns(projection)
                        .selection(selection, selectionArgs)
                        .orderBy(sortOrder);
                cursor = db.query(builder.create());
                break;
            case CODE_SEARCH_WITH_ID:
                builder = androidx.sqlite.db.SupportSQLiteQueryBuilder.builder(DataContract.Search.TABLE_NAME)
                        .columns(projection)
                        .selection(DataContract.Search.COLUMN_MOVIE_ID + " = ? ", new String[]{uri.getLastPathSegment()})
                        .orderBy(sortOrder);
                cursor = db.query(builder.create());
                break;
            case CODE_SEARCH_TV:
                builder = androidx.sqlite.db.SupportSQLiteQueryBuilder.builder(DataContract.SearchTv.TABLE_NAME)
                        .columns(projection)
                        .selection(selection, selectionArgs)
                        .orderBy(sortOrder);
                cursor = db.query(builder.create());
                break;
            case CODE_SEARCH_TV_WITH_ID:
                builder = androidx.sqlite.db.SupportSQLiteQueryBuilder.builder(DataContract.SearchTv.TABLE_NAME)
                        .columns(projection)
                        .selection(DataContract.Search.COLUMN_MOVIE_ID + " = ? ", new String[]{uri.getLastPathSegment()})
                        .orderBy(sortOrder);
                cursor = db.query(builder.create());
                break;
            case CODE_REVIEW:
                builder = androidx.sqlite.db.SupportSQLiteQueryBuilder.builder(DataContract.Review.TABLE_NAME)
                        .columns(projection)
                        .selection(selection, selectionArgs)
                        .orderBy(sortOrder);
                cursor = db.query(builder.create());
                break;
            case CODE_REVIEW_WITH_ID:
                builder = androidx.sqlite.db.SupportSQLiteQueryBuilder.builder(DataContract.Review.TABLE_NAME)
                        .columns(projection)
                        .selection(DataContract.Review.COLUMN_MOVIE_ID + " = ? ", new String[]{uri.getLastPathSegment()})
                        .orderBy(sortOrder);
                cursor = db.query(builder.create());
                break;
            case CODE_VIDEOS:
                builder = androidx.sqlite.db.SupportSQLiteQueryBuilder.builder(DataContract.Videos.TABLE_NAME)
                        .columns(projection)
                        .selection(selection, selectionArgs)
                        .orderBy(sortOrder);
                cursor = db.query(builder.create());
                break;
            case CODE_VIDEOS_WITH_ID:
                builder = androidx.sqlite.db.SupportSQLiteQueryBuilder.builder(DataContract.Videos.TABLE_NAME)
                        .columns(projection)
                        .selection(DataContract.Videos.COLUMN_MOVIE_ID + " = ? ", new String[]{uri.getLastPathSegment()})
                        .orderBy(sortOrder);
                cursor = db.query(builder.create());
                break;
            case CODE_SEASONS:
                builder = androidx.sqlite.db.SupportSQLiteQueryBuilder.builder(DataContract.Seasons.TABLE_NAME)
                        .columns(projection)
                        .selection(selection, selectionArgs)
                        .orderBy(sortOrder);
                cursor = db.query(builder.create());
                break;
            case CODE_SEASONS_WITH_ID:
                builder = androidx.sqlite.db.SupportSQLiteQueryBuilder.builder(DataContract.Seasons.TABLE_NAME)
                        .columns(projection)
                        .selection(DataContract.Seasons.COLUMN_SERIE_ID + " = ? ", new String[]{uri.getLastPathSegment()})
                        .orderBy(sortOrder);
                cursor = db.query(builder.create());
                break;
            case CODE_EPISODES:
                builder = androidx.sqlite.db.SupportSQLiteQueryBuilder.builder(DataContract.Episodes.TABLE_NAME)
                        .columns(projection)
                        .selection(selection, selectionArgs)
                        .orderBy(sortOrder);
                cursor = db.query(builder.create());
                break;
            case CODE_EPISODES_WITH_ID:
                builder = androidx.sqlite.db.SupportSQLiteQueryBuilder.builder(DataContract.Episodes.TABLE_NAME)
                        .columns(projection)
                        .selection(DataContract.Episodes.COLUMN_EPISODE_ID + " = ? ", new String[]{uri.getLastPathSegment()})
                        .orderBy(sortOrder);
                cursor = db.query(builder.create());
                break;
            case CODE_PERSON:
                builder = androidx.sqlite.db.SupportSQLiteQueryBuilder.builder(DataContract.Person.TABLE_NAME)
                        .columns(projection)
                        .selection(selection, selectionArgs)
                        .orderBy(sortOrder);
                cursor = db.query(builder.create());
                break;
            case CODE_PERSON_WITH_ID:
                builder = androidx.sqlite.db.SupportSQLiteQueryBuilder.builder(DataContract.Person.TABLE_NAME)
                        .columns(projection)
                        .selection(DataContract.Person.COLUMN_PERSON_ID + " = ? ", new String[]{uri.getLastPathSegment()})
                        .orderBy(sortOrder);
                cursor = db.query(builder.create());
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        switch (uriMatcher.match(uri)) {
            case CODE_POPULAR_MOVIES:
                return DataContract.PopularMovieEntry.CONTENT_TYPE;
            case CODE_POPULAR_MOVIES_WITH_ID:
            case CODE_POPULAR_MOVIES_WITH_TITLE:
                return DataContract.PopularMovieEntry.CONTENT_ITEM_TYPE;
            case CODE_TOP_MOVIES:
                return DataContract.TopRatedMovieEntry.CONTENT_TYPE;
            case CODE_TOP_MOVIES_WITH_ID:
                return DataContract.TopRatedMovieEntry.CONTENT_ITEM_TYPE;
            case CODE_UPCOMING_MOVIES:
                return DataContract.UpcomingMovieEntry.CONTENT_TYPE;
            case CODE_UPCOMING_MOVIES_WITH_ID:
                return DataContract.UpcomingMovieEntry.CONTENT_ITEM_TYPE;
            case CODE_FAVORITES:
                return DataContract.Favorites.CONTENT_TYPE;
            case CODE_FAVORITES_WITH_ID:
                return DataContract.Favorites.CONTENT_ITEM_TYPE;
            case CODE_POPULAR_SERIES:
                return DataContract.PopularSerieEntry.CONTENT_TYPE;
            case CODE_POPULAR_SERIES_WITH_ID:
                return DataContract.PopularSerieEntry.CONTENT_ITEM_TYPE;
            case CODE_TOP_SERIES:
                return DataContract.TopRatedSerieEntry.CONTENT_TYPE;
            case CODE_TOP_SERIES_WITH_ID:
                return DataContract.TopRatedSerieEntry.CONTENT_ITEM_TYPE;
            case CODE_ON_THE_AIR_SERIES:
                return DataContract.OnTheAirSerieEntry.CONTENT_TYPE;
            case CODE_ON_THE_AIR_SERIES_WITH_ID:
                return DataContract.OnTheAirSerieEntry.CONTENT_ITEM_TYPE;
            case CODE_SEARCH:
                return DataContract.Search.CONTENT_TYPE;
            case CODE_SEARCH_WITH_ID:
                return DataContract.Search.CONTENT_ITEM_TYPE;
            case CODE_SEARCH_TV:
                return DataContract.SearchTv.CONTENT_TYPE;
            case CODE_SEARCH_TV_WITH_ID:
                return DataContract.SearchTv.CONTENT_ITEM_TYPE;
            case CODE_THEATER_MOVIES:
                return DataContract.TheaterMovieEntry.CONTENT_TYPE;
            case CODE_THEATER_MOVIES_WITH_ID:
                return DataContract.TheaterMovieEntry.CONTENT_ITEM_TYPE;
            case CODE_CAST:
                return DataContract.Credits.CONTENT_TYPE_CAST;
            case CODE_CAST_WITH_ID:
                return DataContract.Credits.CONTENT_ITEM_TYPE_CAST;
            case CODE_CREW:
                return DataContract.Credits.CONTENT_TYPE_CREW;
            case CODE_CREW_WITH_ID:
                return DataContract.Credits.CONTENT_ITEM_TYPE_CREW;
            case CODE_REVIEW:
                return DataContract.Review.CONTENT_TYPE;
            case CODE_REVIEW_WITH_ID:
                return DataContract.Review.CONTENT_ITEM_TYPE;
            case CODE_VIDEOS:
                return DataContract.Videos.CONTENT_TYPE;
            case CODE_VIDEOS_WITH_ID:
                return DataContract.Videos.CONTENT_ITEM_TYPE;
            case CODE_SEASONS:
                return DataContract.Seasons.CONTENT_TYPE;
            case CODE_SEASONS_WITH_ID:
                return DataContract.Seasons.CONTENT_ITEM_TYPE;
            case CODE_EPISODES:
                return DataContract.Episodes.CONTENT_TYPE;
            case CODE_EPISODES_WITH_ID:
                return DataContract.Episodes.CONTENT_ITEM_TYPE;
            case CODE_PERSON:
                return DataContract.Person.CONTENT_TYPE;
            case CODE_PERSON_WITH_ID:
                return DataContract.Person.CONTENT_ITEM_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, ContentValues values) {
        final androidx.sqlite.db.SupportSQLiteDatabase db = database.getOpenHelper().getWritableDatabase();
        Uri returnUri;
        switch (uriMatcher.match(uri)) {
            case CODE_POPULAR_MOVIES: {
                long _id = db.insert(DataContract.PopularMovieEntry.TABLE_NAME, android.database.sqlite.SQLiteDatabase.CONFLICT_NONE, values);
                if (_id > 0) {
                    returnUri = DataContract.PopularMovieEntry.buildMovieUriWithId(_id);
                } else {
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                }
                break;
            }
            case CODE_TOP_MOVIES: {
                long _id = db.insert(DataContract.TopRatedMovieEntry.TABLE_NAME, android.database.sqlite.SQLiteDatabase.CONFLICT_NONE, values);
                if (_id > 0) {
                    returnUri = DataContract.TopRatedMovieEntry.buildMovieUriWithId(_id);
                } else {
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                }
                break;
            }
            case CODE_UPCOMING_MOVIES: {
                long _id = db.insert(DataContract.UpcomingMovieEntry.TABLE_NAME, android.database.sqlite.SQLiteDatabase.CONFLICT_NONE, values);
                if (_id > 0) {
                    returnUri = DataContract.UpcomingMovieEntry.buildMovieUriWithId(_id);
                } else {
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                }
                break;
            }
            case CODE_THEATER_MOVIES: {
                long _id = db.insert(DataContract.TheaterMovieEntry.TABLE_NAME, android.database.sqlite.SQLiteDatabase.CONFLICT_NONE, values);
                if (_id > 0) {
                    returnUri = DataContract.TheaterMovieEntry.buildMovieUriWithId(_id);
                } else {
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                }
                break;
            }
            case CODE_CAST: {
                long _id = db.insert(DataContract.Credits.TABLE_NAME, android.database.sqlite.SQLiteDatabase.CONFLICT_NONE, values);
                if (_id > 0) {
                    returnUri = DataContract.Credits.buildCastUriWithId(_id);
                } else {
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                }
                break;
            }
            case CODE_CREW: {
                long _id = db.insert(DataContract.Credits.TABLE_NAME, android.database.sqlite.SQLiteDatabase.CONFLICT_NONE, values);
                if (_id > 0) {
                    returnUri = DataContract.Credits.buildCrewUriWithId(_id);
                } else {
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                }
                break;
            }
            case CODE_POPULAR_SERIES: {
                long _id = db.insert(DataContract.PopularSerieEntry.TABLE_NAME, android.database.sqlite.SQLiteDatabase.CONFLICT_NONE, values);
                if (_id > 0) {
                    returnUri = DataContract.PopularSerieEntry.buildSerieUriWithId(_id);
                } else {
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                }
                break;
            }
            case CODE_TOP_SERIES: {
                long _id = db.insert(DataContract.TopRatedSerieEntry.TABLE_NAME, android.database.sqlite.SQLiteDatabase.CONFLICT_NONE, values);
                if (_id > 0) {
                    returnUri = DataContract.TopRatedSerieEntry.buildSerieUriWithId(_id);
                } else {
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                }
                break;
            }
            case CODE_ON_THE_AIR_SERIES: {
                long _id = db.insert(DataContract.OnTheAirSerieEntry.TABLE_NAME, android.database.sqlite.SQLiteDatabase.CONFLICT_REPLACE, values);
                if (_id > 0) {
                    returnUri = DataContract.OnTheAirSerieEntry.buildSerieUriWithId(_id);
                } else {
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                }
                break;
            }
            case CODE_FAVORITES: {
                long _id = db.insert(DataContract.Favorites.TABLE_NAME, android.database.sqlite.SQLiteDatabase.CONFLICT_REPLACE, values);
                if (_id > 0) {
                    returnUri = DataContract.Favorites.buildFavoritesUriWithId(_id);
                } else {
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                }
                break;
            }
            case CODE_SEARCH: {
                long _id = db.insert(DataContract.Search.TABLE_NAME, android.database.sqlite.SQLiteDatabase.CONFLICT_REPLACE, values);
                if (_id > 0) {
                    returnUri = DataContract.Search.buildSearchUriWithId(_id);
                } else {
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                }
                break;
            }
            case CODE_SEARCH_TV: {
                long _id = db.insert(DataContract.SearchTv.TABLE_NAME, android.database.sqlite.SQLiteDatabase.CONFLICT_REPLACE, values);
                if (_id > 0) {
                    returnUri = DataContract.SearchTv.buildSearchUriWithId(_id);
                } else {
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                }
                break;
            }
            case CODE_REVIEW: {
                long _id = db.insert(DataContract.Review.TABLE_NAME, android.database.sqlite.SQLiteDatabase.CONFLICT_REPLACE, values);
                if (_id > 0) {
                    returnUri = DataContract.Review.buildReviewUriWithId(_id);
                } else {
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                }
                break;
            }
            case CODE_VIDEOS: {
                long _id = db.insert(DataContract.Videos.TABLE_NAME, android.database.sqlite.SQLiteDatabase.CONFLICT_REPLACE, values);
                if (_id > 0) {
                    returnUri = DataContract.Videos.buildVideoUriWithId(_id);
                } else {
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                }
                break;
            }
            case CODE_SEASONS: {
                long _id = db.insert(DataContract.Seasons.TABLE_NAME, android.database.sqlite.SQLiteDatabase.CONFLICT_REPLACE, values);
                if (_id > 0) {
                    returnUri = DataContract.Seasons.buildSeasonUriWithId(_id);
                } else {
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                }
                break;
            }
            case CODE_EPISODES: {
                long _id = db.insert(DataContract.Episodes.TABLE_NAME, android.database.sqlite.SQLiteDatabase.CONFLICT_REPLACE, values);
                if (_id > 0) {
                    returnUri = DataContract.Episodes.buildEpisodeUriWithId(_id);
                } else {
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                }
                break;
            }
            case CODE_PERSON: {
                long _id = db.insert(DataContract.Person.TABLE_NAME, android.database.sqlite.SQLiteDatabase.CONFLICT_REPLACE, values);
                if (_id > 0) {
                    returnUri = DataContract.Person.buildPersonUriWithId(_id);
                } else {
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                }
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    /**
     * Handles requests to insert a set of new rows. In our case, we are going to be
     * inserting multiple rows of data at a time from a json.
     *
     * @param uri    The content:// URI of the insertion request.
     * @param values An array of sets of column_name/value pairs to add to the database.
     *               This must not be {@code null}.
     * @return The number of values that were inserted.
     */
    @Override
    public int bulkInsert(@NonNull Uri uri, @NonNull ContentValues[] values) {
        final androidx.sqlite.db.SupportSQLiteDatabase db = database.getOpenHelper().getWritableDatabase();
        int rowsInserted = 0;
        switch (uriMatcher.match(uri)) {
            case CODE_POPULAR_MOVIES:
                db.beginTransaction();
                try {
                    for (ContentValues value : values) {
                        long _id = db.insert(DataContract.PopularMovieEntry.TABLE_NAME, android.database.sqlite.SQLiteDatabase.CONFLICT_REPLACE, value);
                        if (_id != -1) {
                            rowsInserted++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                if (rowsInserted > 0) {
                    getContext().getContentResolver().notifyChange(uri, null);
                }
                return rowsInserted;
            case CODE_TOP_MOVIES:
                db.beginTransaction();
                try {
                    for (ContentValues value : values) {
                        long _id = db.insert(DataContract.TopRatedMovieEntry.TABLE_NAME, android.database.sqlite.SQLiteDatabase.CONFLICT_REPLACE, value);
                        if (_id != -1) {
                            rowsInserted++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                if (rowsInserted > 0) {
                    getContext().getContentResolver().notifyChange(uri, null);
                }
                return rowsInserted;
            case CODE_UPCOMING_MOVIES:
                db.beginTransaction();
                try {
                    for (ContentValues value : values) {
                        long _id = db.insert(DataContract.UpcomingMovieEntry.TABLE_NAME, android.database.sqlite.SQLiteDatabase.CONFLICT_REPLACE, value);
                        if (_id != -1) {
                            rowsInserted++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                if (rowsInserted > 0) {
                    getContext().getContentResolver().notifyChange(uri, null);
                }
                return rowsInserted;
            case CODE_THEATER_MOVIES:
                db.beginTransaction();
                try {
                    for (ContentValues value : values) {
                        long _id = db.insert(DataContract.TheaterMovieEntry.TABLE_NAME, android.database.sqlite.SQLiteDatabase.CONFLICT_REPLACE, value);
                        if (_id != -1) {
                            rowsInserted++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                if (rowsInserted > 0) {
                    getContext().getContentResolver().notifyChange(uri, null);
                }
                return rowsInserted;
            case CODE_CAST:
                db.beginTransaction();
                try {
                    for (ContentValues value : values) {
                        long _id = db.insert(DataContract.Credits.TABLE_NAME, android.database.sqlite.SQLiteDatabase.CONFLICT_REPLACE, value);
                        if (_id != -1) {
                            rowsInserted++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                if (rowsInserted > 0) {
                    getContext().getContentResolver().notifyChange(uri, null);
                }
                return rowsInserted;
            case CODE_CREW:
                db.beginTransaction();
                try {
                    for (ContentValues value : values) {
                        long _id = db.insert(DataContract.Credits.TABLE_NAME, android.database.sqlite.SQLiteDatabase.CONFLICT_REPLACE, value);
                        if (_id != -1) {
                            rowsInserted++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                if (rowsInserted > 0) {
                    getContext().getContentResolver().notifyChange(uri, null);
                }
                return rowsInserted;
            case CODE_POPULAR_SERIES:
                db.beginTransaction();
                try {
                    for (ContentValues value : values) {
                        long _id = db.insert(DataContract.PopularSerieEntry.TABLE_NAME, android.database.sqlite.SQLiteDatabase.CONFLICT_REPLACE, value);
                        if (_id != -1) {
                            rowsInserted++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                if (rowsInserted > 0) {
                    getContext().getContentResolver().notifyChange(uri, null);
                }
                return rowsInserted;
            case CODE_TOP_SERIES:
                db.beginTransaction();
                try {
                    for (ContentValues value : values) {
                        long _id = db.insert(DataContract.TopRatedSerieEntry.TABLE_NAME, android.database.sqlite.SQLiteDatabase.CONFLICT_REPLACE, value);
                        if (_id != -1) {
                            rowsInserted++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                if (rowsInserted > 0) {
                    getContext().getContentResolver().notifyChange(uri, null);
                }
                return rowsInserted;
            case CODE_ON_THE_AIR_SERIES:
                db.beginTransaction();
                try {
                    for (ContentValues value : values) {
                        long _id = db.insert(DataContract.OnTheAirSerieEntry.TABLE_NAME, android.database.sqlite.SQLiteDatabase.CONFLICT_REPLACE, value);
                        if (_id != -1) {
                            rowsInserted++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                if (rowsInserted > 0) {
                    getContext().getContentResolver().notifyChange(uri, null);
                }
                return rowsInserted;
            case CODE_FAVORITES:
                db.beginTransaction();
                try {
                    for (ContentValues value : values) {
                        long _id = db.insert(DataContract.Favorites.TABLE_NAME, android.database.sqlite.SQLiteDatabase.CONFLICT_REPLACE, value);
                        if (_id != -1) {
                            rowsInserted++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                if (rowsInserted > 0) {
                    getContext().getContentResolver().notifyChange(uri, null);
                }
                return rowsInserted;
            case CODE_SEARCH:
                db.beginTransaction();
                try {
                    for (ContentValues value : values) {
                        long _id = db.insert(DataContract.Search.TABLE_NAME, android.database.sqlite.SQLiteDatabase.CONFLICT_REPLACE, value);
                        if (_id != -1) {
                            rowsInserted++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                if (rowsInserted > 0) {
                    getContext().getContentResolver().notifyChange(uri, null);
                }
                return rowsInserted;
            case CODE_SEARCH_TV:
                db.beginTransaction();
                try {
                    for (ContentValues value : values) {
                        long _id = db.insert(DataContract.SearchTv.TABLE_NAME, android.database.sqlite.SQLiteDatabase.CONFLICT_REPLACE, value);
                        if (_id != -1) {
                            rowsInserted++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                if (rowsInserted > 0) {
                    getContext().getContentResolver().notifyChange(uri, null);
                }
                return rowsInserted;
            case CODE_REVIEW:
                db.beginTransaction();
                try {
                    for (ContentValues value : values) {
                        long _id = db.insert(DataContract.Review.TABLE_NAME, android.database.sqlite.SQLiteDatabase.CONFLICT_REPLACE, value);
                        if (_id != -1) {
                            rowsInserted++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                if (rowsInserted > 0) {
                    getContext().getContentResolver().notifyChange(uri, null);
                }
                return rowsInserted;
            case CODE_VIDEOS:
                db.beginTransaction();
                try {
                    for (ContentValues value : values) {
                        long _id = db.insert(DataContract.Videos.TABLE_NAME, android.database.sqlite.SQLiteDatabase.CONFLICT_REPLACE, value);
                        if (_id != -1) {
                            rowsInserted++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                if (rowsInserted > 0) {
                    getContext().getContentResolver().notifyChange(uri, null);
                }
                return rowsInserted;
            case CODE_SEASONS:
                db.beginTransaction();
                try {
                    for (ContentValues value : values) {
                        long _id = db.insert(DataContract.Seasons.TABLE_NAME, android.database.sqlite.SQLiteDatabase.CONFLICT_REPLACE, value);
                        if (_id != -1) {
                            rowsInserted++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                if (rowsInserted > 0) {
                    getContext().getContentResolver().notifyChange(uri, null);
                }
                return rowsInserted;
            case CODE_EPISODES:
                db.beginTransaction();
                try {
                    for (ContentValues value : values) {
                        long _id = db.insert(DataContract.Episodes.TABLE_NAME, android.database.sqlite.SQLiteDatabase.CONFLICT_REPLACE, value);
                        if (_id != -1) {
                            rowsInserted++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                if (rowsInserted > 0) {
                    getContext().getContentResolver().notifyChange(uri, null);
                }
                return rowsInserted;
            case CODE_PERSON:
                db.beginTransaction();
                try {
                    for (ContentValues value : values) {
                        long _id = db.insert(DataContract.Person.TABLE_NAME, android.database.sqlite.SQLiteDatabase.CONFLICT_REPLACE, value);
                        if (_id != -1) {
                            rowsInserted++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                if (rowsInserted > 0) {
                    getContext().getContentResolver().notifyChange(uri, null);
                }
                return rowsInserted;
            default:
                return super.bulkInsert(uri, values);
        }
    }

    /**
     * Deletes data at a given URI with optional arguments for more fine tuned deletions.
     *
     * @param uri           The full URI to query
     * @param selection     An optional restriction to apply to rows when deleting.
     * @param selectionArgs Used in conjunction with the selection statement
     * @return The number of rows deleted
     */
    @Override
    public int delete(@NonNull Uri uri, String selection, String[] selectionArgs) {
        final androidx.sqlite.db.SupportSQLiteDatabase db = database.getOpenHelper().getWritableDatabase();
        /* Users of the delete method will expect the number of rows deleted to be returned. */
        int rowsDeleted;
        /*
         * If we pass null as the selection to SQLiteDatabase#delete, our entire table will be
         * deleted. However, if we do pass null and delete all of the rows in the table, we won't
         * know how many rows were deleted. According to the documentation for SQLiteDatabase,
         * passing "1" for the selection will delete all rows and return the number of rows
         * deleted, which is what the caller of this method expects.
         */
        if (null == selection) selection = "1";
        switch (uriMatcher.match(uri)) {
            case CODE_POPULAR_MOVIES:
                rowsDeleted = db.delete(
                        DataContract.PopularMovieEntry.TABLE_NAME,
                        selection,
                        selectionArgs);
                break;
            case CODE_POPULAR_MOVIES_WITH_ID:
                rowsDeleted = db.delete(
                        DataContract.PopularMovieEntry.TABLE_NAME,
                        DataContract.PopularMovieEntry._ID + " = ? ",
                        selectionArgs);
                break;
            case CODE_TOP_MOVIES:
                rowsDeleted = db.delete(
                        DataContract.TopRatedMovieEntry.TABLE_NAME,
                        selection,
                        selectionArgs);
                break;
            case CODE_TOP_MOVIES_WITH_ID:
                rowsDeleted = db.delete(
                        DataContract.TopRatedMovieEntry.TABLE_NAME,
                        DataContract.TopRatedMovieEntry._ID + " = ? ",
                        selectionArgs);
                break;
            case CODE_UPCOMING_MOVIES:
                rowsDeleted = db.delete(
                        DataContract.UpcomingMovieEntry.TABLE_NAME,
                        selection,
                        selectionArgs);
                break;
            case CODE_UPCOMING_MOVIES_WITH_ID:
                rowsDeleted = db.delete(
                        DataContract.UpcomingMovieEntry.TABLE_NAME,
                        DataContract.UpcomingMovieEntry._ID + " = ? ",
                        selectionArgs);
                break;
            case CODE_THEATER_MOVIES:
                rowsDeleted = db.delete(
                        DataContract.TheaterMovieEntry.TABLE_NAME,
                        selection,
                        selectionArgs);
                break;
            case CODE_THEATER_MOVIES_WITH_ID:
                rowsDeleted = db.delete(
                        DataContract.TheaterMovieEntry.TABLE_NAME,
                        DataContract.TheaterMovieEntry._ID + " = ? ",
                        selectionArgs);
                break;
            case CODE_CAST:
                rowsDeleted = db.delete(
                        DataContract.Credits.TABLE_NAME,
                        DataContract.Credits.COLUMN_TYPE + " = ? ",
                        new String[]{"0"});
                break;
            case CODE_CAST_WITH_ID:
                rowsDeleted = db.delete(
                        DataContract.Credits.TABLE_NAME,
                        DataContract.Credits._ID + " = ? ",
                        selectionArgs);
                break;
            case CODE_CREW:
                rowsDeleted = db.delete(
                        DataContract.Credits.TABLE_NAME,
                        DataContract.Credits.COLUMN_TYPE + " = ? ",
                        new String[]{"1"});
                break;
            case CODE_CREW_WITH_ID:
                rowsDeleted = db.delete(
                        DataContract.Credits.TABLE_NAME,
                        DataContract.Credits._ID + " = ? ",
                        selectionArgs);
                break;
            case CODE_POPULAR_SERIES:
                rowsDeleted = db.delete(
                        DataContract.PopularSerieEntry.TABLE_NAME,
                        selection,
                        selectionArgs);
                break;
            case CODE_POPULAR_SERIES_WITH_ID:
                rowsDeleted = db.delete(
                        DataContract.PopularSerieEntry.TABLE_NAME,
                        DataContract.PopularSerieEntry._ID + " = ? ",
                        selectionArgs);
                break;
            case CODE_TOP_SERIES:
                rowsDeleted = db.delete(
                        DataContract.TopRatedSerieEntry.TABLE_NAME,
                        selection,
                        selectionArgs);
                break;
            case CODE_TOP_SERIES_WITH_ID:
                rowsDeleted = db.delete(
                        DataContract.TopRatedSerieEntry.TABLE_NAME,
                        DataContract.TopRatedSerieEntry._ID + " = ? ",
                        selectionArgs);
                break;
            case CODE_ON_THE_AIR_SERIES:
                rowsDeleted = db.delete(
                        DataContract.OnTheAirSerieEntry.TABLE_NAME,
                        selection,
                        selectionArgs);
                break;
            case CODE_ON_THE_AIR_SERIES_WITH_ID:
                rowsDeleted = db.delete(
                        DataContract.OnTheAirSerieEntry.TABLE_NAME,
                        DataContract.OnTheAirSerieEntry._ID + " = ? ",
                        selectionArgs);
                break;
            case CODE_FAVORITES:
                rowsDeleted = db.delete(
                        DataContract.Favorites.TABLE_NAME,
                        selection,
                        selectionArgs);
                break;
            case CODE_FAVORITES_WITH_ID:
                rowsDeleted = db.delete(
                        DataContract.Favorites.TABLE_NAME,
                        DataContract.PopularMovieEntry.COLUMN_MOVIE_ID + " = ? ",
                        selectionArgs);
                break;
            case CODE_SEARCH:
                rowsDeleted = db.delete(
                        DataContract.Search.TABLE_NAME,
                        selection,
                        selectionArgs);
                break;
            case CODE_SEARCH_WITH_ID:
                rowsDeleted = db.delete(
                        DataContract.Search.TABLE_NAME,
                        DataContract.Search._ID + " = ? ",
                        selectionArgs);
                break;
            case CODE_SEARCH_TV:
                rowsDeleted = db.delete(
                        DataContract.SearchTv.TABLE_NAME,
                        selection,
                        selectionArgs);
                break;
            case CODE_SEARCH_TV_WITH_ID:
                rowsDeleted = db.delete(
                        DataContract.SearchTv.TABLE_NAME,
                        DataContract.SearchTv._ID + " = ? ",
                        selectionArgs);
                break;
            case CODE_REVIEW:
                rowsDeleted = db.delete(
                        DataContract.Review.TABLE_NAME,
                        selection,
                        selectionArgs);
                break;
            case CODE_REVIEW_WITH_ID:
                rowsDeleted = db.delete(
                        DataContract.Review.TABLE_NAME,
                        DataContract.Review._ID + " = ? ",
                        selectionArgs);
                break;
            case CODE_VIDEOS:
                rowsDeleted = db.delete(
                        DataContract.Videos.TABLE_NAME,
                        selection,
                        selectionArgs);
                break;
            case CODE_VIDEOS_WITH_ID:
                rowsDeleted = db.delete(
                        DataContract.Videos.TABLE_NAME,
                        DataContract.Videos._ID + " = ? ",
                        selectionArgs);
                break;
            case CODE_SEASONS:
                rowsDeleted = db.delete(
                        DataContract.Seasons.TABLE_NAME,
                        selection,
                        selectionArgs);
                break;
            case CODE_SEASONS_WITH_ID:
                rowsDeleted = db.delete(
                        DataContract.Seasons.TABLE_NAME,
                        DataContract.Seasons._ID + " = ? ",
                        selectionArgs);
                break;
            case CODE_EPISODES:
                rowsDeleted = db.delete(
                        DataContract.Episodes.TABLE_NAME,
                        selection,
                        selectionArgs);
                break;
            case CODE_EPISODES_WITH_ID:
                rowsDeleted = db.delete(
                        DataContract.Episodes.TABLE_NAME,
                        DataContract.Episodes._ID + " = ? ",
                        selectionArgs);
                break;
            case CODE_PERSON:
                rowsDeleted = db.delete(
                        DataContract.Person.TABLE_NAME,
                        selection,
                        selectionArgs);
                break;
            case CODE_PERSON_WITH_ID:
                rowsDeleted = db.delete(
                        DataContract.Person.TABLE_NAME,
                        DataContract.Person._ID + " = ? ",
                        selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        /* If we actually deleted any rows, notify that a change has occurred to this URI */
        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        final androidx.sqlite.db.SupportSQLiteDatabase db = database.getOpenHelper().getWritableDatabase();
        int rowsUpdated;
        switch (uriMatcher.match(uri)) {
            case CODE_POPULAR_MOVIES:
                rowsUpdated = db.update(DataContract.PopularMovieEntry.TABLE_NAME, android.database.sqlite.SQLiteDatabase.CONFLICT_REPLACE, values, selection, selectionArgs);
                break;
            case CODE_TOP_MOVIES:
                rowsUpdated = db.update(DataContract.TopRatedMovieEntry.TABLE_NAME, android.database.sqlite.SQLiteDatabase.CONFLICT_REPLACE, values, selection, selectionArgs);
                break;
            case CODE_UPCOMING_MOVIES:
                rowsUpdated = db.update(DataContract.UpcomingMovieEntry.TABLE_NAME, android.database.sqlite.SQLiteDatabase.CONFLICT_REPLACE, values, selection, selectionArgs);
                break;
            case CODE_THEATER_MOVIES:
                rowsUpdated = db.update(DataContract.TheaterMovieEntry.TABLE_NAME, android.database.sqlite.SQLiteDatabase.CONFLICT_REPLACE, values, selection, selectionArgs);
                break;
            case CODE_CAST:
                rowsUpdated = db.update(DataContract.Credits.TABLE_NAME, android.database.sqlite.SQLiteDatabase.CONFLICT_REPLACE, values, selection, selectionArgs);
                break;
            case CODE_CREW:
                rowsUpdated = db.update(DataContract.Credits.TABLE_NAME, android.database.sqlite.SQLiteDatabase.CONFLICT_REPLACE, values, selection, selectionArgs);
                break;
            case CODE_POPULAR_SERIES:
                rowsUpdated = db.update(DataContract.PopularSerieEntry.TABLE_NAME, android.database.sqlite.SQLiteDatabase.CONFLICT_REPLACE, values, selection, selectionArgs);
                break;
            case CODE_TOP_SERIES:
                rowsUpdated = db.update(DataContract.TopRatedSerieEntry.TABLE_NAME, android.database.sqlite.SQLiteDatabase.CONFLICT_REPLACE, values, selection, selectionArgs);
                break;
            case CODE_ON_THE_AIR_SERIES:
                rowsUpdated = db.update(DataContract.OnTheAirSerieEntry.TABLE_NAME, android.database.sqlite.SQLiteDatabase.CONFLICT_REPLACE, values, selection, selectionArgs);
                break;
            case CODE_FAVORITES:
                rowsUpdated = db.update(DataContract.Favorites.TABLE_NAME, android.database.sqlite.SQLiteDatabase.CONFLICT_REPLACE, values, selection, selectionArgs);
                break;
            case CODE_SEARCH:
                rowsUpdated = db.update(DataContract.Search.TABLE_NAME, android.database.sqlite.SQLiteDatabase.CONFLICT_REPLACE, values, selection, selectionArgs);
                break;
            case CODE_SEARCH_TV:
                rowsUpdated = db.update(DataContract.SearchTv.TABLE_NAME, android.database.sqlite.SQLiteDatabase.CONFLICT_REPLACE, values, selection, selectionArgs);
                break;
            case CODE_REVIEW:
                rowsUpdated = db.update(DataContract.Review.TABLE_NAME, android.database.sqlite.SQLiteDatabase.CONFLICT_REPLACE, values, selection, selectionArgs);
                break;
            case CODE_VIDEOS:
                rowsUpdated = db.update(DataContract.Videos.TABLE_NAME, android.database.sqlite.SQLiteDatabase.CONFLICT_REPLACE, values, selection, selectionArgs);
                break;
            case CODE_SEASONS:
                rowsUpdated = db.update(DataContract.Seasons.TABLE_NAME, android.database.sqlite.SQLiteDatabase.CONFLICT_REPLACE, values, selection, selectionArgs);
                break;
            case CODE_EPISODES:
                rowsUpdated = db.update(DataContract.Episodes.TABLE_NAME, android.database.sqlite.SQLiteDatabase.CONFLICT_REPLACE, values, selection, selectionArgs);
                break;
            case CODE_PERSON:
                rowsUpdated = db.update(DataContract.Person.TABLE_NAME, android.database.sqlite.SQLiteDatabase.CONFLICT_REPLACE, values, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsUpdated;
    }
}
