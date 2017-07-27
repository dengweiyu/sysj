package com.li.videoapplication.ui.activity;

import android.graphics.Color;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.ifeimo.im.activity.ChatActivity;
import com.ifeimo.im.framwork.IMSdk;
import com.ifeimo.im.framwork.Proxy;
import com.li.videoapplication.R;
import com.li.videoapplication.data.DataManager;
import com.li.videoapplication.data.image.GlideHelper;
import com.li.videoapplication.data.model.entity.Member;
import com.li.videoapplication.data.model.response.CoachDetailEntity;
import com.li.videoapplication.data.network.UITask;
import com.li.videoapplication.framework.TBaseAppCompatActivity;
import com.li.videoapplication.tools.FeiMoIMHelper;
import com.li.videoapplication.tools.ToastHelper;
import com.li.videoapplication.ui.ActivityManager;
import com.li.videoapplication.ui.DialogManager;
import com.li.videoapplication.ui.dialog.LoadingDialog;
import com.li.videoapplication.utils.ScreenUtil;
import com.li.videoapplication.utils.StringUtil;

/**
 * 教练信息详情
 */

public class CoachDetailActivity extends TBaseAppCompatActivity implements View.OnClickListener {

    private String mMemberId;
    private String mCoachNickName;
    private String mCoachAvatar;
    private String mCoachQQ;

    private View mDetailInfo;
    private View mGameInfo;
    private View mOperation;
    private ImageView mIcon;
    private TextView mNickName;
    private TextView mScore;
    private RatingBar mRatingBar;
    private TextView mGame;
    private TextView mOrder;
    private TextView mRank;
    private ImageView mRankIcon;

    private TextView mWin;
    private TextView mLose;
    private TextView mRate;

    private LoadingDialog mLoadingDialog;

