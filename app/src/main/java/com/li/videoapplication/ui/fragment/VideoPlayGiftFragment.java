package com.li.videoapplication.ui.fragment;


import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.IPullToRefresh;
import com.li.videoapplication.R;
import com.li.videoapplication.data.DataManager;
import com.li.videoapplication.data.cache.RequestCache;
import com.li.videoapplication.data.model.entity.Member;
import com.li.videoapplication.data.model.entity.NetworkError;
import com.li.videoapplication.data.model.event.InputNumberEvent;
import com.li.videoapplication.data.model.event.RefreshCommendEvent;
import com.li.videoapplication.data.model.event.UserInfomationEvent;
import com.li.videoapplication.data.model.response.PlayGiftResultEntity;
import com.li.videoapplication.data.model.response.PlayGiftTypeEntity;
import com.li.videoapplication.data.model.response.ServiceTimeEntity;
import com.li.videoapplication.data.model.response.TimeLineGiftEntity;
import com.li.videoapplication.data.network.RequestParams;
import com.li.videoapplication.data.network.RequestUrl;
import com.li.videoapplication.data.preferences.PreferencesHepler;
import com.li.videoapplication.framework.AppAccount;
import com.li.videoapplication.framework.TBaseFragment;
import com.li.videoapplication.tools.ToastHelper;
import com.li.videoapplication.tools.UmengAnalyticsHelper;
import com.li.videoapplication.ui.ActivityManager;
import com.li.videoapplication.ui.DialogManager;
import com.li.videoapplication.ui.activity.VideoPlayActivity;
import com.li.videoapplication.ui.adapter.PlayGiftChoiceAdapter;
import com.li.videoapplication.ui.dialog.GiftNumberInputDialog;
import com.li.videoapplication.ui.dialog.LoadingDialog;
import com.li.videoapplication.ui.dialog.PlayGiftDialog;
import com.li.videoapplication.ui.view.GiftItemDecoration;
import com.li.videoapplication.ui.view.SimpleItemDecoration;
import com.li.videoapplication.utils.StringUtil;

import org.apache.commons.io.filefilter.FalseFileFilter;

import java.util.ArrayList;
import java.util.List;

/**
 * 打赏页面 礼物选择
 */

public class VideoPlayGiftFragment extends TBaseFragment implements View.OnClickListener {
    private RecyclerView mGift;
    private PlayGiftDialog mSuccessDialog;
    private LoadingDialog mLoadingDialog;
    private PlayGiftChoiceAdapter mAdapter;
    private BottomSheetBehavior mSheetBehavior;
    private SimpleItemDecoration mDecoration;
    private View mRoot;
    private  GiftNumberInputDialog mInputDialog;
    private List<PlayGiftTypeEntity.DataBean> mData;
    private List<PlayGiftTypeEntity.NumberSenseBean> mNumberData;
    private TimeLineGiftEntity.DataBean mEntity;
    private String mVideoId;
    private TextView mCoin;
    private TextView mBeans;
    private TextView mNum;
    private int mNumber = 1;

    private long mServiceTime = 0;
    private boolean isPlaying;

