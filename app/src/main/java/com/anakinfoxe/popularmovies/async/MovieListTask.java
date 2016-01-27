package com.anakinfoxe.popularmovies.async;

import android.content.Context;
import android.os.AsyncTask;

import com.anakinfoxe.popularmovies.api.TheMovieDBApi;
import com.anakinfoxe.popularmovies.model.Movie;
import com.anakinfoxe.popularmovies.util.Network;

import java.util.List;

/**
 * Created by xing on 1/17/16.
 */
public class MovieListTask extends AsyncTask<Integer, Void, AsyncResult<List<Movie>>> {

    private static final String LOG_TAG = MovieListTask.class.getSimpleName();

    private final Context context;
    private final AsyncResponse<AsyncResult<List<Movie>>> delegate;


    public MovieListTask(Context c, AsyncResponse<AsyncResult<List<Movie>>> d) {
        this.context = c;
        this.delegate = d;
    }

    @Override
    protected AsyncResult<List<Movie>> doInBackground(Integer... params) {
        if (Network.isConnected(context))
            return new AsyncResult<>(TheMovieDBApi.getMovies(LOG_TAG, params[0], params[1]));
        else
            return new AsyncResult<>("Network is not available");
    }

    @Override
    protected void onPostExecute(AsyncResult<List<Movie>> result) {
        delegate.processFinish(result);
    }

}
