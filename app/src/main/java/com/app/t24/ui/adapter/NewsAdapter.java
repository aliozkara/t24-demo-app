package com.app.t24.ui.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.t24.R;
import com.app.t24.model.entity.NewsCategoriesItem;
import com.app.t24.model.entity.NewsItem;
import com.app.t24.ui.activity.NewsContentActivity;
import com.app.t24.utils.PositionListener;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by alican on 16.04.2017.
 */

public class NewsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_NEWS = 0;
    private static final int TYPE_CATEGORIES_NEWS = 1;

    private Activity context;
    private List<Object> mList;
    private PositionListener positionListener = null;

    public NewsAdapter(Activity activity, List<Object> itemList, @Nullable PositionListener positionListener) {
        super();
        setHasStableIds(true);
        this.context = activity;
        this.mList = itemList;
        this.positionListener = positionListener;
    }

    @Override
    public int getItemViewType(int position) {

        if(positionListener != null){
            if(position>9){
                positionListener.onState(9);
            }
            else {
                positionListener.onState(position);
            }
        }

        if (mList.get(position) instanceof NewsItem) {
            return TYPE_NEWS;
        } else if (mList.get(position) instanceof NewsCategoriesItem) {
            return TYPE_CATEGORIES_NEWS;
        }

        return -1;

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());

        switch (viewType) {
            case TYPE_NEWS:
                View v1 = inflater.inflate(R.layout.activity_home_list_item, viewGroup, false);
                viewHolder = new NewsViewHolder(v1);
                break;
            case TYPE_CATEGORIES_NEWS:
                View v2 = inflater.inflate(R.layout.activity_news_content_list_item, viewGroup, false);
                viewHolder = new NewsViewHolder(v2);
                break;
        }

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        switch (viewHolder.getItemViewType()) {
            case TYPE_NEWS:
                NewsViewHolder vh1 = (NewsViewHolder) viewHolder;
                configureViewHolder1(vh1, position);
                break;
            case TYPE_CATEGORIES_NEWS:
                NewsViewHolder vh2 = (NewsViewHolder) viewHolder;
                configureViewHolder2(vh2, position);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private void configureViewHolder1(final NewsViewHolder vh, final int position) {

        final NewsItem item = (NewsItem) mList.get(position);

        Picasso.with(context).load("http://"+item.getPostImage()).into(vh.activity_home_iv_image);
        vh.activity_home_tv_title.setText(item.getPostTitle());

        vh.activity_home_cv_wrapper.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(context, NewsContentActivity.class);
                myIntent.putExtra("newsId", item.getPostId());
                context.startActivity(myIntent);
            }
        });

    }

    private void configureViewHolder2(final NewsViewHolder vh, final int position) {

        final NewsCategoriesItem item = (NewsCategoriesItem) mList.get(position);

        Picasso.with(context).load("http://"+item.getPostImage()).into(vh.activity_home_iv_image);
        vh.activity_home_tv_title.setText(item.getPostTitle());

        vh.activity_home_cv_wrapper.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(context, NewsContentActivity.class);
                myIntent.putExtra("newsId", item.getPostId());
                context.startActivity(myIntent);
            }
        });

    }

    public class NewsViewHolder extends RecyclerView.ViewHolder {

        public TextView activity_home_tv_title;
        public ImageView activity_home_iv_image;
        public CardView activity_home_cv_wrapper;

        public NewsViewHolder(View view) {
            super(view);
            activity_home_tv_title = (TextView) view.findViewById(R.id.activity_home_tv_title);
            activity_home_iv_image = (ImageView) view.findViewById(R.id.activity_home_iv_image);
            activity_home_cv_wrapper = (CardView) view.findViewById(R.id.activity_home_cv_wrapper);
        }

    }

}
