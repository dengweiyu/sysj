package com.li.videoapplication.ui.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.handmark.pulltorefresh.library.IPullToRefresh;
import com.li.videoapplication.R;
import com.li.videoapplication.data.image.GlideHelper;
import com.li.videoapplication.data.model.event.WelcomeScrollEvent;
import com.li.videoapplication.framework.TBaseFragment;
import com.li.videoapplication.utils.StringUtil;

import io.rong.eventbus.EventBus;

/**
 *
 */

public class WelcomeImageFragment extends TBaseFragment implements View.OnClickListener{

    public static WelcomeImageFragment newInstance(int resId){
        WelcomeImageFragment fragment = new WelcomeImageFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("res_id",resId);
        fragment.setArguments(bundle);
        return fragment;
    }

    public static WelcomeImageFragment newInstance(String path){
        WelcomeImageFragment fragment = new WelcomeImageFragment();
        Bundle bundle = new Bundle();
        bundle.putString("res_path",path);
        fragment.setArguments(bundle);
        return fragment;
    }


    @Override
    protected int getCreateView() {
        return R.layout.fragment_image;
    }

    @Override
    protected void initContentView(View view) {
        Bundle bundle = getArguments();
        if (bundle != null){
            int resId = bundle.getInt("res_id",0);
            String path = bundle.getString("res_path","");
            ImageView image = (ImageView) view.findViewById(R.id.iv_detail);
            if (resId != 0){
                GlideHelper.displayImage(getActivity(),resId,image);
            }
            if (!StringUtil.isNull(path)){
                GlideHelper.displayImage(getActivity(),path,image);
            }
        }

        view.findViewById(R.id.welcome_go_to).setOnClickListener(this);
    }

    @Override
    protected IPullToRefresh getPullToRefresh() {
        return null;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.welcome_go_to:
                EventBus.getDefault().post(new WelcomeScrollEvent(1));
                break;
        }
    }
}
