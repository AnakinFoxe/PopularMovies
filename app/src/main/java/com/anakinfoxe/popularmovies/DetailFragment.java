package com.anakinfoxe.popularmovies;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.ShareActionProvider;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.anakinfoxe.popularmovies.model.Movie;
import com.anakinfoxe.popularmovies.model.response.ReviewResponse;
import com.anakinfoxe.popularmovies.model.response.VideoResponse;
import com.anakinfoxe.popularmovies.service.ServiceManager;
import com.facebook.drawee.view.SimpleDraweeView;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by xing on 1/18/16.
 */
public class DetailFragment extends Fragment {

    private static final String LOG_TAG = DetailFragment.class.getSimpleName();

    public static final String MOVIE_OBJECT = "movieObject";

    private Movie mMovie = null;

    private SimpleDateFormat sdf = new SimpleDateFormat("MMMM d, yyyy", new Locale("en"));
    private DecimalFormat df = new DecimalFormat("#.#");

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        df.setRoundingMode(RoundingMode.HALF_EVEN);

        // this fragment will have option menu
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.detail_fragment, container, false);


        // obtain movie object
        Bundle args = getArguments();
        if (args != null)
            mMovie = args.getParcelable(MOVIE_OBJECT);


        // set data to view
        if (mMovie != null) {
            showMoviePrimaryInfo(rootView, mMovie);

            fetchVideos(mMovie.getId());
            fetchReviews(mMovie.getId());
        }

        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_detail, menu);

        MenuItem share = menu.findItem(R.id.action_share);
        ShareActionProvider shareAP =
                (ShareActionProvider) MenuItemCompat.getActionProvider(share);

        if (shareAP != null)
            shareAP.setShareIntent(createShareDetailIntent(mMovie));
        else
            Log.d(LOG_TAG, "Share Action Provider is null?");
    }


    private void showMoviePrimaryInfo(View rootView, Movie movie) {
//        ((SimpleDraweeView) rootView.findViewById(R.id.drawee_backdrop))
//                .setImageURI(movie.getBackdropPath());

        ((SimpleDraweeView) rootView.findViewById(R.id.drawee_poster))
                .setImageURI(movie.getPosterPath());

        ((TextView) rootView.findViewById(R.id.textview_title))
                .setText(movie.getTitle().toUpperCase());

        ((TextView) rootView.findViewById(R.id.textview_overview))
                .setText(movie.getOverview());

        ((TextView) rootView.findViewById(R.id.textview_released_date))
                .setText(sdf.format(movie.getReleaseDate()));

        // show different size of text
        String text = df.format(movie.getVoteAverage()) + "/10";
        SpannableString ss = new SpannableString(text);
        int textSize = getResources().getDimensionPixelSize(R.dimen.vote_average_text_size);
        ss.setSpan(new AbsoluteSizeSpan(textSize),
                0, text.length() - 3, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);

        ((TextView) rootView.findViewById(R.id.textview_vote_average))
                .setText(ss);
    }


    private Intent createShareDetailIntent(Movie movie) {
        // compose content text
        StringBuilder sb = new StringBuilder();
        sb.append("Check out movie: ")
                .append(movie.getOriginalTitle())
                .append(" (rating ")
                .append(df.format(movie.getVoteAverage()))
                .append(") http://www.themoviedb.org/movie/")
                .append(movie.getId());

        // send intent
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, sb.toString());

        return shareIntent;
    }

    private void fetchVideos(long id) {
        Call<VideoResponse> response = ServiceManager.getDetailService()
                                        .getMovieVideos(id);
        response.enqueue(new Callback<VideoResponse>() {
            @Override
            public void onResponse(Response<VideoResponse> response) {
                VideoResponse resp = response.body();
                Log.d(LOG_TAG, "fetched videos: " + resp.getVideos().size());
            }

            @Override
            public void onFailure(Throwable t) {
                Log.e(LOG_TAG, "getting videos error ", t);
            }
        });
    }

    private void fetchReviews(long id) {
        Call<ReviewResponse> response = ServiceManager.getDetailService()
                .getMovieReviews(id);
        response.enqueue(new Callback<ReviewResponse>() {
            @Override
            public void onResponse(Response<ReviewResponse> response) {
                ReviewResponse resp = response.body();
                Log.d(LOG_TAG, "fetched reviews: " + resp.getReviews().size());
            }

            @Override
            public void onFailure(Throwable t) {
                Log.e(LOG_TAG, "getting reviews error ", t);
            }
        });
    }
}
