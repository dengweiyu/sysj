package com.li.videoapplication.ui.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.li.videoapplication.R;
import com.li.videoapplication.data.DataManager;
import com.li.videoapplication.data.local.ScreenShotEntity;
import com.li.videoapplication.data.model.entity.Game;
import com.li.videoapplication.data.model.event.ImageView2ImageShareEvent;
import com.li.videoapplication.data.model.event.SearchGame2VideoShareEvent;
import com.li.videoapplication.data.preferences.PreferencesHepler;
import com.li.videoapplication.data.upload.Contants;
import com.li.videoapplication.data.upload.ImageShareResponseObject;
import com.li.videoapplication.framework.AppManager;
import com.li.videoapplication.framework.TBaseActivity;
import com.li.videoapplication.tools.BitmapHelper;
import com.li.videoapplication.tools.BitmapLoader;
import com.li.videoapplication.ui.ActivityManeger;
import com.li.videoapplication.ui.adapter.ResultImageUploadAdapter;
import com.li.videoapplication.ui.popupwindows.MoreTypePopupWindow;
import com.li.videoapplication.tools.ToastHelper;
import com.li.videoapplication.utils.InputUtil;
import com.li.videoapplication.utils.NetUtil;
import com.li.videoapplication.utils.StringUtil;
import com.li.videoapplication.views.GridViewY1;
import com.li.videoapplication.views.SmoothCheckBox;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cn.sharesdk.framework.ShareSDK;

/**
 * 活动：图文分享
 */
@SuppressLint("HandlerLeak")
public class HomeImageShareActivity extends TBaseActivity implements
        OnClickListener {

    @Override
    public void refreshIntent() {
        Intent intent = getIntent();
        try {
            game = (Game) intent.getSerializableExtra("game");
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            isHideLife = intent.getBooleanExtra("isHideLife", false);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private Game game;
    private boolean isHideLife;
    private SmoothCheckBox checkBox;

    private GridViewY1 gridView;
    private ResultImageUploadAdapter adapter;
    private List<ScreenShotEntity> data = new ArrayList<>();

    private TextView typeText;
    private View type;
    private ImageView typeArrow;
    private EditText title;
    private boolean isFirstIn = true;

    public String getTypeText() {
        if (typeText.getText() == null)
            return "";
        return typeText.getText().toString().trim();
    }

    public String getShareTitle() {
        if (title.getText() == null)
            return "";
        return title.getText().toString().trim();
    }

    @Override
    public int getContentView() {
        return R.layout.activity_homeimageshare;
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
        setAbTitle("分享图片");
    }

    @Override
    public void initView() {
        super.initView();

        gridView = (GridViewY1) findViewById(R.id.homeimageshare_gridview);
        typeText = (TextView) findViewById(R.id.homeimageshare_type_text);
        title = (EditText) findViewById(R.id.homeimageshare_title);
        type = findViewById(R.id.homeimageshare_type);
        typeArrow = (ImageView) findViewById(R.id.imageshare_type_arrow);
        checkBox = (SmoothCheckBox) findViewById(R.id.homeimageshare_checkState);

        findViewById(R.id.homeimageshare_check).setOnClickListener(this);
        findViewById(R.id.homeimageupload_share).setOnClickListener(this);
        type.setOnClickListener(this);
    }

    @Override
    public void loadData() {
        super.loadData();

        if (game != null) {
            typeText.setText(game.getGroup_name());
        }

        ImageViewActivity.imageViewDeleteData.clear();

        ScreenShotEntity imageInfo = new ScreenShotEntity();
        data.add(imageInfo);

        adapter = new ResultImageUploadAdapter(this, data, R.layout.adapter_imageupload);
        gridView.setAdapter(adapter);
    }

    @Override
    public void onResume() {
        super.onResume();

        if (isFirstIn) {
            //启动后进入选择图片页
            ActivityManeger.startImageViewActivity(this);
            isFirstIn = false;
        }
    }

    @SuppressLint("InlinedApi")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.homeimageshare_check:
                checkBox.performClick();
                break;

            case R.id.homeimageshare_type:// 游戏选择
                try {
                    InputUtil.closeKeyboard(this);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        showPopupWindow();
                    }
                }, 400);
                break;

            case R.id.homeimageupload_share:// 立即分享
                try {
                    if (!NetUtil.isConnect(this)) {
                        ToastHelper.s("当前网络不可用，请检查后重试");
                        return;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (!isLogin()) {
                    showToastLogin();
                    return;
                }
                if (ImageViewActivity.imageViewDeleteData.size() < 1) {
                    showToastShort("请选择要上传的图片");
                    return;
                }
                if (getTypeText().equals("请选择类型")) {
                    showToastShort("请选择图文类型");
                    return;
                }
                if (game == null) {
                    showToastShort("请选择类型");
                    return;
                }
                if (StringUtil.isNull(getShareTitle())){
                    showToastShort("请输入标题");
                    return;
                }
                if (!checkBox.isChecked()) {
                    showToastShort("请仔细阅读并同意使用条款及隐私协议");
                    return;
                }
                uploadImage();
                break;
        }
    }

    /**
     * 选择视频类型
     */
    private MoreTypePopupWindow popupWindow;

    private void showPopupWindow() {
        dismissPopupWindow();
        popupWindow = new MoreTypePopupWindow(this, typeArrow, isHideLife);
        popupWindow.showPopupWindow(type);
    }

    private void dismissPopupWindow() {
        if (popupWindow != null && popupWindow.isShowing())
            popupWindow.dismiss();
    }

    /***
     * 上传图文到服务器
     */
    private void uploadImage() {
        String title = this.title.getText().toString();
        String game_id = this.game.getGame_id();

        List<ScreenShotEntity> list = new ArrayList<>();
        list.addAll(data);
        list.remove(list.size() - 1);
        String member_id = PreferencesHepler.getInstance().getMember_id();

        // 上传图片服务
        DataManager.UPLOAD.uploadImage(game_id, member_id, title, "", list);
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
                showToastLong("上传成功");
                AppManager.getInstance().removeActivity(GroupDetailActivity.class);
                ActivityManeger.startGroupDetailActivityNewTask(this, game.getGroup_id());
                finish();
            } else
                showToastLong(msg);
        }
    }

    /**
     * 组件间的通讯：查找游戏 查找精彩生活
     */
    public void onEventMainThread(SearchGame2VideoShareEvent event) {

        String game_name = event.getAssociate().getGame_name();
        String game_id = event.getAssociate().getGame_id();
        String group_id = event.getAssociate().getGroup_id();
        String flag = event.getAssociate().getFlag();

        game = new Game();
        game.setGameName(game_name);
        game.setGame_id(game_id);
        game.setGroup_id(group_id);
        game.setFlag(flag);

        typeText.setText(game.getGameName());
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

        adapter = new ResultImageUploadAdapter(this, data, R.layout.adapter_imageupload);
        gridView.setAdapter(adapter);
    }
}
