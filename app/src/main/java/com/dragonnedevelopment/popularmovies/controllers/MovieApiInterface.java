package com.dragonnedevelopment.popularmovies.controllers;

import com.dragonnedevelopment.popularmovies.models.Film;
import com.dragonnedevelopment.popularmovies.models.MovieResponse;

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

public interface MovieApiInterface {

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
    Call<MovieResponse> getFilmList(@Path("preference") String preference, @Query("api_key") String apiKey);
}
