package com.dragonnedevelopment.popularmovies;

import android.app.Dialog;
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
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.dragonnedevelopment.popularmovies.adapters.MovieListAdapter;
import com.dragonnedevelopment.popularmovies.controllers.MovieApiController;
import com.dragonnedevelopment.popularmovies.controllers.MovieApiInterface;
import com.dragonnedevelopment.popularmovies.exceptions.NoConnectivityException;
import com.dragonnedevelopment.popularmovies.models.Film;
import com.dragonnedevelopment.popularmovies.models.MovieResponse;
import com.dragonnedevelopment.popularmovies.utils.BuildConfig;
import com.dragonnedevelopment.popularmovies.utils.Utils;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements MovieListAdapter.MovieListAdapterOnClickHandler, SwipeRefreshLayout.OnRefreshListener {

    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    private static final String STATE_DIALOG = "state_dialog";
    private static final String STATE_MOVIE = "state_movie";

    private final Context context = this;
    private Toast toast;
    private List<Film> filmList;
    private MovieResponse movieResponse;
    private RecyclerView recyclerView;
    private MovieListAdapter movieListAdapter;
    private ProgressBar progressBar;
    private SwipeRefreshLayout swipeRefreshLayout;
    private Dialog dialog;
    private TextView textViewDialogTitle;
    private TextView textViewDialogCaption;
    private Button buttonDialog;

    private Film currentFilm;
    private boolean isDialogVisible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialise views
        initializeUI();
        setCustomTypeFace();

        // enable layout for SwipeRefresh
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorSchemeColors(ContextCompat.getColor(this, R.color.colorAccent));

        // Initialise Recyclerview for displaying poster images using grid layout
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, setGridColumns());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true); // changes in content don't change the child layout size

        // Set adapter
        movieListAdapter = new MovieListAdapter(this);
        recyclerView.setAdapter(movieListAdapter);

        loadMovieData();
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

    /**
     * loads film/movie data into the adapter and displays the info in the Recyclerview layout
     * Displays alert messages if
     * - The API key is missing
     * - The user device is not connected to the internet
     * - Something went wrong whilst fetching/loading film/movie data from the API
     */
    private void loadMovieData() {

        if (isApiMissing()) return;

        // get the sort order preference before making a call to the API
        String sortByPreferences = getSortOrderPreference();

        setActivityTitle(sortByPreferences);

        // make Retrofit call to TMDb API
        try {

            swipeRefreshLayout.setRefreshing(false);
            progressBar.setVisibility(View.VISIBLE);

            MovieApiInterface movieApiInterface = MovieApiController.getRetrofit(context).create(MovieApiInterface.class);

            final Call<MovieResponse> responseCall = movieApiInterface.getFilmList(sortByPreferences);

            makeRetrofitCall(responseCall);

        } catch (NoConnectivityException noConnectivityException) {

            progressBar.setVisibility(View.INVISIBLE);
            displayDialog(getString(R.string.error_no_connection));

        }


    }

    private void makeRetrofitCall(Call<MovieResponse> responseCall) {
        responseCall.enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                int statusCode = response.code();
                if (response.isSuccessful()) {
                    progressBar.setVisibility(View.INVISIBLE);
                    movieResponse = response.body();
                    movieListAdapter.setMovieData(movieResponse);
                    movieListAdapter.notifyDataSetChanged();
                    filmList = movieResponse.getFilmList();
                } else {
                    progressBar.setVisibility(View.INVISIBLE);
                    displayDialog(getString(R.string.error_movie_load_failed) + statusCode);
                }
            }

            @Override
            public void onFailure(Call<MovieResponse> call, Throwable t) {
                progressBar.setVisibility(View.INVISIBLE);
                displayDialog(getString(R.string.error_movie_fetch_failed));
            }
        });
    }

    @NonNull
    private String getSortOrderPreference() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        return sharedPreferences.getString(
                getString(R.string.settings_sort_by_key),
                getString(R.string.settings_sort_by_default));
    }

    private boolean isApiMissing() {
        if (Utils.isEmptyString(BuildConfig.API_KEY)) {
            displayDialog(getString(R.string.alert_api_key_missing));
            swipeRefreshLayout.setRefreshing(false);
            return true;
        }
        return false;
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
        }

        return super.onOptionsItemSelected(item);
    }

    private void displayDialog(String message) {
        textViewDialogCaption.setText(message);

        // set message visibility to false when user clicks on it
        buttonDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textViewDialogCaption.setText("");
                dialog.dismiss();
                isDialogVisible = false;
            }
        });

        dialog.show();
        isDialogVisible = true;
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

    private void setCustomTypeFace() {
        Utils.setCustomTypeFace(context, textViewDialogCaption);
        Utils.setCustomTypeFace(context, textViewDialogTitle);
        Utils.setCustomTypeFace(context, buttonDialog);
    }

    private void initializeUI() {

        recyclerView = findViewById(R.id.recyclerView_movies);
        swipeRefreshLayout = findViewById(R.id.swipe_refresh_layout);
        progressBar = findViewById(R.id.progress_bar);

        dialog = new Dialog(context);
        dialog.setContentView(R.layout.dialog);
        textViewDialogTitle = dialog.findViewById(R.id.tv_dialog_title);
        textViewDialogCaption = dialog.findViewById(R.id.tv_dialog_caption);
        buttonDialog = dialog.findViewById(R.id.dismiss_dialog_btn);
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
}
