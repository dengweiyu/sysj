package com.li.videoapplication.ui.activity;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.MediaMetadataRetriever;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;

import com.li.videoapplication.R;
import com.li.videoapplication.data.EventManager;
import com.li.videoapplication.data.LocalManager;
import com.li.videoapplication.data.database.VideoCaptureEntity;
import com.li.videoapplication.data.database.VideoCaptureManager;
import com.li.videoapplication.data.local.FileUtil;
import com.li.videoapplication.data.local.LPDSStorageUtil;
import com.li.videoapplication.framework.TBaseActivity;
import com.li.videoapplication.tools.SRTHelper;
import com.li.videoapplication.tools.ToastHelper;
import com.li.videoapplication.ui.dialog.LoadingDialog;
import com.li.videoapplication.ui.fragment.AudioFragment;
import com.li.videoapplication.ui.fragment.CoverFragment;
import com.li.videoapplication.ui.fragment.EditorFragment;
import com.li.videoapplication.ui.fragment.SubtitleFragment;
import com.li.videoapplication.ui.view.BackgroundView;
import com.li.videoapplication.ui.view.BottomView;
import com.li.videoapplication.ui.view.PlayingView;
import com.li.videoapplication.ui.view.VideoEditorActionBar;
import com.li.videoapplication.ui.view.VideoEditorMenu;
import com.li.videoapplication.utils.BitmapUtil;
import com.li.videoapplication.utils.ThreadUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 *
 */

public class VideoEditorActivity2 extends TBaseActivity {


    @BindView(R.id.videoEditorActionBar)
    VideoEditorActionBar videoEditorActionBar;
    @BindView(R.id.playingView)
    public  PlayingView playingView;
    @BindView(R.id.videoEditorMenu)
    VideoEditorMenu videoEditorMenu;
    @BindView(R.id.backgroundView)
    BackgroundView backgroundView;
    @BindView(R.id.bottomView)
    BottomView bottomView;

    private VideoCaptureEntity entity;
    private String extName;
    public Handler handler = new Handler();

    public boolean isAudioRecording, isAddingSubtitle = false;

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();

