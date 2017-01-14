package com.li.videoapplication.ui.fragment;

import android.app.Activity;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.IPullToRefresh;
import com.li.videoapplication.R;
import com.li.videoapplication.data.DataManager;
import com.li.videoapplication.data.local.ScreenShotResponseObject;
import com.li.videoapplication.data.local.ScreenShotEntity;
import com.li.videoapplication.framework.TBaseFragment;
import com.li.videoapplication.tools.IntentHelper;
import com.li.videoapplication.tools.UmengAnalyticsHelper;
import com.li.videoapplication.ui.activity.VideoMangerActivity;
import com.li.videoapplication.ui.adapter.MyScreenShotAdapter;
import com.li.videoapplication.utils.StringUtil;
import com.li.videoapplication.utils.TextUtil;

import java.util.ArrayList;
import java.util.List;

import me.everything.android.ui.overscroll.VerticalOverScrollBounceEffectDecorator;
import me.everything.android.ui.overscroll.adapters.AbsListViewOverScrollDecorAdapter;

/**
 * 碎片：截图
 */
public class MyScreenShotFragment extends TBaseFragment implements View.OnClickListener {

	public ListView listView;
    public MyScreenShotAdapter adapter;
    public List<ScreenShotEntity> data = new ArrayList<ScreenShotEntity>();

    // 空间提示
	public TextView storgeText;
	private LinearLayout storgeRoot;

    private VideoMangerActivity activity;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            this.activity = (VideoMangerActivity) activity;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            UmengAnalyticsHelper.onEvent(getActivity(), UmengAnalyticsHelper.SLIDER, "我的视频-截图");
        }
    }

    @Override
    protected IPullToRefresh getPullToRefresh() {
        return null;
    }

    @Override
    protected int getCreateView() {
        return R.layout.fragment_mylocalvideo;
    }

    @Override
    protected void initContentView(View view) {

        initListView(view);

        // 加载截图
        DataManager.LOCAL.loadScreenShots();
    }

	private void initListView(View view) {

        storgeText = (TextView) view.findViewById(R.id.videomanager_text);
        storgeRoot = (LinearLayout) view.findViewById(R.id.videomanager_root);
        storgeRoot.setVisibility(View.GONE);
        storgeText.setText("");

        listView = (ListView) view.findViewById(R.id.listview);
        new VerticalOverScrollBounceEffectDecorator(new AbsListViewOverScrollDecorAdapter(listView));

        TextView emptyText = (TextView) view.findViewById(R.id.mylocalvideo_empty);
        emptyText.setText("您还没有截图");
        listView.setEmptyView(emptyText);

        adapter = new MyScreenShotAdapter(getActivity(), data);
        listView.setAdapter(adapter);
	}

    public void refreshStorge() {

        if (storgeRoot == null || storgeText == null)
            return;

        if (activity != null &&
                !StringUtil.isNull(activity.inSDCardTotalSize) &&
                !StringUtil.isNull(activity.inSDCardUsedSize) &&
                !StringUtil.isNull(activity.inSDCardAppSize)) {// 显示
            storgeRoot.setVisibility(View.VISIBLE);
            storgeText.setText(Html.fromHtml(
                    "共" + toRedColor(activity.myLocalVideoSize + "") + "个视频，" +
                            toRedColor(activity.myScreenShotSize + "") + "张图片，" +
                            "已使用内存" + toRedColor(activity.inSDCardUsedSize) + "/" + toRedColor(activity.inSDCardTotalSize)));
        } else {
            storgeRoot.setVisibility(View.GONE);
            storgeText.setText("");
        }
    }

    private String toRedColor(String text) {
        return TextUtil.toColor(text, "#fc3c2d");
    }

    @Override
    public void onClick(View v) {

    }

    /**
     * 回调：加载截图
     */
    public void onEventMainThread(ScreenShotResponseObject event) {

        if (event.getData() != null) {
            data.clear();
            data.addAll(event.getData());
            activity.setMyScreenShotSize(data.size());
            adapter.notifyDataSetChanged();
        }
    }
}
