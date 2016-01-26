package com.anakinfoxe.popularmovies;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
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
    private FloatingActionsMenu mFamPoster;
    private FloatingActionButton mFabSorting;
    private FloatingActionButton mFabFavorite;
    private FrameLayout mFlInterceptor;

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

        updatePosters(sortingType, 1);

        // TODO: kind of ugly to use 1 for the first loading and setup 2 for the listener
        rv.addOnScrollListener(new InfiniteScrollListener(mLayoutManager, 2) {
            @Override
            public void onLoadMore(int page, int totalItemCount) {
                updatePosters(sortingType, page);
            }
        });

        // setup floating action buttons
        setupFab();

        return rv;
    }


    @Override
    public void onStart() {
        super.onStart();

        // update posters when program starts
//        updatePosters(sortingType, 1);
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
        mFamPoster = (FloatingActionsMenu) getActivity().findViewById(R.id.fam_poster);
        mFabSorting = (FloatingActionButton) getActivity().findViewById(R.id.fab_sorting);
        mFabFavorite = (FloatingActionButton) getActivity().findViewById(R.id.fab_favorite);
        mFlInterceptor = (FrameLayout) getActivity().findViewById(R.id.fl_interceptor);

        mFlInterceptor.setBackgroundColor(ContextCompat.getColor(getContext(),
                R.color.black_semi_transparent));
        mFlInterceptor.setClickable(false);
        mFlInterceptor.setVisibility(View.GONE);
        mFlInterceptor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mFamPoster.collapse();
            }
        });


        mFamPoster.setOnFloatingActionsMenuUpdateListener(
                new FloatingActionsMenu.OnFloatingActionsMenuUpdateListener() {
                    @Override
                    public void onMenuExpanded() {
                        mFlInterceptor.setClickable(true);
                        mFlInterceptor.setVisibility(View.VISIBLE);


                        if (sortingType == TheMovieDBApi.SORTING_BY_POPULARITY)
                            mFabSorting.setTitle("Sort by Rating");
                        else if (sortingType == TheMovieDBApi.SORTING_BY_RATING)
                            mFabSorting.setTitle("Sort by Popularity");
                    }

                    @Override
                    public void onMenuCollapsed() {
                        // do nothing
                        mFlInterceptor.setClickable(false);
                        mFlInterceptor.setVisibility(View.GONE);
                    }
                });


        mFabSorting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // flip
                sortingType = (sortingType == TheMovieDBApi.SORTING_BY_POPULARITY ?
                        TheMovieDBApi.SORTING_BY_RATING : TheMovieDBApi.SORTING_BY_POPULARITY);

                // replace posters according to new sorting
                replacePosters(sortingType, 1);

                // collapse fam
                mFamPoster.collapse();
            }
        });

        mFabFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "Ta-dah!! TODO in next stage!", Toast.LENGTH_SHORT)
                        .show();

                // collapse fam
                mFamPoster.collapse();
            }
        });
    }
}
