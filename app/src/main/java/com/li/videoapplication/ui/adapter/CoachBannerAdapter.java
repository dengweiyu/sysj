package com.li.videoapplication.ui.adapter;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.li.videoapplication.R;
import com.li.videoapplication.data.image.GlideHelper;
import com.li.videoapplication.framework.BaseBaseAdapter;
import com.li.videoapplication.ui.activity.ImageDetailSceneActivity;
import com.li.videoapplication.ui.activity.ImageViewPagerActivity;
import com.li.videoapplication.views.ViewFlow;

import java.util.List;

/**
 * banner 图
 */

public class CoachBannerAdapter extends BaseBaseAdapter {
    private Context mContext;
    private List<String> mUrls;
    private ViewFlow   mBannerFlow;
    public CoachBannerAdapter(Context context, List<String> urls, ViewFlow viewFlow) {
        mContext = context;
        mUrls = urls;
        mBannerFlow = viewFlow;
    }

    @Override
    protected Context getContext() {
        return mContext;
    }

    @Override
    public int getCount() {
        return mUrls == null?0:mUrls.size();
    }

    @Override
    public Object getItem(int position) {
        return mUrls == null?null:mUrls.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view;

        view = LayoutInflater.from(mContext).inflate(R.layout.adapter_home_banner, null);

        final ImageView icon = (ImageView) view.findViewById(R.id.banner_pic);
        GlideHelper.displayImage(mContext,mUrls.get(position),icon);
        icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mContext instanceof Activity){
                    ImageDetailSceneActivity.startImageDetailActivity((Activity)mContext,icon,position,mUrls.toArray(new String[]{}),false);
                }
            }
        });

        return view;
    }


}
