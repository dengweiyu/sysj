package com.li.videoapplication.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.li.videoapplication.R;
import com.li.videoapplication.data.DataManager;
import com.li.videoapplication.data.image.ImageLoaderHelper;
import com.li.videoapplication.data.model.entity.Advertisement;
import com.li.videoapplication.data.model.event.ConnectivityChangeEvent;
import com.li.videoapplication.data.preferences.PreferencesHepler;
import com.li.videoapplication.tools.TimeHelper;
import com.li.videoapplication.ui.activity.WebActivity;
import com.li.videoapplication.utils.NetUtil;
import com.li.videoapplication.utils.URLUtil;

import io.rong.eventbus.EventBus;


/**
 * 视图：广告（750*100）
 */
public class BannerView extends FrameLayout implements
                View.OnClickListener{

    public final String action = this.getClass().getName();
    public final String tag = this.getClass().getSimpleName();

    private LayoutInflater inflater;
    protected WindowManager windowManager;
    protected int srceenWidth, srceenHeight;
    private Context context;
    private RelativeLayout container;
    private ImageView image, delete;
    private Animation appearAnim, disappearAnim;

    private boolean appear = true;

    public void setAppear(boolean appear) {
        this.appear = appear;
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        EventBus.getDefault().unregister(this);
    }

    /**
     * 回调：网络变化事件
     */
    public void onEventMainThread(ConnectivityChangeEvent event) {
        refreshData(ad_location_id);
    }

    public BannerView(Context context) {
        this(context, null);
    }

    public BannerView(Context context, AttributeSet attrs) {
        super(context, attrs);

        initContentView();
    }

    private void initContentView() {

        context = getContext();
        inflater = LayoutInflater.from(getContext());
        View view = inflater.inflate(R.layout.view_banner, this);
        container = (RelativeLayout) view.findViewById(R.id.container);
        image = (ImageView) view.findViewById(R.id.banner_image);
        delete = (ImageView) view.findViewById(R.id.banner_delete);
        image.setOnClickListener(this);
        delete.setOnClickListener(this);

        windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        srceenWidth = windowManager.getDefaultDisplay().getWidth();
        srceenHeight = windowManager.getDefaultDisplay().getHeight();
        int height = (int) (((float) (srceenWidth)) / 7.5);

        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) container.getLayoutParams();
        if (params == null)
            params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, height);
        params.width = srceenWidth;
        params.height = height;
        container.setLayoutParams(params);

        container.setVisibility(GONE);

        appearAnim = new TranslateAnimation(0, 0, height, 0);
        appearAnim.setDuration(300);
        Animation.AnimationListener appearListener = new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
                image.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                image.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {}
        };
        appearAnim.setAnimationListener(appearListener);

        disappearAnim = new TranslateAnimation(0, 0, 0, height);
        disappearAnim.setDuration(300);
        Animation.AnimationListener disappearListener = new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
                container.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                container.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {}
        };
        disappearAnim.setAnimationListener(disappearListener);
    }

    private void refreshContentView () {
        Log.d(tag, "refreshContentView: ");
        if (image != null &&
                advertisement != null &&
                advertisement.getFlag() != null &&
                TimeHelper.isBannerValid(advertisement.getStarttime(), advertisement.getEndtime()) &&
                NetUtil.isConnect() &&
                appear) {
            Log.d(tag, "refreshContentView: 1");
            ImageLoaderHelper.displayImageEmpty(advertisement.getFlag(), image);
            appear();
        } else {
            Log.d(tag, "refreshContentView: 2");
             disappear();
        }
    }

    public void disappear() {
        if (container.getVisibility() == View.VISIBLE) {
            Log.d(tag, "disappear: ");
            container.startAnimation(disappearAnim);
        }
    }

    private void appear() {
        if (container.getVisibility() != View.VISIBLE) {
            Log.d(tag, "appear: ");
            container.setVisibility(VISIBLE);
            container.startAnimation(appearAnim);
        }
    }

    private int ad_location_id;
    private Advertisement advertisement;

    public void refreshData(int ad_location_id) {
        this.ad_location_id = ad_location_id;
        Log.d(tag, "refreshHomeData: ad_location_id=" + ad_location_id);
        if (ad_location_id == DataManager.ADVERTISEMENT.ADVERTISEMENT_8) {
            // 视频管理-本地视频
            this.advertisement = PreferencesHepler.getInstance().getAdvertisement_8();
        }
        Log.d(tag, "refreshHomeData: advertisement=" + advertisement);
        if (this.advertisement != null) {
            if (this.ad_location_id == this.advertisement.getAd_location_id()) {
                refreshContentView();
            }
        }
    }

    @Override
    public void onClick(View v) {
        if (v == image) {
            if (advertisement != null &&
                    advertisement.getAd_url_android() != null &&
                    URLUtil.isURL(advertisement.getAd_url_android())) {
                Log.d(tag, "onClick: ");
                WebActivity.startWebActivity(context, advertisement.getAd_url_android());
            }
        } else if (v == delete) {
            appear = false;
            refreshContentView();
        }
    }
}
