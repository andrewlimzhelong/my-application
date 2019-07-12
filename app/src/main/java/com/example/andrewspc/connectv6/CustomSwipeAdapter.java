package com.example.andrewspc.connectv6;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;


// YOUTUBE LINK: https://www.youtube.com/watch?v=byLKoPgB7yA&t=645s

public class CustomSwipeAdapter extends PagerAdapter {

    Context context;
    LayoutInflater layoutInflater;

    public CustomSwipeAdapter(Context context)
    {
        this.context = context;
    }

    public int[] slide_images = {
            R.drawable.slidercleaningservice, R.drawable.sliderhealthydog,
            R.drawable.sliderservice, R.drawable.sliderservice
    };

    public String[] slide_headings = {

        "image 1",
        "image 2",
        "image 3",
        "image 4"
    };

    @Override
    public int getCount() {
        return slide_headings.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {

        layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View item_view = layoutInflater.inflate(R.layout.swipe_layout, container, false);

        ImageView imageView = item_view.findViewById(R.id.swiping_image_view);
        imageView.setImageResource(slide_images[position]);
        container.addView(item_view);

        return item_view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((LinearLayout)object);
    }

}
