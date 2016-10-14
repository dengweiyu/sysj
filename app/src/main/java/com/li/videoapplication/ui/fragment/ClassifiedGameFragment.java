package com.li.videoapplication.ui.fragment;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.IPullToRefresh;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.li.videoapplication.R;
import com.li.videoapplication.data.DataManager;
import com.li.videoapplication.data.model.entity.Game;
import com.li.videoapplication.data.model.entity.GroupType;
import com.li.videoapplication.data.model.response.GameListEntity;
import com.li.videoapplication.data.model.response.GroupAttentionGroupEntity;
import com.li.videoapplication.data.model.response.GroupType2Entity;
import com.li.videoapplication.data.network.RequestConstant;
import com.li.videoapplication.framework.AppConstant;
import com.li.videoapplication.framework.PullToRefreshActivity;
import com.li.videoapplication.framework.TBaseFragment;
import com.li.videoapplication.tools.PullToRefreshHepler;
import com.li.videoapplication.tools.UmengAnalyticsHelper;
import com.li.videoapplication.ui.adapter.ClassifiedAdapter;
import com.li.videoapplication.ui.adapter.ClassifiedGameAdapter;
import com.li.videoapplication.utils.ScreenUtil;
import com.li.videoapplication.views.GridViewY1;

/**
 * 碎片：游戏分类
 */
public class ClassifiedGameFragment extends TBaseFragment implements OnRefreshListener2<ListView>, OnClickListener {

    private PullToRefreshListView mPullToRefreshListView;
    private ListView mListView;
    private ClassifiedGameAdapter adapter;
    private List<Game> data;

    private int page = 1;
    public String sort = RequestConstant.GAMELIST_SORT_HOT;

    private View mHeaderView;
    private GridViewY1 mGridView;
    private ClassifiedAdapter headerAdapter;
    private List<GroupType> headerData;
    private View mTitleView;
    private TextView show;
    private RelativeLayout pop;

    @Override
    protected int getCreateView() {
        return R.layout.fragment_classifiedgame;
    }

    @SuppressWarnings("rawtypes")
    @Override
    protected IPullToRefresh getPullToRefresh() {
        return mPullToRefreshListView;
    }

    @Override
    protected void initContentView(View view) {

        mPullToRefreshListView = (PullToRefreshListView) view.findViewById(R.id.pulltorefresh);
        mPullToRefreshListView.setMode(Mode.PULL_FROM_END);
        mListView = mPullToRefreshListView.getRefreshableView();

        data = new ArrayList<>();
        adapter = new ClassifiedGameAdapter(getActivity(), data, this);

        mListView.addHeaderView(getHeaderView());
        mListView.addHeaderView(getTitleView());
        showHeaderView(true);
        mListView.setAdapter(adapter);

        mPullToRefreshListView.setOnRefreshListener(this);

        if (sort.equals(RequestConstant.GAMELIST_SORT_TIME)) {
            setTextViewText(show, R.string.classifiedgame_new);
        } else if (sort.equals(RequestConstant.GAMELIST_SORT_HOT)) {
            setTextViewText(show, R.string.classifiedgame_hot);
        }

        handler.postDelayed(new Runnable() {

            @Override
            public void run() {

                onPullDownToRefresh(mPullToRefreshListView);
            }
        }, AppConstant.TIME.GAME_CLASSIFIED);
    }

    private View getHeaderView() {

        if (mHeaderView == null) {
            mHeaderView = inflater.inflate(R.layout.header_classifiedgame_classified, null);
            ListView.LayoutParams params = new ListView.LayoutParams(ListView.LayoutParams.MATCH_PARENT, ListView.LayoutParams.WRAP_CONTENT);
            mHeaderView.setLayoutParams(params);
            mGridView = (GridViewY1) mHeaderView.findViewById(R.id.gridview);
            headerData = new ArrayList<GroupType>();
            for (int i = 0; i < 8; i++) {
                headerData.add(new GroupType());
            }
            headerAdapter = new ClassifiedAdapter(getActivity(), headerData);
            mGridView.setAdapter(headerAdapter);
        }
        return mHeaderView;
    }

    private View getTitleView() {

        if (mTitleView == null) {
            mTitleView = View.inflate(getActivity(), R.layout.header_classifiedgame_title, null);
            show = (TextView) mTitleView.findViewById(R.id.classifiedgame_title_text);
            pop = (RelativeLayout) mTitleView.findViewById(R.id.classifiedgame_title_pop);
            pop.setOnClickListener(this);
            setLayoutParams(mTitleView, 22);
        }
        return mTitleView;
    }

