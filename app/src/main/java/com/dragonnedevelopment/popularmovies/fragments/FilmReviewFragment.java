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
import com.dragonnedevelopment.popularmovies.adapters.ReviewAdapter;
import com.dragonnedevelopment.popularmovies.controllers.FilmApiController;
import com.dragonnedevelopment.popularmovies.controllers.FilmApiInterface;
import com.dragonnedevelopment.popularmovies.exceptions.NoConnectivityException;
import com.dragonnedevelopment.popularmovies.models.Film;
import com.dragonnedevelopment.popularmovies.models.FilmReview;
import com.dragonnedevelopment.popularmovies.models.FilmReviewResponse;
import com.dragonnedevelopment.popularmovies.utils.BuildConfig;
import com.dragonnedevelopment.popularmovies.utils.UtilDialog;
import com.dragonnedevelopment.popularmovies.utils.Utils;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * PopularMovies Created by Muir on 29/03/2018.
 */

public class FilmReviewFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private static final String LOG_TAG = FilmReviewFragment.class.getSimpleName();

    private View viewFragment;

    private DetailActivity detailActivity;

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;

    private ReviewAdapter reviewAdapter;

    private ProgressBar progressBar;

    private TextView textViewEmptyList;

    private SwipeRefreshLayout swipeRefreshLayout;

    private final Film film = DetailActivity.currentFilm;

    private FilmReviewResponse filmReviewResponse;

    private List<FilmReview> filmReviewList;

    private boolean isDialogVisible = false;

    private String errorMessage;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        detailActivity = (DetailActivity) getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        // inflate view object
        viewFragment = inflater.inflate(R.layout.fragment_review, container, false);
        progressBar = viewFragment.findViewById(R.id.progress_indicator);
        textViewEmptyList = viewFragment.findViewById(R.id.tv_empty_list);

        // enable layout for SwipeRefresh
        swipeRefreshLayout = viewFragment.findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorSchemeColors(ContextCompat.getColor(detailActivity, R.color.colorAccent));

        initialiseRecyclerViewLayout();

        // couple RecyclerView with the Adapter
        reviewAdapter = new ReviewAdapter(filmReviewResponse, film);
        recyclerView.setAdapter(reviewAdapter);

        loadReviewData();

        return viewFragment;

    }

    /**
     * loads film review data into the adapter and displays it in the Recyclerview layout.
     * Alert Messages are displayed if one of the following occurs:
     * 1. the API is missing
     * 2. There is no connectivity
     * 3. A failure happened while fetching/loading film data from the API
     */
    private void loadReviewData() {
        if (Utils.isEmptyString(BuildConfig.API_KEY)) {
            swipeRefreshLayout.setRefreshing(false);
            isDialogVisible = UtilDialog.showDialog(getString(R.string.alert_api_key_missing), detailActivity);
            errorMessage = new StringBuilder()
                    .append(getString(R.string.alert_api_key_missing))
                    .append(getString(R.string.error_review_fetch_failed))
                    .toString();

            showHideEmptyListMessage(true);
            return;
        }

        try {
            swipeRefreshLayout.setRefreshing(false);
            progressBar.setVisibility(View.VISIBLE);

            FilmApiInterface filmApiInterface = FilmApiController.getClient(detailActivity)
                    .create(FilmApiInterface.class);

            final Call<FilmReviewResponse> reviewResponseCall =
                    filmApiInterface.getFilmReviewList(film.getId(), BuildConfig.API_KEY);

            reviewResponseCall.enqueue(new Callback<FilmReviewResponse>() {
                @Override
                public void onResponse(Call<FilmReviewResponse> call, Response<FilmReviewResponse> response) {
                    int statusCode = response.code();

                    if (response.isSuccessful()) {
                        progressBar.setVisibility(View.INVISIBLE);
                        filmReviewResponse = response.body();
                        reviewAdapter.setReviewData(filmReviewResponse);
                        if (reviewAdapter.getItemCount() > 1) {
                            reviewAdapter.notifyDataSetChanged();
                            filmReviewList = filmReviewResponse.getFilmReviewList();
                            errorMessage = "";
                            showHideEmptyListMessage(false);
                        }else {
                            errorMessage = getString(R.string.alert_no_reviews);
                            showHideEmptyListMessage(true);
                        }
                    }else {
                        progressBar.setVisibility(View.INVISIBLE);
                        isDialogVisible = UtilDialog
                                .showDialog(getString(R.string.error_review_load_failed)
                                + statusCode, detailActivity);
                        errorMessage = getString(R.string.error_review_fetch_failed);
                        showHideEmptyListMessage(true);
                    }
                }

                @Override
                public void onFailure(Call<FilmReviewResponse> call, Throwable t) {

                    progressBar.setVisibility(View.INVISIBLE);
                    isDialogVisible = UtilDialog.showDialog(getString(R.string.error_review_fetch_failed), detailActivity);
                    errorMessage = getString(R.string.error_review_fetch_failed);
                    showHideEmptyListMessage(true);

                }
            });
        } catch (NoConnectivityException nce) {
            progressBar.setVisibility(View.INVISIBLE);
            isDialogVisible = UtilDialog.showDialog(getString(R.string.error_no_connection), detailActivity);
            errorMessage = getString(R.string.error_review_fetch_failed);
            showHideEmptyListMessage(true);
        }
    }

    // shows and hides the empty list message
    private void showHideEmptyListMessage(boolean isEmptyList) {
        // when no list items are displayed
        if (isEmptyList) {
            textViewEmptyList.setText(errorMessage);
            textViewEmptyList.setVisibility(View.VISIBLE);
        }else {
            // when list items are displayed
            textViewEmptyList.setText("");
            textViewEmptyList.setVisibility(View.GONE);
        }
    }

    // Initialises Recyclerview layout in LinearLayout mode to display the list items
    private void initialiseRecyclerViewLayout() {

        layoutManager = new LinearLayoutManager(detailActivity);
        recyclerView = viewFragment.findViewById(R.id.list_reviews);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(reviewAdapter);

    }

    // refreshes the film list when the screen is swiped
    @Override
    public void onRefresh() {

        loadReviewData();

    }

    /**
     * invoked when the activity is destroyed, such as when the screen is rotated.
     * Checks if the dialog is visible. If true, it dismisses the dialog before recreating it.
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (isDialogVisible) {
            UtilDialog.dismissDialog();
            isDialogVisible = false;
        }
    }
}
