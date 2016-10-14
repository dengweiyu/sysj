package com.li.videoapplication.ui.fragment;

import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.IPullToRefresh;
import com.li.videoapplication.R;
import com.li.videoapplication.data.DataManager;
import com.li.videoapplication.data.model.response.DynamicDotEntity;
import com.li.videoapplication.data.model.response.MessageMsgRedEntity;
import com.li.videoapplication.data.preferences.PreferencesHepler;
import com.li.videoapplication.framework.TBaseFragment;
import com.li.videoapplication.tools.TimeHelper;
import com.li.videoapplication.tools.UmengAnalyticsHelper;
import com.li.videoapplication.ui.ActivityManeger;
import com.li.videoapplication.utils.LogHelper;
import com.li.videoapplication.views.CircleImageView;

/**
 * 碎片：发现
 */
public class DiscoverFragment extends TBaseFragment implements OnClickListener {

    private CircleImageView count;

    /**
     * 跳转：动态
     */
    private void startDynamicActivity() {
        ActivityManeger.startMyDynamicActivity(getActivity());
    }

    @Override
    protected int getCreateView() {
        return R.layout.fragment_discover;
    }

    @SuppressWarnings("rawtypes")
    @Override
    protected IPullToRefresh getPullToRefresh() {
        return null;
    }

    @Override
    protected void initContentView(View view) {
        count = (CircleImageView) view.findViewById(R.id.discover_dynamic_count);

        view.findViewById(R.id.discover_recommend).setOnClickListener(this);
        view.findViewById(R.id.discover_square).setOnClickListener(this);
        view.findViewById(R.id.discover_billboard).setOnClickListener(this);
        view.findViewById(R.id.discover_dynamic).setOnClickListener(this);
        view.findViewById(R.id.discover_activity).setOnClickListener(this);
        view.findViewById(R.id.discover_gift).setOnClickListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (isLogin()) {
            long dynamicTime = PreferencesHepler.getInstance().getDynamicTime();
            //动态红点
            DataManager.dynamicDot(getMember_id(), dynamicTime);
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        //该fragment处于最前台交互状态
        if (isVisibleToUser) {
            UmengAnalyticsHelper.onEvent(getActivity(), UmengAnalyticsHelper.MAIN, "进入发现页面次数");
            UmengAnalyticsHelper.onEvent(getActivity(), UmengAnalyticsHelper.DISCOVER, "进入发现页面次数");
        }
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.discover_activity:
                ActivityManeger.startActivityListActivity(getActivity());
                break;

            case R.id.discover_gift:
                ActivityManeger.startGiftListActivity(getActivity());
                break;

            case R.id.discover_recommend:
                ActivityManeger.startRecommendActivity(getActivity());
                break;

            case R.id.discover_square:
                ActivityManeger.startSquareActivity(getActivity());
                break;

            case R.id.discover_billboard:
                ActivityManeger.startBillboardActivity(getActivity());
                UmengAnalyticsHelper.onEvent(getActivity(), UmengAnalyticsHelper.DISCOVER, "风云榜");
                break;

            case R.id.discover_dynamic:
                startDynamicActivity();
                break;
        }
    }

    /**
     * 回调：动态更新红点
     */
    public void onEventMainThread(DynamicDotEntity event) {
        if (event != null && event.isResult()) {
            //动态有更新
            if (event.getData().isHasNew()) {
                count.setVisibility(View.VISIBLE);
            } else {
                count.setVisibility(View.GONE);
            }
        }
    }

}
