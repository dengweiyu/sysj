package com.li.videoapplication.ui.activity;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.li.videoapplication.R;
import com.li.videoapplication.data.DataManager;
import com.li.videoapplication.data.model.entity.Currency;
import com.li.videoapplication.data.model.entity.Member;
import com.li.videoapplication.data.model.event.LoginEvent;
import com.li.videoapplication.data.model.event.UserInfomationEvent;
import com.li.videoapplication.data.model.response.GoodsListEntity;
import com.li.videoapplication.data.model.response.MemberCurrencyEntity;
import com.li.videoapplication.framework.TBaseActivity;
import com.li.videoapplication.mvp.Constant;
import com.li.videoapplication.tools.UmengAnalyticsHelper;
import com.li.videoapplication.ui.ActivityManager;
import com.li.videoapplication.ui.DialogManager;
import com.li.videoapplication.mvp.adapter.MallExpListViewAdapter;
import com.li.videoapplication.utils.AppUtil;
import com.li.videoapplication.utils.StringUtil;

import java.util.ArrayList;
import java.util.List;

import me.everything.android.ui.overscroll.OverScrollDecoratorHelper;

/**
 * 活动：商城
 */
public class MallActivity extends TBaseActivity implements OnClickListener, OnGroupClickListener {

    private ExpandableListView mExpListView;
    private MallExpListViewAdapter mAdapter;
    private List<Currency> mDatas;
    private TextView login, name, beanNum;
    private ImageView pic,isV;
    private View userInfo;

    /**
     * 跳转：兑换记录
     */
    private void startExchangeRecordActivity() {
        ActivityManager.startExchangeRecordActivity(this);
    }

    @Override
    public int getContentView() {
        return R.layout.activity_mall;
    }

    @Override
    public int inflateActionBar() {
        return R.layout.actionbar_second;
    }

    @Override
    public void afterOnCreate() {
        super.afterOnCreate();
        setSystemBarBackgroundWhite();
        setAbTitle("商城");
    }

    @Override
    public void initView() {
        super.initView();
        userInfo = findViewById(R.id.mall_userinfo);
        login = (TextView) findViewById(R.id.mall_login);
        name = (TextView) findViewById(R.id.mall_name);
        beanNum = (TextView) findViewById(R.id.mall_beannum);
        pic = (ImageView) findViewById(R.id.mall_pic);
        isV = (ImageView) findViewById(R.id.mall_isv);
        abQuestion.setVisibility(View.VISIBLE);
        abQuestion.setOnClickListener(this);
        login.setOnClickListener(this);
        pic.setOnClickListener(this);

        mExpListView = (ExpandableListView) findViewById(R.id.mall_explistview);
        mExpListView.setGroupIndicator(null);//取消上下展开的图标
        mExpListView.setOnGroupClickListener(this); //ExpandableListView的组监听事件
        OverScrollDecoratorHelper.setUpOverScroll(mExpListView);

        mDatas = new ArrayList<>();
        mAdapter = new MallExpListViewAdapter(this, mDatas);
        mExpListView.setAdapter(mAdapter);

        findViewById(R.id.mall_exchangerecord).setOnClickListener(this);
        findViewById(R.id.mall_sysjqq).setOnClickListener(this);

        refreshUserBar();
    }

    @Override
    public void loadData() {
        super.loadData();
        //商品列表
        DataManager.getGoodsList();
    }

    @Override
    public void onResume() {
        super.onResume();
        //个人魔豆数量
        DataManager.getMemberCurrency(getMember_id());
    }

    private void refreshHeaderView() {
        Member member = getUser();
        setTextViewText(name, member.getNickname());
        setTextViewText(beanNum, StringUtil.formatNum(member.getCurrency()));
        setImageViewImageNet(pic, member.getAvatar());
        if (member.isV()) isV.setVisibility(View.VISIBLE);
    }

    @Override
    public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
        //ExpandableListView的组监听,设置return true不可点击
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.mall_sysjqq:
                AppUtil.startQQChat(this, Constant.SYSJQQID);
                break;
            case R.id.mall_exchangerecord:
                UmengAnalyticsHelper.onEvent(this,UmengAnalyticsHelper.SLIDER,"视界商城-兑换记录");
                if (!isLogin()) {
                    DialogManager.showLogInDialog(this);
                    return;
                }
                startExchangeRecordActivity();
                break;
            case R.id.ab_question:
                WebActivityJS.startWebActivityJS(this, Constant.WEB_WALLET, "视界商城说明", null);
                break;
            case R.id.mall_login:
                DialogManager.showLogInDialog(this);
                UmengAnalyticsHelper.onEvent(this,UmengAnalyticsHelper.SLIDER,"视界商城-登陆");
                break;
            case R.id.mall_pic:
                if (isLogin())
                    ActivityManager.startMyPersonalInfoActivity(this);
                break;
        }
    }

    /**
     * 回调：个人资料刷新事件
     */
    public void onEventMainThread(UserInfomationEvent event) {
        if (event != null) {
            loadData();
        }
    }

    /**
     * 回调：商品列表
     */
    public void onEventMainThread(GoodsListEntity event) {

        if (event != null && event.isResult()) {
            if (event.getData() != null && event.getData().size() > 0) {
                mDatas.clear();
                mDatas.addAll(event.getData());
                mAdapter.notifyDataSetChanged();
                //设置Group默认展开
                int groupCount = mExpListView.getCount();
                for (int i = 0; i < groupCount; i++) {
                    mExpListView.expandGroup(i);
                }
            }
        }
    }

    /**
     * 回调：个人魔豆数量
     */
    public void onEventMainThread(MemberCurrencyEntity event) {
        setTextViewText(beanNum, StringUtil.formatNum(event.getCurrency()));
    }

    /**
     * 事件：登录
     */
    public void onEventMainThread(LoginEvent event) {

        if (event != null) {
            refreshUserBar();
        }
    }

    private void refreshUserBar() {
        if (isLogin()) {
            pic.setVisibility(View.VISIBLE);
            login.setVisibility(View.GONE);
            userInfo.setVisibility(View.VISIBLE);
            refreshHeaderView();
        } else {
            pic.setVisibility(View.GONE);
            login.setVisibility(View.VISIBLE);
            userInfo.setVisibility(View.INVISIBLE);
        }
    }
}
