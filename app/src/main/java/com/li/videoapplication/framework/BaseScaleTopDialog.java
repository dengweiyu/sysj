package com.li.videoapplication.framework;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.view.Window;
import android.view.WindowManager;

import com.li.videoapplication.R;

/**
 * 基本弹框：中间弹出，渐变，背景灰色透明
 */
@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
public abstract class BaseScaleTopDialog extends BaseDialog {

	public BaseScaleTopDialog(Context context) {
		super(context);
	}

	@Override
	protected void afterContentView(Context context) {
		super.afterContentView(context);

		Window window = getWindow();
		window.setWindowAnimations(R.style.scaleTopAnim); // 设置窗口弹出动画
		window.setBackgroundDrawableResource(android.R.color.transparent); // 设置对话框背景为透明
		WindowManager.LayoutParams params = window.getAttributes();
		// 根据x，y坐标设置窗口需要显示的位置
		params.x = 0; // x小于0左移，大于0右移
		params.y = 0; // y小于0上移，大于0下移
		window.setAttributes(params);
	}
}
