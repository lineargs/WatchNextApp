package com.lineargs.watchnext.workers;

import android.content.ContentValues;
import android.content.Context;
import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.lineargs.watchnext.BuildConfig;
import com.lineargs.watchnext.data.DataContract;
import com.lineargs.watchnext.utils.dbutils.PersonDbUtils;
import com.lineargs.watchnext.utils.retrofit.people.PeopleApiService;
import com.lineargs.watchnext.utils.retrofit.people.Person;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PersonWorker extends Worker {

    public static final String ARG_ID = "id";

    private static final String BASE_URL = "https://api.themoviedb.org/3/";

    private static final Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    public PersonWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        String id = getInputData().getString(ARG_ID);

        if (id == null) {
            return Result.failure();
        }

        PeopleApiService service = retrofit.create(PeopleApiService.class);
        Call<Person> call = service.getPerson(id, BuildConfig.MOVIE_DATABASE_API_KEY);

        try {
            Response<Person> response = call.execute();
            if (response.isSuccessful() && response.body() != null) {
                ContentValues[] values = PersonDbUtils.getPersonContentValues(response.body());
                if (values.length > 0) {
                    getApplicationContext().getContentResolver().bulkInsert(DataContract.Person.CONTENT_URI, values);
                }
            }
            return Result.success();
        } catch (IOException e) {
            e.printStackTrace();
            return Result.retry();
        }
    }
}