    private void setLayoutParams(View view, int height) {
        if (view != null) {
            int pxHeight = ScreenUtil.dp2px(height);
            ListView.LayoutParams params = new ListView.LayoutParams(ListView.LayoutParams.MATCH_PARENT, pxHeight);
            view.setLayoutParams(params);
        }
    }

    private void showHeaderView(boolean isShow) {
        if (isShow) {
            mGridView.setVisibility(View.VISIBLE);
        } else {
            mGridView.setVisibility(View.GONE);
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {

        page = 1;
        onPullUpToRefresh(refreshView);

        handler.postDelayed(new Runnable() {

            @Override
            public void run() {
                // 游戏圈子类型
                DataManager.groupType2();
            }
        }, 200);
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
        PullToRefreshHepler.setLastUpdatedLabel(refreshView);
        onRefreshCompleteDelayed(PullToRefreshActivity.TIME_REFRESH_SHORT);

        // 最热，最新游戏列表
        DataManager.gameList(page, getMember_id(), sort);
    }

    private PopupWindow popupWindow;
    private CheckBox newCheckBox, hotCheckBox;

    private void showPopupWindow(View view) {

        View v = inflater.inflate(R.layout.popup_classifiedgame, null);
        v.findViewById(R.id.classifiedgame_new).setOnClickListener(this);
        v.findViewById(R.id.classifiedgame_hot).setOnClickListener(this);
        newCheckBox = (CheckBox) v.findViewById(R.id.classifiedgame_new_check);
        hotCheckBox = (CheckBox) v.findViewById(R.id.classifiedgame_hot_check);

        if (sort.equals(RequestConstant.GAMELIST_SORT_TIME)) {
            newCheckBox.setChecked(true);
            hotCheckBox.setChecked(false);
        } else if (sort.equals(RequestConstant.GAMELIST_SORT_HOT)) {
            hotCheckBox.setChecked(true);
            newCheckBox.setChecked(false);
        }

        int w = ScreenUtil.dp2px(106);
        int h = ScreenUtil.dp2px(66);
        popupWindow = new PopupWindow(v, w, h, true);
        popupWindow.setTouchable(true);
        popupWindow.setTouchInterceptor(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
            }
        });
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.BLACK));
        int xoff = ScreenUtil.dp2px(16);
        int yoff = ScreenUtil.dp2px(4);
        popupWindow.showAsDropDown(view, xoff, yoff);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.classifiedgame_title_pop:
                showPopupWindow(pop);
                break;

            case R.id.classifiedgame_new:
                hotCheckBox.setChecked(false);
                newCheckBox.setChecked(true);
                setTextViewText(show, R.string.classifiedgame_new);
                popupWindow.dismiss();
                sort = RequestConstant.GAMELIST_SORT_TIME;
                onPullDownToRefresh(mPullToRefreshListView);
                UmengAnalyticsHelper.onEvent(getActivity(), UmengAnalyticsHelper.GAME, "找游戏-最新游戏");
                break;

            case R.id.classifiedgame_hot:
                newCheckBox.setChecked(false);
                hotCheckBox.setChecked(true);
                setTextViewText(show, R.string.classifiedgame_hot);
                popupWindow.dismiss();
                sort = RequestConstant.GAMELIST_SORT_HOT;
                onPullDownToRefresh(mPullToRefreshListView);
                break;

            default:
                break;
        }
    }

    /**
     * 圈子类型回调
     */
    public void onEventMainThread(GroupType2Entity event) {

        if (event != null) {
            boolean result = event.isResult();
            if (result) {
                headerData.clear();
                headerData.addAll(event.getData());
                headerAdapter.notifyDataSetChanged();
                showHeaderView(true);
                onRefreshComplete();
            }
        }
    }

    /**
     * 最热，最新游戏列表回调
     */
    public void onEventMainThread(GameListEntity event) {

        if (event != null) {
            boolean result = event.isResult();
            if (result) {
                if (page == 1) {
                    data.clear();
                }
                data.addAll(event.getData().getList());
                adapter.notifyDataSetChanged();
                if (event.getData().getList().size() > 0) {
                    ++page;
                }
                onRefreshComplete();
            }
        }
    }

    /**
     * 回调：关注圈子201
     */
    public void onEventMainThread(GroupAttentionGroupEntity event) {

        if (event != null) {
            if (event.isResult()) {
                showToastShort(event.getMsg());
            }
        }
    }
}
