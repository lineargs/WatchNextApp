package com.lineargs.watchnext.data.dao;

import android.database.Cursor;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import com.lineargs.watchnext.data.entity.Credits;

@Dao
public interface CreditsDao {

    @Query("SELECT * FROM credits WHERE type = 0")
    Cursor getAllCast();

    @Query("SELECT * FROM credits WHERE type = 0 AND movie_id = :movieId")
    Cursor getCastForMovie(int movieId);

    @Query("SELECT * FROM credits WHERE type = 1")
    Cursor getAllCrew();

    @Query("SELECT * FROM credits WHERE type = 1 AND movie_id = :movieId")
    Cursor getCrewForMovie(int movieId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertCredit(Credits credit);
}
