package com.anakinfoxe.popularmovies.async;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import okhttp3.OkHttpClient;
import retrofit2.GsonConverterFactory;
import retrofit2.Retrofit;

/**
 * Created by xing on 2/1/16.
 */
public class MovieService {

    public static final String API_BASE_URL = "https://api.themoviedb.org/3/movie/";

    private static OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

    private static Gson gson = new GsonBuilder()
                                .registerTypeAdapterFactory(new MovieTypeAdapterFactory())
                                .create();

    private static Retrofit.Builder builder = new Retrofit.Builder()
            .baseUrl(API_BASE_URL);

    public static <T> T createService(Class<T> serviceClass) {
        Retrofit retrofit = builder
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(httpClient.build()).build();

        return retrofit.create(serviceClass);
    }
}
