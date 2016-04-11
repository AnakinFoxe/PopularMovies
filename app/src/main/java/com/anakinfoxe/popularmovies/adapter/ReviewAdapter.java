package com.anakinfoxe.popularmovies.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.anakinfoxe.popularmovies.R;
import com.anakinfoxe.popularmovies.model.Review;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by xing on 4/10/16.
 */
public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ViewHolder> {

    private static final String LOG_TAG = ReviewAdapter.class.getSimpleName();

    private List<Review> mReviews = new ArrayList<>();

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public boolean isExpanded = false;

        @Bind(R.id.textview_review_author) TextView author;
        @Bind(R.id.textview_review_content) TextView content;

        public ViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }

        public TextView getAuthor() {
            return author;
        }

        public TextView getContent() {
            return content;
        }
    }

    public List<Review> getReviews() {
        return mReviews;
    }

    public void setReviews(List<Review> reviews) {
        this.mReviews = reviews;

        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.review_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        if (position >= mReviews.size())
            return;

        final Review review = mReviews.get(position);
        holder.getAuthor().setText(review.getAuthor());
        holder.getContent().setText(review.getContent());
        holder.getContent().setMaxLines(5);

        holder.getContent().setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                holder.isExpanded = !holder.isExpanded;

                if (holder.isExpanded)
                    holder.getContent().setMaxLines(Integer.MAX_VALUE);
                else
                    holder.getContent().setMaxLines(5);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mReviews.size();
    }
}
