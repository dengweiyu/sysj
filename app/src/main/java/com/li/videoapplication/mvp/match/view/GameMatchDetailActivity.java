package com.li.videoapplication.mvp.match.view;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.ifeimo.im.framwork.IMSdk;
import com.ifeimo.im.framwork.Proxy;
import com.ifeimo.im.framwork.message.OnGroupItemOnClickListener;
import com.li.videoapplication.R;
import com.li.videoapplication.data.DataManager;
import com.li.videoapplication.data.model.entity.Match;
import com.li.videoapplication.data.model.entity.Member;
import com.li.videoapplication.data.model.event.LoginEvent;
import com.li.videoapplication.data.model.response.ServiceNameEntity;
import com.li.videoapplication.data.model.response.SignSchedule210Entity;
import com.li.videoapplication.data.model.response.SignScheduleEntity;
import com.li.videoapplication.data.preferences.PreferencesHepler;
import com.li.videoapplication.framework.AppConstant;
import com.li.videoapplication.framework.BaseHttpResult;
import com.li.videoapplication.framework.TBaseAppCompatActivity;
import com.li.videoapplication.mvp.Constant;
import com.li.videoapplication.mvp.match.MatchContract.IMatchDetailView;
import com.li.videoapplication.mvp.match.presenter.MatchPresenter;
import com.li.videoapplication.tools.FeiMoIMHelper;
import com.li.videoapplication.tools.RongIMHelper;
import com.li.videoapplication.tools.TimeHelper;
import com.li.videoapplication.tools.ToastHelper;
import com.li.videoapplication.tools.UmengAnalyticsHelper;
import com.li.videoapplication.ui.ActivityManager;
import com.li.videoapplication.ui.DialogManager;
import com.li.videoapplication.ui.activity.ConversationActivity;
import com.li.videoapplication.ui.fragment.GameMatchRulesFragment;
import com.li.videoapplication.ui.pageradapter.ViewPagerAdapter;
import com.li.videoapplication.utils.NetUtil;
import com.li.videoapplication.utils.StringUtil;
import com.li.videoapplication.views.ViewPagerY4;
import com.li.videoapplication.views.bubblelayout.BubbleLayout;
import com.li.videoapplication.views.bubblelayout.BubblePopupHelper;
import com.umeng.analytics.AnalyticsConfig;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import butterknife.BindView;
import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient.ConnectionStatusListener.ConnectionStatus;
import io.rong.imlib.model.Conversation;
import me.everything.android.ui.overscroll.HorizontalOverScrollBounceEffectDecorator;
import me.everything.android.ui.overscroll.adapters.ViewPagerOverScrollDecorAdapter;

/**
 * 活动：赛事详情
 */
