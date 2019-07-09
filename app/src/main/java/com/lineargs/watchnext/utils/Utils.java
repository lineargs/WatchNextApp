package com.lineargs.watchnext.utils;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.util.Log;
import android.widget.Toast;

import com.lineargs.watchnext.R;
import com.lineargs.watchnext.api.credits.Crew;
import com.lineargs.watchnext.api.series.seasondetails.GuestStar;

import java.util.List;

/**
 * Helper Common Utility class
 */
public final class Utils {

    //Prevents initialization
    private Utils() {
    }

    /**
     * Reads the version name from the app build.gradle file
     *
     * @param context Activity context
     * @return version name String
     */
    private static String versionName(Context context) {
        String version;
        try {
            version = context.getPackageManager().getPackageInfo
                    (context.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            version = "unknown";
        }
        return version;
    }

    /**
     * Reads the version code from the app build.gradle file
     *
     * @param context Activity context
     * @return version code number
     */
    private static int versionCode(Context context) {
        int version;
        try {
            version = context.getPackageManager().getPackageInfo
                    (context.getPackageName(), 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            version = 0;
        }
        return version;
    }

    /**
     * @param context Activity context
     * @return String in following format: Build 1.2.23, used in About Activity
     */
    public static String versionString(Context context) {
        return context.getString(R.string.about_version, versionName(context));
    }

    /**
     * Helper method used for building the Guest Stars String.
     *
     * @param guestStars List of type GuestStar
     * @return String in following format: Keanu Reeves, Morgan Freeman, Jennifer Aniston
     */
    public static String buildGuestStarsString(List<GuestStar> guestStars) {

        if (guestStars == null || guestStars.isEmpty()) {
            return null;
        }

        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < guestStars.size(); i++) {
            GuestStar guestStar = guestStars.get(i);
            stringBuilder.append(guestStar.getName());
            if (i + 1 < guestStars.size()) {
                stringBuilder.append(", ");
            }
        }
        return stringBuilder.toString();
    }

    /**
     * Helper method used for building the Directors String
     *
     * @param crews List of type Crew
     * @return String in following format: Director 1, Director 2, Director 3
     */
    public static String buildDirectorsString(List<Crew> crews) {
        if (crews == null || crews.isEmpty()) {
            return null;
        }

        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < crews.size(); i++) {
            Crew crew = crews.get(i);
            if (crew.getJob().equals("Director")) {
                if (stringBuilder.length() > 0) {
                    stringBuilder.append(", ");
                }
                stringBuilder.append(crew.getName());
            }
        }
        return stringBuilder.toString();
    }

    /**
     * Helper method used for building the Writers String
     *
     * @param crews List of type Crew
     * @return String in following format: Writer 1, Writer 2, Writer 3
     */
    public static String buildWritersString(List<Crew> crews) {
        if (crews == null || crews.isEmpty()) {
            return null;
        }

        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < crews.size(); i++) {
            Crew crew = crews.get(i);
            if (crew.getJob().equals("Writer")) {
                if (stringBuilder.length() > 0) {
                    stringBuilder.append(", ");
                }
                stringBuilder.append(crew.getName());
            }
        }
        return stringBuilder.toString();
    }

    /**
     * Helper method checking if we can find the package i.e. if the app where we try to launch the Intent
     * has been installed on the phone.
     *
     * @param context      The application context
     * @param intent       The Intent
     * @param displayError Whether to display the error
     * @return true / false
     */
    public static boolean tryStartActivity(Context context, Intent intent, boolean displayError) {
        boolean handled = false;

        if (intent.resolveActivity(context.getPackageManager()) != null) {
            try {
                context.startActivity(intent);
                handled = true;
            } catch (ActivityNotFoundException | SecurityException e) {
                Log.i("Utils", "Failed to launch intent", e);
            }

        }

        if (displayError && !handled) {
            Toast.makeText(context, R.string.no_app_available, Toast.LENGTH_LONG).show();
        }

        return handled;
    }

    /**
     * Helper method returning the Star Image Drawable
     *
     * @param context The app context
     * @return The drawable
     */
    public static VectorDrawableCompat starImage(Context context) {
        return VectorDrawableCompat.create(context.getResources(), R.drawable.icon_star_white, context.getTheme());
    }

    /**
     * Helper method returning the Star Border Image Drawable
     *
     * @param context The app context
     * @return The drawable
     */
    public static VectorDrawableCompat starBorderImage(Context context) {
        return VectorDrawableCompat.create(context.getResources(), R.drawable.icon_star_border_white, context.getTheme());
    }

    /**
     * Return value of this method is never used
     *
     * @param context The app context
     * @param intent  The Intent
     * @return true / false
     */
    public static boolean openNewDocument(@NonNull Context context, @NonNull Intent intent) {
        // launch as a new document (separate entry in task switcher)
        // or on older versions: clear from task stack when returning to app
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT);
        } else {
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        }

        return Utils.tryStartActivity(context, intent, true);
    }
}
