package com.li.videoapplication.ui.activity;

import android.annotation.SuppressLint;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.GridView;

import com.li.videoapplication.R;
import com.li.videoapplication.data.DataManager;
import com.li.videoapplication.data.local.ScreenShotEntity;
import com.li.videoapplication.data.model.event.ImageView2ImageShareEvent;
import com.li.videoapplication.data.upload.Contants;
import com.li.videoapplication.data.upload.ImageShareResponseObject;
import com.li.videoapplication.framework.TBaseActivity;
import com.li.videoapplication.ui.ActivityManeger;
import com.li.videoapplication.ui.adapter.ImageShareAdapter;

import java.util.ArrayList;
import java.util.List;

import cn.sharesdk.framework.ShareSDK;

/**
 * 活动：活动页面上传图文
 */
@SuppressLint("HandlerLeak")
public class ActivityImageUploadActivity extends TBaseActivity implements
        OnClickListener {
    public static final String TAG = ActivityImageUploadActivity.class.getSimpleName();

    private String match_id;
    private boolean isFirstIn = true;

    @Override
    public void refreshIntent() {
        try {
            match_id = getIntent().getStringExtra("match_id");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private GridView gridView;
    private ImageShareAdapter adapter;
    private List<ScreenShotEntity> data = new ArrayList<>();

    private EditText title;

    public String getDescription() {
        if (title.getText() == null)
            return "";
        return title.getText().toString().trim();
    }

    @Override
    public int getContentView() {
        return R.layout.activity_activityimageupload;
    }

    @Override
    public int inflateActionBar() {
        return R.layout.actionbar_second;
    }

    @Override
    public void afterOnCreate() {
        super.afterOnCreate();
        ShareSDK.initSDK(this);

        setSystemBarBackgroundWhite();
        setAbTitle("参与活动");
    }

    @Override
    public void initView() {
        super.initView();

        gridView = (GridView) findViewById(R.id.activityimageupload_gridview);
        title = (EditText) findViewById(R.id.activityimageupload_title);
        findViewById(R.id.activityimageupload_upload).setOnClickListener(this);
    }

    @Override
    public void loadData() {
        super.loadData();

        ImageViewActivity.imageViewDeleteData.clear();

        ScreenShotEntity imageInfo = new ScreenShotEntity();
        data.add(imageInfo);

        adapter = new ImageShareAdapter(this, data, R.layout.adapter_imageshare);
        gridView.setAdapter(adapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (isFirstIn) {
            ActivityManeger.startImageViewActivity(this);
            isFirstIn = false;
        }
    }

    @SuppressLint("InlinedApi")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.activityimageupload_upload:// 立即分享
                if (!isLogin()) {
                    showToastLogin();
                    return;
                }
                if (ImageViewActivity.imageViewDeleteData.size() < 1) {
                    showToastShort("请选择要上传的图片");
                    return;
                }
                uploadImage();
                break;
        }
    }

    /***
     * 上传图文到服务器
     */
    private void uploadImage() {
        String title = this.title.getText().toString();

        List<ScreenShotEntity> list = new ArrayList<>();
        list.addAll(data);
        list.remove(list.size() - 1);

        // 上传图片服务
        DataManager.UPLOAD.uploadImage208(match_id, getMember_id(), title, list);
    }

    public static final int STATUS_START = Contants.STATUS_START;

    public static final int STATUS_PREPARING = Contants.STATUS_PREPARING;

    public static final int STATUS_UPLOADING = Contants.STATUS_UPLOADING;

    public static final int STATUS_COMPLETING = Contants.STATUS_COMPLETING;

    public static final int STATUS_END = Contants.STATUS_END;

    /**
     * 回调：上传图片
     */
    public void onEventMainThread(ImageShareResponseObject event) {
        int status = event.getStatus();
        String msg = event.getMsg();
        boolean result = event.isResult();
        if (status == STATUS_START) {
            showProgressDialog2("上传开始");
        } else if (status == STATUS_PREPARING) {
            setProgressText("正在上传");
        } else if (status == STATUS_UPLOADING) {
            setProgressText("正在上传");
        } else if (status == STATUS_COMPLETING) {
            setProgressText("正在上传");
        } else if (status == STATUS_END) {
            dismissProgressDialog();
            if (result) {
                dismissProgressDialog();
                showToastLong("上传成功，请耐心等待审核");
//                AppManager.getInstance().removeActivity(GroupDetailActivity.class);
//                ActivityManeger.startGroupDetailActivityNewTask(this, game);
                finish();
            } else
                showToastLong(msg);
        }
    }


    /**
     * 组件间的通讯：图片选择
     */
    public void onEventMainThread(ImageView2ImageShareEvent event) {

        List<ScreenShotEntity> list = new ArrayList<>();
        for (String imageUrl : ImageViewActivity.imageViewDeleteData) {
            ScreenShotEntity imageInfo = new ScreenShotEntity();
            imageInfo.setPath(imageUrl);
            list.add(imageInfo);
        }
        ScreenShotEntity entity = new ScreenShotEntity();
        list.add(entity);

        data.clear();
        data.addAll(list);

        adapter = new ImageShareAdapter(this, data, R.layout.adapter_imageshare);
        gridView.setAdapter(adapter);
    }
}
