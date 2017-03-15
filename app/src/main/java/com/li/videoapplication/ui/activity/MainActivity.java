package com.li.videoapplication.ui.activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
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
import com.ifeimo.im.framwork.IMSdk;
import com.ifeimo.im.framwork.message.OnSimpleMessageListener;
import com.ifeimo.screenrecordlib.RecordingManager;
import com.ifeimo.screenrecordlib.TaskUtil;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu.OnCloseListener;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu.OnClosedListener;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu.OnOpenListener;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu.OnOpenedListener;
import com.li.videoapplication.R;
import com.li.videoapplication.data.DataManager;
import com.li.videoapplication.data.image.GlideHelper;
import com.li.videoapplication.data.model.entity.Update;
import com.li.videoapplication.data.model.event.LoginEvent;
import com.li.videoapplication.data.model.event.LogoutEvent;
import com.li.videoapplication.data.model.event.UserInfomationEvent;
import com.li.videoapplication.data.model.response.GetRongCloudToken204Entity;
import com.li.videoapplication.data.model.response.UpdateVersionEntity;
import com.li.videoapplication.data.preferences.Constants;
import com.li.videoapplication.data.preferences.NormalPreferences;
import com.li.videoapplication.data.preferences.PreferencesHepler;
import com.li.videoapplication.framework.AppManager;
import com.li.videoapplication.framework.BaseSlidingActivity;
import com.li.videoapplication.mvp.home.view.HomeFragment;
import com.li.videoapplication.mvp.match.view.GameMatchFragment;
import com.li.videoapplication.tools.FeiMoIMHelper;
import com.li.videoapplication.tools.RongIMHelper;
import com.li.videoapplication.tools.ToastHelper;
import com.li.videoapplication.tools.UmengAnalyticsHelper;
import com.li.videoapplication.ui.ActivityManeger;
import com.li.videoapplication.ui.DialogManager;
import com.li.videoapplication.ui.fragment.DiscoverFragment;
import com.li.videoapplication.ui.fragment.GameFragment;
import com.li.videoapplication.ui.fragment.SliderFragment;
import com.li.videoapplication.ui.pageradapter.WelfarePagerAdapter;
import com.li.videoapplication.utils.AppUtil;
import com.li.videoapplication.utils.HareWareUtil;
import com.li.videoapplication.utils.LogHelper;
import com.li.videoapplication.utils.NetUtil;
import com.li.videoapplication.utils.StringUtil;
import com.li.videoapplication.views.CircleImageView;
import com.li.videoapplication.views.ViewPagerY4;
import com.readystatesoftware.systembartint.SystemBarTintManager;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import io.rong.eventbus.EventBus;
import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import me.everything.android.ui.overscroll.HorizontalOverScrollBounceEffectDecorator;
import me.everything.android.ui.overscroll.adapters.ViewPagerOverScrollDecorAdapter;

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
    private List<LinearLayout> bottomButtons;
    private List<ImageView> bottomIcon;
    private List<TextView> bottomText;

    private ImageView right, leftIcon;
    private CircleImageView leftHead;
    public CircleImageView leftCount;

    private FrameLayout left;
    private TextView title;
    private LinearLayout search;
    private ImageView searchIcon;
    private TextView searchText;
    private ImageView scanQnCode;
    private View background;
    private int currIndex = 0;// 当前页卡编号
    private List<Fragment> fragments;
    private HomeFragment home;
    private DiscoverFragment discover;
    private GameFragment game;
    private GameMatchFragment matchFragment;

    private Fragment context;
    private SliderFragment slider;
    public ViewPagerY4 viewPager;
    private WelfarePagerAdapter adapter;
    private boolean isLogin = false;
    private String imageUrl;
    private boolean isShowedUpdate = false;
    private SlidingMenu.CanvasTransformer mTransformer;

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
        ActivityManeger.startSearchActivity(this);
        UmengAnalyticsHelper.onEvent(this, UmengAnalyticsHelper.MAIN, "点击搜索框次数");
    }

    /**
     * 跳转：二维码扫描
     */
    private void startScanQRCodeActivity() {
        ActivityManeger.startScanQRCodeActivity(this);
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
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        getIntentValue();
        getIntentResult();
    }

    private int tab;

    private void getIntentValue() {
        if (getIntent() != null) {
            try {
                tab = getIntent().getIntExtra("tab", 0);
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
        }
    }

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
                if (count > 0) {
                    leftCount.setVisibility(View.VISIBLE);
                } else {
                    leftCount.setVisibility(View.GONE);
                }
            }
        });
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        AppManager.getInstance().removeMainActivity();
        super.onDestroy();
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
        search.setOnClickListener(this);
        searchIcon.setOnClickListener(this);
        searchText.setOnClickListener(this);
        scanQnCode.setOnClickListener(this);
        left.setOnClickListener(this);
        right.setOnClickListener(this);
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
                DialogManager.showRecordDialog(this);
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
                right.setImageResource(R.drawable.ab_vedio_white_205);
                left.setBackgroundResource(R.drawable.ab_red);
                right.setBackgroundResource(R.drawable.ab_red);
                title.setVisibility(View.GONE);
                search.setVisibility(View.VISIBLE);
                search.setBackgroundResource(R.drawable.search_bg);
                leftCount.setImageResource(R.color.white);
                break;

            case 1:// 发现
                background.setBackgroundResource(R.color.ab_backdround_white);
                right.setImageResource(R.drawable.ab_vedio_red_205);
                left.setBackgroundResource(R.drawable.ab_white);
                right.setBackgroundResource(R.drawable.ab_white);
                title.setVisibility(View.GONE);
                search.setVisibility(View.VISIBLE);
                search.setBackgroundResource(R.drawable.search_bg_white);
                leftCount.setImageResource(R.color.ab_backdround_red);
                break;

            case 2:// 找游戏
                background.setBackgroundResource(R.color.ab_backdround_white);
                right.setImageResource(R.drawable.ab_vedio_red_205);
                left.setBackgroundResource(R.drawable.ab_white);
                right.setBackgroundResource(R.drawable.ab_white);
                title.setVisibility(View.GONE);
                search.setVisibility(View.VISIBLE);
                search.setBackgroundResource(R.drawable.search_bg_white);
                leftCount.setImageResource(R.color.ab_backdround_red);
                break;

            case 3:// 福利
                background.setBackgroundResource(R.color.ab_backdround_white);
                right.setImageResource(R.drawable.ab_vedio_red_205);
                left.setBackgroundResource(R.drawable.ab_white);
                right.setBackgroundResource(R.drawable.ab_white);
                title.setVisibility(View.GONE);
                title.setText(R.string.menu_main_fourth);
                search.setVisibility(View.VISIBLE);
                search.setBackgroundResource(R.drawable.search_bg_white);
                leftCount.setImageResource(R.color.ab_backdround_red);
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
        matchFragment = new GameMatchFragment();

        fragments.add(home);
        fragments.add(game);
        fragments.add(discover);
        fragments.add(matchFragment);
        viewPager = (ViewPagerY4) findViewById(R.id.pager);
        viewPager.setScrollable(true);
        viewPager.setOffscreenPageLimit(3);
        adapter = new WelfarePagerAdapter(manager, fragments);
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(this);

        new HorizontalOverScrollBounceEffectDecorator(new ViewPagerOverScrollDecorAdapter(viewPager));
    }

    public void initMenu() {

        menu = findViewById(R.id.menu);

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

    @Override
    public void onPageScrollStateChanged(int arg0) {
    }

    @Override
    public void onPageScrolled(int arg0, float arg1, int arg2) {
    }

    @Override
    public void onPageSelected(int position) {
        switchTab(position);
        switchActionBar(position);
        refreshSystemBar(position);
        currIndex = position;
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
                break;
            case 1: // 打游戏
                bottomIcon.get(0).setImageResource(R.drawable.home_normal);
                bottomIcon.get(1).setImageResource(R.drawable.game_selected);
                bottomIcon.get(2).setImageResource(R.drawable.discover_normal);
                bottomIcon.get(3).setImageResource(R.drawable.event_nomal);
                showGameTipDialog();
                break;
            case 2: // 发现
                bottomIcon.get(0).setImageResource(R.drawable.home_normal);
                bottomIcon.get(1).setImageResource(R.drawable.game_normal);
                bottomIcon.get(2).setImageResource(R.drawable.discover_selected);
                bottomIcon.get(3).setImageResource(R.drawable.event_nomal);
                showDiscoverTipDialog();
                break;
            case 3:// 福利
                bottomIcon.get(0).setImageResource(R.drawable.home_normal);
                bottomIcon.get(1).setImageResource(R.drawable.game_normal);
                bottomIcon.get(2).setImageResource(R.drawable.discover_normal);
                bottomIcon.get(3).setImageResource(R.drawable.event_selected);
                break;
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
        if (true) {
            Class<? extends Window> clazz = activity.getWindow().getClass();
            try {
                int darkModeFlag = 0;
                Class<?> layoutParams = Class.forName("android.view.MiuiWindowManager$LayoutParams");
                Field field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE");
                darkModeFlag = field.getInt(layoutParams);
                Method extraFlagField = clazz.getMethod("setExtraFlags", int.class, int.class);
                extraFlagField.invoke(activity.getWindow(), darkmode ? darkModeFlag : 0, darkModeFlag);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 遮罩提示页：视频录制
     */
    private void showVideoTipDialog() {

        boolean tip = NormalPreferences.getInstance().getBoolean(Constants.TIP_VEDIO, true);
        if (tip) {
            DialogManager.showVideoTipDialog(this);
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
            DialogManager.showGameTipDialogg(this);
            NormalPreferences.getInstance().putBoolean(Constants.TIP_GAME, false);
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
