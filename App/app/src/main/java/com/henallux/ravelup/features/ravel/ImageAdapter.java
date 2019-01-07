package com.henallux.ravelup.features.ravel;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.henallux.ravelup.R;

import java.util.ArrayList;

public class ImageAdapter extends PagerAdapter {
    private Context context;
    private LayoutInflater layoutInflater;
    private ArrayList<String> mResources;

    ImageAdapter(Context context, ArrayList<String> mResources) {
        this.context = context;
        this.mResources = mResources;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return mResources.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view == o;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View itemView = layoutInflater.inflate(R.layout.slide, container, false);

        final ImageView ivPhoto = itemView.findViewById(R.id.imgSlider);

        if (!mResources.get(position).equals("")){
            Glide.with(context)
                    .load(mResources.get(position).trim())
                    .into(ivPhoto);
        }

        container.addView(itemView);

        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((LinearLayout) object);
    }
}
