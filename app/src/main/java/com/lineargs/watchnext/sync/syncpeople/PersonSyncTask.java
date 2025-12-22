package com.lineargs.watchnext.sync.syncpeople;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.os.AsyncTask;
import androidx.annotation.NonNull;

import com.lineargs.watchnext.BuildConfig;
import com.lineargs.watchnext.data.DataContract;
import com.lineargs.watchnext.utils.dbutils.PersonDbUtils;
import com.lineargs.watchnext.utils.retrofit.people.PeopleApiService;
import com.lineargs.watchnext.utils.retrofit.people.Person;

import java.lang.ref.WeakReference;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

class PersonSyncTask {

    private static final String BASE_URL = "https://api.themoviedb.org/3/";

    static void syncPerson(final Context context, final String id) {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        PeopleApiService peopleApiService = retrofit.create(PeopleApiService.class);

        Call<Person> call = peopleApiService.getPerson(id, BuildConfig.MOVIE_DATABASE_API_KEY);

        call.enqueue(new Callback<Person>() {
            @Override
            public void onResponse(@NonNull Call<Person> call, @NonNull Response<Person> response) {

                if (response.isSuccessful() && response.body() != null) {
                    ContentValues[] values = PersonDbUtils.getPersonContentValues(response.body());
                    InsertPerson insertPerson = new InsertPerson(context);
                    insertPerson.execute(values);
                } else if (response.errorBody() != null) {
                    response.errorBody().close();
                }
            }

            @Override
            public void onFailure(@NonNull Call<Person> call, @NonNull Throwable t) {
            }
        });
    }

    static class InsertPerson extends AsyncTask<ContentValues, Void, Void> {
        private final WeakReference<Context> weakReference;

        InsertPerson(Context context) {
            this.weakReference = new WeakReference<>(context);
        }

        @Override
        protected Void doInBackground(ContentValues... contentValues) {
            Context context = weakReference.get();
            if (context != null) {
                ContentResolver contentResolver = context.getContentResolver();
                if (contentValues != null && contentValues.length != 0) {
                    contentResolver.bulkInsert(DataContract.Person.CONTENT_URI, contentValues);
                }
            }
            return null;
        }
    }
}
