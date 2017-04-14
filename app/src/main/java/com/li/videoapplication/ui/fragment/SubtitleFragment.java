package com.li.videoapplication.ui.fragment;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import com.handmark.pulltorefresh.library.IPullToRefresh;
import com.li.videoapplication.R;
import com.li.videoapplication.data.database.VideoCaptureEntity;
import com.li.videoapplication.data.local.LPDSStorageUtil;
import com.li.videoapplication.framework.TBaseFragment;
import com.li.videoapplication.tools.SRTHelper;
import com.li.videoapplication.ui.DialogManager;
import com.li.videoapplication.ui.activity.VideoEditorActivity2;
import com.li.videoapplication.ui.srt.SRT;
import com.li.videoapplication.ui.srt.SRTInfo;
import com.li.videoapplication.ui.view.SubtitleSeekBar;
import com.li.videoapplication.utils.InputUtil;
import com.li.videoapplication.utils.ThreadUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * 碎片：字幕
 */
public class SubtitleFragment extends TBaseFragment {


    @BindView(R.id.subtitleSeekBar)
    SubtitleSeekBar subtitleSeekBar;
    @BindView(R.id.subtitle_submit)
    TextView submit;
    Unbinder unbinder;
    private VideoCaptureEntity entity;
    private VideoEditorActivity2 activity;
    private long duration;
    public long leftTime, rightTime;
    public SRTInfo info = new SRTInfo();
    public SRT srt, newSRT;
    private File file;
    private Handler handler = new Handler();

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            this.activity = (VideoEditorActivity2) activity;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void initContentView(View view) {
        try {
            entity = (VideoCaptureEntity) getArguments().getSerializable("entity");
        } catch (Exception e) {
            e.printStackTrace();
        }

        unbinder = ButterKnife.bind(this, view);

        subtitleSeekBar.init(activity, this, new SubtitleSeekBar.Callback() {

            @Override
            public void updateLeftTime(long leftTime) {
                SubtitleFragment.this.leftTime = leftTime;
                if (activity != null)
                    activity.seekToVideo(leftTime);
                Log.d(tag, "updateLeftTime: leftTime=" + leftTime);
            }

            @Override
            public void updateRightTime(long rightTime) {
                SubtitleFragment.this.rightTime = rightTime;
                if (activity != null)
                    activity.seekToVideo(rightTime);
                Log.d(tag, "updateRightTime: rightTime=" + rightTime);

            }
        });

        loadData(view);

        updateDuration(duration);

        updateSubmit();
    }

    @Override
    protected int getCreateView() {
        return R.layout.fragment_subtitle;
    }

    @Override
    protected IPullToRefresh getPullToRefresh() {
        return null;
    }

    private void loadData(View view) {

        file = LPDSStorageUtil.createSubtitlePath(entity.getVideo_path());
        if (file != null) {
            info = SRTHelper.readSRT(file.getPath());
            activity.addSubtitle(file);
        }

        if (info == null) {
            info = new SRTInfo();
        }
    }

