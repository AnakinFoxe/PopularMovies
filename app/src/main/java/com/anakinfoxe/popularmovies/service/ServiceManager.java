package com.anakinfoxe.popularmovies.service;

import com.anakinfoxe.popularmovies.BuildConfig;
import com.anakinfoxe.popularmovies.model.response.MovieResponse;
import com.anakinfoxe.popularmovies.model.response.ReviewResponse;
import com.anakinfoxe.popularmovies.model.response.VideoResponse;
import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by xing on 2/1/16.
 */
public class ServiceManager {

    public static final String SORTING_BY_POPULARITY   = "popular";
    public static final String SORTING_BY_RATING       = "top_rated";


    private static final String API_KEY      = BuildConfig.THE_MOVIE_DB_API_KEY;
    private static final String API_BASE_URL = "https://api.themoviedb.org/3/movie/";


    public interface MovieService {

        @GET("{sortingType}?api_key=" + API_KEY)
        Call<MovieResponse> getMovieList(
                @Path("sortingType") String sortingType,
                @Query("page") long pageId
        );

    }

    public interface DetailService {

        @GET("{id}/videos?api_key=" + API_KEY)
        Call<VideoResponse> getMovieVideos(
                @Path("id") long id
        );

        @GET("{id}/reviews?api_key=" + API_KEY)
        Call<ReviewResponse> getMovieReviews(
                @Path("id") long id
        );

    }


    private static final Gson GSON = new GsonBuilder()
                                .registerTypeAdapterFactory(new MovieTypeAdapterFactory())
                                .create();

    // build with customized converter
    private static final Retrofit MOVIE_RETROFIT = new Retrofit.Builder()
                                .baseUrl(API_BASE_URL)
                                .client(new OkHttpClient.Builder()
                                        .addNetworkInterceptor(new StethoInterceptor())
                                        .build())
                                .addConverterFactory(GsonConverterFactory.create(GSON))
                                .build();

    // build with general converter
    private static final Retrofit DETAIL_RETROFIT = new Retrofit.Builder()
                                .baseUrl(API_BASE_URL)
                                .client(new OkHttpClient.Builder()
                                        .addNetworkInterceptor(new StethoInterceptor())
                                        .build())
                                .addConverterFactory(GsonConverterFactory.create())
                                .build();

    private static final MovieService MOVIE_SERVICE = MOVIE_RETROFIT
                                .create(MovieService.class);

    private static final DetailService DETAIL_SERVICE = DETAIL_RETROFIT
                                .create(DetailService.class);


    public static MovieService getMovieService() {
        return MOVIE_SERVICE;
    }

    public static DetailService getDetailService() { return DETAIL_SERVICE; }
}
