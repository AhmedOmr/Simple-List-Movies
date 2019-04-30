package com.mecodroid.movies_ytsapi.Views;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

abstract class PaginationScrollListener extends RecyclerView.OnScrollListener {
    // The total number of items in the dataset after the last load
    private int previousTotal = 0;
    // True if we are still waiting for the last set of data to load.
     private boolean loading = true;
    // The minimum amount of items to have below your current scroll position before loading more.
     private int visibleThreshold = 15;
    int firstVisibleItem, visibleItemCount, totalItemCount;

    private int current_page = 1;

    private GridLayoutManager mGridLayoutManager;

    public PaginationScrollListener (GridLayoutManager gridLayoutManager) {
        this.mGridLayoutManager = gridLayoutManager;
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);

        visibleItemCount = recyclerView.getChildCount();
        totalItemCount = mGridLayoutManager.getItemCount();
        firstVisibleItem = mGridLayoutManager.findFirstVisibleItemPosition();

        if (loading) {
            if (totalItemCount > previousTotal) {
                loading = false;
                previousTotal = totalItemCount;
            }
        }
        if (!loading && (totalItemCount - visibleItemCount)
                <= (firstVisibleItem + visibleThreshold)) {
            // End has been reached

            // Do something
            current_page++;

            onLoadMore(current_page);

            loading = true;
        }
    }

    public abstract void onLoadMore(int current_page);

}