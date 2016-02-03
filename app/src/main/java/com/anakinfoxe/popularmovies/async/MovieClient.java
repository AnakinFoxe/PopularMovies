package com.anakinfoxe.popularmovies.async;

import com.anakinfoxe.popularmovies.model.Movie;
import com.anakinfoxe.popularmovies.model.Response;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by xing on 2/1/16.
 */
public interface MovieClient {

    @GET("{sortingType}")
    Call<Response> getMovieList(
            @Path("sortingType") String sortingType,
            @Query("page") long pageId,
            @Query("api_key") String apiKey
    );

    @GET("{id}")
    Call<Movie> getMovieDetail(
            @Path("id") long id,
            @Query("api_key") String apiKey
    );


}
