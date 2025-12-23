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
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.annotation.NonNull;
import android.util.Log;

import com.lineargs.watchnext.BuildConfig;
import com.lineargs.watchnext.R;
import com.lineargs.watchnext.data.DataContract;
import com.lineargs.watchnext.utils.NotificationUtils;
import com.lineargs.watchnext.utils.dbutils.MovieDbUtils;
import com.lineargs.watchnext.utils.dbutils.SerieDbUtils;
import com.lineargs.watchnext.utils.retrofit.movies.MovieApiService;
import com.lineargs.watchnext.utils.retrofit.movies.Movies;
import com.lineargs.watchnext.utils.retrofit.series.Series;
import com.lineargs.watchnext.utils.retrofit.series.SeriesApiService;

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

        try {
            Call<Movies> popularCall = movieApiService.getMovies(PATH_POPULAR, BuildConfig.MOVIE_DATABASE_API_KEY, region);
            Response<Movies> popularResponse = popularCall.execute();
            if (popularResponse.isSuccessful() && popularResponse.body() != null) {
                ContentValues[] values = MovieDbUtils.getPopularContentValues(popularResponse.body().getResults());
                if (values != null && values.length > 0) {
                    getContext().getContentResolver().delete(DataContract.PopularMovieEntry.CONTENT_URI, null, null);
                    getContext().getContentResolver().bulkInsert(DataContract.PopularMovieEntry.CONTENT_URI, values);
                }
            }

            Call<Movies> upcomingCall = movieApiService.getMovies(PATH_UPCOMING, BuildConfig.MOVIE_DATABASE_API_KEY, region);
            Response<Movies> upcomingResponse = upcomingCall.execute();
            if (upcomingResponse.isSuccessful() && upcomingResponse.body() != null) {
                ContentValues[] values = MovieDbUtils.getUpcomingContentValues(upcomingResponse.body().getResults());
                if (values != null && values.length > 0) {
                    getContext().getContentResolver().delete(DataContract.UpcomingMovieEntry.CONTENT_URI, null, null);
                    getContext().getContentResolver().bulkInsert(DataContract.UpcomingMovieEntry.CONTENT_URI, values);
                }
            }

            Call<Movies> topCall = movieApiService.getMovies(PATH_TOP_RATED, BuildConfig.MOVIE_DATABASE_API_KEY, region);
            Response<Movies> topResponse = topCall.execute();
            if (topResponse.isSuccessful() && topResponse.body() != null) {
                ContentValues[] values = MovieDbUtils.getTopContentValues(topResponse.body().getResults());
                if (values != null && values.length > 0) {
                    getContext().getContentResolver().delete(DataContract.TopRatedMovieEntry.CONTENT_URI, null, null);
                    getContext().getContentResolver().bulkInsert(DataContract.TopRatedMovieEntry.CONTENT_URI, values);
                }
            }

            Call<Movies> theaterCall = movieApiService.getMovies(PATH_THEATER, BuildConfig.MOVIE_DATABASE_API_KEY, region);
            Response<Movies> theaterResponse = theaterCall.execute();
            if (theaterResponse.isSuccessful() && theaterResponse.body() != null) {
                ContentValues[] values = MovieDbUtils.getTheaterContentValues(theaterResponse.body().getResults());
                if (values != null && values.length > 0) {
                    getContext().getContentResolver().delete(DataContract.TheaterMovieEntry.CONTENT_URI, null, null);
                    getContext().getContentResolver().bulkInsert(DataContract.TheaterMovieEntry.CONTENT_URI, values);
                }
            }

            final SeriesApiService seriesApiService = retrofit.create(SeriesApiService.class);

            Call<Series> popularSeriesCall = seriesApiService.getSeries(PATH_POPULAR, BuildConfig.MOVIE_DATABASE_API_KEY);
            Response<Series> popularSeriesResponse = popularSeriesCall.execute();
            if (popularSeriesResponse.isSuccessful() && popularSeriesResponse.body() != null) {
                ContentValues[] values = SerieDbUtils.getPopularContentValues(popularSeriesResponse.body().getResults());
                if (values != null && values.length > 0) {
                    getContext().getContentResolver().delete(DataContract.PopularSerieEntry.CONTENT_URI, null, null);
                    getContext().getContentResolver().bulkInsert(DataContract.PopularSerieEntry.CONTENT_URI, values);
                }
            }

            Call<Series> topSeriesCall = seriesApiService.getSeries(PATH_TOP_RATED, BuildConfig.MOVIE_DATABASE_API_KEY);
            Response<Series> topSeriesResponse = topSeriesCall.execute();
            if (topSeriesResponse.isSuccessful() && topSeriesResponse.body() != null) {
                ContentValues[] values = SerieDbUtils.getTopContentValues(topSeriesResponse.body().getResults());
                if (values != null && values.length > 0) {
                    getContext().getContentResolver().delete(DataContract.TopRatedSerieEntry.CONTENT_URI, null, null);
                    getContext().getContentResolver().bulkInsert(DataContract.TopRatedSerieEntry.CONTENT_URI, values);
                }
            }

            Call<Series> onTheAirCall = seriesApiService.getSeries(PATH_ON_THE_AIR, BuildConfig.MOVIE_DATABASE_API_KEY);
            Response<Series> onTheAirResponse = onTheAirCall.execute();
            if (onTheAirResponse.isSuccessful() && onTheAirResponse.body() != null) {
                ContentValues[] values = SerieDbUtils.getOnTheAirContentValues(onTheAirResponse.body().getResults());
                if (values != null && values.length > 0) {
                    getContext().getContentResolver().delete(DataContract.OnTheAirSerieEntry.CONTENT_URI, null, null);
                    getContext().getContentResolver().bulkInsert(DataContract.OnTheAirSerieEntry.CONTENT_URI, values);
                }
            }

        } catch (java.io.IOException e) {
            Log.e(LOG_TAG, "Error performing sync", e);
        }

        NotificationUtils.syncComplete(SYNC_NOTIFICATION_ID, context);
    }}


