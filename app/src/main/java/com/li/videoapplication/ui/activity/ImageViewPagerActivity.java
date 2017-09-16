package com.li.videoapplication.ui.activity;

import java.util.ArrayList;
import java.util.List;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.li.videoapplication.R;
import com.li.videoapplication.data.image.GlideHelper;
import com.li.videoapplication.framework.TBaseActivity;

/**
 * 活动：图片浏览
 */
public class ImageViewPagerActivity extends TBaseActivity implements ViewPager.OnPageChangeListener {

	public static final int REQUEST_CODE = 11;
	public static final  String SHARED_TRANSITION = "shared_transition";
	private static final String TAG = ImageViewPagerActivity.class.getSimpleName();

	/**
	 * 图片浏览
	 */
	public synchronized final static void startImageViewPagerActivity(Context context, View source,int position, String[] urls) {
		if (context == null) {
			return;
		}

		if (urls == null || urls.length == 0) {
			return;
		}

		Activity activity = null;
		if(context instanceof Activity){
			activity = (Activity)context;
		}

		Intent intent = new Intent();
		intent.putExtra("checkPosition", position);
		intent.putExtra("urls", urls);
		intent.setClass(context, ImageViewPagerActivity.class);
		context.startActivity(intent);
		((Activity) context).overridePendingTransition(android.R.anim.fade_in , R.anim.activity_hold);
	}

	@Override
	public void refreshIntent() {
		super.refreshIntent();

		Intent intent = getIntent();
		if (intent.hasExtra("checkPosition")) {
			position = intent.getIntExtra("checkPosition", 0);
			urls = intent.getStringArrayExtra("urls");
		}
		if (urls == null || urls.length == 0) {
			finish();
		}
		if (position >= urls.length) {
			position = 0;
		}
	}

	@Override
	public int getContentView() {
		return R.layout.swi_viewpager;
	}


	private int position = 0;
	private String[] urls;
	
	private static final Drawable TRANSPARENT = new ColorDrawable(Color.TRANSPARENT);

	@Override
	public void beforeOnCreate() {
		super.beforeOnCreate();
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setSystemBar(false);
		getWindow().setBackgroundDrawable(TRANSPARENT);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
	}

	@Override
	public void initView() {
		super.initView();

		initGoBack();
		initViewPager();
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			finish();
		}
		return false;
	}
	
	@Override
	public void finish() {
		super.finish();
		overridePendingTransition(R.anim.activity_hold , android.R.anim.fade_out);
	}

	/**
	 * 返回图标
	 */
	private ImageView goback;

	private void initGoBack() {
		goback = (ImageView) findViewById(R.id.swi_viewpager_goback);
		goback.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}
	
	private ViewPager viewPager;
	private List<ImageView> zoomables;
	private List<ImageView> points;
	private ImageView point;
	private PageAdapter adapter;
	private LinearLayout container;
	
//	private static MyBitmapUtils myBitmapUtils = new MyBitmapUtils();

	private void initViewPager() {
		
		viewPager = (ViewPager) findViewById(R.id.swi_viewpager_viewpager);


		zoomables = new ArrayList<ImageView>();
		for (int i = 0; i < urls.length; i++) {
			final ImageView zoom = (ImageView) View.inflate(this, R.layout.swi_zoomable, null);
			GlideHelper.displayImage(ImageViewPagerActivity.this,urls[i],zoom);
			zoom.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					finish();
				}
			});

			ViewPager.LayoutParams params = new ViewPager.LayoutParams();
			params.width = ViewPager.LayoutParams.MATCH_PARENT;
			params.height = ViewPager.LayoutParams.MATCH_PARENT;
			zoom.setLayoutParams(params);
			zoomables.add(zoom);
		}


		container = (LinearLayout) findViewById(R.id.swi_viewpager_point);
		points = new ArrayList<ImageView>();

		int width = dip2px(6);
		int margin = dip2px(3);
		for (int i = 0; i < urls.length; i++) {
			point = new ImageView(getApplicationContext());
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width, width);
			params.weight = 1;
			params.gravity = Gravity.CENTER;
			if (i != urls.length - 1) {
				params.rightMargin = margin;
			}
			point.setLayoutParams(params);
			points.add(i, point);

			if (i == 0) {
				point.setBackgroundResource(R.drawable.swi_radio_select);
			} else {
				point.setBackgroundResource(R.drawable.swi_radio_unselect);
			}
			container.addView(point);
		}
		
		adapter = new PageAdapter(zoomables);
		viewPager.setAdapter(adapter);
		viewPager.setOnPageChangeListener(this);

		viewPager.setOffscreenPageLimit(2);
		viewPager.setCurrentItem(position);
	}
	
	@Override
	public void onPageScrollStateChanged(int arg0) {}

	@Override
	public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

	@Override
	public void onPageSelected(int position) {
		
		for (int i = 0; i < points.size(); i++) {
			if (position == i) {
				points.get(i).setBackgroundResource(R.drawable.swi_radio_select);
			} else {
				points.get(i).setBackgroundResource(R.drawable.swi_radio_unselect);
			}
		}
		if (zoomables.get(position) != null && urls.length > position) {

		//	GlideHelper.displayImageWhite(ImageViewPagerActivity.this,urls[position],zoomables.get(position));
		}
	}

	private class PageAdapter extends PagerAdapter {
		
		private List<ImageView> list;

		public PageAdapter(List<ImageView> list) {
			this.list = list;
		}

		@Override
		public void destroyItem(View container, int position, Object object) {
			((ViewPager) container).removeView(list.get(position));
		}

		@Override
		public int getCount() {
			return list.size();
		}

		@Override
		public Object instantiateItem(View container, int position) {
			((ViewPager) container).addView(list.get(position), 0);
			return list.get(position);
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view == object;
		}
	}

	
	/*#####################################*/
    
	public int pix2dip(float pxValue) {
		final float scale = this.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}
	
	public int dip2px(float dpValue) {
		final float scale = this.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}
}