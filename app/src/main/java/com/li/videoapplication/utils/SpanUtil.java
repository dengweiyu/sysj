package com.li.videoapplication.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.Editable;
import android.text.Selection;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.text.style.MetricAffectingSpan;
import android.util.Log;
import android.widget.EditText;

import com.li.videoapplication.framework.AppManager;


public class SpanUtil {

    public static final String TAG = SpanUtil.class.getSimpleName();

    // -------------------------------------------------------------------------------

    public static Bitmap createBitmap(Context context, String s) {
        int h = ScreenUtil.dp2px(12);
        Paint p = new Paint();
        p.setTextSize(h);
        int w = (int) p.measureText(s);
        Log.d(TAG, "createBitmap: h=" + h);
        Log.d(TAG, "createBitmap: w=" + w);
        Bitmap bitmap = Bitmap.createBitmap(w + 12, h + 12, Bitmap.Config.ARGB_4444);

        Canvas canvas = new Canvas(bitmap);

        Paint paint = new Paint();
        paint.setColor(Color.parseColor("#40a8fe"));
        paint.setTextSize(h);
        canvas.drawText(s, 6, h + 6, paint);

        canvas.save(Canvas.ALL_SAVE_FLAG);
        canvas.restore();
        return bitmap;
    }

    public static MetricAffectingSpan insertText(EditText editText, String s) {
        synchronized (SpanUtil.class) {
            Context context = AppManager.getInstance().getContext();
            Bitmap bitmap = createBitmap(context, s);
            if (bitmap != null) {
                ImageSpan imageSpan = new ImageSpan(context, bitmap);
                // TextAppearanceSpan textAppearanceSpan = new TextAppearanceSpan("monospace", Typeface.BOLD_ITALIC, ScreenUtil.dp2px(12), ColorStateList.valueOf(Color.parseColor("#40a8fe")), null);
                SpannableString spannableString = new SpannableString(s);
                spannableString.setSpan(imageSpan, 0, s.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                int index = editText.getSelectionStart();
                Editable editable = editText.getEditableText();
                // Editable editable = textView.getEditableText();
                if (index < 0 || index >= editable.length()) {
                    editable.append(spannableString);
                } else {
                    editable.insert(index, spannableString);
                }
                return imageSpan;
            }
            return null;
        }
    }

    public static int[] removeSpan(EditText editText, MetricAffectingSpan span) {
        Log.d(TAG, "removeSpan: // ------------------------------------------------------------------");
        synchronized (SpanUtil.class) {
            if (span != null) {
                synchronized (TAG) {
                    Editable editable = editText.getEditableText();
                    int start = editable.getSpanStart(span);
                    int end = editable.getSpanEnd(span);
                    Log.d(TAG, "removeSpan: start=" + start);
                    Log.d(TAG, "removeSpan: end=" + end);
                    try {
                        editable.removeSpan(span);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    try {
                        editable.delete(start, end);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    editText.invalidate();
                    Log.d(TAG, "removeSpan: true");
                    return new int[] { start, end };
                }
            }
            return null;
        }
    }

    public static void clearSpans(EditText description) {
        Log.d(TAG, "clearSpans: // ------------------------------------------------------------------");
        synchronized (SpanUtil.class) {
            ImageSpan[] spans = null;
            try {
                spans = description.getText().getSpans(0, description.getText().length(), ImageSpan.class);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (spans != null) {
                for (ImageSpan span : spans) {
                    try {
                        description.getText().delete(
                                description.getText().getSpanStart(span),
                                description.getText().getSpanEnd(span));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public static String getText(EditText description) {
        String s = "";
        try {
            s = description.getText().toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Editable editable = description.getText();
        ImageSpan[] spans = editable.getSpans(0, editable.length(), ImageSpan.class);
        if (spans != null && spans.length > 0) {
            String[] strings = new String[spans.length];
            int i = 0;
            for (ImageSpan span :
                    spans) {
                try {
                    strings[i] = s.substring(editable.getSpanStart(span),
                            editable.getSpanEnd(span));
                    Log.d(TAG, "getText: strings[i]=" + strings[i]);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                 ++ i;
            }
            Log.d(TAG, "getText: s=" + s);
            for (String rep :
                    strings) {
                Log.d(TAG, "getText: rep=" + rep);
                s = s.replace(rep, "");
            }

        }
        Log.d(TAG, "getText: s=" + s);
        return s;
    }

    public static void lastSelection(EditText editText) {
        try {
            Selection.setSelection(editText.getText(), editText.getText().length());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
