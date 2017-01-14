
package com.li.videoapplication.framework;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.view.Display;
import android.view.Window;
import android.view.WindowManager;

import com.li.videoapplication.R;

/**
 * 基本弹框
 */
@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH) 
public abstract class BaseEmptyRightDialog extends Dialog {

    protected final String action = this.getClass().getName();

    protected final String tag = this.getClass().getSimpleName();

	public BaseEmptyRightDialog(Context context) {
		super(context, R.style.AppTranslucentDialog);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		Window window = getWindow();
		window.setWindowAnimations(R.style.slideRightAnim); // 设置窗口弹出动画
		window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
		setCanceledOnTouchOutside(true);

		setContentView(getContentView());

		WindowManager manager = ((Activity) context).getWindowManager();
		Display display = manager.getDefaultDisplay();
		WindowManager.LayoutParams params = window.getAttributes();
		params.width = (display.getWidth()); // 设置宽度

		window.setAttributes(params);
	
	}
	
	protected abstract int getContentView();
}
