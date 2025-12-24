package com.lineargs.watchnext.data;

import android.app.Application;
import androidx.lifecycle.LiveData;

import com.lineargs.watchnext.data.dao.DetailsDao;
import com.lineargs.watchnext.data.entity.Person;

public class PersonRepository {

    private final DetailsDao detailsDao;

    public PersonRepository(Application application) {
        WatchNextDatabase database = WatchNextDatabase.getDatabase(application);
        detailsDao = database.detailsDao();
    }

    public LiveData<Person> getPerson(int personId) {
        return detailsDao.getPersonLiveData(personId);
    }
}
