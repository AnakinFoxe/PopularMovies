package com.anakinfoxe.popularmovies.async;

import android.content.Context;
import android.os.AsyncTask;

import com.anakinfoxe.popularmovies.api.TheMovieDBApi;
import com.anakinfoxe.popularmovies.model.Movie;
import com.anakinfoxe.popularmovies.util.Network;

/**
 * Created by xing on 1/17/16.
 */
public class MovieDetailTask extends AsyncTask<Long, Void, AsyncResult<Movie>> {

    private static final String LOG_TAG = MovieDetailTask.class.getSimpleName();

    private final Context context;
    private final AsyncResponse<AsyncResult<Movie>> delegate;

    public MovieDetailTask(Context c, AsyncResponse<AsyncResult<Movie>> d) {
        this.context = c;
        this.delegate = d;
    }

    @Override
    protected AsyncResult<Movie> doInBackground(Long... params) {
        if (Network.isConnected(context))
            return new AsyncResult<>(TheMovieDBApi.getMovieDetail(LOG_TAG, params[0]));
        else
            return new AsyncResult<>("Network is not available");
    }

    @Override
    protected void onPostExecute(AsyncResult<Movie> result) {
        delegate.processFinish(result);
    }
}
