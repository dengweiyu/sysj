package com.li.videoapplication.ui.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fmsysj.screeclibinvoke.logic.videoedit.MP4parserHelper;
import com.handmark.pulltorefresh.library.IPullToRefresh;
import com.li.videoapplication.R;
import com.li.videoapplication.data.database.VideoCaptureEntity;
import com.li.videoapplication.data.local.FileUtil;
import com.li.videoapplication.data.local.LPDSStorageUtil;
import com.li.videoapplication.data.network.UITask;
import com.li.videoapplication.framework.TBaseFragment;
import com.li.videoapplication.tools.IntentHelper;
import com.li.videoapplication.ui.ActivityManeger;
import com.li.videoapplication.ui.activity.VideoEditorActivity2;
import com.li.videoapplication.ui.dialog.LoadingDialog;
import com.li.videoapplication.ui.view.EditorSeekBar;
import com.li.videoapplication.utils.ScreenUtil;
import com.li.videoapplication.utils.ThreadUtil;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * 碎片：剪辑
 */
public class EditorFragment extends TBaseFragment {


    @BindView(R.id.editorSeekBar)
    EditorSeekBar editorSeekBar;
    @BindView(R.id.editor_text)
    TextView text;
    @BindView(R.id.editor_cut)
    TextView cut;
    Unbinder unbinder;

    private VideoCaptureEntity entity;
    private VideoEditorActivity2 activity;
    public long leftTime, rightTime;
    private long duration;


    public void onAttachedToWindow() {

        if (activity != null) {
            if (ScreenUtil.getAreaOneRatio(activity) - 1.7 < 0) {

                if (text != null) {
                    LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) text.getLayoutParams();
                    if (params != null) {
                        params.topMargin = ScreenUtil.dp2px(4);
                        text.setLayoutParams(params);
                    }
                }

                if (cut != null) {
                    LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) cut.getLayoutParams();
                    if (params != null) {
                        params.topMargin = ScreenUtil.dp2px(4);
                        params.bottomMargin = ScreenUtil.dp2px(4);
                        cut.setLayoutParams(params);
                    }
                }
            }
        }
    }

    public void onDetachedFromWindow() {

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            this.activity = (VideoEditorActivity2) activity;
        } catch (Exception e) {
            e.printStackTrace();
        }

        onAttachedToWindow();
    }

    @Override
    protected int getCreateView() {
        return R.layout.fragment_editor;
    }

    @Override
    protected void initContentView(View view) {
        try {
            entity = (VideoCaptureEntity) getArguments().getSerializable("entity");
        } catch (Exception e) {
            e.printStackTrace();
        }
        unbinder = ButterKnife.bind(this, view);
        editorSeekBar.init(new EditorSeekBar.Callback() {

            @Override
            public void updateLeftTime(long leftTime) {
                EditorFragment.this.leftTime = leftTime;
                if (activity != null)
                    activity.seekToVideo(leftTime);
                Log.d(tag, "updateLeftTime: leftTime=" + leftTime);
            }

            @Override
            public void updateRightTime(long rightTime) {
                EditorFragment.this.rightTime = rightTime;
                if (activity != null)
                    activity.seekToVideo(rightTime);
                Log.d(tag, "updateRightTime: rightTime=" + rightTime);

            }
        });

        updateDuration(duration);

        onAttachedToWindow();
    }

    @Override
    protected IPullToRefresh getPullToRefresh() {
        return null;
    }

    public void updateDuration(long duration) {
        this.duration = duration;
        rightTime = duration;
        if (editorSeekBar != null) {
            editorSeekBar.setMaxTime(duration);
            editorSeekBar.setLeftTime(0);
            editorSeekBar.setRightTime(duration);
            editorSeekBar.setMinTime(10 * 1000);
            editorSeekBar.notifyChange();
        }
    }

    public void resetTime() {
        if (editorSeekBar != null) {
            editorSeekBar.setLeftTime(0);
            editorSeekBar.setRightTime(duration);
            editorSeekBar.notifyChange();
        }
    }

    @OnClick({R.id.editor_cut})
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.editor_cut:
                startTrim();
                break;
        }
    }

    /**
     * 开始剪辑
     */
    private void startTrim() {
        if (!FileUtil.isFile(entity.getVideo_path())) {
            showToastShort(R.string.editor_cut_tip_exit);
            return;
        }
        if (leftTime < 0 || rightTime < 0) {
            showToastShort(R.string.editor_cut_tip_error);
            return;
        }
        if (rightTime - leftTime < 10 * 1000) {
            showToastShort(R.string.editor_cut_tip_time);
            return;
        }
        final File targetFile = LPDSStorageUtil.createTmpRecPath();
        if (targetFile == null) {
            showToastShort(R.string.editor_cut_tip_file);
            return;
        }
        final String targetPath = targetFile.getPath();
        Log.d(tag, "startTrim: leftTime=" + leftTime);
        Log.d(tag, "startTrim: rightTime=" + rightTime);
        Log.d(tag, "startTrim: path=" + entity.getVideo_path());
        Log.d(tag, "startTrim: file=" + targetFile);
        if (activity != null) {
            activity.showProgressDialogCancelable(LoadingDialog.CUTTING);
        }
        ThreadUtil.start(new Runnable() {
            @Override
            public void run() {

                final boolean flag = MP4parserHelper.trimMedia(entity.getVideo_path(), targetPath,
                        leftTime, rightTime);

                if (activity != null) {
                    activity.dismissProgressDialog();
                }

                UITask.post(new Runnable() {
                    @Override
                    public void run() {
                        if (flag == true && FileUtil.isFile(targetPath)) {
                            VideoCaptureEntity a = generateVideoCaptureEntity(targetPath);
                            if (a != null) {
                                showToastShort(R.string.editor_cut_tip_success);
                                try {
                                    activity.finish();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                // 视频编辑
                                ActivityManeger.startVideoEditorActivity_2(getActivity(), a);
                            }
                            // 通知系统扫描媒体文件
                            IntentHelper.scanFile();
                        } else {
                            showToastShort(R.string.editor_cut_tip_failed);
                        }
                    }
                });
            }
        });
    }

    public static VideoCaptureEntity generateVideoCaptureEntity(String targetPath) {
        VideoCaptureEntity a = new VideoCaptureEntity();
        File file = null;
        try {
            file = new File(targetPath);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (file == null || file.isDirectory())
            return null;
        String targetName = file.getName();
        String extName = FileUtil.getExtName(targetName);
        if (!extName.startsWith("."))
            extName = "." + extName;
        targetName = targetName.replace(extName, "");
        a.setVideo_name(targetName);
        a.setVideo_path(targetPath);
        a.setVideo_source("");
        a.setVideo_station(VideoCaptureEntity.VIDEO_STATION_LOCAL);
        return a;
    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
