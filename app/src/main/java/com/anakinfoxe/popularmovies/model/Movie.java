package com.anakinfoxe.popularmovies.model;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Date;

/**
 * Created by xing on 1/17/16.
 */
public class Movie implements Parcelable {

    @SerializedName("adult")
    @Expose
    private boolean adult;

    // TODO: change from only one backdrop to multiple backdrops
    private Uri backdropPath;

    @SerializedName("homepage")
    @Expose
    private String homepage;

    @SerializedName("id")
    @Expose
    private long id;

    @SerializedName("original_title")
    @Expose
    private String originalTitle;

    @SerializedName("overview")
    @Expose
    private String overview;

    @SerializedName("popularity")
    @Expose
    private double popularity;

    private Uri posterPath;

    private Date releaseDate;

    @SerializedName("runtime")
    @Expose
    private long runtime;

    @SerializedName("title")
    @Expose
    private String title;

    @SerializedName("vote_average")
    @Expose
    private double voteAverage;

    @SerializedName("vote_count")
    @Expose
    private long voteCount;

    public Movie() {
    }

    public boolean isAdult() {
        return adult;
    }

    public void setAdult(boolean adult) {
        this.adult = adult;
    }

    public Uri getBackdropPath() {
        return backdropPath;
    }

    public void setBackdropPath(Uri backdropPath) {
        this.backdropPath = backdropPath;
    }

    public String getHomepage() {
        return homepage;
    }

    public void setHomepage(String homepage) {
        this.homepage = homepage;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public double getPopularity() {
        return popularity;
    }

    public void setPopularity(double popularity) {
        this.popularity = popularity;
    }

    public Uri getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(Uri posterPath) {
        this.posterPath = posterPath;
    }

    public Date getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(Date releaseDate) {
        this.releaseDate = releaseDate;
    }

    public long getRuntime() {
        return runtime;
    }

    public void setRuntime(long runtime) {
        this.runtime = runtime;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public double getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(double voteAverage) {
        this.voteAverage = voteAverage;
    }

    public long getVoteCount() {
        return voteCount;
    }

    public void setVoteCount(long voteCount) {
        this.voteCount = voteCount;
    }


    /* following part for Parcelable */

    protected Movie(Parcel in) {
        adult = in.readByte() != 0x00;
        backdropPath = Uri.parse(in.readString());
        homepage = in.readString();
        id = in.readLong();
        originalTitle = in.readString();
        overview = in.readString();
        popularity = in.readDouble();
        posterPath = Uri.parse(in.readString());
        long tmpReleaseDate = in.readLong();
        releaseDate = tmpReleaseDate != -1L ? new Date(tmpReleaseDate) : null;
        runtime = in.readLong();
        title = in.readString();
        voteAverage = in.readDouble();
        voteCount = in.readLong();
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte((byte) (adult ? 0x01 : 0x00));
        dest.writeString(backdropPath.toString());
        dest.writeString(homepage);
        dest.writeLong(id);
        dest.writeString(originalTitle);
        dest.writeString(overview);
        dest.writeDouble(popularity);
        dest.writeString(posterPath.toString());
        dest.writeLong(releaseDate != null ? releaseDate.getTime() : -1L);
        dest.writeLong(runtime);
        dest.writeString(title);
        dest.writeDouble(voteAverage);
        dest.writeLong(voteCount);
    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

}
