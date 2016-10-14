package com.li.videoapplication.ui.activity;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.li.videoapplication.R;
import com.li.videoapplication.data.network.RequestExecutor;
import com.li.videoapplication.framework.BaseActivity;
import com.li.videoapplication.utils.StringUtil;
import com.li.videoapplication.views.ZoomableImageView;
/**
 * 活动：显示图片
 */
@SuppressLint("HandlerLeak")
public class ImageActivity extends BaseActivity {

    /**
     * 加载网页图片
     */
    public synchronized final static void startImageActivityWeb(Context context, String url){
        ImageActivity.startImageActivity(context, url, 0);
    }

    /**
     * 加载本地图片
     */
    public synchronized final static void startImageActivityLocal(Context context, String url){
        ImageActivity.startImageActivity(context, url, 1);
    }

    /**
     * 加载图片
     *
     * @param picType
     *               0   网页图片
     *               其他   本地图片
     */
    private synchronized final static void startImageActivity(Context context, String url, int picType){
        if (context == null) {
            return;
        }
        if (StringUtil.isNull(url)) {
            return;
        }
        Intent intent = new Intent();
        intent.putExtra("url", url);
        intent.putExtra("picType", picType);
        intent.setClass(context, ImageActivity.class);
        context.startActivity(intent);
        ((Activity) context).overridePendingTransition(android.R.anim.fade_in, R.anim.activity_hold);
    }

	private static final String TAG = ImageActivity.class.getSimpleName();
	protected static Handler mHandler = new Handler(Looper.getMainLooper());
	private String url;
	private int picType;
	private ZoomableImageView zoomable;
	private ImageView image;
	private ArrayList<View> list = new ArrayList<>();

	@Override
	public void refreshIntent() {
		super.refreshIntent();

		try {
			url = getIntent().getStringExtra("url");
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			picType = getIntent().getIntExtra("picType", 1);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void beforeOnCreate() {
		super.beforeOnCreate();
		requestWindowFeature(Window.FEATURE_NO_TITLE);
	}

	@Override
	public void afterOnCreate() {
		super.afterOnCreate();
		setContentView(R.layout.swi_image);
	}

	@Override
	public void initView() {
		super.initView();

		zoomable = (ZoomableImageView) findViewById(R.id.swi_zoomble);
		final View loadingView = findViewById(R.id.swi_loading);
		View failedView = findViewById(R.id.swi_failed);

		failedView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				loadData();
			}
		});

		list.add(loadingView);
		list.add(zoomable);
		list.add(failedView);

		image = (ImageView) findViewById(R.id.swi_loading_progress);
		AnimationDrawable anim = (AnimationDrawable) image.getDrawable();
		anim.start();
	}

	@Override
	public void loadData() {
		super.loadData();

		swicthContentView(0);//刷新中
		if(TextUtils.isEmpty(url)){//加载失败
			swicthContentView(2);
		} else {
			if (picType == 0) {//网页图片
				loadWebImage();
			} else {//本地图片
				loadLocalImage();
			}
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (image != null) {
			image.clearAnimation();
		}
	}
	
	/**
	 * 选择页面
	 * @param page
	 *            0      图片加载中
	 *            1      图片加载成功
	 *            2      图片加载失败
	 */
	private void swicthContentView(int page) {
		if (list == null || list.isEmpty() || list.size() != 3) {
			return;
		}
		for (int i = 0; i < list.size(); i++) {
			if (page == i) {
				list.get(i).setVisibility(View.VISIBLE);
			} else {
				list.get(i).setVisibility(View.GONE);
			}
		}
	}
	
	private void loadLocalImage() {
		zoomable.setImageBitmap(BitmapFactory.decodeFile(url));
		swicthContentView(1);
	}
	
	private void loadWebImage() {
		
		final Handler handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				Bitmap bitmap = (Bitmap) msg.obj;
				if (bitmap == null) {
					swicthContentView(2);
				} else {
					zoomable.setImageBitmap(bitmap);
					mHandler.postDelayed(new Runnable() {//延迟加载
						
						@Override
						public void run() {
							swicthContentView(1);
						}
					}, 500);
				}
			}
		};
		
		final Runnable runnable = new Runnable() {
			public void run() {
				Bitmap bitmap = null;
				try {
					Drawable drawable = loadImageFromUrl(url);
					BitmapDrawable bd = (BitmapDrawable) drawable;
					bitmap = bd.getBitmap();
				} catch (Exception e) {
					e.printStackTrace();
				}
				handler.obtainMessage(0, bitmap).sendToTarget();
			}
		};
		RequestExecutor.execute(runnable);
	}
	
	@SuppressWarnings("unused")
	private static Bitmap loadImageFromRes(Activity activity, int res) throws IOException {
		Bitmap bitmap = BitmapFactory.decodeResource(activity.getResources(), res);
		return bitmap;
	}
	
	private static Drawable loadImageFromUrl(String url) throws IOException {
		Drawable drawable = null;
		URL mURL = new URL(url);
		InputStream inputStream = (InputStream) mURL.getContent();
		drawable = Drawable.createFromStream(inputStream, "src");
		return drawable;
	}
	
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			finish();
		    overridePendingTransition(R.anim.activity_hold, android.R.anim.fade_out);
		}
		return false;
	}
}
