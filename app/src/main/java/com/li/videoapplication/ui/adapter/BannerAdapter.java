package com.li.videoapplication.ui.adapter;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.li.videoapplication.R;
import com.li.videoapplication.data.model.entity.Gift;
import com.li.videoapplication.data.model.entity.Banner;
import com.li.videoapplication.data.model.entity.VideoImage;
import com.li.videoapplication.framework.BaseBaseAdapter;
import com.li.videoapplication.data.image.ImageLoaderHelper;
import com.li.videoapplication.tools.UmengAnalyticsHelper;
import com.li.videoapplication.ui.ActivityManeger;
import com.li.videoapplication.ui.activity.RecommendActivity;
import com.li.videoapplication.ui.activity.WebActivity;
import com.li.videoapplication.ui.toast.ToastHelper;
import com.li.videoapplication.utils.NetUtil;
import com.li.videoapplication.utils.StringUtil;

/**
 * 适配器：首页广告
 */
public class BannerAdapter extends BaseBaseAdapter {

    public static final int PAGER_HOME = 1;
    public static final int PAGER_RECOMMEND = 2;

    private int page;

    /**
     * 跳转：视频播放
     */
    private void startVideoPlayActivity(VideoImage videoImage) {
        ActivityManeger.startVideoPlayActivity(getContext(), videoImage);
    }

    /**
     * 页面跳转：礼包详情
     */
    private void startGiftDetailActivity(Gift item) {
        ActivityManeger.startGiftDetailActivity(getContext(), item.getId());
    }

    /**
     * 页面跳转：赛事详情
     */
    private void startGameMatchDetailActivity(String event_id) {
        ActivityManeger.startGameMatchDetailActivity(getContext(), event_id);
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
    public BannerAdapter(Context context, List<Banner> data, int page) {
        this.mContext = context;
        this.data = data;
        this.page = page;

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
        return data.get(position % data.size());
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
                ImageLoaderHelper.displayImageVideo(record.getFlagPath(), holder.pic);
            } else if (!StringUtil.isNull(record.getFlag())) {
                ImageLoaderHelper.displayImageVideo(record.getFlag(), holder.pic);
            }
        }

        view.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (!NetUtil.isConnect()) {
                    ToastHelper.s("当前网络不可用，请检查后重试");
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
                            if (!StringUtil.isNull(record.getGo_url())) {
                                startWebActivity(record.getGo_url());
                            }
                        }
                        if (record.getType().equals(Banner.TYPE_EVENT)) {// 赛事
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
                }
            }
        });

        return view;
    }

    private void setPicLayoutParams(ImageView view) {
        // 320/180
        int w = srceenWidth;
        int h = w * 160 / 320;
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
