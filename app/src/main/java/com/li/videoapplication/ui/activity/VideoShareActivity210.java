package com.li.videoapplication.ui.activity;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.graphics.Bitmap;

import android.os.Build;
import android.text.Editable;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.style.ImageSpan;
import android.text.style.MetricAffectingSpan;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.li.videoapplication.R;
import com.li.videoapplication.data.DataManager;
import com.li.videoapplication.data.database.VideoCaptureEntity;
import com.li.videoapplication.data.database.VideoCaptureManager;
import com.li.videoapplication.data.image.VideoCover;
import com.li.videoapplication.data.image.VideoDuration;
import com.li.videoapplication.data.local.FileUtil;
import com.li.videoapplication.data.local.SYSJStorageUtil;
import com.li.videoapplication.data.model.entity.Game;
import com.li.videoapplication.data.model.entity.Match;
import com.li.videoapplication.data.model.entity.Tag;
import com.li.videoapplication.data.model.event.SearchGame2VideoShareEvent;
import com.li.videoapplication.data.model.event.Share2VideoShareEvent;
import com.li.videoapplication.data.model.event.Tag2VideoShareEvent;
import com.li.videoapplication.data.model.response.BaseInfoEntity;
import com.li.videoapplication.data.model.response.GameTagListEntity;
import com.li.videoapplication.data.model.response.RecommendedLocationEntity;
import com.li.videoapplication.data.model.response.SelectMatchEntity;
import com.li.videoapplication.data.model.response.VideoDisplayVideoEntity;
import com.li.videoapplication.data.upload.VideoShareTask208;
import com.li.videoapplication.framework.AppConstant;
import com.li.videoapplication.framework.TBaseActivity;
import com.li.videoapplication.tools.UmengAnalyticsHelper;
import com.li.videoapplication.ui.ActivityManeger;
import com.li.videoapplication.ui.DialogManager;
import com.li.videoapplication.ui.adapter.JoinAdapter;
import com.li.videoapplication.ui.popupwindows.ApplyPopupWindow;
import com.li.videoapplication.ui.popupwindows.MoreTypePopupWindow;
import com.li.videoapplication.ui.toast.ToastHelper;
import com.li.videoapplication.utils.BitmapUtil;
import com.li.videoapplication.utils.InputUtil;
import com.li.videoapplication.utils.SpanUtil;
import com.li.videoapplication.utils.StringUtil;
import com.li.videoapplication.views.GridViewY1;
import com.li.videoapplication.views.ListViewY1;
import com.li.videoapplication.views.SmoothCheckBox;
import com.li.videoapplication.views.SmoothCheckBox.OnCheckedChangeListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import cn.sharesdk.framework.ShareSDK;

/**
 * 活动：视频分享
 */
