package com.lineargs.watchnext.data;

/**
 * Created by goranminov on 23/11/2017.
 * <p>
 * See {@link Query}
 */

public interface ReviewQuery {

    String[] REVIEW_PROJECTION = new String[]{
            DataContract.Review.COLUMN_AUTHOR,
            DataContract.Review.COLUMN_CONTENT,
            DataContract.Review.COLUMN_URL,
    };

    int AUTHOR = 0;
    int CONTENT = 1;
    int URL = 2;
}
