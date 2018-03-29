package com.dragonnedevelopment.popularmovies.controllers;

import com.dragonnedevelopment.popularmovies.models.Film;
import com.dragonnedevelopment.popularmovies.models.FilmReview;
import com.dragonnedevelopment.popularmovies.models.FilmReviewResponse;
import com.dragonnedevelopment.popularmovies.models.FilmTrailer;
import com.dragonnedevelopment.popularmovies.models.FilmTrailerResponse;
import com.dragonnedevelopment.popularmovies.models.FilmResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * PopularMovies Created by Muir on 13/03/2018.
 *
 * Retrofit Interface for TMDb API. This interface defines the endpoints that includes details of
 * request methods and parameters
 */

public interface FilmApiInterface {

    /**
     * requests details of a specific movie by ID
     *
     * @param id     found in TMDb movie list
     * @param apiKey individual API key
     * @return {@link Film} object
     */
    @GET("movie/{id}")
    Call<Film> getMovieDetails(@Path("id") int id, @Query("api_key") String apiKey);

    /**
     * requests list of popular movies
     *
     * @param preference the order in which to list the movies (popular or top rated)
     * @return list of movies sorted by preference
     */
    @GET("movie/{preference}")
    Call<FilmResponse> getFilmList(@Path("preference") String preference, @Query("api_key") String apiKey);

    /**
     * Requests list of trailers of a specific film by ID
     * @param id the ID found in TMDb film list
     * @param apiKey the individual api key
     * @return {@link FilmTrailer} object
     */
    @GET ("movie/{id}/videos")
    Call <FilmTrailerResponse> getFilmTrailerList(@Path("id") int id, @Query("api_key") String apiKey);

    /**
     * Requests a list of reviews of a specific film by ID
     * @param id the ID found in TMDb film list
     * @param apiKey individual API key
     * @return {@link FilmReview} object
     */
    @GET("movie/{id}/reviews")
    Call<FilmReviewResponse> getFilmReviewList(@Path("id") int id, @Query("api_key") String apiKey);
}
