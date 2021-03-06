package com.anakinfoxe.popularmovies.util;


import com.anakinfoxe.popularmovies.model.Movie;
import com.anakinfoxe.popularmovies.model.Video;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by xing on 4/10/16.
 */
public class Helper {

    // The Movie Database
    private static final String TMDB_BASE_URL       = "http://www.themoviedb.org/";

    // YouTube
    private static final String YOUTUBE_BASE_URL    = "http://www.youtube.com/";
    private static final String YOUTUBE_THUMB_URL   = "http://img.youtube.com/vi/";

    // Date format
    private static final SimpleDateFormat FMT       =
            new SimpleDateFormat("yyyy-MM-dd", new Locale("en"));


    public static String getTmdbMovieUrl(Movie movie) {
        return TMDB_BASE_URL + "movie/" + movie.getId();
    }

    public static String getYoutubeVideoUrl(Video video) {
        return YOUTUBE_BASE_URL + "watch?v=" + video.getKey();
    }

    public static String getYoutubeThumbnailUrl(Video video) {
        return YOUTUBE_THUMB_URL + video.getKey() + "/0.jpg";
    }

    public static String convertDateToString(Date date) {
        return FMT.format(date);
    }

    public static Date convertDateFromString(String dateStr)
            throws ParseException {
        return FMT.parse(dateStr);
    }

}
