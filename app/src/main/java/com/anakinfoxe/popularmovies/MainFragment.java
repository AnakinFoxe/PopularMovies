package com.anakinfoxe.popularmovies;

import android.content.res.Configuration;
import android.database.Cursor;
import android.net.Uri;
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
import com.anakinfoxe.popularmovies.data.MovieContract;
import com.anakinfoxe.popularmovies.listener.InfiniteScrollListener;
import com.anakinfoxe.popularmovies.model.Movie;
import com.anakinfoxe.popularmovies.model.response.MovieResponse;
import com.anakinfoxe.popularmovies.service.ServiceManager;
import com.anakinfoxe.popularmovies.util.Helper;
import com.anakinfoxe.popularmovies.util.Network;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by xing on 1/18/16.
 */
public class MainFragment extends Fragment {

    private static final String LOG_TAG = MainFragment.class.getSimpleName();

    private static final String SAVED_MOVIES = "saved_movies";
    private static final String CURRENT_SORT = "current_sort";
    private static final String FAVORITE     = "favorite";

    private PosterAdapter mPosterAdapter;

    private InfiniteScrollListener mInfiniteScrollListener;
    private String mSortingType;
    private boolean isFavorite;

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

        mInfiniteScrollListener = new InfiniteScrollListener(layoutManager, 2) {
            @Override
            public void onLoadMore(int page, int totalItemCount) {
                updatePosters(mSortingType, page);
            }
        };

        // set layout manager
        mRvPosters.setLayoutManager(layoutManager);

        // instantiate poster adapter
        mPosterAdapter = new PosterAdapter(getActivity());

        // set recycler view adapter
        mRvPosters.setAdapter(mPosterAdapter);

        // init posters
        if (savedInstanceState != null && savedInstanceState.containsKey(SAVED_MOVIES)) {
            mSortingType = savedInstanceState.getString(CURRENT_SORT);
            isFavorite = savedInstanceState.getBoolean(FAVORITE);
            List<Movie> savedMovies = savedInstanceState.getParcelableArrayList(SAVED_MOVIES);
            mPosterAdapter.setMovies(savedMovies);
        } else {
            mSortingType = ServiceManager.SORTING_BY_POPULARITY;
            isFavorite = false;
            replacePosters(mSortingType, 1);
        }

        // set OnScrollListener to load more data
        if (!isFavorite) {
            mRvPosters.clearOnScrollListeners();
            mRvPosters.addOnScrollListener(mInfiniteScrollListener);
        }

