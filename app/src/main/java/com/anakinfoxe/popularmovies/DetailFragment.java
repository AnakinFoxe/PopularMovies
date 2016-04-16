package com.anakinfoxe.popularmovies;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.ShareActionProvider;
import android.support.v7.widget.Toolbar;
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

import com.anakinfoxe.popularmovies.adapter.ReviewAdapter;
import com.anakinfoxe.popularmovies.adapter.VideoAdapter;
import com.anakinfoxe.popularmovies.model.Movie;
import com.anakinfoxe.popularmovies.model.Review;
import com.anakinfoxe.popularmovies.model.Video;
import com.anakinfoxe.popularmovies.model.response.ReviewResponse;
import com.anakinfoxe.popularmovies.model.response.VideoResponse;
import com.anakinfoxe.popularmovies.service.ServiceManager;
import com.anakinfoxe.popularmovies.util.Helper;
import com.facebook.drawee.view.SimpleDraweeView;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by xing on 1/18/16.
 */
public class DetailFragment extends Fragment {

    private static final String LOG_TAG = DetailFragment.class.getSimpleName();

    public static final String MOVIE_OBJECT     = "movieObject";
    private static final String VIDEO_OBJECTS   = "videoObjects";
    private static final String REVIEW_OBJECTS  = "reviewObjects";

    private Movie mMovie = null;
    private List<Video> mVideos = null;
    private List<Review> mReviews = null;

    private SimpleDateFormat sdf = new SimpleDateFormat("MMMM d, yyyy", new Locale("en"));
    private DecimalFormat df = new DecimalFormat("#.#");

    private VideoAdapter mVideoAdapter;
    private ReviewAdapter mReviewAdapter;

    private ShareActionProvider mShareAP;

    @Bind(R.id.drawee_poster) SimpleDraweeView mPosterView;
    @Bind(R.id.textview_title) TextView mTitleView;
    @Bind(R.id.textview_overview) TextView mOverviewView;
    @Bind(R.id.textview_released_date) TextView mRelDateView;
    @Bind(R.id.textview_vote_average) TextView mVoteAvgView;
    @Bind(R.id.recyclerview_videos) RecyclerView mRvVideos;
    @Bind(R.id.recyclerview_reviews) RecyclerView mRvReviews;
    @Bind(R.id.toolbar) Toolbar mToolbar;
    @Bind(R.id.drawee_backdrop) SimpleDraweeView mBackdropView;

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

        ButterKnife.bind(this, rootView);

        // bind actionbar to the toolbar with collapsing toolbar
        mToolbar.setTitle(" "); // I don't want to show anything on toolbar
        ((AppCompatActivity)getActivity()).setSupportActionBar(mToolbar);

        // set layout manager and adapter for videos recycler view
        RecyclerView.LayoutManager lmVideos = new LinearLayoutManager(mRvVideos.getContext(),
                LinearLayoutManager.HORIZONTAL, false);
        mRvVideos.setLayoutManager(lmVideos);
        mVideoAdapter = new VideoAdapter(mRvReviews.getContext());
        mRvVideos.setAdapter(mVideoAdapter);

        // set layout manager and adapter for reviews recycler view
        RecyclerView.LayoutManager lmReviews = new LinearLayoutManager(mRvReviews.getContext(),
                LinearLayoutManager.VERTICAL, false);
        mRvReviews.setLayoutManager(lmReviews);
        mReviewAdapter = new ReviewAdapter();
        mRvReviews.setAdapter(mReviewAdapter);

        if (savedInstanceState == null) {
            Bundle args = getArguments();
            if (args != null)
                mMovie = args.getParcelable(MOVIE_OBJECT);
            else {
                // obtain movie object from intent
                Intent intent = getActivity().getIntent();
                if (intent != null && intent.hasExtra(MOVIE_OBJECT))
                    mMovie = intent.getExtras().getParcelable(MOVIE_OBJECT);
            }
        } else
            mMovie = savedInstanceState.getParcelable(DetailFragment.MOVIE_OBJECT);

        // set data to view
        if (mMovie != null) {
            // set backdrop
            mBackdropView.setImageURI(mMovie.getBackdropPath());

            showMoviePrimaryInfo(mMovie);

            if (savedInstanceState == null) {
                fetchVideos(mMovie.getId());
                fetchReviews(mMovie.getId());
            } else {
                mVideos = savedInstanceState.getParcelableArrayList(VIDEO_OBJECTS);
                mVideoAdapter.setVideos(mVideos);

                mReviews = savedInstanceState.getParcelableArrayList(REVIEW_OBJECTS);
                mReviewAdapter.setReviews(mReviews);
            }

        }

        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_detail, menu);

        MenuItem share = menu.findItem(R.id.action_share);
        mShareAP = (ShareActionProvider) MenuItemCompat
                .getActionProvider(share);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelable(MOVIE_OBJECT, mMovie);
        outState.putParcelableArrayList(VIDEO_OBJECTS,
                new ArrayList<Parcelable>(mVideos));
        outState.putParcelableArrayList(REVIEW_OBJECTS,
                new ArrayList<Parcelable>(mReviews));
        super.onSaveInstanceState(outState);
    }

    private void showMoviePrimaryInfo(Movie movie) {
        mPosterView.setImageURI(movie.getPosterPath());
        mTitleView.setText(movie.getTitle().toUpperCase());
        mOverviewView.setText(movie.getOverview());
        mRelDateView.setText(sdf.format(movie.getReleaseDate()));

        // show different size of text
        String text = df.format(movie.getVoteAverage()) + "/10";
        SpannableString ss = new SpannableString(text);
        int textSize = getResources().getDimensionPixelSize(R.dimen.vote_average_text_size);
        ss.setSpan(new AbsoluteSizeSpan(textSize),
                0, text.length() - 3, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);

        mVoteAvgView.setText(ss);
    }


    private void createShareDetailIntent(Movie movie, List<Video> videos) {
        StringBuilder sb = new StringBuilder();

        // compose content text
        String title = movie.getTitle();
        if (videos != null && videos.size() > 0)
            sb.append("Check out movie trailer: ")
                    .append(title)
                    .append(" (rating ")
                    .append(df.format(movie.getVoteAverage()))
                    .append(") ")
                    .append(Helper.getYoutubeVideoUrl(videos.get(0)));
        else
            sb.append("Check out movie: ")
                    .append(title)
                    .append(" (rating ")
                    .append(df.format(movie.getVoteAverage()))
                    .append(") ")
                    .append(Helper.getTmdbMovieUrl(movie));


        // create intent
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            shareIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT);
        else
            shareIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, sb.toString());

        // reset share intent to share action provider
        if (mShareAP != null)
            mShareAP.setShareIntent(shareIntent);
        else
            Log.d(LOG_TAG, "Share Action Provider is null?");
    }

    private void fetchVideos(long id) {
        Call<VideoResponse> response = ServiceManager.getDetailService()
                                        .getMovieVideos(id);
        response.enqueue(new Callback<VideoResponse>() {
            @Override
            public void onResponse(Response<VideoResponse> response) {
                VideoResponse resp = response.body();
                Log.d(LOG_TAG, "fetched videos: " + resp.getVideos().size());

                mVideos = resp.getVideos();

                mVideoAdapter.setVideos(mVideos);

                createShareDetailIntent(mMovie, mVideos);
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

                mReviews = resp.getReviews();

                mReviewAdapter.setReviews(mReviews);
            }

            @Override
            public void onFailure(Throwable t) {
                Log.e(LOG_TAG, "getting reviews error ", t);
            }
        });
    }
}
