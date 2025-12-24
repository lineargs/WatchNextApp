package com.lineargs.watchnext.data.entity;

public interface Movie {
    int getTmdbId();
    String getTitle();
    String getPosterPath();
    String getBackdropPath();
    String getOverview();
    String getVoteAverage();
    String getReleaseDate();
    String getImdbId();
    Integer getRuntime();
    String getProductionCompanies();
    String getProductionCountries();
    String getGenres();
}
