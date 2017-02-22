package com.narvin.android.popularmovies;

/**
 * Movies Contract Class
 */
public class Movies {

    private String mTitle;
    private String mOverview;
    private String mReleaseDate;
    private String mPosterUrl;
    private double mRating;

    // Initial Movies constructor
    public Movies(String title, String overview, String releaseDate, String posterPath, double rating) {
        mOverview = overview;
        mPosterUrl = posterPath;
        mRating = rating;
        mReleaseDate = releaseDate;
        mTitle = title;
    }

    public String getOverview() {
        return mOverview;
    }

    public String getPosterUrl() {
        return mPosterUrl;
    }

    public double getRating() {
        return mRating;
    }

    public String getReleaseDate() {
        return mReleaseDate;
    }

    public String getTitle() {
        return mTitle;
    }
}
