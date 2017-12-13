package com.lineargs.watchnext.data;

/**
 * Created by goranminov on 23/11/2017.
 * <p>
 * Public interface to store array of strings and their indexes for the
 * rows of data that we are interested to display
 */

public interface Query {

    /* The columns of data that we are interested in displaying.
     * As we do not use all the columns from the query
     * it is better implementation to take them out from the query.
     * If ya know what I mean
     */
    String[] PROJECTION = new String[]{
            DataContract.PopularMovieEntry._ID,
            DataContract.PopularMovieEntry.COLUMN_MOVIE_ID,
            DataContract.PopularMovieEntry.COLUMN_TITLE,
            DataContract.PopularMovieEntry.COLUMN_ORIGINAL_TITLE,
            DataContract.PopularMovieEntry.COLUMN_RELEASE_DATE,
            DataContract.PopularMovieEntry.COLUMN_OVERVIEW,
            DataContract.PopularMovieEntry.COLUMN_VOTE_AVERAGE,
            DataContract.PopularMovieEntry.COLUMN_POSTER_PATH,
            DataContract.PopularMovieEntry.COLUMN_BACKDROP_PATH,
            DataContract.PopularMovieEntry.COLUMN_ORIGINAL_LANGUAGE
    };

    /*
     * We store the indices of the values in the array of Strings above to more quickly be able
     * to access the data from our query. If the order of the Strings above changes, these
     * indices must be adjusted to match the order of the Strings.
     */
    int _ID = 0;
    int ID = 1;
    int TITLE = 2;
    int ORIGINAL_TITLE = 3;
    int RELEASE_DATE = 4;
    int OVERVIEW = 5;
    int VOTE_AVERAGE = 6;
    int POSTER_PATH = 7;
    int BACKDROP_PATH = 8;
    int ORIGINAL_LANGUAGE = 9;
}
