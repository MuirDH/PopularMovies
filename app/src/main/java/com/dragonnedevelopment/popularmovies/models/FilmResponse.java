package com.dragonnedevelopment.popularmovies.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * PopularMovies Created by Muir on 13/03/2018.
 * an object that contains a list of {@link Film} items
 */

public class FilmResponse {

    /*
     * {@link FilmResponse} attributes.
     * Each has a corresponding @SerializedName that is needed for GSON to map the JSON keys
     * (from TMDb API) with the attributes of the object.
     */

    // Film (Movie) list
    @SerializedName("results")
    private List<Film> filmList;

    // page number
    @SerializedName("page")
    private int page;

    // total number of movies in the list
    @SerializedName("total_results")
    private int totalResults;

    // total number of pages
    @SerializedName("total_pages")
    private int totalPages;

    // empty constructor
    public FilmResponse() {

    }

    /**
     * default constructor. constructs a new {@link FilmResponse} object
     *
     * @param filmList     the list of films
     * @param page         the page number
     * @param totalResults the total number of movies in the list
     * @param totalPages   the total number of pages
     */
    private FilmResponse(List<Film> filmList, int page, int totalResults, int totalPages) {
        this.filmList = filmList;
        this.page = page;
        this.totalResults = totalResults;
        this.totalPages = totalPages;
    }

    /* *************************************
     *      Getters and Setters
     ************************************* */

    // film list
    public List<Film> getFilmList() {
        return filmList;
    }

    public void setFilmList(List<Film> filmList) {
        this.filmList = filmList;
    }

    // page
    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    //total results
    public int getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(int totalResults) {
        this.totalResults = totalResults;
    }

    // total pages
    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }
}
