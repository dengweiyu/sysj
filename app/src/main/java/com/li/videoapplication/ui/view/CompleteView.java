package com.li.videoapplication.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.li.videoapplication.R;
import com.li.videoapplication.data.DataManager;
import com.li.videoapplication.data.model.entity.VideoImage;
import com.li.videoapplication.data.model.entity.VideoImageGroup;
import com.li.videoapplication.data.preferences.PreferencesHepler;
import com.li.videoapplication.framework.AppManager;
import com.li.videoapplication.tools.ArrayHelper;
import com.li.videoapplication.tools.RandomUtil;
import com.li.videoapplication.ui.activity.VideoPlayActivity;
import com.li.videoapplication.ui.adapter.BannerAdapter;
import com.li.videoapplication.ui.adapter.VideoRecommendationAdapter;
import com.li.videoapplication.utils.LogHelper;
import com.li.videoapplication.views.CircleFlowIndicator;
import com.li.videoapplication.views.ViewFlow;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * 视图：完成播放：重复播放
 */
public class CompleteView extends RelativeLayout implements IVideoPlay, ViewFlow.ViewSwitchListener {

    public final String action = this.getClass().getName();
    public final String tag = this.getClass().getSimpleName();
    private VideoPlayActivity activity;

    private LayoutInflater inflater;

    private View replay;
    private ViewFlow videoFlow;
    private CircleFlowIndicator videoIndicator;
    private VideoRecommendationAdapter adapter;

    private List<List<VideoImage>> groupData = new ArrayList<>();

    public CompleteView(Context context) {
        this(context, null);
    }

    public CompleteView(Context context, AttributeSet attrs) {
        super(context, attrs);
        inflater = LayoutInflater.from(getContext());
    }

    public void init(VideoPlayActivity activity) {
        LogHelper.d(tag,"CompleteView  init");
        this.activity = activity;
        initContentView();
//        adapter.setMaxValue(false);
        hideView();
    }

    private void initContentView() {
        LogHelper.d(tag,"CompleteView  initContentView");
        View view = inflater.inflate(R.layout.view_videoplay_complete, this);
        replay = view.findViewById(R.id.complete_replay);

        videoFlow = (ViewFlow) view.findViewById(R.id.viewflow);
        videoIndicator = (CircleFlowIndicator) view.findViewById(R.id.circleflowindicator);
        videoFlow.setSideBuffer(4); // 实际张数
        videoFlow.setFlowIndicator(videoIndicator);
//        videoFlow.setTimeSpan(4000);
        videoFlow.setSelection(0); // 设置初始位置

        adapter = new VideoRecommendationAdapter(activity, groupData);
        videoFlow.setAdapter(adapter);
        videoFlow.setOnViewSwitchListener(this);
    }

    public void setData(List<VideoImage> data) {
        LogHelper.d(tag,"CompleteView  setData");
        groupData.clear();
        addData(data);
    }

    public void addData(List<VideoImage> data) {
        LogHelper.d(tag,"CompleteView  addData");
        groupData.addAll(ArrayHelper.createList(data, 2));
        adapter.notifyDataSetChanged();
//        if (activity.isLandscape){
//            activity.setMaxSize();
//            activity.prepareShareBtn.setVisibility(GONE);
//        }
    }

    public void setReplayListener(OnClickListener listener) {
        replay.setOnClickListener(listener);
    }

    @Override
    public void showView() {
        Log.i(tag, "showView");
        setVisibility(VISIBLE);
//        startAutoFlowTimer();
    }

    @Override
    public void hideView() {
        Log.i(tag, "hideView");
        setVisibility(GONE);
//        stopAutoFlowTimer();
    }

    @Override
    public void minView() {
    }

    @Override
    public void maxView() {
    }

    @Override
    public void onSwitched(View view, int position) {
//        adapter.setMaxValue(true);
//        startAutoFlowTimer();
    }

    public synchronized void startAutoFlowTimer() {
        if (videoFlow != null && !videoFlow.isAutoFlowTimer()) {
            // 自动播放
            videoFlow.startAutoFlowTimer();
        }
    }

    public synchronized void stopAutoFlowTimer() {
        if (videoFlow != null && videoFlow.isAutoFlowTimer()) {
            // 暂停播放
            videoFlow.stopAutoFlowTimer();
        }
    }

    @Override
    public void showCover() {

    }

    @Override
    public void hideCover() {

    }

    @Override
    public void showPlay() {

    }

    @Override
    public void hidePlay() {

    }
}
