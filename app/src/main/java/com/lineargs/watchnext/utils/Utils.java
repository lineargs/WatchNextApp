package com.lineargs.watchnext.utils;

import android.content.Context;
import android.content.pm.PackageManager;
import android.net.Uri;

import com.lineargs.watchnext.R;
import com.lineargs.watchnext.data.DataDbHelper;
import com.lineargs.watchnext.utils.retrofit.series.seasondetails.Crew;
import com.lineargs.watchnext.utils.retrofit.series.seasondetails.GuestStar;
import com.lineargs.watchnext.utils.retrofit.series.seriesdetails.Genre;
import com.lineargs.watchnext.utils.retrofit.series.seriesdetails.ProductionCompany;

import java.util.List;

public final class Utils {

    private Utils() {}

    public static String version(Context context) {
        String version;
        try {
            version = context.getPackageManager().getPackageInfo
                    (context.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            version = "unknown";
        }
        return version;
    }

    public static String versionString(Context context) {
        return context.getString(R.string.about_version, version(context),
                DataDbHelper.DATABASE_VERSION);
    }

    public static Uri getBaseUri (Uri uri) {
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
}
