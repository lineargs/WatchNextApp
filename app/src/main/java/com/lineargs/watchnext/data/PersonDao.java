package com.lineargs.watchnext.data;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

@Dao
public interface PersonDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Person person);

    @Query("SELECT * FROM person WHERE person_id LIKE :personId")
    LiveData<Person> getPerson(int personId);
}
