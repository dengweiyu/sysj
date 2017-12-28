package com.li.videoapplication.ui.activity;


import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.ifeimo.im.activity.ChatActivity;
import com.ifeimo.im.framwork.IMSdk;
import com.ifeimo.im.framwork.Proxy;
import com.li.videoapplication.R;
import com.li.videoapplication.data.DataManager;
import com.li.videoapplication.data.cache.RequestCache;
import com.li.videoapplication.data.image.GlideHelper;
import com.li.videoapplication.data.model.entity.Member;
import com.li.videoapplication.data.model.entity.NetworkError;
import com.li.videoapplication.data.model.event.PayNowEvent;
import com.li.videoapplication.data.model.response.CoachListEntity;
import com.li.videoapplication.data.model.response.ConfirmOrderEntity;
import com.li.videoapplication.data.model.response.OrderTimeEntity;
import com.li.videoapplication.data.model.response.PlayWithOrderDetailEntity;
import com.li.videoapplication.data.model.response.PlayWithOrderEntity;
import com.li.videoapplication.data.model.response.PlayWithOrderOptionsEntity;
import com.li.videoapplication.data.model.response.PlayWithOrderPriceEntity;
import com.li.videoapplication.data.network.RequestParams;
import com.li.videoapplication.data.network.RequestUrl;
import com.li.videoapplication.data.preferences.PreferencesHepler;
import com.li.videoapplication.framework.TBaseAppCompatActivity;
import com.li.videoapplication.mvp.adapter.ChoiceOptionAdapter;
import com.li.videoapplication.tools.FeiMoIMHelper;
import com.li.videoapplication.tools.TimeHelper;
import com.li.videoapplication.tools.ToastHelper;
import com.li.videoapplication.ui.ActivityManager;
import com.li.videoapplication.ui.DialogManager;
import com.li.videoapplication.ui.dialog.ConfirmPlayWithDialog;
import com.li.videoapplication.ui.dialog.LoadingDialog;
import com.li.videoapplication.ui.dialog.SimpleChoiceDialog;
import com.li.videoapplication.ui.dialog.SimpleDoubleChoiceDialog;
import com.li.videoapplication.ui.dialog.UploadVideoPhoneDialog;
import com.li.videoapplication.ui.view.SpanItemDecoration;
import com.li.videoapplication.utils.MD5Util;
import com.li.videoapplication.utils.ScreenUtil;
import com.li.videoapplication.utils.StringUtil;
import com.li.videoapplication.utils.TextUtil;

import java.util.Calendar;
import java.util.List;

/**
 * 创建陪玩订单
 */

public class CreatePlayWithOrderActivity extends TBaseAppCompatActivity implements View.OnClickListener {

    public final static int PAGE_CREATE_ORDER = 1;
    public final static int PAGE_ORDER_DETAIL = 2;

    public final static int MODE_ORDER_NORMAL= 10;          //普通模式
    public final static int MODE_ORDER_GRAB= 11;            //抢单模式
    public final static int MODE_ORDER_AGAIN = 12;          //抢单模式订单结束后，点击"继续找TA"。仍为抢单模式只是UI显示有区别

    private View mOperation;

    private LoadingDialog mLoadingDialog;

    private SimpleChoiceDialog mGameRankDialog;

    private SimpleChoiceDialog mGameCountDialog;

    private SimpleDoubleChoiceDialog mGameTimeDialog;

    private ConfirmPlayWithDialog mConfirmDialog;

    private UploadVideoPhoneDialog mBindPhoneDialog;

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
    private TextView mNotice;
    private View mTopLayoutDiscount;
    private TextView mTopDiscountMessage;
    private View mLayoutDiscount;
    private TextView mDiscountMessage;
    private RecyclerView mServerNameList;
    private RecyclerView mModeNameList;

    private ChoiceOptionAdapter mServerNameAdapter;
    private ChoiceOptionAdapter mModeNameAdapter;

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

    private String mCoachQQ;

    private PlayWithOrderDetailEntity.CoachBean mCoachBean;

    private CoachListEntity.DataBean.IncludeBean mCoachEntity;

    private long mStartSecond;

    private long mSecondTime;   //对开始时间取整5后

    private long mEndSecond;

    private long mIntervalTime;

    private int mCurrentOrderMode = MODE_ORDER_NORMAL;      //当前订单模式
    @Override
    public void refreshIntent() {
        super.refreshIntent();
        getExtraData();
    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        getExtraData();
        refreshViewByMode();
    }


