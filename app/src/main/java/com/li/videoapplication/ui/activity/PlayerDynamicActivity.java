package com.li.videoapplication.ui.activity;

import android.graphics.Color;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.JsonSyntaxException;
import com.li.videoapplication.R;
import com.li.videoapplication.data.DataManager;
import com.li.videoapplication.data.image.GlideHelper;
import com.li.videoapplication.data.model.entity.Member;
import com.li.videoapplication.data.model.entity.VideoImage;
import com.li.videoapplication.data.model.response.GroupAttentionGroupEntity;
import com.li.videoapplication.data.model.response.MemberAttention201Entity;
import com.li.videoapplication.data.model.response.UserProfilePersonalInformationEntity;
import com.li.videoapplication.data.model.response.UserProfileTimelineListsEntity;
import com.li.videoapplication.data.model.response.VideoCollect2Entity;
import com.li.videoapplication.data.model.response.VideoFlower2Entity;
import com.li.videoapplication.data.network.UITask;
import com.li.videoapplication.framework.PullToRefreshActivity;
import com.li.videoapplication.tools.UmengAnalyticsHelper;
import com.li.videoapplication.ui.ActivityManager;
import com.li.videoapplication.ui.DialogManager;
import com.li.videoapplication.ui.adapter.DynamicVideoAdapter;
import com.li.videoapplication.utils.StringUtil;
import com.li.videoapplication.views.CircleImageView;

import io.rong.eventbus.EventBus;


/**
 * 活动：玩家动态
 */
