package com.anakinfoxe.popularmovies;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.anakinfoxe.popularmovies.adapter.PosterAdapter;
import com.anakinfoxe.popularmovies.model.Movie;

public class MainActivity extends AppCompatActivity implements PosterAdapter.CallBack {

    private static final String DETAILFRAGMENT_TAG  = "DF_TAG";

    private static final String INITED_FLAG         = "inited_flag";

    private boolean mTwoPane;
    private boolean mInited = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.main_activity);

        if (findViewById(R.id.container_detail) != null) {
            mTwoPane = true;

            if (savedInstanceState == null)
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container_detail,
                                new DetailFragment(),
                                DETAILFRAGMENT_TAG)
                        .commit();
        } else
            mTwoPane = false;

        if (savedInstanceState != null && savedInstanceState.containsKey(INITED_FLAG))
            mInited = savedInstanceState.getBoolean(INITED_FLAG);

        // trigger loading the first movie
        if (mTwoPane && !mInited) {
            MainFragment mf = (MainFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.fragment_main);

            mf.loadFirstMovie();
        }

        mInited = true;
    }

    @Override
    public void onItemSelected(Movie movie) {
        if (mTwoPane) {
            Bundle args = new Bundle();
            args.putParcelable(DetailFragment.MOVIE_OBJECT, movie);

            DetailFragment df = new DetailFragment();
            df.setArguments(args);

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container_detail, df, DETAILFRAGMENT_TAG)
                    .commit();
        } else {
            Intent intent = new Intent(this, DetailActivity.class);
            intent.putExtra(DetailFragment.MOVIE_OBJECT, movie);
            startActivity(intent);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putBoolean(INITED_FLAG, mInited);

        super.onSaveInstanceState(outState);
    }
}
