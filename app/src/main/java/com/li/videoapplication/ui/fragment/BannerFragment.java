package com.li.videoapplication.ui.fragment;

import java.util.List;

import android.os.CountDownTimer;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.IPullToRefresh;
import com.li.videoapplication.R;
import com.li.videoapplication.data.model.entity.LaunchImage;
import com.li.videoapplication.data.model.response.IndexLaunchImageEntity;
import com.li.videoapplication.data.preferences.PreferencesHepler;
import com.li.videoapplication.framework.TBaseFragment;
import com.li.videoapplication.tools.TimeHelper;
import com.li.videoapplication.ui.ActivityManeger;
import com.li.videoapplication.ui.activity.WebActivity;
import com.li.videoapplication.utils.StringUtil;
import com.li.videoapplication.utils.URLUtil;

/**
 * 碎片：图片广告
 */
public class BannerFragment extends TBaseFragment {

	/**
	 * 跳转：主页
	 */
	public void startMainActivity() {
		ActivityManeger.startMainActivity(getActivity());
		if (flag == true) {
			Log.d(tag, "startMainActivity: web");
			WebActivity.startWebActivity(getActivity(), entity.getData().get(0).getGo_url());
		}
	}

	private ImageView image;
	private TextView jump;
    private AlphaAnimation alphaAnimation;

	private IndexLaunchImageEntity entity;
	private String go_url;
	private boolean flag = false;
	
	public static BannerFragment newInstance() {
		return new BannerFragment();
	}

	@Override
	protected int getCreateView() {
		return R.layout.fragment_banner;
	}

	@SuppressWarnings("rawtypes")
	@Override
	protected IPullToRefresh getPullToRefresh() {
		return null;
	}

	@Override
	protected void initContentView(View view) {

		image = (ImageView) view.findViewById(R.id.banner_image);
		jump = (TextView) view.findViewById(R.id.banner_jump);
		image.setVisibility(View.GONE);
		jump.setVisibility(View.GONE);

		image.setOnTouchListener(new View.OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				Log.d(tag, "onTouch: ");
				if (entity != null &&
						entity.getData() != null &&
						entity.getData().size() > 0 &&
						entity.getData().get(0).getGo_url() != null &&
						TimeHelper.isBannerValid(entity.getData().get(0).getStarttime(), entity.getData().get(0).getEndtime())) {
					go_url = entity.getData().get(0).getGo_url();
					flag = true;
					removeHandler();
					cancelTimer();
					Log.d(tag, "onTouch: go_url=" + go_url);
					startMainActivity();
				}
				return true;
			}
		});

		jump.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Log.d(tag, "onClick: ");
				removeHandler();
				cancelTimer();
				flag = false;
				startMainActivity();
			}
		});

        alphaAnimation = new AlphaAnimation(0.4f, 1.0f);
        alphaAnimation.setDuration(1000);
        alphaAnimation.setInterpolator(new DecelerateInterpolator());
        alphaAnimation.setAnimationListener(new Animation.AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {
			}

			@Override
			public void onAnimationEnd(Animation animation) {
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
			}
		});

		entity = PreferencesHepler.getInstance().getIndexLaunchImage();
		Log.d(tag, "entity=" + entity);
		if (entity != null) {
			List<LaunchImage> data = entity.getData();
			if (data != null &&
					data.size() > 0) {
				String url = data.get(0).getFlag();
				if (!StringUtil.isNull(url) && URLUtil.isURL(url) &&
						TimeHelper.isBannerValid(entity.getData().get(0).getStarttime(), entity.getData().get(0).getEndtime())) {
					image.setVisibility(View.VISIBLE);
					setImageViewImageNet(image, url);
                    image.startAnimation(alphaAnimation);
				}
			}
		}

		if (entity != null &&
				entity.getData() != null &&
				entity.getData().size() > 0 &&
				entity.getData().get(0).getGo_url() != null &&
				TimeHelper.isBannerValid(entity.getData().get(0).getStarttime(), entity.getData().get(0).getEndtime())) {

			startTimer();
		} else {

			handler.post(new Runnable() {

				@Override
				public void run() {
					startMainActivity();
				}
			});
		}
	}

	private void removeHandler() {
		if (handler != null)
            handler.removeCallbacksAndMessages(null);
	}

	private void  startTimer() {
		timer.start();
	}

	private void cancelTimer() {
		if (timer != null)
			try {
				timer.cancel();
			} catch (Exception e) {
				e.printStackTrace();
			}
	}

	private CountDownTimer timer = new CountDownTimer(5000, 1600) {

		public int index = 3;

		@Override
		public void onFinish() {
			Log.d(tag, "onFinish: 0");
			if (jump != null) {
				jump.setVisibility(View.VISIBLE);
				jump.setText("跳过 " + index);
			}
			startMainActivity();
		}

		@Override
		public void onTick(long millisUntilFinished) {
			Log.d(tag, "onTick: millisUntilFinished=" + millisUntilFinished);
			Log.d(tag, "onTick: " + index);
			if (jump != null) {
				jump.setVisibility(View.VISIBLE);
				jump.setText("跳过 " + index);
			}
			--index;
		}
	};
}
