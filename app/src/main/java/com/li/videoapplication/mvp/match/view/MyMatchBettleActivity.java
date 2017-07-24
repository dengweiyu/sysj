package com.li.videoapplication.mvp.match.view;

import android.graphics.Color;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.li.videoapplication.R;
import com.li.videoapplication.data.DataManager;
import com.li.videoapplication.data.model.entity.Match;
import com.li.videoapplication.data.model.event.VideoUploadCompleteEvent;
import com.li.videoapplication.data.model.response.MemberEndPKInfoEntity;
import com.li.videoapplication.data.model.response.SaveEventVideoEntity;
import com.li.videoapplication.data.model.response.ServiceName217Entity;
import com.li.videoapplication.data.preferences.PreferencesHepler;
import com.li.videoapplication.data.upload.Contants;
import com.li.videoapplication.data.upload.VideoShareTask208;
import com.li.videoapplication.framework.TBaseAppCompatActivity;
import com.li.videoapplication.tools.TimeHelper;
import com.li.videoapplication.tools.UmengAnalyticsHelper;
import com.li.videoapplication.ui.ActivityManager;
import com.li.videoapplication.ui.activity.VideoShareActivity;
import com.li.videoapplication.utils.StringUtil;
import com.li.videoapplication.utils.TextUtil;

import java.util.List;
import java.util.Random;

import io.rong.imkit.RongIM;
import me.everything.android.ui.overscroll.OverScrollDecoratorHelper;

/**
 * 活动：我的对战详情
 */
public class MyMatchBettleActivity extends TBaseAppCompatActivity implements View.OnClickListener,VideoShareTask208.Callback {

    private String event_id, schedule_id;
    private ProgressBar progress;
    private TextView uploadVideoText, progressText;
    private TextView process, schedule_time;
    private TextView myresult_text, enemyresult_text;
    private TextView a_team_name, a_name, b_name, b_team_name;
    private ImageView a_avatar, b_avatar, result_signet;
    private TextView notice1;
    private String currencyNum;
    public Button uploadImage;
    private View uploadVideoView, my_bgLine;
    private Match match;
    private String customerServiceID, customerServiceName;
    private LinearLayout scheduleView, buttonView, tipsView;

    /**
     * 跳转：选择上传视频
     */
    private void startVideoChooseActivity() {
        ActivityManager.startVideoChooseActivity(this, null, VideoShareActivity.TO_FINISH);
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_mymatchbettle;
    }

    @Override
    public void afterOnCreate() {
        super.afterOnCreate();
        setSystemBarBackgroundWhite();
    }

