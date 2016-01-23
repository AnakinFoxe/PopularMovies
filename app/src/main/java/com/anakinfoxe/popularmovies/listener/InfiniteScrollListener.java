package com.anakinfoxe.popularmovies.listener;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

/**
 * Inspired by
 * https://guides.codepath.com/android/Endless-Scrolling-with-AdapterViews-and-RecyclerView
 * 
 * Created by xing on 1/23/16.
 */
public abstract class InfiniteScrollListener extends RecyclerView.OnScrollListener {

    private int visibleThreshold = 5;

    private int startPage = 0;
    private int currentPage = 0;

    private int previousTotalItemCount = 0;

    private boolean isLoading = true;

    private RecyclerView.LayoutManager mLayoutManager;


    public InfiniteScrollListener(GridLayoutManager layoutManager, int startPage) {
        this.mLayoutManager = layoutManager;
        this.startPage = startPage;
    }

    public InfiniteScrollListener(LinearLayoutManager layoutManager, int startPage) {
        this.mLayoutManager = layoutManager;
        this.startPage = startPage;
    }


    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        int lastVisibleItemPosition = 0;
        int totalItemCount = mLayoutManager.getItemCount();

        if (mLayoutManager instanceof GridLayoutManager) {
            lastVisibleItemPosition = ((GridLayoutManager) mLayoutManager)
                    .findLastVisibleItemPosition();

            int visible = mLayoutManager.getChildCount();
            Log.v("INFINITE", visible + ", " + totalItemCount);
        } else if (mLayoutManager instanceof LinearLayoutManager)
            lastVisibleItemPosition = ((LinearLayoutManager) mLayoutManager)
                    .findLastVisibleItemPosition();

        if (totalItemCount < previousTotalItemCount) {
            this.currentPage = this.startPage;
            this.previousTotalItemCount = totalItemCount;
            if (totalItemCount == 0)
                this.isLoading = true;
        }

        if (isLoading && (totalItemCount > previousTotalItemCount)) {
            isLoading = false;
            previousTotalItemCount = totalItemCount;
        }

        if (!isLoading && (lastVisibleItemPosition + visibleThreshold) >= totalItemCount) {
            onLoadMore(++currentPage, totalItemCount);
            isLoading = true;
        }
    }

    public abstract void onLoadMore(int page, int totalItemCount);


}
