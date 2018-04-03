package com.dragonnedevelopment.popularmovies.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * PopularMovies Created by Muir on 29/03/2018.
 *
 * A {@link FilmReview} object which contains details related to a single Film Review
 */
public class FilmReview implements Parcelable{

    /**
     * {@link FilmReview} attributes.
     * Each attribute has a corresponding @SerializedName which is needed for GSON to map the JSON
     * keys from TMDb API with the attributes of a {@link FilmReview} object.
     */

    @SerializedName("id")
    private String reviewId;

    @SerializedName("author")
    private String reviewAuthor;

    @SerializedName("content")
    private String reviewContent;

    @SerializedName("url")
    private String reviewUrl;


    public FilmReview() {

        // Empty constructor
    }

    private FilmReview(Parcel in) {
        reviewId = in.readString();
        reviewAuthor = in.readString();
        reviewContent = in.readString();
        reviewUrl = in.readString();
    }

    // Getters and Setters


    public String getReviewId() {
        return reviewId;
    }

    public void setReviewId(String reviewId) {
        this.reviewId = reviewId;
    }

    public String getReviewAuthor() {
        return reviewAuthor;
    }

    public void setReviewAuthor(String reviewAuthor) {
        this.reviewAuthor = reviewAuthor;
    }

    public String getReviewContent() {
        return reviewContent;
    }

    public void setReviewContent(String reviewContent) {
        this.reviewContent = reviewContent;
    }

    public String getReviewUrl() {
        return reviewUrl;
    }

    public void setReviewUrl(String reviewUrl) {
        this.reviewUrl = reviewUrl;
    }

    public static final Creator<FilmReview> CREATOR = new Creator<FilmReview>() {
        @Override
        public FilmReview createFromParcel(Parcel in) {
            return new FilmReview(in);
        }

        @Override
        public FilmReview[] newArray(int size) {
            return new FilmReview[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {

        parcel.writeString(reviewId);
        parcel.writeString(reviewAuthor);
        parcel.writeString(reviewContent);
        parcel.writeString(reviewUrl);
    }
}
