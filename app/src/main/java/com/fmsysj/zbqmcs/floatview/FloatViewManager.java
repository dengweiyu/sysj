package com.fmsysj.zbqmcs.floatview;

/**
 * 浮窗管理
 *
 * @author lin
 * Create：2014-07
 */

import android.content.Context;
import android.graphics.PixelFormat;
import android.media.AudioManager;
import android.media.SoundPool;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;

import com.fmsysj.zbqmcs.utils.ExApplication;
import com.fmsysj.zbqmcs.utils.RUtils;
import com.fmsysj.zbqmcs.utils.RecordVideo;
import com.li.videoapplication.framework.AppManager;

public class FloatViewManager {

    public static final String TAG = FloatViewManager.class.getSimpleName();

    public static boolean isLandscape = false; // 是否横屏
    private WindowManager windowManager;
    private Context context;
    private LayoutParams params1;
    private LayoutParams params2;
    private LayoutParams paramsContent;

    private FloatView1 floatView1;
    private FloatView2 floatView2;
    private FloatContentView floatContentView;
    private FloatContentView2 floatContentView2;
    private int displayWidth;
    private int displayHeight;
    private int isView1 = 0;
    private int layY1 = 0;

    private FloatViewManager() {
        context = AppManager.getInstance().getContext();
        windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        displayWidth = windowManager.getDefaultDisplay().getWidth();
        displayHeight = windowManager.getDefaultDisplay().getHeight();
        layY1 = displayHeight / 2;
    }

    private static FloatViewManager instance;

    public static synchronized FloatViewManager getInstance() {
        if (instance == null) {
            synchronized (FloatViewManager.class) {
                if (instance == null) {
                    instance = new FloatViewManager();
                }
            }
        }
        return instance;
    }

