package com.li.videoapplication.ui.fragment;

import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.handmark.pulltorefresh.library.IPullToRefresh;
import com.li.videoapplication.R;
import com.li.videoapplication.data.image.Constant;
import com.li.videoapplication.data.local.SYSJStorageUtil;
import com.li.videoapplication.data.model.response.AdvertisementDto;
import com.li.videoapplication.data.model.entity.LaunchImage;
import com.li.videoapplication.data.preferences.PreferencesHepler;
import com.li.videoapplication.framework.TBaseFragment;
import com.li.videoapplication.mvp.home.HomeContract;
import com.li.videoapplication.mvp.home.presenter.HomePresenter;
import com.li.videoapplication.tools.ToastHelper;
import com.li.videoapplication.ui.ActivityManager;
import com.li.videoapplication.ui.activity.WebActivity;
import com.li.videoapplication.utils.HareWareUtil;
import com.li.videoapplication.utils.NetUtil;
import com.li.videoapplication.utils.StringUtil;

import java.io.File;

/**
 * 碎片：图片广告
 */
public class BannerFragment extends TBaseFragment {

    private AdvertisementDto data;
    private HomeContract.IHomePresenter presenter;
    private View go;

    /**
     * 跳转：主页
     */
    public void startMainActivity() {
        try {
            ActivityManager.startMainActivity(getActivity());
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (flag && data != null) {
            LaunchImage launchImage = data.getData().get(0);
            int ad_type = launchImage.getAd_type();
            String download_android = launchImage.getDownload_android();
            switch (ad_type) {
                case 1://页面展示
                    String go_url = launchImage.getGo_url();
                    if (!StringUtil.isNull(go_url))
                        WebActivity.startWebActivity(getActivity(), go_url);
                    else
                        WebActivity.startWebActivity(getActivity(), download_android);
                    break;
                case 2://文件下载
//                    String app_name = launchImage.getDownload_desc().get(0).getApp_name();
//                    Download download = new Download();
//                    download.setDownload_url(download_android);
//                    download.setTitle(app_name);
//                    DownloadHelper.downloadFile(getActivity(), download);
                    startDownloadManagerActivity(launchImage);
                    break;
                case 3://内部跳转
                    if (!StringUtil.isNull(launchImage.getModule()) &&
                            !StringUtil.isNull(launchImage.getAssociated_id())) {
                        switch (launchImage.getModule()) {
                            case "event"://跳转赛事
                                ActivityManager.startGameMatchDetailActivity(getActivity(),
                                        launchImage.getAssociated_id());
                                break;
                            case "active"://跳转活动
                                ActivityManager.startActivityDetailActivity(getActivity(),
                                        launchImage.getAssociated_id());
                                break;
                        }
                    }
                    break;
            }

            // 广告点击统计+1
            presenter.adClick(
                    launchImage.getAd_id(),
                    AdvertisementDto.AD_CLICK_STATUS_23,
                    HareWareUtil.getHardwareCode(),
                    HareWareUtil.getIMEI());
        }
    }

    /**
     * 跳转：下载管理
     */
    public void startDownloadManagerActivity(LaunchImage launchImage) {
        Log.d(tag, "startDownloadManagerActivity: ");
        ActivityManager.startDownloadManagerActivity(getActivity(), launchImage);
    }

    private ImageView image;
    private TextView jump;

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

        initView(view);

        addOnClickListener();

        if (!NetUtil.isConnect()) {
            ToastHelper.s(R.string.net_disable);
            handler.post(new Runnable() {

                @Override
                public void run() {
                    cancelTimer();
                    startMainActivity();
                }
            });
        }
    }

    private void addOnClickListener() {
        go.setOnClickListener(mGoToListener);
        image.setOnClickListener(mGoToListener);

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
        go = view.findViewById(R.id.banner_go);
        jump = (TextView) view.findViewById(R.id.banner_jump);
        jump.setVisibility(View.GONE);

        if (PreferencesHepler.getInstance().getIndexLaunchImage() != null) {
            data = PreferencesHepler.getInstance().getIndexLaunchImage();
            String imageName = StringUtil.getFileNameWithExt(data.getData().get(0).getServer_pic_a());
            String path = SYSJStorageUtil.getSysjDownload() + File.separator + imageName;

            Glide.with(getActivity())
                    .load(path)
                    .placeholder(Constant.APPSTART_WHITE)
                    .error(Constant.APPSTART_WHITE)
                    .into(image);
            startTimer();
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


    final View.OnClickListener mGoToListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (data != null && data.isResult() && data.getData().get(0).getAd_id() != 0) {
                flag = true;
                removeHandler();
                cancelTimer();
                startMainActivity();
            }
        }
    };

    private CountDownTimer timer = new CountDownTimer(5000, 1600) {

        int index = 3;

        @Override
        public void onFinish() {
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
