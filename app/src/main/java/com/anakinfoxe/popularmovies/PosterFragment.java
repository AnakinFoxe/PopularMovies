package com.anakinfoxe.popularmovies;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.anakinfoxe.popularmovies.adapter.PosterAdapter;
import com.anakinfoxe.popularmovies.api.TheMovieDBApi;
import com.anakinfoxe.popularmovies.async.AsyncResponse;
import com.anakinfoxe.popularmovies.async.MovieListTask;
import com.anakinfoxe.popularmovies.listener.InfiniteScrollListener;
import com.anakinfoxe.popularmovies.model.Movie;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;

import java.util.List;

/**
 * Created by xing on 1/18/16.
 */
public class PosterFragment extends Fragment {

    private static final String LOG_TAG = PosterFragment.class.getSimpleName();

    private GridLayoutManager mLayoutManager;

    private PosterAdapter mPosterAdapter;

    private int sortingType = TheMovieDBApi.SORTING_BY_POPULARITY;
    private FloatingActionsMenu famPoster;
    private FloatingActionButton fabSorting;
    private FloatingActionButton fabFavorite;

    public PosterFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // inflate recycler view
        RecyclerView rv = (RecyclerView) inflater
                .inflate(R.layout.poster_fragment, container, false);

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
                updatePosters(sortingType, page);
            }
        });

        // setup floating action buttons
        setupFab();

        rv.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                Log.v(LOG_TAG, "isExpanded=" + famPoster.isExpanded());
                if (famPoster.isExpanded()) {
                    famPoster.collapse();

                    Log.v(LOG_TAG, "it is expanded!");

                    return true;
                }

                return false;
            }
        });

        return rv;
    }


    @Override
    public void onStart() {
        super.onStart();

        // update posters when program starts
        updatePosters(sortingType, 1);
    }

    private void updatePosters(int sortingType, int pageNum) {
        MovieListTask task = new MovieListTask(new AsyncResponse<List<Movie>>() {
            @Override
            public void processFinish(List<Movie> output) {
                if (output != null)
                    mPosterAdapter.addMovies(output);
            }
        });
        task.execute(sortingType, pageNum);
    }

    private void replacePosters(int sortingType, int pageNum) {
        MovieListTask task = new MovieListTask(new AsyncResponse<List<Movie>>() {
            @Override
            public void processFinish(List<Movie> output) {
                if (output != null)
                    mPosterAdapter.setMovies(output);
            }
        });
        task.execute(sortingType, pageNum);
    }

    private void setupFab() {
        famPoster = (FloatingActionsMenu) getActivity().findViewById(R.id.fam_poster);
        fabSorting = (FloatingActionButton) getActivity().findViewById(R.id.fab_sorting);
        fabFavorite = (FloatingActionButton) getActivity().findViewById(R.id.fab_favorite);


        famPoster.setOnFloatingActionsMenuUpdateListener(
                new FloatingActionsMenu.OnFloatingActionsMenuUpdateListener() {
                    @Override
                    public void onMenuExpanded() {
                        if (sortingType == TheMovieDBApi.SORTING_BY_POPULARITY)
                            fabSorting.setTitle("Sort by Rating");
                        else if (sortingType == TheMovieDBApi.SORTING_BY_RATING)
                            fabSorting.setTitle("Sort by Popularity");
                    }

                    @Override
                    public void onMenuCollapsed() {
                        // do nothing
                    }
                });


        fabSorting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // flip
                sortingType = (sortingType == TheMovieDBApi.SORTING_BY_POPULARITY ?
                        TheMovieDBApi.SORTING_BY_RATING : TheMovieDBApi.SORTING_BY_POPULARITY);

                // replace posters according to new sorting
                replacePosters(sortingType, 1);

                // collapse fam
//                famPoster.collapse();
            }
        });

        fabFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "Ta-dah!! TODO in next stage!", Toast.LENGTH_SHORT)
                        .show();

                // collapse fam
//                famPoster.collapse();
            }
        });
    }
}
