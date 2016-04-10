package com.anakinfoxe.popularmovies;

import android.content.Intent;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import com.anakinfoxe.popularmovies.model.Movie;
import com.facebook.drawee.view.SimpleDraweeView;

public class DetailActivity extends AppCompatActivity {

    private SimpleDraweeView mBackdropView;

    private Movie mMovie = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_activity);

        mBackdropView = ((SimpleDraweeView) findViewById(R.id.drawee_backdrop));

        // get movie object
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra(DetailFragment.MOVIE_OBJECT))
            mMovie = intent.getExtras().getParcelable(DetailFragment.MOVIE_OBJECT);

        if (mMovie != null) {
            // set backdrop
            mBackdropView.setImageURI(mMovie.getBackdropPath());

            Bundle args = new Bundle();
            args.putParcelable(DetailFragment.MOVIE_OBJECT, mMovie);

            Fragment fragment = new DetailFragment();
            fragment.setArguments(args);

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container_detail, fragment)
                    .commit();
        }
    }
}
