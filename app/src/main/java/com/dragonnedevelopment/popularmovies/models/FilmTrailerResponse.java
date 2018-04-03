package com.dragonnedevelopment.popularmovies.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * PopularMovies Created by Muir on 29/03/2018.
 * <p>
 * A {@link FilmTrailerResponse} object which contains a list of Film Trailers
 */
public class FilmTrailerResponse {

    /*
     * {@link FilmResponse} Attributes
     * Each attribute has a corresponding @SerializedName which is needed for GSON to map the JSON
     * keys (from TMDb API) with the attributes of a {@link FilmTrailerResponse} object.
     */

    // Film Trailer List
    @SerializedName("results")
    private List<FilmTrailer> filmTrailerList;

    // Film Id
    @SerializedName("id")
    private int filmId;

    public FilmTrailerResponse() {

        // Empty constructor

    }

    /**
     * Default constructor which constructs a new {@link FilmTrailerResponse} object
     *
     * @param filmTrailerList list of film trailers
     * @param filmId          id of film
     */
    private FilmTrailerResponse(List<FilmTrailer> filmTrailerList, int filmId) {

        this.filmTrailerList = filmTrailerList;
        this.filmId = filmId;

    }

    // Getter and setter methods

    public List<FilmTrailer> getFilmTrailerList() {

        return filmTrailerList;

    }

    public void setFilmTrailerList(List<FilmTrailer> filmTrailerList) {

        this.filmTrailerList = filmTrailerList;

    }

    public int getFilmId() {

        return filmId;

    }

    public void setFilmId(int filmId) {

        this.filmId = filmId;

    }
}
