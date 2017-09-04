package com.li.videoapplication.ui.activity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.SystemClock;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RatingBar;
import android.widget.TextView;

import com.googlecode.mp4parser.boxes.mp4.objectdescriptors.AudioSpecificConfig;
import com.ifeimo.im.activity.ChatActivity;
import com.ifeimo.im.framwork.IMSdk;
import com.ifeimo.im.framwork.Proxy;
import com.ifeimo.im.framwork.commander.IFileTransfer;
import com.li.videoapplication.R;
import com.li.videoapplication.data.DataManager;
import com.li.videoapplication.data.cache.RequestCache;
import com.li.videoapplication.data.model.entity.HomeDto;
import com.li.videoapplication.data.model.entity.Member;
import com.li.videoapplication.data.model.event.PayNowEvent;
import com.li.videoapplication.data.model.event.RefreshOrderDetailEvent;
import com.li.videoapplication.data.model.response.CancelPlayWithOrderEntity;
import com.li.videoapplication.data.model.response.CoachConfirmRefundEntity;
import com.li.videoapplication.data.model.response.ConfirmOrderDoneEntity;
import com.li.videoapplication.data.model.response.ConfirmOrderEntity;
import com.li.videoapplication.data.model.response.ConfirmTakeOrderEntity;
import com.li.videoapplication.data.model.response.CustomerInfoEntity;
import com.li.videoapplication.data.model.response.GroupAttentionGroupEntity;
import com.li.videoapplication.data.model.response.PlayWithOrderDetailEntity;
import com.li.videoapplication.data.model.response.PlayWithTakeOrderEntity;
import com.li.videoapplication.data.network.RequestParams;
import com.li.videoapplication.data.network.RequestUrl;
import com.li.videoapplication.data.network.UITask;
import com.li.videoapplication.framework.TBaseAppCompatActivity;
import com.li.videoapplication.interfaces.IShowDialogListener;
import com.li.videoapplication.tools.FeiMoIMHelper;
import com.li.videoapplication.tools.TimeHelper;
import com.li.videoapplication.tools.ToastHelper;
import com.li.videoapplication.tools.UmengAnalyticsHelper;
import com.li.videoapplication.ui.ActivityManager;
import com.li.videoapplication.ui.DialogManager;
import com.li.videoapplication.ui.dialog.ConfirmDialog;
import com.li.videoapplication.ui.dialog.ConfirmPlayWithDialog;
import com.li.videoapplication.ui.dialog.LoadingDialog;
import com.li.videoapplication.ui.dialog.OrderMoreOperationDialog;
import com.li.videoapplication.ui.fragment.PlayWithOrderDetailFragment;
import com.li.videoapplication.ui.view.OrderOperationView;
import com.li.videoapplication.utils.StringUtil;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import io.rong.eventbus.EventBus;

/**
 * 陪练订单详情
 */

public class PlayWithOrderDetailActivity extends TBaseAppCompatActivity implements SwipeRefreshLayout.OnRefreshListener{

    //以什么角色查看订单
    public final static int ROLE_OWNER = 0;              //下单者
    public final static int ROLE_COACH = 1;              //教练

    private String mOrderId;

    private PlayWithOrderDetailFragment mFragment;

    private PlayWithOrderDetailEntity mOrderDetail;

    private  CustomerInfoEntity mCustomerEntity;

    private OrderMoreOperationDialog mMoreDialog;

    private LoadingDialog mLoadingDialog ;

    private SwipeRefreshLayout mRefresh;

    private OrderOperationView mOperationView;
    private int mRole;

    private boolean mIsShowCoach = true;

    private View mTickerRoot;

    private Chronometer mTicker;

    private TextView mCancelOrder;

    private ImageView mRadar;

    private TextView mTickerTip;

    private RotateAnimation mRadarAnimation;
    public static int getRole(String memberId,String userId,String coachId){
        if (StringUtil.isNull(memberId) || StringUtil.isNull(userId) || StringUtil.isNull(coachId)){
            return -1;
        }

        if (memberId.equals(userId)){
            return ROLE_OWNER;
        }else if (memberId.equals(coachId)){
            return ROLE_COACH;
        }
        return -1;
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_play_with_order_detail;
    }

    @Override
    public void refreshIntent() {
        super.refreshIntent();
        mOrderId = getIntent().getStringExtra("order_id");
        mRole = getIntent().getIntExtra("role",-1);
        mIsShowCoach = getIntent().getBooleanExtra("is_show_coach",true);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        mOrderId = getIntent().getStringExtra("order_id");
        mRole = getIntent().getIntExtra("role",-1);
        mIsShowCoach = getIntent().getBooleanExtra("is_show_coach",true);

    }