    public static void destroyView() {
        try {
            instance.destoryAllFloatView();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            if (instance != null)
                instance = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void destoryAllFloatView() {
        try {
            windowManager.removeView(floatView1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            windowManager.removeView(floatView2);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            windowManager.removeView(floatContentView);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            windowManager.removeView(floatContentView2);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 显示浮窗1
     */
    public void showFirst() {
        floatView1 = getView();
        if (floatView1.getParent() == null) {
            windowManager.addView(floatView1, params1);
            isView1 = 1;
        }
    }

    /**
     * 移除浮窗1
     */
    public void removeFirst() {
        if (floatView1 != null) {
            windowManager.removeView(floatView1);
            floatView1 = null;
            isView1 = 0;
        }
    }


    /**
     * 展开浮窗详情页
     */
    public void showContent() {
        floatContentView = getContentView();
        if (floatContentView.getParent() == null) {
            windowManager.addView(floatContentView, paramsContent);
        }

        isView1 = 0;
    }

    /**
     * 显示浮窗2并同时移除浮窗1
     */
    public void Show2AndRemove1() {
        if (params1 != null) {
            layY1 = params1.y;
            windowManager.addView(getView2(), params2);
            windowManager.removeView(floatView1);
            params1 = null;
            floatView1 = null;
        } else if (paramsContent != null) {
            layY1 = paramsContent.y;
            windowManager.addView(getView2(), params2);
            windowManager.removeView(floatContentView);
            paramsContent = null;
            floatContentView = null;
        }
        isView1 = 2;
    }

    /**
     * 显示浮窗1并同时移除浮窗2
     */
    public void Show1AndRemove2() {
        try {
            layY1 = params2.y;
            windowManager.addView(getView1(), params1);
            windowManager.removeView(floatView2);
            params2 = null;
            floatView2 = null;
            isView1 = 1;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 显示浮窗展开状态
     *
     * @param view
     */
    public void showContent(View view) {
        if (!RecordVideo.isStart) {
            try {
                layY1 = params1.y;
                windowManager.addView(getContentView(), paramsContent);
                windowManager.removeView(view);
                params1 = null;
                params2 = null;
                floatView1 = null;
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            layY1 = params2.y;
            windowManager.addView(getContentView(), paramsContent);
            windowManager.removeView(view);
            params1 = null;
            params2 = null;
            floatView1 = null;
        }
        isView1 = 0;
    }

    public void showContentForView2(View view) {

        try {
            windowManager.addView(getContentView2(), paramsContent);
            windowManager.removeView(view);
            params1 = null;
            params2 = null;
            floatView2 = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 回收浮窗弹出状态(平时状态)
    public void back() {
        if (!RecordVideo.isStart) {
            if (paramsContent != null) {
                layY1 = paramsContent.y;
                // 如果窗口已经添加，则移除后再次添加窗口
                try {
                    windowManager.addView(getView1(), params1);
                } catch (Exception e) {
                    windowManager.removeView(getView1());
                    windowManager.addView(getView1(), params1);
                }
                windowManager.removeView(floatContentView);
                paramsContent = null;
                floatContentView = null;
                isView1 = 1;
                SoundPool soundPool = new SoundPool(10, AudioManager.STREAM_SYSTEM, 5);
                soundPool.play(1, 1, 1, 0, 0, 1);
                instance.back();
            }
        } else {
            if (paramsContent != null) {
                layY1 = paramsContent.y;
                windowManager.addView(getView2(), params2);
                windowManager.removeView(floatContentView);
                paramsContent = null;
                floatContentView = null;
                isView1 = 2;
            }
        }
    }

    // 回收浮窗弹出状态(录屏状态)
    public void back2() {
        if (paramsContent != null) {
            layY1 = paramsContent.y;
            windowManager.addView(getView2(), params2);
            windowManager.removeView(floatContentView2);
            paramsContent = null;
            floatContentView2 = null;
            isView1 = 2;
        }
    }

    public void move(View view, int delatX, int deltaY) {
        if (view == floatView1) {
            params1.x += delatX;
            params1.y += deltaY;
            layY1 = params1.y;
            // 更新View
            windowManager.updateViewLayout(view, params1);
        } else if (view == floatView2) {
            params2.x += delatX;
            params2.y += deltaY;
            layY1 = params2.y;
            // 更新View
            windowManager.updateViewLayout(view, params2);
        } else if (view == floatContentView) {
            paramsContent.x += delatX;
            paramsContent.y += deltaY;
            layY1 = paramsContent.y;
            // 更新View
            windowManager.updateViewLayout(view, paramsContent);
        }
    }

    // 移除悬浮框
    public void dismiss() {
        windowManager.removeView(floatContentView);
        floatContentView = null;
    }

    public void BackToView1() {
        if (paramsContent != null) {
            layY1 = paramsContent.y;
        }
        windowManager.addView(getView1(), params1);
        if (floatContentView2 != null) {
            windowManager.removeView(floatContentView2);
        }
        if (params2 != null) {
            windowManager.removeView(floatView2);
        }
        paramsContent = null;
        floatContentView2 = null;
        isView1 = 1;
    }

    public void BackToView2() {
        layY1 = paramsContent.y;
        try {
            windowManager.addView(getView2(), params2);
            windowManager.removeView(floatContentView);
            paramsContent = null;
            floatContentView = null;
            isView1 = 2;
        } catch (Exception e) {
            windowManager.removeView(floatContentView);
            paramsContent = null;
            floatContentView = null;
            isView1 = 2;
        }
    }

    public boolean isUpdate() {
        if (floatContentView == null) {
            return false;
        }
        return true;
    }

    private String getRString(String name) {
        return RUtils.getRString(name);
    }

    public FloatView1 getView() {
        if (floatView1 == null) {
            floatView1 = new FloatView1(context);
        }
        if (params1 == null) {
            params1 = new LayoutParams();
            params1.type = LayoutParams.TYPE_PHONE;
            params1.format = PixelFormat.RGBA_8888;
            params1.flags = LayoutParams.FLAG_NOT_TOUCH_MODAL | LayoutParams.FLAG_NOT_FOCUSABLE;
            params1.gravity = Gravity.LEFT | Gravity.TOP;

            params1.width = floatView1.mWidth;
            params1.height = floatView1.mHeight;

            params1.x = displayWidth - floatView1.mWidth;
            params1.y = displayHeight / 2;
        }
        return floatView1;
    }

    public FloatView1 getView1() {
        if (floatView1 == null) {
            floatView1 = new FloatView1(context);
        }
        if (params1 == null) {
            params1 = new LayoutParams();
            params1.type = LayoutParams.TYPE_PHONE;
            params1.format = PixelFormat.RGBA_8888;
            params1.flags = LayoutParams.FLAG_NOT_TOUCH_MODAL | LayoutParams.FLAG_NOT_FOCUSABLE;
            params1.gravity = Gravity.LEFT | Gravity.TOP;

            params1.width = floatView1.mWidth;
            params1.height = floatView1.mHeight;

            if (this.isLandscape) {
                // 如果浮窗1有进行移动，则按照移动后的坐标显示
                if (ExApplication.moveX != -1 && ExApplication.moveY != -1) {
                    params1.x = (int) ExApplication.moveX;
                    params1.y = (int) ExApplication.moveY - ExApplication.statusBarHeight;
                } else {
                    params1.x = displayHeight - floatView1.mWidth;
                    params1.y = layY1;
                }
            } else {
                // 如果如果计时浮窗1有进行移动，则按照移动后的坐标显示
                if (ExApplication.moveX != -1 && ExApplication.moveY != -1) {
                    params1.x = (int) ExApplication.moveX;
                    params1.y = (int) ExApplication.moveY - ExApplication.statusBarHeight;
                } else {
                    params1.x = displayHeight - floatView1.mWidth;
                    params1.y = layY1;
                }
            }
        }
        return floatView1;
    }

    public FloatView2 getView2() {
        if (floatView2 == null) {
            floatView2 = new FloatView2(context);
        }
        if (params2 == null) {
            params2 = new LayoutParams();
            params2.type = LayoutParams.TYPE_PHONE;
            params2.format = PixelFormat.RGBA_8888;
            params2.flags = LayoutParams.FLAG_NOT_TOUCH_MODAL | LayoutParams.FLAG_NOT_FOCUSABLE;
            params2.gravity = Gravity.LEFT | Gravity.TOP;
            params2.width = floatView2.mWidth;
            params2.height = floatView2.mHeight;
            if (this.isLandscape) {
                // 如果计时浮窗2有进行移动，则按照移动后的坐标显示
                if (ExApplication.moveX != -1 && ExApplication.moveY != -1) {
                    params2.x = (int) ExApplication.moveX;
                    params2.y = (int) ExApplication.moveY - ExApplication.statusBarHeight;
                } else {
                    params2.x = displayHeight - floatView2.mWidth;
                    params2.y = layY1;
                }
            } else {
                // 如果计时浮窗2有进行移动，则按照移动后的坐标显示
                if (ExApplication.moveX != -1 && ExApplication.moveY != -1) {
                    params2.x = (int) ExApplication.moveX;
                    params2.y = (int) ExApplication.moveY - ExApplication.statusBarHeight;
                } else {
                    params2.x = displayWidth - floatView2.mWidth;
                    params2.y = layY1;
                }
            }
        }
        return floatView2;
    }

    public FloatContentView getContentView() {
        if (floatContentView == null) {
            floatContentView = new FloatContentView(context);
        }
        if (paramsContent == null) {
            paramsContent = new LayoutParams();
            paramsContent.type = LayoutParams.TYPE_PHONE;
            paramsContent.format = PixelFormat.RGBA_8888;
            paramsContent.flags = LayoutParams.FLAG_NOT_TOUCH_MODAL | LayoutParams.FLAG_NOT_FOCUSABLE;
            paramsContent.gravity = Gravity.LEFT | Gravity.TOP;
            paramsContent.width = floatContentView.mWidth;
            paramsContent.height = floatContentView.mHeight;
            if (this.isLandscape) {
                paramsContent.x = displayHeight - floatContentView.mWidth;
                paramsContent.y = layY1;
            } else {
                paramsContent.x = displayWidth - floatContentView.mWidth;
                paramsContent.y = layY1;
            }
        }
        return floatContentView;
    }

    public FloatContentView2 getContentView2() {
        if (floatContentView2 == null) {
            floatContentView2 = new FloatContentView2(context);
        }
        if (paramsContent == null) {
            paramsContent = new LayoutParams();
            paramsContent.type = LayoutParams.TYPE_PHONE;
            paramsContent.format = PixelFormat.RGBA_8888;
            paramsContent.flags = LayoutParams.FLAG_NOT_TOUCH_MODAL | LayoutParams.FLAG_NOT_FOCUSABLE;
            paramsContent.gravity = Gravity.LEFT | Gravity.TOP;
            paramsContent.width = floatContentView2.mWidth;
            paramsContent.height = floatContentView2.mHeight;
            if (this.isLandscape) {
                paramsContent.x = displayHeight - floatContentView2.mWidth;
                paramsContent.y = layY1;
            } else {
                paramsContent.x = displayWidth - floatContentView2.mWidth;
                paramsContent.y = layY1;
            }
        }
        return floatContentView2;
    }

    // 输入参数：1为横屏，0为竖屏
    public void changeOrientation(int i) {
        switch (i) {
            case 0: {
                if (paramsContent != null) {
                    paramsContent.x = displayWidth - floatContentView.mWidth;
                    paramsContent.y = displayHeight / 2;
                    // windowManager.updateViewLayout(floatContentView, paramsContent);
                } else if (params1 != null) {
                    params1.x = displayWidth - floatView1.mWidth;
                    params1.y = displayHeight / 2;
                    windowManager.updateViewLayout(floatView1, params1);
                } else if (params2 != null) {
                    params2.x = displayWidth - floatView2.mWidth;
                    params2.y = displayHeight / 2;
                    windowManager.updateViewLayout(floatView2, params2);
                }
            }

            case 1: {
                if (paramsContent != null) {
                    paramsContent.x = displayHeight / 2;
                    paramsContent.y = displayWidth - floatContentView.mWidth;
                    // windowManager.updateViewLayout(floatContentView, paramsContent);
                } else if (params1 != null) {
                    params1.x = displayHeight / 2;
                    params1.y = displayWidth - floatView1.mWidth;
                    windowManager.updateViewLayout(floatView1, params1);
                } else if (params2 != null) {
                    params2.x = displayHeight / 2;
                    params2.y = displayWidth - floatView2.mHeight;
                    windowManager.updateViewLayout(floatView2, params2);
                }
            }
        }
    }
}