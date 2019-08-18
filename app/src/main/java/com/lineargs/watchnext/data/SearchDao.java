package com.lineargs.watchnext.data;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface SearchDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Search search);

    @Query("DELETE FROM search")
    void deleteAll();

    @Query("SELECT * FROM search ORDER BY id ASC")
    LiveData<List<Search>> getSearchResults();
}
