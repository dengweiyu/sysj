package com.li.videoapplication.ui.activity;


import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.View;
import android.widget.TextView;

import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.zxing.client.result.CalendarParsedResult;
import com.ifeimo.im.activity.ChatActivity;
import com.ifeimo.im.framwork.IMSdk;
import com.ifeimo.im.framwork.Proxy;
import com.li.videoapplication.R;
import com.li.videoapplication.data.DataManager;
import com.li.videoapplication.data.cache.RequestCache;
import com.li.videoapplication.data.model.entity.Member;
import com.li.videoapplication.data.model.event.PayNowEvent;
import com.li.videoapplication.data.model.response.ConfirmOrderEntity;
import com.li.videoapplication.data.model.response.OrderTimeEntity;
import com.li.videoapplication.data.model.response.PlayGiftTypeEntity;
import com.li.videoapplication.data.model.response.PlayWithOrderEntity;
import com.li.videoapplication.data.model.response.PlayWithOrderOptionsEntity;
import com.li.videoapplication.data.model.response.PlayWithOrderPriceEntity;
import com.li.videoapplication.data.model.response.PlayWithPlaceOrderEntity;
import com.li.videoapplication.data.network.RequestParams;
import com.li.videoapplication.data.network.RequestUrl;
import com.li.videoapplication.data.network.UITask;
import com.li.videoapplication.data.preferences.PreferencesHepler;
import com.li.videoapplication.framework.TBaseAppCompatActivity;
import com.li.videoapplication.tools.FeiMoIMHelper;
import com.li.videoapplication.tools.TimeHelper;
import com.li.videoapplication.tools.ToastHelper;
import com.li.videoapplication.ui.ActivityManager;
import com.li.videoapplication.ui.DialogManager;
import com.li.videoapplication.ui.dialog.ConfirmPlayWithDialog;
import com.li.videoapplication.ui.dialog.LoadingDialog;
import com.li.videoapplication.ui.dialog.SimpleChoiceDialog;
import com.li.videoapplication.ui.dialog.SimpleDoubleChoiceDialog;
import com.li.videoapplication.utils.StringUtil;
import com.li.videoapplication.utils.TextUtil;

import java.util.Calendar;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.rong.eventbus.EventBus;

import static com.li.videoapplication.ui.dialog.SimpleChoiceDialog.TYPE_CHOICE_SERVER;

/**
 * 创建陪玩订单
 */

public class CreatePlayWithOrderActivity extends TBaseAppCompatActivity implements View.OnClickListener {

    public final static int PAGE_CREATE_ORDER = 1;
    public final static int PAGE_ORDER_DETAIL = 2;
    private View mOperation;

    private LoadingDialog mLoadingDialog;

    private SimpleChoiceDialog mServerDialog;

    private SimpleChoiceDialog mGameModeDialog;

    private SimpleChoiceDialog mGameRankDialog;

    private SimpleChoiceDialog mGameCountDialog;

    private SimpleDoubleChoiceDialog mGameTimeDialog;

    private ConfirmPlayWithDialog mConfirmDialog;

    private List<String> mServerList = Lists.newArrayList();

    private List<String> mGameModeList =  Lists.newArrayList();

    private List<String> mRankList = Lists.newArrayList();

    private List<String> mGameCountList = Lists.newArrayList();

    private List<String> mHourList = Lists.newArrayList();

    private List<String> mMinuteList = Lists.newArrayList();

    private List<Long> mHourSecond = Lists.newArrayList();
    private TextView mServerName;
    private TextView mGameModeName;
    private TextView mRankName;
    private TextView mGameCount;
    private TextView mGameTime;
    private TextView mPrice;
    private TextView mPriceTotal;
    private TextView mOriginalPrice;

    private PlayWithOrderOptionsEntity mOptions;

    private int mServerIndex = 0;

    private int mGameModeIndex = 0;

    private int mRankIndex = 0;

    private int mGameCountIndex = 0;

    private int mHourIndex = 0;

    private int mMinuteIndex = 0;

    private String mCoachId;

