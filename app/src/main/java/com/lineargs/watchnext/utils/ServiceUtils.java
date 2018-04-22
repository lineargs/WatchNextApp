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

    @NonNull
    public static RequestCreator loadPicasso(Context context, String path) {
        return Picasso.with(context).load(path);
    }

    public static void openYouTube(Context context, String youTube) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(youTube));
        context.startActivity(intent);
    }

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

    public static void openWeb(Context context, String link) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(link));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT);
        }
        context.startActivity(intent);
    }

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

    private static void searchGoogle(Context context, String title) {
        Utils.openNewDocument(context, buildGoogleSearchIntent(title));
    }

    private static Intent buildGoogleSearchIntent(String title) {
        Intent intent = new Intent(Intent.ACTION_WEB_SEARCH);
        intent.putExtra(SearchManager.QUERY, title);
        return intent;
    }

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

    private static void searchYouTube(Context context, String title) {
        Utils.openNewDocument(context, buildYouTubeIntent(context, title));
    }

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

    private static void searchGooglePlay(Context context, String title) {
        Utils.openNewDocument(context, buildGooglePlayIntent(title, context));
    }

    private static Intent buildGooglePlayIntent(String title, Context context) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        String playStoreQuery = String.format(context.getString(R.string.google_play_search),
                Uri.encode(title));
        intent.setData(Uri.parse(playStoreQuery));
        return intent;
    }
}
