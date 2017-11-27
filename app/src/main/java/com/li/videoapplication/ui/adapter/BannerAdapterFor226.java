package com.li.videoapplication.ui.adapter;

/**
 * Created by y on 2017/11/15.
 */

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.li.videoapplication.R;
import com.li.videoapplication.data.image.GlideHelper;
import com.li.videoapplication.data.model.entity.Banner;
import com.li.videoapplication.data.model.entity.Gift;
import com.li.videoapplication.data.model.entity.VideoImage;
import com.li.videoapplication.data.model.response.HomeModuleEntity;
import com.li.videoapplication.framework.BaseBaseAdapter;
import com.li.videoapplication.tools.ToastHelper;
import com.li.videoapplication.tools.UmengAnalyticsHelper;
import com.li.videoapplication.ui.ActivityManager;
import com.li.videoapplication.ui.activity.RecommendActivity;
import com.li.videoapplication.ui.activity.WebActivity;
import com.li.videoapplication.utils.NetUtil;
import com.li.videoapplication.utils.StringUtil;

import java.util.ArrayList;
import java.util.List;

public class BannerAdapterFor226 extends BaseBaseAdapter {

    public static final int PAGER_HOME = 1;
    public static final int PAGER_RECOMMEND = 2;

    private int page;
    private List<HomeModuleEntity.ADataBean.ListBean> list;

    /**
     * 跳转：视频播放
     */
    private void startVideoPlayActivity(VideoImage videoImage) {
        ActivityManager.startVideoPlayActivity(getContext(), videoImage);
    }

    /**
     * 页面跳转：礼包详情
     */
    private void startGiftDetailActivity(Gift item) {
        ActivityManager.startGiftDetailActivity(getContext(), item.getId());
    }

    /**
     * 页面跳转：赛事详情
     */
    private void startGameMatchDetailActivity(String event_id) {
        ActivityManager.startGameMatchDetailActivity(getContext(), event_id);
    }

    /**
     * 页面跳转：活动
     */
    private void startActivityDetailActivity208(String match_id) {
        ActivityManager.startActivityDetailActivity(getContext(), match_id);
    }

    /**
     * 页面跳转：网页
     */
    private void startWebActivity(String url) {
        WebActivity.startWebActivity(getContext(), url);
    }

    private Context mContext;
    private List<Banner> data;
    private boolean maxValue;

    private int srceenWidth;
    private WindowManager windowManager;
    private Activity activity;

    public void setMaxValue(boolean maxValue) {
        this.maxValue = maxValue;
    }

    @SuppressWarnings("deprecation")
    public BannerAdapterFor226(Context context, List<HomeModuleEntity.ADataBean.ListBean> list, int page) {
        this.mContext = context;
        this.page = page;
        List<Banner> bannerData = new ArrayList<>();

        for (int i = 0; i < list.size(); i++) {
            Banner banner = new Banner();
            HomeModuleEntity.ADataBean.ListBean dataBean = list.get(i);

            banner.setType(dataBean.getType());
            banner.setVideo_id(dataBean.getVideo_id());
            banner.setPackage_id(dataBean.getPackage_id());

            banner.setFlag(dataBean.getFlag());
            banner.setFlagPath(dataBean.getFlagPath());
            banner.setTitle(dataBean.getTitle());
            banner.setUrl(dataBean.getUrl());
            banner.setQn_key(dataBean.getQn_key());

            bannerData.add(banner);
        }
        this.data = bannerData;

        inflater = LayoutInflater.from(context);
        resources = context.getResources();

        windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        srceenWidth = windowManager.getDefaultDisplay().getWidth();

        try {
            activity = (Activity) context;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getCount() {
        if (maxValue) {
            // 返回很大的值使得getView中的position不断增大来实现循环
            return Integer.MAX_VALUE;
        } else {
            return data.size();
        }
    }

    @Override
    public Banner getItem(int position) {
        int p = position % data.size();

        return data.get(p);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View view, ViewGroup parent) {

        final Banner record = getItem(position);
        final ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.adapter_home_banner, null);
            holder.pic = (ImageView) view.findViewById(R.id.banner_pic);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        if (data.size() != 0) {
            if (!StringUtil.isNull(record.getFlagPath())) {
                GlideHelper.displayImage(getContext(), record.getFlagPath(), holder.pic);
            } else if (!StringUtil.isNull(record.getFlag())) {
                GlideHelper.displayImage(getContext(), record.getFlag(), holder.pic);
            }
        }

        view.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (!NetUtil.isConnect()) {
                    ToastHelper.s(R.string.net_disable);
                    return;
                }
                if (page == PAGER_HOME) {
                    if (data.size() != 0 && record.getType() != null) {
                        if (record.getType().equals(Banner.TYPE_VIDEO)) {// 视频
                            startVideoPlayActivity(getVideoImage(record));
                        }
                        if (record.getType().equals(Banner.TYPE_URL)) {// 网页
                            if (!StringUtil.isNull(record.getGo_url())) {
                                startWebActivity(record.getGo_url());
                            }
                        }
                        if (record.getType().equals(Banner.TYPE_PACKAGE)) {// 礼包
                            startGiftDetailActivity(getGift(record));
                        }
                        if (record.getType().equals(Banner.TYPE_ACTIVITY)) {// 活动
                            if (!StringUtil.isNull(record.getType_id()))
                                startActivityDetailActivity208(record.getType_id());
                        }
                        if (record.getType().equals(Banner.TYPE_EVENT)) {// 赛事
                            if (!StringUtil.isNull(record.getType_id()))
                                startGameMatchDetailActivity(record.getType_id());
                        }
                    }
                } else if (page == PAGER_RECOMMEND) {
                    startVideoPlayActivity(getVideoImage(record));
                }

                if (activity instanceof RecommendActivity) {
                    UmengAnalyticsHelper.onEvent(mContext, UmengAnalyticsHelper.DISCOVER, "精彩推荐-轮播图");
                } else {
                    UmengAnalyticsHelper.onEvent(mContext, UmengAnalyticsHelper.MAIN, "轮播图");
                    UmengAnalyticsHelper.onEvent(mContext, UmengAnalyticsHelper.MAIN,
                            "焦点图-焦点图" + position + 1 + "-点击焦点图" + position + 1 + "次数");
                }
            }
        });

        return view;
    }

    private void setPicLayoutParams(ImageView view) {
        //750:350 = 15:7
        int w = srceenWidth;
        int h = w * 7 / 15;
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) view.getLayoutParams();
        params.width = w;
        params.height = h;
        view.setLayoutParams(params);
    }

    private Gift getGift(final Banner record) {
        Gift item = new Gift();
        item.setId(record.getPackage_id());
        return item;
    }

    private class ViewHolder {
        public ImageView pic;
    }

    @Override
    protected Context getContext() {
        return mContext;
    }

    private VideoImage getVideoImage(final Banner record) {

        VideoImage videoImage = new VideoImage();
        videoImage.setId(record.getVideo_id());
        videoImage.setVideo_id(record.getVideo_id());
        return videoImage;
    }
}
