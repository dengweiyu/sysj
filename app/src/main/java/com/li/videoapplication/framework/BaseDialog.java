
package com.li.videoapplication.framework;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.view.Display;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.li.videoapplication.tools.TextImageHelper;
import com.li.videoapplication.tools.ToastHelper;
/**
 * 基本弹框
 */
@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH) 
public abstract class BaseDialog extends Dialog {

    protected final String action = this.getClass().getName();

    protected final String tag = this.getClass().getSimpleName();
    
    private TextImageHelper textImageHelper = new TextImageHelper();
	protected Context mContext;

	public BaseDialog(Context context) {
		this(context, 0);
	}
	
	public BaseDialog(Context context, int theme) {
		super(context, theme);
		this.mContext = context;
		initContentView(context);
	}

	private void initContentView(Context context) {
		
		beforeContentView(context);
		setContentView(getContentView());
		afterContentView(context);
	}
	
	protected void beforeContentView(Context context) {
		Log.d(tag, "beforeContentView: ");
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		Window window = getWindow();
		window.setBackgroundDrawableResource(android.R.color.transparent);
		setCanceledOnTouchOutside(true);
	};
	
	@SuppressWarnings("deprecation")
	protected void afterContentView(Context context) {
		Log.d(tag, "afterContentView: ");
		Window window = getWindow();
		WindowManager manager = ((Activity) context).getWindowManager();
		Display display = manager.getDefaultDisplay();
		WindowManager.LayoutParams params = window.getAttributes();
		params.width = (int) (display.getWidth()); // 设置宽度
		window.setAttributes(params);
	}; 
	protected abstract int getContentView();
	
	/*##############  加载文本和图像   ##############*/
	
	protected void setTextViewText(TextView mTextView, String text) {
		textImageHelper.setTextViewText(mTextView, text);
	}
	
	protected void setTextViewText(TextView mTextView, int res) {
		textImageHelper.setTextViewText(getContext(), mTextView, res);
	}
	
	protected void setTextViewTextVisibility(TextView mTextView, String text) {
		textImageHelper.setTextViewTextVisibility(mTextView, text);
	}
	
	protected void setTextViewTextVisibility(TextView mTextView, int res) {
		textImageHelper.setTextViewTextVisibility(getContext(), mTextView, res);
	}
	
	protected void setImageViewImageRes(ImageView mImageView, int res) {
		textImageHelper.setImageViewImageRes(mImageView, res);
	}
	
	protected void setImageViewImageLocal(ImageView mImageView, String path) {
		textImageHelper.setImageViewImageLocal(mImageView, path);
	}

	protected void setImageViewImageNet(ImageView mImageView, String url) {
		textImageHelper.setImageViewImageNet(mImageView, url);
	}
	
	/*##############  提示   ##############*/
	
	protected void showToastShort(final String text) {
		ToastHelper.s(text);
	}
	
	protected void showToastShort(final int res) {
		ToastHelper.s(res);
	}
	
	protected void showToastLong(final String text) {
		ToastHelper.l(text);
	}
	
	protected void showToastLong(final int res) {
		ToastHelper.l(res);
	}
}
