package com.lineargs.watchnext.data;

/**
 * Created by goranminov on 18/11/2017.
 * <p>
 * See {@link Query}
 */

public interface SearchQuery {

    String[] SEARCH_PROJECTION = new String[]{
            DataContract.Search._ID,
            DataContract.Search.COLUMN_MOVIE_ID,
            DataContract.Search.COLUMN_TITLE,
            DataContract.Search.COLUMN_POSTER_PATH
    };

    int _ID = 0;
    int ID = 1;
    int TITLE = 2;
    int POSTER_PATH = 3;
}
