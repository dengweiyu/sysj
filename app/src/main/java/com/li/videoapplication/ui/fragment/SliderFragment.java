package com.li.videoapplication.ui.fragment;

import android.app.Activity;
import android.media.Image;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.IPullToRefresh;
import com.li.videoapplication.R;
import com.li.videoapplication.data.DataManager;
import com.li.videoapplication.data.model.entity.Member;
import com.li.videoapplication.data.model.response.LoginEntity;
import com.li.videoapplication.data.model.event.LoginEvent;
import com.li.videoapplication.data.model.event.LogoutEvent;
import com.li.videoapplication.data.model.event.UserInfomationEvent;
import com.li.videoapplication.data.model.response.MessageMsgRedEntity;
import com.li.videoapplication.framework.AppAccount;
import com.li.videoapplication.framework.TBaseFragment;
import com.li.videoapplication.tools.RongIMHelper;
import com.li.videoapplication.tools.ShareSDKLoginHelper;
import com.li.videoapplication.tools.UmengAnalyticsHelper;
import com.li.videoapplication.ui.ActivityManeger;
import com.li.videoapplication.ui.activity.MainActivity;
import com.li.videoapplication.ui.activity.MyPlayerActivity;
import com.li.videoapplication.utils.StringUtil;
import com.li.videoapplication.views.CircleImageView;

import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Message;

/**
 * 碎片：侧滑菜单
 */
public class SliderFragment extends TBaseFragment implements OnClickListener {

    /**
     * 跳转：个人资料
     */
    public void startMyPersonalInfoActivity() {
        ActivityManeger.startMyPersonalInfoActivity(getActivity());
    }

    /**
     * 跳转：个人中心
     */
    public void startMyPersonalCenterActivity() {
        ActivityManeger.startMyPersonalCenterActivity(getActivity());
        UmengAnalyticsHelper.onEvent(getActivity(), UmengAnalyticsHelper.SLIDER, "已登录-头像");
    }

    /**
     * 跳转：我的关注
     */
    public void startMyPlayerActivityMyFocus() {
        ActivityManeger.startMyPlayerActivity(getActivity(), MyPlayerActivity.PAGE_MYFOCUS, getMember_id());
    }

    /**
     * 跳转：我的粉丝
     */
    public void startMyPlayerActivityMyFans() {
        ActivityManeger.startMyPlayerActivity(getActivity(), MyPlayerActivity.PAGE_MYFANS, getMember_id());
    }

    /**
     * 跳转：登录
     */
    public void startLoginActivity() {
        ActivityManeger.startLoginActivity(getActivity());
    }

    /**
     * 跳转：我的赛事
     */
    public void startMyMatchActivity() {
        ActivityManeger.startMyMatchActivity(getActivity());
        UmengAnalyticsHelper.onEvent(getActivity(), UmengAnalyticsHelper.SLIDER, "我的赛事");
    }

    /**
     * 跳转：我的消息
     */
    public void startMyMessageActivity() {
        ActivityManeger.startMyMessageActivity(getActivity());
        UmengAnalyticsHelper.onEvent(getActivity(), UmengAnalyticsHelper.SLIDER, "我的消息");
    }

    /**
     * 跳转：我的福利
     */
    public void startMyWelfareActivity() {
        ActivityManeger.startMyWelfareActivity(getActivity());
        UmengAnalyticsHelper.onEvent(getActivity(), UmengAnalyticsHelper.SLIDER, "我的福利");
    }

    /**
     * 跳转：我的任务
     */
    public void startMyTaskActivity() {
        ActivityManeger.startMyTaskActivity(getActivity());
        UmengAnalyticsHelper.onEvent(getActivity(), UmengAnalyticsHelper.SLIDER, "我的任务");
    }

    /**
     * 跳转：我的视频
     */
    public void startVideoMangerActivity() {
        ActivityManeger.startVideoMangerActivity(getActivity());
        UmengAnalyticsHelper.onEvent(getActivity(), UmengAnalyticsHelper.SLIDER, "我的视频");
    }


    /**
     * 跳转：我的收藏
     */
    public void startCollectionActivity() {
        ActivityManeger.startCollectionActivity(getActivity());
        UmengAnalyticsHelper.onEvent(getActivity(), UmengAnalyticsHelper.SLIDER, "我的收藏");
    }

