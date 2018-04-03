package com.dragonnedevelopment.popularmovies.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * PopularMovies Created by Muir on 29/03/2018.
 *
 * A {@link FilmReviewResponse} object which contains a list of Film Trailers
 */
public class FilmReviewResponse {

    /*
     * {@link FilmResponse} attributes.
     * Each attribute has a corresponding @SerializedName which is needed for GSON to map the JSON
     * keys from TMDb API with the attributes of {@link FilmReviewResponse} object.
     */

    @SerializedName("results")
    private List<FilmReview> filmReviewList;

    @SerializedName("id")
    private int filmId;

    @SerializedName("page")
    private int page;

    public FilmReviewResponse() {
        // empty constructor
    }

    /**
     * default constructor which constructs a new {@link FilmReviewResponse} object
     * @param filmReviewList a list of film reviews
     * @param filmId the id of the film
     * @param page the page it's on
     */
    private FilmReviewResponse(List<FilmReview> filmReviewList, int filmId, int page) {
        this.filmReviewList = filmReviewList;
        this.filmId = filmId;
        this.page = page;
    }

    // getters and setters

    public List<FilmReview> getFilmReviewList() {
        return filmReviewList;
    }

    public void setFilmReviewList(List<FilmReview> filmReviewList) {
        this.filmReviewList = filmReviewList;
    }

    public int getFilmId() {
        return filmId;
    }

    public void setFilmId(int filmId) {
        this.filmId = filmId;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }


}
