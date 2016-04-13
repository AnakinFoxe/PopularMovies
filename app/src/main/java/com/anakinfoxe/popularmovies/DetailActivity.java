package com.anakinfoxe.popularmovies;

import android.content.Intent;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;

import com.anakinfoxe.popularmovies.model.Movie;
import com.facebook.drawee.view.SimpleDraweeView;

import butterknife.Bind;
import butterknife.ButterKnife;

public class DetailActivity extends AppCompatActivity {

    @Bind(R.id.toolbar) Toolbar mToolbar;
    @Bind(R.id.drawee_backdrop) SimpleDraweeView mBackdropView;

    private Movie mMovie = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_activity);

        ButterKnife.bind(this);

        // bind actionbar to the toolbar with collapsing toolbar
        setSupportActionBar(mToolbar);

        if (savedInstanceState == null) {
            // get movie object from intent
            Intent intent = getIntent();
            if (intent != null && intent.hasExtra(DetailFragment.MOVIE_OBJECT))
                mMovie = intent.getExtras().getParcelable(DetailFragment.MOVIE_OBJECT);

            if (mMovie != null) {
                Bundle args = new Bundle();
                args.putParcelable(DetailFragment.MOVIE_OBJECT, mMovie);

                Fragment fragment = new DetailFragment();
                fragment.setArguments(args);

                getSupportFragmentManager().beginTransaction()
                        .add(R.id.container_detail, fragment)
                        .commit();
            }
        } else
            mMovie = savedInstanceState.getParcelable(DetailFragment.MOVIE_OBJECT);

        // set backdrop
        if (mMovie != null)
            mBackdropView.setImageURI(mMovie.getBackdropPath());
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelable(DetailFragment.MOVIE_OBJECT, mMovie);
        super.onSaveInstanceState(outState);
    }
}
