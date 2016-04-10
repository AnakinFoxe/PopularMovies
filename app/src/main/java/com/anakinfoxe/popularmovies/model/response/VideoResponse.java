package com.anakinfoxe.popularmovies.model.response;

import com.anakinfoxe.popularmovies.model.Video;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by xing on 4/8/16.
 */
public class VideoResponse {

    @SerializedName("id")
    @Expose
    private long id;

    @SerializedName("results")
    @Expose
    private List<Video> videos;

    public VideoResponse() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public List<Video> getVideos() {
        return videos;
    }

    public void setVideos(List<Video> videos) {
        this.videos = videos;
    }
}
