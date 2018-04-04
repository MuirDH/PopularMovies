package com.dragonnedevelopment.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.dragonnedevelopment.popularmovies.adapters.FilmListAdapter;
import com.dragonnedevelopment.popularmovies.controllers.FilmApiController;
import com.dragonnedevelopment.popularmovies.controllers.FilmApiInterface;
import com.dragonnedevelopment.popularmovies.exceptions.NoConnectivityException;
import com.dragonnedevelopment.popularmovies.models.Film;
import com.dragonnedevelopment.popularmovies.models.FilmResponse;
import com.dragonnedevelopment.popularmovies.utils.BuildConfig;
import com.dragonnedevelopment.popularmovies.utils.UtilDialog;
import com.dragonnedevelopment.popularmovies.utils.Utils;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements
        FilmListAdapter.MovieListAdapterOnClickHandler, SwipeRefreshLayout.OnRefreshListener {

    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    private static final String STATE_DIALOG = "state_dialog";
    private static final String STATE_MOVIE = "state_movie";

    private final Context context = this;
    private Toast toast;
    private List<Film> filmList;
    private FilmResponse filmResponse;
    private RecyclerView recyclerView;
    private FilmListAdapter filmListAdapter;
    private ProgressBar progressBar;
    private SwipeRefreshLayout swipeRefreshLayout;


    private Film currentFilm;
    private boolean isDialogVisible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        // Initialise views
        initialiseUI();

        // enable layout for SwipeRefresh
        enableLayoutForSwipeRefresh();

        // Initialise Recyclerview for displaying poster images using grid layout
        initialiseRecyclerViewGridLayout();

        // Set adapter
        setAdapter();

        loadMovieData();
    }

    private void setAdapter() {
        filmListAdapter = new FilmListAdapter(this);
        recyclerView.setAdapter(filmListAdapter);
    }

    private void initialiseRecyclerViewGridLayout() {
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, setGridColumns());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true); // changes in content don't change the child layout size
    }

    private void enableLayoutForSwipeRefresh() {
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorSchemeColors(ContextCompat.getColor(this, R.color.colorAccent));
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(STATE_MOVIE, currentFilm);
        outState.putBoolean(STATE_DIALOG, isDialogVisible);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        if (savedInstanceState != null) {
            currentFilm = savedInstanceState.getParcelable(STATE_MOVIE);
            isDialogVisible = savedInstanceState.getBoolean(STATE_DIALOG);
        }
    }

    private void initialiseUI() {

        recyclerView = findViewById(R.id.recyclerView_movies);
        swipeRefreshLayout = findViewById(R.id.swipe_refresh_layout);
        progressBar = findViewById(R.id.progress_bar);

    }

    /**
     * determines the number of columns in the grid layout
     *
     * @return gridColumns (3 for portrait, 4 for landscape)
     */
    private int setGridColumns() {

        int gridColumns = 0;

        switch (getResources().getConfiguration().orientation) {
            case Configuration.ORIENTATION_PORTRAIT:
                gridColumns = BuildConfig.GRID_COLUMNS_PORTRAIT;
                break;

            case Configuration.ORIENTATION_LANDSCAPE:
                gridColumns = BuildConfig.GRID_COLUMNS_LANDSCAPE;
                break;
        }

        return gridColumns;
    }

    /**
     * loads film/movie data into the adapter and displays the info in the Recyclerview layout
     * Displays alert messages if
     * - The API key is missing
     * - The user device is not connected to the internet
     * - Something went wrong whilst fetching/loading film/movie data from the API
     */
    private void loadMovieData() {

        if (isApiKeyPresent()) return;

        String sortByPreferences = getSortOrderPreference();

        setActivityTitle(sortByPreferences);

        // make retrofit call to tmdb api
        makeRetrofitCallToApi(sortByPreferences);

    }

    private void makeRetrofitCallToApi(String sortByPreferences) {
        try {
            swipeRefreshLayout.setRefreshing(false);
            progressBar.setVisibility(View.VISIBLE);

            FilmApiInterface apiInterface = FilmApiController.getClient(context)
                    .create(FilmApiInterface.class);

            final Call<FilmResponse> responseCall = apiInterface
                    .getFilmList(sortByPreferences, BuildConfig.API_KEY);

            responseCall.enqueue(new Callback<FilmResponse>() {
                @Override
                public void onResponse(Call<FilmResponse> call, Response<FilmResponse> response) {
                    int statusCode = response.code();
                    if (response.isSuccessful()) {
                        setProgressBarInvisible();
                        filmResponse = response.body();
                        filmListAdapter.setMovieData(filmResponse);
                        filmListAdapter.notifyDataSetChanged();
                        filmList = filmResponse.getFilmList();
                    } else {
                        setProgressBarInvisible();
                        isDialogVisible = UtilDialog.showDialog
                                (getString(R.string.error_movie_load_failed)
                                        + statusCode, context);
                    }
                }

                @Override
                public void onFailure(Call<FilmResponse> call, Throwable t) {

                    setProgressBarInvisible();
                    isDialogVisible = UtilDialog.showDialog(getString(R.string.error_movie_fetch_failed),
                            context);
                }
            });
        } catch (NoConnectivityException nce) {
            setProgressBarInvisible();
            isDialogVisible = UtilDialog.showDialog(getString(R.string.error_no_connection), context);
        }
    }

    private void setProgressBarInvisible() {
        progressBar.setVisibility(View.INVISIBLE);
    }

    @NonNull
    private String getSortOrderPreference() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        return sharedPreferences.getString(
                getString(R.string.settings_sort_by_key),
                getString(R.string.settings_sort_by_default)
        );
    }

    private boolean isApiKeyPresent() {
        if (Utils.isEmptyString(BuildConfig.API_KEY)) {
            isDialogVisible = UtilDialog.showDialog(getString(R.string.alert_api_key_missing), context);
            swipeRefreshLayout.setRefreshing(false);
            return true;
        }
        return false;
    }

    @Override
    public void onRefresh() {
        // refresh list when user swipes the device screen
        loadMovieData();

    }

    /**
     * launches DetailActivity screen when a poster is clicked
     *
     * @param film the film poster which was clicked
     */
    @Override
    public void onClick(Film film) {

        currentFilm = film;
        Intent intent = new Intent(MainActivity.this, DetailActivity.class);
        intent.putExtra(BuildConfig.INTENT_EXTRA_KEY_MOVIE, currentFilm);
        intent.putExtra(BuildConfig.INTENT_EXTRA_KEY_TITLE, getTitle());
        startActivity(intent);

    }

    /**
     * sets the activity title depending on the movie list preference selected
     *
     * @param sortByPreferences sort order preference (popular/top rated/now playing/upcoming)
     */
    private void setActivityTitle(String sortByPreferences) {

        if (sortByPreferences.equals(getString(R.string.settings_sort_by_popularity_value))) {
            setTitle(getString(R.string.movies, getString(R.string.settings_sort_by_popularity_label)));
        } else if (sortByPreferences.equals(getString(R.string.settings_sort_by_rating_value))) {
            setTitle(getString(R.string.movies, getString(R.string.settings_sort_by_rating_label)));
        } else if (sortByPreferences.equals(getString(R.string.settings_sort_by_now_playing_value))) {
            setTitle(getString(R.string.movies, getString(R.string.settings_sort_by_now_playing_label)));
        } else if (sortByPreferences.equals(getString(R.string.settings_sort_by_upcoming_value))) {
            setTitle(getString(R.string.movies, getString(R.string.settings_sort_by_upcoming_label)));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu); // inflate settings menu
        getMenuInflater().inflate(R.menu.menu_favourites, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        // if a menu item is clicked, set the activity accordingly
        switch (id) {
            case R.id.menu_settings:
                startActivity(new Intent(this, SettingsActivity.class));
                return true;

            case R.id.menu_show_favorites:
                startActivity(new Intent(this, FavouritesActivity.class));
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (isDialogVisible) {
            UtilDialog.dismissDialog();
            isDialogVisible = false;
        }
    }
}
