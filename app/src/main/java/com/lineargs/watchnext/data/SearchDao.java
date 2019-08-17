package com.lineargs.watchnext.data;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface SearchDao {

    @Query("SELECT * FROM search ORDER BY id ASC")
    LiveData<List<Search>> getSearchResults();
}
