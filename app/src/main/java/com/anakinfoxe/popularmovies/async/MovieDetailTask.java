package com.anakinfoxe.popularmovies.async;

import android.os.AsyncTask;

import com.anakinfoxe.popularmovies.api.TheMovieDBApi;
import com.anakinfoxe.popularmovies.model.Movie;

/**
 * Created by xing on 1/17/16.
 */
public class MovieDetailTask extends AsyncTask<Long, Void, Movie> {

    private static final String LOG_TAG = MovieDetailTask.class.getSimpleName();

    public AsyncResponse<Movie> delegate = null;

    public MovieDetailTask(AsyncResponse<Movie> d) {
        this.delegate = d;
    }

    @Override
    protected Movie doInBackground(Long... params) {
        return TheMovieDBApi.getMovieDetail(LOG_TAG, params[0]);
    }

    @Override
    protected void onPostExecute(Movie movie) {
        delegate.processFinish(movie);
    }
}
