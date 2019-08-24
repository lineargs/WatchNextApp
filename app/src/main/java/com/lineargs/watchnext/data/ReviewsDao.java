package com.lineargs.watchnext.data;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface ReviewsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Reviews reviews);

    @Query("SELECT * FROM reviews WHERE tmdb_id LIKE :tmdbId")
    LiveData<List<Reviews>> getReviews(int tmdbId);
}