    @Override
    public void afterOnCreate() {
        super.afterOnCreate();
        initToolbar();

        mLoadingDialog = new LoadingDialog(this);
        mOperationView = (OrderOperationView) findViewById(R.id.oov_order_operation);
        mRefresh = (SwipeRefreshLayout)findViewById(R.id.srl_coach_detail);
        mRefresh.setColorSchemeResources(android.R.color.holo_green_light, android.R.color.holo_blue_light,
                android.R.color.holo_orange_light, android.R.color.holo_red_light);
        mRefresh.setOnRefreshListener(this);
    }

    @Override
    public void loadData() {
        super.loadData();

        mLoadingDialog.show();
        DataManager.getPlayWithOrderDetail(getMember_id(),mOrderId);
        //获取客服信息
        DataManager.getCustomerInfo();
    }

    @Override
    public void onRefresh() {
        //清除缓存
        RequestCache.save(RequestUrl.getInstance().getPlayWithOrderDetail(), RequestParams.getInstance().getPlayWithOrderDetail(getMember_id(),mOrderId),"");
        //
        DataManager.getPlayWithOrderDetail(getMember_id(),mOrderId);
    }

    private void  initToolbar(){
        findViewById(R.id.tb_back).setOnClickListener(mListener);
        ((TextView)findViewById(R.id.tb_title)).setText("订单信息");
        setSupportActionBar(((Toolbar)findViewById(R.id.toolbar)));
        findViewById(R.id.iv_more_operation).setOnClickListener(mListener);
    }

