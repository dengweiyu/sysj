package com.li.videoapplication.tools;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.li.videoapplication.data.image.ImageLoaderHelper;
import com.li.videoapplication.framework.AppManager;
import com.li.videoapplication.utils.BitmapUtil;
import com.li.videoapplication.utils.StringUtil;

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
            ImageLoaderHelper.displayImageWhite(url, view);
        } else {
            if (view != null) {
                view.setImageBitmap(null);
            }
        }
    }

    /**
     * 功能：ImageView显示网络图像渐变展示
     */
    public synchronized void setImageViewImageNetAlpha(ImageView view, String url) {
        if (view != null && !StringUtil.isNull(url)) {
            ImageLoaderHelper.displayImageWithAlpha(url, view);
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
            // 内存溢出
            // PicassoHelper.load(view, res);
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
            // 内存溢出
            // view.setImageBitmap(BitmapFactory.decodeFile(path));
            // 显示不全
            // PicassoHelper.load(view, path);
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