    public static  VideoPlayGiftFragment newInstance(String videoId){
        VideoPlayGiftFragment fragment = new  VideoPlayGiftFragment();
        Bundle bundle = new Bundle();
        bundle.putString("video_id",videoId);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof VideoPlayActivity){
            mActivity = (VideoPlayActivity)activity;
        }
    }


    @Override
    protected int getCreateView() {
        return mActivity.isLandscape()?R.layout.fragment_play_gift_horizontal:R.layout.fragment_play_gift_vertical;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mInputDialog != null && mInputDialog.isShowing()){
            mInputDialog.dismiss();
        }
    }

    @Override
    protected void initContentView(View view) {

        Bundle bundle = getArguments();
        if (bundle != null){
            mVideoId = bundle.getString("video_id","");
        }
        //隐藏余额两字
        view.findViewById(R.id.tv_currency).setVisibility(View.GONE);

        mRoot = view;
        view.findViewById(R.id.tv_video_play_gift).setOnClickListener(this);
        view.findViewById(R.id.tv_video_play_recharge).setOnClickListener(this);
        mCoin = (TextView)view.findViewById(R.id.tv_my_currency_coin);
        mBeans =  (TextView)view.findViewById(R.id.tv_my_currency_beans);
        mNum =  (TextView)view.findViewById(R.id.tv_video_play_num);
        try {
            mCoin.setText(StringUtil.formatMoneyOnePoint(Float.parseFloat(getUser().getCoin())));
            mBeans.setText(StringUtil.formatMoney(Float.parseFloat(getUser().getCurrency())));
        } catch (Exception e) {
            e.printStackTrace();
        }

        mGift = (RecyclerView)view.findViewById(R.id.rv_play_gift);
        mData = new ArrayList<>();
        view.findViewById(R.id.ll_video_play_input).setOnClickListener(this);
        if (mActivity.isLandscape()){
            LinearLayoutManager manager = new LinearLayoutManager(mActivity);
            manager.setOrientation(LinearLayoutManager.HORIZONTAL);
            mAdapter = new PlayGiftChoiceAdapter(R.layout.video_play_gift_item_land,mData);
            mGift.setLayoutManager(manager);
            mDecoration = new GiftItemDecoration(getContext(),true);
        }else {
            mAdapter = new PlayGiftChoiceAdapter(R.layout.video_play_gift_item,mData);
            mGift.setLayoutManager(new GridLayoutManager(getContext(),4));
            mDecoration = new GiftItemDecoration(getContext(),false);
        }

        mGift.addItemDecoration(mDecoration);
        mGift.setAdapter(mAdapter);
        mGift.addOnItemTouchListener(mListener);
        mRoot.getViewTreeObserver().addOnGlobalLayoutListener(mLayoutListener);

        mSheetBehavior = BottomSheetBehavior.from(view.findViewById(R.id.ab_sheet_behavior_layout));
        mSheetBehavior.setBottomSheetCallback(mCallback);

        //load data
        DataManager.getGiftType();

        //
        mHandler.post(mSyncTask);

        setGiftByCache();
    }

    //use cache first
    private void setGiftByCache(){
        String data =  RequestCache.get(RequestUrl.getInstance().giftType(), RequestParams.getInstance().giftType());
        if (!StringUtil.isNull(data)){
            Gson gson = new Gson();
            try {
                PlayGiftTypeEntity entity = gson.fromJson(data,PlayGiftTypeEntity.class);
                if (entity != null){
                    mNumberData = entity.getNumberSense();
                    mAdapter.setNewData(entity.getData());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroy() {
        mHandler.removeCallbacks(mSyncTask);

        super.onDestroy();

    }

    @Override
    protected IPullToRefresh getPullToRefresh() {
        return null;
    }

    public View getRootView(){
        return mRoot;
    }

    public void showContent(){
        if (mRoot != null){
            mRoot.getViewTreeObserver().addOnGlobalLayoutListener(mLayoutListener);
        }
    }

    public void hideContent(){
        mSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);

    }

    private VideoPlayActivity mActivity;

    //
    final ViewTreeObserver.OnGlobalLayoutListener mLayoutListener = new ViewTreeObserver.OnGlobalLayoutListener() {
        @Override
        public void onGlobalLayout() {

            mSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            mGift.getViewTreeObserver().removeOnGlobalLayoutListener(this);
        }
    };

    //
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


    final OnItemClickListener mListener = new OnItemClickListener() {
        @Override
        public void SimpleOnItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
            mAdapter.setSelected(i);
        }
    };


    //服务器时间同步器
    private Handler mHandler = new Handler();
    private Runnable mSyncTask = new Runnable() {
        @Override
        public void run() {
            DataManager.getServiceTime();
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ll_video_play_input:
                if (mInputDialog == null){
                    mInputDialog = new GiftNumberInputDialog(getActivity(),v,mNumberData);
                }

                if (mActivity.isLandscape()){
                    UmengAnalyticsHelper.onEvent(getContext(), UmengAnalyticsHelper.VIDEOPLAY, "视频播放页-横屏-礼物数量选择");

                }else {
                    UmengAnalyticsHelper.onEvent(getContext(), UmengAnalyticsHelper.VIDEOPLAY, "视频播放页-竖屏-礼物数量选择");
                }

                mInputDialog.showOrHide();
                break;
            case R.id.tv_video_play_gift:               //打赏
                if (mActivity.isLandscape()){
                    UmengAnalyticsHelper.onEvent(getContext(), UmengAnalyticsHelper.VIDEOPLAY, "视频播放页-横屏-打赏按钮");

                }else {
                    UmengAnalyticsHelper.onEvent(getContext(), UmengAnalyticsHelper.VIDEOPLAY, "视频播放页-竖屏-打赏按钮");
                }

                if (!isLogin()) {
                    DialogManager.showLogInDialog(getActivity());
                    break;
                }
                if (mAdapter.getData() != null && mAdapter.getData().size() > 0){
                    if (mLoadingDialog == null){
                        mLoadingDialog = new LoadingDialog(getContext());
                        mLoadingDialog.setProgressText("打赏中...");
                        mLoadingDialog.setCancelable(false);
                    }
                    mLoadingDialog.show();
                    playGift(getMember_id(),mVideoId,mAdapter.getData().get(mAdapter.getSelected()).getGift_id(),mActivity.getProgress(),mNumber);
                }
                break;
            case R.id.tv_video_play_recharge:           //充值
                ActivityManager.startMyWalletActivity(getContext(),0);
                if (mActivity.isLandscape()){
                    UmengAnalyticsHelper.onEvent(getContext(), UmengAnalyticsHelper.VIDEOPLAY, "视频播放页-横屏-充值");
                }else {
                    UmengAnalyticsHelper.onEvent(getContext(), UmengAnalyticsHelper.VIDEOPLAY, "视频播放页-竖屏-充值");
                }
                break;
        }
    }

    private void cancelLoadingDialog(){
        if (mLoadingDialog != null && mLoadingDialog.isShowing()){
            mLoadingDialog.dismiss();
        }
    }

    /**
     * 打赏
     */
    private void playGift(String memberId,String videoId,String giftId,long videoNode,int number){
        isPlaying = true;
        mServiceTime = System.currentTimeMillis()/1000+mServiceTimeOffset;
        if (mServiceTimeOffset == -1){
            DataManager.getServiceTime();
        }

        final String node = String.valueOf((int) (videoNode/1000));
        String sign = sign(memberId,videoId,giftId,mServiceTime+"",number);
        DataManager.playGift(sign,memberId,videoId,giftId,node,number,mServiceTime);
        PlayGiftTypeEntity.DataBean data =  mAdapter.getData().get(mAdapter.getSelected());
        mSuccessDialog = new PlayGiftDialog(getContext(),data.getGift_icon(),mNumber);
        mEntity = new TimeLineGiftEntity.DataBean();
        mEntity.setAvatar(getUser().getAvatar());
        mEntity.setGift_icon(data.getGift_icon());
        mEntity.setGift_name(data.getGift_name());
        mEntity.setGift_name(data.getGift_name());
        mEntity.setName(getUser().getName());
        mEntity.setVideo_node(node);
        mEntity.setMember_id(getMember_id());
        mEntity.setNum(number+"");
        mEntity.setLeft_gift_icon(data.getLeft_gift_icon());
        mEntity.setVideo_id(videoId);

    }

    /**
     * 生成签名
     */
    private String sign(String memberId,String videoId,String giftId,String time,int number){
        String appKey = getContext().getResources().getString(R.string.play_gift_sign_key);
        String params = memberId+giftId+number+videoId+appKey;

        return AppAccount.sign(params,time);
    }

    /**
     *网络错误
     * @param error
     */
    public void onEventMainThread(NetworkError error){
        if (error != null){
            if (RequestUrl.getInstance().playGift().equals(error.getUrl()) || RequestUrl.getInstance().getServiceTime().equals(error.getUrl())){
                cancelLoadingDialog();
                ToastHelper.l("网络好像有点问题哦~");
            }
        }
    }

    /**
     * 打赏结果
     */
    public void onEventMainThread(PlayGiftResultEntity entity){
        cancelLoadingDialog();
        if (entity != null){
            if (entity.isResult()){
                mSuccessDialog.show();
                ToastHelper.l("打赏成功啦~");
                //更新个人信息
                Member member = getUser();
                member.setCurrency(entity.getData().getCurrency());
                member.setCoin(entity.getData().getCoin());
                PreferencesHepler.getInstance().saveUserProfilePersonalInformation(member);

                if (mCoin != null){
                    try {
                        mCoin.setText(StringUtil.formatMoneyOnePoint(Float.parseFloat(getUser().getCoin())));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                if (mBeans != null){
                    try {
                        mBeans.setText(StringUtil.formatMoney(Float.parseFloat(getUser().getCurrency())));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                //更新打赏榜
                DataManager.getPlayGiftList(getMember_id(),mVideoId);

                //直接插入一条时间轴信息
                if (mEntity != null){
                    io.rong.eventbus.EventBus.getDefault().post(mEntity);
                }
                //更新视频评论列表
                io.rong.eventbus.EventBus.getDefault().post(new RefreshCommendEvent());

            }else if (entity.getCode() == 20001){
                ToastHelper.l(entity.getMsg());

            } else {
                ToastHelper.l("哎呀~出现问题打赏失败了~");
            }

        }else {
            ToastHelper.l("网络好像有点问题哦~");
        }
    }

    /**
     * 事件：更新个人资料
     */
    public void onEventMainThread(UserInfomationEvent event) {

        if (event != null) {
            if (mCoin != null){
                try {
                    mCoin.setText(StringUtil.formatMoneyOnePoint(Float.parseFloat(getUser().getCoin())));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (mBeans != null){
                try {
                    mBeans.setText(StringUtil.formatMoney(Float.parseFloat(getUser().getCurrency())));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 礼物类型回调
     */
    public void onEventMainThread(PlayGiftTypeEntity entity){
        if (entity != null){
            mNumberData = entity.getNumberSense();
            mAdapter.setNewData(entity.getData());
        }
    }

    /**
     * 输入数量改变
     */
    public void onEventMainThread(InputNumberEvent entity){
        if (entity != null){
            mNumber = entity.getNumber();
            mNum.setText("x"+mNumber);
        }
    }


    private long mServiceTimeOffset = -1;
    /**
     * 同步服务器时间
     */
    public void onEventMainThread(ServiceTimeEntity entity){
        if (entity != null && entity.getTimestamp() != 0){
            mServiceTimeOffset = entity.getTimestamp() - System.currentTimeMillis()/1000;
            if (isPlaying){
                isPlaying = false;
                if (mAdapter.getData() != null && mAdapter.getData().size() > 0){
               //     playGift(getMember_id(),mVideoId,mAdapter.getData().get(mAdapter.getSelected()).getGift_id(),mActivity.getProgress(),mNumber);
                }
            }

            //4分钟同步一次 修正误差
            mHandler.postDelayed(mSyncTask,240000);
        }else {
            mServiceTimeOffset = -1;
            ToastHelper.l("网络好像有点问题哦~");
            isPlaying = false;
        }
    }
}
