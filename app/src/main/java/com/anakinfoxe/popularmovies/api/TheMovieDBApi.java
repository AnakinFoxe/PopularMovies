package com.anakinfoxe.popularmovies.api;

import android.net.Uri;
import android.util.Log;

import com.anakinfoxe.popularmovies.BuildConfig;
import com.anakinfoxe.popularmovies.model.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by xing on 1/17/16.
 */
public class TheMovieDBApi {
    // TODO: use Gson to replace org.json

    public static final int SORTING_BY_POPULARITY   = 1;
    public static final int SORTING_BY_RATING       = 3;

    // themoviedb.org api key
    // register an account to obtain it at https://www.themoviedb.org/
    private static final String API_KEY             = BuildConfig.THE_MOVIE_DB_API_KEY;

    // themoviedb.org api base url
    private static final String API_BASE_URL        = "https://api.themoviedb.org/3/movie/";
    private static final String POSTER_BASE_URL     = "http://image.tmdb.org/t/p/w342";
    private static final String BACKDROP_BASE_URL   = "http://image.tmdb.org/t/p/w780";

    private static SimpleDateFormat formatter =
            new SimpleDateFormat("yyyy-MM-dd", new Locale("en"));


    public static List<Movie> getMovies(String logTag, int sortingType, int pageNum) {
        URL url = null;

        Log.v(logTag, "Getting " + getSortingStr(sortingType) + " movie list page " + pageNum);

        try {
            // construct url
            Uri.Builder builder = Uri.parse(API_BASE_URL)
                    .buildUpon()
                    .appendPath(getSortingStr(sortingType))
                    .appendQueryParameter("page", String.valueOf(pageNum))
                    .appendQueryParameter("api_key", API_KEY);
            url = new URL(builder.build().toString());
        } catch (MalformedURLException e) {
            Log.e(logTag, "URL Error ", e);
        }

        String response = getResponseFromUrl(logTag, url);

        return getMoviesFromJson(logTag, response);
    }

    public static Movie getMovieDetail(String logTag, Long id) {
        if (id == null)
            return null;

        URL url = null;

        try {
            // construct url
            Uri.Builder builder = Uri.parse(API_BASE_URL)
                    .buildUpon()
                    .appendPath(id.toString())
                    .appendQueryParameter("api_key", API_KEY);

            url = new URL(builder.build().toString());
        } catch (MalformedURLException e) {
            Log.e(logTag, "URL Error ", e);
        }

        String response = getResponseFromUrl(logTag, url);

        return getMovieDetailFromJson(logTag, response);
    }


    private static String getResponseFromUrl(String logTag, URL url) {
        if (url == null)
            return null;

        HttpURLConnection urlConnection = null;
        BufferedReader br = null;

        try {
            // create request and open the connection
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // read input stream and put into a string
            InputStream is = urlConnection.getInputStream();
            StringBuilder sb = new StringBuilder();
            if (is == null)
                return null;
            br = new BufferedReader(new InputStreamReader(is));

            String line;
            while ((line = br.readLine()) != null)
                sb.append(line).append("\n");

            if (sb.length() == 0)
                return null;

            return sb.toString();
        } catch (IOException e) {
            Log.e(logTag, "Connection Error ", e);
        } finally {
            if (urlConnection != null)
                urlConnection.disconnect();
            if (br != null)
                try {
                    br.close();
                } catch (IOException e) {
                    Log.e(logTag, "Error closing stream", e);
                }
        }

        return null;
    }


    private static List<Movie> getMoviesFromJson(String logTag, String movieListStr) {
        if (movieListStr == null)
            return null;

        try {

            JSONObject movieList = new JSONObject(movieListStr);
            JSONArray movies = movieList.getJSONArray("results");

            List<Movie> movieObjs = new ArrayList<>(movies.length());
            for (int i = 0; i < movies.length(); ++i) {
                JSONObject movie = movies.getJSONObject(i);

                Movie movieObj = new Movie();

                // the only crucial parts are
                // 1) poster url
                movieObj.setPosterPath(Uri.parse(POSTER_BASE_URL
                                        + movie.getString("poster_path")));
                // 2) movie id
                movieObj.setId(movie.getLong("id"));
                // still grab everything we got from the response
                movieObj.setAdult(movie.getBoolean("adult"));
                movieObj.setOverview(movie.getString("overview"));
                movieObj.setOriginalTitle(movie.getString("original_title"));
                movieObj.setTitle(movie.getString("title"));
                movieObj.setBackdropPath(Uri.parse(BACKDROP_BASE_URL
                                        + movie.getString("backdrop_path")));
                movieObj.setPopularity(movie.getDouble("popularity"));
                movieObj.setReleaseDate(formatter.parse(movie.getString("release_date")));
                movieObj.setVoteCount(movie.getLong("vote_count"));
                movieObj.setVoteAverage(movie.getDouble("vote_average"));

                movieObjs.add(movieObj);
            }

            return movieObjs;
        } catch (JSONException | ParseException e) {
            Log.e(logTag, "JSON parsing Error ", e);
        }

        return null;
    }

    private static Movie getMovieDetailFromJson(String logTag, String movieStr) {
        if (movieStr == null)
            return null;

        try {
            JSONObject movie = new JSONObject(movieStr);

            Movie movieObj = new Movie();

            movieObj.setAdult(movie.getBoolean("adult"));
            movieObj.setBackdropPath(Uri.parse(BACKDROP_BASE_URL
                                    + movie.getString("backdrop_path")));
            movieObj.setHomepage(movie.getString("homepage"));
            movieObj.setId(movie.getLong("id"));
            movieObj.setOriginalTitle(movie.getString("original_title"));
            movieObj.setOverview(movie.getString("overview"));
            movieObj.setPopularity(movie.getDouble("popularity"));
            movieObj.setPosterPath(Uri.parse(POSTER_BASE_URL
                                    + movie.getString("poster_path")));
            movieObj.setReleaseDate(formatter.parse(movie.getString("release_date")));
            movieObj.setRuntime(movie.getInt("runtime"));
            movieObj.setTitle(movie.getString("title"));
            movieObj.setVoteAverage(movie.getDouble("vote_average"));
            movieObj.setVoteCount(movie.getLong("vote_count"));

            return movieObj;
        } catch (JSONException | ParseException e) {
            Log.e(logTag, "JSON parsing Error ", e);
        }

        return null;
    }

    private static String getSortingStr(int sortingType) {
        switch (sortingType) {
            case SORTING_BY_RATING:
                return "top_rated";
            case SORTING_BY_POPULARITY:
            default:    // default using popularity
                return "popular";
        }
    }
}
