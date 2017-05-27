package com.li.videoapplication.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.li.videoapplication.R;
import com.li.videoapplication.data.DataManager;
import com.li.videoapplication.data.image.GlideHelper;
import com.li.videoapplication.data.model.entity.Member;
import com.li.videoapplication.data.model.entity.VideoImage;
import com.li.videoapplication.data.model.event.LoginEvent;
import com.li.videoapplication.data.model.event.LogoutEvent;
import com.li.videoapplication.data.model.event.UserInfomationEvent;
import com.li.videoapplication.data.model.response.UserProfileTimelineListsEntity;
import com.li.videoapplication.data.model.response.UserProfileUploadCoverEntity;
import com.li.videoapplication.data.model.response.VideoCollect2Entity;
import com.li.videoapplication.data.model.response.VideoFlower2Entity;
import com.li.videoapplication.framework.PullToRefreshActivity;
import com.li.videoapplication.tools.BitmapLoader;
import com.li.videoapplication.tools.PhotoHelper;
import com.li.videoapplication.tools.UmengAnalyticsHelper;
import com.li.videoapplication.ui.ActivityManager;
import com.li.videoapplication.ui.DialogManager;
import com.li.videoapplication.ui.adapter.DynamicVideoAdapter;
import com.li.videoapplication.ui.dialog.LoadingDialog;
import com.li.videoapplication.utils.StringUtil;
import com.li.videoapplication.views.CircleImageView;

import java.io.File;

/**
 * 活动：个人中心
 */
