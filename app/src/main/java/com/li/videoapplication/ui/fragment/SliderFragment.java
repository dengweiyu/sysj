package com.li.videoapplication.ui.fragment;

import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.IPullToRefresh;
import com.ifeimo.im.framwork.Proxy;
import com.ifeimo.im.framwork.message.OnUnReadChange;
import com.li.videoapplication.R;
import com.li.videoapplication.data.DataManager;
import com.li.videoapplication.data.model.entity.Member;
import com.li.videoapplication.data.model.event.UnReadMessageEvent;
import com.li.videoapplication.data.model.response.LoginEntity;
import com.li.videoapplication.data.model.event.LoginEvent;
import com.li.videoapplication.data.model.event.LogoutEvent;
import com.li.videoapplication.data.model.event.UserInfomationEvent;
import com.li.videoapplication.data.model.response.MessageMsgRedEntity;
import com.li.videoapplication.framework.AppAccount;
import com.li.videoapplication.framework.TBaseFragment;
import com.li.videoapplication.mvp.Constant;
import com.li.videoapplication.tools.RongIMHelper;
import com.li.videoapplication.tools.ShareSDKLoginHelper;
import com.li.videoapplication.tools.UmengAnalyticsHelper;
import com.li.videoapplication.ui.ActivityManager;
import com.li.videoapplication.ui.activity.MainActivity;
import com.li.videoapplication.ui.activity.MyPlayerActivity;
import com.li.videoapplication.ui.activity.MyWalletActivity;
import com.li.videoapplication.ui.dialog.ServiceDialog;
import com.li.videoapplication.utils.StringUtil;
import com.li.videoapplication.views.CircleImageView;

/**
 * 碎片：侧滑菜单
 */
public class SliderFragment extends TBaseFragment implements OnClickListener {

    private MainActivity mActivity;
    /**
     * 跳转：商城
     */
    public void startMallActivity() {
        ActivityManager.startMallActivity(getActivity());
        UmengAnalyticsHelper.onEvent(getActivity(), UmengAnalyticsHelper.SLIDER, "视界商城-点击视界商城次数");
    }

    /**
     * 跳转：我的钱包
     */
    public void startMyWalletActivity() {
        if (!isLogin()) {
            startLoginActivity();
            return;
        }
        ActivityManager.startMyWalletActivity(getActivity(), MyWalletActivity.PAGE_BEANS);
        UmengAnalyticsHelper.onEvent(getActivity(), UmengAnalyticsHelper.SLIDER, "我的钱包-点击我的钱包次数");
    }

    /**
     * 跳转：个人资料
     */
    public void startMyPersonalInfoActivity() {
        ActivityManager.startMyPersonalInfoActivity(getActivity());
    }

    /**
     * 跳转：个人中心
     */
    public void startMyPersonalCenterActivity() {
        ActivityManager.startMyPersonalCenterActivity(getActivity());
        UmengAnalyticsHelper.onEvent(getActivity(), UmengAnalyticsHelper.SLIDER, "已登录-头像");
    }

    /**
     * 跳转：我的关注
     */
    public void startMyPlayerActivityMyFocus() {
        if (!isLogin()) {
            startLoginActivity();
            return;
        }
        ActivityManager.startMyPlayerActivity(getActivity(), MyPlayerActivity.PAGE_MYFOCUS, getMember_id());
        UmengAnalyticsHelper.onEvent(getActivity(), UmengAnalyticsHelper.SLIDER, "关注/粉丝数-关注数");
    }

    /**
     * 跳转：我的粉丝
     */
    public void startMyPlayerActivityMyFans() {
        if (!isLogin()) {
            startLoginActivity();
            return;
        }
        ActivityManager.startMyPlayerActivity(getActivity(), MyPlayerActivity.PAGE_MYFANS, getMember_id());
        UmengAnalyticsHelper.onEvent(getActivity(), UmengAnalyticsHelper.SLIDER, "关注/粉丝数-粉丝数");
    }

    /**
     * 跳转：登录
     */
    public void startLoginActivity() {
        ActivityManager.startLoginActivity(getActivity());
    }

    /**
     * 跳转：我的赛事
     */
    public void startMyMatchActivity() {
        if (!isLogin()) {
            startLoginActivity();
            return;
        }
        ActivityManager.startPlayWithOrderAndMatchActivity(getContext(),0,0);
        UmengAnalyticsHelper.onEvent(getActivity(), UmengAnalyticsHelper.SLIDER, "我的赛事");
    }

    /**
     * 跳转：我的消息
     */
    public void startMyMessageActivity() {
        if (!isLogin()) {
            startLoginActivity();
            return;
        }
        ActivityManager.startMyMessageActivity(getActivity());
        UmengAnalyticsHelper.onEvent(getActivity(), UmengAnalyticsHelper.SLIDER, "我的消息");
    }

    /**
     * 跳转：我的任务
     */
    public void startMyTaskActivity() {
        ActivityManager.startMyTaskActivity(getActivity());
        UmengAnalyticsHelper.onEvent(getActivity(), UmengAnalyticsHelper.SLIDER, "我的任务");
    }

    /**
     * 跳转：我的视频
     */
    public void startVideoMangerActivity() {
        ActivityManager.startVideoMangerActivity(getActivity());
        UmengAnalyticsHelper.onEvent(getActivity(), UmengAnalyticsHelper.SLIDER, "我的视频");
    }


    /**
     * 跳转：我的收藏
     */
    public void startCollectionActivity() {
        if (!isLogin()) {
            startLoginActivity();
            return;
        }
        ActivityManager.startCollectionActivity(getActivity());
        UmengAnalyticsHelper.onEvent(getActivity(), UmengAnalyticsHelper.SLIDER, "我的收藏");
    }

