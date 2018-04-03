package com.dragonnedevelopment.popularmovies.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.util.Log;

import com.dragonnedevelopment.popularmovies.R;

import java.util.Objects;

/**
 * PopularMovies Created by Muir on 27/03/2018.
 *
 * {@link ContentProvider} for Films database
 */
public class FilmProvider extends ContentProvider {

    public static final String LOG_TAG = FilmProvider.class.getSimpleName();

    // URI matcher code for the content URI of all film records
    private static final int CODE_FILMS = 100;

    // URI matcher code for the content URI of a single film
    private static final int CODE_FILM_WITH_ID = 101;

    // return value of ID when the insert fails
    private static final int ERROR_INSERT_ID = -1;

    // Db helper object
    private FilmDbHelper dbHelper;

    private Context context;

    // UriMatcher object to match a content Uri to a corresponding code
    private static final UriMatcher uriMatcher = buildUriMatcher();

    /**
     * associates URIs with their integer match
     * @return uriMatcher
     */
    private static UriMatcher buildUriMatcher() {

        // Initialise a UriMatcher with no matches by passing in NO_MATCH to the constructor
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

        // Matches for the task directory and a single item by ID
        uriMatcher.addURI(FilmContract.CONTENT_AUTHORITY, FilmContract.PATH_FILMS, CODE_FILMS);
        uriMatcher.addURI(FilmContract.CONTENT_AUTHORITY, FilmContract.PATH_FILMS + "/#", CODE_FILM_WITH_ID);

        return uriMatcher;
    }

    @Override
    public boolean onCreate() {
        context = getContext();
        dbHelper = new FilmDbHelper(context);
        return true;
    }

    /**
     * Performs a query for the given URI and loads the cursor with the results fetched from the
     * Db table. The result can have multiple rows or just a single row depending on the given URI
     */
    @Nullable
    @Override
    public Cursor query(Uri uri, @Nullable String[] projection, @Nullable String selection,
                        @Nullable String[] selectionArgs, @Nullable String sortOrder) {

        SQLiteDatabase sqLiteDatabase = dbHelper.getReadableDatabase();

        Cursor cursor; // holds the query result

        // check if the uri matches a specific URI CODE
        int match = uriMatcher.match(uri);

        switch (match) {
            case CODE_FILMS:
                cursor = sqLiteDatabase.query(FilmContract.FilmsEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;

            case CODE_FILM_WITH_ID:
                selection = FilmContract.FilmsEntry._ID + "=?";
                selectionArgs = new String[] {
                        String.valueOf(ContentUris.parseId(uri))
                };
                cursor = sqLiteDatabase.query(FilmContract.FilmsEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;

                default:
                    throw new UnsupportedOperationException(context.getString(R.string.exception_unknown_uri, uri));
        }

        // set notification URI on the cursor so it knows when to update in the event the data in the cursor changes
        cursor.setNotificationUri(Objects.requireNonNull(getContext()).getContentResolver(), uri);
        return cursor;
    }

    // determines the type of URI used to query the table
    @Nullable
    @Override
    public String getType(Uri uri) {
        final int match = uriMatcher.match(uri);

        switch (match) {
            case CODE_FILMS:
                return FilmContract.FilmsEntry.CONTENT_LIST_TYPE;
            case CODE_FILM_WITH_ID:
                return FilmContract.FilmsEntry.CONTENT_ITEM_TYPE;
                default:
                    throw new UnsupportedOperationException(context.getString(R.string.exception_default_message));
        }
    }

    /**
     * inserts records in the Db table
     */
    @Nullable
    @Override
    public Uri insert(Uri uri, @Nullable ContentValues contentValues) {
        Uri returnUri;
        long id;

        SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase();

        // check if the uri matches with a specific URI CODE
        int match = uriMatcher.match(uri);

        switch (match) {
            case CODE_FILMS:
                id = sqLiteDatabase.insert(FilmContract.FilmsEntry.TABLE_NAME, null, contentValues);
                break;

                default:
                    throw new UnsupportedOperationException(context.getString(R.string.exception_unknown_uri, uri));
        }

        //if the ID = -1, the insert has failed
        if (id == ERROR_INSERT_ID) {
            Log.e(LOG_TAG, (context.getString(R.string.exception_insert_failed, uri)));
            return null;
        }

        returnUri = ContentUris.withAppendedId(FilmContract.FilmsEntry.CONTENT_URI, id);

        // notify the resolver if the uri has been changed, and return the newly inserted URI
        Objects.requireNonNull(getContext()).getContentResolver().notifyChange(uri, null);

        // this points to the newly inserted row of data
        return  returnUri;
    }

    // deletes records from the Db table
    @Override
    public int delete(Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {

        SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase();

        int match = uriMatcher.match(uri);

        int rowsDeleted;

        switch (match) {
            case CODE_FILMS:
                // delete all rows which match the selection and selection args
                rowsDeleted = sqLiteDatabase.delete(
                        FilmContract.FilmsEntry.TABLE_NAME,
                        selection,
                        selectionArgs
                );
                break;

            case CODE_FILM_WITH_ID:
                // delete a single row given by the ID in the URI
                selection = FilmContract.FilmsEntry._ID + "=?";
                selectionArgs = new String[] {
                        String.valueOf(ContentUris.parseId(uri))
                };
                rowsDeleted = sqLiteDatabase.delete(
                        FilmContract.FilmsEntry.TABLE_NAME,
                        selection,
                        selectionArgs
                );
                break;

                default:
                    throw new UnsupportedOperationException(context.getString(R.string.exception_unknown_uri, uri));
        }

        // Notify the ContentResolver of a change and return the number of items deleted
        if (rowsDeleted != 0) {
            Objects.requireNonNull(getContext()).getContentResolver().notifyChange(uri, null);
        }

        return rowsDeleted;
    }

    @Override
    public int update(Uri uri, @Nullable ContentValues contentValues, @Nullable String selection, @Nullable String[] selectionArgs) {
        throw new UnsupportedOperationException(context.getString(R.string.exception_default_message));
    }

}
