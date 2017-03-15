package com.li.videoapplication.ui.activity;

import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.li.videoapplication.R;
import com.li.videoapplication.framework.AppManager;
import com.li.videoapplication.framework.TBaseActivity;
import com.li.videoapplication.tools.UmengAnalyticsHelper;
import com.li.videoapplication.ui.fragment.MyCollectionFragment;
import com.li.videoapplication.ui.fragment.MyHistoryFragment;
import com.li.videoapplication.ui.pageradapter.GamePagerAdapter;
import com.li.videoapplication.tools.ToastHelper;
import com.li.videoapplication.views.ViewPagerY4;

import java.util.ArrayList;
import java.util.List;

/**
 * 活动：收藏
 */
public class CollectionActivity extends TBaseActivity implements OnClickListener {

    private View topContainer;
    private View bottomContainer;
    private TextView bottomAll, bottomDlete;

    @Override
    public int getContentView() {
        return R.layout.activity_collection;
    }

    public int inflateActionBar() {
        return R.layout.actionbar_second;
    }

    @Override
    public void afterOnCreate() {
        super.afterOnCreate();

        setSystemBarBackgroundWhite();
        setAbTitle(R.string.collection_title);
    }

    @Override
    public void initView() {
        super.initView();

        initContentView();
        initTopMenu();
    }

    @Override
    public void doBack(View view) {
        if (myCollectionFragment.adapter.isDeleteMode() || myHistoryFragment.adapter.isDeleteMode()) {
            abCollectionCancel.performClick();
        } else {
            super.doBack(view);
        }
    }

    @Override
    public void onBackPressed() {

        if (myCollectionFragment.adapter.isDeleteMode() || myHistoryFragment.adapter.isDeleteMode()) {
            abCollectionCancel.performClick();
        } else {
            super.onBackPressed();
        }
    }

    private void initContentView() {

        topContainer = findViewById(R.id.container);
        bottomContainer = findViewById(R.id.bottom);
        bottomAll = (TextView) findViewById(R.id.bottom_left);
        bottomDlete = (TextView) findViewById(R.id.bottom_right);

        abCollectionDelte.setVisibility(View.VISIBLE);
        abCollectionCancel.setVisibility(View.GONE);
        bottomContainer.setVisibility(View.GONE);

        abCollectionDelte.setOnClickListener(this);
        abCollectionCancel.setOnClickListener(this);
        bottomAll.setOnClickListener(this);
        bottomDlete.setOnClickListener(this);
    }

    /**
     * 底部编辑状态
     *
     * @param flag true 正常状态 false 编辑状态
     */
    private void refreshContentView(boolean flag) {
        if (flag) {// 正常状态
            if (currIndex == 0) {
                myCollectionFragment.adapter.setDeleteMode(false);
            } else if (currIndex == 1) {
                myHistoryFragment.adapter.setDeleteMode(false);
            }
            viewPager.setScrollable(true);
            abGoback.setVisibility(View.VISIBLE);
            abCollectionDelte.setVisibility(View.VISIBLE);
            abCollectionCancel.setVisibility(View.GONE);
            bottomContainer.setVisibility(View.GONE);
            setTopMenu(true);
        } else {// 删除状态
            if (currIndex == 0) {
                myCollectionFragment.adapter.setDeleteMode(true);
                myHistoryFragment.adapter.setDeleteMode(false);
            } else if (currIndex == 1) {
                myCollectionFragment.adapter.setDeleteMode(false);
                myHistoryFragment.adapter.setDeleteMode(true);
            }
            viewPager.setScrollable(false);
            abGoback.setVisibility(View.GONE);
            abCollectionDelte.setVisibility(View.GONE);
            abCollectionCancel.setVisibility(View.VISIBLE);
            bottomContainer.setVisibility(View.VISIBLE);
            setTopMenu(false);
        }
    }

    /**
     * 顶部菜单是否可以点击
     */
    private void setTopMenu(boolean clickable) {
        for (RelativeLayout view : topButtons) {
            view.setEnabled(clickable);
            view.setClickable(clickable);
        }
    }

