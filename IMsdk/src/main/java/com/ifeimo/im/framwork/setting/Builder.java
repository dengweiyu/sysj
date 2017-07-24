package com.ifeimo.im.framwork.setting;

import android.app.Notification;
import android.content.Context;
import android.content.SharedPreferences;

import com.ifeimo.im.framwork.IMSdk;
import com.ifeimo.im.framwork.commander.IMSetting;

/**
 * Created by lpds on 2017/3/3.
 */
public class Builder {

    private static final String SP_NAME = "im_setting_sp";
    private static NotificationMode notification;
    private IMSetting settingManager;

    public Builder(IMSetting imSetting) {
        settingManager = imSetting;
        getNotificationSettings(true);
    }

    /**
     * 获取 Notification 设置
     * @param refresh true 重新获取缓存
     * @return
     */
    public NotificationMode getNotificationSettings(boolean refresh) {
        synchronized (this) {
            if (notification == null) {
                notification = new NotificationMode();
                refresh = true;
            }
            if (refresh) {
                getNotificationSettings(notification);
            }
            return (NotificationMode) notification.clone();
        }
    }

    public void setNotificationSettings(NotificationMode notificationSettings) {
        synchronized (this) {
            notification = notificationSettings;
            SharedPreferences sharedPreferences = IMSdk.CONTEXT.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
            sharedPreferences.edit().putString(NotificationMode.MODE, notification.getMode()).commit();
        }
        getNotificationSettings(true);
    }

    private void getNotificationSettings(NotificationMode notification) {
        synchronized (this) {
            SharedPreferences sharedPreferences = IMSdk.CONTEXT.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
            String set = sharedPreferences.getString(NotificationMode.MODE, NotificationMode.APP_AUTO_MODE);
            notification.setMode(set);
        }
    }

    public static final class NotificationMode implements Cloneable{
        public static final String MODE = "notification_mode";
        /**
         * 那里都显示
         */
        public static final String APP_AUTO_MODE = "0x1000";
        /**
         * 不显示
         */
        public static final String APP_NONE_MODE = "0x1001";
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
                case APP_NONE_MODE:
                case APP_BACKGROUND_MODE:
                    this.mode = mode;
                    break;
                default:
                    this.mode = APP_AUTO_MODE;
                    break;
            }
        }

        @Override
        protected Object clone(){
            try {
                return super.clone();
            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

}
