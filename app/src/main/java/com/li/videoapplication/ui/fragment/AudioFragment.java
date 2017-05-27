package com.li.videoapplication.ui.fragment;

import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaMetadataRetriever;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fmsysj.screeclibinvoke.logic.videoedit.MP4parserHelper;
import com.fmsysj.screeclibinvoke.logic.videoedit.SoundHelper;
import com.handmark.pulltorefresh.library.IPullToRefresh;
import com.li.videoapplication.R;
import com.li.videoapplication.data.database.VideoCaptureEntity;
import com.li.videoapplication.data.local.FileUtil;
import com.li.videoapplication.data.local.LPDSStorageUtil;
import com.li.videoapplication.data.network.UITask;
import com.li.videoapplication.framework.TBaseFragment;
import com.li.videoapplication.tools.AudioRecorder;
import com.li.videoapplication.tools.IntentHelper;
import com.li.videoapplication.tools.ToastHelper;
import com.li.videoapplication.ui.ActivityManager;
import com.li.videoapplication.ui.activity.VideoEditorActivity2;
import com.li.videoapplication.ui.dialog.LoadingDialog;
import com.li.videoapplication.utils.ScreenUtil;
import com.li.videoapplication.utils.ThreadUtil;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * 碎片：配音
 */
public class AudioFragment extends TBaseFragment {


    @BindView(R.id.audio_progress)
    ProgressBar progress;
    @BindView(R.id.audio_text)
    TextView text;
    @BindView(R.id.top)
    RelativeLayout top;
    @BindView(R.id.audio_original)
    TextView original;
    @BindView(R.id.audio_sound)
    ImageView sound;
    @BindView(R.id.audio_record)
    TextView record;
    Unbinder unbinder;
    private VideoEditorActivity2 activity;
    private AudioManager audioManager;
    private AudioRecorder audioRecorder;
    private VideoCaptureEntity entity;
    private EditorFragment editorFragment;

