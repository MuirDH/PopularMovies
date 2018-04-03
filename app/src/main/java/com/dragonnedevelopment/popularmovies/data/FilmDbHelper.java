package com.dragonnedevelopment.popularmovies.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.dragonnedevelopment.popularmovies.data.FilmContract.FilmsEntry;

/**
 * PopularMovies Created by Muir on 29/03/2018.
 * <p>
 * Database helper for the Films Db Table. Manages database creation and version management.
 */

public class FilmDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "filmsDb.db";

    private static final int VERSION = 1;

    // default constructor
    FilmDbHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    /**
     * called when the database is created for the first time
     *
     * @param sqLiteDatabase the database created
     */
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        // create films table
        final String SQL_CREATE_TABLE = "CREATE TABLE " +
                FilmsEntry.TABLE_NAME + " (" +
                FilmsEntry._ID + " INTEGER" + " PRIMARY KEY" + ", " +
                FilmsEntry.COLUMN_FILM_ID + " INTEGER" + " NOT NULL" + ", " +
                FilmsEntry.COLUMN_POSTER_PATH + " TEXT" + " NOT NULL" + ", " +
                FilmsEntry.COLUMN_FILM_TITLE + " TEXT" + " NOT NULL" + ", " +
                FilmsEntry.COLUMN_LAST_UPDATED + " TIMESTAMP" + " NOT NULL" + " DEFAULT CURRENT_TIMESTAMP);";

        sqLiteDatabase.execSQL(SQL_CREATE_TABLE);
    }

    /**
     * discards the old table of data and calls oncreate to recreate a new one. This only occurs
     * when the version number is incremented
     *
     * @param sqLiteDatabase films database
     * @param oldVersion     old database version
     * @param newVersion     new database version
     */
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {

        final String SQL_COMMAND_DROP_TABLE = "DROP TABLE IF EXISTS ";

        sqLiteDatabase.execSQL(SQL_COMMAND_DROP_TABLE + FilmsEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
