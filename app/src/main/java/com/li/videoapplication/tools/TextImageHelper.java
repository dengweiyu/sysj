package com.li.videoapplication.tools;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.li.videoapplication.data.image.GlideHelper;
import com.li.videoapplication.framework.AppManager;
import com.li.videoapplication.utils.BitmapUtil;
import com.li.videoapplication.utils.StringUtil;
import com.li.videoapplication.views.CircleImageView;

import java.io.File;

/**
 * 功能：加载文本和图像
 */
public class TextImageHelper {

    private Handler handler = new Handler(Looper.getMainLooper());

    /**
     * 功能：延迟View的点击，选择等功能
     */
    public void setView(final View view, int delayMillis) {

        view.setEnabled(false);
        handler.postDelayed(new Runnable() {

            @Override
            public void run() {

                view.setEnabled(true);
            }
        }, delayMillis);
    }

    /**
     * 功能：TextView显示文字
     */

    public synchronized void setTextViewText(TextView view, String text) {
        if (view != null && !StringUtil.isNull(text)) {
            view.setText(StringUtil.convert(text));
            view.setVisibility(View.VISIBLE);
        } else {
            if (view != null) {
                view.setText("");
            }
        }
    }

    /**
     * 功能：TextView显示文字
     */
    public synchronized void setTextViewText(Context context, TextView view, int res) {
        if (res != 0) {
            String text = context.getResources().getString(res);
            setTextViewText(view, text);
        }
    }

    /**
     * 功能：TextView显示文字，是否隐藏和显示
     */
    public synchronized void setTextViewTextVisibility(TextView view, String text) {
        view.setText(StringUtil.convert(text));
        if (view != null && !StringUtil.isNull(text)) {
            view.setVisibility(View.VISIBLE);
        } else {
            if (view != null) {
                view.setText("");
                view.setVisibility(View.GONE);
            }
        }
    }

    /**
     * 功能：TextView显示文字，是否隐藏和显示
     */
    public synchronized void setTextViewTextVisibility(Context context, TextView view, int res) {
        if (res != 0) {
            String text = context.getResources().getString(res);
            setTextViewTextVisibility(view, text);
        }
    }

    /**
     * 功能：ImageView显示网络图像
     */
    public synchronized void setImageViewImageNet(ImageView view, String url) {
        if (view != null && !StringUtil.isNull(url)) {
            Context context = AppManager.getInstance().getContext();// FIXME: 不应该使用全局上下文，不然没办法即时 GC
            GlideHelper.displayImageWhite(context, url, view);
        } else {
            if (view != null) {
                view.setImageBitmap(null);
            }
        }
    }
    public synchronized void setImageViewImageNet(Context context, ImageView view, String url) {
        if (view != null && !StringUtil.isNull(url)) {
            GlideHelper.displayImageWhite(context, url, view);
        } else {
            if (view != null) {
                view.setImageBitmap(null);
            }
        }
    }

    /**
     * 功能：ImageView显示网络图像渐变展示
     */
    public synchronized void setImageViewNetAlpha(ImageView view, String url) {
        if (view != null && !StringUtil.isNull(url)) {
            GlideHelper.displayImageFade(url, view);
        } else {
            if (view != null) {
                view.setImageBitmap(null);
            }
        }
    }

    /**
     * 功能：ImageView显示网络图像渐变展示
     */
    public synchronized void setImageViewNetAlpha(Context context, ImageView view, String url) {
        if (view != null && !StringUtil.isNull(url)) {
            GlideHelper.displayImageFade(context, url, view);
        } else {
            if (view != null) {
                view.setImageBitmap(null);
            }
        }
    }

    /**
     * 功能：ImageView显示网络图像渐变展示
     */
    public synchronized void setImageViewNetAlpha(Fragment fragment, ImageView view, String url) {
        if (view != null && !StringUtil.isNull(url)) {
            GlideHelper.displayImageFade(fragment, url, view);
        } else {
            if (view != null) {
                view.setImageBitmap(null);
            }
        }
    }

    /**
     * 功能：ImageView显示网络图像渐变展示
     */
    public synchronized void setCircleImageNetAlpha(Context context, ImageView view, String url) {
        if (view != null && !StringUtil.isNull(url)) {
            GlideHelper.displayCircleImageFade(context, url, view);
        } else {
            if (view != null) {
                view.setImageBitmap(null);
            }
        }
    }

    /**
     * 功能：ImageView显示网络图像渐变展示
     */
    public synchronized void setCircleImageNetAlpha(Fragment fragment, ImageView view, String url) {
        if (view != null && !StringUtil.isNull(url)) {
            GlideHelper.displayCircleImageFade(fragment, url, view);
        } else {
            if (view != null) {
                view.setImageBitmap(null);
            }
        }
    }

    /**
     * 功能：CircleImageView显示网络图像渐变展示
     */
    public synchronized void setCircleImageViewNetAlpha(CircleImageView view, String url) {
        if (view != null && !StringUtil.isNull(url)) {
            GlideHelper.displayImageFade(url, view);
        } else {
            if (view != null) {
                view.setImageBitmap(null);
            }
        }
    }


        /**
         * 功能：ImageView显示资源图像
         */
    public synchronized void setImageViewImageRes(ImageView view, int res) {
        if (view != null && res != 0) {
            view.setImageResource(res);
        } else {
            if (view != null) {
                view.setImageBitmap(null);
            }
        }
    }

    /**
     * 功能：ImageView显示本地文件图像
     */
    public synchronized void setImageViewImageLocal(ImageView view, String path) {
        if (view != null && !StringUtil.isNull(path)) {
            try {
                File file = new File(path);
                Bitmap bitmap = BitmapUtil.readBitmap(AppManager.getInstance().getApplication(), file);
                view.setImageBitmap(bitmap);
            } catch (Exception e) {
                e.printStackTrace();
                view.setImageBitmap(null);
            }
        } else {
            if (view != null) {
                view.setImageBitmap(null);
            }
        }
    }

}
