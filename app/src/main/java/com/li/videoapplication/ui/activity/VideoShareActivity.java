package com.li.videoapplication.ui.activity;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.os.Build;
import android.text.Editable;
import android.text.Selection;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.li.videoapplication.R;
import com.li.videoapplication.data.DataManager;
import com.li.videoapplication.data.database.VideoCaptureEntity;
import com.li.videoapplication.data.database.VideoCaptureManager;
import com.li.videoapplication.data.image.MyLocalVideoImageLoader;
import com.li.videoapplication.data.image.VideoTimeLoader;
import com.li.videoapplication.data.local.FileUtil;
import com.li.videoapplication.data.model.entity.Game;
import com.li.videoapplication.data.model.entity.Match;
import com.li.videoapplication.data.model.event.SearchGame2VideoShareEvent;
import com.li.videoapplication.data.model.event.Share2VideoShareEvent;
import com.li.videoapplication.data.model.response.BaseInfoEntity;
import com.li.videoapplication.data.model.response.SelectMatch203Entity;
import com.li.videoapplication.framework.TBaseActivity;
import com.li.videoapplication.ui.ActivityManeger;
import com.li.videoapplication.ui.adapter.JoinAdapter;
import com.li.videoapplication.ui.popupwindows.ApplyPopupWindow;
import com.li.videoapplication.ui.popupwindows.MoreTypePopupWindow;
import com.li.videoapplication.ui.toast.ToastHelper;
import com.li.videoapplication.utils.InputUtil;
import com.li.videoapplication.utils.StringUtil;
import com.li.videoapplication.views.ListViewY1;

import java.util.ArrayList;

import cn.sharesdk.framework.ShareSDK;

/**
 * 活动：视频分享
 */
@SuppressLint("HandlerLeak")
@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
public class VideoShareActivity extends TBaseActivity implements OnClickListener, TextWatcher, CompoundButton.OnCheckedChangeListener {

