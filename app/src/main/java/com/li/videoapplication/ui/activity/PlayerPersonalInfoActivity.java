package com.li.videoapplication.ui.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.IPullToRefresh;
import com.li.videoapplication.R;
import com.li.videoapplication.data.DataManager;
import com.li.videoapplication.data.model.entity.GroupType;
import com.li.videoapplication.data.model.entity.Member;
import com.li.videoapplication.data.model.response.GroupType210Entity;
import com.li.videoapplication.data.model.response.GroupType2Entity;
import com.li.videoapplication.data.model.response.MemberAttention201Entity;
import com.li.videoapplication.data.model.response.UserProfilePersonalInformationEntity;
import com.li.videoapplication.data.network.UITask;
import com.li.videoapplication.framework.AppManager;
import com.li.videoapplication.framework.TBaseActivity;
import com.li.videoapplication.ui.ActivityManeger;
import com.li.videoapplication.ui.adapter.MyPersonalInfoAdapter;
import com.li.videoapplication.utils.StringUtil;
import com.li.videoapplication.views.CircleImageView;
import com.li.videoapplication.views.HorizontalListView;

import io.rong.imkit.RongIM;

/**
 * 活动：玩家个人资料
 */
public class PlayerPersonalInfoActivity extends TBaseActivity implements OnClickListener {

    private CircleImageView head;
    private TextView name;
    private TextView gender;
    private TextView introduce;
    private TextView qq, focus , mobile;
    private RelativeLayout qqContainer, mobileContainer;
    private RelativeLayout likeContainer;

    private List<View> views = new ArrayList<>();
    private Member member;

    @Override
    public void refreshIntent() {
        member = (Member) getIntent().getSerializableExtra("member");
        if (member == null)
            finish();
        if (StringUtil.isNull(member.getId())) {
            finish();
        }
    }

    private List<GroupType> groupTypes = new ArrayList<>();

    private MyPersonalInfoAdapter adapter;
    private HorizontalListView mHorizontalListView;
    private List<GroupType> data;

    @Override
    public int getContentView() {
        return R.layout.activity_playerpersonalinfo;
    }

    public int inflateActionBar() {
        return R.layout.actionbar_second;
    }

    @Override
    public void afterOnCreate() {
        super.afterOnCreate();

        setSystemBarBackgroundWhite();
        setAbTitle(R.string.playerpersonalinfo_title);
    }

    @Override
    public void initView() {
        super.initView();

        initContentView();

        refreshContentView(member);
        switchListView(member);
    }

    @Override
    public void loadData() {
        super.loadData();

        // 个人资料
        DataManager.userProfilePersonalInformation(member.getId(), getMember_id());

        UITask.postDelayed(new Runnable() {

            @Override
            public void run() {

                // 圈子类型
                DataManager.groupType210();
            }
        }, 200);
    }