    @Override
    public void refreshIntent() {
        super.refreshIntent();

        try {
            mMemberId = getIntent().getStringExtra("member_id");
            mCoachNickName = getIntent().getStringExtra("nick_name");
            mCoachAvatar = getIntent().getStringExtra("avatar");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public int getContentView() {
        return R.layout.activity_coach_detail;
    }

    @Override
    public void initView() {
        super.initView();
        initToolbar();
        mIcon = (ImageView)findViewById(R.id.civ_coach_detail_icon);
        mNickName = (TextView)findViewById(R.id.tv_coach_detail_nick_name);
        mScore = (TextView)findViewById(R.id.tv_coach_detail_score);

        mRatingBar = (RatingBar)findViewById(R.id.rb_coach_detail_score);
        mGame = (TextView)findViewById(R.id.tv_coach_game_description);

        mDetailInfo = findViewById(R.id.cv_coach_detail_info);
        mGameInfo = findViewById(R.id.cv_coach_detail_game);
        mOperation = findViewById(R.id.ll_coach_operation);

        mOrder = (TextView) findViewById(R.id.tv_coach_detail_order);
        mRank = (TextView)findViewById(R.id.tv_coach_rank);
        mRankIcon = (ImageView)findViewById(R.id.iv_coach_rank_icon);

        mWin = (TextView)findViewById(R.id.tv_coach_detail_win_num);
        mLose = (TextView)findViewById(R.id.tv_coach_detail_lose_num);

        mRate = (TextView)findViewById(R.id.tv_coach_detail_rate);

        findViewById(R.id.tv_chat_with_coach).setOnClickListener(this);
        findViewById(R.id.tv_coach_selected).setOnClickListener(this);

        View view = findViewById(R.id.rv_coach_info_header);

        view.setOnClickListener(this);
        view.setBackgroundResource(R.drawable.background_press);
        mLoadingDialog = new LoadingDialog(this);
        mLoadingDialog.setProgressText("加载中..");
    }

    private void initToolbar(){
        findViewById(R.id.tb_back).setOnClickListener(this);
        ((TextView)findViewById(R.id.tb_title)).setText("陪练信息");
        TextView right = (TextView)findViewById(R.id.tb_topup_record);
        right.setVisibility(View.VISIBLE);
        right.setText("价格");
        right.setTextColor(Color.parseColor("#575757"));
        right.setOnClickListener(this);
        right.setPadding(ScreenUtil.dp2px(this,8),right.getTop(),ScreenUtil.dp2px(this,8),right.getBottom());
        right.setOnClickListener(this);

        setSupportActionBar(((Toolbar)findViewById(R.id.toolbar)));
    }

    private void refreshData(final CoachDetailEntity.DataBean data){
        if (data == null){
            return;
        }

        mCoachQQ = data.getQq();

        mDetailInfo.setVisibility(View.VISIBLE);
        mGameInfo.setVisibility(View.VISIBLE);
        mOperation.setVisibility(View.VISIBLE);

        //头像这个控件和 Glide一起用 有问题
        GlideHelper.displayImage(this,data.getAvatar(),mIcon);
        UITask.postDelayed(new Runnable() {
            @Override
            public void run() {
                GlideHelper.displayImage(CoachDetailActivity.this,data.getAvatar(),mIcon);
            }
        },500);
        mNickName.setText(data.getName());
        mGame.setText(data.getGame_name());

        mScore.setText(data.getScore()+"分");
        try {
            mRatingBar.setRating(Float.parseFloat(data.getScore()));
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

        try {
            mOrder.setText(StringUtil.toUnitW(data.getOrder_total()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        mRank.setText(data.getGame_level());

        mWin.setText(data.getWin_num()+"胜");
        mLose.setText(data.getLose_num()+"负");
        mRate.setText(data.getWin_rate());

        if (data.getStatusX() == 2){
            findViewById(R.id.tv_tip_playing).setVisibility(View.VISIBLE);
        }else {
            findViewById(R.id.tv_tip_playing).setVisibility(View.GONE);
        }
    }


    @Override
    public void loadData() {
        super.loadData();
        if (mLoadingDialog != null){
            mLoadingDialog.show();
        }
        if (mMemberId != null){
            DataManager.getCoachList(mMemberId);
        }
    }

    /**
     * 教练详情信息
     */
    public void onEventMainThread(CoachDetailEntity entity){
        if (entity != null && entity.isResult() && entity.getData() != null){
            refreshData(entity.getData());
        }else {

        }
        if (mLoadingDialog != null){
            mLoadingDialog.dismiss();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tb_back:
                finish();
                break;
            case R.id.tb_topup_record:                      //价格
                WebActivityJS.startWebActivityJS(CoachDetailActivity.this,"https://sapp.17sysj.com/Sysj222/Coach/price","价格","");
                break;
            case R.id.tv_chat_with_coach:                   //私聊
                chatWith();
                break;
            case R.id.tv_coach_selected:                    //选TA陪练
                if (getMember_id().equals(mMemberId)){
                    ToastHelper.l("不能为自己下单哦~");
                    return;
                }
                ActivityManager.startCreatePlayWithOrderActivity(CoachDetailActivity.this,mMemberId,mCoachNickName,mCoachAvatar,mCoachQQ);
                break;
            case R.id.rv_coach_info_header:                 //个人中心
                startPersonCenterActivity();
                break;
        }
    }

    /**
     * 私聊
     */
    private void chatWith(){
        Member user = getUser();

        if (StringUtil.isNull(getMember_id())){
            DialogManager.showLogInDialog(this);
            return;
        }

        if (getMember_id().equals(mMemberId)){
            ToastHelper.s("不能和自己聊天哦~");
            return;
        }

        FeiMoIMHelper.Login(user.getId(), user.getNickname(), user.getAvatar());
        IMSdk.createChat(
                this,
                mMemberId,
                mCoachNickName,
                mCoachAvatar,
                ChatActivity.SHOW_FAST_REPLY,
                mCoachQQ);
    }

    /**
     * 跳转个人中心
     */
    private void startPersonCenterActivity(){
        Member member = new Member();
        member.setMember_id(mMemberId);
        member.setId(mMemberId);
        ActivityManager.startPlayerDynamicActivity(this,member);
    }
}
