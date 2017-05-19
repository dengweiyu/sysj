package master.flame.danmaku.ui.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.text.style.ImageSpan;

/**
 * 图片居中样式
 */

public class CenterImageSpan extends ImageSpan {

    public CenterImageSpan(Bitmap b) {
        super(b);
    }

    public CenterImageSpan(Bitmap b, int verticalAlignment) {
        super(b, verticalAlignment);
    }

    public CenterImageSpan(Context context, Bitmap b) {
        super(context, b);
    }

    public CenterImageSpan(Context context, Bitmap b, int verticalAlignment) {
        super(context, b, verticalAlignment);
    }

    public CenterImageSpan(Drawable d) {
        super(d);
    }

    public CenterImageSpan(Drawable d, int verticalAlignment) {
        super(d, verticalAlignment);
    }

    public CenterImageSpan(Drawable d, String source) {
        super(d, source);
    }

    public CenterImageSpan(Drawable d, String source, int verticalAlignment) {
        super(d, source, verticalAlignment);
    }

    public CenterImageSpan(Context context, Uri uri) {
        super(context, uri);
    }

    public CenterImageSpan(Context context, Uri uri, int verticalAlignment) {
        super(context, uri, verticalAlignment);
    }

    public CenterImageSpan(Context context, int resourceId) {
        super(context, resourceId);
    }

    public CenterImageSpan(Context context, int resourceId, int verticalAlignment) {
        super(context, resourceId, verticalAlignment);
    }

    @Override
    public int getSize(Paint paint, CharSequence text, int start, int end, Paint.FontMetricsInt fm) {
        Drawable d = getDrawable();
        Rect rect = d.getBounds();

        Paint.FontMetricsInt fmPaint=paint.getFontMetricsInt();
        //获得文字、图片高度
        int fontHeight = fmPaint.bottom - fmPaint.top;
        int drHeight=rect.bottom-rect.top;

        int top= drHeight/2 - fontHeight/2;
        int bottom=drHeight/2 + fontHeight/2;

        if (fm != null){
            fm.ascent=-bottom;
            fm.top=-bottom;
            fm.bottom=top;
            fm.descent=top;
        }
        return rect.right;
    }

    @Override
    public void draw(Canvas canvas, CharSequence text, int start, int end, float x, int top, int y, int bottom, Paint paint) {
        Drawable b = getDrawable();
        canvas.save();

        int transY = ((bottom-top) - b.getBounds().bottom)/2+top;
        if (mVerticalAlignment == ALIGN_BASELINE) {
            transY -= paint.getFontMetricsInt().descent;
        }

        canvas.translate(x, transY);
        b.draw(canvas);
        canvas.restore();
    }
}
