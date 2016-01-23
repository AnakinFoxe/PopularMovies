package com.anakinfoxe.popularmovies;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.anakinfoxe.popularmovies.adapter.PosterAdapter;
import com.anakinfoxe.popularmovies.async.AsyncResponse;
import com.anakinfoxe.popularmovies.async.MovieListTask;
import com.anakinfoxe.popularmovies.listener.InfiniteScrollListener;
import com.anakinfoxe.popularmovies.model.Movie;

import java.util.List;

/**
 * Created by xing on 1/18/16.
 */
public class PosterFragment extends Fragment {

    private static final String LOG_TAG = PosterFragment.class.getSimpleName();

    private GridLayoutManager mLayoutManager;

    private PosterAdapter mPosterAdapter;

    public PosterFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // inflate recycler view
        RecyclerView rv = (RecyclerView) inflater.inflate(R.layout.poster_fragment, container, false);

        // instantiate layout manager
        // TODO: make spanCount adaptive to dimension
        mLayoutManager = new GridLayoutManager(rv.getContext(), 2);

        // set layout manager
        rv.setLayoutManager(mLayoutManager);

        // instantiate poster adapter
        mPosterAdapter = new PosterAdapter(getActivity());

        // set recycler view adapter
        rv.setAdapter(mPosterAdapter);

        rv.addOnScrollListener(new InfiniteScrollListener(mLayoutManager, 1) {
            @Override
            public void onLoadMore(int page, int totalItemCount) {
                updatePosters(page);
            }
        });

        return rv;
    }


    @Override
    public void onStart() {
        super.onStart();

        // update posters when program starts
        updatePosters(1);
    }

    private void updatePosters(int pageNum) {
        MovieListTask task = new MovieListTask(new AsyncResponse<List<Movie>>() {
            @Override
            public void processFinish(List<Movie> output) {
                if (output != null)
                    mPosterAdapter.addMovies(output);
            }
        });
        task.execute(pageNum);
    }
}
