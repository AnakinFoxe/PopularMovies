package com.anakinfoxe.popularmovies.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.anakinfoxe.popularmovies.R;
import com.anakinfoxe.popularmovies.model.Video;
import com.anakinfoxe.popularmovies.util.Helper;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by xing on 4/10/16.
 */
public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.ViewHolder>{

    private static final String LOG_TAG = VideoAdapter.class.getSimpleName();

    private List<Video> mVideos = new ArrayList<>();

    private Context mContext;

    public VideoAdapter(Context c) {
        this.mContext = c;

        this.mVideos.clear();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.drawee_video) SimpleDraweeView drawee;

        public ViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }

        public SimpleDraweeView getDrawee() {
            return this.drawee;
        }
    }

    public List<Video> getVideos() {
        return mVideos;
    }

    public void setVideos(List<Video> videos) {
        this.mVideos = videos;

        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.video_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (position >= mVideos.size())
            return;

        final Video video = mVideos.get(position);

        loadImage2View(video, holder.getDrawee());

        holder.getDrawee().setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // send intent to start YouTube app
                Uri uri = Uri.parse(Helper.getYoutubeVideoUrl(video));
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mVideos.size();
    }

    private void loadImage2View(Video video, SimpleDraweeView drawee) {
        Uri uri = Uri.parse(Helper.getYoutubeThumbnailUrl(video));
        drawee.setImageURI(uri);
    }
}
