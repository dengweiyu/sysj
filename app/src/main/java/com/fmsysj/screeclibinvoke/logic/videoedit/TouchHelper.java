package com.fmsysj.screeclibinvoke.logic.videoedit;

import android.content.Context;
import android.provider.Settings;

import com.li.videoapplication.framework.AppManager;


/**
 * 触摸
 */
public class TouchHelper {

    /**
     * 设置触摸显示
     */
    public static void toogle(boolean flag) {
        Context context = AppManager.getInstance().getContext();
        if (flag) {
            try {
                Settings.System.putInt(context.getContentResolver(), "show_touches", 1);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            try {
                Settings.System.putInt(context.getContentResolver(), "show_touches", 0);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
