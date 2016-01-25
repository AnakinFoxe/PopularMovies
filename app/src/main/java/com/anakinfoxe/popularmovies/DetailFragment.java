package com.anakinfoxe.popularmovies;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.anakinfoxe.popularmovies.async.AsyncResponse;
import com.anakinfoxe.popularmovies.async.MovieDetailTask;
import com.anakinfoxe.popularmovies.model.Movie;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;
import com.squareup.picasso.Picasso;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * Created by xing on 1/18/16.
 */
public class DetailFragment extends Fragment {

    private static final String LOG_TAG = DetailFragment.class.getSimpleName();

    public static final String MOVIE_OBJECT = "movieObject";

    private Movie movie = null;

    private SimpleDraweeView draweeBackdrop = null;
    private SimpleDraweeView draweePoster = null;
    private TextView textViewTitle = null;
    private TextView textViewOverview = null;
    private TextView textViewReleasedDate = null;
    private TextView textViewVoteAverage = null;

    private SimpleDateFormat sdf = new SimpleDateFormat("MMMM d, yyyy", new Locale("en"));
    private DecimalFormat df = new DecimalFormat("#.#");

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        df.setRoundingMode(RoundingMode.HALF_EVEN);

        // this fragment will have option menu
//        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.detail_fragment, container, false);

        Intent intent = getActivity().getIntent();
        if (intent != null) {
            if (movie == null)
                movie = intent.getExtras().getParcelable(MOVIE_OBJECT);

            draweeBackdrop = (SimpleDraweeView) rootView.findViewById(R.id.drawee_backdrop);
            draweePoster = (SimpleDraweeView) rootView.findViewById(R.id.drawee_poster);
            textViewTitle = (TextView) rootView.findViewById(R.id.textview_title);
            textViewOverview = (TextView) rootView.findViewById(R.id.textview_overview);
            textViewReleasedDate = (TextView) rootView.findViewById(R.id.textview_released_date);
            textViewVoteAverage = (TextView) rootView.findViewById(R.id.textview_vote_average);

            showMoviePrimaryInfo(movie);
        }

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();

//        if (movie != null)
//            updateMovieDetail(movie.getId());
    }


    private void showMoviePrimaryInfo(Movie movie) {
        if (draweeBackdrop != null)
            draweeBackdrop.setImageURI(movie.getBackdropPath());

        if (draweePoster != null)
            draweePoster.setImageURI(movie.getPosterPath());

        if (textViewTitle != null)
            textViewTitle.setText(movie.getTitle().toUpperCase());

        if (textViewOverview != null)
            textViewOverview.setText(movie.getOverview());

        if (textViewReleasedDate != null)
            textViewReleasedDate.setText(sdf.format(movie.getReleaseDate()));

        if (textViewVoteAverage != null)
            textViewVoteAverage.setText(df.format(movie.getVoteAverage()));
    }


//    private void updateMovieDetail(long movieId) {
//        MovieDetailTask task = new MovieDetailTask(new AsyncResponse<Movie>() {
//            @Override
//            public void processFinish(Movie output) {
//                movie = output;
//
//                if (draweeBackdrop != null)
//                    Picasso.with(getContext())
//                            .load(movie.getBackdropPath())
//                            .into(draweeBackdrop);
//
//                if (draweePoster != null)
//                    Picasso.with(getContext())
//                            .load(movie.getPosterPath())
//                            .into(draweePoster);
//
//                if (textViewTitle != null)
//                    textViewTitle.setText(movie.getTitle().toUpperCase());
//
//                if (textViewOverview != null)
//                    textViewOverview.setText(movie.getOverview());
//
//
//            }
//        });
//
//        task.execute(movieId);
//    }
}
