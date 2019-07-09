package com.lineargs.watchnext.utils;

import com.lineargs.watchnext.api.movies.Genre;
import com.lineargs.watchnext.api.movies.ProductionCompany;
import com.lineargs.watchnext.api.movies.ProductionCountry;

import java.util.List;

public class RoomUtils {

    //Never initialize
    private RoomUtils(){}

    /**
     * Helper method used for building Companies String
     *
     * @param companies List of type ProductionCompany
     * @return String in following format: Google, Google, Google
     */
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

    /**
     * Helper method used for building Countries String
     *
     * @param countries List of type ProductionCountry
     * @return String in following format: Macedonia, England, United States
     */
    public static String buildCountriesString(List<ProductionCountry> countries) {

        if (countries == null || countries.isEmpty()) {
            return null;
        }

        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < countries.size(); i++) {
            ProductionCountry country = countries.get(i);
            stringBuilder.append(country.getName());
            if (i + 1 < countries.size()) {
                stringBuilder.append(", ");
            }
        }
        return stringBuilder.toString();
    }

    /**
     * Helper method used for building Genres String
     *
     * @param genres List of type Genre
     * @return String in following format: Comedy, Horror, Fantasy
     */
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
