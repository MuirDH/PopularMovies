package com.dragonnedevelopment.popularmovies;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.dragonnedevelopment.popularmovies.adapters.FragmentAdapter;
import com.dragonnedevelopment.popularmovies.models.Film;
import com.dragonnedevelopment.popularmovies.utils.BuildConfig;
import com.dragonnedevelopment.popularmovies.utils.Utils;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.dragonnedevelopment.popularmovies.data.FilmContract.FilmsEntry;

/**
 * PopularMovies Created by Muir on 13/03/2018.
 */

public class DetailActivity extends AppCompatActivity  implements LoaderManager.LoaderCallbacks<Cursor>{

    private static final String LOG_TAG = DetailActivity.class.getSimpleName();
    private static final String STATE_TAG_FAVOURITE = "tag_favourite";

    final Context context = this;

    public static Film currentFilm;

    private Toast toast;

    private static final int LOADER_ID = 0;

    @BindView(R.id.sliding_tabs)
    TabLayout tabLayout;

    private Menu menu;
    private MenuItem menuAddFavourite;
    private boolean isFavouriteFilm;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);

        // retrieve intent extras
        Intent intent = getIntent();

        if (intent.getParcelableExtra(BuildConfig.INTENT_EXTRA_KEY_MOVIE) != null) {
            currentFilm = intent.getParcelableExtra(BuildConfig.INTENT_EXTRA_KEY_MOVIE);
        }
        if (intent.getExtras() != null) {
            setTitle(intent.getStringExtra(BuildConfig.INTENT_EXTRA_KEY_TITLE));
        }

        // set up Tab layout and viewpager
        ViewPager viewPager = findViewById(R.id.viewpager);
        viewPager.setAdapter(new FragmentAdapter(getSupportFragmentManager(), DetailActivity.this));
        tabLayout.setupWithViewPager(viewPager);

        // initialise the loader when the activity is launched for the first time
        if (savedInstanceState != null) {
            isFavouriteFilm = savedInstanceState.getBoolean(STATE_TAG_FAVOURITE);
        } else {
            getSupportLoaderManager().initLoader(LOADER_ID, null, this);
        }

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(STATE_TAG_FAVOURITE, isFavouriteFilm);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        if (savedInstanceState != null) {
            isFavouriteFilm = savedInstanceState.getBoolean(STATE_TAG_FAVOURITE);
        }
    }

    /**
     * Inflate menu options
     * @param menu options menu
     * @return true
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add_favourite, menu);
        menuAddFavourite = menu.getItem(0);
        return true;
    }

    /**
     * Handle actions when individual menu item is clicked
     * @param item the item clicked
     * @return true/false
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_add_favourite:
                if (isFavouriteFilm) {
                    Utils.showToastMessage(context, toast, getString(R.string.info_already_faved)).show();
                }else {
                    addToFavourites();
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menuAddFavourite = menu.getItem(0);
        if (isFavouriteFilm) {
            menuAddFavourite.setIcon(R.drawable.ic_star);
        }else {
            menuAddFavourite.setIcon(R.drawable.ic_star_border);
        }
        return super.onPrepareOptionsMenu(menu);
    }


    private void addToFavourites() {
        Uri uri;

        ContentValues values = new ContentValues();
        values.put(FilmsEntry.COLUMN_FILM_ID, currentFilm.getId());
        values.put(FilmsEntry.COLUMN_FILM_TITLE, currentFilm.getTitle());
        values.put(FilmsEntry.COLUMN_POSTER_PATH, currentFilm.getPosterPath());

        try {
            uri = getContentResolver().insert(FilmsEntry.CONTENT_URI, values);
        }catch (IllegalArgumentException iae) {
            uri = null;
            Log.e(LOG_TAG, iae.toString());
        }

        /*
         * If the row id is -1, then there was an error with the insertion. Otherwise, the insertion
         * was successful and we can display a toast with the row ID.
         */
        if (uri == null) {
            Utils.showToastMessage(context, toast, getString(R.string.error_insert)).show();
        }else {
            Utils.showToastMessage(context, toast, getString(R.string.info_insert_successful)).show();
            isFavouriteFilm = true;
            menuAddFavourite.setIcon(R.drawable.ic_star);
        }

    }

    /**
     * instantiates and returns a new AsyncTaskLoader with the given ID
     * @param id of the loader
     * @param args content
     * @return new AsyncTaskLoader
     */
    @NonNull
    @SuppressLint("StaticFieldLeak")
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        final String selectionClause = "((" + FilmsEntry.COLUMN_FILM_ID + " = " + currentFilm.getId() + "))";

        return new AsyncTaskLoader<Cursor>(this) {

            Cursor data = null;

            @Override
            protected void onStartLoading() {
                if (data != null) {

                    // delivers any previously loaded data
                    deliverResult(data);

                }else {
                    // force a new load
                    forceLoad();
                }
            }

            @Override
            public Cursor loadInBackground() {
                try {
                    return getContentResolver().query(FilmsEntry.CONTENT_URI,
                            null,
                            selectionClause,
                            null,
                            FilmsEntry._ID);
                }catch (Exception e) {
                    Utils.showToastMessage(context, toast, getString(R.string.error_favourites_load_failed)).show();
                    Log.e(LOG_TAG, getString(R.string.error_favourites_load_failed));
                    e.printStackTrace();
                    return null;
                }
            }

            public void deliverResult(Cursor data){
                data = this.data;
                super.deliverResult(data);
            }

        };

    }

    /**
     * handles when a previously created loader has finished its load.
     * @param loader the loader that has finished
     * @param cursor the data generated by the loader
     */

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor cursor) {

        if (cursor.moveToFirst()) {
            DatabaseUtils.dumpCursor(cursor);
            isFavouriteFilm = true;

            // redraw menu
            invalidateOptionsMenu();

        } else {
            isFavouriteFilm = false;
        }

    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {

    }
}
