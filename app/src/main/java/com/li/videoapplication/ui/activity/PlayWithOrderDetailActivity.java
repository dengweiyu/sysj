package com.li.videoapplication.ui.activity;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.PopupWindow;
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
import com.li.videoapplication.data.model.response.CoachConfirmRefundEntity;
import com.li.videoapplication.data.model.response.ConfirmOrderDoneEntity;
import com.li.videoapplication.data.model.response.ConfirmOrderEntity;
import com.li.videoapplication.data.model.response.ConfirmTakeOrderEntity;
import com.li.videoapplication.data.model.response.CustomerInfoEntity;
import com.li.videoapplication.data.model.response.PlayWithOrderDetailEntity;
import com.li.videoapplication.data.model.response.PlayWithTakeOrderEntity;
import com.li.videoapplication.data.network.RequestParams;
import com.li.videoapplication.data.network.RequestUrl;
import com.li.videoapplication.data.network.UITask;
import com.li.videoapplication.framework.TBaseAppCompatActivity;
import com.li.videoapplication.tools.FeiMoIMHelper;
import com.li.videoapplication.tools.ToastHelper;
import com.li.videoapplication.ui.ActivityManager;
import com.li.videoapplication.ui.DialogManager;
import com.li.videoapplication.ui.dialog.ConfirmPlayWithDialog;
import com.li.videoapplication.ui.dialog.LoadingDialog;
import com.li.videoapplication.ui.dialog.OrderMoreOperationDialog;
import com.li.videoapplication.ui.fragment.PlayWithOrderDetailFragment;
import com.li.videoapplication.utils.StringUtil;

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

    private int mCommentCount;

    private OrderMoreOperationDialog mMoreDialog;

    private ConfirmPlayWithDialog mConfirmDialog;

    private LoadingDialog mLoadingDialog ;

    private SwipeRefreshLayout mRefresh;
    private int mRole;

    private boolean mIsShowCoach = true;
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

    private void showOrderFragment(PlayWithOrderDetailEntity entity){
        mFragment = PlayWithOrderDetailFragment.newInstance(entity,mIsShowCoach);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.rl_play_with_detail_fragment,mFragment)
                .commitAllowingStateLoss();
        initOperationView(entity);
    }

    private void initOperationView(PlayWithOrderDetailEntity entity){
        if (entity != null && entity.isResult()){
            findViewById(R.id.ll_coach_operation).setVisibility(View.VISIBLE);
            TextView chat = (TextView)findViewById(R.id.tv_chat_with_coach);
            TextView select = (TextView)findViewById(R.id.tv_coach_selected);
            chat.setOnClickListener(mListener);
            select.setOnClickListener(mListener);

            mOrderDetail = entity;

            chat.setVisibility(View.VISIBLE);
            select.setVisibility(View.VISIBLE);
            switch (entity.getData().getStatusX()){
                case "0":
                    chat.setText("私聊");                                             //待支付
                    if (mRole == ROLE_OWNER  ){
                        select.setText("立即支付");
                    }else {
                        select.setVisibility(View.GONE);
                    }
                    break;
                case "1":
                    chat.setText("支付失败");
                    if (mRole == ROLE_OWNER  ){
                        select.setText("再来一单");
                    }else {
                        select.setVisibility(View.GONE);
                    }
                    break;
                case "2":
                    chat.setText("私聊");                                               //待接单
                    if (mRole == ROLE_COACH ){                                          //只允许教练接单
                        select.setText("确认接单");
                    }else {
                        select.setVisibility(View.GONE);
                    }
                    break;
                case "3":
                    if (mRole == ROLE_COACH){
                        if ("0".equals(entity.getData().getHas_result())){              //客户已确认
                            chat.setText("私聊");
                            select.setText("提交结果");
                        }else {                                                         //客户未确认 理论上不会出现
                            chat.setText("私聊");
                            select.setVisibility(View.GONE);
                        }
                    }else {
                        chat.setText("私聊");
                        select.setVisibility(View.GONE);
                    }
                    break;

                case "4":
                    if (mRole == ROLE_OWNER){
                        if ("1".equals(entity.getData().getHas_result())){              //教练已提交结果  等待客户确认
                            chat.setText("确认完成");
                            select.setText("再来一单");
                            chat.setBackgroundResource(R.drawable.background_press_red);
                            select.setBackgroundResource(R.drawable.background_press);
                            chat.setTextColor(getResources().getColor(R.color.white));
                            select.setTextColor(getResources().getColor(R.color.text_color));
                        }else {
                            chat.setText("评价");                                       //已确认  理论上不会出现
                            select.setText("再来一单");
                        }
                     }else {
                        chat.setText("私聊");
                        select.setVisibility(View.GONE);
                    }
                     break;
                case "5":                                                           //完成订单
                    if (mRole == ROLE_OWNER){
                        chat.setText("评价");
                        select.setText("再来一单");
                        chat.setBackgroundResource(R.drawable.background_press);
                        select.setBackgroundResource(R.drawable.background_press_red);
                        chat.setTextColor(getResources().getColor(R.color.text_color));
                        select.setTextColor(getResources().getColor(R.color.white));
                        try {
                            mCommentCount = Integer.parseInt(entity.getData().getEvaluate_counter());
                            if (mCommentCount == 1){
                                chat.setText("修改评价");
                                select.setText("再来一单");
                            }else if (mCommentCount > 1){
                                chat.setVisibility(View.GONE);
                                select.setText("再来一单");
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }else {
                        chat.setText("私聊");
                        select.setVisibility(View.GONE);
                    }
                    break;
                case "10":                          //退款中
                    chat.setText("私聊");
                    if (mRole == ROLE_OWNER){
                        select.setText("再来一单");
                    }else {
                        select.setText("确认退款");
                    }

                    break;
                case "11":                          //退款完成
                    chat.setText("私聊");
                    if (mRole == ROLE_OWNER){
                        select.setText("再来一单");
                    }else {
                        select.setVisibility(View.GONE);
                    }
                    break;
                }
        }
    }

    final View.OnClickListener mListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.tv_chat_with_coach){
                //左边灰色背景按钮
                switch (mOrderDetail.getData().getStatusX()){
                    case "0":
                            chatWith();
                        break;
                    case "1":

                        break;
                    case "2":

                        chatWith();
                        break;
                    case "3":
                        chatWith();

                        break;

                    case "4":
                        if (mRole == ROLE_OWNER){
                            //执行确认完成操作
                            confirmOrderDone();
                        }else {
                            chatWith();
                        }
                        break;

                    case "5":                                                           //完成订单
                        if (mRole == ROLE_OWNER ){
                            //跳转评价
                            startCommentActivity();
                        }else {
                            chatWith();
                        }
                        break;
                    case "10":                                                           //退款中
                    case "11":                                                          //退款完成
                        chatWith();
                        break;
                }
            }else if(v.getId() == R.id.tv_coach_selected){
                //右边红色背景按钮
                switch (mOrderDetail.getData().getStatusX()){
                    case "0":
                        if (mRole == ROLE_OWNER  ){
                           //重新支付
                            int coin = 0;
                            float priceTotal = 0;

                            try {
                                coin = Integer.parseInt(getUser().getCoin());
                                priceTotal = Float.parseFloat(mOrderDetail.getData().getPrice_total());
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            if (priceTotal != 0){
                                mConfirmDialog = new ConfirmPlayWithDialog(PlayWithOrderDetailActivity.this,priceTotal,coin,CreatePlayWithOrderActivity.PAGE_ORDER_DETAIL);
                                mConfirmDialog.show();
                            }

                        }
                        break;
                    case "1":
                        if (mRole == ROLE_OWNER  ){
                            //跳转下单页
                            startCreateOrderActivity();
                        }
                        break;
                    case "2":
                        if (mRole == ROLE_COACH ){
                            //教练确认接单
                            mLoadingDialog.setProgressText("接单中...");
                            mLoadingDialog.show();
                            DataManager.confirmTakeOrder(getMember_id(),mOrderDetail.getData().getOrder_id());
                        }
                        break;
                    case "3":
                        if (mRole == ROLE_COACH) {
                            //教练提交结果
                            if ("0".equals(mOrderDetail.getData().getHas_result())) {
                                startSubmitResultActivity();
                            }
                        }
                        break;
                    case "4":                                                           //完成订单
                        if (mRole == ROLE_OWNER){
                            //再来一单
                            startCreateOrderActivity();
                        }
                        break;
                    case "5":
                        if (mRole == ROLE_OWNER){
                            //再来一单
                            startCreateOrderActivity();
                        }
                        break;
                    case "10":                          //退款中
                        if (mRole == ROLE_COACH){
                            mLoadingDialog.show();
                            //确认退款
                            DataManager.coachConfirmRefund(getMember_id(),mOrderId);
                        }else {
                            //再来一单
                            startCreateOrderActivity();
                        }
                        break;
                    case "11":                          //退款完成
                        if (mRole == ROLE_OWNER){
                            //再来一单
                            startCreateOrderActivity();
                        }
                        break;
                }

            }else if (v.getId() == R.id.tb_back){
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
                }
                mMoreDialog.setCustomerEntity(mCustomerEntity);
                mMoreDialog.show();
                changeWindowAlpha(0.5f);
            }
        }
    };

    /**
     * 私聊
     */
    private void chatWith(){
        Member user = getUser();

        if (user == null || StringUtil.isNull(getMember_id())){
            DialogManager.showLogInDialog(this);
            return;
        }

        if (!Proxy.getConnectManager().isConnect()) {
            FeiMoIMHelper.Login(user.getMember_id(), user.getNickname(), user.getAvatar());
        }

        String memberId;
        String nickName;
        String avatar;
        if (mRole == ROLE_OWNER){
            memberId = mOrderDetail.getCoach().getMember_id();
            nickName = mOrderDetail.getCoach().getNickname();
            avatar = mOrderDetail.getCoach().getAvatar();
        }else {
            memberId = mOrderDetail.getUser().getMember_id();
            nickName = mOrderDetail.getUser().getNickname();
            avatar = mOrderDetail.getUser().getAvatar();
        }

        if (getMember_id().equals(memberId)){
            ToastHelper.s("不能和自己聊天哦~");
            return;
        }


        IMSdk.createChat(this,memberId,nickName,avatar, ChatActivity.SHOW_FAST_REPLY);
    }

    /**
     * 确认完成订单
     */
    private void confirmOrderDone(){
        mLoadingDialog.show();
        DataManager.confirmOrderDone(getMember_id(),mOrderDetail.getData().getOrder_id());
    }

    /**
     *跳转评论页
     */
    private void startCommentActivity(){

        ActivityManager.startPlayWithOrderCommentActivity(PlayWithOrderDetailActivity.this,
                mOrderDetail.getCoach().getMember_id(),
                mOrderDetail.getCoach().getNickname(),
                mOrderDetail.getCoach().getAvatar(),
                mOrderDetail.getCoach().getScore(),
                mOrderDetail.getData().getOrder_id());

    }

    /**
     *教练提交结果
     */
    private void startSubmitResultActivity(){
        ActivityManager.startConfirmOrderDoneActivity(
                PlayWithOrderDetailActivity.this,
                mOrderDetail.getCoach().getNickname(),
                mOrderDetail.getCoach().getAvatar(),
                mOrderDetail.getData().getOrder_id(),
                mOrderDetail.getData().getInning(),
                mOrderDetail.getUser().getOrderCount());
    }

    /**
     *再来一单  跳转订单生成页面
     */
    private void startCreateOrderActivity(){
        ActivityManager.startCreatePlayWithOrderActivity(this,
                mOrderDetail.getCoach().getMember_id(),
                mOrderDetail.getCoach().getNickname(),
                mOrderDetail.getCoach().getAvatar());
    }


    private void changeWindowAlpha(float scale){
        WindowManager.LayoutParams params = PlayWithOrderDetailActivity.this.getWindow().getAttributes();
        params.alpha = scale;
        PlayWithOrderDetailActivity.this.getWindow().setAttributes(params);
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

        if (entity != null && entity.isResult()){

            if (mOrderDetail != null){
                if (entity.getData().getOrder_id().equals(mOrderDetail.getData().getOrder_id())){
                    if (entity.getData().getStatusX().equals(mOrderDetail.getData().getStatusX())){
                        return;
                    }
                }
            }

            mOrderDetail = entity;
            showOrderFragment(entity);
            if (mLoadingDialog.isShowing()){
                mLoadingDialog.dismiss();
            }

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
            if (mConfirmDialog != null){
                if (mConfirmDialog.isShowing()){
                    mConfirmDialog.dismiss();
                }
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
}
