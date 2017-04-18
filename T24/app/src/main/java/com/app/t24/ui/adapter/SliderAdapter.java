package com.app.t24.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.t24.R;
import com.app.t24.ui.activity.NewsContentActivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by alican on 15.04.2017.
 */

public class SliderAdapter extends PagerAdapter {

    private ArrayList<HashMap<String,String>> data;
    private LayoutInflater inflater;
    private Context context;

    public SliderAdapter(Context context, ArrayList<HashMap<String,String>> data) {
        this.context = context;
        this.data = data;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object instantiateItem(ViewGroup view, final int position) {
        View myImageLayout = inflater.inflate(R.layout.activity_home_slide_layout, view, false);

        ImageView myImage = (ImageView) myImageLayout.findViewById(R.id.slide_layout_iw_view);
        TextView title = (TextView) myImageLayout.findViewById(R.id.slide_layout_tw_title);

        title.setText(data.get(position).get("title"));
        Picasso.with(context).load("http://"+data.get(position).get("image")).into(myImage);

        myImage.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(context, NewsContentActivity.class);
                myIntent.putExtra("newsId", data.get(position).get("id"));
                context.startActivity(myIntent);
            }
        });

        view.addView(myImageLayout);

        return myImageLayout;

    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }




}