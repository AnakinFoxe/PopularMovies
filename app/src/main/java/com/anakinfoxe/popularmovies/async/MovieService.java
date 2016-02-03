package com.anakinfoxe.popularmovies.async;

import com.anakinfoxe.popularmovies.BuildConfig;
import com.anakinfoxe.popularmovies.model.Movie;
import com.anakinfoxe.popularmovies.model.Response;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Call;
import retrofit2.GsonConverterFactory;
import retrofit2.Retrofit;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by xing on 2/1/16.
 */
public class MovieService {

    public interface MovieClient {

        @GET("{sortingType}?api_key=" + API_KEY)
        Call<Response> getMovieList(
                @Path("sortingType") String sortingType,
                @Query("page") long pageId
        );

        @GET("{id}?api_key=" + API_KEY)
        Call<Movie> getMovieDetail(
                @Path("id") long id
        );


    }

    public static final String SORTING_BY_POPULARITY   = "popular";
    public static final String SORTING_BY_RATING       = "top_rated";


    private static final String API_KEY      = BuildConfig.THE_MOVIE_DB_API_KEY;

    private static final String API_BASE_URL = "https://api.themoviedb.org/3/movie/";

    private static final Gson GSON = new GsonBuilder()
                                .registerTypeAdapterFactory(new MovieTypeAdapterFactory())
                                .create();

    private static final Retrofit RETROFIT = new Retrofit.Builder()
                                .baseUrl(API_BASE_URL)
                                .addConverterFactory(GsonConverterFactory.create(GSON))
                                .build();

    private static final MovieClient MOVIE_CLIENT = RETROFIT.create(MovieClient.class);


    public static MovieClient getService() {
        return MOVIE_CLIENT;
    }
}
