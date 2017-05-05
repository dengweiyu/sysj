package com.ifeimo.im.common.util;

import android.os.Build;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Created by liuwei on 2017/4/6.
 */

public class StatusBarBlackTextHelper {

    /**
     * 设置状态栏字体颜色
     */
    public static void initStatusBarTextColor(Window window,boolean dark) {

            //6.0+设置状态栏字体颜色
            if (Build.VERSION.SDK_INT >= 23){
                if (dark){
                    window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE|View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                    Log.e("initStatusBar","系统6.0+ 设置深色字体成功");
                }else {
                    window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
                    Log.e("initStatusBar","系统6.0+ 设置亮色字体成功");
                }

            }else{
                if (setMiuiStatusBarLightMode(window,dark)){                         //miui
                    Log.e("initStatusBar","MIUI 设置深色字体成功");

                }else if (setFlymeStatusBarLightMode(window,dark)){                  //flyme
                    Log.e("initStatusBar","flyme 设置深色字体成功");
                }
            }

    }


    /**
     * MIUI6.0+ 黑色字体
     * @return
     */
    private static boolean setMiuiStatusBarLightMode(Window window,boolean dark){
        boolean result = false;
        if (window != null) {
            Class clazz = window.getClass();
            try {
                int darkModeFlag = 0;
                Class layoutParams = Class.forName("android.view.MiuiWindowManager$LayoutParams");
                Field field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE");
                darkModeFlag = field.getInt(layoutParams);
                Method extraFlagField = clazz.getMethod("setExtraFlags", int.class, int.class);
                extraFlagField.invoke(window,dark?darkModeFlag:0,darkModeFlag);        //黑色字体
                result=true;
            }catch (Exception e){
            }
        }
        return result;
    }

    /**
     * flyme 黑色字体
     * @return
     */
    private static boolean setFlymeStatusBarLightMode(Window window,boolean dark){
        boolean result = false;
        if (window != null) {
            try {
                WindowManager.LayoutParams lp = window.getAttributes();
                Field darkFlag = WindowManager.LayoutParams.class
                        .getDeclaredField("MEIZU_FLAG_DARK_STATUS_BAR_ICON");
                Field meizuFlags = WindowManager.LayoutParams.class
                        .getDeclaredField("meizuFlags");
                darkFlag.setAccessible(true);
                meizuFlags.setAccessible(true);
                int bit = darkFlag.getInt(null);
                int value = meizuFlags.getInt(lp);
                if (dark){
                    value |= bit;
                }else {
                    value &=~value;
                }

                meizuFlags.setInt(lp, value);
                window.setAttributes(lp);
                result = true;
            } catch (Exception e) {

            }
        }
        return result;
    }
}
