package com.anakinfoxe.popularmovies.service;

import android.net.Uri;
import android.util.Log;

import com.anakinfoxe.popularmovies.model.Movie;
import com.anakinfoxe.popularmovies.util.Helper;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * Created by xing on 2/1/16.
 */
public class MovieTypeAdapterFactory extends CustomizedTypeAdapterFactory<Movie> {

    private static final String LOG_TAG = MovieTypeAdapterFactory.class.getSimpleName();

    // JSON property name
    private static final String NAME_BACKDROP_PATH     = "backdrop_path";
    private static final String NAME_POSTER_PATH       = "poster_path";
    private static final String NAME_RELEASE_DATE      = "release_date";

    private static final String POSTER_BASE_URL     = "http://image.tmdb.org/t/p/w342";
    private static final String BACKDROP_BASE_URL   = "http://image.tmdb.org/t/p/w780";

    // place holders in case the movie db does not have image yet
    // TODO: pretty sure these uri won't work but don't know why...
    private static final Uri POSTER_PLACEHOLDER     = Uri.parse(
            "android.resource://com.anakinfoxe.popularmovies/drawable/placeholder_poster.png"
    );
    private static final Uri BACKDROP_PLACEHOLDER   = Uri.parse(
            "android.resource://com.anakinfoxe.popularmovies/drawable/placeholder_backdrop.png"
    );


    public MovieTypeAdapterFactory() {
        super(Movie.class);
    }

    @Override
    protected void beforeWrite(Movie source, JsonElement toSerialize) {
        JsonObject jsonObject = toSerialize.getAsJsonObject();

        // uri => string
        String backdropPath = source.getBackdropPath().toString();
        jsonObject.addProperty(NAME_BACKDROP_PATH, backdropPath);

        // uri => string
        String posterPath = source.getPosterPath().toString();
        jsonObject.addProperty(NAME_POSTER_PATH, posterPath);

        // date => string
        String releaseDate = Helper.convertDateToString(source.getReleaseDate());
        jsonObject.addProperty(NAME_RELEASE_DATE, releaseDate);
    }

    @Override
    protected void afterRead(Movie source, JsonElement deserialized) {
        // string => uri
        try {
            String backdropPath = BACKDROP_BASE_URL + deserialized.getAsJsonObject()
                    .get(NAME_BACKDROP_PATH)
                    .getAsString();
            source.setBackdropPath(Uri.parse(backdropPath));
        } catch (Exception e) {
            source.setBackdropPath(BACKDROP_PLACEHOLDER);
        }

        // string => uri
        try {
            String posterPath = POSTER_BASE_URL + deserialized.getAsJsonObject()
                    .get(NAME_POSTER_PATH)
                    .getAsString();
            source.setPosterPath(Uri.parse(posterPath));
        } catch (Exception e) {
            source.setPosterPath(POSTER_PLACEHOLDER);
        }

        // string => date
        String releaseDate = deserialized.getAsJsonObject()
                                        .get(NAME_RELEASE_DATE)
                                        .getAsString();
        try {
            source.setReleaseDate(Helper.convertDateFromString(releaseDate));
        } catch (ParseException e) {
            Log.e(LOG_TAG, "Date parsing error ", e);
        }

    }
}
