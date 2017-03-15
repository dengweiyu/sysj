package com.li.videoapplication.mvp.match.view;

import android.app.Activity;
import android.graphics.Color;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.IPullToRefresh;
import com.li.videoapplication.R;
import com.li.videoapplication.data.DataManager;
import com.li.videoapplication.data.database.VideoCaptureEntity;
import com.li.videoapplication.data.local.ScreenShotEntity;
import com.li.videoapplication.data.model.entity.Match;
import com.li.videoapplication.data.model.event.UploadMatchPicEvent;
import com.li.videoapplication.data.model.event.VideoUploadCompleteEvent;
import com.li.videoapplication.data.model.response.MemberMatchPKEntity204;
import com.li.videoapplication.data.model.response.SaveEventResult204Entity;
import com.li.videoapplication.data.model.response.SaveEventVideoEntity;
import com.li.videoapplication.data.preferences.Constants;
import com.li.videoapplication.data.preferences.NormalPreferences;
import com.li.videoapplication.data.upload.Contants;
import com.li.videoapplication.data.upload.ImageShareResponseObject;
import com.li.videoapplication.data.upload.VideoShareTask208;
import com.li.videoapplication.framework.AppManager;
import com.li.videoapplication.framework.TBaseFragment;
import com.li.videoapplication.tools.BitmapHelper;
import com.li.videoapplication.tools.TimeHelper;
import com.li.videoapplication.tools.UmengAnalyticsHelper;
import com.li.videoapplication.ui.ActivityManeger;
import com.li.videoapplication.ui.DialogManager;
import com.li.videoapplication.ui.activity.MyMatchProcessActivity;
import com.li.videoapplication.tools.ToastHelper;
import com.li.videoapplication.ui.activity.VideoShareActivity;
import com.li.videoapplication.utils.StringUtil;
import com.li.videoapplication.utils.TextUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import io.rong.imkit.RongIM;
import me.everything.android.ui.overscroll.OverScrollDecoratorHelper;

/**
 * 碎片：我的赛程（进行中）
 */
