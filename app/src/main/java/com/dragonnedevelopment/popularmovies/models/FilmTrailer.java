package com.dragonnedevelopment.popularmovies.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.dragonnedevelopment.popularmovies.utils.BuildConfig;
import com.google.gson.annotations.SerializedName;

/**
 * PopularMovies Created by Muir on 29/03/2018.
 * <p>
 * A {@link FilmTrailer} object which contains details related to a single film trailer.
 */
public class FilmTrailer implements Parcelable {

    /**
     * {@link FilmTrailer} attributes
     * Each attribute has a corresponding @SerializedName which is needed for GSON to map the JSON
     * keys (from TMBd API) with the attributes of a {@link FilmTrailer} object
     */

    @SerializedName("id")
    private String trailerId;

    @SerializedName("iso_639_1")
    private String trailerIsoCode639;

    @SerializedName("iso_3166_1")
    private String trailerIsoCode3166;

    @SerializedName("key")
    private String trailerKey;

    @SerializedName("name")
    private String trailerName;

    @SerializedName("site")
    private String trailerSite;

    @SerializedName("size")
    private int trailerSize;

    @SerializedName("type")
    private String trailerType;

    public FilmTrailer() {

        // Empty constructor

    }

    /**
     * Default constructor constructs a new {@link FilmTrailer} object. The scope for this constructor
     * is private so that the CREATOR can access it.
     */
    private FilmTrailer(Parcel parcel) {

        trailerId = parcel.readString();
        trailerIsoCode639 = parcel.readString();
        trailerIsoCode3166 = parcel.readString();
        trailerKey = parcel.readString();
        trailerName = parcel.readString();
        trailerSite = parcel.readString();
        trailerSize = parcel.readInt();
        trailerType = parcel.readString();

    }

    // Getters and Setters

    public String getTrailerId() {

        return trailerId;

    }

    public void setTrailerId(String trailerId) {

        this.trailerId = trailerId;

    }

    public String getTrailerIsoCode639() {

        return trailerIsoCode639;

    }

    public void setTrailerIsoCode639(String trailerIsoCode639) {

        this.trailerIsoCode639 = trailerIsoCode639;

    }

    public String getTrailerIsoCode3166() {

        return trailerIsoCode3166;

    }

    public void setTrailerIsoCode3166(String trailerIsoCode3166) {

        this.trailerIsoCode3166 = trailerIsoCode3166;

    }

    public String getTrailerKey() {
        return trailerKey;
    }

    public void setTrailerKey(String trailerKey) {
        this.trailerKey = trailerKey;
    }

    public String getTrailerName() {
        return trailerName;
    }

    public void setTrailerName(String trailerName) {
        this.trailerName = trailerName;
    }

    public String getTrailerSite() {
        return trailerSite;
    }

    public void setTrailerSite(String trailerSite) {
        this.trailerSite = trailerSite;
    }

    public int getTrailerSize() {
        return trailerSize;
    }

    public void setTrailerSize(int trailerSize) {
        this.trailerSize = trailerSize;
    }

    public String getTrailerType() {
        return trailerType;
    }

    public void setTrailerType(String trailerType) {
        this.trailerType = trailerType;
    }

    // Getter method for video URL
    public String getVideoUrl(FilmTrailer filmTrailer) {
        if (filmTrailer.getTrailerSite().equals(BuildConfig.VIDEO_SITE_NAME))
            return String.format(BuildConfig.BASE_VIDEO_URL, filmTrailer.getTrailerKey());
        else return null;

    }

    // getter method for video thumbnail image url
    public String getVideoThumbnailImage(FilmTrailer filmTrailer) {

        if (filmTrailer.getTrailerSite().equals(BuildConfig.VIDEO_SITE_NAME))
            return String.format(BuildConfig.BASE_VIDEO_THUMBNAIL_URL, filmTrailer.getTrailerKey());
        else return null;

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString(trailerId);
        dest.writeString(trailerIsoCode639);
        dest.writeString(trailerIsoCode3166);
        dest.writeString(trailerKey);
        dest.writeString(trailerName);
        dest.writeString(trailerSite);
        dest.writeInt(trailerSize);
        dest.writeString(trailerType);

    }

    public static final Creator<FilmTrailer> CREATOR = new Creator<FilmTrailer>() {
        @Override
        public FilmTrailer createFromParcel(Parcel source) {
            return new FilmTrailer(source);
        }

        @Override
        public FilmTrailer[] newArray(int size) {
            return new FilmTrailer[size];
        }
    };
}
