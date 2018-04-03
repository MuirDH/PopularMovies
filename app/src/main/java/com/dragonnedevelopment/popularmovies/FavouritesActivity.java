package com.dragonnedevelopment.popularmovies;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.dragonnedevelopment.popularmovies.adapters.FavouritesAdapter;
import com.dragonnedevelopment.popularmovies.data.FilmContract;
import com.dragonnedevelopment.popularmovies.utils.Utils;

/**
 * PopularMovies Created by Muir on 27/03/2018.
 */

public class FavouritesActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final String LOG_TAG = FavouritesActivity.class.getSimpleName();
    private static final int LOADER_ID = 0;

    final Context context = this;

    private Toast toast;

    private FavouritesAdapter favouritesAdapter;
    private RecyclerView recyclerView;
    private TextView textViewEmptyList;
    private ImageView imageViewEmptyList;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourites);

        recyclerView = findViewById(R.id.list_favourites);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        favouritesAdapter = new FavouritesAdapter(context);
        recyclerView.setAdapter(favouritesAdapter);

        textViewEmptyList = findViewById(R.id.tv_empty_list);
        imageViewEmptyList = findViewById(R.id.iv_empty_list);

        // method which recognises when an item is swiped by the user to be deleted
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {

                int numRowDeleted = 0;

                int idToBeDeleted = (int) viewHolder.itemView.getTag();

                String stringId = Integer.toString(idToBeDeleted);
                Uri uri = FilmContract.FilmsEntry.CONTENT_URI;
                uri = uri.buildUpon().appendPath(stringId).build();

                // delete a single row using the uri
                numRowDeleted = getContentResolver().delete(uri, null, null);
                if (numRowDeleted == 1) {
                    Utils.showToastMessage(context, toast, getString(R.string.info_delete_successful)).show();
                }else {
                    Utils.showToastMessage(context, toast, getString(R.string.error_delete)).show();
                }

                // restart the loader to re-query for all tasks after the deletion
                getSupportLoaderManager().restartLoader(LOADER_ID, null, FavouritesActivity.this);

            }
        }).attachToRecyclerView(recyclerView);

        // initialise the loader
        getSupportLoaderManager().initLoader(LOADER_ID, null, this);
    }

    // instantiates and returns a new AsyncTaskLoader with the given id
    @SuppressLint("StaticFieldLeak")
    @Nullable
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        return new AsyncTaskLoader<Cursor>(this) {

            Cursor data = null;

            @Override
            protected void onStartLoading() {
                if (data != null) {
                    // deliver any previously loaded data
                    deliverResult(data);
                }else {
                    forceLoad();
                }
            }

            @Nullable
            @Override
            public Cursor loadInBackground() {
               try {
                   return getContentResolver().query(FilmContract.FilmsEntry.CONTENT_URI,
                   null,
                   null,
                   null,
                           FilmContract.FilmsEntry._ID);
               }catch (Exception e) {
                   Utils.showToastMessage(context, toast, getString(R.string.error_favourites_load_failed)).show();
                   Log.e(LOG_TAG, getString(R.string.error_favourites_load_failed));
                   e.printStackTrace();
                   return null;
               }
            }

            public void deliverResult(Cursor data) {
                this.data = data;
                super.deliverResult(data);
            }
        };
    }

    // handles when a previously created loader has finished its load
    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        favouritesAdapter.swapCursor(data);
        if (favouritesAdapter.getItemCount() == 0) {
            textViewEmptyList.setText(getString(R.string.alert_no_favourites));
            textViewEmptyList.setVisibility(View.VISIBLE);
            imageViewEmptyList.setVisibility(View.VISIBLE);
        }else {
            textViewEmptyList.setText("");
            textViewEmptyList.setVisibility(View.GONE);
            imageViewEmptyList.setVisibility(View.GONE);
        }
    }

    // handles when a previously created loader is being reset, thus making its data unavailable
    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

        favouritesAdapter.swapCursor(null);
    }

    // called after the activity has been paused or restarted
    @Override
    protected void onResume() {
        super.onResume();
        getSupportLoaderManager().restartLoader(LOADER_ID, null, this);
    }
}