    private void initContentView() {

        head = (CircleImageView) findViewById(R.id.playerpersonalinfo_head);
        name = (TextView) findViewById(R.id.playerpersonalinfo_name);
        gender = (TextView) findViewById(R.id.playerpersonalinfo_gender);
        introduce = (TextView) findViewById(R.id.playerpersonalinfo_introduce);

        qq = (TextView) findViewById(R.id.playerpersonalinfo_qq);
        qqContainer = (RelativeLayout) findViewById(R.id.playerpersonalinfo_qq_container);

        mobile = (TextView) findViewById(R.id.playerpersonalinfo_mobile);
        mobileContainer = (RelativeLayout) findViewById(R.id.playerpersonalinfo_mobile_container);

        likeContainer = (RelativeLayout) findViewById(R.id.playerpersonalinfo_like_container);
        focus = (TextView) findViewById(R.id.playerpersonalinfo_focus);

        head.setOnClickListener(this);
        focus.setOnClickListener(this);
        findViewById(R.id.playerpersonalinfo_sendmessage).setOnClickListener(this);

        views.add(findViewById(R.id.divider_1));
        views.add(findViewById(R.id.divider_2));

        mHorizontalListView = (HorizontalListView) findViewById(R.id.horizontallistvierw);
        data = new ArrayList<>();
        adapter = new MyPersonalInfoAdapter(this, data);
        mHorizontalListView.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.playerpersonalinfo_head:

                break;
            case R.id.playerpersonalinfo_focus:
                if (!isLogin()) {
                    showToastShort("请先登录！");
                    return;
                }
                if (member.getIsAttent() == 1) {
                    member.setIsAttent(0);
                } else {
                    member.setIsAttent(1);
                }
                // 修改玩家关注
                DataManager.memberAttention201(member.getId(), getMember_id());

                //刷新玩家动态页面关注按钮状态
                PlayerDynamicActivity playerDynamicActivity = (PlayerDynamicActivity) AppManager.getInstance().getActivity(PlayerDynamicActivity.class);
                if (playerDynamicActivity != null) {
                    playerDynamicActivity.setFocus(member);
                }
                break;
            case R.id.playerpersonalinfo_sendmessage:
                if (RongIM.getInstance() != null && member != null &&
                        member.getNickname() != null && member.getId() != null) {

                    ActivityManeger.startConversationActivity(this, member.getId(),
                            member.getNickname(), false);
                }
                break;
        }
    }

    private void refreshFocus(Member member) {
        if (member != null) {
            if (member.getIsAttent() == 1) {// 已关注
                setTextViewText(focus, R.string.dynamic_focused);
                focus.setTextColor(Color.parseColor("#949494"));
            } else { // 未关注
                setTextViewText(focus, R.string.dynamic_focus);
                focus.setTextColor(Color.parseColor("#ff3d2e"));
            }
        }
    }

    private void refreshContentView(Member item) {

        if (member != null) {
            setAbTitle(item.getNickname());
            setImageViewImageNet(head, item.getAvatar());
            setTextViewText(name, item.getNickname());
            setTextViewText(gender, getGender(item.getSex()));
            if (StringUtil.isNull(item.getSignature())) {
                introduce.setText("");
            } else {
                introduce.setText(item.getSignature());
            }
            setTextViewText(qq, item.getQq());
            setTextViewText(mobile,item.getMobile());
        }
    }

    private void switchListView(Member item) {

        if (getUser() != null) {

            if (item.getDisplay() == 1) {// 公开私密资料
                for (View diviver : views) {
                    diviver.setVisibility(View.VISIBLE);
                }
                qqContainer.setVisibility(View.VISIBLE);
                mobileContainer.setVisibility(View.VISIBLE);

                if (item.getLikeGroupType().size() > 0) {
                    likeContainer.setVisibility(View.VISIBLE);
                } else {
                    likeContainer.setVisibility(View.GONE);
                }
            } else {// 不公开私密资料
                for (View diviver : views) {
                    diviver.setVisibility(View.GONE);
                }
                qqContainer.setVisibility(View.GONE);
                mobileContainer.setVisibility(View.GONE);
                likeContainer.setVisibility(View.GONE);
            }
        }
    }

    public String getGender(int gender) {

        if (gender == 1) {
            return "男";
        } else if (gender == 0) {
            return "女";
        } else {
            return "无";
        }
    }

    /**
     * 刷新列表
     */
    private void refreshListView(Member item) {
        data.clear();
        if (groupTypes != null
                && groupTypes.size() > 0
                && item.getLikeGroupType() != null
                && item.getLikeGroupType().size() > 0) {
            for (String id : item.getLikeGroupType()) {
                for (GroupType groupType : groupTypes) {
                    if (id.equals(groupType.getGroup_type_id())) {
                        data.add(groupType);
                    }
                }
            }
        }
        adapter.setMode(MyPersonalInfoAdapter.MODE_NORMAL);
        adapter.notifyDataSetChanged();
    }

    /**
     * 回调：玩家关注
     */
    public void onEventMainThread(MemberAttention201Entity event) {

        if (event != null) {
            if (event.isResult()) {
                refreshFocus(member);
            } else {
                showToastShort(event.getMsg());
            }
        }
    }

    /**
     * 回调：圈子類型
     */
    public void onEventMainThread(GroupType210Entity event) {

        if (event != null) {
            if (event.isResult()) {
                if (event.getData().size() > 0) {
                    this.groupTypes.clear();
                    this.groupTypes.addAll(event.getData());
                    switchListView(member);
                    refreshListView(member);
                }
            }
        }
    }

    /**
     * 回调：个人资料
     */
    public void onEventMainThread(UserProfilePersonalInformationEntity event) {

        if (event != null) {
            if (event.isResult()) {
                if (event.getData() != null) {
                    if (!StringUtil.isNull(event.getData().getId())) {
                        member = event.getData();
                        refreshContentView(member);
                        switchListView(member);
                        refreshListView(member);
                        refreshFocus(member);
                    }
                }
            }
        }
    }
}
