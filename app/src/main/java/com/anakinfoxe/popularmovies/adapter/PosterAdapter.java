package com.anakinfoxe.popularmovies.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.anakinfoxe.popularmovies.DetailActivity;
import com.anakinfoxe.popularmovies.DetailFragment;
import com.anakinfoxe.popularmovies.R;
import com.anakinfoxe.popularmovies.model.Movie;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

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

        public final ImageView mImageView;

        public ViewHolder(View itemView) {
            super(itemView);

            mImageView = (ImageView) itemView.findViewById(R.id.imageview_poster);
        }
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
    public void onBindViewHolder(PosterAdapter.ViewHolder holder, final int position) {
        loadImage2View(position, holder.mImageView);

        holder.mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (position >= mMovies.size())
                    return;

                Log.v(LOG_TAG, "position = " + position + " of total = " + mMovies.size());

                Movie movie = mMovies.get(position);
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


    private void loadImage2View(int position, ImageView imageView) {
        if (position >= mMovies.size())
            return;

        String url = mMovies.get(position).getPosterPath();
        Picasso.with(mContext).load(url).into(imageView);
    }
}
