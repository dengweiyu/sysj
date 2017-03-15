package com.li.videoapplication.ui.activity;


import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.li.videoapplication.R;
import com.li.videoapplication.data.DataManager;
import com.li.videoapplication.data.database.VideoCaptureEntity;
import com.li.videoapplication.data.database.VideoCaptureManager;
import com.li.videoapplication.data.database.VideoCaptureResponseObject;
import com.li.videoapplication.data.image.VideoDuration;
import com.li.videoapplication.data.local.FileUtil;
import com.li.videoapplication.data.model.entity.Game;
import com.li.videoapplication.data.model.entity.Match;
import com.li.videoapplication.data.preferences.VideoPreferences;
import com.li.videoapplication.framework.TBaseAppCompatActivity;
import com.li.videoapplication.tools.UmengAnalyticsHelper;
import com.li.videoapplication.ui.ActivityManeger;
import com.li.videoapplication.ui.adapter.ChooseLocalVideoAdapter;
import com.li.videoapplication.ui.adapter.MyImportVideoAdapter;
import com.li.videoapplication.tools.ToastHelper;
import com.li.videoapplication.utils.LogHelper;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import me.everything.android.ui.overscroll.OverScrollDecoratorHelper;

/**
 * 活动：活动视频上传 选择
 */
public class VideoChooseActivity extends TBaseAppCompatActivity implements OnClickListener {


    /**
     * 跳转：参加活动--视频上传
     */
    private void startVideoUploadActivity(VideoCaptureEntity entity) {
        ActivityManeger.startVideoUploadActivity(this, entity, match);
    }

    /**
     * 跳转：视频分享
     */
    private void startVideoShareActivity210(VideoCaptureEntity record) {
        ActivityManeger.startVideoShareActivity210(this, record, game, to);
    }

    private RecyclerView recyclerView;
    private ChooseLocalVideoAdapter adapter;
    private List<VideoCaptureEntity> data = new ArrayList<>();
    private TextView tipEmpty;

    /**
     * 已选择，将要导入的文件列表
     */
    private List<VideoCaptureEntity> myImportData = new ArrayList<>();
    private int pageIndex = 0;
    private Match match;
    private Game game;
    private int to;

