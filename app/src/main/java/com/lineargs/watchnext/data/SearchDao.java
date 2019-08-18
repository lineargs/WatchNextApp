package com.lineargs.watchnext.data;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

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
