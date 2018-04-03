package com.dragonnedevelopment.popularmovies.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * PopularMovies Created by Muir on 27/03/2018.
 *
 * Database schema for Favourite Films Database
 */
public class FilmContract {

    private FilmContract() {
        // empty constructor
    }

    public static final String CONTENT_AUTHORITY = "com.dragonnedevelopment.popularmovies";

    static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    // path for the Films Directory. This is appended to the base URI for possible URIs
    public static final String PATH_FILMS = "films";

    // Inner class which defines the content of the FILMS table
    public static final class FilmsEntry implements BaseColumns {

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_FILMS).build();

        /**
         * MIME type of the {@link #CONTENT_URI} for a list of items
         */
        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_FILMS;

        /**
         * MIME type of the {@link #CONTENT_URI} for a single item
         */
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_FILMS;

        public static final String TABLE_NAME = "films";

        // Column names
        public static final String COLUMN_FILM_ID = "film_id";
        public static final String COLUMN_FILM_TITLE = "film_title";
        public static final String COLUMN_POSTER_PATH = "poster_path";
        public static final String COLUMN_LAST_UPDATED = "last_updated";
    }
}