public class GameMatchDetailActivity extends TBaseAppCompatActivity implements IMatchDetailView,
        View.OnClickListener,
        OnPageChangeListener {

    @BindView(R.id.header)
    ImageView header;
    @BindView(R.id.gamematch_refresh)
    ImageView refresh;
    @BindView(R.id.gamematch_refreshline)
    View refreshLine;
    @BindView(R.id.gamematch_con)
    LinearLayout con;
    @BindView(R.id.gamematch_viewpager)
    ViewPagerY4 mViewPager;
    @BindView(R.id.gamematch_signup)
    TextView cardViewText;

    public Match match;
    private List<Fragment> fragments;
    private GameMatchRulesFragment rulesFragment;
    private GameMatchProcessFragment processFragment;

    public String event_id;
    private String customerServiceID, customerServiceName,customerServiceIcon;
    private PopupWindow popupWindow;
    private BubbleLayout bubbleLayout;
    private MatchPresenter presenter;
    private LinearLayout btnBar;
    private boolean isFirstIn = true;

    /**
     * 跳转：报名
     */
    private void startSignUpActivity() {
        ActivityManager.startSignUpActivity(this, event_id, match, customerServiceID, customerServiceName);
        UmengAnalyticsHelper.onEvent(this, UmengAnalyticsHelper.MATCH, "报名次数");
    }

    /**
     * 跳转：我的赛程
     */
    private void startMyMatchProcessActivity(Match match) {
        ActivityManager.startMyMatchProcessActivity(this, match, customerServiceID, customerServiceName);
    }

    /**
     * 页面跳转：安装应用
     */
    private void install() {
        if (match != null && match.getGame_id() != null) {
           /* Uri uri = Uri.parse(match.getAndroid_address());
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
            overridePendingTransition(R.anim.activity_hold, R.anim.activity_hold);*/
            ActivityManager.startDownloadManagerActivity(this,match.getGame_id(),"3",match.getGame_id());
            // 游戏下载数+1
            DataManager.downloadClick217(match.getGame_id(), getMember_id(),
                    Constant.DOWNLOAD_LOCATION_MATCH, event_id);
        }
        UmengAnalyticsHelper.onEvent(this, UmengAnalyticsHelper.MATCH, "赛事导航-游戏下载");
    }

    /**
     * 分享
     */
    public void startShareActivity() {
        if (match != null) {
            final String url = match.getShare_url();
            final String title = "精彩赛事分享";
            final String imageUrl = match.getShare_icon();
            final String content = match.getShare_description();

            ActivityManager.startActivityShareActivity4VideoPlay(this, url, title, imageUrl, content);
            presenter.eventsRecordClick(event_id, 15);//赛事流水点击:15为app分享
        }
        UmengAnalyticsHelper.onEvent(this, UmengAnalyticsHelper.MATCH, "赛事导航-赛事分享");
    }

    @Override
    public void refreshIntent() {
        super.refreshIntent();
        try {
            event_id = getIntent().getStringExtra("event_id");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        try {
            event_id = getIntent().getStringExtra("event_id");
        } catch (Exception e) {
            e.printStackTrace();
        }
        loadData();
    }

    @Override
    public int getContentView() {
        return R.layout.activity_gamematchdetail;
    }

    @Override
    public void afterOnCreate() {
        setSystemBarBackgroundColor(Color.BLACK);
        super.afterOnCreate();


    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (mViewPager.getCurrentItem() == 0)
            loadData();
        if (mViewPager.getCurrentItem() == 1)
            cardViewText.setText("我的赛程");
        if (popupWindow != null && popupWindow.isShowing())
            popupWindow.dismiss();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.eventsRecordClick(event_id, 10);//赛事流水点击:10为该页面点击次数
    }

    @Override
    public void initView() {
        super.initView();
        initToolbar();
        btnBar = (LinearLayout) findViewById(R.id.gamematch_btnbar);

        refresh.setOnClickListener(this);
        cardViewText.setOnClickListener(this);
        findViewById(R.id.gamematch_startchat).setOnClickListener(this);

        //客服菜单弹窗
        findViewById(R.id.gamematch_cs).setOnClickListener(this);
        bubbleLayout = (BubbleLayout) LayoutInflater.from(this).inflate(R.layout.gamematch_popup, null);
        bubbleLayout.findViewById(R.id.gamematch_popup_cs).setOnClickListener(this);
        bubbleLayout.findViewById(R.id.gamematch_popup_course).setOnClickListener(this);

        refreshImageView(true);
        initViewPager();

        presenter = MatchPresenter.getInstance();
        presenter.setMatchDetailView(this);
    }

    @Override
    public void loadData() {
        super.loadData();
        presenter.getEventsInfo(event_id, getMember_id());
    }

    private void initToolbar() {
        findViewById(R.id.tb_back).setOnClickListener(this);
        findViewById(R.id.tb_download).setOnClickListener(this);
        findViewById(R.id.tb_share).setOnClickListener(this);

        if (AppConstant.SHOW_DOWNLOAD_AD) {
            findViewById(R.id.tb_download).setVisibility(View.VISIBLE);
        } else {
            findViewById(R.id.tb_download).setVisibility(View.GONE);
        }

        if (isNeedHideDownload(this)){
            findViewById(R.id.tb_download).setVisibility(View.GONE);
        }

    }


    /**
     *
     */
    private boolean isNeedHideDownload(Context context){
        String channel = AnalyticsConfig.getChannel(context);
        if ("huawei".equalsIgnoreCase(channel) || "xiaomi".equalsIgnoreCase(channel) || "anzhi".equalsIgnoreCase(channel)){
            return true;
        }
        return false;
    }

    private void refreshImageView(boolean isFirstIn) {
        if (isFirstIn) {
            LayoutParams params = header.getLayoutParams();
            WindowManager windowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);

            //改为750:350
            int srceenWidth = windowManager.getDefaultDisplay().getWidth();
            params.width = srceenWidth;
            params.height = srceenWidth * 7 / 15;
            header.setLayoutParams(params);
        } else {
            //加载广告图片
            setImageViewImageNet(header, match.getFlag());
            showAnim();
        }
    }

    private void showAnim() {
        if (isFirstIn) {
            animationHelper.startCircularRevealAnim(header);
            animationHelper.beginFadeSlideDelayTransition(btnBar);
            animationHelper.beginFadeSlideDelayTransition(con);
            showVISIBLE(btnBar, con);
            isFirstIn = false;
        }
    }

    private void showVISIBLE(View... views) {
        for (View view : views) {
            view.setVisibility(View.VISIBLE);
        }
    }

    private void initViewPager() {
        if (fragments == null) {
            fragments = new ArrayList<>();

            rulesFragment = new GameMatchRulesFragment();
            processFragment = new GameMatchProcessFragment();

            fragments.add(rulesFragment);
            fragments.add(processFragment);
        }

        final String[] tabTitle = {"赛规", "赛程"};
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager(), fragments, tabTitle);
        mViewPager.setAdapter(adapter);
        mViewPager.addOnPageChangeListener(this);
        new HorizontalOverScrollBounceEffectDecorator(new ViewPagerOverScrollDecorAdapter(mViewPager));

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
    }

    private void setRuleFragmentData() {
        if (match != null) {
            if (match.getRule_url() != null) {
                rulesFragment.setRuleUrl(match.getRule_url());
            }
        }
    }

    private void refreshBtnColor() {
        if (mViewPager.getCurrentItem() == 0 && match != null) {
            //8:签到已截止，9：比赛结束，12：匹配已截止
            if (match.getBtn_status() == 9 || match.getBtn_status() == 12 || match.getBtn_status() == 8) {
                cardViewText.setBackgroundResource(R.drawable.gamematchdetail_btn_gray);
                refresh.setBackgroundResource(R.drawable.button_gray_right);//灰色B3B3B3
                refreshLine.setBackgroundColor(Color.parseColor("#898989"));//灰色898989
            } else {
                cardViewText.setBackgroundResource(R.drawable.button_red_left);//红色ff3d2e
                refresh.setBackgroundResource(R.drawable.button_red_right);
                refreshLine.setBackgroundColor(Color.parseColor("#ec0000"));//红色ec0000
            }
        } else {
            cardViewText.setBackgroundResource(R.drawable.button_red_left);//红色ff3d2e
            refresh.setBackgroundResource(R.drawable.button_red_right);
            refreshLine.setBackgroundColor(Color.parseColor("#ec0000"));
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.gamematch_signup:
                if (!isLogin()) {
                    DialogManager.showLogInDialog(this);
                    return;
                }
                if (mViewPager.getCurrentItem() == 0 && match != null) {//赛规页面
                    switch (match.getBtn_status()) {
                        case 1://立即报名
                            startSignUpActivity();
                            break;
                        case 2://进行中
                            ToastHelper.s("比赛正在火热进行中");
                            break;
                        case 3://立即签到
                            presenter.signSchedule(getMember_id(), match.getSchedule_id(), match.getEvent_id());
                            showLoadingDialog("签到中", "正在签到，请稍等...", false);
                            break;
                        case 4:// 对战表生成中
                            ToastHelper.s("对战表生成中，请等待比赛开始");
                            break;
                        case 5:// xx月xx日 xxxx-xxxx 参加比赛
                            break;
                        case 6:// 开始匹配对手
                            DialogManager.showMatchOpponentDialog(this, match.getSchedule_id());
                            break;
                        case 7:// xx月xx日 xxxx-xxxx 点击这里签到
                            String s;
                            try {
                                if (match == null){
                                    return;
                                }
                                String signStatrTime = TimeHelper.getWholeTimeFormat(match.getSign_starttime());
                                String signEndTime = TimeHelper.getTime2HmFormat(match.getSign_endtime());
                                s = "请于" + signStatrTime + "~" + signEndTime + "前进行签到";
                                ToastHelper.s(s);
                            } catch (Exception e) {
                                ToastHelper.s("请于规定时间内进行签到");
                            }
                            break;
                        case 8://签到已截止
                            break;
                        case 9://比赛结束
                            ToastHelper.s("比赛已结束，请关注下一个比赛");
                            break;
                        case 10://对手匹配中
                            ToastHelper.s("正在为您匹配对手，请耐心等待");
                            break;
                        case 11://匹配成功！点击这里约战TA
                            startMyMatchProcessActivity(match);
                            break;
                        case 12://匹配已截止
                            break;
                        case 13://前往我的赛程约战对手
                            startMyMatchProcessActivity(match);
                            break;
                        case 14://胜方请于xxx时间前上传截图
                            break;
                        case 15:// 对战表生成中
                            ToastHelper.s("对战表生成中");
                            break;
                    }
                } else if (mViewPager.getCurrentItem() == 1) {//赛程页面
                    startMyMatchProcessActivity(match);
                }
                break;
            case R.id.gamematch_refresh:
                try {
                    if (NetUtil.isConnect(this)) {
                        if (mViewPager.getCurrentItem() == 0) {
                            ToastHelper.s("刷新成功");
                            loadData();
                        } else if (mViewPager.getCurrentItem() == 1) {
                            processFragment.onRefresh();
                        }
                    } else {
                        ToastHelper.s(R.string.net_disable);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case R.id.tb_back:
                finish();
                break;
            case R.id.tb_download:
                install();
                break;
            case R.id.tb_share:
                startShareActivity();
                break;
            case R.id.gamematch_startchat:
                if (isLogin() && match != null) {
                    if (match.isPrivateIM()){
                        if (Proxy.getMessageManager().getOnGroupItemOnClickListener() == null) {
                            IMSdk.setOnGroupItemOnClick(new OnGroupItemOnClickListener() {
                                @Override
                                public void onGroupItemOnClick(String memberid, String name) {
                                  IMSdk.createChat(GameMatchDetailActivity.this,memberid,name,"");
                                }
                            });
                        }
                        if (!Proxy.getConnectManager().isConnect()) {
                            Member user = getUser();
                            String memberId = user.getMember_id();
                            if(null == memberId  || memberId.equals("")){
                                memberId = getMember_id();
                            }
                            FeiMoIMHelper.Login(memberId, user.getNickname(), user.getAvatar());
                        }


                        FeiMoIMHelper.createMuccRoom(this, match.getRoomJID(), match.getRoomName(), match.getRoomPicurl());
                    }else {
                        presenter.groupJoin(getMember_id(), match.getChatroom_group_id());
                    }

                    UmengAnalyticsHelper.onEvent(this, UmengAnalyticsHelper.MATCH, "群聊");
                } else {
                    DialogManager.showLogInDialog(this);
                }
                break;
            case R.id.gamematch_cs://客服教程弹窗
                popupWindow = BubblePopupHelper.create(this, bubbleLayout);
                int[] location = new int[2];
                v.getLocationInWindow(location);
                popupWindow.showAtLocation(v, Gravity.NO_GRAVITY, location[0], v.getHeight() + location[1]);
                break;
            case R.id.gamematch_popup_cs://客服
                if (!isLogin()) {
                    DialogManager.showLogInDialog(this);
                    return;
                }
                if (match != null&& customerServiceID != null){
                    if (match.isPrivateIM()){

                        if (!Proxy.getConnectManager().isConnect()) {
                            Member user = getUser();
                            String memberId = user.getMember_id();
                            if(null == memberId  || memberId.equals("")){
                                memberId = getMember_id();
                            }
                            FeiMoIMHelper.Login(memberId, user.getNickname(), user.getAvatar());
                        }

                        IMSdk.createChat(GameMatchDetailActivity.this,customerServiceID,customerServiceName,customerServiceIcon);

                    }else {
                        if (RongIM.getInstance() != null && customerServiceID != null) {
                            ActivityManager.startConversationActivity(this, customerServiceID, customerServiceName, false);
                            UmengAnalyticsHelper.onEvent(this, UmengAnalyticsHelper.MATCH, "客服");
                        }
                    }
                }

                break;
            case R.id.gamematch_popup_course://教程
                ActivityManager.startHelpActivity(this);
                UmengAnalyticsHelper.onEvent(this, UmengAnalyticsHelper.MATCH, "教程");
                break;
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
        refreshBtnColor();
        if (match != null){
            switch (position) {
                case 0:
                    con.setVisibility(View.VISIBLE);
                    cardViewText.setVisibility(View.VISIBLE);
                    cardViewText.setText(match.getBtn_text());
                    break;
                case 1:
                    con.setVisibility(View.VISIBLE);
                    cardViewText.setVisibility(View.VISIBLE);
                    cardViewText.setText("我的赛程");
                    break;
            }
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }

    private void showVictoryDialog() {
        if (match == null){
            return;
        }
        switch (match.getAlert_status()) {
            case 0://没有弹框
                break;
            case 1://有弹框非最后轮
                DialogManager.showVictoryDialog(this, match.getNext_time(), false, match.getSchedule_id());
                break;
            case 2://有弹框最后轮
                DialogManager.showVictoryDialog(this, "", true, match.getSchedule_id());
                break;
        }
    }

    /**
     * 回调：赛事详情
     */
    @Override
    public void refreshMatchDetailData(Match data) {
        match = data;
        refreshImageView(false);
        if (mViewPager.getCurrentItem() == 0)
            cardViewText.setText(match.getBtn_text());
        refreshBtnColor();
        setRuleFragmentData();
        getCustomerServiceID();
        showVictoryDialog();
    }

    private void getCustomerServiceID() {
        if (match == null){
            return;
        }
        String[] cs = match.getCustomer_service();
        PreferencesHepler.getInstance().saveCustomerServiceIDs(Arrays.asList(cs));//保存客服id集合
        int num = new Random().nextInt(cs.length);//随机获取一个客服ID
        customerServiceID = cs[num];
        //根据id获取客服名称
        presenter.getServiceName(customerServiceID);
    }

    /**
     * 回调：客服名称
     */
    @Override
    public void refreshServiceName(ServiceNameEntity data) {
        if (!StringUtil.isNull(data.getName())) {
            customerServiceName = data.getName();
            customerServiceIcon = data.getIcon();
        }
    }

    /**
     * 回调：赛事签到
     */
    @Override
    public void refreshSignScheduleData(SignScheduleEntity data) {
        if (data.isResult()) {
            if (data.getData().getList() != null) {
                try {
                    String schedule_starttime = TimeHelper.getMMddHHmmTimeFormat(data.getData().getList().getSchedule_starttime());
                    String schedule_endtime = TimeHelper.getMMddHHmmTimeFormat(data.getData().getList().getSchedule_endtime());
                    cancelProgressDialog();
                    DialogManager.showSignInSuccessDialog(this, schedule_starttime, schedule_endtime);
                } catch (Exception e) {
                    changeType2SuccessDialog("签到完成", "请在规定时间内到赛规页面约战自己的对手进行参加比赛", "确认");
                }
                UmengAnalyticsHelper.onEvent(this, UmengAnalyticsHelper.MATCH, "签到");
            }
        } else {
            changeType2ErrorDialog("错误", "签到异常!", "确认");
        }
    }

    /**
     * 回调：加入群聊
     */
    @Override
    public void refreshGroupJoin(BaseHttpResult data) {
        if (match != null && data.isResult() && RongIM.getInstance() != null &&
                RongIM.getInstance().getCurrentConnectionStatus() == ConnectionStatus.CONNECTED) {

            RongIMHelper.setCoversationNotifMute(Conversation.ConversationType.GROUP,
                    match.getChatroom_group_id(), true);

            ActivityManager.startConversationActivity(this,
                    match.getChatroom_group_id(),
                    match.getChatroom_group_name(),
                    ConversationActivity.GROUP,
                    customerServiceID,
                    customerServiceName);
        }
    }

    /**
     * 回调：赛事匹配
     */
    public void onEventMainThread(SignSchedule210Entity event) {

        if (event != null) {
            if (event.isResult()) {
                loadData();
            }
        }
    }

    /**
     * 事件：登录
     */
    public void onEventMainThread(LoginEvent event) {
        if (event != null) {
            loadData();
        }
    }
}
