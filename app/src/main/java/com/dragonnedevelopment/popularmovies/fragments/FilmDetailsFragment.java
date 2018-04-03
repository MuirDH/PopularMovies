package com.dragonnedevelopment.popularmovies.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.dragonnedevelopment.popularmovies.DetailActivity;
import com.dragonnedevelopment.popularmovies.R;
import com.dragonnedevelopment.popularmovies.models.Film;
import com.dragonnedevelopment.popularmovies.utils.BuildConfig;
import com.dragonnedevelopment.popularmovies.utils.Utils;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * PopularMovies Created by Muir on 29/03/2018.
 */
public class FilmDetailsFragment extends Fragment {

    private static final String LOG_TAG = FilmDetailsFragment.class.getSimpleName();

    private final Film film = DetailActivity.currentFilm;
    private Animation showAnimation;
    private DetailActivity parentActivity;

    private View viewFragmentDetail;

    @BindView(R.id.iv_movie)
    ImageView imageViewFilmPoster;

    @BindView(R.id.tv_movie_title)
    TextView textViewFilmTitle;

    @BindView(R.id.tv_movie_date)
    TextView textViewFilmDate;

    @BindView(R.id.tv_movie_language)
    TextView textViewFilmLanguage;

    @BindView(R.id.tv_movie_genre)
    TextView textViewFilmGenre;

    @BindView(R.id.tv_movie_rating)
    TextView textViewFilmRating;

    @BindView(R.id.tv_movie_overview)
    TextView textViewFilmOverview;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        parentActivity = (DetailActivity) getActivity();

        // Inflate the view object
        viewFragmentDetail = inflater.inflate(R.layout.fragment_detail, container, false);

        initialiseUi();
        setCustomFont();

        displayFilmData();

        return viewFragmentDetail;
    }

    private void displayFilmData() {

        int [] genreIds = film.getGenreIds();

        // display backdrop poster image
        Picasso.with(parentActivity)
                .load(BuildConfig.BACKDROP_POSTER_BASE_URL + film.getBackdropPath())
                .placeholder(R.drawable.backdrop_image_placeholder)
                .error(R.drawable.backdrop_image_placeholder)
                .into(imageViewFilmPoster);

        imageViewFilmPoster.startAnimation(showAnimation);

        // display film title
        textViewFilmTitle.setText(film.getTitle());

        // display release date
        textViewFilmDate.setText(Utils.getFormattedDate(parentActivity, film.getReleaseDate()));

        // display language
        textViewFilmLanguage.setText(Utils.getLanguageName(film.getOriginalLanguage()));

        // display genre
        textViewFilmGenre.setText(film.getGenres(genreIds));

        // display average user rating
        textViewFilmRating.setText(getString(R.string.vote_display));

        // display plot synopsis
        textViewFilmOverview.setText(film.getOverview());

    }

    private void setCustomFont() {

        Utils.setCustomTypeFace(parentActivity, textViewFilmDate);
        Utils.setCustomTypeFace(parentActivity, textViewFilmLanguage);
        Utils.setCustomTypeFace(parentActivity, textViewFilmGenre);
        Utils.setCustomTypeFace(parentActivity, textViewFilmRating);
        Utils.setCustomTypeFace(parentActivity, textViewFilmOverview);

    }

    private void initialiseUi() {

        ButterKnife.bind(this, viewFragmentDetail);
        showAnimation = AnimationUtils.loadAnimation(parentActivity, R.anim.poster_animation);

    }
}
