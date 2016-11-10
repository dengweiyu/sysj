package com.li.videoapplication.ui.popupwindows;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.ifeimo.screenrecordlib.RecordingManager;
import com.li.videoapplication.R;
import com.li.videoapplication.framework.AppManager;
import com.li.videoapplication.ui.ActivityManeger;
import com.li.videoapplication.ui.activity.MainActivity;
import com.li.videoapplication.ui.toast.ToastHelper;
import com.li.videoapplication.utils.ScreenUtil;

/**
 * 弹框：视频录制
 */
public class RecordPopupWindow extends PopupWindow implements OnClickListener, PopupWindow.OnDismissListener {

	protected final String action = this.getClass().getName();
	protected final String tag = this.getClass().getSimpleName();

    private View view;
    private LinearLayout video, record;
	private LayoutInflater inflater;
	private MainActivity activity;
	private Context context;

	private double w,h;
	private double screenWidth, screenHeight;

	@SuppressWarnings("deprecation")
	public RecordPopupWindow() {
		activity = AppManager.getInstance().getMainActivity();
		context = AppManager.getInstance().getContext();
		inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		view = inflater.inflate(R.layout.popup_record, null);
		video = (LinearLayout) view.findViewById(R.id.record_video);
		record = (LinearLayout) view.findViewById(R.id.record_record);
		video.setOnClickListener(this);
		record.setOnClickListener(this);

		w = ScreenUtil.dp2px(110);
		h = ScreenUtil.dp2px(88);
		screenWidth = ScreenUtil.getScreenWidth();
		screenHeight = ScreenUtil.getScreenHeight();
		Log.i(tag, "w=" + w);
		Log.i(tag, "h=" + h);
		Log.i(tag, "screenWidth=" + screenWidth);
		Log.i(tag, "screenHeight=" + screenHeight);

		this.setContentView(view);
		this.setWidth((int) w);
		this.setHeight((int) h);
		this.setFocusable(true);
		this.setOutsideTouchable(true);
		// 刷新状态
		this.update();
		ColorDrawable colorDrawable = new ColorDrawable(0x00000000);
		this.setBackgroundDrawable(colorDrawable);
		this.setOnDismissListener(this);
        this.setAnimationStyle(R.style.scaleTopAnim);
	}

	/**
	 * 显示窗口
	 */
	public void showPopupWindow(View anchor) {
		if (this.isShowing()) {
            this.dismiss();
		}
		// 48 * 38
		// 110 * 88
		int anchorWidth = anchor.getWidth();
		int anchorHeight = anchor.getHeight();
		int y = 0;
		int x = (int) w - anchorWidth/2 - ScreenUtil.dp2px(12 + 13/2);
		Log.i(tag, "x=" + x);
		Log.i(tag, "y=" + y);
        this.showAsDropDown(anchor, -x, 0);
	}

	@Override
	public void onDismiss() {

	}

	@Override
	public void onClick(View v) {

		if (v == video) {
			if (activity != null)
				activity.startScreenRecordActivity();
		} else if (v == record) {
			if (activity == null)
				return;
			if (RecordingManager.getInstance().isRecording()) {
				ToastHelper.s("正在录屏中");
				return;
			}
			ActivityManeger.startCameraRecoedActivity(activity);
		}
		this.dismiss();
	}
}
