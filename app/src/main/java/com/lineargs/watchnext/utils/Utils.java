package com.lineargs.watchnext.utils;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.util.Log;
import android.widget.Toast;

import com.lineargs.watchnext.R;
import com.lineargs.watchnext.utils.retrofit.series.seasondetails.Crew;
import com.lineargs.watchnext.utils.retrofit.series.seasondetails.GuestStar;
import com.lineargs.watchnext.utils.retrofit.series.seriesdetails.Genre;
import com.lineargs.watchnext.utils.retrofit.series.seriesdetails.ProductionCompany;

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
     *
     * @param context Activity context
     * @return String in following format: Build 1.2.23, used in About Activity
     */
    public static String versionString(Context context) {
        return context.getString(R.string.about_version, versionName(context));
    }

    public static Uri getBaseUri(Uri uri) {
        String stringUri = uri.toString();
        stringUri = stringUri.substring(0, stringUri.lastIndexOf('/'));
        return Uri.parse(stringUri);
    }

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

    public static String buildCompaniesString(List<ProductionCompany> companies) {

        if (companies == null || companies.isEmpty()) {
            return null;
        }

        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < companies.size(); i++) {
            ProductionCompany company = companies.get(i);
            stringBuilder.append(company.getName());
            if (i + 1 < companies.size()) {
                stringBuilder.append(", ");
            }
        }
        return stringBuilder.toString();
    }

    public static String buildGenresString(List<Genre> genres) {

        if (genres == null || genres.isEmpty()) {
            return null;
        }

        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < genres.size(); i++) {
            Genre genre = genres.get(i);
            stringBuilder.append(genre.getName());
            if (i + 1 < genres.size()) {
                stringBuilder.append(", ");
            }
        }
        return stringBuilder.toString();
    }

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

    public static VectorDrawableCompat starImage(Context context) {
        return VectorDrawableCompat.create(context.getResources(), R.drawable.icon_star_white, context.getTheme());
    }

    public static VectorDrawableCompat starBorderImage(Context context) {
        return VectorDrawableCompat.create(context.getResources(), R.drawable.icon_star_border_white, context.getTheme());
    }

    public static boolean openNewDocument(@NonNull Context context, @NonNull Intent intent) {
        // launch as a new document (separate entry in task switcher)
        // or on older versions: clear from task stack when returning to app
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT);
        } else {
            //noinspection deprecation
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        }

        return Utils.tryStartActivity(context, intent, true);
    }
}