    private String mCoachNickName;

    private String mCoachAvatar;

    private long mStartSecond;

    private long mEndSecond;

    private long mIntervalTime;
    @Override
    public void refreshIntent() {
        super.refreshIntent();
        try {
            mCoachId = getIntent().getStringExtra("coach_id");
            mCoachNickName = getIntent().getStringExtra("nick_name");
            mCoachAvatar = getIntent().getStringExtra("avatar");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_create_play_with_order;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    @Override
    public void initView() {
        super.initView();
        initToolbar();

        mOperation = findViewById(R.id.ll_coach_operation);
        mOperation.setVisibility(View.VISIBLE);

        mServerName = (TextView)findViewById(R.id.tv_server_name);
        mGameModeName = (TextView)findViewById(R.id.tv_mode_name);
        mRankName = (TextView)findViewById(R.id.tv_rank_name);
        mGameCount = (TextView)findViewById(R.id.tv_game_count);
        mGameTime = (TextView)findViewById(R.id.tv_start_time);
        mPrice = (TextView)findViewById(R.id.tv_single_price);
        mPriceTotal = (TextView)findViewById(R.id.tv_price_total) ;
        mOriginalPrice = (TextView)findViewById(R.id.tv_original_price);
        //添加横线
        mOriginalPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG|Paint.ANTI_ALIAS_FLAG);

        findViewById(R.id.ll_choice_server).setOnClickListener(this);
        findViewById(R.id.ll_choice_game_mode).setOnClickListener(this);
        findViewById(R.id.ll_choice_my_rank).setOnClickListener(this);
        findViewById(R.id.ll_choice_game_count).setOnClickListener(this);
        findViewById(R.id.ll_choice_start_time).setOnClickListener(this);

        TextView chat = (TextView) findViewById(R.id.tv_chat_with_coach);
        chat.setOnClickListener(this);
        TextView createOrder = (TextView) findViewById(R.id.tv_coach_selected);
        createOrder.setText("立即下单");
        createOrder.setOnClickListener(this);

        mLoadingDialog = new LoadingDialog(this);
    }



    private void initToolbar(){
        findViewById(R.id.tb_back).setOnClickListener(this);
        ((TextView)findViewById(R.id.tb_title)).setText("订单信息");
        setSupportActionBar(((Toolbar)findViewById(R.id.toolbar)));

    }

    @Override
    public void loadData() {
        super.loadData();
        setOptionsByCache();
        //load options
        DataManager.getPlayWithOrderOptions(mCoachId);
        //load time
        DataManager.getOrderTime();
    }

    //use cache first
    private void setOptionsByCache(){
        String data =  RequestCache.get(RequestUrl.getInstance().getPlayWithOrderOptions(), null);
        if (!StringUtil.isNull(data)){
            Gson gson = new Gson();
            try {
                mOptions = gson.fromJson(data,PlayWithOrderOptionsEntity.class);
                if (mOptions != null){
                    refreshOptions();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void refreshOptions(){
        refreshTimeText();
        if (mOptions == null){
            return;
        }

        mServerList.clear();
        for (PlayWithOrderOptionsEntity.GameAreaMapBean area:
             mOptions.getGameAreaMap()) {
            mServerList.add(area.getText());
        }

        mGameModeList.clear();
        for (PlayWithOrderOptionsEntity.GameModeMapBean mode:
                mOptions.getGameModeMap()) {
            mGameModeList.add(mode.getText());
        }

        mRankList.clear();
        for (PlayWithOrderOptionsEntity.GameLevelMapBean level:
                mOptions.getGameLevelMap()) {
            mRankList.add(level.getText());
        }

        mGameCountList.clear();
        for (int i = 1; i <= mOptions.getMaxInning(); i++) {
            mGameCountList.add(i+"");
            if (i == mOptions.getDefaultInning()){
                mGameCountIndex = i;
            }
        }

        if (mServerList.size() > mServerIndex){
            mServerName.setText(mServerList.get(mServerIndex));
        }

        if (mGameModeList.size() > mGameModeIndex){
            mGameModeName.setText(mGameModeList.get(mGameModeIndex));
        }

        if (mRankList.size() > mRankIndex){
            mRankName.setText(mRankList.get(mRankIndex));
        }

        if (mGameCountList.size() > mGameCountIndex){
            mGameCount.setText(mGameCountList.get(mGameCountIndex));
        }

        //更新订单价格
        refreshOrderPrice();
    }

    @Override
    public void onClick(View v) {
        if (mOptions == null){
            //load data
            DataManager.getPlayWithOrderOptions(mCoachId);
        }
        switch (v.getId()){
            case R.id.tb_back:
                finish();
                break;
            case R.id.ll_choice_server:                             //选择大区
                showServerDialog();
                break;

            case R.id.ll_choice_game_mode:                         //选择游戏模式
                showGameModeDialog();
                break;
            case R.id.ll_choice_my_rank:                            //选择分段
                showGameRankDialog();
                break;
            case R.id.ll_choice_game_count:                         //选择局数
                showGameCountDialog();
                break;
            case R.id.ll_choice_start_time:                         //选择时间
                DataManager.getOrderTime();
                if (mHourSecond.size() == 0){
                    mLoadingDialog.show();
                    return;
                }

                reCalculationMinute(mIntervalTime,mHourIndex);
                showGameTimeDialog();
                break;
            case R.id.tv_coach_selected:                            //立即下单
                showConfirmDialog(mTotal);
                break;
            case R.id.tv_chat_with_coach:                           //私聊
                chatWith();
                break;
        }
    }


    public void showServerDialog(){
        if (mServerDialog == null){
            mServerDialog  =  new SimpleChoiceDialog(CreatePlayWithOrderActivity.this,mServerList , TYPE_CHOICE_SERVER);
            mServerDialog.setSelectPosition(mServerIndex);
            mServerDialog.setListener(mListener);
        }else {
            mServerDialog.notifyDataSetChanged();
        }
        if (!mServerDialog.isShowing()){
            mServerDialog.show();
        }
    }


    public void showGameModeDialog(){
        if ( mGameModeDialog == null){
            mGameModeDialog  =  new SimpleChoiceDialog(CreatePlayWithOrderActivity.this,mGameModeList,SimpleChoiceDialog.TYPE_CHOICE_MODE);
            mGameModeDialog.setSelectPosition(mGameModeIndex);
            mGameModeDialog.setListener(mListener);
        }else {
            mGameModeDialog.notifyDataSetChanged();
        }

        if (!mGameModeDialog.isShowing()){
            mGameModeDialog.show();
        }
    }

    public void showGameRankDialog(){
        if (mGameRankDialog == null){
            mGameRankDialog  =  new SimpleChoiceDialog(CreatePlayWithOrderActivity.this, mRankList,SimpleChoiceDialog.TYPE_CHOICE_RANK);
            mGameRankDialog.setSelectPosition(mRankIndex);
            mGameRankDialog.setListener(mListener);
        }else {
            mGameRankDialog.notifyDataSetChanged();
        }
        if (!mGameRankDialog.isShowing()){
            mGameRankDialog.show();
        }
    }

    public void showGameCountDialog(){
        if (mGameCountDialog == null ){
            mGameCountDialog  =  new SimpleChoiceDialog(CreatePlayWithOrderActivity.this, mGameCountList,SimpleChoiceDialog.TYPE_CHOICE_COUNT);
            mGameCountDialog.setSelectPosition(mGameCountIndex);
            mGameCountDialog.setListener(mListener);
        }
        if (!mGameCountDialog.isShowing()){
            mGameCountDialog.show();
        }
    }


    public void showGameTimeDialog(){

        reCalculationMinute(mIntervalTime,mHourIndex);
        reCalculationHour(mStartSecond,mEndSecond);
        if (mGameTimeDialog == null){
            mGameTimeDialog = new SimpleDoubleChoiceDialog(this,mHourList,mMinuteList);
            mGameTimeDialog.setHourPosition(mHourIndex);
            mGameTimeDialog.setMinutePosition(mMinuteIndex);
            mGameTimeDialog.setListener(mListener);
        }
        if (!mGameTimeDialog.isShowing()){
            mGameTimeDialog.show();
        }
    }

    public void showConfirmDialog(float price){
        int coin  = 0;
        if (getUser() != null){
            try {
                coin = Integer.parseInt(getUser().getCoin());
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
        mConfirmDialog = new ConfirmPlayWithDialog(this, price, coin,PAGE_CREATE_ORDER);
        if (!mConfirmDialog.isShowing()){
            mConfirmDialog.show();
        }
    }

    /**
     * 更新时间显示
     */
    private void refreshTimeText(){
        if (mHourList.size() > 0 && mMinuteList.size() > 0){
            mGameTime.setText(mHourList.get(mHourIndex)+":"+mMinuteList.get(mMinuteIndex));
        }
    }

    /**
     *小时发生改变了
     */
    public void onCurrentHourChange(int position){

        reCalculationMinute(mIntervalTime,position);

        if (mGameTimeDialog != null){
            //分钟列表发生改变 滚动到初始位置
        //    mGameTimeDialog.smoothScrollStartMinute();

            //更新分钟
            mGameTimeDialog.notifyMinuteDataSetChange();

        }
    }

    /**
     * 生成分钟数据
     * @param duration
     *                     间隔
     */
    private void reCalculationMinute(long duration,int hourIndex){
        mMinuteList.clear();
        boolean isStart = true;
        long second = mStartSecond;

        if (hourIndex >= mHourSecond.size()){
            return;
        }

        if (duration == 0){
            return;
        }

        second = mHourSecond.get(hourIndex);

        if (hourIndex == mHourSecond.size() -1 ){
            isStart = false;
        }

        System.out.println("reCalculationMinute second:"+second);
        if (isStart){
            int startMinute = TimeHelper.getCurrentCalendar(second).get(Calendar.MINUTE);
            int lastMinute = startMinute;

            while(lastMinute < 60){
                System.out.println("reCalculationMinute A "+lastMinute+" "+startMinute);
                mMinuteList.add(formatMinute(lastMinute));
                lastMinute += duration/60;
            }
        }else {
            int endMinute = TimeHelper.getCurrentCalendar(second).get(Calendar.MINUTE);
            int minute = 0;

            while(minute <= endMinute){
                System.out.println("reCalculationMinute B "+minute+" "+endMinute);
                mMinuteList.add(formatMinute(minute));
                minute += duration/60;
            }
        }

    }

    /**
     *生成小时
     */
    private void reCalculationHour(long startSecond,long endSecond){
        mHourList.clear();
        mHourSecond.clear();
        int startHour = TimeHelper.getCurrentCalendar(startSecond).get(Calendar.HOUR_OF_DAY);
        int endHour = TimeHelper.getCurrentCalendar(endSecond).get(Calendar.HOUR_OF_DAY);

        for (int i = startHour; i <= endHour; i++) {
            mHourList.add(formatHour(i));
            if (i == startHour){            //开始值
                mHourSecond.add(startSecond);
            }else if (i == endHour){        //结束值
                mHourSecond.add(endSecond);
            }else {                         //中间值
                //使用起始时间构造整时 时间戳
                Calendar  interCalendar= TimeHelper.getCurrentCalendar(startSecond);
                //设置小时
                interCalendar.set(Calendar.HOUR_OF_DAY,i);
                //设置分钟
                interCalendar.set(Calendar.MINUTE,0);
                //设置秒
                interCalendar.set(Calendar.SECOND,0);
                mHourSecond.add(interCalendar.getTime().getTime());
            }
        }

    }


    /**
     *格式化小时
     */
    private String formatHour(int hour){
        if (hour >= 24){
            hour = hour % 24;
        }

        if (hour < 10){
            return "0"+hour;
        }
        return hour+"";
    }

    /**
     * 格式化分钟
     */
    private String formatMinute(int minute){
        if (minute >= 60){
            minute = minute % 60;
        }

        if (minute < 10){
            return "0"+minute;
        }
        return minute+"";
    }


    final SimpleChoiceDialog.OnSelectedListener mListener = new SimpleChoiceDialog.OnSelectedListener() {
        @Override
        public void onSelected(int type, int position) {
            switch (type){
                case SimpleChoiceDialog.TYPE_CHOICE_SERVER:
                    mServerIndex = position;
                    mServerName.setText(mServerList.get(position));

                    break;
                case SimpleChoiceDialog.TYPE_CHOICE_MODE:
                    if (position != mGameModeIndex){
                        mLoadingDialog.show();
                        mGameModeIndex = position;
                        //更新订单价格
                        refreshOrderPrice();
                        mGameModeName.setText(mGameModeList.get(position));
                    }
                    break;
                case SimpleChoiceDialog.TYPE_CHOICE_RANK:
                    if (mRankIndex != position){
                        mLoadingDialog.show();
                        mRankIndex = position;
                        //更新订单价格
                        refreshOrderPrice();
                        mRankName.setText(mRankList.get(position));
                    }

                    break;
                case SimpleChoiceDialog.TYPE_CHOICE_COUNT:

                    if(mGameCountIndex != position){
                        mGameCountIndex = position;
                        //更新订单价格
                        refreshOrderPrice();
                        mGameCount.setText(mGameCountList.get(position));
                    }

                    break;
                case SimpleChoiceDialog.TYPE_CHOICE_HOUR:
                    mHourIndex = position;
                    //选择后 根据当前的小时 重新计算分钟列表

                    reCalculationMinute(mIntervalTime,mHourIndex);

                    refreshTimeText();

                    break;
                case SimpleChoiceDialog.TYPE_CHOICE_MINUTE:
                    mMinuteIndex = position;
                    //选择后 根据当前的小时 重新计算分钟列表
                    reCalculationMinute(mIntervalTime,mHourIndex);

                    refreshTimeText();
                    break;
            }
        }
    };

    private void refreshOrderPrice(){
        if (mOptions == null){
            return;
        }

        int rankValue = -1;
        int modeValue = -1;
        if (mOptions.getGameLevelMap() != null && mOptions.getGameLevelMap().size() > mRankIndex){
            rankValue = mOptions.getGameLevelMap().get(mRankIndex).getValue();
        }

        if (mOptions.getGameModeMap() != null && mOptions.getGameLevelMap().size() > mGameModeIndex){
            modeValue = mOptions.getGameModeMap().get(mGameModeIndex).getValue();
        }
        if (rankValue != -1 && modeValue != -1){
            //更新订单价格
            DataManager.getPreviewOrderPrice(getMember_id(),rankValue,modeValue);
        }
    }

    /**
     * 下单
     */
    public void createOrder(){
        if (mLoadingDialog == null){
            mLoadingDialog = new LoadingDialog(this);
            mLoadingDialog.setProgressText("加载中...");

        }
        if (!mLoadingDialog.isShowing()){
            mLoadingDialog.show();
        }
        //
        if (mOptions == null){
            return;
        }

        long startTime = 0L;

        try {

            String year = TimeHelper.getSysMessageTime(mHourSecond.get(mHourIndex)+"");
            //转换成秒的时间戳
            startTime = TimeHelper.getSecondTime(year+" "+mHourList.get(mHourIndex)+":"+mMinuteList.get(mMinuteIndex))/1000;
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (startTime == 0){
            return;
        }
        try {
            DataManager.createPlayWithOrder(getMember_id(),
                    mCoachId,
                    mOptions.getGameAreaMap().get(mServerIndex).getValue(),
                    mOptions.getGameLevelMap().get(mRankIndex).getValue(),
                    mOptions.getGameModeMap().get(mGameModeIndex).getValue(),
                    startTime,
                    Integer.parseInt(mGameCountList.get(mGameCountIndex)));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void chatWith(){
        Member user = getUser();

        if (user == null || StringUtil.isNull(getMember_id())){
            DialogManager.showLogInDialog(this);
            return;
        }

        if (getMember_id().equals(mCoachId)){
            ToastHelper.s("不能和自己聊天哦~");
            return;
        }
        if (!Proxy.getConnectManager().isConnect()) {
            FeiMoIMHelper.Login(user.getMember_id(), user.getNickname(), user.getAvatar());
        }

        IMSdk.createChat(this,mCoachId,mCoachNickName,mCoachAvatar, ChatActivity.SHOW_FAST_REPLY);
    }

    /**
     *提交订单
     */
    public void onEventMainThread(PayNowEvent event){
        if (event.getPage() == PAGE_CREATE_ORDER){
            createOrder();
        }
    }

    /**
     *价格回调
     */
    private float mTotal;
    public void onEventMainThread(PlayWithOrderPriceEntity entity){
        if (entity != null){
            mPrice.setText(entity.getPrice()+" 魔币");
            try {
                int count = Integer.parseInt(mGameCountList.get(mGameCountIndex));
                mPriceTotal.setText(Html.fromHtml(TextUtil.toColor(entity.getPrice()*count+"","#fc3c2e")+" 魔币"));
                mTotal = entity.getPrice()*count;
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
        mLoadingDialog.dismiss();
    }

    /**
     *订单时间选项
     */
    public void onEventMainThread(OrderTimeEntity entity){
        if (mLoadingDialog.isShowing()){
            mLoadingDialog.dismiss();
        }

        //时间真的发生改变
        if (entity.isResult() && mStartSecond != entity.getStartTime() && mEndSecond != entity.getEndTime()){
            mStartSecond = entity.getStartTime();
            mEndSecond = entity.getEndTime();
            mIntervalTime = entity.getIntervalTime();

            reCalculationHour(mStartSecond,mEndSecond);

            reCalculationMinute(mIntervalTime,mHourIndex);

            //数据更新了 重新默认选择第一个
            if (mGameTimeDialog != null){
                mGameTimeDialog.setMinutePosition(0);
                mGameTimeDialog.setHourPosition(0);
                mGameTimeDialog.notifyDataSetChange();
            }

            mHourIndex = 0;
            mMinuteIndex = 0;

            refreshTimeText();
        }else {
            ToastHelper.s(entity.getMsg());
        }
    }

    /**
     *订单选项
     */
    public void onEventMainThread(PlayWithOrderOptionsEntity entity){
        if (entity != null){
            mOptions = entity;
            refreshOptions();
        }
    }

    private PlayWithOrderEntity mOrderEntity;
    /**
     *订单生成结果
     */
    public void onEventMainThread(PlayWithOrderEntity entity){
        if (entity != null && entity.isResult()){
            mOrderEntity = entity;
            //支付订单
            DataManager.confirmOrder(getMember_id(),entity.getOrder().getId()+"");
        }else {
            ToastHelper.l("订单生成失败，请稍后再试哦~");
            if (mConfirmDialog != null && mConfirmDialog.isShowing()){
                mConfirmDialog.dismiss();
            }
            mLoadingDialog.dismiss();
        }
    }


    /**
     *订单支付结果
     */
    public void onEventMainThread(ConfirmOrderEntity entity){
        if (entity != null && entity.isResult()){
            ToastHelper.l("下单成功啦~");
            if (mConfirmDialog != null && mConfirmDialog.isShowing()){
                mConfirmDialog.dismiss();
            }

            //更新魔币余额
            Member user =getUser();
            if (user != null){
                user.setCoin(entity.getResidue_coin()+"");
            }
            PreferencesHepler.getInstance().saveUserProfilePersonalInformation(user);
            ActivityManager.startPlayWithOrderDetailActivity(this,mOrderEntity.getOrder().getId()+"",PlayWithOrderDetailActivity.ROLE_OWNER,true);
        }else {
            ToastHelper.l("订单生成失败，请稍后再试哦~");
            if (mConfirmDialog != null && mConfirmDialog.isShowing()){
                mConfirmDialog.dismiss();
            }
        }
        mLoadingDialog.dismiss();
    }
}
