package com.anakinfoxe.popularmovies.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.anakinfoxe.popularmovies.DetailActivity;
import com.anakinfoxe.popularmovies.DetailFragment;
import com.anakinfoxe.popularmovies.R;
import com.anakinfoxe.popularmovies.model.Movie;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by xing on 1/18/16.
 */
public class PosterAdapter extends RecyclerView.Adapter<PosterAdapter.ViewHolder> {

    private static final String LOG_TAG = PosterAdapter.class.getSimpleName();

    private List<Movie> mMovies = new ArrayList<>();

    private Context mContext;

    public PosterAdapter(Context c) {
        this.mContext = c;

        this.mMovies.clear();
    }


    // view holder class
    public static class ViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.drawee_poster) SimpleDraweeView drawee;

        public ViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }

        public SimpleDraweeView getDrawee() {
            return this.drawee;
        }
    }

    public List<Movie> getMovies() {
        return this.mMovies;
    }

    public void setMovies(List<Movie> movies) {
        this.mMovies = movies;

        notifyDataSetChanged();
    }

    public void addMovies(List<Movie> movies) {
        this.mMovies.addAll(movies);

        notifyDataSetChanged();
    }


    @Override
    public PosterAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                                .inflate(R.layout.poster_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final PosterAdapter.ViewHolder holder, int position) {
        loadImage2View(position, holder.getDrawee());

        final Movie movie = (position < mMovies.size()) ?
                mMovies.get(position) : null;

        holder.getDrawee().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // send intent to start DetailActivity
                if (movie != null) {
                    Intent intent = new Intent(mContext, DetailActivity.class);

                    intent.putExtra(DetailFragment.MOVIE_OBJECT, movie);

                    mContext.startActivity(intent);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mMovies.size();
    }


    private void loadImage2View(int position, SimpleDraweeView drawee) {
        if (position >= mMovies.size())
            return;

        Uri uri = mMovies.get(position).getPosterPath();
        drawee.setImageURI(uri);
    }
}
