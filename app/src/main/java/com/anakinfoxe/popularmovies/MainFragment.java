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
import com.anakinfoxe.popularmovies.listener.InfiniteScrollListener;
import com.anakinfoxe.popularmovies.model.Movie;
import com.anakinfoxe.popularmovies.model.response.MovieResponse;
import com.anakinfoxe.popularmovies.service.ServiceManager;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;

/**
 * Created by xing on 1/18/16.
 */
public class MainFragment extends Fragment {

    private static final String LOG_TAG = MainFragment.class.getSimpleName();

    private static final String SAVED_MOVIES = "saved_movies";
    private static final String CURRENT_SORT = "current_sort";

    private PosterAdapter mPosterAdapter;

    private String sortingType;

    @Bind(R.id.fam_poster) FloatingActionsMenu mFamPoster;
    @Bind(R.id.fab_sorting) FloatingActionButton mFabSorting;
    @Bind(R.id.fab_favorite) FloatingActionButton mFabFavorite;
    @Bind(R.id.fl_interceptor) FrameLayout mFlInterceptor;
    @Bind(R.id.recyclerview_poster) RecyclerView mRvPosters;

    public MainFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // inflate recycler view
        View rootView = inflater.inflate(R.layout.main_fragment, container, false);

        ButterKnife.bind(this, rootView);

        // instantiate layout manager
        RecyclerView.LayoutManager layoutManager;
        if (getActivity().getResources().getConfiguration().orientation
                == Configuration.ORIENTATION_PORTRAIT)
            layoutManager = new GridLayoutManager(mRvPosters.getContext(), 2);
        else
            layoutManager = new GridLayoutManager(mRvPosters.getContext(), 3);

        // set layout manager
        mRvPosters.setLayoutManager(layoutManager);

        // instantiate poster adapter
        mPosterAdapter = new PosterAdapter(getActivity());

        // set recycler view adapter
        mRvPosters.setAdapter(mPosterAdapter);

        // init posters
        if (savedInstanceState != null && savedInstanceState.containsKey(SAVED_MOVIES)) {
            sortingType = savedInstanceState.getString(CURRENT_SORT);

            List<Movie> savedMovies = savedInstanceState.getParcelableArrayList(SAVED_MOVIES);
            mPosterAdapter.setMovies(savedMovies);
        } else {
            sortingType = ServiceManager.SORTING_BY_POPULARITY;

            replacePosters(sortingType, 1);
        }

        // set OnScrollListener to load more data
        mRvPosters.addOnScrollListener(new InfiniteScrollListener(layoutManager, 2) {
            @Override
            public void onLoadMore(int page, int totalItemCount) {
                updatePosters(sortingType, page);
            }
        });

        // setup floating action buttons
        setupFab(rootView);

        return rootView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        if (mPosterAdapter != null) {
            List<Movie> movies2Save = mPosterAdapter.getMovies();

            if (movies2Save != null)
                outState.putParcelableArrayList(SAVED_MOVIES,
                        (ArrayList<? extends Parcelable>) movies2Save);
        }
        outState.putString(CURRENT_SORT, sortingType);
        super.onSaveInstanceState(outState);
    }

    public void setmTwoPane(boolean mTwoPane) {
        if (mPosterAdapter != null)
            mPosterAdapter.setmTwoPane(mTwoPane);
    }


    private void updatePosters(String sortingType, int pageId) {
        Call<MovieResponse> response = ServiceManager.getMovieService()
                                        .getMovieList(sortingType, pageId);
        response.enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(retrofit2.Response<MovieResponse> response) {
                MovieResponse resp = response.body();
                mPosterAdapter.addMovies(resp.getMovies());
            }

            @Override
            public void onFailure(Throwable t) {
                Log.e(LOG_TAG, "getting posters error ", t);
            }
        });
    }

    private void replacePosters(String sortingType, int pageId) {
        Call<MovieResponse> response = ServiceManager.getMovieService()
                                        .getMovieList(sortingType, pageId);
        response.enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(retrofit2.Response<MovieResponse> response) {
                MovieResponse resp = response.body();
                mPosterAdapter.setMovies(resp.getMovies());
            }

            @Override
            public void onFailure(Throwable t) {
                Log.e(LOG_TAG, "getting posters error ", t);
            }
        });
    }



    private void setupFab(View rootView) {
//        mFamPoster = (FloatingActionsMenu) rootView.findViewById(R.id.fam_poster);
//        mFabSorting = (FloatingActionButton) rootView.findViewById(R.id.fab_sorting);
//        mFabFavorite = (FloatingActionButton) rootView.findViewById(R.id.fab_favorite);
//        mFlInterceptor = (FrameLayout) rootView.findViewById(R.id.fl_interceptor);

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

                        if (sortingType.equals(ServiceManager.SORTING_BY_POPULARITY)) {
                            mFabSorting.setTitle(getResources()
                                    .getString(R.string.fab_sort_by_rating));
                            mFabSorting.setIconDrawable(ContextCompat.getDrawable(getContext(),
                                    R.drawable.ic_star_rate_white_18dp));
                        } else if (sortingType.equals(ServiceManager.SORTING_BY_RATING)) {
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
                sortingType = (sortingType.equals(ServiceManager.SORTING_BY_POPULARITY) ?
                        ServiceManager.SORTING_BY_RATING : ServiceManager.SORTING_BY_POPULARITY);

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
