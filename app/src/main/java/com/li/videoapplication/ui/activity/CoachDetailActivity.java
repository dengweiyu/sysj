package com.li.videoapplication.ui.activity;

import android.graphics.Color;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import com.ifeimo.im.activity.ChatActivity;
import com.ifeimo.im.framwork.IMSdk;
import com.li.videoapplication.R;
import com.li.videoapplication.data.DataManager;
import com.li.videoapplication.data.image.GlideHelper;
import com.li.videoapplication.data.model.entity.Member;
import com.li.videoapplication.data.model.response.CoachCommentEntity;
import com.li.videoapplication.data.model.response.CoachDetailEntity;
import com.li.videoapplication.framework.TBaseAppCompatActivity;
import com.li.videoapplication.tools.FeiMoIMHelper;
import com.li.videoapplication.tools.ToastHelper;
import com.li.videoapplication.ui.ActivityManager;
import com.li.videoapplication.ui.DialogManager;
import com.li.videoapplication.ui.adapter.CoachBannerAdapter;
import com.li.videoapplication.ui.adapter.CoachCommentAdapter;
import com.li.videoapplication.ui.dialog.LoadingDialog;
import com.li.videoapplication.ui.view.SimpleItemDecoration;
import com.li.videoapplication.ui.view.SpanItemDecoration;
import com.li.videoapplication.utils.ScreenUtil;
import com.li.videoapplication.utils.StringUtil;
import com.li.videoapplication.views.CircleFlowIndicator;
import com.li.videoapplication.views.ViewFlow;
import java.util.ArrayList;
import java.util.List;

/**
 * 教练信息详情
 */

public class CoachDetailActivity extends TBaseAppCompatActivity implements View.OnClickListener,ViewFlow.ViewSwitchListener {

    private String mMemberId;
    private String mCoachNickName;
    private String mCoachAvatar;
    private String mCoachQQ;

    private View mDetailInfo;
    private View mGameInfo;
    private View mOperation;
    private View mCommentLayout;
    private RecyclerView mComment;
    private ImageView mIcon;
    private TextView mNickName;
    private TextView mScore;
    private RatingBar mRatingBar;
    private TextView mGame;
    private TextView mOrder;
    private TextView mRank;
    private ImageView mRankIcon;
    private TextView mDescription;

    private TextView mWin;
    private TextView mLose;
    private TextView mRate;

    private LoadingDialog mLoadingDialog;

    private CoachCommentAdapter mCommentAdapter;
    private ViewFlow mBannerFlow;
    private List<String> mUrls = new ArrayList<>();

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

        mCommentLayout = findViewById(R.id.cv_coach_detail_comment);
        mComment = (RecyclerView) findViewById(R.id.rv_coach_comment);
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
        mDescription = (TextView)findViewById(R.id.tv_coach_description);


        findViewById(R.id.tv_chat_with_coach).setOnClickListener(this);
        findViewById(R.id.tv_coach_selected).setOnClickListener(this);
        findViewById(R.id.ll_coach_all_comment).setOnClickListener(this);

        View view = findViewById(R.id.rv_coach_info_header);

        view.setOnClickListener(this);
        view.setBackgroundResource(R.drawable.background_press);
        mLoadingDialog = new LoadingDialog(this);
        mLoadingDialog.setProgressText("加载中..");


        mComment.setLayoutManager(new LinearLayoutManager(this));
        mComment.addItemDecoration(new SimpleItemDecoration(this,false,false,false,true));

        mComment.addItemDecoration(new SpanItemDecoration(ScreenUtil.dp2px(15),true,true,true,false));


        List<CoachCommentEntity.ADataBean> data = new ArrayList<>();
        mCommentAdapter = new CoachCommentAdapter(data);
        mComment.setAdapter(mCommentAdapter);
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

        findViewById(R.id.sv_coach_detail).setVisibility(View.VISIBLE);

        mCoachQQ = data.getQq();

        mDetailInfo.setVisibility(View.VISIBLE);
        mGameInfo.setVisibility(View.VISIBLE);
        mOperation.setVisibility(View.VISIBLE);


        GlideHelper.displayImageWhite(this,data.getAvatar(),mIcon);

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

        if (StringUtil.isNull(data.getIndividuality_signature())){
            findViewById(R.id.v_divider_description).setVisibility(View.GONE);
        }else {
            mDescription.setVisibility(View.VISIBLE);
            mDescription.setText(data.getIndividuality_signature());
        }

        mUrls = data.getPicture();
        setBannerView(mUrls);

    }


    private void setBannerView(List<String> urls) {
        mBannerFlow = (ViewFlow)findViewById(R.id.viewflow);
        CircleFlowIndicator bannerIndicator = (CircleFlowIndicator)findViewById(R.id.circleflowindicator);

        if (mUrls == null || mUrls.size() ==0){
            findViewById(R.id.rv_coach_banner).setVisibility(View.GONE);
        }else {


            findViewById(R.id.rv_coach_banner).setVisibility(View.VISIBLE);
            setBannerViewSize(mBannerFlow);
            mBannerFlow.setSideBuffer(urls.size()); // 初始化轮播图张数
            mBannerFlow.setFlowIndicator(bannerIndicator);
            mBannerFlow.setTimeSpan(4000);
            mBannerFlow.setSelection(0); // 设置初始位置

            mBannerFlow.setAdapter(new CoachBannerAdapter(this, urls,mBannerFlow));
            mBannerFlow.setOnViewSwitchListener(this);


            if (mUrls.size() <= 1){
                bannerIndicator.setVisibility(View.GONE);
            }else {
                bannerIndicator.setVisibility(View.VISIBLE);
            }
        }
    }

    private void setBannerViewSize(View view) {
        //750:350 = 15:7
        int w = srceenWidth;
        int h = w * 7 / 15;

        ViewGroup.LayoutParams params = view.getLayoutParams();
        params.height = h;
        params.width = w;
        view.setLayoutParams(params);

    }

    @Override
    public void onResume() {
        super.onResume();
        startAutoFlowTimer();
    }

    @Override
    public void onPause() {
        super.onPause();
        stopAutoFlowTimer();
    }

    @Override
    public void onSwitched(View view, int position) {
        startAutoFlowTimer();
    }

    @Override
    public void loadData() {
        super.loadData();
        if (mLoadingDialog != null){
            mLoadingDialog.show();
        }
        if (mMemberId != null){
            DataManager.getCoachDetail(mMemberId);
            DataManager.getCoachComment(mMemberId,"alone",1);
        }
    }

    public synchronized void startAutoFlowTimer() {
        if (mBannerFlow != null && !mBannerFlow.isAutoFlowTimer()) {
            // 自动播放
            mBannerFlow.startAutoFlowTimer();
        }
    }

    public synchronized void stopAutoFlowTimer() {
        if (mBannerFlow != null && mBannerFlow.isAutoFlowTimer()) {
            // 暂停播放
            mBannerFlow.stopAutoFlowTimer();
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
            case R.id.ll_coach_all_comment:                 //全部评价
                ActivityManager.startCoachAllCommentActivity(CoachDetailActivity.this,mMemberId);
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


    public void onEventMainThread(CoachCommentEntity entity){
        if (entity.isResult()){
            if ( mCommentAdapter.getData().size() >0 ){
                return;
            }
            if(entity.getAData() != null && entity.getAData().size() > 0){
                mCommentAdapter.setNewData(entity.getAData());
                mCommentLayout.setVisibility(View.VISIBLE);
            }else {
                mCommentLayout.setVisibility(View.GONE);
            }

        }else{

        }
    }
}
