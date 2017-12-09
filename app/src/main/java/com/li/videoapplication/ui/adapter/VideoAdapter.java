package com.li.videoapplication.ui.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.li.videoapplication.R;
import com.li.videoapplication.data.model.entity.Member;
import com.li.videoapplication.data.model.entity.VideoImage;
import com.li.videoapplication.framework.BaseArrayAdapter;
import com.li.videoapplication.framework.TBaseFragment;
import com.li.videoapplication.tools.TimeHelper;
import com.li.videoapplication.tools.UmengAnalyticsHelper;
import com.li.videoapplication.ui.ActivityManager;
import com.li.videoapplication.ui.activity.CollectionActivity;
import com.li.videoapplication.ui.fragment.MyCollectionFragment;
import com.li.videoapplication.ui.fragment.MyHistoryFragment;
import com.li.videoapplication.utils.ScreenUtil;
import com.li.videoapplication.utils.StringUtil;
import com.li.videoapplication.utils.URLUtil;
import com.li.videoapplication.views.CircleImageView;

import java.util.ArrayList;
import java.util.List;

import io.rong.eventbus.EventBus;

/**
 * 适配器：视频
 */
@SuppressLint("InflateParams")
public class VideoAdapter extends BaseArrayAdapter<VideoImage> implements
        AbsListView.OnScrollListener {

    /**
     * 选择要删除的文件列表选
     */
    public List<Boolean> positionData = new ArrayList<>();
    /**
     * 已选择，将要操作的列表
     */
    public List<VideoImage> deleteData = new ArrayList<>();

    public List<VideoImage> data;
    private TBaseFragment fragment;

    private int longPosition = -1;

    private  boolean isScrolling = false;

    private boolean showFlag = false; //渲染后为true

    public boolean getScrolling() {
        return isScrolling;
    }

    public void setScrolling(boolean scrolling) {
        this.isScrolling = scrolling;
    }

    /**
     * 跳转：视频播放
     */
    private void startVideoPlayActivity(VideoImage item) {
        if (!StringUtil.isNull(item.getVideo_id())) {
            ActivityManager.startVideoPlayActivity(getContext(), item);
        }
    }

    /**
     * 是否处于批量删除状态
     */
    private boolean deleteMode = false;
    /**
     * 跳转：玩家动态 居然要这个参数。。。。
     */
    private void startPlayerDynamicActivity(Member member) {
        if (StringUtil.isNull(member.getId())) {
            member.setId(member.getMember_id());
        }
        ActivityManager.startPlayerDynamicActivity(getContext(), member);
    }


    public void setDeleteMode(boolean isDeleteMode) {
        this.deleteMode = isDeleteMode;
        if (isDeleteMode && data != null) {
            deleteData.clear();
            positionData.clear();
            for (int i = 0; i < data.size(); i++) {
                positionData.add(false);
            }
        }
        notifyDataSetChanged();
    }

    public boolean isDeleteMode() {
        return deleteMode;
    }

    public VideoAdapter(Context context, List<VideoImage> data) {
        super(context, R.layout.adapter_video, data);
        EventBus.getDefault().register(this);
        this.data = data;

        deleteData.clear();
        positionData.clear();
        for (int i = 0; i < data.size(); i++) {
            positionData.add(false);
        }
    }

    public VideoAdapter(Context context, List<VideoImage> data, TBaseFragment fragment) {
        super(context, R.layout.adapter_video, data);
        this.data = data;
        this.fragment = fragment;

        deleteData.clear();
        positionData.clear();
        for (int i = 0; i < data.size(); i++) {
            positionData.add(false);
        }
    }

    public void addData(List<VideoImage> moreData) {
        this.data.addAll(moreData);
    }

    public void setVideoType(String more_mark) {
        for (int i = 0; i < data.size(); i++) {
            data.get(i).setMore_mark(more_mark);
        }
    }

    @Override
    public View getView(final int position, View view, ViewGroup parent) {

        final VideoImage record = getItem(position);
        final ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.adapter_video, null);
            holder.title = (TextView) view.findViewById(R.id.video_title);
            //holder.playCount = (TextView) view.findViewById(R.id.video_playCount);
            //holder.play = (ImageView) view.findViewById(R.id.video_play);//播放数
            holder.cover = (ImageView) view.findViewById(R.id.video_cover);
            holder.allTime = (TextView) view.findViewById(R.id.video_allTime);
            holder.deleteState = (CheckBox) view.findViewById(R.id.vedio_deleteState);
            holder.deleteButton = (ImageView) view.findViewById(R.id.vedio_deleteButton);
            holder.root = view.findViewById(R.id.root);
            holder.avatar= (CircleImageView) view.findViewById(R.id.civ_user);//FIXME 这里采用CircleImageView 可能会出错
            holder.nickname= (TextView) view.findViewById(R.id.tv_up_user_name);

            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        setLayoutParams(holder.root);

        // 删除
        setDelete(position, holder.deleteState, holder.deleteButton, record);

        // 标题
        if (!StringUtil.isNull(record.getVideo_name())) {
            setTextViewText(holder.title, record.getVideo_name());
        } else if (!StringUtil.isNull(record.getTitle())) {
            setTextViewText(holder.title, record.getTitle());
        }
        //作者名字
        if (!StringUtil.isNull((record.getNickname()))){
            setTextViewText(holder.nickname,record.getNickname());
        }

        //播放数格式成以万为单位
        String clickCount = StringUtil.toUnitW(record.getClick_count());
        // 播放数
        setTextViewText(holder.playCount, clickCount);

        // 播放时长
        setTimeLength(holder.allTime, record);

        // 封面
        if (!StringUtil.isNull(record.getFlag())) {
            if (URLUtil.isURL(record.getFlag())) {
                setImageViewImageNetAlpha(holder.cover, record.getFlagPath());
            }
        }
        if (!isScrolling) {
            if (!StringUtil.isNull(record.getFlagPath())) {
                if (URLUtil.isURL(record.getFlagPath())) {
                    setImageViewImageNetAlpha(holder.cover, record.getFlagPath());
                }
            }

            Log.w(tag, "没有滚动，图渲染..");
        } else {
            holder.cover.setImageResource(R.drawable.default_video_211);
            Log.w(tag, "在滚动，先本地set..");
        }
        Log.w(tag, "执行notifyDataSetChanged，isScrolling：" + isScrolling);
        //上传主的头像
        if (!StringUtil.isNull(record.getAvatar())){
            if (URLUtil.isURL(record.getAvatar())){
                setCircleImageViewNetAlpha(holder.avatar,record.getAvatar());
            }
        }

        holder.deleteButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Log.i(tag, "onClick");
                if (deleteMode) {
                    if (positionData.get(position)) {// 已选择
                        positionData.set(position, false);
                        deleteData.remove(record);
                        holder.deleteState.setChecked(false);
                    } else {//未选择
                        positionData.set(position, true);
                        deleteData.add(record);
                        holder.deleteState.setChecked(true);
                    }
                    Log.i(tag, "deleteMode=" + deleteMode);
                    Log.i(tag, "positionData=" + positionData);
                    Log.i(tag, "deleteData=" + deleteData);
                }
            }
        });

        // 如果是通过长按召唤出的，将长按的控件设置为选中
        if (deleteMode && longPosition != -1 && longPosition == position) {
            positionData.set(position, true);
            deleteData.add(record);
            holder.deleteState.setChecked(true);
            longPosition = -1;
        }
        //头像点击
        holder.avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (record.getMember_id()!=null){
                    //跳转
                    Log.d(tag,record.getMember_id()+"点击了头像");
                }
            }
        });

        // 长按删除
        view.setOnLongClickListener(new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(View v) {

                if (!deleteMode) {
                    longPosition = position;
                    CollectionActivity.performClick2();
                    return true;
                }
                return false;
            }
        });

        view.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                startVideoPlayActivity(record);

                if (!StringUtil.isNull(record.getMore_mark()))
                    UmengAnalyticsHelper.onMainGameEvent(getContext(), record.getMore_mark());

                if (fragment != null) {
                    if (fragment instanceof MyCollectionFragment) {
                        UmengAnalyticsHelper.onEvent(getContext(), UmengAnalyticsHelper.SLIDER, "我的收藏-有效");
                    } else if (fragment instanceof MyHistoryFragment) {
                        UmengAnalyticsHelper.onEvent(getContext(), UmengAnalyticsHelper.SLIDER, "观看记录-有效");
                    }
                }
            }
        });

        return view;
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

    /**
     * 视频时间长度
     */
    private void setTimeLength(TextView view, VideoImage record) {
        // 17:00
        try {
            setTextViewText(view, TimeHelper.getVideoPlayTime(record.getTime_length()));
        } catch (Exception e) {
            e.printStackTrace();
            setTextViewText(view, "");
        }
    }

    /**
     * 删除
     */
    private void setDelete(final int position, final CheckBox view, ImageView v, final VideoImage record) {

        if (deleteMode) {
            view.setVisibility(View.VISIBLE);
            v.setVisibility(View.VISIBLE);
            view.setChecked(positionData.get(position));
        } else {
            v.setVisibility(View.GONE);
            view.setVisibility(View.GONE);
        }
    }

    private static class ViewHolder {
        TextView title;
        ImageView play;
        ImageView cover;
        TextView playCount;// 785
        TextView allTime;// 18:22
        CheckBox deleteState;
        ImageView deleteButton;
        View root;
        CircleImageView avatar;
        TextView nickname;
        TextView userID;
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

        if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_FLING) {

        } else if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {

        } else if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {

        } else {

        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

    }

    public void onEventMainThread(boolean isScrolling) {
        this.isScrolling = isScrolling;
        Log.w(tag, "改变滚动状态..");
    }

}