        // setup floating action buttons
        setupFab();

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
        outState.putString(CURRENT_SORT, mSortingType);
        outState.putBoolean(FAVORITE, isFavorite);
        super.onSaveInstanceState(outState);
    }

    public void setmTwoPane(boolean mTwoPane) {
        if (mPosterAdapter != null)
            mPosterAdapter.setmTwoPane(mTwoPane);
    }


    private void updatePosters(String sortingType, int pageId) {
        if (!Network.isConnected(getContext())) {
            displayFavorite();

            Toast.makeText(getActivity(),
                    "Network not available. Display favorite movies only.",
                    Toast.LENGTH_SHORT)
                    .show();
        }

        Call<MovieResponse> response = ServiceManager.getMovieService()
                                        .getMovieList(sortingType, pageId);
        response.enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                if (!response.isSuccessful()) {
                    Log.e(LOG_TAG,
                            "getting " + call.request().url() + ": failed: " + response.code());
                    return;
                }

                MovieResponse resp = response.body();
                mPosterAdapter.addMovies(resp.getMovies());
            }

            @Override
            public void onFailure(Call<MovieResponse> call, Throwable t) {
                Log.e(LOG_TAG, "getting " + call.request().url() + ": failed: " + t);
            }
        });
    }

    private void replacePosters(String sortingType, int pageId) {
        if (!Network.isConnected(getContext())) {
            displayFavorite();

            Toast.makeText(getActivity(),
                    "Network not available. Display favorite movies only.",
                    Toast.LENGTH_SHORT)
                    .show();
        }

        Call<MovieResponse> response = ServiceManager.getMovieService()
                                        .getMovieList(sortingType, pageId);
        response.enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                if (!response.isSuccessful()) {
                    Log.e(LOG_TAG,
                            "getting " + call.request().url() + ": failed: " + response.code());
                    return;
                }

                MovieResponse resp = response.body();
                mPosterAdapter.setMovies(resp.getMovies());
            }

            @Override
            public void onFailure(Call<MovieResponse> call, Throwable t) {
                Log.e(LOG_TAG, "getting " + call.request().url() + ": failed: " + t);
            }
        });
    }



    private void setupFab() {
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

                        if (mSortingType.equals(ServiceManager.SORTING_BY_POPULARITY)) {
                            mFabSorting.setTitle(getResources()
                                    .getString(R.string.fab_sort_by_rating));
                            mFabSorting.setIconDrawable(ContextCompat.getDrawable(getContext(),
                                    R.drawable.ic_star_rate_white_18dp));
                        } else if (mSortingType.equals(ServiceManager.SORTING_BY_RATING)) {
                            mFabSorting.setTitle(getResources()
                                    .getString(R.string.fab_sort_by_popularity));
                            mFabSorting.setIconDrawable(ContextCompat.getDrawable(getContext(),
                                    R.drawable.ic_trending_up_white_18dp));
                        }

                        if (isFavorite)
                            mFabFavorite.setVisibility(View.GONE);
                        else
                            mFabFavorite.setVisibility(View.VISIBLE);
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
                mSortingType = (mSortingType.equals(ServiceManager.SORTING_BY_POPULARITY) ?
                        ServiceManager.SORTING_BY_RATING : ServiceManager.SORTING_BY_POPULARITY);

                // replace posters according to new sorting
                replacePosters(mSortingType, 1);

                // add infinite onScroll listener
                mRvPosters.clearOnScrollListeners();
                mRvPosters.addOnScrollListener(mInfiniteScrollListener);

                // collapse fam
                mFamPoster.collapse();

                if (Network.isConnected(getContext()))
                    isFavorite = false;
            }
        });

        mFabFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayFavorite();

                // collapse fam
                mFamPoster.collapse();
            }
        });
    }

    private void displayFavorite() {
        mPosterAdapter.setMovies(getMoviesFromDb());

        // remove infinite onScroll listener
        mRvPosters.clearOnScrollListeners();

        isFavorite = true;
    }

    private List<Movie> getMoviesFromDb() {
        List<Movie> movies = new ArrayList<>();

        Cursor c = getContext().getContentResolver().query(
                MovieContract.MovieEntry.CONTENT_URI,
                null,
                null,
                null,
                null
        );

        if (c != null) {
            while (c.moveToNext()) {
                movies.add(buildMovieObject(c));
            }
            c.close();
        }

        return movies;
    }

    private Movie buildMovieObject(Cursor c) {
        if (c != null) {
            Movie movie = new Movie();

            int colAdult = c.getColumnIndex(MovieContract.MovieEntry.COLUMN_ADULT);
            int colBackdropPath = c.getColumnIndex(MovieContract.MovieEntry.COLUMN_BACKDROP_PATH);
            int colHomePage = c.getColumnIndex(MovieContract.MovieEntry.COLUMN_HOMEPAGE);
            int colMovieId = c.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_ID);
            int colOriginalTitle = c.getColumnIndex(MovieContract.MovieEntry.COLUMN_ORIGINAL_TITLE);
            int colOverview = c.getColumnIndex(MovieContract.MovieEntry.COLUMN_OVERVIEW);
            int colPopularity = c.getColumnIndex(MovieContract.MovieEntry.COLUMN_POPULARITY);
            int colPosterPath = c.getColumnIndex(MovieContract.MovieEntry.COLUMN_POSTER_PATH);
            int colReleaseDate = c.getColumnIndex(MovieContract.MovieEntry.COLUMN_RELEASE_DATE);
            int colRuntime = c.getColumnIndex(MovieContract.MovieEntry.COLUMN_RUNTIME);
            int colTitle = c.getColumnIndex(MovieContract.MovieEntry.COLUMN_TITLE);
            int colVoteAverage = c.getColumnIndex(MovieContract.MovieEntry.COLUMN_VOTE_AVERAGE);
            int colVoteCount = c.getColumnIndex(MovieContract.MovieEntry.COLUMN_VOTE_COUNT);

            movie.setAdult(Boolean.parseBoolean(c.getString(colAdult)));
            movie.setBackdropPath(Uri.parse(c.getString(colBackdropPath)));
            movie.setHomepage(c.getString(colHomePage));
            movie.setId(c.getLong(colMovieId));
            movie.setOriginalTitle(c.getString(colOriginalTitle));
            movie.setOverview(c.getString(colOverview));
            movie.setPopularity(c.getDouble(colPopularity));
            movie.setPosterPath(Uri.parse(c.getString(colPosterPath)));
            try {
                movie.setReleaseDate(Helper.convertDateFromString(c.getString(colReleaseDate)));
            } catch (ParseException e) {
                Log.e(LOG_TAG, "Date parsing error ", e);
            }
            movie.setRuntime(c.getInt(colRuntime));
            movie.setTitle(c.getString(colTitle));
            movie.setVoteAverage(c.getDouble(colVoteAverage));
            movie.setVoteCount(c.getInt(colVoteCount));

            return movie;
        }

        return null;
    }
}