    private VideoCaptureEntity entity;
    private Game game;
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
            game = (Game) getIntent().getSerializableExtra("game");
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            match = (Match) getIntent().getSerializableExtra("match");
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.i(tag, "entity=" + entity);
        Log.i(tag, "game=" + game);
        Log.i(tag, "match=" + match);
    }

    // 文件大小， 视频图片，编辑
    private TextView size;
    private ImageView cover;
    private TextView edit;

    // 视频描述
    private EditText description;
    private TextView descriptionCount;
    private TextView typeText;
    private EditText headline;

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

    public int getApply() {
        if (apply.isChecked())
            return 1;
        else
            return 0;
    }

    // 分享
    private TextView share;
    private CheckBox apply;
    private ImageView applyQuestion;

    // 活动
    private View join;
    private ListViewY1 listView;
    private ArrayList<Match> data = new ArrayList<Match>();
    private JoinAdapter adapter;

    // 三角箭头
    private ImageView typeArrow;
    private View type;

    @Override
    public int getContentView() {
        return R.layout.activity_videoshare;
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
        setAbTitle(R.string.videoshare_title);
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

        String game_id = null;
        try {
            game_id = game.getGame_id();
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 参加活动列表203
        DataManager.selectMatch203(game_id);
    }

    protected void onDestroy() {
        ShareSDK.stopSDK(this);
        JoinAdapter.match_id = null;
        super.onDestroy();
        if (bitmap != null && !bitmap.isRecycled())
            bitmap.recycle();
    }

    private void initContentView() {

        size = (TextView) findViewById(R.id.videoshare_size);
        cover = (ImageView) findViewById(R.id.videoshare_cover);
        edit = (TextView) findViewById(R.id.videoshare_edit);

        headline = (EditText) findViewById(R.id.videoshare_headline);
        type = findViewById(R.id.videoshare_type);
        typeText = (TextView) findViewById(R.id.videoshare_type_text);
        typeArrow = (ImageView) findViewById(R.id.videoshare_type_arrow);
        description = (EditText) findViewById(R.id.videoshare_description);
        descriptionCount = (TextView) findViewById(R.id.videoshare_description_count);

        share = (TextView) findViewById(R.id.videoshare_share);
        apply = (CheckBox) findViewById(R.id.videoshare_apply);
        applyQuestion = (ImageView) findViewById(R.id.videoshare_apply_question);

        edit.setOnClickListener(this);
        headline.setOnClickListener(this);
        type.setOnClickListener(this);
        share.setOnClickListener(this);
        applyQuestion.setOnClickListener(this);

        apply.setOnCheckedChangeListener(this);
        apply.setChecked(false);

        findViewById(R.id.videoshare_applytext).setOnClickListener(this);
        description.addTextChangedListener(this);
        descriptionCount.setText("0/50");

        join = findViewById(R.id.videoshare_join);
        join.setVisibility(View.GONE);
        listView = (ListViewY1) findViewById(R.id.listview);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
    }

    @Override
    public void afterTextChanged(Editable s) {
        descriptionCount.setText(s.length() + "/50");
        if (s.length() > 50)
            showToastShort("视频描述多50个字");
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
        if (!StringUtil.isValidDate(entity.getVideo_name())) {
            headline.setText(entity.getVideo_name());
        }
        // 视频大小
        size.setText(fileSize);
        if (game != null)
            typeText.setText(game.getGroup_name());
        refreshListView();
    }

    private void updateContentView() {

        typeText.setAlpha(1);
        typeText.setText(game.getGameName());
        String video_title = null;
        // 检查并删除带书名号的游戏类型标题
        try {
            video_title = getHeadline().substring(getHeadline().indexOf("》") + 1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        headline.setText("《" + game.getGameName() + "》" + video_title);
        Editable editable = headline.getText();
        Selection.setSelection(editable, editable.length());
        refreshListView();
    }

    private void refreshListView() {
        if (!StringUtil.isNull(getTypeText()) && data.size() > 0) {
            join.setVisibility(View.VISIBLE);
        } else {
            join.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {

        if (v == edit) {// 编辑视频
            if (video_length > 30000) {
                ActivityManeger.startVideoEditorActivity(this, entity);
            } else {
                showToastShort("30秒以上的时长才可以编辑");
            }
        } else if (v == type) {
            try {
                InputUtil.closeKeyboard(this);
            } catch (Exception e) {
                e.printStackTrace();
            }
            showMoreTypePopupWindow();
        } else if (v == share) {
            //敏感词过滤
            DataManager.baseInfo(getHeadline()+" "+getDescription());
        } else if (v == applyQuestion) {
            showApplyPopupWindow();
        } else if (v.getId() == R.id.videoshare_applytext) {
            isChecked = !isChecked;
            apply.setChecked(isChecked);
        }
    }

    /**
     * 选择视频类型
     */
    private ApplyPopupWindow applyPopupWindow;

    private void showApplyPopupWindow(){
        dismissApplyPopupWindow();
        applyPopupWindow = new ApplyPopupWindow(this, applyQuestion);
        applyPopupWindow.showPopupWindow();
    }

    private void dismissApplyPopupWindow(){
        if (applyPopupWindow != null && applyPopupWindow.isShowing())
            applyPopupWindow.dismiss();
    }

    /**
     * 游戏类型
     */
    private MoreTypePopupWindow moreTypePopupWindow;

    private void showMoreTypePopupWindow() {
        dismissMoreTypePopupWindow();
        moreTypePopupWindow = new MoreTypePopupWindow(this, typeArrow, false);
        moreTypePopupWindow.showPopupWindow(type);
    }

    private void dismissMoreTypePopupWindow() {
        if (moreTypePopupWindow != null && moreTypePopupWindow.isShowing())
            moreTypePopupWindow.dismiss();
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
     * 对即将上传的文件进行视频标题、类型非空判断
     */
    private void shareNow() {
        if (StringUtil.isNull(getHeadline())) {
            showToastShort("请输入游戏标题");
            return;
        }
        if (StringUtil.isNull(getTypeText())) {
            showToastShort("请选择视频类型");
            return;
        }
        if (isChecked && getDescription().length() < 10) {
            showToastShort("申请官方推荐视频描述不少于10个字");
            return;
        }
        ActivityManeger.startShareActivity4MyLocalVideo(this);
    }

    /**
     * 回调：敏感词过滤
     */
    public void onEventMainThread(BaseInfoEntity event) {

        if (event != null) {
            if (event.isResult()) {
                if (!event.getData().isHasBad()) {
                    updateDatabase();
                    shareNow();
                } else {
                    ToastHelper.s("请勿使用敏感词汇");
                }
            }
        }
    }

    /**
     * 组件间的通讯：查找游戏 查找精彩生活
     */
    public void onEventMainThread(SearchGame2VideoShareEvent event) {

        Log.d(tag, "SearchGame2VideoShareEvent: "+event);
        String game_name = event.getAssociate().getGame_name();
        String game_id = event.getAssociate().getGame_id();

        game = new Game();
        game.setGameName(game_name);
        game.setGame_id(game_id);

        loadData();
        updateContentView();
    }

    /**
     * 组件间的通讯：分享
     */
    public void onEventMainThread(Share2VideoShareEvent event) {
        Log.i(tag, "event=" + event);
        String shareChannel = event.getShareChannel();

        DataManager.UPLOAD.uploadVideo(shareChannel,
                getMember_id(),
                getHeadline(),
                game.getGame_id(),
                JoinAdapter.match_id,
                getDescription(),
                getApply(),
                entity);

        finish();
    }

    /**
     * 回调：参加活动列表
     */
    public void onEventMainThread(SelectMatch203Entity event) {

        if (event.isResult()) {
            if (event.getData() != null && event.getData().size() > 0) {
                if (data != null)
                    data.clear();
                data.addAll(event.getData());
                adapter = new JoinAdapter(this, data, R.layout.adapter_join);
                listView.setAdapter(adapter);
                listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
                adapter.notifyDataSetChanged();
            }
        }
        refreshListView();
    }
}