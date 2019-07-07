package com.lineargs.watchnext.data;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;
import android.util.Log;

import com.lineargs.watchnext.BuildConfig;
import com.lineargs.watchnext.api.person.PeopleApiService;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PersonViewModel extends AndroidViewModel {

    private static final String BASE_URL = "https://api.themoviedb.org/3/";
    private static final String IMAGE_MEDIUM_BASE = "http://image.tmdb.org/t/p/w500/";

    private WatchNextRepository repository;
    private MutableLiveData<Person> personDetails;

    public PersonViewModel(@NonNull Application application) {
        super(application);
        repository = new WatchNextRepository(application);
    }

    public MutableLiveData<Person> getPersonDetails(String id) {
        if (personDetails == null) {
            personDetails = new MutableLiveData<>();
            syncPersonDetails(id);
        }
        return personDetails;
    }

    private void syncPersonDetails(String id) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        PeopleApiService peopleApiService = retrofit.create(PeopleApiService.class);

        Call<com.lineargs.watchnext.api.person.Person> call = peopleApiService.getPerson(id, BuildConfig.MOVIE_DATABASE_API_KEY);

        call.enqueue(new Callback<com.lineargs.watchnext.api.person.Person>() {
            @Override
            public void onResponse(@NonNull Call<com.lineargs.watchnext.api.person.Person> call, @NonNull Response<com.lineargs.watchnext.api.person.Person> response) {

                if (response.isSuccessful() && response.body() != null) {
                    Person person = new Person();
                    person.setPersonId(response.body().getId());
                    person.setBiography(response.body().getBiography());
                    person.setHomepage(response.body().getHomepage());
                    person.setName(response.body().getName());
                    person.setPlaceOfBirth(response.body().getPlaceOfBirth());
                    person.setProfilePath(IMAGE_MEDIUM_BASE + response.body().getProfilePath());
                    repository.insertPerson(person);

                } else if (response.errorBody() != null) {
                    response.errorBody().close();
                    try {
                        Log.e("ERROR", response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<com.lineargs.watchnext.api.person.Person> call, @NonNull Throwable t) {
            }
        });
    }

    public void insert(Person person) {
        repository.insertPerson(person);
    }

    public LiveData<Person> getPerson(int personId) {
        return repository.getPerson(personId);
    }
}
