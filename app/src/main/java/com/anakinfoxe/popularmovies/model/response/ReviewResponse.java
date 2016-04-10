package com.anakinfoxe.popularmovies.model.response;

import com.anakinfoxe.popularmovies.model.Review;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by xing on 4/8/16.
 */
public class ReviewResponse {

    @SerializedName("id")
    @Expose
    private long id;

    @SerializedName("page")
    @Expose
    private long page;

    @SerializedName("results")
    @Expose
    private List<Review> reviews;

    @SerializedName("total_pages")
    @Expose
    private long totalPages;

    @SerializedName("total_results")
    @Expose
    private long totalReviews;

    public ReviewResponse() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getPage() {
        return page;
    }

    public void setPage(long page) {
        this.page = page;
    }

    public List<Review> getReviews() {
        return reviews;
    }

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }

    public long getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(long totalPages) {
        this.totalPages = totalPages;
    }

    public long getTotalReviews() {
        return totalReviews;
    }

    public void setTotalReviews(long totalReviews) {
        this.totalReviews = totalReviews;
    }
}
