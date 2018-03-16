package com.dragonnedevelopment.popularmovies.utils;

/**
 * PopularMovies Created by Muir on 13/03/2018.
 * Used to store global constants used throughout the app
 */

public class BuildConfig {

    // TODO: you must add your TMDb API key here for the app to work
    // TODO: before uploading code to GitHub or other public forum, remember to REMOVE the API_KEY string!
    public static final String API_KEY = "";

    // number of columns to be displayed
    public static final int GRID_COLUMNS_PORTRAIT = 3;
    public static final int GRID_COLUMNS_LANDSCAPE = 4;

    // TMDb base URL
    public static final String TMDB_BASE_URL = "http://api.themoviedb.org/3/";

    // Movie Poster Base URL
    public final static String MOVIE_POSTER_BASE_URL = "http://image.tmdb.org/t/p/w185/";

    // Backdrop Poster Base URL
    public final static String BACKDROP_POSTER_BASE_URL = "http://image.tmdb.org/t/p/w500/";

    // Duration in Milliseconds after which connection times out
    public static final int DURATION_CONNECTION_TIMEOUT = 10000;

    // Intent Extra keys
    public static final String INTENT_EXTRA_KEY_MOVIE = "Movie";
    public static final String INTENT_EXTRA_KEY_TITLE = "ActionBar Title";

}