    @Override
    public void refreshIntent() {
        super.refreshIntent();
        try {
            event_id = getIntent().getStringExtra("event_id");
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            schedule_id = getIntent().getStringExtra("schedule_id");
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (StringUtil.isNull(event_id) || StringUtil.isNull(schedule_id)) finish();
    }

    @Override
    public void initView() {
        super.initView();
        initToolbar();
        initContentView();
        VideoShareTask208.addCallbacks(this);
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        TextView tb_title = (TextView) findViewById(R.id.tb_title);
        tb_title.setText("我的赛程");
        findViewById(R.id.tb_back).setOnClickListener(this);

    }

    private void initContentView() {
        ScrollView root = (ScrollView) findViewById(R.id.havedata_root);
        OverScrollDecoratorHelper.setUpOverScroll(root);
        scheduleView = (LinearLayout) findViewById(R.id.scheduleview);
        buttonView = (LinearLayout) findViewById(R.id.buttonview);
        tipsView = (LinearLayout) findViewById(R.id.tipsview);

        uploadVideoView = findViewById(R.id.ongoing_progress_container);
        progress = (ProgressBar) findViewById(R.id.ongoing_progress);
        uploadVideoText = (TextView) findViewById(R.id.ongoing_uploadtext);
        progressText = (TextView) findViewById(R.id.ongoing_progress_text);

        process = (TextView) findViewById(R.id.ongoing_process);
        schedule_time = (TextView) findViewById(R.id.ongoing_time);

        my_bgLine = findViewById(R.id.mybgline);
        myresult_text = (TextView) findViewById(R.id.myresult_text);
        a_avatar = (ImageView) findViewById(R.id.myicon);
        a_name = (TextView) findViewById(R.id.myname);
        a_team_name = (TextView) findViewById(R.id.myteamname);

        enemyresult_text = (TextView) findViewById(R.id.enemyresult_text);
        b_avatar = (ImageView) findViewById(R.id.enemyicon);
        b_name = (TextView) findViewById(R.id.enemyname);
        b_team_name = (TextView) findViewById(R.id.enemyteamname);

        result_signet = (ImageView) findViewById(R.id.myresult_image);//胜败印章

        uploadImage = (Button) findViewById(R.id.upload_image);

        notice1 = (TextView) findViewById(R.id.ongoing_notice1);

        findViewById(R.id.ongoing_customerservice).setOnClickListener(this);
        uploadVideoView.setOnClickListener(this);
    }

    @Override
    public void loadData() {
        super.loadData();
        DataManager.getMemberEndPKWindow(getMember_id(), event_id, schedule_id);

        loadServiceName();
    }

    private void loadServiceName() {
        List<String> csIDList = PreferencesHepler.getInstance().getCustomerServiceIDs();
        Log.d(tag, "CustomerServiceIDs: " + csIDList);
        if (csIDList != null) {
            int num = new Random().nextInt(csIDList.size());//随机获取一个客服ID
            customerServiceID = csIDList.get(num);
            Log.d(tag, "ID: " + customerServiceID);
            DataManager.getServiceName(customerServiceID);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tb_back:
                finish();
                break;
            case R.id.ongoing_progress_container://上传视频
                if (match != null && !StringUtil.isNull(match.getTeam_b().getLeader_id())) {
                    //1=>为已上传，24小时内不可上传，0=>为24小时内可上传
                    if (match.getTeam_a().getIs_upvideo().equals("0") &&
                            !VideoShareTask208.isUploading()) {
                        startVideoChooseActivity();
                        UmengAnalyticsHelper.onEvent(this, UmengAnalyticsHelper.MATCH, "我的赛程-上传视频");
                    }
                }
                break;
            case R.id.ongoing_customerservice://客服
                if (RongIM.getInstance() != null && !StringUtil.isNull(customerServiceID) &&
                        !StringUtil.isNull(customerServiceName)) {
                    ActivityManager.startConversationActivity(this,
                            customerServiceID,
                            customerServiceName,
                            false);
                }
                break;
        }
    }

    private void refreshData() {
        showAnim();
        Match record;
        if (match != null) {
            record = match.getSchedule();
            process.setText(TextUtil.numberAtRed(record.getName(), "#ff3d2e"));
            setTime(record);
        }
        if (match != null && match.getTeam_a() != null) {
            record = match.getTeam_a();
            setImageViewImageNet(a_avatar, record.getAvatar());
            setTextViewText(a_name, record.getLeader_game_role());
            if (record.getTeam_name() != null && !record.getTeam_name().equals("")) {
                a_team_name.setVisibility(View.VISIBLE);
                setTextViewText(a_team_name, "战队名：" + record.getTeam_name());
            } else {
                a_team_name.setVisibility(View.GONE);
            }

            if (record.getIs_upvideo().equals("1")) {//1=>为已上传，24小时内不可上传，0=>为24小时内可上传
                uploadVideoText.setText("已上传视频");
                uploadVideoView.setFocusable(false);
                uploadVideoView.setClickable(false);
            }

            setResult();
        }
        if (match != null && match.getTeam_b() != null) {
            record = match.getTeam_b();

            if (!StringUtil.isNull(record.getAvatar())) {
                setImageViewImageNet(b_avatar, record.getAvatar());
                setTextViewText(b_name, record.getLeader_game_role());
            } else {
                setImageViewImageRes(b_avatar, R.drawable.matchprocess_noenemy_large);
                setTextViewText(b_name, "本局轮空");
            }

            if (!StringUtil.isNull(record.getTeam_name())) {
                b_team_name.setVisibility(View.VISIBLE);
                setTextViewText(b_team_name, "战队名：" + record.getTeam_name());
            } else {
                b_team_name.setVisibility(View.GONE);
            }
        }

        //引导句子1：匹配对手成功后，点击约战TA进行比赛，如果对方未在19:00前与您对战，则截屏聊天记录或其他截图证明上传。
        //红色：#ff3d2e，蓝色：#48c5ff
        //引导句子3  红色：#ff3d2e，蓝色：#48c5ff
        String s1 = "上传" + TextUtil.toColor("比赛视频", "#48c5ff") + "奖励" + TextUtil.toColor(currencyNum, "#ff3d2e") + "魔豆";
        notice1.setText(Html.fromHtml(s1));

    }

    private void showAnim() {
        animationHelper.beginFadeSlideDelayTransition(tipsView);
        animationHelper.beginFadeSlideTransition(scheduleView, Gravity.START);
        animationHelper.beginFadeSlideTransition(buttonView, Gravity.END);
        openVisibility(tipsView, scheduleView, buttonView);
    }

    private void openVisibility(View... views) {
        for (View v : views) {
            boolean isGone = v.getVisibility() != View.VISIBLE;
            if (isGone) v.setVisibility(View.VISIBLE);
        }
    }

    private void setTime(Match record) {
        try {
            String string = TimeHelper.getWholeTimeFormat(record.getSchedule_starttime())
                    + "~" + TimeHelper.getTime2HmFormat(record.getSchedule_endtime());
            setTextViewText(schedule_time, string);
        } catch (Exception e) {
            e.printStackTrace();
            schedule_time.setText("");
        }
    }

    //我方输赢
    private void setResult() {
        int is_win = match.getTeam_a().getIs_win();
        int enemyWin = match.getTeam_b().getIs_win();
        if (is_win == 0) {//输
            myresult_text.setBackgroundColor(Color.parseColor("#ababab"));
            myresult_text.setText("我方败");

            uploadImage.setText("失败");
            uploadImage.setBackgroundResource(R.drawable.button_gray);
            my_bgLine.setBackgroundResource(R.drawable.match_userinfo_lose);
            setImageViewImageRes(result_signet, R.drawable.matchresult_lose_signet);
        }else {
            myresult_text.setBackgroundColor(Color.parseColor("#ff3d2e"));
            myresult_text.setText("我方胜");
        }

        if(enemyWin == 0){  //输
            enemyresult_text.setText("敌方败");
            enemyresult_text.setBackgroundColor(Color.parseColor("#ababab"));
        }else {
            enemyresult_text.setText("敌方胜");
            enemyresult_text.setBackgroundColor(Color.parseColor("#ff3d2e"));
        }
    }

    /**
     * 回调：已结束的赛事详情
     */
    public void onEventMainThread(MemberEndPKInfoEntity event) {
        if (event != null) {
            if (event.isResult() && event.getData().size() > 0 && event.getData() != null) {
                match = event.getData().get(0);
                currencyNum = event.getCurrencyNum();

                refreshData();
            }
        }
    }

    /**
     * 回调：客服名称
     */
    public void onEventMainThread(ServiceName217Entity event) {
        if (event != null && event.isResult()) {
            customerServiceName = event.getData().getName();
            Log.d(tag, "customerServiceName: " + customerServiceName);
        }
    }

    @Override
    public void updateProgress(String filePath, boolean result, int status, String msg, double percent) {
        Log.d(tag, "filePath=" + filePath);
        Log.d(tag, "result=" + result);
        Log.d(tag, "status=" + status);
        Log.d(tag, "msg=" + msg);
        Log.d(tag, "progress=" + percent);
        Log.d(tag, "~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        progressText.setVisibility(View.VISIBLE);
        uploadVideoText.setVisibility(View.GONE);
        int pro = (int) (percent * 100);
        if (pro - progress.getProgress() > 0) {
            progress.setProgress(pro);
            progressText.setText(pro + "%");
        }

        switch (status) {
            case Contants.STATUS_SUCCESS:
                progressText.setVisibility(View.GONE);
                uploadVideoText.setVisibility(View.VISIBLE);
                uploadVideoText.setText("视频上传成功");
                uploadVideoView.setFocusable(false);
                uploadVideoView.setClickable(false);
                progress.setProgress(0);
                break;
            case Contants.STATUS_FAILURE:
                progressText.setVisibility(View.GONE);
                uploadVideoText.setVisibility(View.VISIBLE);
                uploadVideoText.setText("视频上传失败");
                progress.setProgress(0);
                break;
        }
    }

    /**
     * 事件：完成上传视频
     */
    public void onEventMainThread(VideoUploadCompleteEvent event) {
        if (event != null) {
            //保存赛事视频
            DataManager.saveEventVideo(match.getTeam_a().getTeam_id(),
                    match.getTeam_a().getPk_id(),
                    event.getVideo_id(),
                    event_id,
                    getMember_id());
        }
    }

    /**
     * 回调：保存赛事视频
     */
    public void onEventMainThread(SaveEventVideoEntity event) {
        if (event != null) {
            Log.d(tag, "SaveEventVideoEntity: " + event.getMsg());
            UmengAnalyticsHelper.onEvent(this, UmengAnalyticsHelper.MATCH, "赛程-视频上传成功");
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        VideoShareTask208.removeCallbacks(this);
    }
}
