package com.dragonnedevelopment.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.dragonnedevelopment.popularmovies.models.Film;
import com.dragonnedevelopment.popularmovies.utils.BuildConfig;
import com.dragonnedevelopment.popularmovies.utils.Utils;
import com.squareup.picasso.Picasso;

/**
 * PopularMovies Created by Muir on 13/03/2018.
 */

public class DetailActivity extends AppCompatActivity {

    private static final String LOG_TAG = DetailActivity.class.getSimpleName();

    private final Context context = this;

    private Film currentFilm;
    private ImageView ivMoviePoster;
    private Animation showAnimation;

    private TextView tvTitle;
    private TextView tvDate;
    private TextView tvLanguage;
    private TextView tvGenre;
    private TextView tvVote;
    private TextView tvOverview; // synopsis

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        // retrieve intent extras
        Intent intent = getIntent();

        if (intent.getParcelableExtra(BuildConfig.INTENT_EXTRA_KEY_MOVIE) != null) {
            currentFilm = intent.getParcelableExtra(BuildConfig.INTENT_EXTRA_KEY_MOVIE);
        }
        if (intent.getExtras() != null) {
            setTitle(intent.getStringExtra(BuildConfig.INTENT_EXTRA_KEY_TITLE));
        }

        initializeUI();

        setCustomFont((ViewGroup) this.findViewById(android.R.id.content));

        displayMovieData();
    }

    private void displayMovieData() {
        int[] genreIds = currentFilm.getGenreIds();

        // display backdrop poster image
        Picasso.with(context)
                .load(BuildConfig.BACKDROP_POSTER_BASE_URL + currentFilm.getBackdropPath())
                .into(ivMoviePoster);
        ivMoviePoster.startAnimation(showAnimation);

        // display title
        tvTitle.setText(currentFilm.getTitle());

        // display release date
        tvDate.setText(currentFilm.getReleaseDate());

        // display language
        tvLanguage.setText(Utils.getLanguageName(currentFilm.getOriginalLanguage()));

        // display genre
        tvGenre.setText(currentFilm.getGenres(genreIds));

        // display user average rating
        tvVote.setText(getString(R.string.vote_display, currentFilm.getVoteAverage()));

        // overview/synopsis
        tvOverview.setText(currentFilm.getOverview());
    }

    private void setCustomFont(ViewGroup viewGroup) {

        for (int i = 0; i < viewGroup.getChildCount(); i++) {
            View view = viewGroup.getChildAt(i);
            if (view instanceof TextView) {
                Utils.setCustomTypeFace(context, view);
            } else if (view instanceof ViewGroup) {
                setCustomFont((ViewGroup) view);
            }
        }

    }

    private void initializeUI() {

        ivMoviePoster = findViewById(R.id.iv_movie);
        tvTitle = findViewById(R.id.tv_movie_title);
        tvDate = findViewById(R.id.tv_movie_date);
        tvLanguage = findViewById(R.id.tv_movie_language);
        tvGenre = findViewById(R.id.tv_movie_genre);
        tvVote = findViewById(R.id.tv_movie_rating);
        tvOverview = findViewById(R.id.tv_movie_overview);
        showAnimation = AnimationUtils.loadAnimation(context, R.anim.poster_animation);

    }
}
