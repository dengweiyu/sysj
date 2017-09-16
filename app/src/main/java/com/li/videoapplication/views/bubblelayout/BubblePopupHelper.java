package com.li.videoapplication.views.bubblelayout;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.PopupWindow;

import com.li.videoapplication.R;

/**
 * Created by sudamasayuki on 16/05/02.
 */
public class BubblePopupHelper {

    public static PopupWindow create(@NonNull final Activity activity, @NonNull BubbleLayout bubbleLayout) {

        final PopupWindow popupWindow = new PopupWindow(activity);

        popupWindow.setContentView(bubbleLayout);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setAnimationStyle(android.R.style.Animation_Dialog);
        // change background color to transparent
        Drawable drawable;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            drawable = activity.getDrawable(R.drawable.popup_window_transparent);
        } else {
            drawable = activity.getResources().getDrawable(R.drawable.popup_window_transparent);
        }
        popupWindow.setBackgroundDrawable(drawable);

        setBackgroundAlpha(activity, 0.5f);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                setBackgroundAlpha(activity, 1f);
            }
        });

        return popupWindow;
    }

    /**
     * 设置添加屏幕的背景透明度
     */
    private static void setBackgroundAlpha(Activity activity, float alpha) {
        Window window = activity.getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        params.alpha = alpha;
        window.setAttributes(params);
    }

}
