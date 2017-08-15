package com.li.videoapplication.ui.activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fmsysj.screeclibinvoke.logic.screenrecord.RecordingService;
import com.fmsysj.screeclibinvoke.ui.activity.ScreenRecordActivity;
import com.ifeimo.im.common.bean.eventbus.ChatWindowEntity;
import com.ifeimo.im.common.util.StatusBarBlackTextHelper;
import com.ifeimo.im.framwork.Proxy;
import com.ifeimo.im.framwork.message.OnHtmlItemClickListener;
import com.ifeimo.screenrecordlib.RecordingManager;
import com.ifeimo.screenrecordlib.constant.Constant;
import com.ifeimo.screenrecordlib.util.TaskUtil;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu.OnCloseListener;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu.OnClosedListener;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu.OnOpenListener;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu.OnOpenedListener;
import com.li.videoapplication.BuildConfig;
import com.li.videoapplication.R;
import com.li.videoapplication.data.DataManager;
import com.li.videoapplication.data.Utils_Data;
import com.li.videoapplication.data.download.DownLoadManager;
import com.li.videoapplication.data.download.FileDownloadRequest;
import com.li.videoapplication.data.image.GlideHelper;
import com.li.videoapplication.data.local.SYSJStorageUtil;
import com.li.videoapplication.data.model.entity.Member;
import com.li.videoapplication.data.model.entity.Update;
import com.li.videoapplication.data.model.entity.VideoImage;
import com.li.videoapplication.data.model.event.LoginEvent;
import com.li.videoapplication.data.model.event.LogoutEvent;
import com.li.videoapplication.data.model.event.UnReadMessageEvent;
import com.li.videoapplication.data.model.event.UserInfomationEvent;
import com.li.videoapplication.data.model.response.DownloadSuccessEntity;
import com.li.videoapplication.data.model.response.GetRongCloudToken204Entity;
import com.li.videoapplication.data.model.response.ParseResultEntity;
import com.li.videoapplication.data.model.response.PatchEntity;
import com.li.videoapplication.data.model.response.UpdateVersionEntity;
import com.li.videoapplication.data.network.UITask;
import com.li.videoapplication.data.preferences.Constants;
import com.li.videoapplication.data.preferences.NormalPreferences;
import com.li.videoapplication.data.preferences.PreferencesHepler;
import com.li.videoapplication.data.preferences.SharedPreferencesUtils;
import com.li.videoapplication.framework.AppConstant;
import com.li.videoapplication.framework.AppManager;
import com.li.videoapplication.framework.BaseSlidingActivity;
import com.li.videoapplication.mvp.home.view.HomeFragment;
import com.li.videoapplication.tools.FeiMoIMHelper;
import com.li.videoapplication.tools.RongIMHelper;
import com.li.videoapplication.tools.ToastHelper;
import com.li.videoapplication.tools.UmengAnalyticsHelper;
import com.li.videoapplication.ui.ActivityManager;
import com.li.videoapplication.ui.DialogManager;
import com.li.videoapplication.ui.fragment.DiscoverFragment;
import com.li.videoapplication.ui.fragment.GameFragment;
import com.li.videoapplication.ui.fragment.PlayWithFragment;
import com.li.videoapplication.ui.fragment.SliderFragment;
import com.li.videoapplication.ui.pageradapter.WelfarePagerAdapter;
import com.li.videoapplication.utils.AppUtil;
import com.li.videoapplication.utils.HareWareUtil;
import com.li.videoapplication.utils.LogHelper;
import com.li.videoapplication.utils.MD5Util;
import com.li.videoapplication.utils.NetUtil;
import com.li.videoapplication.utils.StringUtil;
import com.li.videoapplication.views.CircleImageView;
import com.li.videoapplication.views.ViewPagerY4;
import com.meituan.android.walle.WalleChannelReader;
import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.tencent.tinker.lib.tinker.TinkerInstaller;
import com.umeng.analytics.AnalyticsConfig;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import io.rong.eventbus.EventBus;
import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import me.everything.android.ui.overscroll.HorizontalOverScrollBounceEffectDecorator;
import me.everything.android.ui.overscroll.adapters.ViewPagerOverScrollDecorAdapter;

import static android.support.v4.view.ViewPager.SCROLL_STATE_DRAGGING;
import static android.support.v4.view.ViewPager.SCROLL_STATE_IDLE;
import static android.support.v4.view.ViewPager.SCROLL_STATE_SETTLING;

/**
 * 活动：主页
 */