    /**
     * 跳转：我的设置
     */
    public void startSettingActivity() {
        ActivityManeger.startSettingActivity(getActivity());
        UmengAnalyticsHelper.onEvent(getActivity(), UmengAnalyticsHelper.SLIDER, "设置");
    }

    private View login, person;
    private CircleImageView head;
    private TextView name, fans, focus;
    private ImageView go, slider_message_go;

    private MainActivity activity;
    private TextView count;
    private int total, a, b, c;

    private ShareSDKLoginHelper helper = new ShareSDKLoginHelper();

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            this.activity = (MainActivity) activity;
        } catch (Exception e) {
            e.printStackTrace();
        }

        helper.initSDK(getActivity());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        helper.stopSDK(getActivity());
    }

    @Override
    protected int getCreateView() {
        return R.layout.fragment_slider;
    }

    @SuppressWarnings("rawtypes")
    @Override
    protected IPullToRefresh getPullToRefresh() {
        return null;
    }

    @Override
    protected void initContentView(View view) {

        head = (CircleImageView) view.findViewById(R.id.slider_head);
        name = (TextView) view.findViewById(R.id.slider_name);
        fans = (TextView) view.findViewById(R.id.slider_fans);
        focus = (TextView) view.findViewById(R.id.slider_focus);
//        level = (TextView) view.findViewById(R.id.slider_level);

        go = (ImageView) view.findViewById(R.id.go);
        slider_message_go = (ImageView) view.findViewById(R.id.slider_message_go);

        count = (TextView) view.findViewById(R.id.slider_mymessage_count);
        count.setVisibility(View.GONE);

        login = view.findViewById(R.id.slider_login);
        person = view.findViewById(R.id.slider_person);

        view.findViewById(R.id.slider_mymatch).setOnClickListener(this);
        view.findViewById(R.id.slider_mymessage).setOnClickListener(this);
        view.findViewById(R.id.slider_mywelfare).setOnClickListener(this);
        view.findViewById(R.id.slider_myvideo).setOnClickListener(this);
        view.findViewById(R.id.slider_mytask).setOnClickListener(this);
        view.findViewById(R.id.slider_collection).setOnClickListener(this);
        view.findViewById(R.id.slider_setting).setOnClickListener(this);

        view.findViewById(R.id.slider_qq).setOnClickListener(this);
        view.findViewById(R.id.slider_wx).setOnClickListener(this);
        view.findViewById(R.id.slider_wb).setOnClickListener(this);
        view.findViewById(R.id.slider_phone).setOnClickListener(this);

        head.setOnClickListener(this);
        focus.setOnClickListener(this);
        fans.setOnClickListener(this);
        go.setOnClickListener(this);

        login.setOnClickListener(this);

        switchHeaderView(isLogin(), getUser());
    }

    //刷新侧栏红点
    public void refreshUnReadMessage() {
        // 圈子消息总数
        DataManager.messageMsgRed(getMember_id());
    }

    private void getRongIMUnReadCount() {
        RongIMHelper.getTotalUnreadCount(new RongIMHelper.totalUnreadCountCallback() {
            @Override
            public void totalUnreadCount(int count) {
                total = a + b + c + count;
                refreshRedCount(total);
            }
        });
    }

    private void refreshRedCount(int total) {
        if (isLogin()) {
            if (total > 0) {
                slider_message_go.setVisibility(View.GONE);
                count.setVisibility(View.VISIBLE);
            } else {
                count.setVisibility(View.GONE);
                slider_message_go.setVisibility(View.VISIBLE);
            }
        }
    }

    private void switchHeaderView(boolean isLogin, Member item) {

        if (isLogin) {
            login.setVisibility(View.GONE);
            person.setVisibility(View.VISIBLE);
            setImageViewImageNet(head, item.getAvatar());
            setTextViewText(name, item.getNickname());
            setTextViewText(fans, getFans(item));
            setTextViewText(focus, getFocus(item));
//            setTextViewText(level, getLevel(item));
        } else {
            login.setVisibility(View.VISIBLE);
            person.setVisibility(View.GONE);
        }
    }

    /**
     * 等级
     *
     * @return Lv.11
     */
    private String getLevel(Member item) {
        return "Lv." + item.getDegree();
    }

    public String getFans(Member member) {
        if (StringUtil.isNull(member.getFans())) {
            return "粉丝：0";
        } else {
            return "粉丝：" + member.getFans();
        }
    }

    public String getFocus(Member member) {
        if (StringUtil.isNull(member.getAttention())) {
            return "关注：0";
        } else {
            return "关注：" + member.getAttention();
        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.slider_head:
                startMyPersonalCenterActivity();
                break;

            case R.id.go:
                startMyPersonalCenterActivity();
                break;

            case R.id.slider_focus:
                if (!isLogin()) {
                    showToastLogin();
                    return;
                }
                startMyPlayerActivityMyFocus();
                break;

            case R.id.slider_fans:
                if (!isLogin()) {
                    showToastLogin();
                    return;
                }
                startMyPlayerActivityMyFans();
                break;

            case R.id.slider_person:
                //startMyPersonalCenterActivity();
                break;

            case R.id.slider_login:
                startLoginActivity();
                break;

            case R.id.slider_mymatch:
                startMyMatchActivity();
                break;

            case R.id.slider_mymessage:
                startMyMessageActivity();
                break;

            case R.id.slider_mywelfare:
                startMyWelfareActivity();
                break;

            case R.id.slider_myvideo:
                startVideoMangerActivity();
                break;

            case R.id.slider_mytask:
                startMyTaskActivity();
                break;

            case R.id.slider_collection:
                startCollectionActivity();
                break;

            case R.id.slider_setting:
                startSettingActivity();
                break;

            case R.id.slider_phone:
                startLoginActivity();
                UmengAnalyticsHelper.onEvent(getActivity(), UmengAnalyticsHelper.SLIDER, "手机登录");
                break;

            case R.id.slider_qq:
                helper.qq();
                UmengAnalyticsHelper.onEvent(getActivity(), UmengAnalyticsHelper.SLIDER, "qq登录");
                break;

            case R.id.slider_wx:
                helper.wx();
                UmengAnalyticsHelper.onEvent(getActivity(), UmengAnalyticsHelper.SLIDER, "微信登录");
                break;

            case R.id.slider_wb:
                helper.wb();
                UmengAnalyticsHelper.onEvent(getActivity(), UmengAnalyticsHelper.SLIDER, "新浪微博登录");
                break;
        }
    }

    /**
     * 回调：圈子消息总数+rongIM消息
     */
    public void onEventMainThread(MessageMsgRedEntity event) {

        if (event.isResult()) {
            String vpNum = event.getData().getVpNum();
            String groupNum = event.getData().getGroupNum();
            String sysNum = event.getData().getSysNum();

            try {
                a = Integer.valueOf(vpNum);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }

            try {
                b = Integer.valueOf(groupNum);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }

            try {
                c = Integer.valueOf(sysNum);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }

            getRongIMUnReadCount();
        }
    }

    /**
     * 回调：登录
     */
    public void onEventMainThread(LoginEntity event) {

        if (event != null) {
            if (event.isResult()) {// 成功
                showToastShort("登录成功");
                // 获取个人资料
                DataManager.userProfilePersonalInformation(getMember_id(), getMember_id());
                handler.postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        AppAccount.login();
                    }
                }, 400);
                UmengAnalyticsHelper.onEvent(getActivity(), UmengAnalyticsHelper.SLIDER, "登录成功");
                return;
            }
        }
        showToastShort("登录失败！");
    }

    /**
     * 事件：登录
     */
    public void onEventMainThread(LoginEvent event) {

        if (event != null) {
            switchHeaderView(isLogin(), getUser());
        }
    }

    /**
     * 事件：注销
     */
    public void onEventMainThread(LogoutEvent event) {

        if (event != null) {
            switchHeaderView(isLogin(), getUser());
        }
    }

    /**
     * 事件：更新个人资料
     */
    public void onEventMainThread(UserInfomationEvent event) {

        if (event != null) {
            switchHeaderView(isLogin(), getUser());
        }
    }
}