public class MyOnGoingMatchFragment extends TBaseFragment implements View.OnClickListener,
        VideoShareTask208.Callback {
    private MyMatchProcessActivity activity;
    private String event_id;
    private String pk_id;
    private TextView process, schedule_time;
    private TextView a_team_name, a_leader_game_role;
    private TextView b_leader_game_role, b_team_name;
    private TextView notice1, notice2, notice3;
    private TextView uploadVideoText, progressText;
    private View uploadVideoView;
    private ImageView a_avatar, b_avatar, b_contact_icon;
    private Match match;
    private String currencyNum;
    private Match matchDetailMatch;

    private ScrollView haveData;
    public Button uploadImage;
    private String uploadDeadline, after20minOfStartTime;
    public View contact, noData;
    private ImageView contact_img;
    private TextView contact_text;
    private ProgressBar progress;
    private File tempImage;
    private View uploadImageHint;
    private String videoPath;

    /**
     * 跳转：上传比赛结果截图页面
     */
    private void startUploadMatchResultImageActivity() {
        ActivityManeger.startUploadMatchResultImageActivity(getActivity(),
                matchDetailMatch,
                pk_id,
                match.getTeam_a().getTeam_id(),
                match.getSchedule().getOver_time(),
                match.getSchedule().getIs_last(),
                activity.customerServiceID,
                activity.customerServiceName);
    }

    /**
     * 跳转：选择上传视频
     */
    private void startVideoChooseActivity() {
        ActivityManeger.startVideoChooseActivity(getActivity(), null, VideoShareActivity.TO_FINISH);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            this.activity = (MyMatchProcessActivity) activity;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected int getCreateView() {
        return R.layout.fragment_myongoingmatch;
    }

    @Override
    protected void initContentView(View view) {
        initView(view);
    }

    private void initView(View view) {
        VideoShareTask208.addCallbacks(this);

        haveData = (ScrollView) view.findViewById(R.id.havedata_root);
        OverScrollDecoratorHelper.setUpOverScroll(haveData);
        noData = view.findViewById(R.id.ongoing_nodata);
        TextView emptyText = (TextView) view.findViewById(R.id.emptyview_text);
        emptyText.setText("暂无进行中的赛程");

        uploadVideoView = view.findViewById(R.id.ongoing_progress_container);
        progress = (ProgressBar) view.findViewById(R.id.ongoing_progress);
        uploadVideoText = (TextView) view.findViewById(R.id.ongoing_uploadtext);
        progressText = (TextView) view.findViewById(R.id.ongoing_progress_text);

        contact = view.findViewById(R.id.ongoing_contact);
        contact_img = (ImageView) view.findViewById(R.id.ongoing_contact_img);
        contact_text = (TextView) view.findViewById(R.id.ongoing_contact_text);

        process = (TextView) view.findViewById(R.id.ongoing_process);
        schedule_time = (TextView) view.findViewById(R.id.ongoing_time);
        a_avatar = (ImageView) view.findViewById(R.id.ongoing_myicon);
        a_leader_game_role = (TextView) view.findViewById(R.id.ongoing_myname);
        a_team_name = (TextView) view.findViewById(R.id.ongoing_myteamname);
        b_avatar = (ImageView) view.findViewById(R.id.ongoing_enemyicon);
        b_contact_icon = (ImageView) view.findViewById(R.id.blue_contact_icon);
        b_leader_game_role = (TextView) view.findViewById(R.id.ongoing_enemyname);
        b_team_name = (TextView) view.findViewById(R.id.ongoing_enemyteamname);
        uploadImage = (Button) view.findViewById(R.id.upload_image);
        uploadImageHint = view.findViewById(R.id.upload_image_hint);

        notice1 = (TextView) view.findViewById(R.id.ongoing_notice1);
        notice2 = (TextView) view.findViewById(R.id.ongoing_notice2);
        notice3 = (TextView) view.findViewById(R.id.ongoing_notice3);

        view.findViewById(R.id.ongoing_customerservice).setOnClickListener(this);
        uploadImage.setOnClickListener(this);
        uploadVideoView.setOnClickListener(this);
        contact.setOnClickListener(this);
        b_avatar.setOnClickListener(this);
    }

    private void loadData() {
        if (activity != null) {
            if (activity.event_id != null) {
                event_id = activity.event_id;
                DataManager.getMemberMatchPK210(getMember_id(), event_id);
            }
            if (activity.matchDetailMatch != null) {
                matchDetailMatch = activity.matchDetailMatch;
            }
        }
    }

    //fragment处于当前交互状态
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {//该fragment处于最前台交互状态
            //刷新数据
            loadData();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        VideoShareTask208.removeCallbacks(this);
    }

    private void refreshData() {
        Log.d(tag, "refreshData: ");
        noData.setVisibility(View.GONE);
        haveData.setVisibility(View.VISIBLE);

        Match record;
        if (match != null) {
            record = match.getSchedule();
            process.setText(TextUtil.numberAtRed(record.getName(), "#ff3d2e"));
            setTime(record);
            try {
                uploadDeadline = TimeHelper.getMMddHHmmTimeFormat(record.getResult_pic());//上传截图截止时间
                after20minOfStartTime = TimeHelper.after20minFormat(record.getSchedule_starttime());//比赛开始后20分钟
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (match != null && match.getTeam_a() != null) {
            record = match.getTeam_a();
            pk_id = record.getPk_id();
            setImageViewImageNet(a_avatar, record.getAvatar());
            setTextViewText(a_leader_game_role, record.getLeader_game_role());
            if (record.getTeam_name() != null && !record.getTeam_name().equals("")) {
                a_team_name.setVisibility(View.VISIBLE);
                setTextViewText(a_team_name, "战队名：" + record.getTeam_name());
            } else {
                a_team_name.setVisibility(View.GONE);
            }
            if (record.getIs_uppic().equals("1")) {//已上传截图
                uploadImage.setText("继续上传");
                uploadImageHint.setVisibility(View.VISIBLE);
            }
            if (record.getIs_upvideo().equals("1")) {//已上传视频
                uploadVideoText.setText("已上传视频");
                uploadVideoView.setFocusable(false);
                uploadVideoView.setClickable(false);
            }
        }
        if (match != null && match.getTeam_b() != null) {
            //1：已发布对战信息
            boolean isPublicMatchList = match.getSchedule().getPk_status().equals("1");
            //1：即时匹配
            boolean isOnce = match.getSchedule().getIs_once().equals("1");

            String b_leader_id = match.getTeam_b().getLeader_id();

            //即时匹配：需要签到--已签到--已经发布对战表--是即时匹配--B=null --> 匹配中
            //手动匹配：需要签到--已签到--未发布对战表 -->匹配中
            //反之：已经发布对战表--非即时匹配--B!=null --> 正常显示，其余匹配中。
            //我劝你不要动，这里逻辑很乱
            if (isPublicMatchList) {
                if (isOnce && StringUtil.isNull(b_leader_id)) {
                    showMatchingView();
                } else {
                    showNormalView();
                }
            } else {
                showMatchingView();
            }
        }

        //引导句子1：匹配对手成功后，点击约战TA进行比赛，如果对方未在19:00前与您对战，则截屏聊天记录或其他截图证明上传。
        //红色：#ff3d2e，蓝色：#48c5ff
        String s1 = "点击" + TextUtil.toColor("约战TA", "#48c5ff") + "进行比赛，如果对方未在" +
                TextUtil.toColor(after20minOfStartTime, "#ff3d2e") + "前回复，" +
                TextUtil.toColor("截屏聊天记录上传即判你获胜", "#ff3d2e");
        notice1.setText(Html.fromHtml(s1));

        //引导句子2  红色：#ff3d2e
        String s2 = TextUtil.toColor("胜方", "#ff3d2e") + "请在" + TextUtil.toColor(uploadDeadline, "#ff3d2e") + "前上传比赛结果截图";
        notice2.setText(Html.fromHtml(s2));

        //引导句子3  红色：#ff3d2e，蓝色：#48c5ff
        String s3 = "上传" + TextUtil.toColor("比赛视频", "#48c5ff") + "奖励" + TextUtil.toColor(currencyNum, "#ff3d2e") + "飞磨豆";
        notice3.setText(Html.fromHtml(s3));
    }

    //显示匹配中视图
    private void showMatchingView() {
        Log.d(tag, "showMatchingView: ");
        b_team_name.setVisibility(View.GONE);
        b_contact_icon.setVisibility(View.GONE);
        setTextViewText(b_leader_game_role, "匹配中");
        contact_text.setTextColor(Color.parseColor("#cacaca"));//gray
        setImageViewImageRes(contact_img, R.drawable.ongoing_contact_gray);
        uploadImage.setBackgroundResource(R.drawable.button_gray);
    }

    //显示有数据时正常显示视图
    private void showNormalView() {
        Log.d(tag, "showNormalView: ");
        Match record;
        b_team_name.setVisibility(View.VISIBLE);
        b_contact_icon.setVisibility(View.VISIBLE);
        contact_text.setTextColor(Color.parseColor("#48c5ff"));//blue
        setImageViewImageRes(contact_img, R.drawable.ongoing_contact_blue);

        record = match.getTeam_b();
        String enemy_leader_name = record.getLeader_game_role();

        setImageViewImageNet(b_avatar, record.getAvatar());
        setTextViewText(b_leader_game_role, enemy_leader_name);
        if (record.getTeam_name() != null && !record.getTeam_name().equals("")) {
            b_team_name.setVisibility(View.VISIBLE);
            setTextViewText(b_team_name, "战队名：" + record.getTeam_name());
        } else {
            b_team_name.setVisibility(View.GONE);
        }

        //有对战信息时，显示遮罩引导
        showContactTipDialog();
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

    @Override
    protected IPullToRefresh getPullToRefresh() {
        return null;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.upload_image://上传比赛截图
            case R.id.ongoing_progress_container://上传视频
                long currentTime = 0;
                try {
                    currentTime = TimeHelper.getCurrentTime();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Long uploadDeadlineLong = Long.valueOf(match.getSchedule().getResult_pic());

                if (match != null && !StringUtil.isNull(match.getTeam_b().getLeader_id())) {
                    if (currentTime > uploadDeadlineLong) {
                        ToastHelper.s("已超过上传时间");
                    } else {
                        if (v.getId() == R.id.upload_image) {
                            DialogManager.showUploadPicDialog(getActivity());
                            UmengAnalyticsHelper.onEvent(getActivity(), UmengAnalyticsHelper.MATCH, "我的赛程-上传截图");
                        } else if (v.getId() == R.id.ongoing_progress_container) {
                            if (!VideoShareTask208.isUploading()) {
                                startVideoChooseActivity();
                                UmengAnalyticsHelper.onEvent(getActivity(), UmengAnalyticsHelper.MATCH, "我的赛程-上传视频");
                            }
                        }
                    }
                } else {
                    ToastHelper.s("对手匹配中");
                }
                break;
            case R.id.ongoing_contact://约战
                b_avatar.performClick();
                UmengAnalyticsHelper.onEvent(getActivity(), UmengAnalyticsHelper.MATCH, "我的赛程-约战TA");
                break;
            case R.id.ongoing_enemyicon://敌方头像
                if (RongIM.getInstance() != null && match != null && !StringUtil.isNull(match.getTeam_b().getLeader_id())) {
                    ActivityManeger.startConversationActivity(getActivity(),
                            match.getTeam_b().getLeader_id(),
                            match.getTeam_b().getLeader_game_role(),
                            true,
                            match.getTeam_b().getQq());
                }
                UmengAnalyticsHelper.onEvent(getActivity(), UmengAnalyticsHelper.MATCH, "我的赛程-对方头像");
                break;
            case R.id.ongoing_customerservice://客服
                if (RongIM.getInstance() != null && activity != null &&
                        activity.customerServiceID != null && activity.customerServiceName != null) {

                    ActivityManeger.startConversationActivity(getActivity(),
                            activity.customerServiceID,
                            activity.customerServiceName,
                            false);
                }
                break;
        }
    }

    /**
     * 遮罩提示页：约战
     */
    public void showContactTipDialog() {

        boolean tip = NormalPreferences.getInstance().getBoolean(Constants.TIP_ONGOING_CONTACT, true);
        if (tip) {
            DialogManager.showOnGoingContactTipDialog(getActivity());

            NormalPreferences.getInstance().putBoolean(Constants.TIP_ONGOING_CONTACT, false);
        }
    }

    /**
     * 回调：进行中赛事
     */
    public void onEventMainThread(MemberMatchPKEntity204 event) {
        if (event != null) {
            if (event.isResult() && event.getData().size() > 0 && event.getData() != null) {
                match = event.getData().get(0);
                currencyNum = event.getCurrencyNum();
                //是否需要签到
                boolean isNeedSign = match.getSchedule().getIs_sign().equals("1");
                //是否已签到
                boolean isSigned = !StringUtil.isNull(match.getSchedule().getSign_times());
                //是否已发布对战信息
                boolean isPublicMatchList = match.getSchedule().getPk_status().equals("1");

                if (isNeedSign) {
                    if (isSigned) {
                        refreshData();
                    } else {
                        showNoDataView();
                    }
                } else { //不需要签到
                    if (isPublicMatchList) { //已发布
                        refreshData();
                    } else { //未发布
                        showNoDataView();
                    }
                }
            } else {
                showNoDataView();
            }
        }
    }

    private void showNoDataView() {
        noData.setVisibility(View.VISIBLE);
        haveData.setVisibility(View.GONE);
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
        videoPath = filePath;
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
            UmengAnalyticsHelper.onEvent(getActivity(), UmengAnalyticsHelper.MATCH, "赛程-视频上传成功");
        }
    }

    /**
     * 事件：赛事上传图片事件
     */
    public void onEventMainThread(UploadMatchPicEvent event) {
        if (event != null && event.getData() != null) {
            showProgressDialog2("正在压缩...");
            uploadImage(event.getData());
        }
    }

    /**
     * 上传图片到服务器
     */
    private void uploadImage(List<ScreenShotEntity> data) {
        compressBitmap(data);//压缩图片（最大500k）
        List<ScreenShotEntity> list = new ArrayList<>();
        list.addAll(data);
        list.remove(list.size() - 1);

        // 上传图片服务
        DataManager.UPLOAD.uploadImage204(pk_id, getMember_id(), list, match.getTeam_a().getTeam_id());
    }

    //压缩图片（最大500k）
    private void compressBitmap(List<ScreenShotEntity> data) {
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

    /**
     * 回调：上传图片
     */
    public void onEventMainThread(ImageShareResponseObject event) {
        int status = event.getStatus();
        String msg = event.getMsg();
        boolean result = event.isResult();
        if (status == Contants.STATUS_START) {
            setProgressText("开始上传");
        } else if (status == Contants.STATUS_PREPARING) {
            setProgressText("正在上传");
        } else if (status == Contants.STATUS_UPLOADING) {
            setProgressText("正在上传");
        } else if (status == Contants.STATUS_COMPLETING) {
            setProgressText("正在上传");
        } else if (status == Contants.STATUS_END) {
            dismissProgressDialog();
            if (tempImage.exists()) {
                boolean delete = tempImage.delete();
                Log.d(tag, "uploadImage: tempImage.delete(): " + delete);
            }
            if (result)
                dismissProgressDialog();
            else
                showToastLong(msg);
        }
    }

    /**
     * 回调：上传图片
     */
    public void onEventMainThread(SaveEventResult204Entity event) {
        if (event != null && event.isResult() && event.getData() != null) {
            if (match.getSchedule().getIs_last().equals("0")) {//非最后一轮
                try {
                    String over_time = TimeHelper.getMMddHHmmTimeFormat2(match.getSchedule().getOver_time());//比赛结束时间
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
            loadData();
            UmengAnalyticsHelper.onEvent(getActivity(), UmengAnalyticsHelper.MATCH, "赛程-截图上传成功");
        }
    }

    //对话框点击确认后的事件
    @Override
    protected void confirmButtonEvent() {
        super.confirmButtonEvent();
    }
}
