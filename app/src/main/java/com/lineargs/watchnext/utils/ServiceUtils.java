package com.lineargs.watchnext.utils;

import android.app.Activity;
import android.app.SearchManager;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ShareCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.lineargs.watchnext.R;
import com.lineargs.watchnext.utils.dbutils.DbUtils;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;

/**
 * Created by goranminov on 30/11/2017.
 * <p>
 * Utilities helper class
 */

public final class ServiceUtils {

    private static final String IMDB_APP_POST_TITLE_URI = "/";

    private static final String IMDB_APP_TITLE_URI = "imdb:///title/";

    private static final String IMDB_TITLE_URL = "http://imdb.com/title/";

    private static final String YOUTUBE_SEARCH = "http://www.youtube.com/results?search_query=%s";

    private static final String YOUTUBE_PACKAGE = "com.google.android.youtube";

    /**
     * The class is never initialized
     */
    private ServiceUtils() {
    }

    /**
     * Helper Picasso method
     * @param context The view context
     * @param path Image url
     * @return RequestCreator
     */
    @NonNull
    public static RequestCreator loadPicasso(Context context, String path) {
        return Picasso.with(context).load(path);
    }

    /**
     * Helper method for opening youtube videos via Intent
     * @param context The activity context
     * @param youTube Video url
     */
    public static void openYouTube(Context context, String youTube) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(youTube));
        context.startActivity(intent);
    }

    /**
     * Helper method for building movie share Intent
     * @param activity The activity used
     * @param id The movieDb ID
     */
    public static void shareMovie(Activity activity, String id) {
        ShareCompat.IntentBuilder intentBuilder = ShareCompat.IntentBuilder.from(activity)
                .setText(activity.getString(R.string.share_movie, id))
                .setType("text/plain");
        try {
            intentBuilder.startChooser();
        } catch (ActivityNotFoundException e) {
            Toast.makeText(activity, R.string.no_apps_installed, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Helper method for building series share Intent
     * @param activity The activity used
     * @param id The movieDb ID
     */
    public static void shareSerie(Activity activity, String id) {
        ShareCompat.IntentBuilder intentBuilder = ShareCompat.IntentBuilder.from(activity)
                .setText(activity.getString(R.string.share_serie, id))
                .setType("text/plain");
        try {
            intentBuilder.startChooser();
        } catch (ActivityNotFoundException e) {
            Toast.makeText(activity, R.string.no_apps_installed, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Opens implicit Intent
     * @param context The activity context
     * @param link Websites link
     */
    public static void openWeb(Context context, String link) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(link));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT);
        }
        context.startActivity(intent);
    }

    /**
     * Helper method for setting up the IMDB button used in the Movie Details Activity. If there is no button
     * there is nothing to set up, if the ID is null or = 0 then the button is disabled as there is no
     * point to open imdb
     * @param imdbId IMDB ID
     * @param imdbButton The view used for set up
     */
    public static void setUpImdbButton(final String imdbId, final View imdbButton) {
        if (imdbButton != null) {
            if (imdbId != null) {
                if (!imdbId.equals("0")) {
                    imdbButton.setEnabled(true);
                    imdbButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            openImdb(imdbId, v.getContext());
                        }
                    });
                } else {
                    imdbButton.setEnabled(false);
                }
            } else {
                imdbButton.setEnabled(false);
            }
        }
    }

    /**
     * Opens the movie in the IMDB app or web page if there is no app installed on the device
     * @param imdbId IMDB ID
     * @param context Activity context
     */
    private static void openImdb(String imdbId, Context context) {
        if (context == null || imdbId.equals("0")) {
            return;
        }

        Intent intent = new Intent(Intent.ACTION_VIEW,
                Uri.parse(IMDB_APP_TITLE_URI + imdbId
                + IMDB_APP_POST_TITLE_URI));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT);
        } else {
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        }

        if (!Utils.tryStartActivity(context, intent, false)) {
            //TODO Track action
            openWeb(context, IMDB_TITLE_URL + imdbId);
        }


    }

    /**
     * Helper method used for setting up the web search button. If there is no button there is nothing to
     * set up, if the title is empty then there is no point launching implicit Intent to web search
     * @param title Movie / Serie title
     * @param button View for setting up
     */
    public static void setUpGoogleSearchButton(final String title, View button) {
        if (button == null) {
            return;
        } else if (TextUtils.isEmpty(title)) {
            button.setEnabled(false);
            return;
        }

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchGoogle(v.getContext(), title);
            }
        });
    }

    /**
     * Used to search the Web.
     * @param context Activity context
     * @param title Movie / Serie title
     */
    private static void searchGoogle(Context context, String title) {
        Utils.openNewDocument(context, buildGoogleSearchIntent(title));
    }

    /**
     *
     * @param title Movie / Serie title
     * @return implicit Intent for performing web search
     */
    private static Intent buildGoogleSearchIntent(String title) {
        Intent intent = new Intent(Intent.ACTION_WEB_SEARCH);
        intent.putExtra(SearchManager.QUERY, title);
        return intent;
    }

    /**
     * Helper method used for setting up the YouTube search button. If there is no button there is
     * nothing to set up, if title is empty then there is no point performing the search
     * @param title Movie / Serie title
     * @param button View
     */
    public static void setUpYouTubeButton(final String title, View button) {
        if (button == null) {
            return;
        } else if (TextUtils.isEmpty(title)) {
            button.setEnabled(false);
            return;
        }

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchYouTube(v.getContext(), title);
            }
        });
    }

    /**
     * Helper method used for setting up the Comments button. If there is no button there is
     * nothing to set up, if  MovieID does not exist in Review table then there is no point opening
     * empty activity
     * @param context Activity context
     * @param movieId MovieID
     * @param button View
     */
    public static void setUpCommentsButton(Context context, String movieId, View button) {
        //TODO Refactor the statement
        if (button == null) {
            return;
        } else if (!DbUtils.checkForComments(context, movieId)) {
            button.setEnabled(false);
            return;
        }
    }

    /**
     * Helper method used for setting up the Videos button. If there is no button there is
     * nothing to set up, if  MovieID does not exist in Videos table then there is no point opening
     * empty activity
     * @param context Activity context
     * @param movieId MovieID
     * @param button View
     */
    public static void setUpVideosButton(Context context, String movieId, View button) {
        //TODO Refactor the statement
        if (button == null) {
            return;
        } else if (!DbUtils.checkForVideos(context, movieId)) {
            button.setEnabled(false);
            return;
        }
    }

    /**
     * Tries to open and perform YouTube search. If there is an app installed will open the app otherwise will
     * perform search in browser.
     * @param context Activity context
     * @param title Movie / Serie title
     */
    private static void searchYouTube(Context context, String title) {
        Utils.openNewDocument(context, buildYouTubeIntent(context, title));
    }

    /**
     *
     * @param context Activity context
     * @param title Movie / Serie title
     * @return Implicit Intent
     */
    private static Intent buildYouTubeIntent(Context context, String title) {
        PackageManager packageManager = context.getPackageManager();
        boolean hasYouTube;
        try {
            packageManager.getPackageInfo(YOUTUBE_PACKAGE, PackageManager.GET_ACTIVITIES);
            hasYouTube = true;
        } catch (PackageManager.NameNotFoundException notInstalled) {
            hasYouTube = false;
        }

        Intent intent;
        if (hasYouTube) {
            // Directly search the YouTube app
            intent = new Intent(Intent.ACTION_SEARCH);
            intent.setPackage(YOUTUBE_PACKAGE);
            intent.putExtra("query", title);
        } else {
            // Launch a web search
            intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(String.format(YOUTUBE_SEARCH, Uri.encode(title))));
        }
        return intent;
    }

    /**
     * Helper method used for setting up the Google Play Movie button. If there is no view there is nothing
     * to set up, if the title is empty there is no point performing the search
     * @param title Movie / Serie title
     * @param button View
     */
    public static void setUpGooglePlayButton(final String title, View button) {
        if (button == null) {
            return;
        } else if (TextUtils.isEmpty(title)) {
            button.setEnabled(false);
            return;
        }

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchGooglePlay(v.getContext(), title);
            }
        });
    }

    /**
     * Tries to open the Google Play Movie app and performs search if there is an app installed, otherwise
     * opens in browser
     * @param context Activity context
     * @param title Movie / Serie title
     */
    private static void searchGooglePlay(Context context, String title) {
        Utils.openNewDocument(context, buildGooglePlayIntent(title, context));
    }

    /**
     *
     * @param title Movie / Serie title
     * @param context Activity context
     * @return Implicit intent
     */
    private static Intent buildGooglePlayIntent(String title, Context context) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        String playStoreQuery = String.format(context.getString(R.string.google_play_search),
                Uri.encode(title));
        intent.setData(Uri.parse(playStoreQuery));
        return intent;
    }
}
