package com.li.videoapplication.data.js;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.webkit.JavascriptInterface;

import com.li.videoapplication.data.DataManager;
import com.li.videoapplication.data.model.entity.Download;
import com.li.videoapplication.data.model.entity.FGame;
import com.li.videoapplication.data.model.response.GameDetailEntity;
import com.li.videoapplication.data.preferences.PreferencesHepler;
import com.li.videoapplication.framework.AppAccount;
import com.li.videoapplication.framework.AppManager;
import com.li.videoapplication.framework.TBaseActivity;
import com.li.videoapplication.mvp.Constant;
import com.li.videoapplication.mvp.billboard.view.MatchRewardBillboardActivity;
import com.li.videoapplication.mvp.match.view.MatchResultActivity;
import com.li.videoapplication.tools.DownloadHelper;
import com.li.videoapplication.tools.UmengAnalyticsHelper;
import com.li.videoapplication.ui.ActivityManager;
import com.li.videoapplication.ui.DialogManager;
import com.li.videoapplication.ui.activity.MainActivity;
import com.li.videoapplication.ui.activity.MyWalletActivity;
import com.li.videoapplication.ui.activity.WebActivity;
import com.li.videoapplication.utils.StringUtil;

/**
 * App端和JS交互接口
 */
public class JSInterface {

    public static final String TAG = JSInterface.class.getSimpleName();

    private Context context;

    public JSInterface(Context context) {
        this.context = context;
    }

    @JavascriptInterface
    public String getMember_id() {
        Log.d(TAG, "getMember_id: // ----------------------------------------");
        return PreferencesHepler.getInstance().getMember_id();
    }

    @JavascriptInterface
    public void setMember_id(String member_id) {

        Log.d(TAG, "setMember_id: // ----------------------------------------");
        Log.d(TAG, "setMember_id: member_id=" + member_id);
        if (!StringUtil.isNull(member_id)) {
            // 登录用户id
            PreferencesHepler.getInstance().saveMember_id(member_id);
            // 个人资料
            DataManager.userProfilePersonalInformation(member_id, member_id);
        }
    }

    @JavascriptInterface
    public void removeMember_id() {
        Log.d(TAG, "removeMember_id: // ----------------------------------------");
        // 应用全局注销
        AppAccount.logout();
    }

    @JavascriptInterface
    public String getPlatform() {
        Log.d(TAG, "getPlatform: // ----------------------------------------");
        return "sysj";
    }

    @JavascriptInterface
    public void downloadGame(String game_id) {
        Log.d(TAG, "downloadGame: // ----------------------------------------");
        Log.d(TAG, "downloadGame: game_id=" + game_id);
        try {
            TBaseActivity activity = (TBaseActivity) this.context;
            activity.showProgressDialog("请稍后...");
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (!StringUtil.isNull(game_id)) {
            // 游戏详情
            DataManager.gameDetail(game_id);
        }
    }

    /**
     * 回调：游戏详情
     */
    public void onEventMainThread(GameDetailEntity event) {
        Log.d(TAG, "onMessage: // ----------------------------------------");
        Log.d(TAG, "onMessage: event=" + event);
        try {
            TBaseActivity activity = (TBaseActivity) this.context;
            activity.dismissProgressDialog();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (event.getCode() == 200) {// 成功
            FGame fGame = event.getData();
            if (fGame != null && !StringUtil.isNull(fGame.getDownlink())) {

                String app_name = fGame.getGamename();
                Download download = new Download();
                download.setDownload_url(fGame.getDownlink());
                download.setTitle(app_name);
                DownloadHelper.downloadFile(context, download);
            }
        }
    }

    //赛事奖金榜-我要参赛
    @JavascriptInterface
    public void apply() {
        Log.d(TAG, "apply: // ----------------------------------------");
        try {
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    AppManager.getInstance().removeActivity(MatchRewardBillboardActivity.class);
                    final MainActivity activity = AppManager.getInstance().getMainActivity();
                    activity.viewPager.setCurrentItem(3);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        UmengAnalyticsHelper.onEvent(context, UmengAnalyticsHelper.DISCOVER, "赛事奖金榜-我要参赛");
    }

    @JavascriptInterface
    public void ap() {
        Log.d(TAG, "ap: // ----------------------------------------");
        try {
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    AppManager.getInstance().removeActivity(MatchResultActivity.class);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //抽奖页 登陆
    @JavascriptInterface
    public void login() {
        Log.d(TAG, "login: // ----------------------------------------");
        try {
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    DialogManager.showLogInDialog(context);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //抽奖页 做任务
    @JavascriptInterface
    public void doTask() {
        Log.d(TAG, "dotask: // ----------------------------------------");
        try {
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    if (!PreferencesHepler.getInstance().isLogin())
                        DialogManager.showLogInDialog(context);
                    else
                        ActivityManager.startMyWalletActivity(context, MyWalletActivity.PAGE_TASK);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //抽奖页 立即充值
    @JavascriptInterface
    public void pay() {
        Log.d(TAG, "pay: // ----------------------------------------");
        try {
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    if (PreferencesHepler.getInstance().isLogin()) {
                        ActivityManager.startTopUpActivity(context, Constant.TOPUP_ENTRY_SWEEP,0);
                    } else {
                        DialogManager.showLogInDialog(context);
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //抽奖页后退
    @JavascriptInterface
    public void back() {
        Log.d(TAG, "back: // ----------------------------------------");
        try {
            AppManager.getInstance().removeActivity(WebActivity.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
