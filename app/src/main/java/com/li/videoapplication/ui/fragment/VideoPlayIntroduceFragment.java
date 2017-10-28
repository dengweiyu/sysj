package com.li.videoapplication.ui.fragment;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.IPullToRefresh;
import com.li.videoapplication.R;
import com.li.videoapplication.data.DataManager;
import com.li.videoapplication.data.model.entity.VideoImage;
import com.li.videoapplication.framework.AppConstant;
import com.li.videoapplication.framework.TBaseFragment;
import com.li.videoapplication.mvp.Constant;
import com.li.videoapplication.tools.UmengAnalyticsHelper;
import com.li.videoapplication.tools.ToastHelper;
import com.li.videoapplication.ui.ActivityManager;
import com.li.videoapplication.utils.StringUtil;
import com.li.videoapplication.views.RoundedImageView;
import com.umeng.analytics.AnalyticsConfig;

/**
 * 碎片：视频播放/游戏简介
 */
public class VideoPlayIntroduceFragment extends TBaseFragment implements OnClickListener {

    /**
     * 跳转：安装应用
     */
    private void install() {
        if (item != null && !StringUtil.isNull(item.getAndroid_address())) {
            /*Uri uri = Uri.parse(item.getAndroid_address());
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            getActivity().startActivity(intent);
            getActivity().overridePendingTransition(R.anim.activity_hold, R.anim.activity_hold);*/
            ActivityManager.startDownloadManagerActivity(getActivity(),item.getGame_id(),"2",item.video_id);
            UmengAnalyticsHelper.onEvent(getActivity(), UmengAnalyticsHelper.VIDEOPLAY, "游戏简介-安装");
            // 游戏下载数+1
            DataManager.downloadClick217(item.getGame_id(), getMember_id(),
                    Constant.DOWNLOAD_LOCATION_VIDEOPLAY, item.getVideo_id());
        } else {
            ToastHelper.s("该游戏暂无安卓版本");
        }
    }

    private VideoImage item;

    public void setItem(VideoImage item) {
        this.item = item;
        Log.d(tag, "Item: == " + item);
        refreshContentView();

        if (isNeedHideDownload(getActivity())){
            if (install != null){
                install.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            UmengAnalyticsHelper.onEvent(getActivity(), UmengAnalyticsHelper.VIDEOPLAY, "游戏简介");
        }
    }

    @Override
    protected int getCreateView() {
        return R.layout.fragment_videoplay_introduce;
    }

    @SuppressWarnings("rawtypes")
    @Override
    protected IPullToRefresh getPullToRefresh() {
        return null;
    }

    private RoundedImageView pic;
    private TextView title, content, install, setting, race;


    @Override
    protected void initContentView(View view) {
        pic = (RoundedImageView) view.findViewById(R.id.videoplay_introduce_pic);
        title = (TextView) view.findViewById(R.id.videoplay_introduce_title);
        content = (TextView) view.findViewById(R.id.videoplay_introduce_content);
        install = (TextView) view.findViewById(R.id.videoplay_introduce_install);
        setting = (TextView) view.findViewById(R.id.videoplay_introduce_setting);
        race = (TextView) view.findViewById(R.id.videoplay_introduce_race);

        install.setOnClickListener(this);

        if (AppConstant.SHOW_DOWNLOAD_AD) {
            install.setVisibility(View.VISIBLE);
        } else {
            install.setVisibility(View.GONE);
        }

        if (isNeedHideDownload(getActivity())){
            install.setVisibility(View.GONE);
        }
    }



    /**
     *
     */
    private boolean isNeedHideDownload(Context context){
        String channel = AnalyticsConfig.getChannel(context);
        if ("huawei".equals(channel) || "xiaomi".equalsIgnoreCase(channel) || "anzhi".equalsIgnoreCase(channel)){
            return true;
        }
        return false;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.videoplay_introduce_install:
                install();
                break;
        }
    }

    private void refreshContentView() {

        if (item != null) {
            setImageViewImageNet(pic, item.getG_flag());
            setTextViewText(title, item.getGame_name());
            setTextViewText(content, item.getG_description());
            if (install != null){
                if (StringUtil.isNull(item.getAndroid_address())){
                    install.setVisibility(View.GONE);
                }else {
                    install.setVisibility(View.VISIBLE);
                }
            }

        }
    }
}
