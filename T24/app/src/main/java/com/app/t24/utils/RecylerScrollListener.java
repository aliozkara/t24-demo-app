package com.app.t24.utils;

import android.support.v7.widget.RecyclerView;

/**
 * Created by alican on 20.01.2017.
 */

public abstract class RecylerScrollListener extends RecyclerView.OnScrollListener {

    public RecylerScrollListener() {}


    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        super.onScrollStateChanged(recyclerView, newState);

    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);

        if (!recyclerView.canScrollVertically(3)) {
            onLoadMore();
        }

    }

    public abstract void onLoadMore();

}
