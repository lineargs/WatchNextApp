package com.lineargs.watchnext.data;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by goranminov on 15/10/2017.
 * <p>
 * Defines table and columns names for the movie database
 */

public class DataContract {

    /**
     * The "CONTENT_AUTHORITY" is a name for the entire content provider, similar to the
     * relationship between a domain name and its website. A convenient string to use for the
     * content authority is the package name for the app, which is guaranteed to be unique on the
     * Play Store.
     */
    static final String CONTENT_AUTHORITY = "com.lineargs.watchnext";

    /**
     * Use CONTENT_AUTHORITY to create the base of all URI's which apps will use to contact
     * the content provider.
     */
    static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    /*
     * Possible paths that can be appended to BASE_CONTENT_URI to form valid URI's that BakeApp
     * can handle. For instance,
     *
     *     content://com.lineargs.watchnext/popularmovie/
     *     [           BASE_CONTENT_URI   ][ PATH_POPULAR_MOVIE ]
     *
     * is a valid path for looking at popular movie data.
     *
     *      content://com.example.goranminov.bakeapp/givemeroot/
     *
     * will fail, as the ContentProvider hasn't been given any information on what to do with
     * "givemeroot".
     */
    public static final String PATH_POPULAR_MOVIE = "popularmovie";
    public static final String PATH_TOP_RATED_MOVIE = "topratedmovie";
    public static final String PATH_UPCOMING_MOVIE = "upcomingmovie";
    public static final String PATH_THEATER_MOVIE = "theatermovie";
    public static final String PATH_CREDIT_CAST = "creditcast";
    public static final String PATH_CREDIT_CREW = "creditcrew";
    public static final String PATH_POPULAR_SERIE = "popularserie";
    public static final String PATH_TOP_SERIE = "topratedserie";
    public static final String PATH_ON_THE_AIR_SERIE = "ontheair";
    public static final String PATH_FAVORITES = "favorites";
    public static final String PATH_SEARCH = "search";
    public static final String PATH_SEARCH_TV = "searchtv";
    public static final String PATH_REVIEW = "review";
    public static final String PATH_VIDEOS = "videos";
    public static final String PATH_SEASONS = "seasons";
    public static final String PATH_EPISODES = "episodes";
    public static final String PATH_PERSON = "person";

    /* Inner class that defines the table contents of the popular movie table */
    public static final class PopularMovieEntry implements BaseColumns {

        /* The base CONTENT_URI used to query the popular movie table from the content provider */
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_POPULAR_MOVIE)
                .build();
        /*The movie details as returned by API*/
        public static final String COLUMN_MOVIE_ID = "movie_id";
        public static final String COLUMN_VOTE_AVERAGE = "vote_average";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_POSTER_PATH = "poster_path";
        public static final String COLUMN_BACKDROP_PATH = "backdrop_path";
        public static final String COLUMN_OVERVIEW = "overview";
        public static final String COLUMN_RELEASE_DATE = "release_date";
        public static final String COLUMN_ORIGINAL_LANGUAGE = "original_language";
        public static final String COLUMN_ORIGINAL_TITLE = "original_title";
        public static final String COLUMN_IMDB_ID = "imdb_id";
        public static final String COLUMN_HOMEPAGE = "homepage";
        public static final String COLUMN_PRODUCTION_COMPANIES = "production_companies";
        public static final String COLUMN_PRODUCTION_COUNTRIES = "production_countries";
        public static final String COLUMN_RUNTIME = "runtime";
        public static final String COLUMN_STATUS = "status";
        public static final String COLUMN_GENRES = "genres";
        static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_POPULAR_MOVIE;
        static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_POPULAR_MOVIE;
        /* Used internally as the name of our popular movies table. */
        public static final String TABLE_NAME = "popularmovies";

        /**
         * Builds a URI that adds the movie id to the end of the popular movies content URI path.
         * This is used to query details about a single movie entry by id.
         *
         * @param id Movie ID
         * @return Uri to query details about a single movie entry
         */
        public static Uri buildMovieUriWithId(long id) {
            return CONTENT_URI.buildUpon()
                    .appendPath(Long.toString(id))
                    .build();
        }

