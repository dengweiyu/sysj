package com.li.videoapplication.ui.activity;

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
import com.li.videoapplication.data.model.entity.Member;
import com.li.videoapplication.data.model.entity.VideoImage;
import com.li.videoapplication.data.model.response.MemberAttention201Entity;
import com.li.videoapplication.data.model.response.UserProfilePersonalInformationEntity;
import com.li.videoapplication.data.model.response.UserProfileTimelineListsEntity;
import com.li.videoapplication.data.model.response.VideoCollect2Entity;
import com.li.videoapplication.data.model.response.VideoFlower2Entity;
import com.li.videoapplication.data.network.UITask;
import com.li.videoapplication.framework.PullToRefreshActivity;
import com.li.videoapplication.ui.ActivityManeger;
import com.li.videoapplication.ui.adapter.DynamicVideoAdapter;
import com.li.videoapplication.utils.StringUtil;
import com.li.videoapplication.views.CircleImageView;

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
        ActivityManeger.startPlayerPersonalInfoActivity(this, member);
    }

    /**
     * 跳转：我的粉丝
     */
    public void startMyPlayerActivityMyFans() {
        ActivityManeger.startMyPlayerActivity(this, MyPlayerActivity.PAGE_MYFANS, item.getMember_id());
    }

    /**
     * 跳转：我的关注
     */
    public void startMyPlayerActivityMyFocus() {
        ActivityManeger.startMyPlayerActivity(this, MyPlayerActivity.PAGE_MYFOCUS, item.getMember_id());
    }


    private DynamicVideoAdapter adapter;
    private View noData;
    private View headerView;
    private View user, tourist;
    private CircleImageView head;
    private TextView loginText, name, fans, attention, introduce, focus;
    private RelativeLayout touch;
    private ImageView loginIcon, textBg, go, text, textBtn, head_v;

    private View emptyView;

    private int currentpage = 1;
    private int pagelength = 10;

    private Member item;

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

    private View getHeaderView() {
        if (headerView == null) {
            noData = findViewById(R.id.nodata);

            headerView = inflater.inflate(R.layout.header_dynamic, null);
            head = (CircleImageView) headerView.findViewById(R.id.dynamic_head);
            head_v = (ImageView) headerView.findViewById(R.id.dynamic_v);

            user = headerView.findViewById(R.id.dynamic_user);
            tourist = headerView.findViewById(R.id.dynamic_tourist);

            loginIcon = (ImageView) headerView.findViewById(R.id.dynamic_loginIcon);
            loginText = (TextView) headerView.findViewById(R.id.dynamic_loginText);

            name = (TextView) headerView.findViewById(R.id.dynamic_name);
//            level = (TextView) headerView.findViewById(R.id.dynamic_level);
            fans = (TextView) headerView.findViewById(R.id.dynamic_fans);
            attention = (TextView) headerView.findViewById(R.id.dynamic_attention);
            introduce = (TextView) headerView.findViewById(R.id.dynamic_introduce);
            focus = (TextView) headerView.findViewById(R.id.dynamic_focus);
            text = (ImageView) headerView.findViewById(R.id.dynamic_text);
            textBtn = (ImageView) headerView.findViewById(R.id.dynamic_textBtn);
            textBg = (ImageView) headerView.findViewById(R.id.dynamic_textBg);
            go = (ImageView) headerView.findViewById(R.id.dynamic_go);
            touch = (RelativeLayout) headerView.findViewById(R.id.dynamic_touch);

            loginIcon.setOnClickListener(this);
            loginText.setOnClickListener(this);

            head.setOnClickListener(this);
            textBtn.setOnClickListener(this);
            focus.setOnClickListener(this);
            touch.setOnClickListener(this);
            go.setOnClickListener(this);
            fans.setOnClickListener(this);
            attention.setOnClickListener(this);

            textBtn.setVisibility(View.GONE);
            focus.setVisibility(View.GONE);
            text.setVisibility(View.INVISIBLE);
            setListViewLayoutParams(headerView, 206);
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
//            setLevel(level, item);
            setMark(fans, attention, item);
            setFocus(item);
            if (!StringUtil.isNull(item.getCover())) {
                setImageViewImageNet(textBg, item.getCover());
            }

            setAbBackgroundTranceparent();
            setAbGobackWhite();
            setAbTitleWhite();
        }
    }

    /**
     * 关注
     */
    public void setFocus(Member item) {
        if (item != null) {
            if (item.getIsAttent() == 1) {// 已关注
                focus.setBackgroundResource(R.drawable.focus_gray);
                setTextViewText(focus, R.string.dynamic_focused);
            } else { // 未关注
                focus.setBackgroundResource(R.drawable.focus_red);
                setTextViewText(focus, R.string.dynamic_focus);
            }
        }
    }

    /**
     * 等级
     *
     * @return Lv.11
     */
//    private void setLevel(TextView view, Member item) {
//        level.setText("Lv." + item.getDegree());
//    }

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

            case R.id.dynamic_focus:
                if (!isLogin()) {
                    showToastShort("请先登录！");
                    return;
                }
                if (item.getIsAttent() == 1) {
                    item.setIsAttent(0);
                } else {
                    item.setIsAttent(1);
                }
                // 玩家关注
                DataManager.memberAttention201(item.getMember_id(), getMember_id());
                refreshHeaderView(item);
                break;
            case R.id.dynamic_fans:
                if (!isLogin()) {
                    showToastLogin();
                    return;
                }
                startMyPlayerActivityMyFans();
                break;

            case R.id.dynamic_attention:
                if (!isLogin()) {
                    showToastLogin();
                    return;
                }
                startMyPlayerActivityMyFocus();
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
                item = m;
            }
            refreshHeaderView(item);
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
                    noData.setVisibility(View.VISIBLE);
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
