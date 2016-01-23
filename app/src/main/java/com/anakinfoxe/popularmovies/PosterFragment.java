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
import com.anakinfoxe.popularmovies.model.Movie;

import java.util.List;

/**
 * Created by xing on 1/18/16.
 */
public class PosterFragment extends Fragment {

    private static final String LOG_TAG = PosterFragment.class.getSimpleName();

    private GridLayoutManager mLayoutManager;

    private PosterAdapter mPosterAdapter;

    private boolean isLoading = true;
    private int loadedPage = 1;

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

        // update the content of posters
        updatePosters(loadedPage);

        // set recycler view adapter
        rv.setAdapter(mPosterAdapter);

        rv.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                int firstVisibleItemPosition = mLayoutManager.findFirstVisibleItemPosition();
                int visibleItemCount = mLayoutManager.getChildCount();
                int totalItemCount = mLayoutManager.getItemCount();

                Log.v(LOG_TAG, firstVisibleItemPosition + ", " + visibleItemCount + ", " + totalItemCount + ". dy = " + dy);

                if (dy > 0) {

                    if (isLoading
                        && (firstVisibleItemPosition + visibleItemCount) >= totalItemCount) {
//                        isLoading = false;
                        updatePosters(++loadedPage);
                    }
                }
            }
        });

        return rv;
    }


    @Override
    public void onStart() {
        super.onStart();

        // update posters when program starts
        updatePosters(loadedPage);
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
