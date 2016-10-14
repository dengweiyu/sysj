package com.li.videoapplication.ui.popupwindows;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.li.videoapplication.R;
import com.li.videoapplication.framework.AppManager;
import com.li.videoapplication.utils.ScreenUtil;

/**
 * 弹框：视频分享推荐
 */
public class ApplyPopupWindow extends PopupWindow {

	protected final String action = this.getClass().getName();
	protected final String tag = this.getClass().getSimpleName();

    private View view, triangle, anchor;
	private LayoutInflater inflater;
    private Context context;
	private Activity activity;

	private int anchorWidth;
	private int anchorHeight;
	private int[] anchorLocation;
	private int anchorX;
	private int anchorY;

	private int w, h;
	private int triangleWidth;
	private int triangleHeight;

	private int x;
	private int y;

	protected int srceenWidth, srceenHeight;

	@SuppressWarnings("deprecation")
	public ApplyPopupWindow(Activity activity, View anchor) {
		this.activity = activity;
		this.anchor = anchor;

		context = AppManager.getInstance().getContext();
		WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		srceenWidth = windowManager.getDefaultDisplay().getWidth();
		srceenHeight = windowManager.getDefaultDisplay().getHeight();
		Log.i(tag, "srceenWidth=" + srceenWidth);
		Log.i(tag, "srceenHeight=" + srceenHeight);

		inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		view = inflater.inflate(R.layout.popup_apply, null);
		triangle = view.findViewById(R.id.apply_triangle);

        w = ScreenUtil.dp2px(260);
        h = ScreenUtil.dp2px(72);
		Log.i(tag, "w=" + w);
		Log.i(tag, "h=" + h);

		triangleWidth = ScreenUtil.dp2px(14);
		triangleHeight = ScreenUtil.dp2px(10);
		Log.i(tag, "triangleWidth=" + triangleWidth);
		Log.i(tag, "triangleHeight=" + triangleHeight);

		anchorLocation = new int[2];
		anchor.getLocationInWindow(anchorLocation);
		anchor.getLocationOnScreen(anchorLocation);
		anchorX = anchorLocation[0];
		anchorY = anchorLocation[1];
		Log.i(tag, "anchorX=" + anchorX);
		Log.i(tag, "anchorY=" + anchorY);

		anchorWidth = anchor.getWidth();
		anchorHeight = anchor.getHeight();
		Log.i(tag, "anchorWidth=" + anchorWidth);
		Log.i(tag, "anchorHeight=" + anchorHeight);

		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(triangleWidth, triangleHeight);
		params.gravity = Gravity.CENTER_HORIZONTAL;
		params.leftMargin = anchorX - srceenWidth/2 +  + anchorWidth/2;
		triangle.setLayoutParams(params);

		x = anchorX - (srceenWidth - w)/2;
		y = h + anchorHeight;
		Log.i(tag, "x=" + x);
		Log.i(tag, "y=" + y);

		this.setContentView(view);
		this.setWidth(w);
		this.setHeight(h);
		this.setFocusable(true);
		this.setOutsideTouchable(true);
		this.update();
		this.setBackgroundDrawable(new ColorDrawable(0000000000));
        this.setAnimationStyle(R.style.scaleBottomAnim);
		this.setOnDismissListener(new OnDismissListener() {
			@Override
			public void onDismiss() {
				setBackgroundAlpha(1f);
			}
		});
		this.setTouchInterceptor(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				setBackgroundAlpha(1f);
				return false;
			}
		});
	}

	/**
	 * 显示窗口
	 */
	public void showPopupWindow() {
		if (this.isShowing()) {
            this.dismiss();
		}
		setBackgroundAlpha(0.8f);
        this.showAsDropDown(anchor, -x, -y);
	}

	/**
	 * 设置添加屏幕的背景透明度
	 */
	public void setBackgroundAlpha(float alpha) {
		Window window = activity.getWindow();
		WindowManager.LayoutParams params = window.getAttributes();
		params.alpha = alpha;
		window.setAttributes(params);
	}
}