    /**
     * 跳转：我的设置
     */
    public void startSettingActivity() {
        ActivityManager.startSettingActivity(getActivity());
        UmengAnalyticsHelper.onEvent(getActivity(), UmengAnalyticsHelper.SLIDER, "设置");
    }

    private View login, person;
    private CircleImageView head;
    private TextView name, fans, focus, beanNum,coinNum;
    private ImageView isV,go, slider_message_go;
    private ImageView mVip;

    private TextView count;
    private int totalRong , a, b, c;    //融云未读消息总数

    private int totalXMPP;              //XMPP的未读消息总数

    private ShareSDKLoginHelper helper = new ShareSDKLoginHelper();

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        helper.initSDK(getActivity());
        if (activity instanceof MainActivity){
            mActivity = (MainActivity)activity;
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser){
            DataManager.userProfilePersonalInformation(getMember_id(), getMember_id());
        }
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
        isV = (ImageView) view.findViewById(R.id.slider_isv);
        mVip = (ImageView)view.findViewById(R.id.iv_slider_vip);
        name = (TextView) view.findViewById(R.id.slider_name);
        fans = (TextView) view.findViewById(R.id.slider_fans);
        focus = (TextView) view.findViewById(R.id.slider_focus);
        beanNum = (TextView) view.findViewById(R.id.slider_bean_num);
        coinNum = (TextView) view.findViewById(R.id.tv_slider_coin);

        go = (ImageView) view.findViewById(R.id.go);
        slider_message_go = (ImageView) view.findViewById(R.id.slider_message_go);

        count = (TextView) view.findViewById(R.id.slider_mymessage_count);
        count.setVisibility(View.GONE);

        login = view.findViewById(R.id.slider_login);
        person = view.findViewById(R.id.slider_person);

        view.findViewById(R.id.slider_mymatch).setOnClickListener(this);
        view.findViewById(R.id.slider_mymessage).setOnClickListener(this);
        view.findViewById(R.id.slider_mywallet).setOnClickListener(this);
        view.findViewById(R.id.slider_sjmall).setOnClickListener(this);
        view.findViewById(R.id.slider_myvideo).setOnClickListener(this);
        view.findViewById(R.id.slider_collection).setOnClickListener(this);
        view.findViewById(R.id.slider_setting).setOnClickListener(this);
        view.findViewById(R.id.slider_customer).setOnClickListener(this);
        view.findViewById(R.id.ll_slider_vip).setOnClickListener(this);

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

        //注册IM 未读消息监听
      /*  Proxy.getMessageManager().onUnReadChange(new OnUnReadChange() {
            @Override
            public void change(final int count) {

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        totalXMPP = count;
                        refreshRedCount(totalRong+totalXMPP);
                    }
                });

            }
        });*/
    }

    /**
     * 更新XMPP 未读消息数目
     */
    public void refreshXMPPUnreadMsg(int count){
        totalXMPP = count;
        refreshRedCount(totalRong+totalXMPP);
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
                totalRong = a + b + c + count;
                refreshRedCount(totalRong+totalXMPP);
            }
        });
    }

    private void refreshRedCount(int total) {
        if (isLogin()) {
            if (mActivity != null){
                mActivity.updateUnReadMessage(new UnReadMessageEvent(total));
            }
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
        if (item == null){
            return;
        }
        if (isLogin) {
            login.setVisibility(View.GONE);
            person.setVisibility(View.VISIBLE);
            setImageViewImageNet(head, item.getAvatar());
            setTextViewText(name, item.getNickname());
            setTextViewText(fans, getFans(item));
            setTextViewText(focus, getFocus(item));
            if (StringUtil.isNull(item.getCurrency())){
                item.setCurrency("0");
            }
            try {
                setTextViewText(beanNum, StringUtil.formatMoney(Float.parseFloat(item.getCurrency())));
                setTextViewText(coinNum,StringUtil.formatMoneyOnePoint(Float.parseFloat(item.getCoin())));
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (item.isV()){        //主播加V
                isV.setVisibility(View.VISIBLE);
            }else {                 //VIP
                if (item.getVipInfo() != null && !StringUtil.isNull(item.getVipInfo().getLevel())){
                    switch (item.getVipInfo().getLevel()){
                        case "1":
                            mVip.setImageResource(R.drawable.vip_level_1_icon);
                            mVip.setVisibility(View.VISIBLE);
                            break;
                        case "2":
                            mVip.setImageResource(R.drawable.vip_level_2_icon);
                            mVip.setVisibility(View.VISIBLE);
                            break;
                        case "3":
                            mVip.setImageResource(R.drawable.vip_level_3_icon);
                            mVip.setVisibility(View.VISIBLE);
                            break;
                    }
                }
            }
        } else {
            login.setVisibility(View.VISIBLE);
            person.setVisibility(View.GONE);
        }
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
                startMyPlayerActivityMyFocus();
                break;

            case R.id.slider_fans:
                startMyPlayerActivityMyFans();
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

            case R.id.slider_myvideo:
                startVideoMangerActivity();
                break;

            case R.id.slider_mywallet:
                startMyWalletActivity();
                break;

            case R.id.slider_sjmall:
                startMallActivity();
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
            case R.id.slider_customer:
                new ServiceDialog(getContext()).show();
                break;
            case R.id.ll_slider_vip:
                ActivityManager.startTopUpActivity(getContext(), Constant.TOPUP_ENTRY_HOME_MENU,2);
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();

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
            } catch (Exception e) {
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
