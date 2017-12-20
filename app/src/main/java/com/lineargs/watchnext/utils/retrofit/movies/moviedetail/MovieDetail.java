package com.lineargs.watchnext.utils.retrofit.movies.moviedetail;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.lineargs.watchnext.utils.retrofit.credits.MovieCredits;
import com.lineargs.watchnext.utils.retrofit.reviews.Reviews;
import com.lineargs.watchnext.utils.retrofit.videos.Videos;

public class MovieDetail {

    @SerializedName("adult")
    @Expose
    private boolean adult;
    @SerializedName("backdrop_path")
    @Expose
    private String backdropPath;
    @SerializedName("belongs_to_collection")
    @Expose
    private Object belongsToCollection;
    @SerializedName("budget")
    @Expose
    private int budget;
    @SerializedName("genres")
    @Expose
    private List<Genre> genres = null;
    @SerializedName("homepage")
    @Expose
    private String homepage;
    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("imdb_id")
    @Expose
    private String imdbId;
    @SerializedName("original_language")
    @Expose
    private String originalLanguage;
    @SerializedName("original_title")
    @Expose
    private String originalTitle;
    @SerializedName("overview")
    @Expose
    private String overview;
    @SerializedName("popularity")
    @Expose
    private double popularity;
    @SerializedName("poster_path")
    @Expose
    private String posterPath;
    @SerializedName("production_companies")
    @Expose
    private List<ProductionCompany> productionCompanies = null;
    @SerializedName("production_countries")
    @Expose
    private List<ProductionCountry> productionCountries = null;
    @SerializedName("release_date")
    @Expose
    private String releaseDate;
    @SerializedName("revenue")
    @Expose
    private int revenue;
    @SerializedName("runtime")
    @Expose
    private int runtime;
    @SerializedName("spoken_languages")
    @Expose
    private List<SpokenLanguage> spokenLanguages = null;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("tagline")
    @Expose
    private String tagline;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("video")
    @Expose
    private boolean video;
    @SerializedName("vote_average")
    @Expose
    private double voteAverage;
    @SerializedName("vote_count")
    @Expose
    private int voteCount;
    @SerializedName("videos")
    @Expose
    private Videos videos;
    @SerializedName("reviews")
    @Expose
    private Reviews reviews;
    @SerializedName("images")
    @Expose
    private Images images;
    @SerializedName("credits")
    @Expose
    private MovieCredits credits;

    public boolean isAdult() {
        return adult;
    }

    public void setAdult(boolean adult) {
        this.adult = adult;
    }

    public MovieDetail withAdult(boolean adult) {
        this.adult = adult;
        return this;
    }

    public String getBackdropPath() {
        return backdropPath;
    }

    public void setBackdropPath(String backdropPath) {
        this.backdropPath = backdropPath;
    }

    public MovieDetail withBackdropPath(String backdropPath) {
        this.backdropPath = backdropPath;
        return this;
    }

    public Object getBelongsToCollection() {
        return belongsToCollection;
    }

    public void setBelongsToCollection(Object belongsToCollection) {
        this.belongsToCollection = belongsToCollection;
    }

    public MovieDetail withBelongsToCollection(Object belongsToCollection) {
        this.belongsToCollection = belongsToCollection;
        return this;
    }

    public int getBudget() {
        return budget;
    }

    public void setBudget(int budget) {
        this.budget = budget;
    }

    public MovieDetail withBudget(int budget) {
        this.budget = budget;
        return this;
    }

    public List<Genre> getGenres() {
        return genres;
    }

    public void setGenres(List<Genre> genres) {
        this.genres = genres;
    }

    public MovieDetail withGenres(List<Genre> genres) {
        this.genres = genres;
        return this;
    }

    public String getHomepage() {
        return homepage;
    }

    public void setHomepage(String homepage) {
        this.homepage = homepage;
    }

