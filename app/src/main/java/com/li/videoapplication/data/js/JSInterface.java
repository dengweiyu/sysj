package com.li.videoapplication.data.js;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.webkit.JavascriptInterface;

import com.li.videoapplication.data.DataManager;
import com.li.videoapplication.data.model.response.MatchRewardBillboardEntity;
import com.li.videoapplication.data.preferences.PreferencesHepler;
import com.li.videoapplication.framework.AppAccount;
import com.li.videoapplication.framework.AppManager;
import com.li.videoapplication.mvp.billboard.view.MatchRewardBillboardActivity;
import com.li.videoapplication.mvp.match.view.MatchResultActivity;
import com.li.videoapplication.tools.ToastHelper;
import com.li.videoapplication.ui.ActivityManeger;
import com.li.videoapplication.ui.activity.MainActivity;
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
}
