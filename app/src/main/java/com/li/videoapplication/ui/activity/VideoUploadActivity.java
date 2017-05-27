package com.li.videoapplication.ui.activity;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.os.Build;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.li.videoapplication.R;
import com.li.videoapplication.data.DataManager;
import com.li.videoapplication.data.database.VideoCaptureEntity;
import com.li.videoapplication.data.database.VideoCaptureManager;
import com.li.videoapplication.data.image.MyLocalVideoImageLoader;
import com.li.videoapplication.data.image.VideoTimeLoader;
import com.li.videoapplication.data.local.FileUtil;
import com.li.videoapplication.data.model.entity.Match;
import com.li.videoapplication.framework.TBaseActivity;
import com.li.videoapplication.tools.ToastHelper;
import com.li.videoapplication.ui.ActivityManager;

/**
 * 活动：活动视频上传
 */
@SuppressLint("HandlerLeak")
@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
public class VideoUploadActivity extends TBaseActivity implements OnClickListener,
        CompoundButton.OnCheckedChangeListener {

    private VideoCaptureEntity entity;
    private Match match;

    @Override
    public void refreshIntent() {
        super.refreshIntent();
        try {
            entity = (VideoCaptureEntity) getIntent().getSerializableExtra("entity");
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (entity == null)
            finish();
        try {
            match = (Match) getIntent().getSerializableExtra("match");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 文件大小， 视频图片，编辑
    private TextView size;
    private ImageView cover;
    private TextView edit;

    // 视频描述
    private EditText title;

    //描述
    public String getVideoTitle() {
        if (title.getText() == null)
            return "";
        return title.getText().toString().trim();
    }

    // 分享
    private TextView share;

    @Override
    public int getContentView() {
        return R.layout.activity_videoupload;
    }

    @Override
    public int inflateActionBar() {
        return R.layout.actionbar_second;
    }

    @Override
    public void afterOnCreate() {
        super.afterOnCreate();
        setSystemBarBackgroundWhite();
        setAbTitle("参与活动");
    }

    @Override
    public void initView() {
        super.initView();
        initContentView();
        refreshVideoData();
        refreshContentView();
    }

    @Override
    public void loadData() {
        super.loadData();

    }

    protected void onDestroy() {
        super.onDestroy();
        if (bitmap != null && !bitmap.isRecycled())
            bitmap.recycle();
    }

    private void initContentView() {
        size = (TextView) findViewById(R.id.videoupload_size);
        cover = (ImageView) findViewById(R.id.videoupload_cover);
        edit = (TextView) findViewById(R.id.videoupload_edit);
        title = (EditText) findViewById(R.id.videoupload_title);
        share = (TextView) findViewById(R.id.videoupload_upload);

        edit.setOnClickListener(this);
        share.setOnClickListener(this);
    }

    private boolean isChecked;

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        this.isChecked = isChecked;
    }

    private long video_length;
    private String filePath;
    private String fileSize;
    private Bitmap bitmap;

    private void refreshVideoData() {
        filePath = entity.getVideo_path();
        Long fileLength = FileUtil.getFileSize(filePath);
        fileSize = FileUtil.formatFileSize(fileLength);
        bitmap = MyLocalVideoImageLoader.getBitmapFromVideo2(filePath);
        try {
            VideoTimeLoader loader = new VideoTimeLoader(this);
            String video_size = loader.getDuration(filePath);
            video_length = Long.valueOf(video_size);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void refreshContentView() {
        if (bitmap != null)
            cover.setImageBitmap(bitmap);
        // 视频大小
        size.setText(fileSize);
    }

    @Override
    public void onClick(View v) {

        if (v == edit) {// 编辑视频
            if (video_length > 30000) {
                ActivityManager.startVideoEditorActivity(this, entity);
            } else {
                showToastShort("30秒以上的时长才可以编辑");
            }
        } else if (v == share) {
            updateDatabase();
            uploadNow();
        }
    }


    /**
     * 将分享页面资料存入数据库
     */
    private void updateDatabase() {
        VideoCaptureManager.updateNamePathByPath(entity.getVideo_path(),
                entity.getVideo_name(),
                null);
    }

    /**
     * 对即将上传的文件进行视频标题非空判断
     */
    private void uploadNow() {

        if (isChecked && getVideoTitle().length() < 1) {
            showToastShort("请填写标题后再提交");
            return;
        }
        DataManager.UPLOAD.startVideo("SYSJ", getMember_id(), getVideoTitle(),
                match.getGame_id(), match.getMatch_id(), "", 0, null, entity, null);

        ToastHelper.s("正在上传...");
        finish();
    }
}