package com.dragonnedevelopment.popularmovies.models;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * PopularMovies Created by Muir on 13/03/2018.
 * an object that relates to a single Film (Movie). I used Film here instead of Movie, as there is
 * an internal library called Movie (graphics) and I did not want to confuse the two.
 */

public class Film implements Parcelable {

    private static final List<Integer> ARRAY_GENRE_IDS = Arrays.asList(12, 14, 16, 18, 27, 28,
            35, 36, 37, 53, 80, 99, 878, 9648, 10402, 10749, 10751, 10752, 10770);

    private static final String[] ARRAY_GENRES = new String[]{
            "Adventure", "Fantasy", "Animation", "Drama", "Horror", "Action", "Comedy", "History", "Western",
            "Thriller", "Crime", "Documentary", "Science Fiction", "Mystery", "Music", "Romance",
            "Family", "War", "TV Movie"
    };

    /**
     * {@link Film} Attributes
     * Each attribute has a corresponding @SerializedName that is needed for GSON to map the JSON
     * keys (from TMDb API) with the attributes of the {@link Film} object
     */

    // ID
    @SerializedName("id")
    private int id;

    // Title
    @SerializedName("title")
    private String title;

    // Release Date
    @SerializedName("release_date")
    private String releaseDate;

    // Original Language
    @SerializedName("original_language")
    private String originalLanguage;

    // User Vote Average
    @SerializedName("vote_average")
    private Double voteAverage;

    // Overview/Synopsis
    @SerializedName("overview")
    private String overview;

    // poster path
    @SerializedName("poster_path")
    private String posterPath;

    // backdrop path
    @SerializedName("backdrop_path")
    private String backdropPath;

    // genres
    @SerializedName("genre_ids")
    private int[] genreIds;

    //Empty constructor
    public Film() {

    }

    /**
     * Default constructor which constructs a new {@link Film} object
     *
     * @param in the information from the API pertaining to one film object
     */
    private Film(Parcel in) {
        id = in.readInt();
        title = in.readString();
        releaseDate = in.readString();
        originalLanguage = in.readString();
        voteAverage = in.readDouble();
        overview = in.readString();
        posterPath = in.readString();
        backdropPath = in.readString();
        genreIds = in.createIntArray();
    }

    /* **********************************
           getter and setter methods
     ************************************ */

    // film(movie) ID
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    // title
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    // release date
    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    // original language
    public String getOriginalLanguage() {
        return originalLanguage;
    }

    public void setOriginalLanguage(String originalLanguage) {
        this.originalLanguage = originalLanguage;
    }

    // user vote average
    public Double getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(Double voteAverage) {
        this.voteAverage = voteAverage;
    }

    // overview/synopsis
    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    // poster path
    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    // backdrop path
    public String getBackdropPath() {
        return backdropPath;
    }

    public void setBackdropPath(String backdropPath) {
        this.backdropPath = backdropPath;
    }

    // genres
    public int[] getGenreIds() {
        return genreIds;
    }

    public void setGenreIds(int[] genreIds) {
        this.genreIds = genreIds;
    }

    // genres (comma separated)
    public String getGenres(int[] genreIds) {
        List<String> genreList = new ArrayList<>();
        String genres; // list of genres separated by commas
        int index;

        for (int i : genreIds) {
            index = ARRAY_GENRE_IDS.indexOf(i);
            if (index >= 0) genreList.add(ARRAY_GENRES[index]);
        }
        genres = TextUtils.join(", ", genreList);
        return genres;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(title);
        parcel.writeString(releaseDate);
        parcel.writeString(originalLanguage);
        parcel.writeDouble(voteAverage);
        parcel.writeString(overview);
        parcel.writeString(posterPath);
        parcel.writeString(backdropPath);
        parcel.writeIntArray(genreIds);
    }


    public static final Creator<Film> CREATOR = new Creator<Film>() {
        @Override
        public Film createFromParcel(Parcel in) {
            return new Film(in);
        }

        @Override
        public Film[] newArray(int size) {
            return new Film[size];
        }
    };

}