public class PlayerDynamicActivity extends PullToRefreshActivity<VideoImage> implements
        AbsListView.OnScrollListener,
        OnClickListener {

    /**
     * 跳转：玩家个人资料
     */
    public void startPlayePersonalInfoActivity(Member member) {
        ActivityManager.startPlayerPersonalInfoActivity(this, member);
    }

    /**
     * 跳转：我的粉丝
     */
    public void startMyPlayerActivityMyFans() {
        ActivityManager.startMyPlayerActivity(this, MyPlayerActivity.PAGE_MYFANS, item.getMember_id());
    }

    /**
     * 跳转：我的关注
     */
    public void startMyPlayerActivityMyFocus() {
        ActivityManager.startMyPlayerActivity(this, MyPlayerActivity.PAGE_MYFOCUS, item.getMember_id());
    }


    private DynamicVideoAdapter adapter;
    private View headerView;
    private View user, tourist, focusView;
    private CircleImageView head;
    private TextView loginText, name, fans, attention, introduce, focus;
    private RelativeLayout touch;
    private ImageView loginIcon, textBg, go, text, textBtn, head_v;

    private int currentpage = 1;
    private int pagelength = 10;

    private Member item;
    private TextView emptyText;

    private int attent;

    private TextView rewardGift;
    private ImageView mFirstGift;
    private ImageView mSecondGift;

    @Override
    public void refreshIntent() {
        super.refreshIntent();
        item = (Member) getIntent().getSerializableExtra("member");
        if (item == null)
            finish();
        if (StringUtil.isNull(item.getId())) {
            finish();
        }
    }

    @Override
    public int getContentView() {
        return R.layout.activity_mydynamic;
    }

    public int inflateActionBar() {
        return R.layout.actionbar_second;
    }

    @Override
    public void beforeOnCreate() {
        super.beforeOnCreate();
        requestWindowFeature(Window.FEATURE_ACTION_BAR_OVERLAY);
        setSystemBar(false);
    }

    @Override
    public void afterOnCreate() {
        super.afterOnCreate();
        setAbTitle(R.string.playerdynamic_title);
    }

    @Override
    public void initView() {
        super.initView();

        setModeEnd();
        setOnScrollListener(this);

        getHeaderView();
        refreshHeaderView(item);
    }

    @Override
    public void loadData() {
        super.loadData();

        // 个人资料
        DataManager.userProfilePersonalInformation(item.getId(), getMember_id());
        UITask.postDelayed(new Runnable() {

            @Override
            public void run() {

                onPullDownToRefresh(pullToRefreshListView);
            }
        }, 400);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (attent != item.getIsAttent()){       //如果关注发生改变
            EventBus.getDefault().post(new GroupAttentionGroupEntity());
        }
    }

    private View getHeaderView() {
        if (headerView == null) {
            emptyText = (TextView) findViewById(R.id.dynamic_empty);
            emptyText.setText("该玩家还没有动态喔~");

            headerView = inflater.inflate(R.layout.header_dynamic, null);
            head = (CircleImageView) headerView.findViewById(R.id.dynamic_head);
            head_v = (ImageView) headerView.findViewById(R.id.dynamic_v);

            user = headerView.findViewById(R.id.dynamic_user);
            tourist = headerView.findViewById(R.id.dynamic_tourist);

            loginIcon = (ImageView) headerView.findViewById(R.id.dynamic_loginIcon);
            loginText = (TextView) headerView.findViewById(R.id.dynamic_loginText);

            name = (TextView) headerView.findViewById(R.id.dynamic_name);
            fans = (TextView) headerView.findViewById(R.id.dynamic_fans);
            attention = (TextView) headerView.findViewById(R.id.dynamic_attention);
            introduce = (TextView) headerView.findViewById(R.id.dynamic_introduce);
            focusView = headerView.findViewById(R.id.dynamic_focus_layout);
            focus = (TextView) headerView.findViewById(R.id.dynamic_focus);
            text = (ImageView) headerView.findViewById(R.id.dynamic_text);
            textBtn = (ImageView) headerView.findViewById(R.id.dynamic_textBtn);
            textBg = (ImageView) headerView.findViewById(R.id.dynamic_textBg);
            go = (ImageView) headerView.findViewById(R.id.dynamic_go);
            touch = (RelativeLayout) headerView.findViewById(R.id.dynamic_touch);

            rewardGift = (TextView)headerView.findViewById(R.id.tv_receive_gift);
            mFirstGift = (ImageView) headerView.findViewById(R.id.iv_first_gift);
            mSecondGift = (ImageView) headerView.findViewById(R.id.iv_second_gift);
            headerView.findViewById(R.id.ll_receive_gift).setOnClickListener(this);

            rewardGift.setOnClickListener(this);

            loginIcon.setOnClickListener(this);
            loginText.setOnClickListener(this);

            head.setOnClickListener(this);
            textBtn.setOnClickListener(this);
            focusView.setOnClickListener(this);
            touch.setOnClickListener(this);
            go.setOnClickListener(this);
            fans.setOnClickListener(this);
            attention.setOnClickListener(this);

            textBtn.setVisibility(View.GONE);
            focusView.setVisibility(View.VISIBLE);
            text.setVisibility(View.INVISIBLE);
            setListViewLayoutParams(headerView, 170);
        }
        return headerView;
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onScroll(AbsListView listView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

        if (firstVisibleItem > 1) {
            // 白色背景
            setAbStyleWhite();
            if (StringUtil.isNull(item.getNickname())) {
                setAbTitle(item.getNickname());
            }
        } else if (firstVisibleItem <= 1) {
            // 透明背景
            setAbTitle(R.string.playerdynamic_title);
            setAbStyleTranceparent();
        }
    }

    @Override
    public void onScrollStateChanged(AbsListView listview, int scrollState) {
    }

    /**
     * 页头数据
     */
    private void refreshHeaderView(Member item) {
        if (item != null && headerView != null) {// 登录用户
            user.setVisibility(View.VISIBLE);
            tourist.setVisibility(View.GONE);
            setAbTitle(item.getNickname());
            setImageViewImageNet(head, item.getAvatar());
            if (item.isV())
                head_v.setVisibility(View.VISIBLE);
            setTextViewText(name, item.getNickname());
            if (StringUtil.isNull(item.getSignature())) {
                introduce.setText("");
            } else {
                introduce.setText(item.getSignature());
            }
            setMark(fans, attention, item);
            setFocus(item);
            if (!StringUtil.isNull(item.getCover())) {
                setImageViewImageNet(textBg, item.getCover());
            }

            attent = item.getIsAttent();

            setAbBackgroundTranceparent();
            setAbGobackWhite();
            setAbTitleWhite();

            //礼物
            if (item.getRewardInfo() == null||item.getRewardInfo().getGift_icon() == null||!item.getRewardInfo().isHasGift()){
                rewardGift.setText("收到的礼物：暂无");
            }else {
                rewardGift.setText("收到的礼物：");
                if (item.getRewardInfo().getGift_icon().size() >= 1){
                    if (mFirstGift != null){
                        GlideHelper.displayImage(this,item.getRewardInfo().getGift_icon().get(0),mFirstGift);
                    }
                }
                if (item.getRewardInfo().getGift_icon().size() >= 2){
                    if (mSecondGift != null){
                        GlideHelper.displayImage(this,item.getRewardInfo().getGift_icon().get(1),mSecondGift);
                    }
                }
            }
        }
    }

    /**
     * 关注
     */
    public void setFocus(Member item) {
        if (item != null) {
            if (item.getIsAttent() == 1) {// 已关注
                focusView.setBackgroundColor(Color.parseColor("#e2e2e2"));
                setTextViewText(focus, R.string.dynamic_focused);
            } else { // 未关注
                focusView.setBackgroundResource(R.drawable.gamematch_cs_bg);
                setTextViewText(focus, R.string.dynamic_focus);
            }
        }
    }

    /**
     * 内容
     *
     * @return 粉丝：11 | 关注：555
     */
    private void setMark(TextView fans, TextView attention, Member item) {
        if (!StringUtil.isNull(item.getFans())) {
            setTextViewText(fans, "粉丝 : " + item.getFans());
        }
        if (!StringUtil.isNull(item.getAttention())) {
            setTextViewText(attention, "关注 : " + item.getAttention());
        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.dynamic_go:
                startPlayePersonalInfoActivity(item);
                break;

            case R.id.dynamic_focus_layout:
                if (!isLogin()) {
                    DialogManager.showLogInDialog(this);
                    return;
                }

                if (item.getIsAttent() == 1) {
                    DialogManager.showConfirmDialog(PlayerDynamicActivity.this, "确认取消关注该玩家?", new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            switch (v.getId()){
                                case R.id.tv_confirm_dialog_yes:
                                    item.setIsAttent(0);
                                    // 玩家关注
                                    DataManager.memberAttention201(item.getMember_id(), getMember_id());
                                    setFocus(item);
                                    break;
                            }
                        }
                    });
                } else {
                    item.setIsAttent(1);
                    // 玩家关注
                    DataManager.memberAttention201(item.getMember_id(), getMember_id());
                    setFocus(item);
                }

                break;
            case R.id.dynamic_fans:
                if (!isLogin()) {
                    DialogManager.showLogInDialog(this);
                    return;
                }
                startMyPlayerActivityMyFans();
                break;

            case R.id.dynamic_attention:
                if (!isLogin()) {
                    DialogManager.showLogInDialog(this);
                    return;
                }
                startMyPlayerActivityMyFocus();
                break;
            case R.id.tv_receive_gift:
            case R.id.ll_receive_gift:
                ActivityManager.startMyGiftBillActivity(this,item.getMember_id());
                break;
        }
    }

    @Override
    public void onRefresh() {
        // 个人视频（时间轴）
        DataManager.userProfileTimelineLists(getMember_id(), item.getMember_id(), currentpage, pagelength);
    }

    @Override
    public void onLoadMore() {
        // 个人视频（时间轴）
        DataManager.userProfileTimelineLists(getMember_id(), item.getMember_id(), currentpage, pagelength);
    }

    /**
     * 回调：个人资料
     */
    public void onEventMainThread(UserProfilePersonalInformationEntity event) {

        if (event.isResult()) {
            Member m = null;
            Member member = event.getData();
            try {
                m = (Member) member.clone();
                m.setMember_id(member.getId());
            } catch (JsonSyntaxException e) {
                e.printStackTrace();
            }
            if (m != null) {
                if (item != null ){
                    if(item.getId().equals(m.getMember_id())){
                        item = m;
                        refreshHeaderView(item);
                    }
                }else {
                    item = m;
                    refreshHeaderView(item);
                }
            }

        }
    }

    /**
     * 回调：个人视频（时间轴）
     */
    public void onEventMainThread(UserProfileTimelineListsEntity event) {

        if (event.isResult()) {
            if (event.getData() != null && event.getData().size() > 0) {
                if (currentpage == 1) {
                    data.clear();
                }
                data.addAll(event.getData());
                ++currentpage;
            } else {
                if (currentpage == 1) {
                    emptyText.setVisibility(View.VISIBLE);
                    setModeDisabled();
                }
            }
        }
        if (adapter == null) {
            addHeaderView(getHeaderView());
            adapter = new DynamicVideoAdapter(this, data);
            setAdapter(adapter);

        } else {
            adapter.notifyDataSetChanged();
        }
        isRefreshing = false;
        refreshComplete();
    }

    /**
     * 回调：视频点赞
     */
    public void onEventMainThread(VideoFlower2Entity event) {

        if (event != null) {
            if (event.isResult()) {
                showToastShort(event.getMsg());
            } else {
                showToastShort(event.getMsg());
            }
        }
    }

    /**
     * 回调：视频收藏
     */
    public void onEventMainThread(VideoCollect2Entity event) {

        if (event != null) {
            if (event.isResult()) {
                showToastShort(event.getMsg());
            } else {
                showToastShort(event.getMsg());
            }
        }
    }

    /**
     * 回调：玩家关注
     */
    public void onEventMainThread(MemberAttention201Entity event) {

        if (event != null) {
            if (event.isResult()) {
                showToastShort(event.getMsg());
            } else {
                showToastShort(event.getMsg());
            }
        }


    }
}
