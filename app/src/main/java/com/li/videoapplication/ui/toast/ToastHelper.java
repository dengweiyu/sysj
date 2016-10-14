package com.li.videoapplication.ui.toast;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources.NotFoundException;
import android.os.Handler;
import android.os.Looper;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.li.videoapplication.R;
import com.li.videoapplication.framework.AppManager;
import com.li.videoapplication.utils.ScreenUtil;
import com.li.videoapplication.utils.StringUtil;

/**
 * 功能：吐司
 */
@SuppressLint("InflateParams")
public class ToastHelper {

    protected final String action = this.getClass().getName();

    protected final String tag = this.getClass().getSimpleName();

    private static ToastHelper instance;

    public static ToastHelper getInstance() {
        if (instance == null) {
            synchronized (ToastHelper.class) {
                if (instance == null) {
                    instance = new ToastHelper();
                }
            }
        }
        return instance;
    }

	private Context context;
	private LayoutInflater inflater;

    private ToastHelper() {
        context = AppManager.getInstance().getContext();
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        context = AppManager.getInstance().getContext();
    }

	private static Handler h = new Handler(Looper.getMainLooper());

	private Toast toast;

	/**
	 * 系统
	 */
	@SuppressWarnings("unused")
	private void a(final String text, final int duration, final int gravity, final int xOffset, final int yOffset) {
		if (StringUtil.isNull(text.toString())) {
			return;
		}
		h.post(new Runnable() {
            @Override
            public void run() {
                synchronized (this) {
                    if (toast != null) {
                        toast.setText(text);
                        toast.setDuration(duration);
                    } else {
                        toast = Toast.makeText(context.getApplicationContext(), text.toString().trim(), duration);
                    }
                    toast.show();
                }
            }
        });
	}

    /**
     * 自定义
     */
    private void b(final int res, final int duration, final int gravity, final int xOffset, final int yOffset) {
        if (res == 0)
            return;
        String text = null;
        try {
            text = context.getResources().getString(res);
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        if (text == null)
            return;
        b(text, duration, gravity, xOffset, yOffset);
    }

    /**
     * 自定义
     */
    private void b(final String text, final int duration, final int gravity, final int xOffset, final int yOffset) {
        if (text == null)
            return;
        if (StringUtil.isNull(text.toString())) {
            return;
        }
        h.post(new Runnable() {
            @Override
            public void run() {
                synchronized (this) {
                    if (toast != null) {
                        setView(toast, text, duration, gravity, xOffset, yOffset);
                    } else {
                        toast = getToast(text, duration, gravity, xOffset, yOffset);
                    }
                    toast.show();
                }
            }
        });
    }

	private Toast getToast(final String text, final int duration, final int gravity, final int xOffset, final int yOffset) {
		Toast toast = new Toast(context);
		setView(toast, text, duration, gravity, xOffset, yOffset);
		return toast;
	}

	private void setView(final Toast toast, final String text, final int duration, final int gravity, final int xOffset, final int yOffset) {
		View view = inflater.inflate(R.layout.view_toast, null);
		TextView textView = (TextView) view.findViewById(R.id.toast_content);
		textView.setText(text);
		toast.setGravity(gravity, xOffset, yOffset);
		toast.setDuration(duration);
		toast.setView(view);
	}

    public static void s(final int res) {
        Context context = AppManager.getInstance().getContext();
        getInstance().b(res, Toast.LENGTH_SHORT, Gravity.BOTTOM, 0, ScreenUtil.dp2px(80));
    }

    public static void s(final String text) {
        Context context = AppManager.getInstance().getContext();
        getInstance().b(text, Toast.LENGTH_SHORT, Gravity.BOTTOM, 0, ScreenUtil.dp2px(80));
    }

    public static void l(final int res) {
        Context context = AppManager.getInstance().getContext();
        getInstance().b(res, Toast.LENGTH_LONG, Gravity.BOTTOM, 0, ScreenUtil.dp2px(80));
    }

    public static void l(final String text) {
        Context context = AppManager.getInstance().getContext();
        getInstance().b(text, Toast.LENGTH_LONG, Gravity.BOTTOM, 0, ScreenUtil.dp2px(80));
    }

    public static void x(final String text, final int duration, final int gravity, final int xOffset, final int yOffset) {
        getInstance().b(text, duration, gravity, xOffset, yOffset);
    }
}
