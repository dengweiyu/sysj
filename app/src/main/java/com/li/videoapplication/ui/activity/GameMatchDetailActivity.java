package com.li.videoapplication.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.li.videoapplication.R;
import com.li.videoapplication.data.DataManager;
import com.li.videoapplication.data.model.entity.Match;
import com.li.videoapplication.data.model.response.CheckAndroidStatusEntity;
import com.li.videoapplication.data.model.response.EventsInfo204Entity;
import com.li.videoapplication.data.model.response.JoinGroup208Entity;
import com.li.videoapplication.data.model.response.SignSchedule204Entity;
import com.li.videoapplication.data.model.response.SignSchedule210Entity;
import com.li.videoapplication.data.model.response.getServiceName204Entity;
import com.li.videoapplication.framework.AppConstant;
import com.li.videoapplication.framework.TBaseAppCompatActivity;
import com.li.videoapplication.tools.RongIMHelper;
import com.li.videoapplication.tools.TextImageHelper;
import com.li.videoapplication.tools.TimeHelper;
import com.li.videoapplication.tools.UmengAnalyticsHelper;
import com.li.videoapplication.ui.ActivityManeger;
import com.li.videoapplication.ui.DialogManager;
import com.li.videoapplication.ui.fragment.GameMatchProcessFragment;
import com.li.videoapplication.ui.fragment.GameMatchResultFragment;
import com.li.videoapplication.ui.fragment.GameMatchRulesFragment;
import com.li.videoapplication.ui.pageradapter.ViewPagerAdapter;
import com.li.videoapplication.ui.toast.ToastHelper;
import com.li.videoapplication.utils.AppUtil;
import com.li.videoapplication.utils.NetUtil;
import com.li.videoapplication.utils.StringUtil;
import com.li.videoapplication.utils.TextUtil;
import com.li.videoapplication.views.ViewPagerY4;
import com.li.videoapplication.views.bubblelayout.BubbleLayout;
import com.li.videoapplication.views.bubblelayout.BubblePopupHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import me.everything.android.ui.overscroll.HorizontalOverScrollBounceEffectDecorator;
import me.everything.android.ui.overscroll.adapters.ViewPagerOverScrollDecorAdapter;

/**
 * 活动：赛事详情
 */
