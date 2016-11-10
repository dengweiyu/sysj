package com.li.videoapplication.data.js;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.util.Log;
import android.webkit.JavascriptInterface;

import com.li.videoapplication.data.DataManager;
import com.li.videoapplication.data.preferences.PreferencesHepler;
import com.li.videoapplication.framework.AppAccount;
import com.li.videoapplication.utils.StringUtil;

/**
 * App端和JS交互：用户信息
 */
public class User {

    public static final String TAG = User.class.getSimpleName();

    private Context context;

    public User(Context context) {
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
        return "lpds";
    }
}
