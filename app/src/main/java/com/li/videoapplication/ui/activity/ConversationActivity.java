package com.li.videoapplication.ui.activity;

import android.app.NotificationManager;
import android.net.Uri;
import android.support.v4.app.FragmentTransaction;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.JsonSyntaxException;
import com.li.videoapplication.R;
import com.li.videoapplication.data.DataManager;
import com.li.videoapplication.data.model.entity.Member;
import com.li.videoapplication.data.model.response.GroupName208Entity;
import com.li.videoapplication.data.model.response.UserProfilePersonalInformationEntity;
import com.li.videoapplication.framework.TBaseAppCompatActivity;
import com.li.videoapplication.tools.RongIMHelper;
import com.li.videoapplication.tools.UmengAnalyticsHelper;
import com.li.videoapplication.ui.ActivityManager;
import com.li.videoapplication.utils.LogHelper;
import com.li.videoapplication.utils.StringUtil;
import com.li.videoapplication.utils.TextUtil;
import com.li.videoapplication.views.MultiDirectionSlidingDrawer;

import io.rong.imkit.RongContext;
import io.rong.imkit.RongIM;
import io.rong.imkit.fragment.ConversationFragment;
import io.rong.imkit.widget.provider.ImageInputProvider;
import io.rong.imkit.widget.provider.InputProvider;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;

/**
 * 活动：客服/聊天
 */
public class ConversationActivity extends TBaseAppCompatActivity implements View.OnClickListener {
    public static final int PRIVATE = 0;
    public static final int GROUP = 1;
    public static final int CHATROOM = 2;

    private String mTargetId, mTitle, qq;
    private boolean isFromCombat;
    private MultiDirectionSlidingDrawer mDrawer;
    private TextView qqNum, title, tipText;
    private Member member;
    private int conversationType;
    private String customerServiceID, customerServiceName;

    @Override
    public int getContentView() {
        return R.layout.activity_customerservice;
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
            mTargetId = getIntent().getStringExtra("targetId");
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            mTitle = getIntent().getStringExtra("title");
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            isFromCombat = getIntent().getBooleanExtra("isFromCombat", false);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            qq = getIntent().getStringExtra("qq");
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            conversationType = getIntent().getIntExtra("conversationType", PRIVATE);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            customerServiceID = getIntent().getStringExtra("customerServiceID");
            customerServiceName = getIntent().getStringExtra("customerServiceName");
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.d(tag, "refreshIntent: mTargetId == " + mTargetId + ", title == " + mTitle);
    }

    private Conversation.ConversationType getConversationType(int type) {
        switch (type) {
            case PRIVATE:
                return Conversation.ConversationType.PRIVATE;
            case GROUP:
                return Conversation.ConversationType.GROUP;
            case CHATROOM:
                return Conversation.ConversationType.CHATROOM;
            default:
                return Conversation.ConversationType.PRIVATE;
        }
    }

    public void setConversationTitle(String title) {
        if (this.title != null && !StringUtil.isNull(title))
            setTextViewText(this.title, title);
    }

    private void enterFragment(Conversation.ConversationType mConversationType, String mTargetId) {

        ConversationFragment fragment = new ConversationFragment();

        Uri uri = Uri.parse("rong://" + getApplicationInfo().packageName).buildUpon()
                .appendPath("conversation")
                .appendPath(mConversationType.getName().toLowerCase())
                .appendQueryParameter("targetId", mTargetId)
                .appendQueryParameter("title", mTitle)
                .build();

        fragment.setUri(uri);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.customerservice_container, fragment);
        transaction.commit();
        //扩展功能自定义
        InputProvider.ExtendProvider[] provider = {
                new ImageInputProvider(RongContext.getInstance()),//图片
        };

        RongIM.getInstance().resetInputExtensionProvider(getConversationType(conversationType), provider);
    }

    @Override
    public void initView() {
        super.initView();
        mDrawer = (MultiDirectionSlidingDrawer) findViewById(R.id.sliding);

        qqNum = (TextView) findViewById(R.id.conversation_qq_number);
        title = (TextView) findViewById(R.id.conversation_title);
        tipText = (TextView) findViewById(R.id.content_text);

        ImageView customerService = (ImageView) findViewById(R.id.customerservice_chat);
        customerService.setOnClickListener(this);

        findViewById(R.id.ab_goback).setOnClickListener(this);

        ImageView personalInfo = (ImageView) findViewById(R.id.conversation_personalinfo);
        personalInfo.setOnClickListener(this);

        if (isFromCombat) {
            mDrawer.setVisibility(View.VISIBLE);
            setQQView();
        } else {
            mDrawer.setVisibility(View.GONE);
        }

        if (conversationType == GROUP || conversationType == CHATROOM)
            personalInfo.setVisibility(View.GONE);

        if (StringUtil.isNull(customerServiceID) && StringUtil.isNull(customerServiceName)) {
            customerService.setVisibility(View.GONE);
        }
    }

    private void setQQView() {
        if (!StringUtil.isNull(qq)) {
            qqNum.setVisibility(View.VISIBLE);
            qqNum.setText(Html.fromHtml("QQ : " + TextUtil.toColor(qq, "#5193ff")));//blue
            String s = "对方未在规定时间内回应则截图此聊天框并上传，提供对手QQ号仅方便选手快速进行比赛，判定依据将以本聊天框为准。";
            tipText.setText(s);
        } else {
            qqNum.setVisibility(View.GONE);
        }
    }

    @Override
    public void loadData() {
        super.loadData();
        enterFragment(getConversationType(conversationType), mTargetId);

        if (!StringUtil.isNull(mTitle))
            setTextViewText(title, mTitle);

        if (conversationType == PRIVATE) {
            //个人资料
            DataManager.userProfilePersonalInformation(mTargetId, getMember_id());
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ab_goback:
                finish();
                break;

            case R.id.conversation_personalinfo:
                if (member != null)
                    ActivityManager.startPlayerPersonalInfoActivity(this, member);
                break;

            case R.id.customerservice_chat:
                if (RongIM.getInstance() != null) {
                    ActivityManager.startConversationActivity(this,
                            customerServiceID,
                            customerServiceName,
                            false);
                }
                UmengAnalyticsHelper.onEvent(this, UmengAnalyticsHelper.MATCH, "群聊-群聊内客服");
                break;
        }
    }

    /**
     * 回调：个人资料
     */
    public void onEventMainThread(UserProfilePersonalInformationEntity event) {
        if (event != null) {

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
                    this.member = m;
                }
            }
        }

        if (conversationType == PRIVATE && StringUtil.isNull(mTitle))
            setConversationTitle(member.getNickname());
    }

    /**
     * 回调：获取群名
     */
    public void onEventMainThread(GroupName208Entity event) {
        if (event != null && event.isResult()) {

            if (StringUtil.isNull(mTitle) && conversationType == GROUP)
                setConversationTitle(event.getData().getChatroom_group_name());
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        if (RongIM.getInstance().getCurrentConnectionStatus() !=
                RongIMClient.ConnectionStatusListener.ConnectionStatus.CONNECTED) {
            LogHelper.i(tag, "onResume -- ConnectionStatus.Disconnected");

            //链接融云
            RongIMHelper.connectIM();
        }

        //清除对应通知
        try {
            NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            nm.cancel(Integer.parseInt(mTargetId));
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
    }
}
