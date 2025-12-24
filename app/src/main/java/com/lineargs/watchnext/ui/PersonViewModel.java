package com.lineargs.watchnext.ui;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.lineargs.watchnext.data.PersonRepository;
import com.lineargs.watchnext.data.entity.Person;

public class PersonViewModel extends AndroidViewModel {

    private final PersonRepository repository;
    private final MutableLiveData<Integer> personId = new MutableLiveData<>();
    private final LiveData<Person> person;

    public PersonViewModel(@NonNull Application application) {
        super(application);
        repository = new PersonRepository(application);
        person = Transformations.switchMap(personId, repository::getPerson);
    }

    public void setPersonId(int id) {
        personId.setValue(id);
    }

    public LiveData<Person> getPerson() {
        return person;
    }
}