    /**
     * 订单详情 fragment
     */
    private void showOrderFragment(PlayWithOrderDetailEntity entity){
        mFragment = PlayWithOrderDetailFragment.newInstance(entity,mIsShowCoach);

        if (entity.getCoach() == null && "2".equals(entity.getData().getOrder_mode())){
            mFragment.addViewGone(R.id.ll_coach_info);
            if (entity.getData().getStatusX().equals("2") || entity.getData().getStatusX().equals("14")){
                startTickView(true);
            }else {
                startTickView(false);
            }

        }else {
            startTickView(false);
        }

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.rl_play_with_detail_fragment,mFragment)
                .commitAllowingStateLoss();
        initOperationView(entity);
        initComment(entity);
    }

    /**
     *底部两个操作按钮
     */
    private void initOperationView(PlayWithOrderDetailEntity entity){
        if (entity != null && entity.isResult()){
            mOperationView.refreshOrder(entity,mRole);
            mOperationView.setListener(new IShowDialogListener() {
                @Override
                public void onShowDialog(boolean isShow) {
                    if (isShow){
                        mLoadingDialog.show();
                    }else {
                        mLoadingDialog.dismiss();
                    }
                }
            });
        }
    }

    /**
     * 继续选择TA
     */
    public void choiceAgain(View source){

        if (mOrderDetail == null || mOrderDetail.getCoach() == null){
            return;
        }
        Intent intent = new Intent();
        intent.setClass( this, CreatePlayWithOrderActivity.class);
        intent.putExtra("order_mode",CreatePlayWithOrderActivity.MODE_ORDER_AGAIN);
        intent.putExtra("coach_bean",mOrderDetail.getCoach());
        startActivity(intent);
    }

    /**
     * 评论item
     *
     * @param entity
     */
    private void initComment(PlayWithOrderDetailEntity entity){
        if(mRole == ROLE_COACH){
            if (entity.getData() != null && entity.getData().getEvaluate() != null){
                findViewById(R.id.cv_order_comment).setVisibility(View.VISIBLE);
                ((TextView)findViewById(R.id.tv_order_comment_content)).setText(entity.getData().getEvaluate().getContent());
                try {
                    ((TextView)findViewById(R.id.tv_order_comment_time)).setText(TimeHelper.getWholeTimeFormat(entity.getData().getEvaluate().getAdd_time()));
                } catch (Exception e) {
                    e.printStackTrace();
                }

                try {
                    ((RatingBar)findViewById(R.id.rb_order_comment_score)).setRating(Float.parseFloat(entity.getData().getEvaluate().getScore()));
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 启动计时
     */
    public void startTickView(boolean isShow){
        mTickerRoot = findViewById(R.id.ll_grab_ticker);
        if (isShow){
            mTickerRoot.setVisibility(View.VISIBLE);
            mTicker = (Chronometer) findViewById(R.id.ct_ticker);
            mCancelOrder = (TextView ) findViewById(R.id.tv_cancel_order);
            mRadar = (ImageView)findViewById(R.id.iv_radar);
            mTickerTip = (TextView)findViewById(R.id.tv_grab_order_tip);

            mCancelOrder.setOnClickListener(mListener);
            if(mOrderDetail != null){
                try {
                    int waitingTime =Integer.parseInt( mOrderDetail.getWaitingTime());
                    //SystemClock.elapsedRealtime() 为系统启动后的运行时间 Chronometer定时计算公式：long seconds = mCountDown ? mBase - SystemClock.elapsedRealtime() : SystemClock.elapsedRealtime() - mBase;
                    long t = SystemClock.elapsedRealtime() - waitingTime*1000;
                    mTicker.setBase(t);
                    mTicker.start();

                    mRadarAnimation = new RotateAnimation(0,360, Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
                    mRadarAnimation.setFillAfter(true);
                    mRadarAnimation.setDuration(500);
                    mRadarAnimation.setRepeatCount(-1);
                    mRadarAnimation.setInterpolator(new LinearInterpolator());


                    if(mOrderDetail.getData().getStatusX().equals("14")){
                        mTicker.stop();
                        mRadarAnimation.cancel();
                        mTickerTip.setText("目前陪练正忙，系统给您自动退款人工客服会为您尽快安排客服");
                        mRadar.setVisibility(View.INVISIBLE);

                        mCancelOrder.setText("派单失败");
                        mCancelOrder.setTextColor(getResources().getColor(R.color.ab_backdround_red));

                        mCancelOrder.setBackground(null);

                        findViewById(R.id.iv_radar_bg).setVisibility(View.INVISIBLE);

                    }else {
                        mTicker.start();
                        mRadar.startAnimation(mRadarAnimation);
                        mRadar.setVisibility(View.VISIBLE);
                        mTickerTip.setText("系统正在派单，请耐心等待");

                        mCancelOrder.setText("取消订单");
                        mCancelOrder.setTextColor(Color.parseColor("#595858"));
                        mCancelOrder.setBackgroundResource(R.drawable.cancel_play_with_order_bg);

                        findViewById(R.id.iv_radar_bg).setVisibility(View.VISIBLE);

                    }

                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            }
        }else {
            mTickerRoot.setVisibility(View.GONE);
            if (mTicker != null){
                mTicker.stop();
            }
            if (mRadar != null && mRadarAnimation != null){
                mRadarAnimation.cancel();
            }
        }
    }

    /**
     *
     */
    final View.OnClickListener mListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
           if (v.getId() == R.id.tb_back){
                finish();
            }else if (v.getId() == R.id.iv_more_operation){             //省略号

                if (mMoreDialog == null) {
                    mMoreDialog = new OrderMoreOperationDialog(PlayWithOrderDetailActivity.this,getUser(), v, mOrderDetail, mRole);
                    mMoreDialog.setOnDismissListener(new PopupWindow.OnDismissListener() {
                        @Override
                        public void onDismiss() {
                            changeWindowAlpha(1f);
                        }
                    });

                    //抢单模式下取消订单 需要确认
                    mMoreDialog.setCancelListener(new OrderMoreOperationDialog.CancelListener() {
                        @Override
                        public void onCancelOrder() {
                            showCancelConfirmDialog();
                        }
                    });
                }
                mMoreDialog.setCustomerEntity(mCustomerEntity);
                mMoreDialog.setOrderDetail(mOrderDetail);
                mMoreDialog.show();
                changeWindowAlpha(0.5f);
            }else if(v.getId() == R.id.tv_cancel_order){                //取消订单
               showCancelConfirmDialog();
            }
        }
    };


    /**
     *更改透明度 出现阴影
     */
    private void changeWindowAlpha(float scale){
        WindowManager.LayoutParams params = PlayWithOrderDetailActivity.this.getWindow().getAttributes();
        params.alpha = scale;
        PlayWithOrderDetailActivity.this.getWindow().setAttributes(params);
    }

    /**
     * 确认取消订单 对话框
     */
    private void showCancelConfirmDialog(){

        Dialog dialog = new ConfirmDialog(PlayWithOrderDetailActivity.this,"陪练正飞速赶来的路上", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    case R.id.tv_confirm_dialog_yes:
                        //
                        if (mLoadingDialog != null){
                            mLoadingDialog.show();
                        }
                        DataManager.cancelPlayWithOrder(getMember_id(),mOrderId);

                        if (mMoreDialog != null){
                            if (mMoreDialog.isShowing()){
                                mMoreDialog.dismiss();
                            }
                        }
                        break;
                }
            }
        });
        TextView yes = (TextView) dialog.findViewById(R.id.tv_confirm_dialog_yes);
        TextView no = (TextView) dialog.findViewById(R.id.tv_confirm_dialog_no);
        yes.setText("依然取消");
        no.setText("再等等");
        no.setTextColor(getResources().getColor(R.color.menu_main_red));
        dialog.show();

    }

    /**
     * 订单确认完成结果
     */
    public void onEventMainThread(ConfirmOrderDoneEntity entity){
        if (entity.isResult()){
            ToastHelper.s("订单已确认完成~");
            EventBus.getDefault().post(new RefreshOrderDetailEvent(
                    mOrderDetail.getData().getOrder_id(),
                    mOrderDetail.getData().getStatusX(),
                    mOrderDetail.getData().getStatusText()));
        }else {
            ToastHelper.s("订单确认完成失败~");
        }

    }


    /**
     * 客服信息
     */
    public void onEventMainThread(CustomerInfoEntity entity){
        if (entity.isResult()){
            mCustomerEntity = entity;
            if( mMoreDialog != null){
                mMoreDialog.setCustomerEntity(mCustomerEntity);
            }
        }
    }

    /**
     *订单详情
     */
    public void onEventMainThread(PlayWithOrderDetailEntity entity){
        mRefresh.setRefreshing(false);
        if (mLoadingDialog.isShowing()){
            mLoadingDialog.dismiss();
        }
        if (entity != null && entity.isResult()){
            showOrderFragment(entity);
            if (mOrderDetail != null){
                if (entity.getData().getOrder_id().equals(mOrderDetail.getData().getOrder_id())){
                    if (entity.getData().getStatusX().equals(mOrderDetail.getData().getStatusX())){
                        return;
                    }
                }
            }

            mOrderDetail = entity;

            EventBus.getDefault().post(new RefreshOrderDetailEvent(
                    mOrderDetail.getData().getOrder_id(),
                    mOrderDetail.getData().getStatusX(),
                    mOrderDetail.getData().getStatusText()));
        }
    }


    /**
     * 教练确认接单
     */
    public void onEventMainThread(ConfirmTakeOrderEntity entity){
        if (entity.isResult()){
            ToastHelper.s("您已成功接单~");
            DataManager.getPlayWithOrderDetail(getMember_id(),mOrderDetail.getData().getOrder_id());
        }else {
            ToastHelper.s(entity.getMsg());
            if (mLoadingDialog.isShowing()){
                mLoadingDialog.dismiss();
            }
        }
    }

    /**
     *需要更新订单状态
     */
    public void onEventMainThread(RefreshOrderDetailEvent event){
        if (event.getOrderId() != null){
            //更新订单状态
            DataManager.getPlayWithOrderDetail(getMember_id(),event.getOrderId());
        }

    }

    /**
     * 立即支付
     */
    public void onEventMainThread(PayNowEvent event){
        if (event.getPage() == CreatePlayWithOrderActivity.PAGE_ORDER_DETAIL){
            //支付订单
            mLoadingDialog.show();
            DataManager.confirmOrder(getMember_id(),mOrderDetail.getData().getOrder_id());
        }
    }

    /**
     * 支付结果
     */
    public void onEventMainThread(ConfirmOrderEntity entity){
        if (entity.isResult()){

            //更新订单ID
            mOrderDetail.getData().setOrder_id(entity.getOrder_id());

            EventBus.getDefault().post(new RefreshOrderDetailEvent(
                    mOrderDetail.getData().getOrder_id(),
                    mOrderDetail.getData().getStatusX(),
                    mOrderDetail.getData().getStatusText()));

            UITask.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mLoadingDialog.dismiss();
                    ToastHelper.s("支付成功~");
                }
            },800);
        }else {
            ToastHelper.s(entity.getMsg());
            mLoadingDialog.dismiss();
        }
    }

    /**
     * 确认退款结果
     */

    public void onEventMainThread(CoachConfirmRefundEntity entity){
        if (entity.isResult()){
            EventBus.getDefault().post(new RefreshOrderDetailEvent(
                    mOrderDetail.getData().getOrder_id(),
                    mOrderDetail.getData().getStatusX(),
                    mOrderDetail.getData().getStatusText()));

            UITask.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mLoadingDialog.dismiss();
                    ToastHelper.s("您已成功确认退款~");
                }
            },800);
        }else {
            ToastHelper.s(entity.getMsg());
            mLoadingDialog.dismiss();
        }
    }

    /**
     * 取消订单结果
     */
    public void onEventMainThread(CancelPlayWithOrderEntity entity){
        if (mLoadingDialog != null){
            mLoadingDialog.dismiss();
        }
        if (entity.isResult()){
            ToastHelper.s("您已成功取消该陪练订单~");
            //
            EventBus.getDefault().post(new RefreshOrderDetailEvent(
                    mOrderDetail.getData().getOrder_id(),
                    mOrderDetail.getData().getStatusX(),
                    mOrderDetail.getData().getStatusText()));
            //取消雷达与计时器
            startTickView(false);
        }else {
            if (entity.getMsg() != null){
                ToastHelper.s(entity.getMsg());
            }
        }
    }
}
