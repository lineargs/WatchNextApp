package com.lineargs.watchnext.sync.syncadapter;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.SyncRequest;
import android.content.SyncResult;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;

import com.lineargs.watchnext.BuildConfig;
import com.lineargs.watchnext.R;
import com.lineargs.watchnext.data.DataContract;
import com.lineargs.watchnext.utils.NotificationUtils;
import com.lineargs.watchnext.utils.dbutils.DbUtils;
import com.lineargs.watchnext.utils.dbutils.MovieDbUtils;
import com.lineargs.watchnext.utils.dbutils.SerieDbUtils;
import com.lineargs.watchnext.api.movies.MovieApiService;
import com.lineargs.watchnext.api.movies.Movies;
import com.lineargs.watchnext.api.series.Series;
import com.lineargs.watchnext.api.series.SeriesApiService;

import java.lang.ref.WeakReference;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by goranminov on 02/12/2017.
 * <p>
 * Our SyncAdapter to sync the movies and series details once in 24 hours.
 * According to the Movie Db API documentation those endpoints
 * are updated once in a day so we have no interest to call for new data more
 * often than that.
 * See https://developers.themoviedb.org/3/movies/get-popular-movies
 */

public class WatchNextSyncAdapter extends AbstractThreadedSyncAdapter {

    private static final String BASE_URL = "https://api.themoviedb.org/3/";
    private static final String PATH_POPULAR = "popular";
    private static final String PATH_TOP_RATED = "top_rated";
    private static final String PATH_UPCOMING = "upcoming";
    private static final String PATH_THEATER = "now_playing";
    private static final String PATH_ON_THE_AIR = "on_the_air";

    private static final int SYNC_NOTIFICATION_ID = 29101988;

    private static final Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    public Context context;

    private static final int SYNC_INTERVAL = 60 * 60 * 24;
    private static final int SYNC_FLEXTIME = SYNC_INTERVAL / 2;
    private static final String LOG_TAG = WatchNextSyncAdapter.class.getSimpleName();

    WatchNextSyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
        this.context = context;
    }

    private static void configurePeriodicSync(Context context, int syncInterval, int flexTime) {
        Account account = getSyncAccount(context);
        String authority = context.getString(R.string.content_authority);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            SyncRequest request = new SyncRequest.Builder()
                    .syncPeriodic(syncInterval, flexTime)
                    .setSyncAdapter(account, authority)
                    .setExtras(new Bundle()).build();
            ContentResolver.requestSync(request);
        } else {
            ContentResolver.addPeriodicSync(account,
                    authority, new Bundle(), syncInterval);
        }
    }

    public static void syncImmediately(Context context) {
        Bundle bundle = new Bundle();
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true);
        ContentResolver.requestSync(getSyncAccount(context),
                context.getString(R.string.content_authority), bundle);
    }

    private static Account getSyncAccount(Context context) {
        AccountManager accountManager =
                (AccountManager) context.getSystemService(Context.ACCOUNT_SERVICE);
        Account newAccount = new Account(context.getString(R.string.sync_account_name), context.getString(R.string.sync_account_type));

        if (null == accountManager.getPassword(newAccount)) {
            if (!accountManager.addAccountExplicitly(newAccount, "", null)) {
                return null;
            }
            onAccountCreated(newAccount, context);
        }
        return newAccount;
    }

    private static void onAccountCreated(Account newAccount, Context context) {
        WatchNextSyncAdapter.configurePeriodicSync(context, SYNC_INTERVAL, SYNC_FLEXTIME);
        ContentResolver.setSyncAutomatically(newAccount, context.getString(R.string.content_authority), true);
        syncImmediately(context);
    }

    public static void initializeSyncAdapter(Context context) {
        getSyncAccount(context);
    }

    @Override
    public void onPerformSync(Account account, Bundle bundle, String s, ContentProviderClient contentProviderClient, SyncResult syncResult) {

        NotificationUtils.syncProgress(SYNC_NOTIFICATION_ID, context);

        final MovieApiService movieApiService = retrofit.create(MovieApiService.class);

        String language = Locale.getDefault().toString();
        language = language.replace('_', '-');
        Log.w("Language", language);
        String region = language.substring(language.indexOf('-') + 1, language.length());
        Log.w("Region", region);
        Log.w("Locale", Locale.getDefault().getLanguage());
        Call<Movies> popularCall = movieApiService.getMovies(PATH_POPULAR, BuildConfig.MOVIE_DATABASE_API_KEY, region);
        popularCall.enqueue(new Callback<Movies>() {
            @Override
            public void onResponse(@NonNull Call<Movies> call, @NonNull final Response<Movies> response) {

                if (response.isSuccessful() && response.body() != null) {
                    ContentValues[] values = MovieDbUtils.getPopularContentValues(response.body().getResults());
                    InsertPopularMovies insertPopularMovies = new InsertPopularMovies(getContext());
                    insertPopularMovies.execute(values);
                } else if (response.errorBody() != null) {
                    response.errorBody().close();
                }
            }

            @Override
            public void onFailure(@NonNull Call<Movies> call, @NonNull Throwable t) {
            }
        });

        Call<Movies> upcomingCall = movieApiService.getMovies(PATH_UPCOMING, BuildConfig.MOVIE_DATABASE_API_KEY, region);

        upcomingCall.enqueue(new Callback<Movies>() {
            @Override
            public void onResponse(@NonNull Call<Movies> call, @NonNull final Response<Movies> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ContentValues[] values = MovieDbUtils.getUpcomingContentValues(response.body().getResults());
                    InsertUpcomingMovies insertUpcomingMovies = new InsertUpcomingMovies(getContext());
                    insertUpcomingMovies.execute(values);
                } else if (response.errorBody() != null) {
                    response.errorBody().close();
                }
            }

            @Override
            public void onFailure(@NonNull Call<Movies> call, @NonNull Throwable t) {
            }
        });

        Call<Movies> topCall = movieApiService.getMovies(PATH_TOP_RATED, BuildConfig.MOVIE_DATABASE_API_KEY, region);

        topCall.enqueue(new Callback<Movies>() {
            @Override
            public void onResponse(@NonNull Call<Movies> call, @NonNull final Response<Movies> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ContentValues[] values = MovieDbUtils.getTopContentValues(response.body().getResults());
                    InsertTopMovies insertTopMovies = new InsertTopMovies(getContext());
                    insertTopMovies.execute(values);
                } else if (response.errorBody() != null) {
                    response.errorBody().close();
                }
            }

            @Override
            public void onFailure(@NonNull Call<Movies> call, @NonNull Throwable t) {
            }
        });

        Call<Movies> theaterCall = movieApiService.getMovies(PATH_THEATER, BuildConfig.MOVIE_DATABASE_API_KEY, region);

        theaterCall.enqueue(new Callback<Movies>() {
            @Override
            public void onResponse(@NonNull Call<Movies> call, @NonNull final Response<Movies> response) {

                if (response.isSuccessful() && response.body() != null) {
                    ContentValues[] values = MovieDbUtils.getTheaterContentValues(response.body().getResults());
                    InsertTheaterMovies insertTheaterMovies = new InsertTheaterMovies(getContext());
                    insertTheaterMovies.execute(values);
                } else if (response.errorBody() != null) {
                    response.errorBody().close();
                }
            }

            @Override
            public void onFailure(@NonNull Call<Movies> call, @NonNull Throwable t) {
            }
        });

        final SeriesApiService seriesApiService = retrofit.create(SeriesApiService.class);

        Call<Series> popularSeriesCall = seriesApiService.getSeries(PATH_POPULAR, BuildConfig.MOVIE_DATABASE_API_KEY);

        popularSeriesCall.enqueue(new Callback<Series>() {
            @Override
            public void onResponse(@NonNull Call<Series> call, @NonNull Response<Series> response) {
                if (response.isSuccessful() && response.body() != null) {

                    ContentValues[] values = SerieDbUtils.getPopularContentValues(response.body().getResults());
                    InsertPopularSeries insertPopularSeries = new InsertPopularSeries(getContext());
                    insertPopularSeries.execute(values);
                } else if (response.errorBody() != null) {
                    response.errorBody().close();
                }
            }

            @Override
            public void onFailure(@NonNull Call<Series> call, @NonNull Throwable t) {
            }
        });

        Call<Series> topSeriesCall = seriesApiService.getSeries(PATH_TOP_RATED, BuildConfig.MOVIE_DATABASE_API_KEY);

        topSeriesCall.enqueue(new Callback<Series>() {
            @Override
            public void onResponse(@NonNull Call<Series> call, @NonNull Response<Series> response) {
                if (response.isSuccessful() && response.body() != null) {

                    ContentValues[] values = SerieDbUtils.getTopContentValues(response.body().getResults());
                    InsertTopSeries insertTopSeries = new InsertTopSeries(getContext());
                    insertTopSeries.execute(values);
                } else if (response.errorBody() != null) {
                    response.errorBody().close();
                }
            }

            @Override
            public void onFailure(@NonNull Call<Series> call, @NonNull Throwable t) {
            }
        });

        Call<Series> onTheAirCall = seriesApiService.getSeries(PATH_ON_THE_AIR, BuildConfig.MOVIE_DATABASE_API_KEY);

        onTheAirCall.enqueue(new Callback<Series>() {
            @Override
            public void onResponse(@NonNull Call<Series> call, @NonNull Response<Series> response) {
                if (response.isSuccessful() && response.body() != null) {

                    ContentValues[] values = SerieDbUtils.getOnTheAirContentValues(response.body().getResults());
                    InsertOnTheAirSeries insertOnTheAirSeries = new InsertOnTheAirSeries(getContext());
                    insertOnTheAirSeries.execute(values);
                } else if (response.errorBody() != null) {
                    response.errorBody().close();
                }
            }

            @Override
            public void onFailure(@NonNull Call<Series> call, @NonNull Throwable t) {
            }
        });

        NotificationUtils.syncComplete(SYNC_NOTIFICATION_ID, context);
    }

    static class InsertPopularMovies extends AsyncTask<ContentValues, Void, Void> {
        private final WeakReference<Context> weakReference;

        InsertPopularMovies(Context context) {
            this.weakReference = new WeakReference<>(context);
        }

        @Override
        protected Void doInBackground(ContentValues... contentValues) {
            Context context = weakReference.get();
            if (context != null) {
                ContentResolver contentResolver = context.getContentResolver();
                if (contentValues != null && contentValues.length != 0) {
                    contentResolver.delete(DataContract.PopularMovieEntry.CONTENT_URI, null, null);
                    contentResolver.bulkInsert(DataContract.PopularMovieEntry.CONTENT_URI, contentValues);
                    DbUtils.addMovieToFavorites(context, DataContract.PopularMovieEntry.buildMovieUriWithId(384018));
                }
            }
            return null;
        }
    }

    static class InsertTopMovies extends AsyncTask<ContentValues, Void, Void> {
        private final WeakReference<Context> weakReference;

        InsertTopMovies(Context context) {
            this.weakReference = new WeakReference<>(context);
        }

        @Override
        protected Void doInBackground(ContentValues... contentValues) {
            Context context = weakReference.get();
            if (context != null) {
                ContentResolver contentResolver = context.getContentResolver();
                if (contentValues != null && contentValues.length != 0) {
                    contentResolver.delete(DataContract.TopRatedMovieEntry.CONTENT_URI, null, null);
                    contentResolver.bulkInsert(DataContract.TopRatedMovieEntry.CONTENT_URI, contentValues);
                }
            }
            return null;
        }
    }

    static class InsertUpcomingMovies extends AsyncTask<ContentValues, Void, Void> {
        private final WeakReference<Context> weakReference;

        InsertUpcomingMovies(Context context) {
            this.weakReference = new WeakReference<>(context);
        }

        @Override
        protected Void doInBackground(ContentValues... contentValues) {
            Context context = weakReference.get();
            if (context != null) {
                ContentResolver contentResolver = context.getContentResolver();
                if (contentValues != null && contentValues.length != 0) {
                    contentResolver.delete(DataContract.UpcomingMovieEntry.CONTENT_URI, null, null);
                    contentResolver.bulkInsert(DataContract.UpcomingMovieEntry.CONTENT_URI, contentValues);
                }
            }
            return null;
        }
    }

    static class InsertTheaterMovies extends AsyncTask<ContentValues, Void, Void> {
        private final WeakReference<Context> weakReference;

        InsertTheaterMovies(Context context) {
            this.weakReference = new WeakReference<>(context);
        }

        @Override
        protected Void doInBackground(ContentValues... contentValues) {
            Context context = weakReference.get();
            if (context != null) {
                ContentResolver contentResolver = context.getContentResolver();
                if (contentValues != null && contentValues.length != 0) {
                    contentResolver.delete(DataContract.TheaterMovieEntry.CONTENT_URI, null, null);
                    contentResolver.bulkInsert(DataContract.TheaterMovieEntry.CONTENT_URI, contentValues);
                }
            }
            return null;
        }
    }

    static class InsertPopularSeries extends AsyncTask<ContentValues, Void, Void> {
        private final WeakReference<Context> weakReference;

        InsertPopularSeries(Context context) {
            this.weakReference = new WeakReference<>(context);
        }

        @Override
        protected Void doInBackground(ContentValues... contentValues) {
            Context context = weakReference.get();
            if (context != null) {
                ContentResolver contentResolver = context.getContentResolver();
                if (contentValues != null && contentValues.length != 0) {
                    contentResolver.delete(DataContract.PopularSerieEntry.CONTENT_URI, null, null);
                    contentResolver.bulkInsert(DataContract.PopularSerieEntry.CONTENT_URI, contentValues);
                    DbUtils.addTVToFavorites(context, DataContract.PopularSerieEntry.buildSerieUriWithId(1434));
                }
            }

            return null;
        }
    }

    static class InsertTopSeries extends AsyncTask<ContentValues, Void, Void> {
        private final WeakReference<Context> weakReference;

        InsertTopSeries(Context context) {
            this.weakReference = new WeakReference<>(context);
        }

        @Override
        protected Void doInBackground(ContentValues... contentValues) {
            Context context = weakReference.get();
            if (context != null) {
                ContentResolver contentResolver = context.getContentResolver();
                if (contentValues != null && contentValues.length != 0) {
                    contentResolver.delete(DataContract.TopRatedSerieEntry.CONTENT_URI, null, null);
                    contentResolver.bulkInsert(DataContract.TopRatedSerieEntry.CONTENT_URI, contentValues);
                }
            }

            return null;
        }
    }

    static class InsertOnTheAirSeries extends AsyncTask<ContentValues, Void, Void> {
        private final WeakReference<Context> weakReference;

        InsertOnTheAirSeries(Context context) {
            this.weakReference = new WeakReference<>(context);
        }

        @Override
        protected Void doInBackground(ContentValues... contentValues) {
            Context context = weakReference.get();
            if (context != null) {
                ContentResolver contentResolver = context.getContentResolver();
                if (contentValues != null && contentValues.length != 0) {
                    contentResolver.delete(DataContract.OnTheAirSerieEntry.CONTENT_URI, null, null);
                    contentResolver.bulkInsert(DataContract.OnTheAirSerieEntry.CONTENT_URI, contentValues);
                }
            }

            return null;
        }
    }
}