    @Override
    public void refreshIntent() {
        super.refreshIntent();
        try {
            match = (Match) getIntent().getSerializableExtra("match");
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            game = (Game) getIntent().getSerializableExtra("game");
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            to = getIntent().getIntExtra("to", VideoShareActivity.TO_VIDEOMANAGER);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getContentView() {
        return R.layout.activity_videochoose;
    }

    @Override
    public void afterOnCreate() {
        super.afterOnCreate();
        setSystemBarBackgroundWhite();
    }

    @Override
    public void initView() {
        super.initView();
        initToolbar();
        initContentView();
    }

    private void initToolbar() {
        findViewById(R.id.tb_back).setOnClickListener(this);
        findViewById(R.id.tb_import).setOnClickListener(this);
    }

    @Override
    public void loadData() {
        super.loadData();
        // 验证并加载本地视频
        DataManager.LOCAL.checkVideoCaptures();
    }

    private void initContentView() {
        tipEmpty = (TextView) findViewById(R.id.videochoose_empty);
        tipEmpty.setVisibility(View.GONE);

        findViewById(R.id.videoupload_ok).setOnClickListener(this);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        OverScrollDecoratorHelper.setUpOverScroll(recyclerView, OverScrollDecoratorHelper.ORIENTATION_VERTICAL);

        adapter = new ChooseLocalVideoAdapter(this, data, recyclerView);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tb_back:
                finish();
                break;
            case R.id.tb_import:
                // 导入外部视频
                DataManager.LOCAL.importVideoCaptures();
                break;
            case R.id.videoupload_ok:
                UmengAnalyticsHelper.onEvent(this, UmengAnalyticsHelper.MAIN, "发布-视频-选择视频后点击确认按钮进入分享页面次数");
                Map<Integer, Boolean> map = adapter.getSelecteMap();
                if (map.containsValue(true)) {
                    for (Map.Entry<Integer, Boolean> m : map.entrySet()) {
                        if (m.getValue()) {
                            Integer key = m.getKey();
                            VideoCaptureEntity entity = data.get(key);

                            // 10秒以上，30分钟以内，800M以内
                            if (share(entity.getVideo_path())) {
                                if (match != null) { //活动上传视频
                                    startVideoUploadActivity(entity);
                                } else { //首页上传视频
                                    startVideoShareActivity210(entity);
                                }
                                finish();
                            }
                        }
                    }
                } else {
                    ToastHelper.s("请选择视频");
                }
                break;
        }
    }

    /**
     * 检查视频信息
     */
    public static boolean share(String filePath) {
        // 检查文件是否存在
        File file = new File(filePath);
        if (!file.exists()) {
            ToastHelper.s("视频文件不存在");
            // 删除该视频
            VideoCaptureManager.deleteByPath(filePath);
            return false;
        }
        String duration;
        try {
            duration = VideoDuration.getDuration(filePath);
        } catch (Exception e) {
            e.printStackTrace();
            ToastHelper.s("视频存在错误，不能分享");
            return false;
        }
        // 获取视频长度（毫秒）
        long secs = 0;
        try {
            secs = Integer.valueOf(duration);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        if (secs < 10 * 1000) {// 10 000
            ToastHelper.s("视频长度不能少于10秒");
            return false;
        } else if (secs > 30 * 60 * 1000) {// 1800 000
            ToastHelper.s("视频长度不能大于30分钟");
            return false;
        }
        // 计算视频文件的大小
        long fileLong = FileUtil.getFileSize(filePath);
        if ((fileLong / 1048576) > 800) {
            ToastHelper.s("视频长度不能大于800M");
            return false;
        }
        return true;
    }

    /**
     * 回调：加载本地视频
     */
    public void onEventMainThread(VideoCaptureResponseObject event) {

        if (event.getResultCode() == VideoCaptureResponseObject.RESULT_CODE_LOADING
                || event.getResultCode() == VideoCaptureResponseObject.RESULT_CODE_CHECKING) {

            if (event.getData() != null && event.getData().size() > 0) {
                if (pageIndex == 0) {
                    data.clear();
                }
                Log.d(tag, "List<VideoCaptureEntity> : " + event.getData());
                data.addAll(event.getData());
                adapter.notifyDataChanged();
                refreshContentView();
                ++pageIndex;
            }
        } else if (event.getResultCode() == VideoCaptureResponseObject.RESULT_CODE_IMPORTING) {
            if (event.getData() != null && event.getData().size() > 0) {
                if (event.getData() != null && event.getData().size() > 0) {
                    List<VideoCaptureEntity> list = event.getData();
                    showImportDialog(list);
                } else {
                    ToastHelper.s("本地没有相关视频");
                }
            }
        }
    }

    public List<VideoCaptureEntity> getMyImportData() {
        return myImportData;
    }

    /**
     * 导入视频对话框
     */
    private void showImportDialog(List<VideoCaptureEntity> list) {
        myImportData.clear();

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("导入外部视频");
        final MyImportVideoAdapter adapter = new MyImportVideoAdapter(this, list, this);
        builder.setAdapter(adapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {

            }
        });
        builder.setPositiveButton("导入", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                LogHelper.i(tag, "import=" + myImportData);
                for (int i = 0; i < myImportData.size(); i++) {
                    LogHelper.i(tag, "import=" + 1);
                    VideoCaptureManager.save(myImportData.get(i).getVideo_path(),
                            VideoCaptureEntity.VIDEO_SOURCE_EXT,
                            VideoCaptureEntity.VIDEO_STATION_LOCAL);
                    // 用来记录视频是否已经导入
                    VideoPreferences.getInstance().putBoolean(myImportData.get(i).getVideo_path(), true);
                }
                pageIndex = 0;
                // 验证并加载本地视频
                DataManager.LOCAL.checkVideoCaptures(10, pageIndex);
            }
        });
        builder.show();
    }

    private void refreshContentView() {
        if (data != null && data.size() > 0) {
            tipEmpty.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        } else {
            tipEmpty.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }
    }

}
