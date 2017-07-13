package com.ifeimo.im.framwork.setting;

import android.content.Context;
import android.content.SharedPreferences;

import com.ifeimo.im.framwork.IMSdk;
import com.ifeimo.im.framwork.commander.IMSetting;

/**
 * Created by lpds on 2017/3/3.
 */
public class Builder {

    private static final String SP_NAME = "im_setting_sp";
    private static Notification notification;
    private IMSetting settingManager;

    public Builder(IMSetting imSetting) {
        settingManager = imSetting;
    }

    /**
     * 获取 Notification 设置
     * @param refresh true 重新获取缓存
     * @return
     */
    public Notification getNotificationSettings(boolean refresh) {
        if (notification == null) {
            notification = new Notification();
            refresh = true;
        }
        if (refresh) {
            getNotificationSettings(notification);
        }
        return notification;
    }

    public void setNotificationSettings(Notification notificationSettings) {
        notification = notificationSettings;
        SharedPreferences sharedPreferences = IMSdk.CONTEXT.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        sharedPreferences.edit().putString(Notification.MODE,notification.getMode()).commit();
    }

    private void getNotificationSettings(Notification notification) {
        SharedPreferences sharedPreferences = IMSdk.CONTEXT.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        String set = sharedPreferences.getString(Notification.MODE, Notification.AUTO_MODE);
        if (!set.equals("-1")) {
            notification.setMode(set);
        } else {
            notification.setMode(Notification.APP_BACKGROUND_MODE);
        }
    }

    public static final class Notification {
        public static final String MODE = "notification_mode";
        /**
         * 那里都显示
         */
        public static final String AUTO_MODE = "0x1000";
        /**
         *
         */
        public static final String APP_TOP_MODE = "0x1001";
        /**
         * app在后端才显示
         */
        public static final String APP_BACKGROUND_MODE = "0x1002";
        public String mode;

        public String getMode() {
            return mode;
        }

        public void setMode(String mode) {
            switch (mode) {
                case AUTO_MODE:
                case APP_TOP_MODE:
                case APP_BACKGROUND_MODE:
                    this.mode = mode;
                    break;
                default:
                    this.mode = "-1";
                    break;
            }
        }
    }

}
