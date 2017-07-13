package com.li.videoapplication.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;

import com.li.videoapplication.framework.AppManager;

/**
 * 功能：屏幕工具
 */
public class ScreenUtil {

    /**
     * 功能：屏幕单位转换
     */
    public static int px2dp(float pxValue) {
        Context context = AppManager.getInstance().getContext();
        return px2dp(context, pxValue);
    }

    /**
     * 功能：屏幕单位转换
     */
    public static int dp2px(float dpValue) {
        Context context = AppManager.getInstance().getContext();
        return dp2px(context, dpValue);
    }

    /**
     * 功能：屏幕单位转换
     */
    public static int px2dp(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 功能：屏幕单位转换
     */
    public static int dp2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }


    /**
     * 功能：sp to px
     */
    public static int sp2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 功能：获得屏幕宽度（px）
     */
    public static int getScreenWidth() {
        Context context = AppManager.getInstance().getContext();
        return context.getResources().getDisplayMetrics().widthPixels;
    }

    /**
     * 功能：获得屏幕高度（px）
     */
    public static int getScreenHeight() {
        Context context = AppManager.getInstance().getContext();
        return context.getResources().getDisplayMetrics().heightPixels;
    }

    /**
     * 获得屏幕宽度（px）
     */
    public static int getScreenWidth(WindowManager manager) {
        DisplayMetrics metrics = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(metrics);
        return metrics.widthPixels;
    }

    /**
     * 获得屏幕高度（px）
     */
    public static int getScreenHeight(WindowManager manager) {
        DisplayMetrics metrics = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(metrics);
        return metrics.heightPixels;
    }

    /**
     * 获取状态栏高度
     *
     * @param activity
     * @return > 0 success; <= 0 fail
     */
    public static int getStatusHeight(Activity activity) {
        int statusHeight = 0;
        Rect localRect = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(localRect);
        statusHeight = localRect.top;
        if (0 == statusHeight) {
            Class<?> localClass;
            try {
                localClass = Class.forName("com.android.internal.R$dimen");
                Object localObject = localClass.newInstance();
                int i5 = Integer.parseInt(localClass.getField("status_bar_height").get(localObject).toString());
                statusHeight = activity.getResources().getDimensionPixelSize(i5);

            } catch (ClassNotFoundException | IllegalAccessException |
                    InstantiationException | IllegalArgumentException |
                    SecurityException | NoSuchFieldException e) {
                e.printStackTrace();
            }
        }
        return statusHeight;
    }

    /**
     * 获得屏幕比例
     */
    public static float getAreaOneRatio(Activity activity){
//		Log.d(TAG, "getAreaOneRatio: // -------------------------------------------------------------");
        int[] size = getAreaOne(activity);
        int a = size[0];
        int b = size[1];
        float ratio = 0f;
        try {
            if (a > b) {
                ratio = (float) a / (float) b;
            } else {
                ratio = (float) b / (float) a;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
//		Log.d(TAG, "getAreaOneRatio: ratio=" + ratio);
        return ratio;
    }


    /**
     * 获得屏幕尺寸（宽 x 高）（px）
     */
    public static int[] getAreaOne(Activity activity){
//		Log.d(TAG, "getAreaOne: // -------------------------------------------------------------");
        Display display = activity.getWindowManager().getDefaultDisplay();
        Point point = new Point();
        display.getSize(point);
//		Log.d(TAG, "getAreaOne: width=" + point.x);
//		Log.d(TAG, "getAreaOne: height=" + point.y);
        return new int[]{ point.x, point.y };
    }

    /**
     * 获取View的图像帧
     * @param v
     * @return
     */
    public static Bitmap getViewShot(View v) {
        Bitmap b = Bitmap.createBitmap(v.getWidth(), v.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(b);
        v.draw(c);
        return b;
    }
}