@SuppressLint("HandlerLeak")
@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
public class VideoShareActivity210 extends TBaseActivity implements OnClickListener,
        TextWatcher {

    public static final List<Tag> TAGS = Collections.synchronizedList(new ArrayList<Tag>());
    public static final List<String> IDS = Collections.synchronizedList(new ArrayList<String>());
    public static final Map<String, MetricAffectingSpan> SPANS = Collections.synchronizedMap(new HashMap<String, MetricAffectingSpan>());
    public static Match m = null;
    public static MetricAffectingSpan span;

    private VideoCaptureEntity entity;
    private Game game;
    private Match match;
    private RecommendedLocationEntity event;

    public static String getMatch_id() {
        if (m == null)
            return "";
        else
            return m.getMatch_id();
    }

    public static String getMatch_name() {
        if (m == null)
            return "";
        else
            return m.getName_tag();
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

    public String getTypeText() {
        if (typeText.getText() == null)
            return "";
        return typeText.getText().toString().trim();
    }

    public String getDescription() {
        if (description.getText() == null)
            return "";
        String s = "";
        try {
            s = SpanUtil.getText(description);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return s;
    }

    public int getIsofficial() {
        if (apply.isChecked())
            return 1;
        else
            return 0;
    }

    public void sharePerformClick() {
        share.performClick();
    }

    // 分享
    private TextView share;
    public SmoothCheckBox apply;
    private TextView applyText;
    private ImageView applyQuestion;

    // 活动
    private View join;
    private ListViewY1 activityListview;
    private ArrayList<Match> data = new ArrayList<>();
    private JoinAdapter adapter;

    // 三角箭头
    private ImageView typeArrow;
    private View type;
    private View shareTag;

    @Override
    public int getContentView() {
        return R.layout.activity_videoshare210;
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
        refresAdapterView();

        initData();
    }

    @Override
    public void loadData() {
        super.loadData();

        if (game != null) {
            // 参加活动列表
            DataManager.selectMatch(game.getGame_id());

            // 视频分享标签
            DataManager.gameTagList(game.getGame_id());
        }
    }

    protected void onDestroy() {
        ShareSDK.stopSDK(this);
        m = null;
        span = null;
        TAGS.clear();
        IDS.clear();
        SPANS.clear();
        super.onDestroy();
        if (bitmap != null && !bitmap.isRecycled())
            bitmap.recycle();
    }

    // ------------------------------------------------------------------------------

    @Override
    public void onClick(final View v) {

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
            v.setEnabled(false);
            v.setClickable(false);
            postDelayed(new Runnable() {
                @Override
                public void run() {

                    showMoreTypePopupWindow();

                    v.setEnabled(true);
                    v.setClickable(true);
                }
            }, 400);
        } else if (v == share) {
            //敏感词过滤
            DataManager.baseInfo(getDescription());
        } else if (v == applyQuestion) {
            showApplyPopupWindow();
        } else if (v == applyText) {
            apply.performClick();
        } else if (v == shareTag) {
            if (game == null) {
                showToastShort("请选择视频类型");
                return;
            }
            if (TAGS == null ||
                    TAGS.size() == 0) {
                showToastShort("该视频类型没有标签");
                return;
            }
            // 标签
            ActivityManeger.startTagActivity(this);
        }
    }

    private int after, before;

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        this.after = after;
    }

    @Override
    public void onTextChanged(CharSequence spanned, int start, int before, int count) {
        this.before = before;
    }

    @Override
    public void afterTextChanged(Editable editable) {
        descriptionCount.setText(editable.length() + "/50");
        if (editable.length() > 50)
            showToastShort("视频标题最多50个字");

        Spanned spanned = description.getText();
        ImageSpan[] spans = spanned.getSpans(0, spanned.length(), ImageSpan.class);
        List<ImageSpan> list = Arrays.asList(spans);

        if (after < before) {
            // 活动
            if (!list.contains(span)) {
                m = null;
                span = null;
                if (adapter != null) {
                    adapter.notifyDataSetChanged();
                }
            }
        }
        // 标签
        Iterator<String> it = SPANS.keySet().iterator();
        while (it.hasNext()) {
            String id = it.next();
            MetricAffectingSpan metricAffectingSpan = SPANS.get(id);
            if (!list.contains(metricAffectingSpan)) {
                IDS.remove(id);
                it.remove();
            }
        }
    }

    // ----------------------------------------------------------------------------------------

    private void initContentView() {

        size = (TextView) findViewById(R.id.videoshare_size);
        cover = (ImageView) findViewById(R.id.videoshare_cover);
        edit = (TextView) findViewById(R.id.videoshare_edit);

        type = findViewById(R.id.videoshare_type);
        shareTag = findViewById(R.id.videoshare_tag);
        typeText = (TextView) findViewById(R.id.videoshare_type_text);
        typeArrow = (ImageView) findViewById(R.id.videoshare_type_arrow);
        description = (EditText) findViewById(R.id.videoshare_description);
        descriptionCount = (TextView) findViewById(R.id.videoshare_descriptionCount);

        share = (TextView) findViewById(R.id.videoshare_share);
        apply = (SmoothCheckBox) findViewById(R.id.videoshare_apply);
        applyText = (TextView) findViewById(R.id.videoshare_apply_text);
        applyQuestion = (ImageView) findViewById(R.id.videoshare_apply_question);

        edit.setOnClickListener(this);
        type.setOnClickListener(this);
        shareTag.setOnClickListener(this);
        share.setOnClickListener(this);
        applyQuestion.setOnClickListener(this);
        applyText.setOnClickListener(this);

        description.addTextChangedListener(this);
        descriptionCount.setText("0/50");

        join = findViewById(R.id.videoshare_join);
        join.setVisibility(View.GONE);
        activityListview = (ListViewY1) findViewById(R.id.activity_listview);
    }

    private void refresAdapterView() {
        if (adapter != null)
            adapter.notifyDataSetChanged();
        if (!StringUtil.isNull(getTypeText()) &&
                data.size() > 0) {
            join.setVisibility(View.VISIBLE);
        } else {
            join.setVisibility(View.GONE);
        }
    }

    private long video_length;
    private String fileSize;
    private Bitmap bitmap;

    private void initData() {

        try {
            bitmap = BitmapUtil.readLocalBitmapQuarter(SYSJStorageUtil.createCoverPath(entity.getVideo_path()).getPath());
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (bitmap == null) {
            try {
                bitmap = VideoCover.generateBitmap(entity.getVideo_path());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (bitmap != null)
            cover.setImageBitmap(bitmap);

        try {
            fileSize = FileUtil.formatFileSize(FileUtil.getFileSize(entity.getVideo_path()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        size.setText(fileSize);

        try {
            video_length = Long.valueOf(VideoDuration.getDuration(entity.getVideo_path()));
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (!StringUtil.isValidDate(entity.getVideo_name())) {
            description.setText(entity.getVideo_name());
        }

        SpanUtil.lastSelection(description);

        game = new Game();
        game.setGameName(entity.getGame_name());
        game.setGroup_name(entity.getGame_name());
        game.setGame_id(entity.getGame_id());
        if (game != null) {
            typeText.setText(game.getGroup_name());
        }

        entity.setShare_channel("");
        entity.setMember_id("");

        if (!StringUtil.isNull(entity.getMatch_id())) {
            m = new Match();
            m.setMatch_id(entity.getMatch_id());
            m.setName_tag(entity.getMatch_name());
        }

        if (entity.getUpvideo_isofficial() == 1) {
            apply.setChecked(true);
        } else {
            apply.setChecked(false);
        }

        List<String> ids = entity.getUpvideo_gametags_2();
        if (ids != null && ids.size() > 0) {
            IDS.addAll(ids);
        }

        List<Tag> tags = entity.getUpvideo_tags_2();
        if (tags != null && tags.size() > 0) {
            TAGS.addAll(tags);
        }

        if (m != null) {
            // 添加选中的
            VideoShareActivity210.span = SpanUtil.insertText(description, "#" + m.getName_tag() + "#");
        }

        Log.d(tag, "initData: entity=" + entity);
        Log.d(tag, "initData: m=" + m);
        Log.d(tag, "initData: IDS=" + IDS);
        Log.d(tag, "initData: TAGS=" + TAGS);
        Log.d(tag, "initData: game=" + game);
    }

    private void updateData() {

        if (game != null) {
            typeText.setText(game.getGameName());
        }

        if (getDescription().contains("《") &&
                getDescription().contains("》")) {
            try {
                description.getText().replace(getDescription().indexOf("《") + 1,
                        getDescription().indexOf("》"),
                        game.getGameName());
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            try {
                description.getText().insert(0,
                        "《" + game.getGameName() + "》");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        SpanUtil.lastSelection(description);
    }

    // ----------------------------------------------------------------------------------------

    /**
     * 选择视频类型
     */
    private ApplyPopupWindow applyPopupWindow;

    private void showApplyPopupWindow() {
        dismissApplyPopupWindow();
        applyPopupWindow = new ApplyPopupWindow(this, applyQuestion);
        applyPopupWindow.showPopupWindow();
    }

    private void dismissApplyPopupWindow() {
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

    // ----------------------------------------------------------------------------------------

    /**
     * 保存数据库
     */
    private void updateDatabase() {
        VideoCaptureManager.updateNamePathByPath(entity.getVideo_path(),
                entity.getVideo_name(),
                null);
    }

    /**
     * 分享
     */
    private void shareNow() {
        if (StringUtil.isNull(getDescription())) {
            showToastShort("请输入游戏标题");
            return;
        }
        if (StringUtil.isNull(getTypeText())) {
            showToastShort("请选择视频类型");
            return;
        }
        if (apply.isChecked() && getDescription().length() < 10) {
            showToastShort("申请官方推荐游戏标题不少于10个字");
            return;
        }
        if (apply.isChecked()) { //申请推荐位
            // 推荐位信息
            DataManager.recommendedLocation(getMember_id());
        } else {
            ActivityManeger.startShareActivity4MyLocalVideo(this);
        }
    }

    public String getGoods_id() {
        if (event != null && event.isResult() && event.getGoods() != null && event.getGoods().getId() != null) {
            return event.getGoods().getId();
        } else {
            return null;
        }
    }

    // ----------------------------------------------------------------------------------------

    /**
     * 回调:推荐位
     */
    public void onEventMainThread(RecommendedLocationEntity event) {

        if (event != null && event.isResult()) {
            this.event = event;

            entity.setVideo_name(getDescription());

            DialogManager.showOfficialPaymentDialog(this, entity, event);

        }
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
        String game_name = event.getAssociate().getGame_name();
        String game_id = event.getAssociate().getGame_id();

        game = new Game();
        game.setGameName(game_name);
        game.setGame_id(game_id);

        if (game != null) {
            // 参加活动列表
            DataManager.selectMatch(game.getGame_id());

            // 视频分享标签
            DataManager.gameTagList(game.getGame_id());
        }

        updateData();
        refresAdapterView();

        try {
            description.requestFocus();
        } catch (Exception e) {
            e.printStackTrace();
        }

        UmengAnalyticsHelper.onShareEvent(this, game_id);
    }

    /**
     * 组件间的通讯：分享
     */
    public void onEventMainThread(Share2VideoShareEvent event) {
        String shareChannel = event.getShareChannel();

        VideoCaptureEntity e = VideoCaptureManager.findByPath(entity.getVideo_path());

        if (e != null) {

            if (e.getVideo_name() != null &&
                    e.getVideo_name().equals(getDescription())) {

                if (game != null && game.getGame_id().equals(e.getGame_id())) {

                    if (getMatch_id().equals(e.getMatch_id())) {

                        if (getIsofficial() == e.getUpvideo_isofficial()) {

                            List<String> list = e.getUpvideo_gametags_2();
                            if (list != null &&
                                    IDS.containsAll(list) &&
                                    IDS.size() == list.size()) {

                                if (e.getVideo_station().equals(VideoCaptureEntity.VIDEO_STATION_SHOW)) {

                                    // 分享
                                    VideoShareTask208.share(e.getVideo_name(),
                                            "",
                                            AppConstant.getWebUrl(e.getUpvideo_qnkey()),
                                            AppConstant.getCoverUrl(e.getUpvideo_flag()),
                                            shareChannel);
                                    finish();
                                    return;

                                } else if (e.getVideo_station().equals(VideoCaptureEntity.VIDEO_STATION_HIDE)) {
                                    // 隐藏云端视频(七牛)
                                    VideoDisplayVideoEntity result = DataManager.videoDisplayVideoQnKey(e.getUpvideo_qnkey());
                                    if (result != null && result.isResult()) {
                                        // 保存上传状态
                                        VideoCaptureManager.updateStationByQnkey(e.getUpvideo_qnkey(), VideoCaptureEntity.VIDEO_STATION_SHOW);
                                        e.setVideo_station(VideoCaptureEntity.VIDEO_STATION_SHOW);

                                        // 分享
                                        VideoShareTask208.share(e.getVideo_name(),
                                                "",
                                                AppConstant.getWebUrl(e.getUpvideo_qnkey()),
                                                AppConstant.getCoverUrl(e.getUpvideo_flag()),
                                                shareChannel);
                                        finish();
                                        return;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        Log.d(tag, "onMessage: IDS=" + IDS);
        Log.d(tag, "onMessage: m=" + m);

        // 保存文件名
        VideoCaptureManager.updateNameByPath(entity.getVideo_path(),
                getDescription());
        List<String> ids = new ArrayList<>(IDS);
        List<Tag> tags = new ArrayList<>();
        for (Tag tag : TAGS) {
            if (ids.contains(tag.getGame_tag_id())) {
                tags.add(tag);
            }
        }
        // 保存游戏，活动，标签
        VideoCaptureManager.updateMatchGameTagsByPath(entity.getVideo_path(),
                game.getGame_id(),
                game.getGameName(),
                getMatch_id(),
                getMatch_name(),
                ids,
                tags);
        // 保存上传状态
        VideoCaptureManager.updateStationByPath(entity.getVideo_path(),
                shareChannel,
                getMember_id(),
                game.getGame_id(),
                getMatch_id(),
                getDescription(),
                "",
                getIsofficial(),
                ids);

        DataManager.UPLOAD.startVideo(shareChannel,
                getMember_id(),
                getDescription(),
                game.getGame_id(),
                getMatch_id(),
                "",
                getIsofficial(),
                ids,
                entity,
                getGoods_id());

        finish();
    }

    /**
     * 组件间的通讯：标签
     */
    public void onEventMainThread(Tag2VideoShareEvent event) {
        Log.d(tag, "onMessage: Tag2VideoShareEvent");

        updateTagSpan();
    }

    public void updateTagSpan() {
        for (Tag tag : TAGS) {
            String id = tag.getGame_tag_id();
            // 添加选中的
            if (IDS.contains(id) &&
                    !SPANS.containsKey(id)) {
                MetricAffectingSpan span = SpanUtil.insertText(description, "#" + tag.getName() + "#");
                if (span != null) {
                    SPANS.put(id, span);
                }
            }
            // 移除没有选中的
            if (!IDS.contains(id) &&
                    SPANS.containsKey(id)) {
                SpanUtil.removeSpan(description, SPANS.get(id));
                SPANS.remove(id);
            }
        }
    }

    /**
     * 回调：参加活动列表
     */
    public void onEventMainThread(SelectMatchEntity event) {
        if (data != null)
            data.clear();
        if (event.isResult()) {
            if (event.getData() != null && event.getData().size() > 0) {
                data.addAll(event.getData());
                adapter = new JoinAdapter(this, data, R.layout.adapter_join);
                activityListview.setAdapter(adapter);
            }
        }

        List<String> ids = new ArrayList<>();
        for (Match match : data) {
            ids.add(match.getMatch_id());
        }

        if (ids.contains(getMatch_id())) {
            if (span == null) {
                // 添加选中的
                MetricAffectingSpan span = SpanUtil.insertText(description, "#" + getMatch_name() + "#");
                VideoShareActivity210.span = span;
            }
        } else {
            // 移除选择的
            SpanUtil.removeSpan(description, span);
            m = null;
            span = null;

        }
        refresAdapterView();
    }

    /**
     * 回调：视频分享标签
     */
    public void onEventMainThread(GameTagListEntity event) {
        TAGS.clear();
        if (event.isResult()) {
            if (event.getData() != null
                    && event.getData().size() > 0) {
                TAGS.addAll(event.getData());
            }
        }
        List<String> ids = new ArrayList<>();
        for (Tag tag : TAGS) {
            ids.add(tag.getGame_tag_id());
        }
        if (IDS.size() > 0) {
            // 取消没有的
            for (String id : IDS) {
                if (!ids.contains(id)) {
                    SpanUtil.removeSpan(description, SPANS.get(id));
                    SPANS.remove(id);
                    IDS.remove(id);
                }
            }
            // 添加选中的
            for (Tag tag : TAGS) {
                String id = tag.getGame_tag_id();
                if (IDS.contains(id) &&
                        !SPANS.containsKey(id)) {
                    MetricAffectingSpan span = SpanUtil.insertText(description, "#" + tag.getName() + "#");
                    if (span != null) {
                        SPANS.put(id, span);
                    }
                }
            }
        }
    }


    public void updateMatchSpan(Match match) {
        if (match != null) {
            if (getMatch_id() != null) {
                if (getMatch_id().equals(match.getMatch_id())) {// 移除选中的
                    SpanUtil.removeSpan(description, span);
                    m = null;
                    VideoShareActivity210.span = null;
                } else {//  移除选中的，再添加
                    SpanUtil.removeSpan(description, span);
                    MetricAffectingSpan span = SpanUtil.insertText(description, "#" + match.getName_tag() + "#");
                    m = match;
                    VideoShareActivity210.span = span;
                }
            } else {// 添加选中的
                MetricAffectingSpan span = SpanUtil.insertText(description, "#" + match.getName_tag() + "#");
                m = match;
                VideoShareActivity210.span = span;
            }
        }
    }
}