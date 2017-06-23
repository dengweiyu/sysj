package com.li.videoapplication.ui.view;

import android.graphics.Canvas;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;

import java.lang.ref.SoftReference;

import master.flame.danmaku.danmaku.model.BaseDanmaku;

/**
 * 设置占位宽度、高度
 */

public class SpannedCacheStuffer extends master.flame.danmaku.danmaku.model.android.SpannedCacheStuffer {

    //为了保证文本或者表情 与边框的top bottom left right的距离相等 需要增加占位宽度和高度来补偿
    private int padding = 0;

    public void setPadding(int padding){
        this.padding = padding;
    }

    @Override
    public void drawText(BaseDanmaku danmaku, String lineText, Canvas canvas, float left, float top, TextPaint paint, boolean fromWorkerThread) {
        if (danmaku.obj == null) {
            super.drawText(danmaku, lineText, canvas, left, top, paint, fromWorkerThread);
            return;
        }
        SoftReference<StaticLayout> reference = (SoftReference<StaticLayout>) danmaku.obj;
        StaticLayout staticLayout = reference.get();
        boolean requestRemeasure = 0 != (danmaku.requestFlags & BaseDanmaku.FLAG_REQUEST_REMEASURE);
        boolean requestInvalidate = 0 != (danmaku.requestFlags & BaseDanmaku.FLAG_REQUEST_INVALIDATE);

        if (requestInvalidate || staticLayout == null) {
            if (requestInvalidate) {
                danmaku.requestFlags &= ~BaseDanmaku.FLAG_REQUEST_INVALIDATE;
            } else if (mProxy != null) {
                mProxy.prepareDrawing(danmaku, fromWorkerThread);
            }
            CharSequence text = danmaku.text;
            if (text != null) {
                if (requestRemeasure) {
                    staticLayout = new StaticLayout(text, paint, (int) StaticLayout.getDesiredWidth(danmaku.text, paint), Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, true);
                    danmaku.paintWidth = staticLayout.getWidth()+padding;           //补偿内边距
                    danmaku.paintHeight = staticLayout.getHeight()+padding;
                    danmaku.requestFlags &= ~BaseDanmaku.FLAG_REQUEST_REMEASURE;
                } else {
                    staticLayout = new StaticLayout(text, paint, (int) danmaku.paintWidth, Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, true);
                }
                danmaku.obj = new SoftReference<>(staticLayout);
            } else {
                return;
            }
        }
        boolean needRestore = false;
        if (left != 0 && top != 0) {
            canvas.save();
            canvas.translate(left, top + paint.ascent());
            needRestore = true;
        }
        staticLayout.draw(canvas);
        if (needRestore) {
            canvas.restore();
        }
    }
}