    public MovieDetail withHomepage(String homepage) {
        this.homepage = homepage;
        return this;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public MovieDetail withId(int id) {
        this.id = id;
        return this;
    }

    public String getImdbId() {
        return imdbId;
    }

    public void setImdbId(String imdbId) {
        this.imdbId = imdbId;
    }

    public MovieDetail withImdbId(String imdbId) {
        this.imdbId = imdbId;
        return this;
    }

    public String getOriginalLanguage() {
        return originalLanguage;
    }

    public void setOriginalLanguage(String originalLanguage) {
        this.originalLanguage = originalLanguage;
    }

    public MovieDetail withOriginalLanguage(String originalLanguage) {
        this.originalLanguage = originalLanguage;
        return this;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
    }

    public MovieDetail withOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
        return this;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public MovieDetail withOverview(String overview) {
        this.overview = overview;
        return this;
    }

    public double getPopularity() {
        return popularity;
    }

    public void setPopularity(double popularity) {
        this.popularity = popularity;
    }

    public MovieDetail withPopularity(double popularity) {
        this.popularity = popularity;
        return this;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public MovieDetail withPosterPath(String posterPath) {
        this.posterPath = posterPath;
        return this;
    }

    public List<ProductionCompany> getProductionCompanies() {
        return productionCompanies;
    }

    public void setProductionCompanies(List<ProductionCompany> productionCompanies) {
        this.productionCompanies = productionCompanies;
    }

    public MovieDetail withProductionCompanies(List<ProductionCompany> productionCompanies) {
        this.productionCompanies = productionCompanies;
        return this;
    }

    public List<ProductionCountry> getProductionCountries() {
        return productionCountries;
    }

    public void setProductionCountries(List<ProductionCountry> productionCountries) {
        this.productionCountries = productionCountries;
    }

    public MovieDetail withProductionCountries(List<ProductionCountry> productionCountries) {
        this.productionCountries = productionCountries;
        return this;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public MovieDetail withReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
        return this;
    }

    public int getRevenue() {
        return revenue;
    }

    public void setRevenue(int revenue) {
        this.revenue = revenue;
    }

    public MovieDetail withRevenue(int revenue) {
        this.revenue = revenue;
        return this;
    }

    public int getRuntime() {
        return runtime;
    }

    public void setRuntime(int runtime) {
        this.runtime = runtime;
    }

    public MovieDetail withRuntime(int runtime) {
        this.runtime = runtime;
        return this;
    }

    public List<SpokenLanguage> getSpokenLanguages() {
        return spokenLanguages;
    }

    public void setSpokenLanguages(List<SpokenLanguage> spokenLanguages) {
        this.spokenLanguages = spokenLanguages;
    }

    public MovieDetail withSpokenLanguages(List<SpokenLanguage> spokenLanguages) {
        this.spokenLanguages = spokenLanguages;
        return this;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public MovieDetail withStatus(String status) {
        this.status = status;
        return this;
    }

    public String getTagline() {
        return tagline;
    }

    public void setTagline(String tagline) {
        this.tagline = tagline;
    }

    public MovieDetail withTagline(String tagline) {
        this.tagline = tagline;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public MovieDetail withTitle(String title) {
        this.title = title;
        return this;
    }

    public boolean isVideo() {
        return video;
    }

    public void setVideo(boolean video) {
        this.video = video;
    }

    public MovieDetail withVideo(boolean video) {
        this.video = video;
        return this;
    }

    public double getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(double voteAverage) {
        this.voteAverage = voteAverage;
    }

    public MovieDetail withVoteAverage(double voteAverage) {
        this.voteAverage = voteAverage;
        return this;
    }

    public int getVoteCount() {
        return voteCount;
    }

    public void setVoteCount(int voteCount) {
        this.voteCount = voteCount;
    }

    public MovieDetail withVoteCount(int voteCount) {
        this.voteCount = voteCount;
        return this;
    }

    public Videos getVideos() {
        return videos;
    }

    public void setVideos(Videos videos) {
        this.videos = videos;
    }

    public MovieDetail withVideos(Videos videos) {
        this.videos = videos;
        return this;
    }

    public Reviews getReviews() {
        return reviews;
    }

    public void setReviews(Reviews reviews) {
        this.reviews = reviews;
    }

    public MovieDetail withReviews(Reviews reviews) {
        this.reviews = reviews;
        return this;
    }

    public Images getImages() {
        return images;
    }

    public void setImages(Images images) {
        this.images = images;
    }

    public MovieDetail withImages(Images images) {
        this.images = images;
        return this;
    }

    public MovieCredits getCredits() {
        return credits;
    }

    public void setCredits(MovieCredits credits) {
        this.credits = credits;
    }

    public MovieDetail withCredits(MovieCredits credits) {
        this.credits = credits;
        return this;
    }

}