package com.lineargs.watchnext.data;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface ReviewsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Reviews reviews);

    @Query("SELECT * FROM reviews WHERE tmdb_id LIKE :tmdbId")
    LiveData<List<Reviews>> getReviews(int tmdbId);
}