public class MainActivity extends BaseSlidingActivity implements View.OnClickListener,
        OnPageChangeListener, OnOpenListener, OnOpenedListener, OnCloseListener,
        OnClosedListener {

    //上面这个static块是判断系统是否是MIUI6
    private static boolean sIsMiuiV6;

    static {
        try {
            Class<?> sysClass = Class.forName("android.os.SystemProperties");
            Method getStringMethod = sysClass.getDeclaredMethod("get", String.class);
            sIsMiuiV6 = "V6".equals(getStringMethod.invoke(sysClass, "ro.miui.ui.version.name"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private View menu;
    private View root;
    private List<LinearLayout> bottomButtons;
    private List<ImageView> bottomIcon;
    private List<TextView> bottomText;

    private ImageView right, leftIcon;
    private CircleImageView leftHead;
    public CircleImageView leftCount;

    private FrameLayout left;
    private TextView title;
    private LinearLayout search;
    private LinearLayout mPlayWith;
    private TextView mOrderList;
    private View mPlayWithIndicator;
    private View mMatchIndicator;
    private TextView mPlayWithTitle;
    private TextView mMatchTitle;

    private ImageView searchIcon;
    private TextView searchText;
    private ImageView scanQnCode;
    private View background;
    private int currIndex = 0;// 当前页卡编号
    private List<Fragment> fragments;
    private HomeFragment home;
    private DiscoverFragment discover;
    private GameFragment game;
    private PlayWithFragment playWithFragment;

    private Fragment context;
    private SliderFragment slider;
    public ViewPagerY4 viewPager;
    private WelfarePagerAdapter adapter;
    private boolean isLogin = false;
    private String imageUrl;
    private boolean isShowedUpdate = false;
    private SlidingMenu.CanvasTransformer mTransformer;

    private String mValidation;

    private TextView mPlayWithBottom;
    private View mDivider;
    /**
     * 双击退出应用
     */
    private boolean isExit = false;
    public SlidingMenu slidingMenu;
    private SystemBarTintManager tintManager;

    /**
     * 跳转：搜索
     */
    private void startSearchActivity() {
        ActivityManager.startSearchActivity(this);
        UmengAnalyticsHelper.onEvent(this, UmengAnalyticsHelper.MAIN, "点击搜索框次数");
    }

    /**
     * 跳转：二维码扫描
     */
    private void startScanQRCodeActivity() {
        ActivityManager.startScanQRCodeActivity(this);
        UmengAnalyticsHelper.onEvent(this, UmengAnalyticsHelper.MAIN, "点击扫码按钮次数");
    }

    /**
     * 跳转：录屏
     */
    public void startScreenRecordActivity() {
        int SDKVesion = AppUtil.getAndroidSDKVersion();
        if (SDKVesion >= 21) {
            ScreenRecordActivity.startScreenRecordActivity(this);
            overridePendingTransition(R.anim.activity_slide_in_top, R.anim.activity_hold);
        } else {
            if (AppUtil.appRoot()) {
                ScreenRecordActivity.startScreenRecordActivity(this);
                overridePendingTransition(R.anim.activity_slide_in_top, R.anim.activity_hold);
            } else {// 提示用户获取root
                ToastHelper.s(R.string.main_rootnotify);
            }
        }
    }

    public void locationAtGame() {

        if (viewPager != null) {
            viewPager.setCurrentItem(1);
        }
        handler.postDelayed(new Runnable() {

            @Override
            public void run() {
                if (game != null) {
                    game.setCurrentItem(0);
                }
            }
        }, 400);
    }



	/* ########### 侧滑菜单 ############### */

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        TinkerInstaller.onReceiveUpgradePatch(getApplicationContext(), Environment.getExternalStorageDirectory().getAbsolutePath() + "/patch_signed_7zip.apk");

        initSystemBar(this);
        setSystemBarBackgroundResource(R.color.menu_main_red);

        EventBus.getDefault().register(this);
        AppManager.getInstance().setMainActivity(this);
        AppManager.getInstance().removeActivity(AppstartActivity.class);

        setBehindContentView(R.layout.view_slider);
        initAnimation();
        initSlider(savedInstanceState);

        setContentView(R.layout.activity_main);
        setActionBar(inflateActionBar());

        initMenu();
        initFragment();

        switchTab(0);
        switchActionBar(0);
        refreshActionBar();

        viewPager.setCurrentItem(0);

        showVideoTipDialog();

        if (!isShowedUpdate) {
            // 版本更新
            DataManager.updateVersion();
        }

        if (isLogin && RongIM.getInstance() != null &&
                RongIM.getInstance().getCurrentConnectionStatus() !=
                        RongIMClient.ConnectionStatusListener.ConnectionStatus.CONNECTED) {
            //链接融云
            RongIMHelper.connectIM();
        }

        getIntentValue();
        getIntentResult();
        // 初始化下载器
        DownLoadManager.getInstance();

        //注册IM 消息点击监听器
        registerIMClickListener();
        //注册EventBus 3.0
        org.greenrobot.eventbus.EventBus.getDefault().register(this);

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        getIntentValue();
        getIntentResult();
    }

    private int tab;
    private int mMainPosition;
    private int mMatchPosition;
    private void getIntentValue() {
        if (getIntent() != null) {
            try {
                //老版本
                tab = getIntent().getIntExtra("tab", 0);
                //新版本
                mMainPosition = getIntent().getIntExtra("main_position",-1);
                mMatchPosition = getIntent().getIntExtra("match_position",-1);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void getIntentResult() {

        if (tab >= 1 && tab <= 4) {
            if (viewPager != null) {
                viewPager.setCurrentItem(tab - 1);
                tab = 0;
            }

            //跳到赛事页面
            if(tab == 4){
                if (playWithFragment != null) {
                    playWithFragment.setCurrentPage(1);
                }
            }
        }

        if (mMainPosition > 0 && mMainPosition < 4){
            if (viewPager != null) {
                viewPager.setCurrentItem(mMainPosition);
                mMainPosition = -1;
            }
        }

        if (mMatchPosition > 0 && mMatchPosition < 2){
            if (playWithFragment != null) {
                playWithFragment.setCurrentPage(mMatchPosition);
                mMatchPosition = -1;
            }
        }

        if (slidingMenu != null){
            if (slidingMenu.isMenuShowing()){
                toggle();
            }
        }
    }

    final Handler mHandler = new Handler();

    Runnable mFetchPatchTask = new Runnable() {
        @Override
        public void run() {
            DataManager.fetchPatch(AnalyticsConfig.getChannel(MainActivity.this),BuildConfig.VERSION_NAME);
            mHandler.postDelayed(this,1800000);
        }
    };

    @Override
    public void onResume() {
        super.onResume();
        if (NetUtil.isConnect() && !isLogin) {
            String ipAddress = NetUtil.getLocalIpAddress();
            //ICP 统计相关接口--用户登录记录
            DataManager.sign("0", "游客", "visit", "sysj_a", ipAddress,
                    HareWareUtil.getHardwareCode(), HareWareUtil.getIMEI(), "");
        }

        //未读消息总数
        RongIMHelper.getTotalUnreadCount(new RongIMHelper.totalUnreadCountCallback() {
            @Override
            public void totalUnreadCount(int count) {
                Log.d(tag, "totalUnreadCount: count == " + count);
                if(leftCount == null){
                     return;
                }
                if (mUnReadMsg == null){
                    mUnReadMsg = new UnReadMessageEvent(count);
                }else {
                    mUnReadMsg.setCount(mUnReadMsg.getCount()+count);
                }

                if (viewPager != null && viewPager.getCurrentItem() == 3){
                    return;
                }
                if (count > 0) {
                    leftCount.setVisibility(View.VISIBLE);
                } else {
                    leftCount.setVisibility(View.GONE);
                }
            }
        });

        //检查补丁包  补丁包一定不要使用7zip的，在百度加固下出现崩溃
        mHandler.post(mFetchPatchTask);
    }

    @Override
    public void onDestroy() {
        //反注册 EventBus 3.0
        org.greenrobot.eventbus.EventBus.getDefault().unregister(this);

        EventBus.getDefault().unregister(this);
        AppManager.getInstance().removeMainActivity();
        super.onDestroy();
        if (RecordingManager.getInstance().isRecording()) {// 录屏中

            // 销毁所有资源（服务，调度器，下载器）
            Utils_Data.destroyAllResouce();
            // 移除任务栈
            // Utils_Data.finishApp();

            System.gc();
        }

        if (mHandler != null){
            mHandler.removeCallbacks(mFetchPatchTask);
        }
    }

    /**
     * 回调：IM点击事件
     */
    private void registerIMClickListener(){
        Proxy.getMessageManager().setOnHtmlItemClickListener(new OnHtmlItemClickListener() {
            @Override
            public void onClick(String memberid, String defaultStr, String[] html, boolean isMe) {
                if (html != null && !StringUtil.isNull(html[0])){
                    //将Url给后台解析
                    DataManager.parseMessage(html[0]);
                }
            }
        });
    }


    public void setCurrentItem(int pager) {
        if (pager == 0 || pager == 1 || pager == 2 || pager == 3) {

            switchTab(pager);
            switchActionBar(pager);
            switchContent(context, fragments.get(pager));
        }
    }

    @Override
    public void onBackPressed() {
        if (isExit) {
            FeiMoIMHelper.LogOut(this, false);
            if (RecordingManager.getInstance().isRecording()) {
                TaskUtil.clearTaskAndAffinity(this);
            } else {
                super.onBackPressed();
                // 停止录屏服务
                RecordingService.stopRecordingService();
            }
            super.onBackPressed();
        } else {
            isExit = true;
            ToastHelper.s("再按一次将退出手游视界");
            handler.postDelayed(new Runnable() {

                @Override
                public void run() {
                    isExit = false;
                }
            }, 1600);
        }
    }

    //侧滑缩放动画
    private void initAnimation() {
        mTransformer = new SlidingMenu.CanvasTransformer() {
            @Override
            public void transformCanvas(Canvas canvas, float percentOpen) {
                float scale = (float) (percentOpen * 0.25 + 0.75);
                canvas.scale(scale, scale, canvas.getWidth() / 2, canvas.getHeight() / 2);
            }
        };
    }

    private void initSlider(Bundle savedInstanceState) {

        if (savedInstanceState == null) {
            FragmentTransaction t = manager.beginTransaction();
            slider = new SliderFragment();
            t.replace(R.id.slider, slider);
            t.commit();
        } else {
            slider = (SliderFragment) manager.findFragmentById(R.id.slider);
        }

        slidingMenu = getSlidingMenu();
        slidingMenu.setShadowWidthRes(R.dimen.slider_shadow_width);
        slidingMenu.setShadowDrawable(R.drawable.slider_shadow);
        slidingMenu.setBehindOffsetRes(R.dimen.slider_offset);
        slidingMenu.setFadeDegree(0.9f);
        slidingMenu.setBehindCanvasTransformer(mTransformer);
        slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        slidingMenu.setOnOpenListener(this);
        slidingMenu.setOnOpenedListener(this);
        slidingMenu.setOnCloseListener(this);
        slidingMenu.setOnClosedListener(this);
    }

    private View inflateActionBar() {
        background = inflater.inflate(R.layout.actionbar_main, null);
        left = (FrameLayout) background.findViewById(R.id.ab_left);
        right = (ImageView) background.findViewById(R.id.ab_right);
        leftHead = (CircleImageView) background.findViewById(R.id.ab_left_head);
        leftCount = (CircleImageView) background.findViewById(R.id.ab_left_count);
        leftIcon = (ImageView) background.findViewById(R.id.ab_left_icon);

        title = (TextView) background.findViewById(R.id.ab_title);
        search = (LinearLayout) background.findViewById(R.id.ab_search);
        searchIcon = (ImageView) background.findViewById(R.id.ab_search_icon);
        searchText = (TextView) background.findViewById(R.id.ab_search_text);
        scanQnCode = (ImageView) background.findViewById(R.id.ab_scanqrcode);

        mPlayWith = (LinearLayout) background.findViewById(R.id.ll_play_with_match_tab);
        mPlayWithIndicator = background.findViewById(R.id.play_with_indicator);
        mPlayWithTitle = (TextView)background.findViewById(R.id.tv_play_with_title);
        mMatchTitle = (TextView)background.findViewById(R.id.tv_match_title);
        mMatchIndicator = background.findViewById(R.id.match_indicator);
        mOrderList = (TextView)background.findViewById(R.id.tv_order_list);

        mOrderList.setOnClickListener(this);
        mPlayWithTitle.setOnClickListener(this);
        mMatchTitle.setOnClickListener(this);
        search.setOnClickListener(this);
        searchIcon.setOnClickListener(this);
        searchText.setOnClickListener(this);
        scanQnCode.setOnClickListener(this);
        left.setOnClickListener(this);
        right.setOnClickListener(this);

        findViewById(R.id.iv_bottom_record).setOnClickListener(this);
        root = findViewById(R.id.rl_main_root);
        return background;
    }

    @Override
    public void onClosed() {
        refreshSystemBar(false);
        home.startAutoFlowTimer();
    }

    @Override
    public void onClose() {
        home.startAutoFlowTimer();
    }

    @Override
    public void onOpened() {
        home.stopAutoFlowTimer();
        slider.refreshUnReadMessage();
        String member_id = PreferencesHepler.getInstance().getMember_id();
        if (!StringUtil.isNull(member_id)){
            DataManager.userProfilePersonalInformation(member_id, member_id);
        }
        UmengAnalyticsHelper.onEvent(this, UmengAnalyticsHelper.MAIN, "打开左侧栏次数");
    }

    @Override
    public void onOpen() {
        refreshSystemBar(true);
        home.stopAutoFlowTimer();
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.ab_left:
                toggle();
                break;

            case R.id.ab_right:
                //消息
                ActivityManager.startMyMessageActivity(this);
                break;
            case R.id.iv_bottom_record:

                DialogManager.showRecordDialog(this,getWindow().getDecorView());
                UmengAnalyticsHelper.onEvent(MainActivity.this,UmengAnalyticsHelper.MAIN,"首页-底部录屏");
                break;

            case R.id.ab_search:
                startSearchActivity();
                break;

            case R.id.ab_search_icon:
                startSearchActivity();
                break;

            case R.id.ab_search_text:
                startSearchActivity();
                break;

            case R.id.ab_scanqrcode:
                startScanQRCodeActivity();
                break;
            case R.id.tv_play_with_title:
                if (playWithFragment != null){
                    playWithFragment.setCurrentPage(0);
                }
                break;
            case R.id.tv_match_title:
                if (playWithFragment != null){
                    playWithFragment.setCurrentPage(1);
                }
                break;
            case R.id.tv_order_list:

                if(PreferencesHepler.getInstance().isLogin()){
                    Member member = PreferencesHepler.getInstance().getUserProfilePersonalInformation();
                    if (member != null ){
                        if (member.isCoach()){
                            ActivityManager.startPlayWithOrderAndMatchActivity(MainActivity.this,0,1);
                        }else {
                            ActivityManager.startPlayWithOrderAndMatchActivity(MainActivity.this,0,0);
                        }
                    }

                }else {
                    DialogManager.showLogInDialog(MainActivity.this);
                }

                break;
        }
    }

    private void switchActionBar(final int index) {

        if (index == 0) {
            if (isLogin) {
                leftIcon.setVisibility(View.GONE);
                leftHead.setVisibility(View.VISIBLE);
            } else {
                leftIcon.setVisibility(View.VISIBLE);
                leftHead.setVisibility(View.GONE);
                leftIcon.setImageResource(R.drawable.ab_person_white);
            }
        } else {
            if (isLogin) {
                leftIcon.setVisibility(View.GONE);
                leftHead.setVisibility(View.VISIBLE);
            } else {
                leftIcon.setVisibility(View.VISIBLE);
                leftHead.setVisibility(View.GONE);
                leftIcon.setImageResource(R.drawable.ab_person_red);
            }
        }

        switch (index) {
            case 0:// 首页
                background.setBackgroundResource(R.color.ab_backdround_red);
                right.setImageResource(R.drawable.home_message_white);
                left.setBackgroundResource(R.drawable.ab_red);
                right.setBackgroundResource(R.drawable.ab_red);
                title.setVisibility(View.GONE);
                search.setVisibility(View.VISIBLE);
                search.setBackgroundResource(R.drawable.search_bg);
                if(leftCount != null){
                    leftCount.setImageResource(R.color.white);
                }
                break;

            case 1:// 发现
                background.setBackgroundResource(R.color.ab_backdround_white);
                right.setImageResource(R.drawable.home_message_red);
                left.setBackgroundResource(R.drawable.ab_white);
                right.setBackgroundResource(R.drawable.ab_white);
                title.setVisibility(View.GONE);
                search.setVisibility(View.VISIBLE);
                search.setBackgroundResource(R.drawable.search_bg_white);
                if (leftCount != null){
                    leftCount.setImageResource(R.color.ab_backdround_red);
                }
                break;

            case 2:// 找游戏
                background.setBackgroundResource(R.color.ab_backdround_white);
                right.setImageResource(R.drawable.home_message_red);
                left.setBackgroundResource(R.drawable.ab_white);
                right.setBackgroundResource(R.drawable.ab_white);
                title.setVisibility(View.GONE);
                search.setVisibility(View.VISIBLE);
                search.setBackgroundResource(R.drawable.search_bg_white);
                if (leftCount != null){
                    leftCount.setImageResource(R.color.ab_backdround_red);
                }
                break;

            case 3:// 福利
                background.setBackgroundResource(R.color.ab_backdround_white);
                right.setImageResource(R.drawable.home_message_red);
                left.setBackgroundResource(R.drawable.ab_white);
                right.setBackgroundResource(R.drawable.ab_white);
                title.setVisibility(View.GONE);
                title.setText(R.string.menu_main_fourth);
                search.setVisibility(View.VISIBLE);
                search.setBackgroundResource(R.drawable.search_bg_white);
                if (leftCount != null){
                    leftCount.setImageResource(R.color.ab_backdround_red);
                }
                break;
        }
    }

	/* ########### 底部菜单栏 ############### */

    /**
     * 刷新标题栏左上角头像
     */
    private void refreshActionBar() {

        isLogin = PreferencesHepler.getInstance().isLogin();
        imageUrl = PreferencesHepler.getInstance().getUserProfilePersonalInformation().getAvatar();
        if (isLogin && !StringUtil.isNull(imageUrl)) {
            GlideHelper.displayImageWhite(this, imageUrl, leftHead);
            leftIcon.setVisibility(View.GONE);
            leftHead.setVisibility(View.VISIBLE);
        } else {
            leftHead.setImageDrawable(new ColorDrawable(Color.TRANSPARENT));
            leftIcon.setVisibility(View.VISIBLE);
            leftHead.setVisibility(View.GONE);
            if(leftCount == null){
                return;
            }
            leftCount.setVisibility(View.GONE);
        }
    }

    private void refreshSystemBar(int index) {
        if (index == 0) {
            setSystemBarBackgroundResource(R.color.menu_main_red);
            setStatusBarDarkMode(false, this);
        } else {
            setSystemBarBackgroundResource(R.color.menu_main_white);
            setStatusBarDarkMode(true, this);
        }
    }

    private void refreshSystemBar(Boolean isOpen) {
        if (isOpen) {
            setSystemBarBackgroundResource(R.color.slider_bg);
            setStatusBarDarkMode(false, this);
        } else {
            refreshSystemBar(currIndex);
        }
    }

    private void initFragment() {
        if (fragments == null) {
            fragments = new ArrayList<>();
        }
        fragments.clear();

        home = new HomeFragment();
        game = new GameFragment();
        discover = new DiscoverFragment();
        playWithFragment = new PlayWithFragment();

        fragments.add(home);
        fragments.add(game);
        fragments.add(discover);
        fragments.add(playWithFragment);
        viewPager = (ViewPagerY4) findViewById(R.id.pager);
        viewPager.setScrollable(true);
        viewPager.setOffscreenPageLimit(3);
        adapter = new WelfarePagerAdapter(manager, fragments);
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(this);
        playWithFragment.setOnScrollListener(mPlayWithListener);

    //    viewPager.setPageTransformer(false,new HomeShadeTransformer(viewPager,search,mPlayWidth,3));
        new HorizontalOverScrollBounceEffectDecorator(new ViewPagerOverScrollDecorAdapter(viewPager));
    }

    public void initMenu() {

        menu = findViewById(R.id.menu);

        mPlayWithBottom = (TextView)findViewById(R.id.tv_bottom_play_with);
        mDivider = findViewById(R.id.v_divider_bottom);

        if (bottomButtons == null) {
            bottomButtons = new ArrayList<>();
            bottomButtons.add((LinearLayout) findViewById(R.id.bottom_first));
            bottomButtons.add((LinearLayout) findViewById(R.id.bottom_second));
            bottomButtons.add((LinearLayout) findViewById(R.id.bottom_third));
            bottomButtons.add((LinearLayout) findViewById(R.id.bottom_fourth));
        }

        if (bottomIcon == null) {
            bottomIcon = new ArrayList<>();
            bottomIcon.add((ImageView) findViewById(R.id.bottom_first_icon));
            bottomIcon.add((ImageView) findViewById(R.id.bottom_second_icon));
            bottomIcon.add((ImageView) findViewById(R.id.bottom_third_icon));
            bottomIcon.add((ImageView) findViewById(R.id.bottom_fourth_icon));
        }

        if (bottomText == null) {
            bottomText = new ArrayList<>();
            bottomText.add((TextView) findViewById(R.id.bottom_first_text));
            bottomText.add((TextView) findViewById(R.id.bottom_second_text));
            bottomText.add((TextView) findViewById(R.id.bottom_third_text));
            bottomText.add((TextView) findViewById(R.id.bottom_fourth_text));
        }

        for (int i = 0; i < bottomButtons.size(); i++) {
            OnTabClickListener onTabClickListener = new OnTabClickListener(i);
            bottomButtons.get(i).setOnClickListener(onTabClickListener);
        }
    }


    private float mLastOffset;
    private int mPreviousPosition;
    private int mScrollState;

    @Override
    public void onPageScrollStateChanged(int arg0) {

        mScrollState = arg0;
        if (mScrollState == SCROLL_STATE_IDLE){
            if (viewPager.getCurrentItem() == 3){
                showPlayWidthTab(true);
            }else {
                showPlayWidthTab(false);
            }
            mPreviousPosition = viewPager.getCurrentItem();
            mLastOffset = 0;
        }
    }

    @Override
    public void onPageScrolled(int arg0, float arg1, int arg2) {
        if (mPreviousPosition == 3 && arg1 == 0){
            return;
        }

        if (arg1 - mLastOffset > 0){              //0->1
            if (mPreviousPosition == 2 || viewPager.getCurrentItem() == 3){
                if (mLastOffset != 0){
                    showShade(arg1);
                    hideShade(1-arg1);
                }
            }
        }else {                                   //1-0
            if (mPreviousPosition == 3 && viewPager.getCurrentItem() == 3){
                showShade(arg1);
                hideShade(1-arg1);
            }
        }
        mLastOffset = arg1;
    }

    @Override
    public void onPageSelected(int position) {
        switchTab(position);
        switchActionBar(position);
        refreshSystemBar(position);
        currIndex = position;
    }

    /**
     * 陪玩 Item 监听
     */
    final OnPageChangeListener mPlayWithListener = new OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            showPlayWidthTab(true);
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };


    private void showShade(float scale){
        if (mPlayWith == null){
            return;
        }
        if (mPlayWith.getVisibility() == View.GONE){
            mPlayWith.setVisibility(View.VISIBLE);
        }

        mPlayWith.setAlpha(scale);
    }

    private void hideShade(float scale){
        if (search == null){
            return;
        }
        search.setAlpha(scale);
    }

    private void switchTab(final int index) {
        for (int i = 0; i < bottomText.size(); i++) {
            if (index == i) {
                bottomText.get(i).setTextColor(resources.getColorStateList(R.color.menu_main_red));

            } else {
                bottomText.get(i).setTextColor(resources.getColorStateList(R.color.menu_main_gray));
            }
        }
        switch (index) {
            case 0:// 首页
                bottomIcon.get(0).setImageResource(R.drawable.home_selected);
                bottomIcon.get(1).setImageResource(R.drawable.game_normal);
                bottomIcon.get(2).setImageResource(R.drawable.discover_normal);
                bottomIcon.get(3).setImageResource(R.drawable.event_nomal);
                showPlayWidthTab(false);
                break;
            case 1: // 打游戏
                bottomIcon.get(0).setImageResource(R.drawable.home_normal);
                bottomIcon.get(1).setImageResource(R.drawable.game_selected);
                bottomIcon.get(2).setImageResource(R.drawable.discover_normal);
                bottomIcon.get(3).setImageResource(R.drawable.event_nomal);
                showPlayWidthTab(false);
                showGameTipDialog();
                break;
            case 2: // 发现
                bottomIcon.get(0).setImageResource(R.drawable.home_normal);
                bottomIcon.get(1).setImageResource(R.drawable.game_normal);
                bottomIcon.get(2).setImageResource(R.drawable.discover_selected);
                bottomIcon.get(3).setImageResource(R.drawable.event_nomal);
                showPlayWidthTab(false);
                showDiscoverTipDialog();
                break;
            case 3:// 福利
                bottomIcon.get(0).setImageResource(R.drawable.home_normal);
                bottomIcon.get(1).setImageResource(R.drawable.game_normal);
                bottomIcon.get(2).setImageResource(R.drawable.discover_normal);
                bottomIcon.get(3).setImageResource(R.drawable.event_selected);
                showPlayWidthTab(true);
                showMatchTipDialog();
                break;
        }
    }

    private void showPlayWidthTab(boolean isShow){
        if (isShow){
            leftCount.setVisibility(View.GONE);
            right.setVisibility(View.GONE);
            mPlayWith.setAlpha(1);
            mPlayWith.setVisibility(View.VISIBLE);
            search.setVisibility(View.GONE);
            mPlayWithBottom.setTextColor(getResources().getColor(R.color.ab_backdround_red));
            mDivider.setBackgroundColor(getResources().getColor(R.color.ab_backdround_red));
            mOrderList.setVisibility(View.VISIBLE);
            switch(playWithFragment.getCurrentPage()){
                case 0:
                    mPlayWithTitle.setTextColor(Color.parseColor("#fc3c2e"));
                    mPlayWithIndicator.setVisibility(View.VISIBLE);

                    mMatchTitle.setTextColor(Color.parseColor("#8b8b8b"));
                    mMatchIndicator.setVisibility(View.INVISIBLE);
                    break;
                case 1:
                    mMatchTitle.setTextColor(Color.parseColor("#fc3c2e"));
                    mMatchIndicator.setVisibility(View.VISIBLE);

                    mPlayWithTitle.setTextColor(Color.parseColor("#8b8b8b"));
                    mPlayWithIndicator.setVisibility(View.INVISIBLE);
                    break;
            }
        }else {

            leftCount.setVisibility(View.VISIBLE);

            right.setVisibility(View.VISIBLE);
            mOrderList.setVisibility(View.GONE);
            mPlayWithBottom.setTextColor(getResources().getColor(R.color.menu_main_gray));
            mDivider.setBackgroundColor(getResources().getColor(R.color.menu_main_gray));
            mPlayWith.setVisibility(View.GONE);
            search.setVisibility(View.VISIBLE);
            search.setAlpha(1);
        }
    }

    public void switchContent(Fragment from, Fragment to) {
        if (context != to) {
            context = to;
            FragmentTransaction transaction = manager.beginTransaction().setCustomAnimations(android.R.anim.fade_in, R.anim.activity_hold);
            if (!to.isAdded()) {
                transaction.hide(from).add(R.id.container, to).commit();
            } else {
                transaction.hide(from).show(to).commit();
            }
        }
    }


    /**
     * 初始化系统状态栏
     */
    private void initSystemBar(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(activity, true);
        }
        tintManager = new SystemBarTintManager(activity);

    }

    /**
     * 设置状态栏颜色
     */
    protected void setSystemBarBackgroundResource(int res) {
        if (tintManager != null) {
            tintManager.setStatusBarTintEnabled(true);
            tintManager.setStatusBarTintResource(res);
        }
    }

    @TargetApi(19)
    private void setTranslucentStatus(Activity activity, boolean on) {
        Window win = activity.getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }

    /**
     * 将状态栏的文字样式设置
     *
     * @param darkmode false:状态栏字体颜色是白色 true:颜色是黑色
     * @param activity
     */
    public void setStatusBarDarkMode(boolean darkmode, Activity activity) {
        StatusBarBlackTextHelper.initStatusBarTextColor(activity.getWindow(),darkmode);
    }

    /**
     * 遮罩提示页：视频录制
     */
    private void showVideoTipDialog() {

        boolean tip = NormalPreferences.getInstance().getBoolean(Constants.TIP_VEDIO, true);
        if (tip) {
            UITask.postDelayed(new Runnable() {
                @Override
                public void run() {
                    DialogManager.showVideoTipDialog(MainActivity.this);
                }
            },300);

            NormalPreferences.getInstance().putBoolean(Constants.TIP_VEDIO, false);
        }
    }

    /**
     * 遮罩提示页：发现
     */
    private void showDiscoverTipDialog() {

        boolean tip = NormalPreferences.getInstance().getBoolean(Constants.TIP_DISCOVER, true);
        if (tip) {
            DialogManager.showDiscoverTipDialog(this);
            NormalPreferences.getInstance().putBoolean(Constants.TIP_DISCOVER, false);
        }
    }

    /**
     * 遮罩提示页：找游戏
     */
    private void showGameTipDialog() {

        boolean tip = NormalPreferences.getInstance().getBoolean(Constants.TIP_GAME, true);
        if (tip) {
            DialogManager.showGameTipDialog(this);
            NormalPreferences.getInstance().putBoolean(Constants.TIP_GAME, false);
        }
    }

    /**
     * 遮罩提示页：赛事
     */
    private void showMatchTipDialog() {
        boolean tip = NormalPreferences.getInstance().getBoolean(Constants.TIP_MATCH, true);
        if (tip) {
            DialogManager.showMatchTipDialog(this);
            NormalPreferences.getInstance().putBoolean(Constants.TIP_MATCH, false);
        }
    }

    /**
     * 事件：登录
     */
    public void onEventMainThread(LoginEvent event) {

        if (event != null) {
            refreshActionBar();
        }
    }


    /**
     * 回调：融云获取token
     */
    public void onEventMainThread(GetRongCloudToken204Entity event) {

        if (event != null) {
            if (event.isResult()) {
                //链接融云
                RongIMHelper.connectIM();
            }
        }
    }

    /**
     * 事件：注销
     */
    public void onEventMainThread(LogoutEvent event) {

        if (event != null) {
            refreshActionBar();
        }
    }

    /**
     * 事件：更新个人资料
     */
    public void onEventMainThread(UserInfomationEvent event) {

        if (event != null) {
            refreshActionBar();
        }
    }

    /**
     * 回调：版本更新
     */
    public void onEventMainThread(UpdateVersionEntity event) {
        if (event != null && event.isResult()) {
            Update update = event.getData().get(0);
            if (update != null && !isShowedUpdate) {
                updateVersion(update);
            }
        }
    }

    /**
     * 补丁文件
     */
    public void  onEventMainThread(PatchEntity entity){
        if (entity.isResult()){
            mValidation = entity.getData().getValidation();
            PatchEntity.DataBean data = entity.getData();
            if (data.getChannel_id() != null && data.getChannel_id().equals(AnalyticsConfig.getChannel(MainActivity.this))){
               final File patchFile = SYSJStorageUtil.createFilecachePath(data.getDownload_url());
                if (BuildConfig.VERSION_NAME.equals(data.getApp_version()) && !StringUtil.isNull(data.getDownload_url())){
                    //生成MD5
                    String md5 = MD5Util.string2MD5(data.getApp_version()+data.getPatch_version()+data.getChannel_id()+data.getDownload_url());
                    if (patchFile != null && patchFile.exists() && md5.equals(SharedPreferencesUtils.getPreference(this, AppConstant.PATCH_MD5))){
                        //Tinker是线性覆盖 重复加载补丁没有影响
                        TinkerInstaller.onReceiveUpgradePatch(getApplicationContext(), patchFile.getAbsolutePath());
                        return;
                    }
                    SharedPreferencesUtils.setPreference(this, AppConstant.PATCH_MD5,md5);
                    //拉取补丁
                    if (!StringUtil.isNull(data.getDownload_url())){
                       new FileDownloadRequest().download(data.getDownload_url(),patchFile.getAbsolutePath(),0,new FileDownloadRequest.DownloadListener(){
                           @Override
                           public void progress(long totalBytesRead, long contentLength, boolean isDone) {
                             //  Log.d("FileDownloadRequest","totalBytesRead:"+totalBytesRead+" contentLength:"+contentLength+" isDone:"+isDone);
                               if (isDone){
                                   String validation= "";
                                   try {
                                       validation = MD5Util.getFileMD5(patchFile);
                                   } catch (NoSuchAlgorithmException e) {
                                       e.printStackTrace();
                                   } catch (IOException e) {
                                       e.printStackTrace();
                                   }
                                   if (mValidation.equals(validation)){
                                       TinkerInstaller.onReceiveUpgradePatch(getApplicationContext(), patchFile.getAbsolutePath());
                                   }
                               }
                           }

                           @Override
                           public void finish() {

                           }
                       });
                    }
                }
            }
        }
    }

    /**
     * 补丁下载成功
     */
   /* public void onEventMainThread(DownloadSuccessEntity entity){
        if (!StringUtil.isNull(entity.getUrl()) && !StringUtil.isNull(mValidation)){
            File patchFile = SYSJStorageUtil.createFilecachePath(entity.getUrl());
            if (patchFile != null && patchFile.exists()){

                String validation= "";

                try {
                    validation = MD5Util.getFileMD5(patchFile);
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                if (mValidation.equals(validation)){
                    TinkerInstaller.onReceiveUpgradePatch(getApplicationContext(), patchFile.getAbsolutePath());
                }
            }
        }
    }*/



    private UnReadMessageEvent mUnReadMsg;
    /**
     *未读消息红点
     */
    public void updateUnReadMessage(UnReadMessageEvent event){
        if (event != null && leftCount != null){
            mUnReadMsg = event;
            if (viewPager != null && viewPager.getCurrentItem() == 3){
                return;
            }
        }
    }

    /**
     * IM 消息URL解析结果
     */
    public void onEventMainThread(ParseResultEntity entity){
        if (entity != null && entity.isResult()){
            switch (entity.getData().getType()){
                case "event":
                    ActivityManager.startGameMatchDetailActivity(MainActivity.this,entity.getData().getId());
                    break;                 //点击了赛事链接
                case "activity":
                    ActivityManager.startActivityDetailActivity(MainActivity.this,entity.getData().getId());
                    break;              //点击了活动链接
                case "video":
                    VideoImage item = new VideoImage();
                    item.setQn_key(entity.getData().getKey());
                    item.setVideo_id(entity.getData().getId());
                    ActivityManager.startVideoPlayActivity(MainActivity.this,item);
                    break;                 //点击了视频链接
            }
        }
    }

    private void updateVersion(final Update update) {
        LogHelper.i(tag, "updateVersion  ");
        if ("U".equals(update.getUpdate_flag()) || // 可用升级
                "A".equals(update.getUpdate_flag())) {// 强制升级
            // 版本更新对话框
            DialogManager.showUpdateDialog(this, update);
            isShowedUpdate = true;
        } else {// N:最新版本
            Log.d(tag, "updateVersion: New");
        }
    }

    /**
     *聊天界面点击事件
     */
    @SuppressWarnings("unused")
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onChatWindowClickListener(ChatWindowEntity entity){
        if (entity != null && entity.getId() == com.ifeimo.im.R.id.id_top_right_layout){
            if (!StringUtil.isNull(entity.getReceiverID())){
                Member member = new Member();
                member.setId(entity.getReceiverID());
                member.setMember_id(entity.getReceiverID());
                ActivityManager.startPlayerPersonalInfoActivity(this,member);
            }
        }
    }

    public class OnTabClickListener implements OnClickListener {

        private int index;

        public OnTabClickListener(int i) {
            this.index = i;
        }

        @Override
        public void onClick(View v) {
            switchTab(index);
            switchActionBar(index);
            refreshSystemBar(index);
            viewPager.setCurrentItem(index);
        }
    }
}