        if (editorFragment != null)
            editorFragment.onAttachedToWindow();
        if (audioFragment != null)
            audioFragment.onAttachedToWindow();
    }

    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();

        if (editorFragment != null)
            editorFragment.onDetachedFromWindow();
        if (audioFragment != null)
            audioFragment.onDetachedFromWindow();
    }

    @Override
    public void refreshIntent() {
        super.refreshIntent();
        try {
            entity = (VideoCaptureEntity) getIntent().getSerializableExtra("entity");
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (entity == null)
            try {
                finish();
            } catch (Exception e) {
                e.printStackTrace();
            }
        File file = null;
        try {
            file = new File(entity.getVideo_path());
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (file != null)
            extName = FileUtil.getExtName(file.getName());
        Log.d(tag, "refreshIntent: entity=" + entity);
        Log.d(tag, "refreshIntent: extName=" + extName);
    }

    private List<Fragment> fragments = new ArrayList<>();
    private EditorFragment editorFragment;
    public SubtitleFragment subtitleFragment;
    private AudioFragment audioFragment;
    private CoverFragment coverFragment;

    private int currIndex;
    private Fragment context;

    public long getLeftTime() {
        if (editorFragment == null)
            throw new NullPointerException("EditorFragment is null");
        if (editorFragment.leftTime < 0 || editorFragment.leftTime > duration)
            return 0l;
        return editorFragment.leftTime;
    }

    public long getRightTime() {
        if (editorFragment == null)
            throw new NullPointerException("EditorFragment is null");
        if (editorFragment.rightTime < 0 || editorFragment.rightTime > duration)
            return duration;
        return editorFragment.rightTime;
    }

    @Override
    public int getContentView() {
        return R.layout.activity_videoeditor;
    }

    @Override
    public void beforeOnCreate() {
        super.beforeOnCreate();
        setSystemBar(true);
    }

    @Override
    public void afterOnCreate() {
        super.afterOnCreate();

        setSystemBarBackgroundColor(Color.BLACK);
    }

    @Override
    public void initView() {
        super.initView();

        showProgressDialogCancelable("视频加载中...");

        playingView.init(this, entity);

        //关闭自动播放
        playingView.setAutoPlay(false);
        //关闭循环播放
        playingView.setLooping(false);
        bottomView.init(this, new BottomView.EditModelable() {

            @Override
            public void editModel(boolean isAdvanced) {
                videoEditorMenu.toogleMenu(!isAdvanced);
                videoEditorMenu.performClick(0);
            }
        });


        videoEditorActionBar.setSaveListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (isAudioRecording == true) {
                    ToastHelper.s(R.string.videoeditor_tip_audiorecording);
                    return;
                }
                if (isAddingSubtitle == true &&
                        subtitleFragment != null) {
                    subtitleFragment.cancelSubtitle();

                    return;
                }
                showProgressDialogCancelable(LoadingDialog.SAVING);
                ThreadUtil.start(new Runnable() {
                    @Override
                    public void run() {
                        saveRecource();
                    }
                });
            }
        });

        videoEditorActionBar.setCancelListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        Bundle bundle = new Bundle();
        bundle.putSerializable("entity", entity);
        editorFragment = new EditorFragment();
        coverFragment = new CoverFragment();
        subtitleFragment = new SubtitleFragment();
        audioFragment = new AudioFragment();

        editorFragment.setArguments(bundle);
        coverFragment.setArguments(bundle);
        subtitleFragment.setArguments(bundle);
        audioFragment.setArguments(bundle);

        fragments.add(editorFragment);
        fragments.add(coverFragment);
        fragments.add(subtitleFragment);
        fragments.add(audioFragment);

        audioFragment.init(editorFragment);
        playingView.init(audioFragment);

        videoEditorMenu.init(this);
        videoEditorMenu.setCallback(new VideoEditorMenu.Callback() {

            @Override
            public void onClick(int index) {

                switchContent(context, fragments.get(index));
                currIndex = index;
            }
        });

        videoEditorMenu.switchTab(2);
        context = fragments.get(2);
        currIndex = 2;
        replaceFragment(fragments.get(2));

        switchContent(context, fragments.get(0));
        currIndex = 0;

        bottomView.performClickButton();
    }

    @Override
    public void loadData() {
        super.loadData();

        refreshInfoAsync();

        File file = LPDSStorageUtil.createSubtitlePath(entity.getVideo_path());
        if (file != null) {
            addSubtitle(file);
        }
    }

    /**
     * 保存资源
     */
    private void saveRecource() {
        if (entity.getVideo_source().equals(VideoCaptureEntity.VIDEO_SOURCE_REC) ||
                entity.getVideo_source().equals(VideoCaptureEntity.VIDEO_SOURCE_CUT) ||
                entity.getVideo_source().equals(VideoCaptureEntity.VIDEO_SOURCE_EXT)) {// 原来的视频
            // 保存封面
            if (coverFragment != null &&
                    coverFragment.isOnTouch()) {
                saveCover();
            }

            // 保存字幕
            saveSubtitle();

            // 加载本地视频
            loadVideoCaptures();
            Log.d(tag, "saveRecource: true");
        } else {// 缓存的视频
            // 剪辑合成
            File dstFile = LPDSStorageUtil.createRecPath();
            if (dstFile != null) {
                // 移动文件
                boolean flag = FileUtil.moveFile(entity.getVideo_path(), dstFile.getPath());
                if (flag == true) {
                    entity.setVideo_path(dstFile.getPath());
                    // 保存
                    VideoCaptureManager.save(entity.getVideo_path(),
                            VideoCaptureEntity.VIDEO_SOURCE_CUT,
                            VideoCaptureEntity.VIDEO_STATION_LOCAL);

                    // 保存封面
                    if (coverFragment != null &&
                            coverFragment.isOnTouch()) {
                        saveCover();
                    }

                    // 保存字幕
                    saveSubtitle();

                    // 加载本地视频
                    loadVideoCaptures();
                    Log.d(tag, "saveRecource: true");
                }
            }
        }
        dismissProgressDialog();
        // 本地视频
        EventManager.postVideoEditor2VideoManagerEvent(new String[]{entity.getVideo_path()}, 1);
        if (audioFragment != null) {
            audioFragment.turnonAudio();
        }
        Log.d(tag, "saveRecource: true");
    }

    /**
     * 保存封面
     */
    private void saveCover() {
        Log.d(tag, "saveCover: ");
        if (coverBitmap != null) {
            File coverFile = LPDSStorageUtil.createCoverPath(entity.getVideo_path());

            if (coverFile != null) {
                BitmapUtil.saveBitmap(coverBitmap, coverFile.getPath());
            }


        }
    }

    /**
     * 保存字幕
     */
    private void saveSubtitle() {
        File srtFile = LPDSStorageUtil.createSubtitlePath(entity.getVideo_path());
        if (subtitleFragment != null && subtitleFragment.info != null && srtFile != null) {
            SRTHelper.saveSRT(srtFile.getPath(), subtitleFragment.info);
        }
    }

    /**
     * 加载本地视频
     */
    private void loadVideoCaptures() {
        LocalManager.loadVideoCaptures();
        handler.post(new Runnable() {
            @Override
            public void run() {

                onBackPressed();
            }
        });
    }

    @OnClick(R.id.ab_videoeditor_cancel)
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ab_videoeditor_cancel:
                finish();
                break;
        }
    }

    public void replaceFragment(Fragment target) {
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.videoeditor_container, target).commit();
    }

    public void switchContent(Fragment from, Fragment to) {
        if (context != to) {
            context = to;
            FragmentTransaction transaction = manager.beginTransaction();
            // .setCustomAnimations(R.anim.fade_in, R.anim.activity_hold);
            if (!to.isAdded()) {
                transaction.hide(from).add(R.id.videoeditor_container, to).commit();
            } else {
                transaction.hide(from).show(to).commit();
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        if (playingView != null) {
            playingView.resumeRescouce();
        }
    }

    @Override
    public void onPause() {
        super.onPause();

        if (playingView != null) {
            playingView.pauseRescouce();
        }
    }

    @Override
    protected void onDestroy() {
        dismissProgressDialog();
        super.onDestroy();

        if (playingView != null) {
            playingView.destroyRescouce();
        }
        recleAllBitmap();
    }

    @Override
    public void onBackPressed() {
        if (isAudioRecording == true) {
            ToastHelper.s(R.string.videoeditor_tip_audiorecording);
            return;
        }
        if (isAddingSubtitle == true) {
            if (subtitleFragment != null) {
                subtitleFragment.cancelSubtitle();
            }
            return;
        }
        super.onBackPressed();
    }

    public void playVideo() {
        if (playingView != null) {
            playingView.playVideo();
        }
    }

    public void pauseVideo() {
        if (playingView != null) {
            playingView.pauseVideo();
        }
    }

    public void seekToVideo(long position) {
        if (playingView != null) {
            playingView.seekToPlayer(position);
        }
    }

    public void setDrag(boolean drag) {
        if (playingView != null) {
            playingView.setDrag(drag);
        }
    }

    /**
     * 加载字幕
     */
    public void addSubtitle(File file) {
        Log.d(tag, "addSubtitle: ");
        if (file != null)
            playingView.addSubtitle(file);
    }

    private MediaMetadataRetriever retriever = new MediaMetadataRetriever();
    private Bitmap firstBitmap, secondBitmap, thirdBitmap, fourthBitmap, coverBitmap;
    private long duration;

    public void refreshInfoAsync() {
        ThreadUtil.start(new Runnable() {
            @Override
            public void run() {
                loadCover();
                try {
                    refreshMedia(entity.getVideo_path());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (coverBitmap == null)
                    try {
                        refreshCover(entity.getVideo_path(), 0);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (playingView != null) {

                            playingView.updatePlayer(4);
                            playingView.updateDuration(duration);
                            playingView.loadCover(coverBitmap);

                        }
                        if (backgroundView != null)
                            backgroundView.displayImage(firstBitmap, secondBitmap, thirdBitmap, fourthBitmap);

                        if (editorFragment != null)
                            editorFragment.updateDuration(duration);
                        if (coverFragment != null)
                            coverFragment.updateDuration(duration);
                        if (subtitleFragment != null)
                            subtitleFragment.updateDuration(duration);
                        dismissProgressDialog();
                    }
                });

            }
        });
    }

    public void refreshCoverAsync(final long position) {
        ThreadUtil.start(new Runnable() {
            @Override
            public void run() {
                try {
                    refreshCover(entity.getVideo_path(), position);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (playingView != null) {
                            playingView.loadCover(coverBitmap);
                        }
                    }
                });
            }
        });
    }

    private void refreshMedia(String path) throws Exception {
        Log.d(tag, "refreshMedia: ");
        retriever.setDataSource(path);
        String allTime = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
        duration = Integer.valueOf(allTime);
        firstBitmap = retriever.getFrameAtTime((duration / 5) * 1 * 1000);
        secondBitmap = retriever.getFrameAtTime((duration / 5) * 2 * 1000);
        thirdBitmap = retriever.getFrameAtTime((duration / 5) * 3 * 1000);
        fourthBitmap = retriever.getFrameAtTime((duration / 5) * 4 * 1000);
    }

    private void loadCover() {
        Log.d(tag, "loadCover: ");
        File coverFile = LPDSStorageUtil.createCoverPath(entity.getVideo_path());
        if (coverFile != null && FileUtil.isFile(coverFile.getPath())) {
            coverBitmap = BitmapUtil.readLocalBitmap(coverFile.getPath());
        }
    }

    private void refreshCover(String path, long position) throws Exception {
        Log.d(tag, "refreshCover: ");
        Log.d(tag, "refreshCover: position=" + position);
        retriever = new MediaMetadataRetriever();
        retriever.setDataSource(path);
        coverBitmap = retriever.getFrameAtTime(position * 1000);
        Log.d(tag, "refreshCover: true");
    }

    private void recleAllBitmap() {
        recleBitmap(firstBitmap);
        recleBitmap(secondBitmap);
        recleBitmap(thirdBitmap);
        recleBitmap(fourthBitmap);
    }

    private void recleBitmap(Bitmap bitmap) {
        if (bitmap != null && !bitmap.isRecycled()) {
            Log.d(tag, "recleBitmap: ");
            bitmap.recycle();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
