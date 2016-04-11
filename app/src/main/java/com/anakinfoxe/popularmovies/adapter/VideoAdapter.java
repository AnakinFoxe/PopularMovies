package com.anakinfoxe.popularmovies.adapter;

import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.anakinfoxe.popularmovies.R;
import com.anakinfoxe.popularmovies.model.Video;
import com.anakinfoxe.popularmovies.util.Utility;
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
        loadImage2View(position, holder.getDrawee());

        final Video video = (position < mVideos.size()) ?
                mVideos.get(position) : null;

        holder.getDrawee().setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // send intent to start YouTube app
                if (video != null) {

                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mVideos.size();
    }

    private void loadImage2View(int position, SimpleDraweeView drawee) {
        if (position >= mVideos.size())
            return;

        Uri uri = getYouTubeThumbnail(mVideos.get(position).getKey());
        drawee.setImageURI(uri);
    }

    private Uri getYouTubeThumbnail(String key) {
        return Uri.parse("http://img.youtube.com/vi/" + key + "/0.jpg");
    }
}
