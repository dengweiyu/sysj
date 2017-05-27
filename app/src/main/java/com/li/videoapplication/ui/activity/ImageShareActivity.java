package com.li.videoapplication.ui.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.li.videoapplication.data.local.ScreenShotEntity;
import com.li.videoapplication.R;
import com.li.videoapplication.data.DataManager;
import com.li.videoapplication.data.model.entity.Game;
import com.li.videoapplication.data.model.event.ImageView2ImageShareEvent;
import com.li.videoapplication.data.model.event.SearchGame2VideoShareEvent;
import com.li.videoapplication.data.upload.Contants;
import com.li.videoapplication.data.upload.ImageShareResponseObject;
import com.li.videoapplication.data.preferences.PreferencesHepler;
import com.li.videoapplication.framework.AppManager;
import com.li.videoapplication.framework.TBaseActivity;
import com.li.videoapplication.ui.ActivityManager;
import com.li.videoapplication.ui.adapter.ImageShareAdapter;
import com.li.videoapplication.ui.popupwindows.MoreTypePopupWindow;
import com.li.videoapplication.utils.InputUtil;
import com.li.videoapplication.utils.StringUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import cn.sharesdk.framework.ShareSDK;

/**
 * 活动：图文分享
 */
@SuppressLint("HandlerLeak")
public class ImageShareActivity extends TBaseActivity implements
                    OnClickListener {
	
	public static final String ACTION = ImageShareActivity.class.getName();
	public static final String TAG = ImageShareActivity.class.getSimpleName();

    private Game game;
    private String imageUrl;
    private String imageTitle;

    @Override
	public void refreshIntent() {
        Intent intent = getIntent();
        try {
            game = (Game) intent.getSerializableExtra("game");
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            imageUrl = intent.getStringExtra("imageUrl");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

	private GridView gridView;
	private ImageShareAdapter adapter;
	private List<ScreenShotEntity> data = new ArrayList<>();

	private EditText headline;
	private TextView typeText;
	private LinearLayout type;
	private ImageView typeArrow;
	private EditText description;
	private TextView submit;

    public String getHeadline() {
        if (headline.getText() == null)
            return "";
        return headline.getText().toString().trim();
    }

    public String getTypeText() {
        if (typeText.getText() == null)
            return "";
        return typeText.getText().toString().trim();
    }

    public String getDescription() {
        if (description.getText() == null)
            return "";
        return description.getText().toString().trim();
    }

	@Override
	public int getContentView() {
		return R.layout.activity_imageshare;
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
        setAbTitle(R.string.imageshare_title);
    }

    @Override
    public void initView() {
        super.initView();

        gridView = (GridView) findViewById(R.id.gridview);
        headline = (EditText) findViewById(R.id.imageshare_headline);
        typeText = (TextView) findViewById(R.id.imageshare_type_text);
        description = (EditText) findViewById(R.id.imageshare_description);
        submit = (TextView) findViewById(R.id.imageshare_submit);
        type = (LinearLayout) findViewById(R.id.imageshare_type);
        typeArrow = (ImageView) findViewById(R.id.imageshare_type_arrow);

        type.setOnClickListener(this);
        submit.setOnClickListener(this);
    }

    @Override
    public void loadData() {
        super.loadData();

        if (imageUrl != null) {
            try {
                File file = new File(imageUrl);
                imageTitle = file.getName();
                // 去掉后后缀名
                imageTitle = imageTitle.split("\\.png")[0];
                headline.setText(imageTitle);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (game != null) {
            typeText.setText(game.getGroup_name());
        }

        ImageViewActivity.imageViewDeleteData.clear();
        if (!StringUtil.isNull(imageUrl)) {
            ScreenShotEntity imageInfo = new ScreenShotEntity();
            imageInfo.setPath(imageUrl);
            data.add(imageInfo);
            ImageViewActivity.imageViewDeleteData.add(imageUrl);
        }
        ScreenShotEntity imageInfo = new ScreenShotEntity();
        data.add(imageInfo);

        adapter = new ImageShareAdapter(this, data, R.layout.adapter_imageshare);
        gridView.setAdapter(adapter);
    }

    @SuppressLint("InlinedApi")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.imageshare_type:// 游戏选择
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

            case R.id.imageshare_submit:// 立即分享
                if (!isLogin()) {
                    showToastLogin();
                    return;
                }
                if (ImageViewActivity.imageViewDeleteData.size() < 1) {
                    showToastShort("请选择要上传的图片");
                    return;
                }
                if (getHeadline().isEmpty()) {
                    showToastShort("请输入图文标题");
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
                uploadImage();
                break;
        }
    }

    /**
     * 选择视频类型
     */
    private MoreTypePopupWindow popupWindow;

    private void showPopupWindow(){
        dismissPopupWindow();
        popupWindow = new MoreTypePopupWindow(this, typeArrow, true);
        popupWindow.showPopupWindow(type);
    }

    private void dismissPopupWindow(){
        if (popupWindow != null && popupWindow.isShowing())
            popupWindow.dismiss();
    }

    /***
     * 上传图文到服务器
     */
    private void uploadImage() {
        String headline = this.headline.getText().toString();
        String description = this.description.getText().toString();
        String game_id = this.game.getGame_id();

        List<ScreenShotEntity> list = new ArrayList<>();
        list.addAll(data);
        list.remove(list.size() - 1);
        String member_id = PreferencesHepler.getInstance().getMember_id();

        // 上传图片服务
        DataManager.UPLOAD.uploadImage(game_id, member_id, headline, description, list);
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
                ActivityManager.startGroupDetailActivityNewTask(this, game.getGroup_id());
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

        game = new Game();
        game.setGameName(game_name);
        game.setGame_id(game_id);
        game.setGroup_id(group_id);

        typeText.setText(game.getGameName());
    }

    /**
     * 组件间的通讯：图片选择
     */
    public void onEventMainThread(ImageView2ImageShareEvent event) {

        List<ScreenShotEntity> list = new ArrayList<>();
        Iterator<String> it = ImageViewActivity.imageViewDeleteData.iterator();
        while (it.hasNext()) {
            String imageUrl = it.next();
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
