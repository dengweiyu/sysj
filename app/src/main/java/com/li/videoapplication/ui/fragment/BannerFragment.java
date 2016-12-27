package com.li.videoapplication.ui.fragment;

import android.os.CountDownTimer;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.IPullToRefresh;
import com.li.videoapplication.data.model.entity.AdvertisementDto;
import com.li.videoapplication.mvp.home.HomeContract;
import com.li.videoapplication.mvp.home.presenter.HomePresenter;
import com.li.videoapplication.R;
import com.li.videoapplication.framework.TBaseFragment;
import com.li.videoapplication.tools.ToastHelper;
import com.li.videoapplication.ui.ActivityManeger;
import com.li.videoapplication.ui.activity.WebActivity;
import com.li.videoapplication.utils.HareWareUtil;
import com.li.videoapplication.utils.NetUtil;

/**
 * 碎片：图片广告
 */
public class BannerFragment extends TBaseFragment implements HomeContract.IAppStartView {

    private AdvertisementDto data;
    private HomeContract.IHomePresenter presenter;

    /**
     * 跳转：主页
     */
    public void startMainActivity() {
        ActivityManeger.startMainActivity(getContext());
        if (flag && data != null && data.getData().get(0).getGo_url() != null) {
            WebActivity.startWebActivity(getContext(), data.getData().get(0).getGo_url());
            // 广告点击统计+1
            presenter.adClick(data.getData().get(0).getAd_id(),
                    AdvertisementDto.AD_CLICK_STATUS_23,
                    HareWareUtil.getHardwareCode());
        }
    }

    private ImageView image;
    private TextView jump;
    private AlphaAnimation alphaAnimation;

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
        presenter = HomePresenter.getInstance();
        presenter.setAppStartView(this);

        initView(view);

        addOnClickListener();

        initAnimation();

        if (NetUtil.isConnect())
            presenter.adImage208(AdvertisementDto.ADVERTISEMENT_6, true);
        else {
            ToastHelper.s("当前网络不可用，请检查后重试。");
            handler.post(new Runnable() {

                @Override
                public void run() {
                    startMainActivity();
                }
            });
        }
    }

    private void initAnimation() {
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
    }

    private void addOnClickListener() {
        image.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (data != null && data.isResult() && data.getData().get(0).getAd_id() != 0) {
                    flag = true;
                    removeHandler();
                    cancelTimer();
                    startMainActivity();
                }
            }
        });

        jump.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                removeHandler();
                cancelTimer();
                flag = false;
                startMainActivity();
            }
        });
    }

    private void initView(View view) {
        image = (ImageView) view.findViewById(R.id.banner_image);
        jump = (TextView) view.findViewById(R.id.banner_jump);
        image.setVisibility(View.GONE);
        jump.setVisibility(View.GONE);
    }

    //广告
    @Override
    public void refreshAdvertisementView(AdvertisementDto data) {
        this.data = data;
        if (data.isResult()) {//有广告
            image.setVisibility(View.VISIBLE);
            setImageViewImageNet(image, data.getData().get(0).getServer_pic_a());
            image.startAnimation(alphaAnimation);
            startTimer();
        } else {//无广告
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

    private void startTimer() {
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

        int index = 3;

        @Override
        public void onFinish() {
            if (jump != null) {
                jump.setVisibility(View.VISIBLE);
                jump.setText("跳过 " + index);
            }
            startMainActivity();
        }

        @Override
        public void onTick(long millisUntilFinished) {
            if (jump != null) {
                jump.setVisibility(View.VISIBLE);
                jump.setText("跳过 " + index);
            }
            --index;
        }
    };

}
