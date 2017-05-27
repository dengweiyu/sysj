package com.li.videoapplication.ui.fragment;


import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.google.common.collect.Lists;
import com.handmark.pulltorefresh.library.IPullToRefresh;
import com.li.videoapplication.R;
import com.li.videoapplication.framework.TBaseFragment;
import com.li.videoapplication.tools.UmengAnalyticsHelper;
import com.li.videoapplication.ui.activity.VideoPlayActivity;
import com.li.videoapplication.ui.adapter.PlayGiftChoiceAdapter;
import com.li.videoapplication.ui.dialog.PlayGiftDialog;

/**
 * 打赏页面 礼物选择
 */

public class VideoPlayGiftFragment extends TBaseFragment {
    private RecyclerView mGift;
    private PlayGiftChoiceAdapter mAdapter;
    private BottomSheetBehavior mSheetBehavior;
    private View mRoot;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof VideoPlayActivity){
            mActivity = (VideoPlayActivity)activity;
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            //UmengAnalyticsHelper.onEvent(getActivity(), UmengAnalyticsHelper.VIDEOPLAY, "视频播放页-打赏页面");
        }
    }

    @Override
    protected int getCreateView() {
        return R.layout.fragment_play_gift;
    }

    @Override
    protected void initContentView(View view) {
        mRoot = view;

        mGift = (RecyclerView)view.findViewById(R.id.rv_play_gift);
        mAdapter = new PlayGiftChoiceAdapter(Lists.newArrayList(1,2,3,4,5,6,7,8));
        mGift.setLayoutManager(new GridLayoutManager(getContext(),4));
        mGift.setAdapter(mAdapter);
        mGift.addOnItemTouchListener(new OnItemClickListener(){
            @Override
            public void SimpleOnItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                new PlayGiftDialog(getContext(),i).show();
            }
        });


        mSheetBehavior = BottomSheetBehavior.from(view.findViewById(R.id.ab_sheet_behavior_layout));
        mSheetBehavior.setBottomSheetCallback(mCallback);
    }

    @Override
    protected IPullToRefresh getPullToRefresh() {
        return null;
    }

    public View getRootView(){
        return mRoot;
    }

    public void showContent(){

        mSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
    }

    public void hideContent(){

        mSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
    }

    private VideoPlayActivity mActivity;

    final BottomSheetBehavior.BottomSheetCallback mCallback = new BottomSheetBehavior.BottomSheetCallback() {
        @Override
        public void onStateChanged(@NonNull View bottomSheet, int newState) {

            Log.e("newState",newState+"");
            if (newState == BottomSheetBehavior.STATE_COLLAPSED){
                if (mActivity != null){
                    mActivity.refreshState(false);
                }
            } else if (newState == BottomSheetBehavior.STATE_EXPANDED) {
                if (mActivity != null){
                    mActivity.refreshState(true);
                }
            }
        }

        @Override
        public void onSlide(@NonNull View bottomSheet, float slideOffset) {
            Log.e("slideOffset",slideOffset+"");
        }
    };


}
