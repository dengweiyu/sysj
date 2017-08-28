package com.li.videoapplication.ui.activity;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Color;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ifeimo.im.framwork.IMSdk;
import com.ifeimo.im.framwork.Proxy;
import com.li.videoapplication.R;
import com.li.videoapplication.data.DataManager;
import com.li.videoapplication.data.model.entity.GroupType;
import com.li.videoapplication.data.model.entity.Member;
import com.li.videoapplication.data.model.response.SwitchChatEntity;
import com.li.videoapplication.data.model.response.GroupType210Entity;
import com.li.videoapplication.data.model.response.MemberAttention201Entity;
import com.li.videoapplication.data.model.response.UserProfilePersonalInformationEntity;
import com.li.videoapplication.data.network.UITask;
import com.li.videoapplication.framework.AppManager;
import com.li.videoapplication.framework.TBaseActivity;
import com.li.videoapplication.tools.FeiMoIMHelper;
import com.li.videoapplication.ui.ActivityManager;
import com.li.videoapplication.ui.DialogManager;
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
    private ImageView isV;
    private TextView name;
    private TextView gender;
    private TextView introduce;
    private TextView qq, focus;
    //    private RelativeLayout qqContainer;
    private RelativeLayout qqContainer,likeContainer;

    //    private List<View> views = new ArrayList<>();
    private Member member;
    private View qqDivider;

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
    private SwitchChatEntity entity;
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

        //默认使用自有IM
        entity = new SwitchChatEntity();
        entity.setPrivateIM(true);
        DataManager.switchChat();

        // 个人资料
        DataManager.userProfilePersonalInformation(member.getId(), getMember_id());

        UITask.postDelayed(new Runnable() {

            @Override
            public void run() {

                // 圈子类型
                DataManager.groupType217();
            }
        }, 200);
    }

    private void initContentView() {

        head = (CircleImageView) findViewById(R.id.playerpersonalinfo_head);
        isV = (ImageView) findViewById(R.id.playerpersonalinfo_isv);
        name = (TextView) findViewById(R.id.playerpersonalinfo_name);
        gender = (TextView) findViewById(R.id.playerpersonalinfo_gender);
        introduce = (TextView) findViewById(R.id.playerpersonalinfo_introduce);

        qq = (TextView) findViewById(R.id.playerpersonalinfo_qq);
        qqDivider = findViewById(R.id.divider_qq);
        qqContainer = (RelativeLayout) findViewById(R.id.playerpersonalinfo_qq_container);

//        mobile = (TextView) findViewById(R.id.playerpersonalinfo_mobile);
//        mobileContainer = (RelativeLayout) findViewById(R.id.playerpersonalinfo_mobile_container);

        likeContainer = (RelativeLayout) findViewById(R.id.playerpersonalinfo_like_container);
        focus = (TextView) findViewById(R.id.playerpersonalinfo_focus);

        head.setOnClickListener(this);
        focus.setOnClickListener(this);
        findViewById(R.id.playerpersonalinfo_sendmessage).setOnClickListener(this);

//        views.add(findViewById(R.id.divider_1));
//        views.add(findViewById(R.id.divider_2));

        mHorizontalListView = (HorizontalListView) findViewById(R.id.horizontallistvierw);
        data = new ArrayList<>();
        adapter = new MyPersonalInfoAdapter(this, data);
        mHorizontalListView.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        if (!isLogin()) {
            DialogManager.showLogInDialog(this);
            return;
        }
        switch (v.getId()) {
            case R.id.playerpersonalinfo_focus:
                if (member.getIsAttent() == 1) {
                    DialogManager.showConfirmDialog(PlayerPersonalInfoActivity.this, "确认取消关注该玩家?", new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            switch (v.getId()){
                                case R.id.tv_confirm_dialog_yes:
                                    member.setIsAttent(0);
                                    // 修改玩家关注
                                    DataManager.memberAttention201(member.getId(), getMember_id());


                                    //刷新玩家动态页面关注按钮状态
                                    PlayerDynamicActivity playerDynamicActivity = (PlayerDynamicActivity) AppManager.getInstance().getActivity(PlayerDynamicActivity.class);
                                    if (playerDynamicActivity != null) {
                                        playerDynamicActivity.setFocus(member);
                                    }
                                    break;
                            }
                        }
                    });
                } else {
                    member.setIsAttent(1);
                    // 修改玩家关注
                    DataManager.memberAttention201(member.getId(), getMember_id());
                    //刷新玩家动态页面关注按钮状态
                    PlayerDynamicActivity playerDynamicActivity = (PlayerDynamicActivity) AppManager.getInstance().getActivity(PlayerDynamicActivity.class);
                    if (playerDynamicActivity != null) {
                        playerDynamicActivity.setFocus(member);
                    }
                }

                break;
            case R.id.playerpersonalinfo_sendmessage:
                if (entity.isPrivateIM()){
                    Member user = getUser();
                    String memberId = user.getMember_id();
                    if (!Proxy.getConnectManager().isConnect()) {

                        if(null == memberId  || memberId.equals("")){
                            memberId = getMember_id();
                        }
                        FeiMoIMHelper.Login(memberId, user.getNickname(), user.getAvatar());
                    }

                    IMSdk.createChat(PlayerPersonalInfoActivity.this,member.getId(),member.getNickname(),member.getAvatar());

                }else {
                    if (RongIM.getInstance() != null && member != null &&
                            member.getNickname() != null && member.getId() != null) {

                        ActivityManager.startConversationActivity(this, member.getId(),
                                member.getNickname(), false);
                    }
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

            if (item.isV()) {
                isV.setVisibility(View.VISIBLE);
            } else {
                isV.setVisibility(View.INVISIBLE);
            }

            if (StringUtil.isNull(item.getSignature())) {
                introduce.setText("");
            } else {
                introduce.setText(item.getSignature());
            }

            if (StringUtil.isNull(item.getQq())) {
                qqContainer.setVisibility(View.GONE);
                qqDivider.setVisibility(View.GONE);
            } else {
                setTextViewText(qq, item.getQq());
            }

//            setTextViewText(mobile, item.getMobile());
        }
    }

    private void switchListView(Member item) {

        if (getUser() != null) {

//            if (item.getDisplay() == 1) {// 公开私密资料
//                for (View diviver : views) {
//                    diviver.setVisibility(View.VISIBLE);
//                }
//                qqContainer.setVisibility(View.VISIBLE);
//                mobileContainer.setVisibility(View.VISIBLE);
//
            if (item.getLikeGroupType().size() > 0) {
                likeContainer.setVisibility(View.VISIBLE);
            } else {
                likeContainer.setVisibility(View.GONE);
                qqDivider.setVisibility(View.GONE);
            }
//            } else {// 不公开私密资料
//                for (View diviver : views) {
//                    diviver.setVisibility(View.GONE);
//                }
//                qqContainer.setVisibility(View.GONE);
//                mobileContainer.setVisibility(View.GONE);
//                likeContainer.setVisibility(View.GONE);
//            }
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
        adapter.notifyDataSetChanged();
    }

    /**
     * 回调：玩家关注
     */
    public void onEventMainThread(MemberAttention201Entity event) {

        if (event != null) {
            showToastShort(event.getMsg());
            refreshFocus(member);
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

    /**
     * 事件：切换聊天
     */
    public void onEventMainThread(SwitchChatEntity event) {

        if (event != null) {
            entity.setPrivateIM(event.isPrivateIM());
        }
    }
}
