package com.li.videoapplication.ui.popupwindows;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.li.videoapplication.R;
import com.li.videoapplication.data.model.entity.VideoImage;
import com.li.videoapplication.data.preferences.PreferencesHepler;
import com.li.videoapplication.framework.AppManager;
import com.li.videoapplication.ui.ActivityManeger;
import com.li.videoapplication.ui.activity.VideoPlayActivity;
import com.li.videoapplication.ui.toast.ToastHelper;
import com.li.videoapplication.utils.ScreenUtil;

/**
 * 弹框：举报
 */
public class ReportPopupWindow extends PopupWindow implements OnClickListener, PopupWindow.OnDismissListener {

	protected final String action = this.getClass().getName();
	protected final String tag = this.getClass().getSimpleName();

    private View view;
    private TextView submit;
	private LayoutInflater inflater;
    private VideoPlayActivity activity;

    private VideoImage videoImage;
	private View container;
	private double w,h;

	@SuppressWarnings("deprecation")
	public ReportPopupWindow(final VideoPlayActivity activity, final VideoImage videoImage) {
        this.videoImage = videoImage;
        this.activity = activity;

		Context context = AppManager.getInstance().getContext();
		inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		view = inflater.inflate(R.layout.popup_report, null);
		container = view.findViewById(R.id.container);
		submit = (TextView) view.findViewById(R.id.report_submit);
		submit.setOnClickListener(this);

		w = ScreenUtil.dp2px(46);
		h = ScreenUtil.dp2px(24);

		if(activity.isLandscape()) {
			w = ScreenUtil.dp2px((int)((float) 46 * 1.2));
			h = ScreenUtil.dp2px((int)((float) 24 * 1.2));
		}
		Log.i(tag, "w=" + w);
		Log.i(tag, "h=" + h);

		this.setContentView(view);
		this.setWidth((int) w);
		this.setHeight((int) h);
		this.setFocusable(true);
		this.setOutsideTouchable(true);
		// 刷新状态
		this.update();
		ColorDrawable colorDrawable = new ColorDrawable(0000000000);
		this.setBackgroundDrawable(colorDrawable);
		this.setOnDismissListener(this);
        this.setAnimationStyle(R.style.scaleRightRAnim);
	}

	/**
	 * 显示窗口
	 */
	public void showPopupWindow(View anchor) {
		if (this.isShowing()) {
            this.dismiss();
		}
		int anchorWidth = anchor.getWidth();
		int anchorHeight = anchor.getHeight();
		Log.i(tag, "anchorWidth=" + anchorWidth);
		Log.i(tag, "anchorHeight=" + anchorHeight);
        int x = -((int) w + anchorWidth) / 2 - 35;
        int y = -((int) h + anchorHeight) / 2;
        this.showAsDropDown(anchor, x, y);
	}

	@Override
	public void onDismiss() {

	}

	@Override
	public void onClick(View v) {
		if (v == submit) {
            if (!PreferencesHepler.getInstance().isLogin()) {
                ToastHelper.s("请先登录");
                return;
            }
            ActivityManeger.startReportActivity(activity, videoImage);
			this.dismiss();
		}
	}
}