    public void onAttachedToWindow() {

        if (activity != null) {
            if (ScreenUtil.getAreaOneRatio(activity) - 1.7 < 0) {

                if (record != null) {
                    RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) record.getLayoutParams();
                    if (params != null) {
                        params.topMargin = ScreenUtil.dp2px(12);
                        record.setLayoutParams(params);
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
        audioManager = (AudioManager) activity.getSystemService(Context.AUDIO_SERVICE);
        audioRecorder = new AudioRecorder();

        onAttachedToWindow();
    }

    @Override
    protected int getCreateView() {
        return R.layout.fragment_audio;
    }

    @Override
    protected void initContentView(View view) {
        try {
            entity = (VideoCaptureEntity) getArguments().getSerializable("entity");
        } catch (Exception e) {
            e.printStackTrace();
        }

        unbinder = ButterKnife.bind(this, view);

        updateText(false);

        progress.setMax(100);
        progress.setProgress(0);

        isAudioMusic = true;
        updateSound(isAudioMusic);
        sound.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (activity != null && activity.isAudioRecording == true) {
                    ToastHelper.s(R.string.videoeditor_tip_audiorecording);
                    return;
                }
                if (isAudioMusic) {
                    isAudioMusic = false;
                } else {
                    isAudioMusic = true;
                }
                updateSound(isAudioMusic);
                Log.d(tag, "onClick: isAudioMusic=" + isAudioMusic);
            }
        });

        activity.isAudioRecording = false;
        updateRecord(false);
        record.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Log.d(tag, "onClick: ");
                if (activity != null && activity.isAudioRecording == false) {// 开始录音
                    SoundHelper.playSystem();
                    updateText(true);
                    updateRecord(true);
                    if (editorFragment != null)
                        editorFragment.resetTime();
                    if (activity != null) {
                        activity.setDrag(false);
                        activity.seekToVideo(0);
                        activity.playVideo();
                    }
                    audioManager.setStreamMute(AudioManager.STREAM_MUSIC, !isAudioMusic);
                    activity.isAudioRecording = true;
                    // 开始录音
                    startAudioRecord();
                    Log.d(tag, "onClick: startAudioRecord");
                } else {// 结束录音
                    SoundHelper.playSystem();
                    updateText(false);
                    updateProgress(0);
                    updateRecord(false);
                    audioManager.setStreamMute(AudioManager.STREAM_MUSIC, false);
                    activity.isAudioRecording = false;
                    // 停止录音
                    stopAudioRecord();
                    if (activity != null) {
                        activity.setDrag(true);
                        activity.pauseVideo();
                        activity.showProgressDialogCancelable(LoadingDialog.MUXING);
                    }
                    // 视频音频合成
                    UITask.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            muxVideoAndAudio();
                        }
                    }, 1200);
                    Log.d(tag, "onClick: stopAudioRecord");
                }
            }
        });

        onAttachedToWindow();
    }

    @Override
    protected IPullToRefresh getPullToRefresh() {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        resetAudio();
    }


    public void init(EditorFragment editorFragment) {
        this.editorFragment = editorFragment;
    }

    public void resetAudio() {
        if (audioManager != null)
            audioManager.setStreamMute(AudioManager.STREAM_MUSIC, false);
    }

    public void turnonAudio() {
        if (audioManager != null)
            audioManager.setStreamMute(AudioManager.STREAM_MUSIC, true);
    }

    public void performClickRecord() {
        if (record != null) {
            record.performClick();
            Log.d(tag, "performClickRecord: true");
        }
    }

    private String videoPath, audioPath, dstPath;
    private long videoDuration, audioDuration;

    /**
     * 开始录音
     */
    private void startAudioRecord() {
        if (audioRecorder != null && entity != null) {
            File file = LPDSStorageUtil.createAudioPath(entity.getVideo_path());
            if (file != null) {
                audioPath = file.getPath();
                audioRecorder.startAudioRecord(audioPath);
            }
        }
    }

    /**
     * 停止录音
     */
    private void stopAudioRecord() {
        if (audioRecorder != null) {
            audioRecorder.stopAudioRecord();
        }
    }

    /**
     * 视频音频合成
     */
    private void muxVideoAndAudio() {
        ThreadUtil.start(new Runnable() {
            @Override
            public void run() {
                videoPath = entity.getVideo_path();
                MediaMetadataRetriever retriever = new MediaMetadataRetriever();
                File dstFile = LPDSStorageUtil.createTmpRecPath();
                if (dstFile != null) {
                    long difference = 1000 * 1000;
                    try {
                        dstPath = dstFile.getPath();
                        String allTime;
                        retriever.setDataSource(videoPath);
                        allTime = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
                        videoDuration = Integer.valueOf(allTime);
                        retriever.setDataSource(audioPath);
                        allTime = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
                        audioDuration = Integer.valueOf(allTime);
                        difference = Math.abs(videoDuration - audioDuration);
                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                    }
                    Log.d(tag, "run: videoDuration=" + videoDuration);
                    Log.d(tag, "run: audioDuration=" + audioDuration);
                    Log.d(tag, "run: difference=" + difference);
                    if (videoDuration >= 10 * 1000 &&
                            audioDuration >= 10 * 1000 &&
                            difference <= 2 * 1000) {
                        // 视频音频合成
                        boolean flag = MP4parserHelper.muxVideoAndAudio(videoPath, audioPath, dstPath);

                        if (flag == true && FileUtil.isFile(dstPath)) {
                            VideoCaptureEntity a = EditorFragment.generateVideoCaptureEntity(dstPath);
                            if (a == null)
                                return;
                            if (a != null) {
                                showToastShort(R.string.audio_mux_tip_success);
                                getActivity().finish();
                                // 视频编辑
                                ActivityManager.startVideoEditorActivity_2(getActivity(), a);
                            }
                            // 通知系统扫描媒体文件
                            IntentHelper.scanFile();
                        } else {
                            showToastShort(R.string.audio_mux_tip_failed);
                        }
                    }
                }
                if (activity != null) {
                    activity.dismissProgressDialog();
                }
            }
        });
    }

    private boolean isAudioMusic = true;

    private void updateSound(boolean flag) {
        if (flag) {// 开始状态
            sound.setImageResource(R.drawable.setting_chk_blue);
        } else {// 停止状态
            sound.setImageResource(R.drawable.setting_chk_gray);
        }
    }

    private void updateText(boolean visibility) {
        if (visibility) {
            text.setVisibility(View.VISIBLE);
        } else {
            text.setVisibility(View.GONE);
        }
    }

    private void updateRecord(boolean state) {
        if (state) {// 开始状态
            record.setText(R.string.audio_recording_stop);
        } else {// 停止状态
            record.setText(R.string.audio_recording_start);
        }
    }

    public void updateProgress(int progress) {
        this.progress.setProgress(progress);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