public class MyPersonalCenterActivity extends PullToRefreshActivity<VideoImage> implements
        AbsListView.OnScrollListener, OnClickListener {

    /**
     * 跳转：我的个人资料
     */
    public void startMyPersonalInfoActivity() {
        ActivityManager.startMyPersonalInfoActivity(this);
        UmengAnalyticsHelper.onEvent(this, UmengAnalyticsHelper.SLIDER, "个人中心-头像-个人资料");
    }

    /**
     * 跳转：我的粉丝
     */
    public void startMyPlayerActivityMyFans() {
        ActivityManager.startMyPlayerActivity(this, MyPlayerActivity.PAGE_MYFANS, getMember_id());
    }

    /**
     * 跳转：我的关注
     */
    public void startMyPlayerActivityMyFocus() {
        ActivityManager.startMyPlayerActivity(this, MyPlayerActivity.PAGE_MYFOCUS, getMember_id());
    }

    /**
     * 弹窗：登陆
     */
    public void showLogInDialog() {
        DialogManager.showLogInDialog(this);
    }

    private PhotoHelper photoHelper = new PhotoHelper();

    /**
     * 跳转：照相
     */
    public void takePhoto() {
        photoHelper.takePhoto(this);
    }

    /**
     * 跳转：相册
     */
    public void pickPhoto() {
        photoHelper.pickPhoto(this);
    }

    private DynamicVideoAdapter adapter;

    private View headerView;
    private View user, tourist, mypersoncenter, focusView;
    private CircleImageView head;
    private TextView loginText, name, introduce, fans, attention;
    private RelativeLayout touch;
    private ImageView loginIcon, textBg, go, text, textBtn, isV;

    private ImageView mVip;
    private int currentpage = 1;
    private int pagelength = 10;

    private Member item;
    private TextView emptyText;



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
        setAbBackgroundTranceparent();
        setAbTitleWhite();
        setAbTitle(R.string.mypersonalcenter_title);
    }

    @Override
    public void initView() {
        super.initView();

        setModeEnd();
        setOnScrollListener(this);
        getHeaderView();
    }

    @Override
    public void loadData() {
        super.loadData();

        item = getUser();
        refreshHeaderView(item);

        onPullDownToRefresh(pullToRefreshListView);
    }

    private View getHeaderView() {
        if (headerView == null) {
            emptyText = (TextView) findViewById(R.id.dynamic_empty);

            headerView = inflater.inflate(R.layout.header_dynamic, null);
            mVip = (ImageView)headerView.findViewById(R.id.iv_dynamic_vip);
            head = (CircleImageView) headerView.findViewById(R.id.dynamic_head);
            isV = (ImageView) headerView.findViewById(R.id.dynamic_v);
            user = headerView.findViewById(R.id.dynamic_user);
            tourist = headerView.findViewById(R.id.dynamic_tourist);
            mypersoncenter = headerView.findViewById(R.id.dynamic_mypersonalcenter);

            loginIcon = (ImageView) headerView.findViewById(R.id.dynamic_loginIcon);
            loginText = (TextView) headerView.findViewById(R.id.dynamic_loginText);

            name = (TextView) headerView.findViewById(R.id.dynamic_name);
            fans = (TextView) headerView.findViewById(R.id.dynamic_fans);
            attention = (TextView) headerView.findViewById(R.id.dynamic_attention);
            introduce = (TextView) headerView.findViewById(R.id.dynamic_introduce);
            focusView = headerView.findViewById(R.id.dynamic_focus_layout);
            text = (ImageView) headerView.findViewById(R.id.dynamic_text);
            textBtn = (ImageView) headerView.findViewById(R.id.dynamic_textBtn);
            textBg = (ImageView) headerView.findViewById(R.id.dynamic_textBg);
            go = (ImageView) headerView.findViewById(R.id.dynamic_go);
            touch = (RelativeLayout) headerView.findViewById(R.id.dynamic_touch);

            loginIcon.setOnClickListener(this);
            loginText.setOnClickListener(this);
            mypersoncenter.setOnClickListener(this);

            head.setOnClickListener(this);
            textBtn.setOnClickListener(this);
            touch.setOnClickListener(this);
            fans.setOnClickListener(this);
            attention.setOnClickListener(this);
            focusView.setVisibility(View.GONE);
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
            setAbStyleTranceparent();
            setAbTitle(R.string.mypersonalcenter_title);
        }
    }

    @Override
    public void onScrollStateChanged(AbsListView listview, int scrollState) {
    }

    /**
     * 页头数据
     */
    private void refreshHeaderView(Member item) {
        if (isLogin()) {// 登录用户
            user.setVisibility(View.VISIBLE);
            tourist.setVisibility(View.GONE);
            setAbGobackWhite();
            if (item != null) {
                setImageViewImageNet(head, item.getAvatar());
                if (item.isV()) {
                    isV.setVisibility(View.VISIBLE);
                } else {
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
                    isV.setVisibility(View.INVISIBLE);
                }
                if (!StringUtil.isNull(item.getCover())) {
                    GlideHelper.displayImageEmpty(this, item.getCover(), textBg);
                    text.setVisibility(View.INVISIBLE);
                } else {
                    text.setVisibility(View.VISIBLE);
                }

                setTextViewText(name, item.getNickname());
                if (StringUtil.isNull(item.getSignature())) {
                    setTextViewText(introduce, "");
                } else {
                    setTextViewText(introduce, item.getSignature());
                }
                setMark(fans, attention, item);
            }
        } else {// 游客
            tourist.setVisibility(View.VISIBLE);
            user.setVisibility(View.GONE);
            setAbGobackGray();
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
            case R.id.dynamic_fans:
                if (!isLogin()) {
                    showLogInDialog();
                    return;
                }
                startMyPlayerActivityMyFans();
                break;

            case R.id.dynamic_attention:
                if (!isLogin()) {
                    showLogInDialog();
                    return;
                }
                startMyPlayerActivityMyFocus();
                break;

            case R.id.dynamic_head:
            case R.id.dynamic_mypersonalcenter:
                if (!isLogin()) {
                    showLogInDialog();
                    return;
                }
                startMyPersonalInfoActivity();
                break;

            case R.id.dynamic_touch:// 更换封面
                break;

            case R.id.dynamic_textBtn:// 更换封面
                if (!isLogin()) {
                    showLogInDialog();
                    return;
                }
                DialogManager.showPhotoDialog(this, this, this);
                UmengAnalyticsHelper.onEvent(this, UmengAnalyticsHelper.SLIDER, "个人中心-背景更换");
                break;

            case R.id.photo_pick:
                pickPhoto();
                break;

            case R.id.photo_take:
                takePhoto();
                break;
        }
    }

    @Override
    public void onRefresh() {
        doRequest();
    }

    @Override
    public void onLoadMore() {
        doRequest();
    }

    protected void doRequest() {

        // 个人视频（时间轴）
        DataManager.userProfileTimelineLists(getMember_id(), getMember_id(), currentpage, pagelength);
    }

    /**
     * 回调：相册，照相
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.i(tag, "requestCode=" + requestCode + "/resultCode=" + resultCode + "/data=" + data);

        if (resultCode == Activity.RESULT_CANCELED) {
            return;
        }
        if (requestCode == PhotoHelper.REQUESTCODE_PHOTO_TAKE) {
            String path = photoHelper.getPath();
            if (path != null)
                updatePhoto(path);
        }
        if (requestCode == PhotoHelper.REQUESTCODE_PHOTO_PICK) {
            if (data != null) {
                Uri uri = data.getData();
                if (uri != null) {
                    Cursor cursor = getContentResolver().query(uri, null, null, null, null);
                    if (null == cursor || !cursor.moveToFirst()) {
                        showToastShort("没有找到您想要的图片");
                        return;
                    }
                    int columnIndex = cursor.getColumnIndex("_data");
                    String path = cursor.getString(columnIndex);
                    if (path != null) {
                        File file = new File(path);
                        if (file == null || !file.exists()) {
                            showToastShort("没有找到您想要的图片");
                            return;
                        }
                    }
                    cursor.close();
                    updatePhoto(path);
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void updatePhoto(String originPath) {

        Bitmap bitmap = com.li.videoapplication.tools.BitmapHelper.getimage(originPath);
        String tempPath = BitmapLoader.saveBitmapToLocal(bitmap);
        File file = new File(tempPath);
        // 上传封面
        DataManager.userProfileUploadCover(getMember_id(), file);
        showProgressDialog(LoadingDialog.LOADING);
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
        refreshAdapter();
        isRefreshing = false;
        refreshComplete();
    }

    private void refreshAdapter() {
        if (getAdapter() == null) {
            addHeaderView(getHeaderView());
            adapter = new DynamicVideoAdapter(this, data);
            setAdapter(adapter);
        } else {
            adapter.notifyDataSetChanged();
        }
    }

    /**
     * 回调：上传封面
     */
    public void onEventMainThread(UserProfileUploadCoverEntity event) {

        dismissProgressDialog();
        if (event != null) {
            if (event.isResult()) {
                showToastShort("封面上传成功");
            }
        }
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
     * 事件：登录
     */
    public void onEventMainThread(LoginEvent event) {

        onPullDownToRefresh(pullToRefreshListView);
    }

    /**
     * 事件：注销
     */
    public void onEventMainThread(LogoutEvent event) {

        finish();
    }

    /**
     * 事件：更新个人资料
     */
    public void onEventMainThread(UserInfomationEvent event) {

        if (event != null) {

            item = getUser();
            refreshHeaderView(item);
        }
    }


}
