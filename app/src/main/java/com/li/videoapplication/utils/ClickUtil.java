package com.li.videoapplication.utils;

import android.util.Log;

import java.util.Calendar;

/**
 * 工具类：点击
 */
public class ClickUtil {
    private static final int MIN_CLICK_DELAY_TIME = 500;
    private static long lastClickTime;

    /*
    * 记录点击的时间，时间过短返回false
    */
    public static boolean canClick() {

        long currentTime = Calendar.getInstance().getTimeInMillis();

        if (currentTime - lastClickTime > MIN_CLICK_DELAY_TIME) {
            lastClickTime = currentTime;
            return true;
        } else {
            return false;
        }
    }
}
