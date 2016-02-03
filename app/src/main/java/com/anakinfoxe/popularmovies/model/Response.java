package com.anakinfoxe.popularmovies.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by xing on 2/2/16.
 */
public class Response {

    @SerializedName("page")
    @Expose
    private long page;

    @SerializedName("results")
    @Expose
    private List<Movie> movies;

    @SerializedName("total_results")
    @Expose
    private long totalMovies;

    @SerializedName("total_pages")
    @Expose
    private long totalPages;

    public Response() {
    }

    public long getPage() {
        return page;
    }

    public void setPage(long page) {
        this.page = page;
    }

    public List<Movie> getMovies() {
        return movies;
    }

    public void setMovies(List<Movie> movies) {
        this.movies = movies;
    }

    public long getTotalMovies() {
        return totalMovies;
    }

    public void setTotalMovies(long totalMovies) {
        this.totalMovies = totalMovies;
    }

    public long getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(long totalPages) {
        this.totalPages = totalPages;
    }
}
