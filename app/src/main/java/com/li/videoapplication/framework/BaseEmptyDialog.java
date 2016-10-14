
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
public abstract class BaseEmptyDialog extends Dialog {

    protected final String action = this.getClass().getName();

    protected final String tag = this.getClass().getSimpleName();

	public BaseEmptyDialog(Context context) {
		super(context, R.style.AppTranslucentDialog);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		Window window = getWindow();
		window.setWindowAnimations(R.style.emptyAnim); // 设置窗口弹出动画
		window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
		setCanceledOnTouchOutside(true);
		
		setContentView(getContentView());
		
		WindowManager manager = ((Activity) context).getWindowManager();
		Display display = manager.getDefaultDisplay();
		WindowManager.LayoutParams params = window.getAttributes();
		params.width = (int) (display.getWidth()); // 设置宽度

		// 根据x，y坐标设置窗口需要显示的位置
//		params.x = 0; // x小于0左移，大于0右移
//		params.y = 0; // y小于0上移，大于0下移
//		params.alpha = 0.0f; //设置透明度
//		params.gravity = Gravity.BOTTOM; // 设置重力
		
		window.setAttributes(params);
	
	}
	
	protected abstract int getContentView();
}