    /**
     * 虚拟点击编辑按钮
     */
    public static void performClick2() {
        CollectionActivity activity = (CollectionActivity) AppManager.getInstance().getActivity(CollectionActivity.class);
        if (activity != null) {
            activity.performClick();
        }
    }

    /**
     * 虚拟点击编辑按钮
     */
    public void performClick() {
        abCollectionDelte.performClick();
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            /**删除图标*/
            case R.id.ab_collection_delete:
                if (currIndex == 0 && myCollectionFragment.data.size() == 0) {
                    ToastHelper.s("当前列表没有视频");
                    return;
                }
                if (currIndex == 1 && myHistoryFragment.data.size() == 0) {
                    ToastHelper.s("当前列表没有视频");
                    return;
                }

                refreshContentView(false);
                untraverseData();
                bottomAll.setText("全选");
                bottomAll.setTextColor(Color.parseColor("#323232"));

                /**我的收藏*/
                if (currIndex == 0) {
                    myCollectionFragment.pullToRefreshGridView.setMode(PullToRefreshBase.Mode.DISABLED);
                    myCollectionFragment.adapter.notifyDataSetChanged();
                    /**历史纪录*/
                } else if (currIndex == 1) {
                    myHistoryFragment.adapter.notifyDataSetChanged();
                    UmengAnalyticsHelper.onEvent(this,UmengAnalyticsHelper.SLIDER,"观看记录-删除");
                }
                break;

            /**取消删除*/
            case R.id.ab_collection_cancel:
                refreshContentView(true);
                /**我的收藏*/
                if (currIndex == 0) {
                    myCollectionFragment.pullToRefreshGridView.setMode(PullToRefreshBase.Mode.BOTH);
                    myCollectionFragment.adapter.notifyDataSetChanged();
                    /**历史纪录*/
                } else if (currIndex == 1) {
                    myHistoryFragment.adapter.notifyDataSetChanged();
                }
                break;

            /**全选和反选*/
            case R.id.bottom_left:

                if (bottomAll.getText().equals("全选")) {
                    traverseData();
                    bottomAll.setText("取消全选");
                    bottomAll.setTextColor(Color.parseColor("#005500"));
                } else {
                    untraverseData();
                    bottomAll.setText("全选");
                    bottomAll.setTextColor(Color.parseColor("#323232"));
                }
                break;

            /**确定删除*/
            case R.id.bottom_right:
                if (currIndex == 0 && myCollectionFragment.adapter.deleteData.size() == 0) {
                    ToastHelper.s("请选择要删除的视频收藏");
                    return;
                }
                if (currIndex == 1 && myHistoryFragment.adapter.deleteData.size() == 0) {
                    ToastHelper.s("请选择要删除的观看记录");
                    return;
                }
                refreshContentView(true);
                /*我的收藏*/
                if (currIndex == 0) {
                    myCollectionFragment.deleteData();
                    UmengAnalyticsHelper.onEvent(this, UmengAnalyticsHelper.SLIDER, "我的收藏-删除");
                /*观看记录*/
                } else if (currIndex == 1) {
                    myHistoryFragment.deleteData();
                    UmengAnalyticsHelper.onEvent(this, UmengAnalyticsHelper.SLIDER, "观看记录-删除");
                }
                break;
        }
    }

    /**
     * 反相遍历数据
     */
    private void untraverseData() {
        if (currIndex == 0) {
            for (int i = 0; i < myCollectionFragment.data.size(); i++) {
                myCollectionFragment.adapter.positionData.set(i, false);
            }
            myCollectionFragment.adapter.deleteData.clear();
            myCollectionFragment.adapter.notifyDataSetChanged();
        } else if (currIndex == 1) {
            for (int i = 0; i < myHistoryFragment.data.size(); i++) {
                myHistoryFragment.adapter.positionData.set(i, false);
            }
            myHistoryFragment.adapter.deleteData.clear();
            myHistoryFragment.adapter.notifyDataSetChanged();
        }
    }

    /***
     * 遍历并选择数据
     */
    private void traverseData() {

        if (currIndex == 0) {// 本地视频
            myCollectionFragment.adapter.deleteData.clear();
            for (int i = 0; i < myCollectionFragment.data.size(); i++) {
                myCollectionFragment.adapter.positionData.set(i, true);
                myCollectionFragment.adapter.deleteData.add(myCollectionFragment.data.get(i));
            }
            myCollectionFragment.adapter.notifyDataSetChanged();
        } else if (currIndex == 1) {// 云端视频
            myHistoryFragment.adapter.deleteData.clear();
            for (int i = 0; i < myHistoryFragment.data.size(); i++) {
                myHistoryFragment.adapter.positionData.set(i, true);
                myHistoryFragment.adapter.deleteData.add(myHistoryFragment.data.get(i));
            }
            myHistoryFragment.adapter.notifyDataSetChanged();
        }
    }

    private List<RelativeLayout> topButtons;
    private List<ImageView> topLines;
    private List<ImageView> topPoints;
    private List<TextView> topTexts;
    private int currIndex = 0;// 当前页卡编号
    private List<Fragment> fragments;
    private ViewPagerY4 viewPager;
    private GamePagerAdapter adapter;
    private MyCollectionFragment myCollectionFragment;
    private MyHistoryFragment myHistoryFragment;

    protected void initTopMenu() {

        if (topButtons == null) {
            topButtons = new ArrayList<RelativeLayout>();
            topButtons.add((RelativeLayout) findViewById(R.id.top_left));
            topButtons.add((RelativeLayout) findViewById(R.id.top_right));
        }

        if (topLines == null) {
            topLines = new ArrayList<ImageView>();
            topLines.add((ImageView) findViewById(R.id.top_left_line));
            topLines.add((ImageView) findViewById(R.id.top_right_line));
        }

        if (topTexts == null) {
            topTexts = new ArrayList<TextView>();
            topTexts.add((TextView) findViewById(R.id.top_left_text));
            topTexts.add((TextView) findViewById(R.id.top_right_text));
        }

        if (topPoints == null) {
            topPoints = new ArrayList<ImageView>();
            topPoints.add((ImageView) findViewById(R.id.top_left_point));
            topPoints.add((ImageView) findViewById(R.id.top_right_point));
        }

        if (fragments == null) {
            fragments = new ArrayList<Fragment>();
            myCollectionFragment = new MyCollectionFragment();
            fragments.add(myCollectionFragment);
            myHistoryFragment = new MyHistoryFragment();
            fragments.add(myHistoryFragment);
        }

        viewPager = (ViewPagerY4) findViewById(R.id.viewpager);
        viewPager.setScrollable(true);
        adapter = new GamePagerAdapter(manager, fragments);
        viewPager.setAdapter(adapter);
        PageChangeListener listener = new PageChangeListener();
        viewPager.addOnPageChangeListener(listener);

        for (int i = 0; i < topButtons.size(); i++) {
            OnTabClickListener onTabClickListener = new OnTabClickListener(i);
            topButtons.get(i).setOnClickListener(onTabClickListener);
        }

        setTextViewText(topTexts.get(0), R.string.collection_top_left);
        setTextViewText(topTexts.get(1), R.string.collection_top_right);

        for (ImageView point : topPoints) {
            point.setVisibility(View.GONE);
        }

        switchTab(0);
        viewPager.setCurrentItem(0);
    }

    private class PageChangeListener implements OnPageChangeListener {

        @Override
        public void onPageScrollStateChanged(int arg0) {
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }

        @Override
        public void onPageSelected(int position) {
            currIndex = position;
            switchTab(position);
        }
    }

    private class OnTabClickListener implements OnClickListener {

        private int index;

        public OnTabClickListener(int i) {
            this.index = i;
        }

        @Override
        public void onClick(View v) {
            viewPager.setCurrentItem(index);
            switchTab(index);
        }
    }

    private void switchTab(int index) {
        for (int i = 0; i < topTexts.size(); i++) {
            if (index == i) {
                topTexts.get(i).setTextColor(resources.getColorStateList(R.color.menu_game_red));
            } else {
                topTexts.get(i).setTextColor(resources.getColorStateList(R.color.menu_game_gray));
            }
        }
        for (int i = 0; i < topLines.size(); i++) {
            if (index == i) {
                topLines.get(i).setImageResource(R.color.menu_game_red);
            } else {
                topLines.get(i).setImageResource(R.color.menu_game_transperent);
            }
        }
    }
}
