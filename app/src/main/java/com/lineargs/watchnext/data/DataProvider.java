package com.lineargs.watchnext.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

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
    public static final int CODE_MOVIE_CAST = 500;
    public static final int CODE_MOVIE_CAST_WITH_ID = 501;
    public static final int CODE_MOVIE_CREW = 600;
    public static final int CODE_MOVIE_CREW_WITH_ID = 601;
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
    private DataDbHelper dataDbHelper;

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
        matcher.addURI(authority, DataContract.PATH_CREDIT_CAST, CODE_MOVIE_CAST);
        matcher.addURI(authority, DataContract.PATH_CREDIT_CAST + "/#", CODE_MOVIE_CAST_WITH_ID);
        matcher.addURI(authority, DataContract.PATH_CREDIT_CREW, CODE_MOVIE_CREW);
        matcher.addURI(authority, DataContract.PATH_CREDIT_CREW + "/#", CODE_MOVIE_CREW_WITH_ID);
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
         * lengthy operations will cause lag in your app. Since DataDbHelper's constructor is
         * very lightweight, we are safe to perform that initialization here.
         */
        dataDbHelper = new DataDbHelper(getContext());
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

        /* The returning cursor*/
        Cursor cursor;

        /*
         * Here's the switch statement that, given a URI, will determine what kind of request is
         * being made and query the database accordingly.
         */
        switch (uriMatcher.match(uri)) {
            /*
             * When uriMatcher's match method is called with a URI that looks EXACTLY like this
             *
             *      content://com.lineargs.watchnext/popularmovie/
             *
             * uriMatcher's match method will return the code that indicates to us that we need
             * to return all of the movies in our popularmovies table.
             *
             * In this case, we want to return a cursor that contains every row of data
             * in our popularmovies table.
             */
            case CODE_POPULAR_MOVIES:
                cursor = dataDbHelper.getReadableDatabase().query(
                        /* Table we are going to query */
                        DataContract.PopularMovieEntry.TABLE_NAME,
                        /*
                         * A projection designates the columns we want returned in our Cursor.
                         * Passing null will return all columns of data within the Cursor.
                         * However, if you don't need all the data from the table, it's best
                         * practice to limit the columns returned in the Cursor with a projection.
                         */
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            /*
             * When uriMatcher's match method is called with a URI that looks something like this
             *
             *      content://com.lineargs.watchnext/popularmovie/123567
             *
             * uriMatcher's match method will return the code that indicates to us that we need
             * to return the movie for a particular movie ID. The ID in this code is at the very
             * end of the URI (123567) and can be accessed programmatically using Uri's getLastPathSegment method.
             *
             * In this case, we want to return a cursor that contains one row of data for
             * a particular movie ID.
             */
            case CODE_POPULAR_MOVIES_WITH_ID:
                cursor = dataDbHelper.getReadableDatabase().query(
                        DataContract.PopularMovieEntry.TABLE_NAME,
                        projection,
                        /*
                         * The URI that matches CODE_POPULAR_MOVIES_WITH_ID contains an ID at the end
                         * of it. We extract that ID and use it with these next two lines to
                         * specify the row of movie we want returned in the cursor. We use a
                         * question mark here and then designate selectionArgs as the next
                         * argument for performance reasons. Whatever Strings are contained
                         * within the selectionArgs array will be inserted into the
                         * selection statement by SQLite under the hood.
                         */
                        DataContract.PopularMovieEntry.COLUMN_MOVIE_ID + " = ? ",
                        /*
                         * In order to determine the movie ID associated with this URI, we look at the last
                         * path segment. In the comment above, the last path segment is 123567 and
                         * represents the movie ID.
                         * The query method accepts a string array of arguments, as there may be more
                         * than one "?" in the selection statement. Even though in our case, we only have
                         * one "?", we have to create a string array that only contains one element
                         * because this method signature accepts a string array.
                         */
                        new String[]{uri.getLastPathSegment()},
                        null,
                        null,
                        sortOrder);
                break;
            /*
             * When uriMatcher's match method is called with a URI that looks something like this
             *
             *      content://com.lineargs.watchnext/popularmovie/thor
             *
             * uriMatcher's match method will return the code that indicates to us that we need
             * to return the movie for a particular movie title. The title in this code is at the very
             * end of the URI (thor) and can be accessed programmatically using Uri's getLastPathSegment method.
             *
             * In this case, we want to return a cursor that contains one row of data for
             * a particular movie title.
             */
            case CODE_POPULAR_MOVIES_WITH_TITLE:
                cursor = dataDbHelper.getReadableDatabase().query(
                        DataContract.PopularMovieEntry.TABLE_NAME,
                        projection,
                        DataContract.PopularMovieEntry.COLUMN_TITLE + " = ? ",
                        new String[]{uri.getLastPathSegment()},
                        null,
                        null,
                        sortOrder);
                break;
            case CODE_TOP_MOVIES:
                cursor = dataDbHelper.getReadableDatabase().query(
                        DataContract.TopRatedMovieEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            case CODE_TOP_MOVIES_WITH_ID:
                cursor = dataDbHelper.getReadableDatabase().query(
                        DataContract.TopRatedMovieEntry.TABLE_NAME,
                        projection,
                        DataContract.PopularMovieEntry.COLUMN_MOVIE_ID + " = ? ",
                        new String[]{uri.getLastPathSegment()},
                        null,
                        null,
                        sortOrder);
                break;
            case CODE_UPCOMING_MOVIES:
                cursor = dataDbHelper.getReadableDatabase().query(
                        DataContract.UpcomingMovieEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            case CODE_UPCOMING_MOVIES_WITH_ID:
                cursor = dataDbHelper.getReadableDatabase().query(
                        DataContract.UpcomingMovieEntry.TABLE_NAME,
                        projection,
                        DataContract.PopularMovieEntry.COLUMN_MOVIE_ID + " = ? ",
                        new String[]{uri.getLastPathSegment()},
                        null,
                        null,
                        sortOrder);
                break;
            case CODE_THEATER_MOVIES:
                cursor = dataDbHelper.getReadableDatabase().query(
                        DataContract.TheaterMovieEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            case CODE_THEATER_MOVIES_WITH_ID:
                cursor = dataDbHelper.getReadableDatabase().query(
                        DataContract.TheaterMovieEntry.TABLE_NAME,
                        projection,
                        DataContract.PopularMovieEntry.COLUMN_MOVIE_ID + " = ? ",
                        new String[]{uri.getLastPathSegment()},
                        null,
                        null,
                        sortOrder);
                break;
            case CODE_MOVIE_CAST:
                cursor = dataDbHelper.getReadableDatabase().query(
                        DataContract.CreditCast.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            case CODE_MOVIE_CAST_WITH_ID:
                cursor = dataDbHelper.getReadableDatabase().query(
                        DataContract.CreditCast.TABLE_NAME,
                        projection,
                        DataContract.CreditCast.COLUMN_MOVIE_ID + " = ? ",
                        new String[]{uri.getLastPathSegment()},
                        null,
                        null,
                        sortOrder);
                break;
            case CODE_MOVIE_CREW:
                cursor = dataDbHelper.getReadableDatabase().query(
                        DataContract.CreditCrew.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            case CODE_MOVIE_CREW_WITH_ID:
                cursor = dataDbHelper.getReadableDatabase().query(
                        DataContract.CreditCrew.TABLE_NAME,
                        projection,
                        DataContract.CreditCrew.COLUMN_MOVIE_ID + " = ? ",
                        new String[]{uri.getLastPathSegment()},
                        null,
                        null,
                        sortOrder);
                break;
            case CODE_POPULAR_SERIES:
                cursor = dataDbHelper.getReadableDatabase().query(
                        DataContract.PopularSerieEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            case CODE_POPULAR_SERIES_WITH_ID:
                cursor = dataDbHelper.getReadableDatabase().query(
                        DataContract.PopularSerieEntry.TABLE_NAME,
                        projection,
                        DataContract.PopularMovieEntry.COLUMN_MOVIE_ID + " = ? ",
                        new String[]{uri.getLastPathSegment()},
                        null,
                        null,
                        sortOrder);
                break;
            case CODE_TOP_SERIES:
                cursor = dataDbHelper.getReadableDatabase().query(
                        DataContract.TopRatedSerieEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            case CODE_TOP_SERIES_WITH_ID:
                cursor = dataDbHelper.getReadableDatabase().query(
                        DataContract.TopRatedSerieEntry.TABLE_NAME,
                        projection,
                        DataContract.PopularMovieEntry.COLUMN_MOVIE_ID + " = ? ",
                        new String[]{uri.getLastPathSegment()},
                        null,
                        null,
                        sortOrder);
                break;
            case CODE_ON_THE_AIR_SERIES:
                cursor = dataDbHelper.getReadableDatabase().query(
                        DataContract.OnTheAirSerieEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            case CODE_ON_THE_AIR_SERIES_WITH_ID:
                cursor = dataDbHelper.getReadableDatabase().query(
                        DataContract.OnTheAirSerieEntry.TABLE_NAME,
                        projection,
                        DataContract.PopularMovieEntry.COLUMN_MOVIE_ID + " = ? ",
                        new String[]{uri.getLastPathSegment()},
                        null,
                        null,
                        sortOrder);
                break;
            case CODE_FAVORITES:
                cursor = dataDbHelper.getReadableDatabase().query(
                        DataContract.Favorites.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            case CODE_FAVORITES_WITH_ID:
                cursor = dataDbHelper.getReadableDatabase().query(
                        DataContract.Favorites.TABLE_NAME,
                        projection,
                        DataContract.PopularMovieEntry.COLUMN_MOVIE_ID + " = ? ",
                        new String[]{uri.getLastPathSegment()},
                        null,
                        null,
                        sortOrder);
                break;
            case CODE_SEARCH:
                cursor = dataDbHelper.getReadableDatabase().query(
                        DataContract.Search.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            case CODE_SEARCH_WITH_ID:
                cursor = dataDbHelper.getReadableDatabase().query(
                        DataContract.Search.TABLE_NAME,
                        projection,
                        DataContract.Search.COLUMN_MOVIE_ID + " = ? ",
                        new String[]{uri.getLastPathSegment()},
                        null,
                        null,
                        sortOrder);
                break;
            case CODE_SEARCH_TV:
                cursor = dataDbHelper.getReadableDatabase().query(
                        DataContract.SearchTv.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            case CODE_SEARCH_TV_WITH_ID:
                cursor = dataDbHelper.getReadableDatabase().query(
                        DataContract.SearchTv.TABLE_NAME,
                        projection,
                        DataContract.Search.COLUMN_MOVIE_ID + " = ? ",
                        new String[]{uri.getLastPathSegment()},
                        null,
                        null,
                        sortOrder);
                break;
            case CODE_REVIEW:
                cursor = dataDbHelper.getReadableDatabase().query(
                        DataContract.Review.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            case CODE_REVIEW_WITH_ID:
                cursor = dataDbHelper.getReadableDatabase().query(
                        DataContract.Review.TABLE_NAME,
                        projection,
                        DataContract.Review.COLUMN_MOVIE_ID + " = ? ",
                        new String[]{uri.getLastPathSegment()},
                        null,
                        null,
                        sortOrder);
                break;
            case CODE_VIDEOS:
                cursor = dataDbHelper.getReadableDatabase().query(
                        DataContract.Videos.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            case CODE_VIDEOS_WITH_ID:
                cursor = dataDbHelper.getReadableDatabase().query(
                        DataContract.Videos.TABLE_NAME,
                        projection,
                        DataContract.Videos.COLUMN_MOVIE_ID + " = ? ",
                        new String[]{uri.getLastPathSegment()},
                        null,
                        null,
                        sortOrder);
                break;
            case CODE_SEASONS:
                cursor = dataDbHelper.getReadableDatabase().query(
                        DataContract.Seasons.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            case CODE_SEASONS_WITH_ID:
                cursor = dataDbHelper.getReadableDatabase().query(
                        DataContract.Seasons.TABLE_NAME,
                        projection,
                        DataContract.Seasons.COLUMN_SERIE_ID + " = ? ",
                        new String[]{uri.getLastPathSegment()},
                        null,
                        null,
                        sortOrder);
                break;
            case CODE_EPISODES:
                cursor = dataDbHelper.getReadableDatabase().query(
                        DataContract.Episodes.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            case CODE_EPISODES_WITH_ID:
                cursor = dataDbHelper.getReadableDatabase().query(
                        DataContract.Episodes.TABLE_NAME,
                        projection,
                        DataContract.Episodes.COLUMN_EPISODE_ID + " = ? ",
                        new String[]{uri.getLastPathSegment()},
                        null,
                        null,
                        sortOrder);
                break;
            case CODE_PERSON:
                cursor = dataDbHelper.getReadableDatabase().query(
                        DataContract.Person.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            case CODE_PERSON_WITH_ID:
                cursor = dataDbHelper.getReadableDatabase().query(
                        DataContract.Person.TABLE_NAME,
                        projection,
                        DataContract.Person.COLUMN_PERSON_ID + " = ? ",
                        new String[]{uri.getLastPathSegment()},
                        null,
                        null,
                        sortOrder);
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
            case CODE_MOVIE_CAST:
                return DataContract.CreditCast.CONTENT_TYPE;
            case CODE_MOVIE_CAST_WITH_ID:
                return DataContract.CreditCast.CONTENT_ITEM_TYPE;
            case CODE_MOVIE_CREW:
                return DataContract.CreditCrew.CONTENT_TYPE;
            case CODE_MOVIE_CREW_WITH_ID:
                return DataContract.CreditCrew.CONTENT_ITEM_TYPE;
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
        final SQLiteDatabase db = dataDbHelper.getWritableDatabase();
        Uri returnUri;
        switch (uriMatcher.match(uri)) {
            case CODE_POPULAR_MOVIES: {
                long _id = db.insert(DataContract.PopularMovieEntry.TABLE_NAME, null, values);
                if (_id > 0) {
                    returnUri = DataContract.PopularMovieEntry.buildMovieUriWithId(_id);
                } else {
                    throw new SQLException("Failed to insert row into " + uri);
                }
                break;
            }
            case CODE_TOP_MOVIES: {
                long _id = db.insert(DataContract.TopRatedMovieEntry.TABLE_NAME, null, values);
                if (_id > 0) {
                    returnUri = DataContract.TopRatedMovieEntry.buildMovieUriWithId(_id);
                } else {
                    throw new SQLException("Failed to insert row into " + uri);
                }
                break;
            }
            case CODE_UPCOMING_MOVIES: {
                long _id = db.insert(DataContract.UpcomingMovieEntry.TABLE_NAME, null, values);
                if (_id > 0) {
                    returnUri = DataContract.UpcomingMovieEntry.buildMovieUriWithId(_id);
                } else {
                    throw new SQLException("Failed to insert row into " + uri);
                }
                break;
            }
            case CODE_THEATER_MOVIES: {
                long _id = db.insert(DataContract.TheaterMovieEntry.TABLE_NAME, null, values);
                if (_id > 0) {
                    returnUri = DataContract.TheaterMovieEntry.buildMovieUriWithId(_id);
                } else {
                    throw new SQLException("Failed to insert row into " + uri);
                }
                break;
            }
            case CODE_MOVIE_CAST: {
                long _id = db.insert(DataContract.CreditCast.TABLE_NAME, null, values);
                if (_id > 0) {
                    returnUri = DataContract.CreditCast.buildCastUriWithId(_id);
                } else {
                    throw new SQLException("Failed to insert row into " + uri);
                }
                break;
            }
            case CODE_MOVIE_CREW: {
                long _id = db.insert(DataContract.CreditCrew.TABLE_NAME, null, values);
                if (_id > 0) {
                    returnUri = DataContract.CreditCrew.buildCrewUriWithId(_id);
                } else {
                    throw new SQLException("Failed to insert row into " + uri);
                }
                break;
            }
            case CODE_POPULAR_SERIES: {
                long _id = db.insert(DataContract.PopularSerieEntry.TABLE_NAME, null, values);
                if (_id > 0) {
                    returnUri = DataContract.PopularSerieEntry.buildSerieUriWithId(_id);
                } else {
                    throw new SQLException("Failed to insert row into " + uri);
                }
                break;
            }
            case CODE_TOP_SERIES: {
                long _id = db.insert(DataContract.TopRatedSerieEntry.TABLE_NAME, null, values);
                if (_id > 0) {
                    returnUri = DataContract.TopRatedSerieEntry.buildSerieUriWithId(_id);
                } else {
                    throw new SQLException("Failed to insert row into " + uri);
                }
                break;
            }
            case CODE_ON_THE_AIR_SERIES: {
                long _id = db.insert(DataContract.OnTheAirSerieEntry.TABLE_NAME, null, values);
                if (_id > 0) {
                    returnUri = DataContract.OnTheAirSerieEntry.buildSerieUriWithId(_id);
                } else {
                    throw new SQLException("Failed to insert row into " + uri);
                }
                break;
            }
            case CODE_FAVORITES: {
                long _id = db.insert(DataContract.Favorites.TABLE_NAME, null, values);
                if (_id > 0) {
                    returnUri = DataContract.Favorites.buildFavoritesUriWithId(_id);
                } else {
                    throw new SQLException("Failed to insert row into " + uri);
                }
                break;
            }
            case CODE_SEARCH: {
                long _id = db.insert(DataContract.Search.TABLE_NAME, null, values);
                if (_id > 0) {
                    returnUri = DataContract.Search.buildSearchUriWithId(_id);
                } else {
                    throw new SQLException("Failed to insert row into " + uri);
                }
                break;
            }
            case CODE_SEARCH_TV: {
                long _id = db.insert(DataContract.SearchTv.TABLE_NAME, null, values);
                if (_id > 0) {
                    returnUri = DataContract.SearchTv.buildSearchUriWithId(_id);
                } else {
                    throw new SQLException("Failed to insert row into " + uri);
                }
                break;
            }
            case CODE_REVIEW: {
                long _id = db.insert(DataContract.Review.TABLE_NAME, null, values);
                if (_id > 0) {
                    returnUri = DataContract.Review.buildReviewUriWithId(_id);
                } else {
                    throw new SQLException("Failed to insert row into " + uri);
                }
                break;
            }
            case CODE_VIDEOS: {
                long _id = db.insert(DataContract.Videos.TABLE_NAME, null, values);
                if (_id > 0) {
                    returnUri = DataContract.Videos.buildVideoUriWithId(_id);
                } else {
                    throw new SQLException("Failed to insert row into " + uri);
                }
                break;
            }
            case CODE_SEASONS: {
                long _id = db.insert(DataContract.Seasons.TABLE_NAME, null, values);
                if (_id > 0) {
                    returnUri = DataContract.Seasons.buildSeasonUriWithId(_id);
                } else {
                    throw new SQLException("Failed to insert row into " + uri);
                }
                break;
            }
            case CODE_EPISODES: {
                long _id = db.insert(DataContract.Episodes.TABLE_NAME, null, values);
                if (_id > 0) {
                    returnUri = DataContract.Episodes.buildEpisodeUriWithId(_id);
                } else {
                    throw new SQLException("Failed to insert row into " + uri);
                }
                break;
            }
            case CODE_PERSON: {
                long _id = db.insert(DataContract.Person.TABLE_NAME, null, values);
                if (_id > 0) {
                    returnUri = DataContract.Person.buildPersonUriWithId(_id);
                } else {
                    throw new SQLException("Failed to insert row into " + uri);
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
        final SQLiteDatabase db = dataDbHelper.getWritableDatabase();
        int rowsInserted = 0;
        switch (uriMatcher.match(uri)) {
            case CODE_POPULAR_MOVIES:
                db.beginTransaction();
                try {
                    for (ContentValues value : values) {
                        long _id = db.insert(DataContract.PopularMovieEntry.TABLE_NAME, null, value);
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
                        long _id = db.insert(DataContract.TopRatedMovieEntry.TABLE_NAME, null, value);
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
                        long _id = db.insert(DataContract.UpcomingMovieEntry.TABLE_NAME, null, value);
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
                        long _id = db.insert(DataContract.TheaterMovieEntry.TABLE_NAME, null, value);
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
            case CODE_MOVIE_CAST:
                db.beginTransaction();
                try {
                    for (ContentValues value : values) {
                        long _id = db.insert(DataContract.CreditCast.TABLE_NAME, null, value);
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
            case CODE_MOVIE_CREW:
                db.beginTransaction();
                try {
                    for (ContentValues value : values) {
                        long _id = db.insert(DataContract.CreditCrew.TABLE_NAME, null, value);
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
                        long _id = db.insert(DataContract.PopularSerieEntry.TABLE_NAME, null, value);
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
                        long _id = db.insert(DataContract.TopRatedSerieEntry.TABLE_NAME, null, value);
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
                        long _id = db.insert(DataContract.OnTheAirSerieEntry.TABLE_NAME, null, value);
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
                        long _id = db.insert(DataContract.Favorites.TABLE_NAME, null, value);
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
                        long _id = db.insert(DataContract.Search.TABLE_NAME, null, value);
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
                        long _id = db.insert(DataContract.SearchTv.TABLE_NAME, null, value);
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
                        long _id = db.insert(DataContract.Review.TABLE_NAME, null, value);
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
                        long _id = db.insert(DataContract.Videos.TABLE_NAME, null, value);
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
                        long _id = db.insert(DataContract.Seasons.TABLE_NAME, null, value);
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
                        long _id = db.insert(DataContract.Episodes.TABLE_NAME, null, value);
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
                        long _id = db.insert(DataContract.Person.TABLE_NAME, null, value);
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
                rowsDeleted = dataDbHelper.getWritableDatabase().delete(
                        DataContract.PopularMovieEntry.TABLE_NAME,
                        selection,
                        selectionArgs);
                break;
            case CODE_POPULAR_MOVIES_WITH_ID:
                rowsDeleted = dataDbHelper.getWritableDatabase().delete(
                        DataContract.PopularMovieEntry.TABLE_NAME,
                        DataContract.PopularMovieEntry._ID + " = ? ",
                        selectionArgs);
                break;
            case CODE_TOP_MOVIES:
                rowsDeleted = dataDbHelper.getWritableDatabase().delete(
                        DataContract.TopRatedMovieEntry.TABLE_NAME,
                        selection,
                        selectionArgs);
                break;
            case CODE_TOP_MOVIES_WITH_ID:
                rowsDeleted = dataDbHelper.getWritableDatabase().delete(
                        DataContract.TopRatedMovieEntry.TABLE_NAME,
                        DataContract.TopRatedMovieEntry._ID + " = ? ",
                        selectionArgs);
                break;
            case CODE_UPCOMING_MOVIES:
                rowsDeleted = dataDbHelper.getWritableDatabase().delete(
                        DataContract.UpcomingMovieEntry.TABLE_NAME,
                        selection,
                        selectionArgs);
                break;
            case CODE_UPCOMING_MOVIES_WITH_ID:
                rowsDeleted = dataDbHelper.getWritableDatabase().delete(
                        DataContract.UpcomingMovieEntry.TABLE_NAME,
                        DataContract.UpcomingMovieEntry._ID + " = ? ",
                        selectionArgs);
                break;
            case CODE_THEATER_MOVIES:
                rowsDeleted = dataDbHelper.getWritableDatabase().delete(
                        DataContract.TheaterMovieEntry.TABLE_NAME,
                        selection,
                        selectionArgs);
                break;
            case CODE_THEATER_MOVIES_WITH_ID:
                rowsDeleted = dataDbHelper.getWritableDatabase().delete(
                        DataContract.TheaterMovieEntry.TABLE_NAME,
                        DataContract.TheaterMovieEntry._ID + " = ? ",
                        selectionArgs);
                break;
            case CODE_MOVIE_CAST:
                rowsDeleted = dataDbHelper.getWritableDatabase().delete(
                        DataContract.CreditCast.TABLE_NAME,
                        selection,
                        selectionArgs);
                break;
            case CODE_MOVIE_CAST_WITH_ID:
                rowsDeleted = dataDbHelper.getWritableDatabase().delete(
                        DataContract.CreditCast.TABLE_NAME,
                        DataContract.CreditCast._ID + " = ? ",
                        selectionArgs);
                break;
            case CODE_MOVIE_CREW:
                rowsDeleted = dataDbHelper.getWritableDatabase().delete(
                        DataContract.CreditCrew.TABLE_NAME,
                        selection,
                        selectionArgs);
                break;
            case CODE_MOVIE_CREW_WITH_ID:
                rowsDeleted = dataDbHelper.getWritableDatabase().delete(
                        DataContract.CreditCrew.TABLE_NAME,
                        DataContract.CreditCrew._ID + " = ? ",
                        selectionArgs);
                break;
            case CODE_POPULAR_SERIES:
                rowsDeleted = dataDbHelper.getWritableDatabase().delete(
                        DataContract.PopularSerieEntry.TABLE_NAME,
                        selection,
                        selectionArgs);
                break;
            case CODE_POPULAR_SERIES_WITH_ID:
                rowsDeleted = dataDbHelper.getWritableDatabase().delete(
                        DataContract.PopularSerieEntry.TABLE_NAME,
                        DataContract.PopularSerieEntry._ID + " = ? ",
                        selectionArgs);
                break;
            case CODE_TOP_SERIES:
                rowsDeleted = dataDbHelper.getWritableDatabase().delete(
                        DataContract.TopRatedSerieEntry.TABLE_NAME,
                        selection,
                        selectionArgs);
                break;
            case CODE_TOP_SERIES_WITH_ID:
                rowsDeleted = dataDbHelper.getWritableDatabase().delete(
                        DataContract.TopRatedSerieEntry.TABLE_NAME,
                        DataContract.TopRatedSerieEntry._ID + " = ? ",
                        selectionArgs);
                break;
            case CODE_ON_THE_AIR_SERIES:
                rowsDeleted = dataDbHelper.getWritableDatabase().delete(
                        DataContract.OnTheAirSerieEntry.TABLE_NAME,
                        selection,
                        selectionArgs);
                break;
            case CODE_ON_THE_AIR_SERIES_WITH_ID:
                rowsDeleted = dataDbHelper.getWritableDatabase().delete(
                        DataContract.OnTheAirSerieEntry.TABLE_NAME,
                        DataContract.OnTheAirSerieEntry._ID + " = ? ",
                        selectionArgs);
                break;
            case CODE_FAVORITES:
                rowsDeleted = dataDbHelper.getWritableDatabase().delete(
                        DataContract.Favorites.TABLE_NAME,
                        selection,
                        selectionArgs);
                break;
            case CODE_FAVORITES_WITH_ID:
                rowsDeleted = dataDbHelper.getWritableDatabase().delete(
                        DataContract.Favorites.TABLE_NAME,
                        DataContract.PopularMovieEntry.COLUMN_MOVIE_ID + " = ? ",
                        selectionArgs);
                break;
            case CODE_SEARCH:
                rowsDeleted = dataDbHelper.getWritableDatabase().delete(
                        DataContract.Search.TABLE_NAME,
                        selection,
                        selectionArgs);
                break;
            case CODE_SEARCH_WITH_ID:
                rowsDeleted = dataDbHelper.getWritableDatabase().delete(
                        DataContract.Search.TABLE_NAME,
                        DataContract.Search._ID + " = ? ",
                        selectionArgs);
                break;
            case CODE_SEARCH_TV:
                rowsDeleted = dataDbHelper.getWritableDatabase().delete(
                        DataContract.SearchTv.TABLE_NAME,
                        selection,
                        selectionArgs);
                break;
            case CODE_SEARCH_TV_WITH_ID:
                rowsDeleted = dataDbHelper.getWritableDatabase().delete(
                        DataContract.SearchTv.TABLE_NAME,
                        DataContract.SearchTv._ID + " = ? ",
                        selectionArgs);
                break;
            case CODE_REVIEW:
                rowsDeleted = dataDbHelper.getWritableDatabase().delete(
                        DataContract.Review.TABLE_NAME,
                        selection,
                        selectionArgs);
                break;
            case CODE_REVIEW_WITH_ID:
                rowsDeleted = dataDbHelper.getWritableDatabase().delete(
                        DataContract.Review.TABLE_NAME,
                        DataContract.Review._ID + " = ? ",
                        selectionArgs);
                break;
            case CODE_VIDEOS:
                rowsDeleted = dataDbHelper.getWritableDatabase().delete(
                        DataContract.Videos.TABLE_NAME,
                        selection,
                        selectionArgs);
                break;
            case CODE_VIDEOS_WITH_ID:
                rowsDeleted = dataDbHelper.getWritableDatabase().delete(
                        DataContract.Videos.TABLE_NAME,
                        DataContract.Videos._ID + " = ? ",
                        selectionArgs);
                break;
            case CODE_SEASONS:
                rowsDeleted = dataDbHelper.getWritableDatabase().delete(
                        DataContract.Seasons.TABLE_NAME,
                        selection,
                        selectionArgs);
                break;
            case CODE_SEASONS_WITH_ID:
                rowsDeleted = dataDbHelper.getWritableDatabase().delete(
                        DataContract.Seasons.TABLE_NAME,
                        DataContract.Seasons._ID + " = ? ",
                        selectionArgs);
                break;
            case CODE_EPISODES:
                rowsDeleted = dataDbHelper.getWritableDatabase().delete(
                        DataContract.Episodes.TABLE_NAME,
                        selection,
                        selectionArgs);
                break;
            case CODE_EPISODES_WITH_ID:
                rowsDeleted = dataDbHelper.getWritableDatabase().delete(
                        DataContract.Episodes.TABLE_NAME,
                        DataContract.Episodes._ID + " = ? ",
                        selectionArgs);
                break;
            case CODE_PERSON:
                rowsDeleted = dataDbHelper.getWritableDatabase().delete(
                        DataContract.Person.TABLE_NAME,
                        selection,
                        selectionArgs);
                break;
            case CODE_PERSON_WITH_ID:
                rowsDeleted = dataDbHelper.getWritableDatabase().delete(
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
        final SQLiteDatabase db = dataDbHelper.getWritableDatabase();
        int rowsUpdated;
        switch (uriMatcher.match(uri)) {
            case CODE_POPULAR_MOVIES:
                rowsUpdated = db.update(DataContract.PopularMovieEntry.TABLE_NAME, values, selection, selectionArgs);
                break;
            case CODE_TOP_MOVIES:
                rowsUpdated = db.update(DataContract.TopRatedMovieEntry.TABLE_NAME, values, selection, selectionArgs);
                break;
            case CODE_UPCOMING_MOVIES:
                rowsUpdated = db.update(DataContract.UpcomingMovieEntry.TABLE_NAME, values, selection, selectionArgs);
                break;
            case CODE_THEATER_MOVIES:
                rowsUpdated = db.update(DataContract.TheaterMovieEntry.TABLE_NAME, values, selection, selectionArgs);
                break;
            case CODE_MOVIE_CAST:
                rowsUpdated = db.update(DataContract.CreditCast.TABLE_NAME, values, selection, selectionArgs);
                break;
            case CODE_MOVIE_CREW:
                rowsUpdated = db.update(DataContract.CreditCrew.TABLE_NAME, values, selection, selectionArgs);
                break;
            case CODE_POPULAR_SERIES:
                rowsUpdated = db.update(DataContract.PopularSerieEntry.TABLE_NAME, values, selection, selectionArgs);
                break;
            case CODE_TOP_SERIES:
                rowsUpdated = db.update(DataContract.TopRatedSerieEntry.TABLE_NAME, values, selection, selectionArgs);
                break;
            case CODE_FAVORITES:
                rowsUpdated = db.update(DataContract.Favorites.TABLE_NAME, values, selection, selectionArgs);
                break;
            case CODE_SEARCH:
                rowsUpdated = db.update(DataContract.Search.TABLE_NAME, values, selection, selectionArgs);
                break;
            case CODE_SEARCH_TV:
                rowsUpdated = db.update(DataContract.SearchTv.TABLE_NAME, values, selection, selectionArgs);
                break;
            case CODE_REVIEW:
                rowsUpdated = db.update(DataContract.Review.TABLE_NAME, values, selection, selectionArgs);
                break;
            case CODE_VIDEOS:
                rowsUpdated = db.update(DataContract.Videos.TABLE_NAME, values, selection, selectionArgs);
                break;
            case CODE_SEASONS:
                rowsUpdated = db.update(DataContract.Seasons.TABLE_NAME, values, selection, selectionArgs);
                break;
            case CODE_EPISODES:
                rowsUpdated = db.update(DataContract.Episodes.TABLE_NAME, values, selection, selectionArgs);
                break;
            case CODE_PERSON:
                rowsUpdated = db.update(DataContract.Person.TABLE_NAME, values, selection, selectionArgs);
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
