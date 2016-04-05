package com.anakinfoxe.popularmovies;

import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.anakinfoxe.popularmovies.adapter.PosterAdapter;
import com.anakinfoxe.popularmovies.async.MovieManager;
import com.anakinfoxe.popularmovies.listener.InfiniteScrollListener;
import com.anakinfoxe.popularmovies.model.Movie;
import com.anakinfoxe.popularmovies.model.Response;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

/**
 * Created by xing on 1/18/16.
 */
public class PosterFragment extends Fragment {

    private static final String LOG_TAG = PosterFragment.class.getSimpleName();

    private static final String SAVED_MOVIES = "saved_movies";


    private RecyclerView.LayoutManager mLayoutManager;

    private PosterAdapter mPosterAdapter;

    private String sortingType = MovieManager.SORTING_BY_POPULARITY;
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
        if (getActivity().getResources().getConfiguration().orientation
                == Configuration.ORIENTATION_PORTRAIT)
            mLayoutManager = new GridLayoutManager(rv.getContext(), 2);
        else
            mLayoutManager = new GridLayoutManager(rv.getContext(), 3);

        // set layout manager
        rv.setLayoutManager(mLayoutManager);

        // instantiate poster adapter
        mPosterAdapter = new PosterAdapter(getActivity());

        // set recycler view adapter
        rv.setAdapter(mPosterAdapter);

        // init posters
        if (savedInstanceState != null && savedInstanceState.containsKey(SAVED_MOVIES)) {
            List<Movie> savedMovies = savedInstanceState.getParcelableArrayList(SAVED_MOVIES);
            mPosterAdapter.setMovies(savedMovies);
        } else
            replacePosters(sortingType, 1);

        // set OnScrollListener to load more data
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
    public void onSaveInstanceState(Bundle outState) {
        if (mPosterAdapter != null) {
            List<Movie> movies2Save = mPosterAdapter.getMovies();

            if (movies2Save != null)
                outState.putParcelableArrayList(SAVED_MOVIES,
                        (ArrayList<? extends Parcelable>) movies2Save);
        }
        super.onSaveInstanceState(outState);
    }

    private void updatePosters(String sortingType, int pageId) {
        Call<Response> response = MovieManager.getService()
                                        .getMovieList(sortingType, pageId);
        response.enqueue(new Callback<Response>() {
            @Override
            public void onResponse(retrofit2.Response<Response> response) {
                Response resp = response.body();
                mPosterAdapter.addMovies(resp.getMovies());
            }

            @Override
            public void onFailure(Throwable t) {
                Log.e(LOG_TAG, "getting posters error ", t);
            }
        });
    }

    private void replacePosters(String sortingType, int pageId) {
        Call<Response> response = MovieManager.getService()
                                        .getMovieList(sortingType, pageId);
        response.enqueue(new Callback<Response>() {
            @Override
            public void onResponse(retrofit2.Response<Response> response) {
                Response resp = response.body();
                mPosterAdapter.setMovies(resp.getMovies());
            }

            @Override
            public void onFailure(Throwable t) {
                Log.e(LOG_TAG, "getting posters error ", t);
            }
        });
    }



    private void setupFab() {
        mFamPoster = (FloatingActionsMenu) getActivity().findViewById(R.id.fam_poster);
        mFabSorting = (FloatingActionButton) getActivity().findViewById(R.id.fab_sorting);
        mFabFavorite = (FloatingActionButton) getActivity().findViewById(R.id.fab_favorite);
        mFlInterceptor = (FrameLayout) getActivity().findViewById(R.id.fl_interceptor);

        // interceptor will intercept the click event when fam is expanded
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
                        // display the interceptor when fam is expanded
                        mFlInterceptor.setClickable(true);
                        mFlInterceptor.setVisibility(View.VISIBLE);

                        if (sortingType.equals(MovieManager.SORTING_BY_POPULARITY)) {
                            mFabSorting.setTitle(getResources()
                                    .getString(R.string.fab_sort_by_rating));
                            mFabSorting.setIconDrawable(ContextCompat.getDrawable(getContext(),
                                    R.drawable.ic_star_rate_white_18dp));
                        } else if (sortingType.equals(MovieManager.SORTING_BY_RATING)) {
                            mFabSorting.setTitle(getResources()
                                    .getString(R.string.fab_sort_by_popularity));
                            mFabSorting.setIconDrawable(ContextCompat.getDrawable(getContext(),
                                    R.drawable.ic_trending_up_white_18dp));
                        }
                    }

                    @Override
                    public void onMenuCollapsed() {
                        // dispel the interceptor when fam is collapsed
                        mFlInterceptor.setClickable(false);
                        mFlInterceptor.setVisibility(View.GONE);
                    }
                });


        mFabSorting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // flip
                sortingType = (sortingType.equals(MovieManager.SORTING_BY_POPULARITY) ?
                        MovieManager.SORTING_BY_RATING : MovieManager.SORTING_BY_POPULARITY);

                // replace posters according to new sorting
                replacePosters(sortingType, 1);

                // collapse fam
                mFamPoster.collapse();
            }
        });

        mFabFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "Oops!! TODO in next stage!", Toast.LENGTH_SHORT)
                        .show();

                // collapse fam
                mFamPoster.collapse();
            }
        });
    }
}
