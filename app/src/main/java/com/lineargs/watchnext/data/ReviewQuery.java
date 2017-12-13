package com.lineargs.watchnext.data;

/**
 * Created by goranminov on 23/11/2017.
 * <p>
 * See {@link Query}
 */

public interface ReviewQuery {

    String[] REVIEW_PROJECTION = new String[]{
//            DataContract.Review._ID,
//            DataContract.Review.COLUMN_MOVIE_ID,
//            DataContract.Review.COLUMN_REVIEW_ID,
            DataContract.Review.COLUMN_AUTHOR,
            DataContract.Review.COLUMN_CONTENT,
            DataContract.Review.COLUMN_URL,
    };

    //    int _ID = 0;
//    int MOVIE_ID = 1;
//    int REVIEW_ID = 2;
    int AUTHOR = 0;
    int CONTENT = 1;
    int URL = 2;
}
