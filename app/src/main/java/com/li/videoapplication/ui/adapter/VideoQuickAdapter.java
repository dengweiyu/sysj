package com.li.videoapplication.ui.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.li.videoapplication.R;
import com.li.videoapplication.data.model.entity.VideoImage;
import com.li.videoapplication.framework.AppManager;
import com.li.videoapplication.tools.TextImageHelper;
import com.li.videoapplication.tools.TimeHelper;
import com.li.videoapplication.utils.ScreenUtil;
import com.li.videoapplication.utils.StringUtil;
import com.li.videoapplication.utils.URLUtil;
import com.li.videoapplication.views.CircleImageView;

import java.util.List;

/**
 * Created by cx on 2017/12/12.
 */

public class VideoQuickAdapter extends BaseQuickAdapter<VideoImage, BaseViewHolder> {
    private final String tag = this.getClass().getSimpleName();

    private List<VideoImage> mData;
    private Fragment mFragment;

    protected TextImageHelper textImageHelper = new TextImageHelper();
    protected Resources resources;
    protected WindowManager windowManager;
    protected int srceenWidth, srceenHeight;

    private  boolean isScrolling = false;

    public VideoQuickAdapter(Fragment fragment, int layoutResId, List<VideoImage> data) {
        super(layoutResId, data);
        mFragment = fragment;
        mData = data;
        init();
    }

    public void setAllData(List<VideoImage> data) {
        if (!mData.isEmpty() && !data.isEmpty()) {
            mData.clear();
            mData.addAll(data);
        }
    }



    @Override
    protected void convert(BaseViewHolder holder, VideoImage videoImage) {
        setLayoutParams(holder.getView(R.id.root));
        TextView title = holder.getView(R.id.video_title);
        ImageView cover = holder.getView(R.id.video_cover);
        TextView allTime = holder.getView(R.id.video_allTime);
        ImageView avatar = holder.getView(R.id.civ_user);//FIXME 这里采用CircleImageView 可能会出错
        TextView nickname= holder.getView(R.id.tv_up_user_name);

        // 标题
        if (!StringUtil.isNull(videoImage.getVideo_name())) {
            textImageHelper.setTextViewText(title, videoImage.getVideo_name());
        } else if (!StringUtil.isNull(videoImage.getTitle())) {
            textImageHelper.setTextViewText(title, videoImage.getTitle());
        }
        //作者名字
        if (!StringUtil.isNull((videoImage.getNickname()))){
            textImageHelper.setTextViewText(nickname, videoImage.getNickname());
        }
        // 播放时长
        setTimeLength(allTime, videoImage);
        if (!isScrolling) {
            // 封面
            if (!StringUtil.isNull(videoImage.getFlagPath())) {
                if (URLUtil.isURL(videoImage.getFlagPath())) {
                    textImageHelper.setImageViewNetAlpha(mFragment, cover, videoImage.getFlagPath());
                }
            }
            //上传主的头像
            if (!StringUtil.isNull(videoImage.getAvatar())){
                if (URLUtil.isURL(videoImage.getAvatar())){
                    textImageHelper.setCircleImageNetAlpha(mFragment, avatar,videoImage.getAvatar());
                }
            }
            Log.w(tag, "没有滚动，图渲染..");
        } else {
            Log.w(tag, "在滚动，不做处理..");
        }
    }

    private void setLayoutParams(View view) {
        if (view != null) {
            // 86/148
            int w = (srceenWidth - ScreenUtil.dp2px(10 * 2)) / 2;
            int h = w * 9 / 16;
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, h);
            view.setLayoutParams(params);
        }
    }

    private void init() {

        resources = AppManager.getInstance().getContext().getResources();
        windowManager = (WindowManager) AppManager.getInstance().getContext().getSystemService(Context.WINDOW_SERVICE);

        srceenWidth = windowManager.getDefaultDisplay().getWidth();
        srceenHeight = windowManager.getDefaultDisplay().getHeight();
    }

    public boolean getScrolling() {
        return isScrolling;
    }

    public void setScrolling(boolean scrolling) {
        this.isScrolling = scrolling;
    }

    public void setVideoType(String more_mark) {
        for (int i = 0; i < mData.size(); i++) {
            mData.get(i).setMore_mark(more_mark);
        }
    }

    /**
     * 视频时间长度
     */
    private void setTimeLength(TextView view, VideoImage record) {
        // 17:00
        try {
            textImageHelper.setTextViewText(view, TimeHelper.getVideoPlayTime(record.getTime_length()));
        } catch (Exception e) {
            e.printStackTrace();
            textImageHelper.setTextViewText(view, "");
        }
    }
}
