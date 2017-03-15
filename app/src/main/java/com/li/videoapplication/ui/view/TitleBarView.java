package com.li.videoapplication.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.happly.link.HpplayLinkWindow;
import com.li.videoapplication.R;
import com.li.videoapplication.framework.AppManager;
import com.li.videoapplication.tools.UmengAnalyticsHelper;
import com.li.videoapplication.ui.ActivityManeger;
import com.li.videoapplication.ui.DialogManager;
import com.li.videoapplication.ui.activity.VideoPlayActivity;
import com.li.videoapplication.utils.StringUtil;
import com.li.videoapplication.utils.URLUtil;

/**
 * 视图：标题栏
 */
public class TitleBarView extends RelativeLayout implements
        View.OnClickListener,
        IVideoPlay {

    public final String action = this.getClass().getName();
    public final String tag = this.getClass().getSimpleName();

    private LayoutInflater inflater;

    private View view;
    private ImageView goback, share, setting;
    private TextView title;
    private ImageView tv;

    private VideoPlayActivity activity;

    public TitleBarView(Context context) {
        this(context, null);
    }

    public TitleBarView(Context context, AttributeSet attrs) {
        super(context, attrs);

        activity = (VideoPlayActivity) AppManager.getInstance().getActivity(VideoPlayActivity.class);

        initContentView();
        minView();
        hideView();
    }

    private void initContentView() {
        inflater = LayoutInflater.from(getContext());
        view = inflater.inflate(R.layout.view_videoplay_titlebar, this);

        goback = (ImageView) view.findViewById(R.id.titlebar_goback);
        title = (TextView) view.findViewById(R.id.titlebar_title);
        share = (ImageView) view.findViewById(R.id.titlebar_share);
        setting = (ImageView) view.findViewById(R.id.titlebar_setting);
        tv = (ImageView) view.findViewById(R.id.titlebar_tv);

        goback.setOnClickListener(this);
        setting.setOnClickListener(this);
        share.setOnClickListener(this);
        tv.setOnClickListener(this);
    }

    public void setTitleText(String text) {
        if (!StringUtil.isNull(text))
            title.setText(text);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.titlebar_share:
                if (activity != null)
                    activity.startShareActivity();
                break;

            case R.id.titlebar_goback:
                if (activity != null) {
                    activity.onBackPressed();
                }
                break;

            case R.id.titlebar_setting:
                if (activity != null) {
                    DialogManager.showSettingDialog(activity);
                    UmengAnalyticsHelper.onEvent(activity, UmengAnalyticsHelper.VIDEOPLAY, "视频-全屏-设置");
                }
                break;

            case R.id.titlebar_tv:
                if (!StringUtil.isNull(activity.qn_key) && URLUtil.isURL(activity.qn_url)) {
                    //选择投屏设备窗口
                    new HpplayLinkWindow(activity, activity.qn_url);
                    return;
                } else if (!StringUtil.isNull(activity.yk_url) && URLUtil.isURL(activity.youku_url)) {
                    new HpplayLinkWindow(activity, activity.youku_url);
                    return;
                }
                if (activity != null)
                    UmengAnalyticsHelper.onEvent(activity, UmengAnalyticsHelper.VIDEOPLAY, "投屏-点击投屏次数");
                break;
        }
    }

    @Override
    public void showView() {
        setVisibility(VISIBLE);
    }

    @Override
    public void hideView() {
        setVisibility(GONE);
    }

    @Override
    public void minView() {
        tv.setVisibility(View.GONE);
        setting.setVisibility(View.GONE);
    }

    @Override
    public void maxView() {
        tv.setVisibility(View.VISIBLE);
        setting.setVisibility(View.VISIBLE);
    }
}