        /**
         * Builds a URI that adds the movie title to the end of the popular movies content URI path.
         * This is used to query details about a single movie entry by title.
         *
         * @param title Movie title
         * @return Uri to query details about a single movie entry
         */
        @SuppressWarnings("unused")
        public static Uri buildMovieUriWithTitle(String title) {
            return CONTENT_URI.buildUpon()
                    .appendPath(title)
                    .build();
        }
    }

    /* Inner class that defines the table contents of the top rated movies table */
    public static final class TopRatedMovieEntry implements BaseColumns {

        /* The base CONTENT_URI used to query the top rated movie table from the content provider */
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_TOP_RATED_MOVIE)
                .build();

        static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_TOP_RATED_MOVIE;
        static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_TOP_RATED_MOVIE;

        /* *************************************
         * We use the Popular Movies column names
         * ************************************* */

        /* Used internally as the name of our top rated movies table. */
        public static final String TABLE_NAME = "topmovies";

        /**
         * Builds a URI that adds the movie id to the end of the top rated content URI path.
         * This is used to query details about a single movie entry by id.
         *
         * @param id Movie ID
         * @return Uri to query details about a single movie entry
         */
        public static Uri buildMovieUriWithId(long id) {
            return CONTENT_URI.buildUpon()
                    .appendPath(Long.toString(id))
                    .build();
        }
    }

    /* Inner class that defines the table contents of the upcoming movies table */
    public static final class UpcomingMovieEntry implements BaseColumns {

        /* The base CONTENT_URI used to query the upcoming table from the content provider */
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_UPCOMING_MOVIE)
                .build();

        static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_UPCOMING_MOVIE;
        static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_UPCOMING_MOVIE;

        /* *************************************
         * We use the Popular Movies column names
         * ************************************* */

        /* Used internally as the name of our upcoming movies table. */
        public static final String TABLE_NAME = "upcomingmovies";

        /**
         * Builds a URI that adds the movie id to the end of the upcoming content URI path.
         * This is used to query details about a single movie entry by id.
         *
         * @param id Movie ID
         * @return Uri to query details about a single movie entry
         */
        public static Uri buildMovieUriWithId(long id) {
            return CONTENT_URI.buildUpon()
                    .appendPath(Long.toString(id))
                    .build();
        }
    }

    /* Inner class that defines the table contents of the in theaters movies table */
    public static final class TheaterMovieEntry implements BaseColumns {

        /* The base CONTENT_URI used to query the in theaters table from the content provider */
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_THEATER_MOVIE)
                .build();

        static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_THEATER_MOVIE;
        static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_THEATER_MOVIE;

        /* *************************************
         * We use the Popular Movies column names
         * ************************************* */

        /* Used internally as the name of our in theaters table. */
        public static final String TABLE_NAME = "theatermovies";

        /**
         * Builds a URI that adds the movie id to the end of the in theaters content URI path.
         * This is used to query details about a single movie entry by id.
         *
         * @param id Movie ID
         * @return Uri to query details about a single movie entry
         */
        public static Uri buildMovieUriWithId(long id) {
            return CONTENT_URI.buildUpon()
                    .appendPath(Long.toString(id))
                    .build();
        }
    }

    /* Inner class that defines the table contents of the credits table */
    public static final class Credits implements BaseColumns {

        /* The base CONTENT_URI used to query the casts table from the content provider */
        public static final Uri CONTENT_URI_CAST = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_CREDIT_CAST)
                .build();
        public static final Uri CONTENT_URI_CREW = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_CREDIT_CREW)
                .build();
        /*The credits details as returned by API*/
        public static final String COLUMN_MOVIE_ID = "movie_id";
        public static final String COLUMN_PERSON_ID = "person_id";
        public static final String COLUMN_CHARACTER_NAME = "character_name";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_JOB = "job";
        /*
         * We use one table for cast and crew for better performance.
         * We check the data in that row if it is crew or cast by
         * checking the type column
         * CAST=0 / CREW=1 */
        public static final String COLUMN_TYPE = "type";
        public static final String COLUMN_PROFILE_PATH = "profile_path";
        static final String CONTENT_TYPE_CAST = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_CREDIT_CAST;
        static final String CONTENT_ITEM_TYPE_CAST = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_CREDIT_CAST;
        static final String CONTENT_TYPE_CREW = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_CREDIT_CREW;
        static final String CONTENT_ITEM_TYPE_CREW = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_CREDIT_CREW;
        /* Used internally as the name of our casts table. */
        public static final String TABLE_NAME = "credits";

        /**
         * Builds a URI that adds the cast id to the end of the casts content URI path.
         * This is used to query details about a single cast entry by id.
         *
         * @param id Cast ID
         * @return Uri to query details about a single cast entry
         */
        public static Uri buildCastUriWithId(long id) {
            return CONTENT_URI_CAST.buildUpon()
                    .appendPath(Long.toString(id))
                    .build();
        }

        /**
         * Builds a URI that adds the crew id to the end of the crew content URI path.
         * This is used to query details about a single crew entry by id.
         *
         * @param id Crew ID
         * @return Uri to query details about a single crew entry
         */
        public static Uri buildCrewUriWithId(long id) {
            return CONTENT_URI_CREW.buildUpon()
                    .appendPath(Long.toString(id))
                    .build();
        }
    }

    /* Inner class that defines the table contents of the crew movies table */
    public static final class CreditCr implements BaseColumns {

        /* The base CONTENT_URI used to query the crew table from the content provider */
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_CREDIT_CREW)
                .build();
        /*The crew details as returned by API*/
        public static final String COLUMN_MOVIE_ID = "movie_id";
        public static final String COLUMN_CREDIT_ID = "credit_id";
        public static final String COLUMN_DEPARTMENT = "department";
        public static final String COLUMN_CREW_ID = "crew_id";
        public static final String COLUMN_JOB = "job";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_PROFILE_PATH = "profile_path";
        static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_CREDIT_CREW;
        static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_CREDIT_CREW;
        /* Used internally as the name of our crew table. */
        public static final String TABLE_NAME = "crew";

        /**
         * Builds a URI that adds the crew id to the end of the crew content URI path.
         * This is used to query details about a single crew entry by id.
         *
         * @param id Crew ID
         * @return Uri to query details about a single crew entry
         */
        public static Uri buildCrewUriWithId(long id) {
            return CONTENT_URI.buildUpon()
                    .appendPath(Long.toString(id))
                    .build();
        }
    }

    /* Inner class that defines the table contents of the popular series table */
    public static final class PopularSerieEntry implements BaseColumns {

        /* The base CONTENT_URI used to query the popular series table from the content provider */
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_POPULAR_SERIE)
                .build();

        static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_POPULAR_SERIE;
        static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_POPULAR_SERIE;

        /* *************************************
         * We use the Popular Movies column names
         * ************************************* */

        /* Used internally as the name of our popular series table. */
        public static final String TABLE_NAME = "popularseries";

        /**
         * Builds a URI that adds the serie id to the end of the popular series content URI path.
         * This is used to query details about a single serie entry by id.
         *
         * @param id Serie ID
         * @return Uri to query details about a single serie entry
         */
        public static Uri buildSerieUriWithId(long id) {
            return CONTENT_URI.buildUpon()
                    .appendPath(Long.toString(id))
                    .build();
        }
    }

    /* Inner class that defines the table contents of the top rated series table */
    public static final class TopRatedSerieEntry implements BaseColumns {

        /* The base CONTENT_URI used to query the top rated series table from the content provider */
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_TOP_SERIE)
                .build();

        static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_TOP_SERIE;
        static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_TOP_SERIE;

        /* *************************************
         * We use the Popular Movies column names
         * ************************************* */

        /* Used internally as the name of our top rated series table. */
        public static final String TABLE_NAME = "topseries";

        /**
         * Builds a URI that adds the serie id to the end of the top rated series content URI path.
         * This is used to query details about a single serie entry by id.
         *
         * @param id Serie ID
         * @return Uri to query details about a single serie entry
         */
        public static Uri buildSerieUriWithId(long id) {
            return CONTENT_URI.buildUpon()
                    .appendPath(Long.toString(id))
                    .build();
        }
    }

    /* Inner class that defines the table contents of the on the air series table */
    public static final class OnTheAirSerieEntry implements BaseColumns {

        /* The base CONTENT_URI used to query the on the air series table from the content provider */
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_ON_THE_AIR_SERIE)
                .build();

        static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_ON_THE_AIR_SERIE;
        static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_ON_THE_AIR_SERIE;

        /* *************************************
         * We use the Popular Movies column names
         * ************************************* */

        /* Used internally as the name of our on the air series table. */
        public static final String TABLE_NAME = "ontheairseries";

        /**
         * Builds a URI that adds the serie id to the end of the on the air series content URI path.
         * This is used to query details about a single serie entry by id.
         *
         * @param id Serie ID
         * @return Uri to query details about a single serie entry
         */
        public static Uri buildSerieUriWithId(long id) {
            return CONTENT_URI.buildUpon()
                    .appendPath(Long.toString(id))
                    .build();
        }
    }

    /* Inner class that defines the table contents of the favorites table */
    public static final class Favorites implements BaseColumns {

        /* The base CONTENT_URI used to query the favorites table from the content provider */
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_FAVORITES)
                .build();
        /*
         * We add this column for better implementation in our Favorites activity
         * where we check if the data in that row is movie or series
         * The value for movie is 0, and for series is 1
         */
        public static final String COLUMN_TYPE = "type";
        static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_FAVORITES;
        static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_FAVORITES;

        /* *************************************
         * We use the Popular Movies column names
         * ************************************* */
        /* Used internally as the name of our favorites table. */
        public static final String TABLE_NAME = "favorites";

        /**
         * Builds a URI that adds the serie/ movie id to the end of the favorites content URI path.
         * This is used to query details about a single entry by id.
         *
         * @param id Serie / Movie ID
         * @return Uri to query details about a single entry
         */
        public static Uri buildFavoritesUriWithId(long id) {
            return CONTENT_URI.buildUpon()
                    .appendPath(Long.toString(id))
                    .build();
        }
    }

    /* Inner class that defines the table contents of the search table */
    public static final class Search implements BaseColumns {

        /* The base CONTENT_URI used to query the search table from the content provider */
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_SEARCH)
                .build();
        /* The search details as returned by API.*/
        public static final String COLUMN_MOVIE_ID = "movie_id";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_POSTER_PATH = "poster_path";
        public static final String COLUMN_TYPE = "type";
        static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_SEARCH;
        static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_SEARCH;
        /* Used internally as the name of our search table. */
        public static final String TABLE_NAME = "search";

        /**
         * Builds a URI that adds the movie id to the end of the search content URI path.
         * This is used to query details about a single entry by id.
         *
         * @param id Movie ID
         * @return Uri to query details about a single entry
         */
        public static Uri buildSearchUriWithId(long id) {
            return CONTENT_URI.buildUpon()
                    .appendPath(Long.toString(id))
                    .build();
        }
    }

    /* Inner class that defines the table contents of the search table */
    public static final class SearchTv implements BaseColumns {

        /* The base CONTENT_URI used to query the search table from the content provider */
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_SEARCH_TV)
                .build();

        static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_SEARCH_TV;
        static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_SEARCH_TV;

        /* *************************************
         * We use the Search column names
         * ************************************* */

        /* Used internally as the name of our search table. */
        public static final String TABLE_NAME = "searchtv";

        /**
         * Builds a URI that adds the serie id to the end of the search content URI path.
         * This is used to query details about a single entry by id.
         *
         * @param id Serie ID
         * @return Uri to query details about a single entry
         */
        static Uri buildSearchUriWithId(long id) {
            return CONTENT_URI.buildUpon()
                    .appendPath(Long.toString(id))
                    .build();
        }
    }

    /* Inner class that defines the table contents of the review table */
    public static final class Review implements BaseColumns {

        /* The base CONTENT_URI used to query the review table from the content provider */
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_REVIEW)
                .build();
        /*
         * The review details as returned by API.
         */
        public static final String COLUMN_MOVIE_ID = "movie_id";
        public static final String COLUMN_REVIEW_ID = "review_id";
        public static final String COLUMN_AUTHOR = "author";
        public static final String COLUMN_CONTENT = "content";
        public static final String COLUMN_URL = "url";
        static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_REVIEW;
        static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_REVIEW;
        /* Used internally as the name of our review table. */
        public static final String TABLE_NAME = "review";

        /**
         * Builds a URI that adds the review id to the end of the content URI path.
         * This is used to query details about a single entry by id.
         *
         * @param id Review ID
         * @return Uri to query details about a single entry
         */
        public static Uri buildReviewUriWithId(long id) {
            return CONTENT_URI.buildUpon()
                    .appendPath(Long.toString(id))
                    .build();
        }
    }

    /* Inner class that defines the table contents of the videos table */
    public static final class Videos implements BaseColumns {

        /* The base CONTENT_URI used to query the videos table from the content provider */
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_VIDEOS)
                .build();
        /*
         * The videos details as returned by API.
         */
        public static final String COLUMN_MOVIE_ID = "movie_id";
        public static final String COLUMN_VIDEO_ID = "video_id";
        public static final String COLUMN_KEY = "key";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_SITE = "site";
        public static final String COLUMN_TYPE = "type";
        public static final String COLUMN_IMG = "image";
        static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_VIDEOS;
        static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_VIDEOS;
        /* Used internally as the name of our videos table. */
        public static final String TABLE_NAME = "videos";

        /**
         * Builds a URI that adds the video id to the end of the content URI path.
         * This is used to query details about a single entry by id.
         *
         * @param id Video ID
         * @return Uri to query details about a single entry
         */
        public static Uri buildVideoUriWithId(long id) {
            return CONTENT_URI.buildUpon()
                    .appendPath(Long.toString(id))
                    .build();
        }
    }

    /* Inner class that defines the table contents of the seasons table */
    public static final class Seasons implements BaseColumns {

        /* The base CONTENT_URI used to query the seasons table from the content provider */
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_SEASONS)
                .build();
        /*
         * The seasons details as returned by API.
         */
        public static final String COLUMN_SEASON_ID = "season_id";
        public static final String COLUMN_SERIE_ID = "movie_id";
        public static final String COLUMN_RELEASE_DATE = "release_date";
        public static final String COLUMN_EPISODE_COUNT = "episode_count";
        public static final String COLUMN_POSTER_PATH = "poster_path";
        public static final String COLUMN_SEASON_NUMBER = "season_number";
        public static final String COLUMN_SHOW_NAME = "name";
        static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_SEASONS;
        static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_SEASONS;
        /* Used internally as the name of our seasons table. */
        public static final String TABLE_NAME = "seasons";

        /**
         * Builds a URI that adds the season id to the end of the content URI path.
         * This is used to query details about a single entry by id.
         *
         * @param id Season ID
         * @return Uri to query details about a single entry
         */
        public static Uri buildSeasonUriWithId(long id) {
            return CONTENT_URI.buildUpon()
                    .appendPath(Long.toString(id))
                    .build();
        }
    }

    /* Inner class that defines the table contents of the episodes table */
    public static final class Episodes implements BaseColumns {

        /* The base CONTENT_URI used to query the table from the content provider */
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_EPISODES)
                .build();
        /*
         * The details as returned by API.
         */
        public static final String COLUMN_SEASON_ID = "season_id";
        public static final String COLUMN_EPISODE_ID = "episode_id";
        public static final String COLUMN_RELEASE_DATE = "release_date";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_STILL_PATH = "still_path";
        public static final String COLUMN_SEASON_NUMBER = "season_number";
        public static final String COLUMN_EPISODE_NUMBER = "episode_number";
        public static final String COLUMN_OVERVIEW = "overview";
        public static final String COLUMN_VOTE_AVERAGE = "vote_average";
        public static final String COLUMN_VOTE_COUNT = "vote_count";
        public static final String COLUMN_DIRECTORS = "directors";
        public static final String COLUMN_WRITERS = "writers";
        public static final String COLUMN_GUEST_STARS = "guest_stars";
        static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_EPISODES;
        static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_EPISODES;
        /* Used internally as the name of our table. */
        public static final String TABLE_NAME = "episodes";

        /**
         * Builds a URI that adds the episode id to the end of the episode content URI path.
         * This is used to query details about a single entry by id.
         *
         * @param id Episode ID
         * @return Uri to query details about a single entry
         */
        public static Uri buildEpisodeUriWithId(long id) {
            return CONTENT_URI.buildUpon()
                    .appendPath(Long.toString(id))
                    .build();
        }
    }

    /* Inner class that defines the table contents of the person table */
    public static final class Person implements BaseColumns {

        /* The base CONTENT_URI used to query the table from the content provider */
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_PERSON)
                .build();
        /*
         * The details as returned by API.
         */
        public static final String COLUMN_PERSON_ID = "person_id";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_BIRTHDAY = "birthday";
        public static final String COLUMN_BIOGRAPHY = "biography";
        public static final String COLUMN_PLACE_OF_BIRTH = "place_of_birth";
        public static final String COLUMN_PROFILE_PATH = "profile_path";
        public static final String COLUMN_HOMEPAGE = "homepage";
        static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_PERSON;
        static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_PERSON;
        /* Used internally as the name of our table. */
        public static final String TABLE_NAME = "person";

        /**
         * Builds a URI that adds the person id to the end of the content URI path.
         * This is used to query details about a single entry by id.
         *
         * @param id Person ID
         * @return Uri to query details about a single entry
         */
        public static Uri buildPersonUriWithId(long id) {
            return CONTENT_URI.buildUpon()
                    .appendPath(Long.toString(id))
                    .build();
        }
    }
}
