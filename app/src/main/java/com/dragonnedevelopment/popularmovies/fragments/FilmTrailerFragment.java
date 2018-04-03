package com.dragonnedevelopment.popularmovies.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.dragonnedevelopment.popularmovies.DetailActivity;
import com.dragonnedevelopment.popularmovies.R;
import com.dragonnedevelopment.popularmovies.adapters.TrailerAdapter;
import com.dragonnedevelopment.popularmovies.controllers.FilmApiController;
import com.dragonnedevelopment.popularmovies.controllers.FilmApiInterface;
import com.dragonnedevelopment.popularmovies.exceptions.NoConnectivityException;
import com.dragonnedevelopment.popularmovies.models.Film;
import com.dragonnedevelopment.popularmovies.models.FilmTrailer;
import com.dragonnedevelopment.popularmovies.models.FilmTrailerResponse;
import com.dragonnedevelopment.popularmovies.utils.BuildConfig;
import com.dragonnedevelopment.popularmovies.utils.UtilDialog;
import com.dragonnedevelopment.popularmovies.utils.Utils;
import com.google.android.youtube.player.YouTubeIntents;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * PopularMovies Created by Muir on 29/03/2018.
 */

public class FilmTrailerFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener,
        TrailerAdapter.TrailerAdapterOnClickHandler {

    private static final String LOG_TAG = FilmTrailerFragment.class.getSimpleName();

    private DetailActivity detailActivity;
    private View viewFragment;
    private RecyclerView recyclerView;
    private TrailerAdapter trailerAdapter;
    private ProgressBar progressBar;
    private TextView textViewEmptyList;
    private SwipeRefreshLayout swipeRefreshLayout;

    private final Film film = DetailActivity.currentFilm;
    private FilmTrailerResponse filmTrailerResponse;
    private List<FilmTrailer> filmTrailerList;

    private boolean isDialogVisible = false;

    private String errorMessage;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        detailActivity = (DetailActivity) getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        // inflate the view object
        viewFragment = inflater.inflate(R.layout.fragment_trailer, container, false);
        progressBar = viewFragment.findViewById(R.id.progress_indicator);
        textViewEmptyList = viewFragment.findViewById(R.id.tv_empty_list);

        // enable layout for SwipeRefresh
        swipeRefreshLayout = viewFragment.findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorSchemeColors(ContextCompat.getColor(detailActivity, R.color.colorAccent));

        initialiseRecyclerViewLayout();

        // couple recyclerview with the adapter
        trailerAdapter = new TrailerAdapter(filmTrailerResponse, film, this);
        recyclerView.setAdapter(trailerAdapter);

        loadTrailerData();

        return viewFragment;
    }

    /**
     * This method loads film trailer data into the adapter and displays it in the RecyclerView
     * layout.
     * alert messages are displayed in the following cases:
     * 1. The API is found missing
     * 2. There is no connectivity
     * 3. A failure happened while fetching or loading the film data from YouTube's API
     */
    private void loadTrailerData() {

        if (Utils.isEmptyString(BuildConfig.API_KEY)) {

            swipeRefreshLayout.setRefreshing(false);

            isDialogVisible = UtilDialog.showDialog(getString(R.string.alert_api_key_missing), detailActivity);

            errorMessage = new StringBuilder()
                    .append(getString(R.string.alert_api_key_missing))
                    .append(getString(R.string.error_trailer_fetch_failed))
                    .toString();

            showHideEmptyListMessage(true);
            return;
        }

        try {
            swipeRefreshLayout.setRefreshing(false);
            progressBar.setVisibility(View.VISIBLE);

            FilmApiInterface filmApiInterface = FilmApiController.getClient(detailActivity).
                    create(FilmApiInterface.class);

            final Call<FilmTrailerResponse> responseCall =
                    filmApiInterface.getFilmTrailerList(film.getId(), BuildConfig.API_KEY);

            responseCall.enqueue(new Callback<FilmTrailerResponse>() {
                @Override
                public void onResponse(Call<FilmTrailerResponse> call, Response<FilmTrailerResponse> response) {

                    int statusCode = response.code();

                    if (response.isSuccessful()) {
                        progressBar.setVisibility(View.INVISIBLE);
                        filmTrailerResponse = response.body();

                        trailerAdapter.setTrailerData(filmTrailerResponse);

                        if (trailerAdapter.getItemCount() > 0) {

                            trailerAdapter.notifyDataSetChanged();
                            filmTrailerList = filmTrailerResponse.getFilmTrailerList();
                            errorMessage = "";
                            showHideEmptyListMessage(true);
                        } else {
                            errorMessage = getString(R.string.alert_no_trailer);
                            showHideEmptyListMessage(true);
                        }

                    }else {
                        progressBar.setVisibility(View.INVISIBLE);
                        isDialogVisible = UtilDialog.showDialog(getString(R.string.error_trailer_load_failed) + statusCode, detailActivity);
                        errorMessage = getString(R.string.error_trailer_fetch_failed);
                        showHideEmptyListMessage(true);
                    }
                }

                @Override
                public void onFailure(Call<FilmTrailerResponse> call, Throwable t) {

                    progressBar.setVisibility(View.INVISIBLE);
                    isDialogVisible = UtilDialog.showDialog(getString(R.string.error_no_connection), detailActivity);
                    errorMessage = getString(R.string.error_trailer_fetch_failed);
                    showHideEmptyListMessage(true);

                }
            });
        } catch (NoConnectivityException nce) {
            progressBar.setVisibility(View.INVISIBLE);
            isDialogVisible = UtilDialog.showDialog(getString(R.string.error_no_connection), detailActivity);
            errorMessage = getString(R.string.error_trailer_fetch_failed);
            showHideEmptyListMessage(true);
        }
    }

    private void showHideEmptyListMessage(boolean isEmptyList) {

        // when no list items are displayed
        if (isEmptyList) {
            textViewEmptyList.setText(errorMessage);
            textViewEmptyList.setVisibility(View.VISIBLE);

        }else {
            // when the list items are displayed
            textViewEmptyList.setText("");
            textViewEmptyList.setVisibility(View.GONE);

        }
    }

    // initialises the RecyclerView layout in LinearLayout mode, to be used to display the list items
    private void initialiseRecyclerViewLayout() {

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(detailActivity);
        recyclerView = viewFragment.findViewById(R.id.list_trailers);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(trailerAdapter);

    }

    // refreshes the film list when the screen is swiped
    @Override
    public void onRefresh() {

        loadTrailerData();

    }

    /**
     * this method is invoked when a list item is clicked. It plays the video of the clicked
     * trailer item
     * @param filmTrailer the trailer clicked on
     */
    @Override
    public void onClick(FilmTrailer filmTrailer) {

        if (YouTubeIntents.isYouTubeInstalled(detailActivity)) {
            if (YouTubeIntents.canResolvePlayVideoIntentWithOptions(detailActivity)) {
                detailActivity
                        .startActivity(YouTubeIntents
                        .createPlayVideoIntentWithOptions(detailActivity, filmTrailer.getTrailerKey(), false, true));
            }else {
                detailActivity
                        .startActivity(YouTubeIntents
                        .createPlayVideoIntent(detailActivity, filmTrailer.getTrailerKey()));
            }
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (isDialogVisible) {
            UtilDialog.dismissDialog();
            isDialogVisible = false;
        }
    }
}