public class GameMatchDetailActivity extends TBaseAppCompatActivity implements View.OnClickListener,
        OnPageChangeListener {

    private ViewPagerAdapter adapter;
    private ViewPagerY4 mViewPager;
    private List<Fragment> fragments;
    private TextView cardViewText;
    public Match match;
    public ImageView header, refresh;
    private View refreshLine;
    private GameMatchRulesFragment rulesFragment;
    private GameMatchResultFragment resultFragment;
    private GameMatchProcessFragment processFragment;

    public String event_id;
    public AppBarLayout appBarLayout;
    public String customerServiceID, customerServiceName;
    private PopupWindow popupWindow;
    private BubbleLayout bubbleLayout;
    private LinearLayout con;

    /**
     * 跳转：报名
     */
    private void startSignUpActivity() {
        ActivityManeger.startSignUpActivity(this, event_id, match, customerServiceID, customerServiceName);
        UmengAnalyticsHelper.onEvent(this, UmengAnalyticsHelper.MATCH, "报名次数");
    }

    /**
     * 跳转：我的赛程
     */
    private void startMyMatchProcessActivity(Match match) {
        ActivityManeger.startMyMatchProcessActivity(this, match, customerServiceID, customerServiceName);
    }

    /**
     * 页面跳转：安装应用
     */
    private void install() {
        if (match != null) {
            Uri uri = Uri.parse(match.getAndroid_address());
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
            overridePendingTransition(R.anim.activity_hold, R.anim.activity_hold);
            // 游戏下载数+1
            DataManager.TASK.downloadClick203(match.getGame_id());
        }
    }

    /**
     * 分享
     */
    public void startShareActivity() {
        if (match != null) {
            final String url = AppConstant.getEventUrl(match.getEvent_id());
            final String title = "精彩赛事分享";
            final String imageUrl = match.getFlag();
            final String content = "快来看看" + match.getTitle();

            ActivityManeger.startActivityShareActivity4VideoPlay(this, url, title, imageUrl, content);
        }
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
        super.afterOnCreate();
        setSystemBarBackgroundColor(Color.BLACK);
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
    public void initView() {
        super.initView();
        initToolbar();
        appBarLayout = (AppBarLayout) findViewById(R.id.appbar_layout);
        cardViewText = (TextView) findViewById(R.id.gamematch_signup);
        con = (LinearLayout) findViewById(R.id.gamematch_con);
        refresh = (ImageView) findViewById(R.id.gamematch_refresh);
        refreshLine = findViewById(R.id.gamematch_refreshline);

        refresh.setOnClickListener(this);
        findViewById(R.id.gamematch_signup).setOnClickListener(this);
        findViewById(R.id.gamematch_startchat).setOnClickListener(this);

        //客服菜单弹窗
        findViewById(R.id.gamematch_cs).setOnClickListener(this);
        bubbleLayout = (BubbleLayout) LayoutInflater.from(this).inflate(R.layout.gamematch_popup, null);
        bubbleLayout.findViewById(R.id.gamematch_popup_cs).setOnClickListener(this);
        bubbleLayout.findViewById(R.id.gamematch_popup_course).setOnClickListener(this);

        refreshImageView();
        initViewPager();
    }

    @Override
    public void loadData() {
        super.loadData();
        DataManager.getEventsInfo210(event_id, getMember_id());
    }

    private void initToolbar() {
        findViewById(R.id.tb_back).setOnClickListener(this);
        findViewById(R.id.tb_download).setOnClickListener(this);
        findViewById(R.id.tb_share).setOnClickListener(this);

        if (AppConstant.DOWNLOAD) {
            findViewById(R.id.tb_download).setVisibility(View.VISIBLE);
        } else {
            findViewById(R.id.tb_download).setVisibility(View.GONE);
        }
    }

    private void refreshImageView() {
        if (header == null) {
            header = (ImageView) findViewById(R.id.header);
            LayoutParams params = header.getLayoutParams();
            WindowManager windowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);

            //设置广告图片宽高比=600*338
            int srceenWidth = windowManager.getDefaultDisplay().getWidth();
            params.width = srceenWidth;
            params.height = srceenWidth * 338 / 600;
            header.setLayoutParams(params);
        } else {
            //加载广告图片
            new TextImageHelper().setImageViewImageNet(header, match.getFlag());
        }
    }

    private void initViewPager() {
        if (fragments == null) {
            fragments = new ArrayList<>();

            rulesFragment = new GameMatchRulesFragment();
            resultFragment = new GameMatchResultFragment();
            processFragment = new GameMatchProcessFragment();

            fragments.add(rulesFragment);
            fragments.add(processFragment);
            fragments.add(resultFragment);

        }

        final String[] tabTitle = {"赛规", "赛程", "结果"};
        adapter = new ViewPagerAdapter(getSupportFragmentManager(), fragments, tabTitle);
        mViewPager = (ViewPagerY4) findViewById(R.id.gamematch_viewpager);
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
        if (mViewPager.getCurrentItem() == 0) {
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
    protected void confirmButtonEvent() {
        super.confirmButtonEvent();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.gamematch_signup:
                if (mViewPager.getCurrentItem() == 0) {//赛规页面
                    switch (match.getBtn_status()) {
                        case 1://立即报名
                            if (isLogin()) {
                                startSignUpActivity();
                            } else {
                                ToastHelper.s("请先登录");
                                ActivityManeger.startLoginActivityFromChat(this, event_id);
                            }
                            break;
                        case 2://进行中
                            ToastHelper.s("比赛正在火热进行中");
                            break;
                        case 3://立即签到
                            if (isLogin()) {
                                DataManager.signSchedule204(getMember_id(), match.getEvent_id());
                                showLoadingDialog("签到中", "正在签到，请稍等...", false);
                            } else {
                                ToastHelper.s("请先登录");
                                ActivityManeger.startLoginActivityFromChat(this, event_id);
                            }
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
                            String s = "";
                            try {
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
                    if (isLogin()) {
                        startMyMatchProcessActivity(match);
                    } else {
                        ToastHelper.s("请先登录");
                    }
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
                        ToastHelper.s("当前网络不可用，请检查后重试");
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
                if (isLogin()) {
                    DataManager.groupJoin208(getMember_id(), match.getChatroom_group_id());
                    UmengAnalyticsHelper.onEvent(this, UmengAnalyticsHelper.MATCH, "群聊");
                } else {
                    ToastHelper.s("请先登录");
                }
                break;
            case R.id.gamematch_cs://客服教程弹窗
                popupWindow = BubblePopupHelper.create(this, bubbleLayout);
                int[] location = new int[2];
                v.getLocationInWindow(location);
                popupWindow.showAtLocation(v, Gravity.NO_GRAVITY, location[0], v.getHeight() + location[1]);
                break;
            case R.id.gamematch_popup_cs://客服
                if (RongIM.getInstance() != null && customerServiceID != null) {
                    ActivityManeger.startConversationActivity(this, customerServiceID, customerServiceName, false);
                    UmengAnalyticsHelper.onEvent(this, UmengAnalyticsHelper.MATCH, "客服");
                }
                break;
            case R.id.gamematch_popup_course://教程
                ActivityManeger.startHelpActivity(this);
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
            case 2:
                con.setVisibility(View.GONE);
                cardViewText.setVisibility(View.GONE);
                break;
            case 3:
                con.setVisibility(View.GONE);
                cardViewText.setVisibility(View.GONE);
                break;
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }

    private void showVictoryDialog() {
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
     * 回调：加入群聊
     */
    public void onEventMainThread(JoinGroup208Entity event) {

        if (event != null && event.isResult()) {
            Log.d(tag, "JoinGroup208Entity: " + event.getMsg());

            if (RongIM.getInstance() != null && RongIM.getInstance().getCurrentConnectionStatus() ==
                    RongIMClient.ConnectionStatusListener.ConnectionStatus.CONNECTED) {

                RongIMHelper.setCoversationNotifMute(Conversation.ConversationType.GROUP, match.getChatroom_group_id(), true);

                ActivityManeger.startConversationActivity(this,
                        match.getChatroom_group_id(),
                        match.getChatroom_group_name(),
                        ConversationActivity.GROUP,
                        customerServiceID,
                        customerServiceName);
            }
        }
    }

    /**
     * 回调：赛事详情
     */
    public void onEventMainThread(EventsInfo204Entity event) {

        if (event != null) {
            if (event.isResult()) {
                if (event.getData() != null) {
                    match = event.getData();
                    refreshImageView();
                    if (mViewPager.getCurrentItem() == 0)
                        cardViewText.setText(match.getBtn_text());
                    refreshBtnColor();
                    setRuleFragmentData();
                    getCustomerServiceID();
                    showVictoryDialog();
                }
            }
        }
    }

    //随机获取一个客服ID
    private void getCustomerServiceID() {
        String[] cs = match.getCustomer_service();
        int num = new Random().nextInt(cs.length);
        customerServiceID = cs[num];
        //根据id获取客服名称
        DataManager.getServiceName204(customerServiceID);
    }

    /**
     * 回调：客服名称
     */
    public void onEventMainThread(getServiceName204Entity event) {

        if (event != null) {
            if (event.isResult() && event.getData() != null) {
                if (event.getData().getName() != null && !event.getData().getName().equals("")) {
                    customerServiceName = event.getData().getName();
                    Log.i(tag, "customerServiceName  : " + customerServiceName);
                }
            }
        }
    }

    /**
     * 回调：赛事签到
     */
    public void onEventMainThread(SignSchedule204Entity event) {

        if (event != null) {
            if (event.isResult()) {
                if (event.getData().getList() != null) {
                    try {
                        String schedule_starttime = TimeHelper.getMMddHHmmTimeFormat(event.getData().getList().getSchedule_starttime());

                        String schedule_endtime = TimeHelper.getMMddHHmmTimeFormat(event.getData().getList().getSchedule_endtime());
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
}
