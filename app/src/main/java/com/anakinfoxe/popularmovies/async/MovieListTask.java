package com.anakinfoxe.popularmovies.async;

import android.os.AsyncTask;

import com.anakinfoxe.popularmovies.api.TheMovieDBApi;
import com.anakinfoxe.popularmovies.model.Movie;

import java.util.List;

/**
 * Created by xing on 1/17/16.
 */
public class MovieListTask extends AsyncTask<Integer, Void, List<Movie>> {

    private static final String LOG_TAG = MovieListTask.class.getSimpleName();

    public AsyncResponse<List<Movie>> delegate = null;

    public MovieListTask(AsyncResponse<List<Movie>> d) {
        this.delegate = d;
    }

    @Override
    protected List<Movie> doInBackground(Integer... params) {
        return TheMovieDBApi.getMovies(LOG_TAG, params[0], params[1]);
    }

    @Override
    protected void onPostExecute(List<Movie> movies) {
        delegate.processFinish(movies);
    }

}
