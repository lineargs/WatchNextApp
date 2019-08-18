package com.lineargs.watchnext.data;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;
import android.util.Log;

import com.lineargs.watchnext.BuildConfig;
import com.lineargs.watchnext.api.search.SearchApiService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SearchViewModel extends AndroidViewModel {

    private WatchNextRepository repository;
    private static final String BASE_URL = "https://api.themoviedb.org/3/";
    private static final String PATH = "multi";
    private static final Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    public SearchViewModel(@NonNull Application application) {
        super(application);
        repository = new WatchNextRepository(application);
    }

    public LiveData<List<Search>> getSearchResults() {
        return repository.getSearchResults();
    }

    public void getSearch(String query, boolean adult) {
        syncSearchResults(query, adult);
    }

    private void syncSearchResults(String query, boolean adult) {
        SearchApiService SEARCH_API_SERVICE = retrofit.create(SearchApiService.class);
        Call<com.lineargs.watchnext.api.search.Search> call = SEARCH_API_SERVICE.multiSearch(PATH, BuildConfig.MOVIE_DATABASE_API_KEY,
                query, adult);
        call.enqueue(new Callback<com.lineargs.watchnext.api.search.Search>() {
            @Override
            public void onResponse(@NonNull Call<com.lineargs.watchnext.api.search.Search> call, @NonNull Response<com.lineargs.watchnext.api.search.Search> response) {
                if (response.isSuccessful() && response.body() != null) {
                    repository.insertSearch(response.body().getResults());
                } else if (response.errorBody() != null) {
                    response.errorBody().close();
                    Log.e("ERROR: ", response.errorBody().toString());
                }
            }

            @Override
            public void onFailure(@NonNull Call<com.lineargs.watchnext.api.search.Search> call, @NonNull Throwable throwable) {

            }
        });
    }
}