    private void getExtraData(){
        try {
            Intent intent = getIntent();
            mCoachId = intent.getStringExtra("coach_id");
            mCoachNickName = intent.getStringExtra("nick_name");
            mCoachAvatar = intent.getStringExtra("avatar");
            mCoachQQ = intent.getStringExtra("qq");
            mCurrentOrderMode = intent.getIntExtra("order_mode",MODE_ORDER_NORMAL);
            //只有在点击【继续选择TA】后会传递教练所有信息过来
            mCoachBean  = (PlayWithOrderDetailEntity.CoachBean)intent.getSerializableExtra("coach_bean");

            if(mCoachBean != null){
                mCoachId = mCoachBean.getMember_id();
            }
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

        mNotice = (TextView)findViewById(R.id.tv_order_create_notice);
        mServerName = (TextView)findViewById(R.id.tv_server_name);
        mGameModeName = (TextView)findViewById(R.id.tv_mode_name);
        mRankName = (TextView)findViewById(R.id.tv_rank_name);
        mGameCount = (TextView)findViewById(R.id.tv_game_count);
        mGameTime = (TextView)findViewById(R.id.tv_start_time);
        mPrice = (TextView)findViewById(R.id.tv_single_price);
        mPriceTotal = (TextView)findViewById(R.id.tv_price_total) ;
        mOriginalPrice = (TextView)findViewById(R.id.tv_original_price);
        mLayoutDiscount = findViewById(R.id.ll_discount_message);
        mDiscountMessage = (TextView)findViewById(R.id.tv_discount_message);

        mTopLayoutDiscount = findViewById(R.id.ll_discount_top_msg);
        mTopDiscountMessage = (TextView)findViewById(R.id.tv_discount_top_msg);
        //添加横线
        mOriginalPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG|Paint.ANTI_ALIAS_FLAG);

        mServerNameList = (RecyclerView)findViewById(R.id.rv_server_name_list);
        mModeNameList = (RecyclerView)findViewById(R.id.rv_mode_name_list);

        mServerNameList.setLayoutManager(new GridLayoutManager(this,2));
        mModeNameList.setLayoutManager(new GridLayoutManager(this,2));

        mServerNameList.addItemDecoration(new SpanItemDecoration(ScreenUtil.dp2px(this,12),false,true,false,false));
        mModeNameList.addItemDecoration(new SpanItemDecoration(ScreenUtil.dp2px(this,12),false,true,false,false));

        findViewById(R.id.ll_choice_my_rank).setOnClickListener(this);
        findViewById(R.id.ll_choice_game_count).setOnClickListener(this);
        findViewById(R.id.ll_choice_start_time).setOnClickListener(this);

        TextView chat = (TextView) findViewById(R.id.tv_chat_with_coach);
        chat.setOnClickListener(this);
        TextView createOrder = (TextView) findViewById(R.id.tv_coach_selected);
        createOrder.setText("立即下单");
        createOrder.setOnClickListener(this);

        mLoadingDialog = new LoadingDialog(this);

        mServerNameList.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                mServerNameAdapter.setIsSelected(position);
                mServerNameAdapter.notifyDataSetChanged();
                mServerIndex = position;
            }
        });

        mModeNameList.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                mModeNameAdapter.setIsSelected(position);
                mModeNameAdapter.notifyDataSetChanged();
                mGameModeIndex = position;
            }

        });


        refreshViewByMode();
    }


    private void initToolbar(){
        findViewById(R.id.tb_back).setOnClickListener(this);
        ((TextView)findViewById(R.id.tb_title)).setText("订单信息");
        setSupportActionBar(((Toolbar)findViewById(R.id.toolbar)));
    }

    /**
     * 根据模式重新渲染UI
     */
    private void refreshViewByMode(){

        switch (mCurrentOrderMode){
            case MODE_ORDER_AGAIN:
                if (mCoachBean == null){
                    return;
                }
                findViewById(R.id.ll_order_status_root).setVisibility(View.GONE);
                View layout = findViewById(R.id.rl_create_header_coach_info);
                layout.setVisibility(View.VISIBLE);

                final ImageView icon = (ImageView)layout.findViewById(R.id.civ_coach_detail_icon);
                TextView nickName = (TextView)layout.findViewById(R.id.tv_coach_detail_nick_name);
                TextView score = (TextView)layout.findViewById(R.id.tv_coach_detail_score);
                RatingBar ratingBar = (RatingBar)layout.findViewById(R.id.rb_coach_detail_score);
                score.setText(mCoachBean.getScore()+"分");
                nickName.setText(mCoachBean.getNickname());

                GlideHelper.displayImage(CreatePlayWithOrderActivity.this,mCoachBean.getAvatar(),icon);
                try {
                    ratingBar.setRating(Float.parseFloat(mCoachBean.getScore()));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case MODE_ORDER_GRAB:
                TextView chat = (TextView) findViewById(R.id.tv_chat_with_coach);
                chat.setVisibility(View.GONE);
                //抢单模式下隐藏时间选择
                findViewById(R.id.ll_choice_start_time).setVisibility(View.GONE);
                break;

        }
    }

    @Override
    public void loadData() {
        super.loadData();
        setOptionsByCache();
        //load options
        DataManager.getPlayWithOrderOptions(mCoachId);
        //load time
        DataManager.getOrderTime(null);
    }

    //use cache first
    private void setOptionsByCache(){
        String data =  RequestCache.get(RequestUrl.getInstance().getPlayWithOrderOptions(), RequestParams.getInstance().getPlayWithOrderOptions(mCoachId));
        if (!StringUtil.isNull(data)){
            Gson gson = new Gson();
            try {
                mOptions = gson.fromJson(data,PlayWithOrderOptionsEntity.class);
                if (mOptions != null){
                    refreshOptions(true);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void refreshOptions(boolean isByCache){
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
                mGameCountIndex = i - 1;
            }
        }

        mServerNameAdapter = new ChoiceOptionAdapter(mServerList);
        mModeNameAdapter = new ChoiceOptionAdapter(mGameModeList);
        mServerNameList.setAdapter(mServerNameAdapter);
        mModeNameList.setAdapter(mModeNameAdapter);

      /*  if (mServerList.size() > mServerIndex){
            mServerName.setText(mServerList.get(mServerIndex));
        }

        if (mGameModeList.size() > mGameModeIndex){
            mGameModeName.setText(mGameModeList.get(mGameModeIndex));
        }
*/
        if (mRankList.size() > mRankIndex){
            mRankName.setText(mRankList.get(mRankIndex));
        }

        if (mGameCountList.size() > mGameCountIndex){
            mGameCount.setText(mGameCountList.get(mGameCountIndex));
        }

        if (mOptions.getNotice() != null){
            mNotice.setText(mOptions.getNotice());
        }

        //缓存数据 则不更新价格显示
        if (!isByCache){
            //更新订单价格
            refreshOrderPrice();
        }

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
            case R.id.ll_choice_my_rank:                            //选择分段
                showGameRankDialog();
                break;
            case R.id.ll_choice_game_count:                         //选择局数
                showGameCountDialog();
                break;
            case R.id.ll_choice_start_time:                         //选择时间
                DataManager.getOrderTime(null);
                if (mHourSecond.size() == 0){
                    mLoadingDialog.show();
                    return;
                }

                reCalculationMinute(mIntervalTime,mHourIndex);
                showGameTimeDialog();
                break;
            case R.id.tv_coach_selected:                            //立即下单

                if (!isLogin()){
                    DialogManager.showLogInDialog(CreatePlayWithOrderActivity.this);
                    return;
                }
                if (mIsDiscount){
                    final Member member = getUser();
                    // 验证是否绑定手机
                    if (StringUtil.isNull(member.getMobile())){
                        showBindPhoneDialog();
                        return;
                    }
                }

                //立即下单对话框
                showConfirmDialog(mTotal);
                break;
            case R.id.tv_chat_with_coach:                           //私聊
                chatWith();
                break;
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
        float coin  = 0;
        if (getUser() != null){
            try {
                coin = Float.parseFloat(getUser().getCoin());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        mConfirmDialog = new ConfirmPlayWithDialog(this, price, coin,PAGE_CREATE_ORDER);
        if (!mConfirmDialog.isShowing()){
            mConfirmDialog.show();
        }
    }

    public void showBindPhoneDialog(){
        final Member member = getUser();

        if (mBindPhoneDialog == null){
            mBindPhoneDialog = new UploadVideoPhoneDialog(CreatePlayWithOrderActivity.this,new UploadVideoPhoneDialog.Callback() {
                @Override
                public void onCall(String phone) {
                    member.setMobile(phone);
                    DataManager.userProfileFinishMemberInfo(member);
                    // 保存数据到Preference
                    PreferencesHepler.getInstance().saveUserProfilePersonalInformation(member);

                    //立即下单对话框
                    showConfirmDialog(mTotal);
                }
            });
            ((TextView) mBindPhoneDialog.findViewById(R.id.tv_bind_phone_tip)).setText("为了保障您更好的享受优惠，需要绑定手机");
        }

        mBindPhoneDialog.show();
    }

    /**
     * 更新时间显示
     */
    private void refreshTimeText(){
        if (mHourList.size() > 0 && mMinuteList.size() > 0){

            String timeStr = "";

            if (mHourList.size() > 0 && mHourIndex < mHourList.size() ){
                timeStr +=  mHourList.get(mHourIndex);
            }

            if (mMinuteList.size() > 0 && mMinuteIndex < mMinuteList.size()){
                timeStr += ":"+mMinuteList.get(mMinuteIndex);
            }
            mGameTime.setText(timeStr);
        }
    }

    /**
     *小时发生改变了
     */
    public void onCurrentHourChange(int position){

        reCalculationMinute(mIntervalTime,position);

        if (mGameTimeDialog != null){
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

        if (isStart){
            int startMinute = TimeHelper.getCurrentCalendar(second).get(Calendar.MINUTE);
            int lastMinute = startMinute;

            while(lastMinute < 60){
                mMinuteList.add(formatMinute(lastMinute));
                if (lastMinute == startMinute){         //
                    int secondMinute = TimeHelper.getCurrentCalendar(mSecondTime).get(Calendar.MINUTE);
                    if (secondMinute > startMinute  && startMinute %(duration/60) !=0){
                        mMinuteList.add(formatMinute(secondMinute));
                        lastMinute = secondMinute;
                    }
                }

                //根据间隔 生成分钟
                lastMinute += duration/60;
            }
        }else {
            int endMinute = TimeHelper.getCurrentCalendar(second).get(Calendar.MINUTE);
            int minute = 0;

            while(minute <= endMinute){
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
        long second = startSecond;

        int endHour = TimeHelper.getCurrentCalendar(endSecond).get(Calendar.HOUR_OF_DAY);
        while(second <= endSecond){
            int hour = TimeHelper.getCurrentCalendar(second).get(Calendar.HOUR_OF_DAY);
            mHourList.add(formatHour(hour));
            Calendar ca = TimeHelper.getCurrentCalendar(second);


            if (second == startSecond ){
                //当前时间
                mHourSecond.add(second);

                //取整到整5倍数的开始时间
                second = mSecondTime;
            }else if (ca.get(Calendar.HOUR_OF_DAY) == endHour){
                mHourSecond.add(endSecond);
            }else{
                ca.set(Calendar.MINUTE,0);
                //保存时间戳
                mHourSecond.add(ca.getTime().getTime()/1000);
            }
            //直接增加一个小时
            second += 3600;
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
            if (mGameCountList.size() > mGameCountIndex){
                //更新订单价格
                DataManager.getPreviewOrderPrice(getMember_id(),rankValue,modeValue,Integer.parseInt(mGameCountList.get(mGameCountIndex)));
            }

        }
    }


    private String mOrderMD5;
    /**
     * 下单
     */
    public void createOrder(){
        //
        if (mHourSecond.size() == 0){
            ToastHelper.s("请选择陪练开始时间");
            return;
        }

        if(mGameCountList.size() == 0){
            ToastHelper.s("请选择陪练局数");
        }

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
        String startTimeStr="";
        try {

            String year = TimeHelper.getSysMessageTime(mHourSecond.get(mHourIndex)+"");
            //转换成秒的时间戳
            startTime = TimeHelper.getSecondTime(year+" "+mHourList.get(mHourIndex)+":"+mMinuteList.get(mMinuteIndex));
            startTimeStr = ""+startTime;
            startTimeStr = startTimeStr.substring(0,startTimeStr.length()-3);
        } catch (Exception e) {
            e.printStackTrace();

            return;
        }
        //抢单模式下不验证时间
        if (mCurrentOrderMode != MODE_ORDER_GRAB){
            if (startTime == 0L || StringUtil.isNull(startTimeStr)){
                return;
            }
        }


        //生成MD5  避免重复提交
        String orderMD5 = MD5Util.string2MD5(
                        mCoachId+
                        mOptions.getGameAreaMap().get(mServerIndex).getValue()+
                        mOptions.getGameLevelMap().get(mRankIndex).getValue()+
                        mOptions.getGameModeMap().get(mGameModeIndex).getValue()+
                        startTimeStr+
                        Integer.parseInt(mGameCountList.get(mGameCountIndex)));

        if (orderMD5.equals(mOrderMD5)){
            ToastHelper.s("您已提交订单，请勿重复提交");
            return;
        }

        mOrderMD5 = orderMD5;

        try {
            DataManager.createPlayWithOrder(getMember_id(),
                    mCoachId,
                    mOptions.getGameAreaMap().get(mServerIndex).getValue(),
                    mOptions.getGameLevelMap().get(mRankIndex).getValue(),
                    mOptions.getGameModeMap().get(mGameModeIndex).getValue(),
                    startTimeStr,
                    Integer.parseInt(mGameCountList.get(mGameCountIndex)),
                    mCurrentOrderMode == MODE_ORDER_GRAB ? 2:1);        //1 => 普通模式 2 => 抢单模式
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
            FeiMoIMHelper.Login(user.getId(), user.getNickname(), user.getAvatar());
        }

        FeiMoIMHelper.Login(user.getId(), user.getNickname(), user.getAvatar());
        IMSdk.createChat(this,mCoachId,mCoachNickName,mCoachAvatar, ChatActivity.SHOW_FAST_REPLY,mCoachQQ);
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
    private boolean mIsDiscount = false;
    public void onEventMainThread(PlayWithOrderPriceEntity entity){
        if (entity != null){

            mPrice.setText(entity.getSign_price()+" 魔币");

            mPriceTotal.setText(Html.fromHtml(TextUtil.toColor(entity.getPrice()+"","#fc3c2e")+" 魔币"));
            mTotal = entity.getPrice();

            if (mTotal == entity.getOriginal_price()){
                mIsDiscount = false;
            }else {
                mIsDiscount = true;
            }

            if (entity.isDiscount()){
                mLayoutDiscount.setVisibility(View.VISIBLE);
                mDiscountMessage.setText(entity.getPromotionMsg());
                mOriginalPrice.setText("  "+entity.getOriginal_price()+"魔币  ");
                mOriginalPrice.setVisibility(View.VISIBLE);

                if (StringUtil.isNull(entity.getTopMsg())){
                    mTopLayoutDiscount.setVisibility(View.GONE);
                }else {
                    mTopLayoutDiscount.setVisibility(View.VISIBLE);
                    mTopDiscountMessage.setText(entity.getTopMsg());
                }

            }else {
                mLayoutDiscount.setVisibility(View.GONE);
                mOriginalPrice.setVisibility(View.GONE);
                mOriginalPrice.setVisibility(View.GONE);
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
            mSecondTime = entity.getSecondTime();

/*            mStartSecond = 1503367080L;
            mEndSecond = 1503377880L;*/

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
            refreshOptions(false);
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
            ToastHelper.s("下单失败啦，请稍后再试哦");


            if (mConfirmDialog != null && mConfirmDialog.isShowing()){
                mConfirmDialog.dismiss();
            }
            mLoadingDialog.dismiss();
        }

        mOrderMD5 = null;
    }


    /**
     *订单支付结果
     */
    public void onEventMainThread(ConfirmOrderEntity entity){
        if (entity != null && entity.isResult()){
            ToastHelper.s("下单成功啦~");
            if (mConfirmDialog != null && mConfirmDialog.isShowing()){
                mConfirmDialog.dismiss();
            }

            //更新魔币余额
            Member user =getUser();
            if (user != null){
                user.setCoin(entity.getResidue_coin()+"");
            }
            PreferencesHepler.getInstance().saveUserProfilePersonalInformation(user);
            ActivityManager.startPlayWithOrderDetailActivity(this,entity.getOrder_id()+"",PlayWithOrderDetailActivity.ROLE_OWNER,true);

            //更新优惠信息
            refreshOptions(false);

        }else {
            if (entity != null){
                ToastHelper.s(entity.getMsg());
            }else {
                ToastHelper.s("订单生成失败，请稍后再试哦~");
            }
            if (mConfirmDialog != null && mConfirmDialog.isShowing()){
                mConfirmDialog.dismiss();
            }
        }
        mLoadingDialog.dismiss();
    }

    /**
     *网络错误
     */
    public void onEventMainThread(NetworkError error){
        mOrderMD5 = null;
    }
}
