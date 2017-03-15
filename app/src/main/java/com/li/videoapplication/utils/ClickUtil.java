package com.li.videoapplication.utils;

import java.util.Calendar;

/**
 * 工具类：点击
 */
public class ClickUtil {
    private static int MIN_CLICK_DELAY_TIME = 600;
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

    /*
   * 记录点击的时间，时间过短返回false
   */
    public static boolean canClick(int delayTime) {
        MIN_CLICK_DELAY_TIME = delayTime;
        return canClick();
    }
}
