package com.li.videoapplication.ui.activity;

import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.GridView;

import com.li.videoapplication.R;
import com.li.videoapplication.data.DataManager;
import com.li.videoapplication.data.local.ScreenShotEntity;
import com.li.videoapplication.data.model.entity.Match;
import com.li.videoapplication.data.model.event.ImageView2ImageShareEvent;
import com.li.videoapplication.data.model.response.SaveEventResult204Entity;
import com.li.videoapplication.data.preferences.PreferencesHepler;
import com.li.videoapplication.data.upload.Contants;
import com.li.videoapplication.data.upload.ImageShareResponseObject;
import com.li.videoapplication.framework.AppManager;
import com.li.videoapplication.framework.TBaseActivity;
import com.li.videoapplication.tools.BitmapHelper;
import com.li.videoapplication.tools.TimeHelper;
import com.li.videoapplication.ui.ActivityManeger;
import com.li.videoapplication.ui.adapter.ResultImageUploadAdapter;
import com.li.videoapplication.utils.TextUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


/**
 * 活动：上传比赛结果截图
 */
public class UploadMatchResultImageActivity extends TBaseActivity implements View.OnClickListener {

    private GridView gridView;
    private List<ScreenShotEntity> data = new ArrayList<>();
    private ResultImageUploadAdapter adapter;
    private String pk_id, team_id;
    private Match matchDetailMatch;
    private String is_last, customer_service, name, over_time;
    private File tempImage;

    @Override
    public void refreshIntent() {
        super.refreshIntent();
        try {
            matchDetailMatch = (Match) getIntent().getSerializableExtra("match");
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            pk_id = getIntent().getStringExtra("pk_id");
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            team_id = getIntent().getStringExtra("team_id");
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            over_time = getIntent().getStringExtra("over_time");
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            is_last = getIntent().getStringExtra("is_last");
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            customer_service = getIntent().getStringExtra("customer_service");
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            name = getIntent().getStringExtra("name");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getContentView() {
        return R.layout.activity_uploadmatchresultimage;
    }

    @Override
    public int inflateActionBar() {
        return R.layout.actionbar_second;
    }

    @Override
    public void afterOnCreate() {
        super.afterOnCreate();

        setSystemBarBackgroundWhite();
        setAbTitle("提交凭证");
    }

    @Override
    public void initView() {
        super.initView();
        gridView = (GridView) findViewById(R.id.gridview);
        findViewById(R.id.upload).setOnClickListener(this);
    }

    @Override
    public void loadData() {
        super.loadData();
        ImageViewActivity.imageViewDeleteData.clear();

        ScreenShotEntity imageInfo = new ScreenShotEntity();
        data.add(imageInfo);

        adapter = new ResultImageUploadAdapter(this, data, R.layout.adapter_imageupload);
        gridView.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.upload:
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

    /**
     * 上传图文到服务器
     */
    private void uploadImage() {
        compressBitmap();//压缩图片（最大500k）
        List<ScreenShotEntity> list = new ArrayList<>();
        list.addAll(data);
        list.remove(list.size() - 1);

        // 上传图片服务
        DataManager.UPLOAD.uploadImage204(pk_id, getMember_id(), list, team_id);
    }

    //压缩图片（最大500k）
    private void compressBitmap() {
        try {
            for (int i = 0; i < data.size() - 1; i++) {
                Log.d(tag, "uploadImage: origin ImagePath == " + data.get(i).getPath());
                String tempImagePath = BitmapHelper.compressBitmap(data.get(i).getPath(), 500.00);
                tempImage = new File(tempImagePath);
                Log.d(tag, "uploadImage: temp ImagePath == " + tempImagePath);
                data.get(i).setPath(tempImagePath);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
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
            if (tempImage.exists()) {
                boolean delete = tempImage.delete();
                Log.d(tag, "uploadImage: tempImage.delete(): " + delete);
            }
            if (result) {
                dismissProgressDialog();
            } else
                showToastLong(msg);
        }
    }

    /**
     * 回调：上传图片
     */
    public void onEventMainThread(SaveEventResult204Entity event) {

        if (event != null) {
            if (event.isResult() && event.getData() != null) {
                if (is_last.equals("0")) {//非最后一轮
                    try {
                        over_time = TimeHelper.getMMddHHmmTimeFormat2(over_time);//比赛结束时间
                        String content = "你提交的截图正在审核，请在 " + over_time + " 分来查看比赛结果。";
                        showSuccessDialogWithListener("截图上传成功", TextUtil.dateAtRed(content), "确认");
                    } catch (Exception e) {
                        e.printStackTrace();
                        String content = "你提交的截图正在审核，请前往我的赛程页面查看结果，请获胜者留意下一轮比赛时间。";
                        showSuccessDialogWithListener("截图上传成功", TextUtil.stringAtRed(content, 14, 18), "确认");
                    }
                } else {//最后一轮
                    String content = "你提交的截图正在审核，请前往我的赛程页面查看结果。";
                    showSuccessDialogWithListener("截图上传成功", TextUtil.stringAtRed(content, 14, 18), "确认");
                }

//                if (over_time != null && !over_time.equals("")) {
//                    String content = "比赛结果于" + over_time + "揭晓，请前往赛程页面查看结果，请获胜者准时参加下轮比赛";
//                    showSuccessDialogWithListener("截图上传成功", TextUtil.dateAtRed(content), "确认");
//                } else {
//                    showSuccessDialogWithListener("截图上传成功", "比赛结果将尽快揭晓，请前往赛程页面查看结果，请获胜者准时参加下轮比赛", "确认");
//                }
            }
        }
    }

    //对话框点击确认后的事件
    @Override
    protected void confirmButtonEvent() {
        AppManager.getInstance().removeActivity(MyMatchProcessActivity.class);
        ActivityManeger.startMyMatchProcessActivityNewTask(this, matchDetailMatch, customer_service, name);
        finish();
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
        adapter.notifyDataSetChanged();
    }
}