    @OnClick({R.id.subtitle_submit})
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.subtitle_submit:
                if (activity != null) {
                    if (activity.isAddingSubtitle == false) {// 添加
                        activity.isAddingSubtitle = true;
                        if (activity != null) {
                            activity.pauseVideo();
                            activity.setDrag(false);
                        }
                        if (activity != null &&
                                activity.playingView != null &&
                                activity.playingView.timedTextView != null) {
                            activity.playingView.timedTextView.hideView();
                        }
                        if (activity != null &&
                                activity.playingView != null &&
                                activity.playingView.srtEditView != null) {
                            activity.playingView.srtEditView.showView(true);
                        }
                    } else {// 确定
                        if (activity != null &&
                                activity.playingView != null &&
                                activity.playingView.srtEditView != null) {
                            if (activity.playingView.srtEditView.isEditEmpty()) {
                                showToastShort(R.string.subtitle_tip_srt_not_empty);
                                return;
                            }
                            activity.playingView.srtEditView.showView(false);
                            activity.isAddingSubtitle = false;
                            if (activity != null) {
                                activity.setDrag(true);
                            }
                            saveAndLoadSubtitle();
                            try {
                                InputUtil.closeKeyboard(getActivity());
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            if (activity != null &&
                                    activity.playingView != null &&
                                    activity.playingView.timedTextView != null) {
                                activity.playingView.timedTextView.showView();
                            }
                        }
                    }
                    if (subtitleSeekBar != null)
                        subtitleSeekBar.notifyChange();
                    updateSubmit();
                }
                break;
        }
    }

    /**
     * 编辑字幕
     */
    public void setEditable() {

        activity.isAddingSubtitle = true;
        if (activity != null) {
            activity.setDrag(false);
            activity.pauseVideo();
        }
        if (activity != null &&
                activity.playingView != null &&
                activity.playingView.timedTextView != null) {
            activity.playingView.timedTextView.hideView();
        }
        if (activity != null &&
                activity.playingView != null &&
                activity.playingView.srtEditView != null) {

            activity.playingView.srtEditView.showView(true);
            if (srt != null)
                activity.playingView.srtEditView.setEdit(srt.getText());
        }

        if (subtitleSeekBar != null)
            subtitleSeekBar.notifyChange();
        updateSubmit();
    }

    private String tmpPath;

    /**
     * 保存和加载字幕
     */
    private void saveAndLoadSubtitle() {

        final List<String> texts = new ArrayList<>();
        texts.add(activity.playingView.srtEditView.getEdit());

        ThreadUtil.start(new Runnable() {
            @Override
            public void run() {

                final SRTInfo newInfo = (SRTInfo) info.clone();
                final List<SRT> list = SRTHelper.getRepeattingSRT(newInfo, leftTime, rightTime);
                if (srt != null && list.contains(srt)) {
                    list.remove(srt);
                }

                if (list.size() > 0) {// 覆盖字幕
                    handler.post(new Runnable() {
                        @Override
                        public void run() {

                            DialogManager.showSubtitleDialog(getActivity(), new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    try {
                                        if (srt != null && newInfo.contains(srt)) {
                                            SRTHelper.removeSRT(newInfo, srt.number);
                                        }

                                        // 插入字幕（覆盖），并排序
                                        info = SRTHelper.newSRTInfo_2(newInfo, leftTime, rightTime, texts);

                                        saveAndLoadSubtitle_2();
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                        srt = null;
                                        if (subtitleSeekBar != null)
                                            subtitleSeekBar.notifyChange();
                                    }
                                }
                            }, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    srt = null;
                                    if (subtitleSeekBar != null)
                                        subtitleSeekBar.notifyChange();
                                }
                            });
                        }
                    });
                } else {
                    if (srt != null && newInfo.contains(srt)) {
                        try {
                            SRTHelper.removeSRT(newInfo, srt.number);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    // 插入字幕，并排序
                    info = SRTHelper.newSRTInfo(newInfo, leftTime, rightTime, texts);

                    saveAndLoadSubtitle_2();
                }
            }
        });
    }

    private void saveAndLoadSubtitle_2() {
        if (tmpPath == null) {
            tmpPath = LPDSStorageUtil.createTmpSubtitlePath().getPath();
        }
        SRTHelper.saveSRT(tmpPath, info);
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (activity != null) {
                    try {
                        activity.addSubtitle(new File(tmpPath));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                srt = null;
                if (subtitleSeekBar != null)
                    subtitleSeekBar.notifyChange();
            }
        });
    }

    public void cancelSubtitle() {
        srt = null;
        if (activity != null) {
            try {
                InputUtil.closeKeyboard(getActivity());
            } catch (Exception e) {
                e.printStackTrace();
            }
            activity.isAddingSubtitle = false;
            if (activity != null) {
                activity.setDrag(true);
            }
            if (activity != null &&
                    activity.playingView != null &&
                    activity.playingView.srtEditView != null) {
                activity.playingView.srtEditView.showView(false);
            }
            if (subtitleSeekBar != null)
                subtitleSeekBar.cancelOnClick();
            updateSubmit();
        }
    }

    public void updateSubmit() {
        if (activity != null && submit != null) {
            if (activity.isAddingSubtitle == false) {// 正常状态
                submit.setText(R.string.subtitle_add);
            } else {// 编辑状态
                submit.setText(R.string.subtitle_confirm);
            }
        }
    }

    public void updateDuration(long duration) {
        Log.d(tag, "updateDuration: duration=" + duration);
        this.duration = duration;
        this.rightTime = duration;
        if (subtitleSeekBar != null) {
            subtitleSeekBar.setMaxTime(duration);
            subtitleSeekBar.setLeftTime(0);
            subtitleSeekBar.setRightTime(duration);
            subtitleSeekBar.setMinTime(1 * 1000);
            subtitleSeekBar.notifyChange();
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
