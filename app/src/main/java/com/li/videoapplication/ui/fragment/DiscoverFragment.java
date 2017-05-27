package com.li.videoapplication.ui.fragment;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import com.handmark.pulltorefresh.library.IPullToRefresh;
import com.li.videoapplication.R;
import com.li.videoapplication.data.DataManager;
import com.li.videoapplication.data.model.response.DynamicDotEntity;
import com.li.videoapplication.data.model.response.SquareDotEntity;
import com.li.videoapplication.data.model.response.SweepstakeStatusEntity;
import com.li.videoapplication.data.preferences.PreferencesHepler;
import com.li.videoapplication.framework.TBaseFragment;
import com.li.videoapplication.mvp.Constant;
import com.li.videoapplication.mvp.billboard.view.BillboardActivity;
import com.li.videoapplication.tools.UmengAnalyticsHelper;
import com.li.videoapplication.ui.ActivityManager;
import com.li.videoapplication.ui.activity.WebActivity;
import com.li.videoapplication.views.CircleImageView;

/**
 * 碎片：发现
 */
public class DiscoverFragment extends TBaseFragment implements OnClickListener {

    private CircleImageView count;
    private View mSquareUnRead;
    private ImageView go;
    private View draw;

    private int mNum = 0;	//进入页面的次数
    private long mLastLoadTime;
    /**
     * 跳转：动态
     */
    private void startDynamicActivity() {
        ActivityManager.startMyDynamicActivity(getActivity());
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
        go = (ImageView) view.findViewById(R.id.discover_dynamic_go);
        draw = view.findViewById(R.id.discover_draw);
        mSquareUnRead = view.findViewById(R.id.tv_square_new_message);

        view.findViewById(R.id.discover_recommend).setOnClickListener(this);
        view.findViewById(R.id.discover_square).setOnClickListener(this);
        view.findViewById(R.id.discover_rewardbillboard).setOnClickListener(this);
        view.findViewById(R.id.discover_playerbillboard).setOnClickListener(this);
        view.findViewById(R.id.discover_videobillboard).setOnClickListener(this);
        view.findViewById(R.id.discover_dynamic).setOnClickListener(this);
        view.findViewById(R.id.discover_activity).setOnClickListener(this);
        view.findViewById(R.id.discover_gift).setOnClickListener(this);
        draw.setOnClickListener(this);


    }

    private void loadData() {
        //抽奖状态获取接口
        DataManager.getSweepstakeStatus();

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
        if (shouldLoadData()){
            loadData();
            mLastLoadTime = System.currentTimeMillis();
        }
        mNum++;
        //该fragment处于最前台交互状态
        if (isVisibleToUser) {
            updateSquareUnRead();
            UmengAnalyticsHelper.onEvent(getActivity(), UmengAnalyticsHelper.MAIN, "进入发现页面次数");
            UmengAnalyticsHelper.onEvent(getActivity(), UmengAnalyticsHelper.DISCOVER, "进入发现页面次数");
        }
    }

    private void updateSquareUnRead(){
        if (isLogin()){
            long squareTime = System.currentTimeMillis();
            //玩家广场红点
            DataManager.squareDot(getMember_id(),squareTime);
        }
    }

    private boolean shouldLoadData(){
        if (mNum == 0 ){
            return false;
        }else {
            if (mLastLoadTime > 0){
                if (System.currentTimeMillis()-mLastLoadTime < 30000){
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.discover_draw:
                if (isLogin()) {
                    WebActivity.startWebActivityWithJS(getActivity(),
                            Constant.API_SWEEPSTAKE + "?mid=" + getMember_id(), Constant.JS_SWEEPSTAKE);
                } else {
                    WebActivity.startWebActivityWithJS(getActivity(),
                            Constant.API_SWEEPSTAKE, Constant.JS_SWEEPSTAKE);
                }
                UmengAnalyticsHelper.onEvent(getActivity(), UmengAnalyticsHelper.DISCOVER, "抽奖");
                break;

            case R.id.discover_activity:
                ActivityManager.startActivityListActivity(getActivity());
                break;

            case R.id.discover_gift:
                ActivityManager.startGiftListActivity(getActivity());
                break;

            case R.id.discover_recommend:
                ActivityManager.startRecommendActivity(getActivity());
                break;

            case R.id.discover_square:
                //更新发现页的红点提示
                mSquareUnRead.setVisibility(View.GONE);
                ActivityManager.startSquareActivity(getActivity(),null);
                break;

            case R.id.discover_rewardbillboard:
                ActivityManager.startMatchReswardBillboardActivity(getActivity());
                UmengAnalyticsHelper.onEvent(getActivity(), UmengAnalyticsHelper.DISCOVER, "奖金榜");
                break;

            case R.id.discover_playerbillboard:
                ActivityManager.startBillboardActivity(getActivity(), BillboardActivity.TYPE_PLAYER);
                UmengAnalyticsHelper.onEvent(getActivity(), UmengAnalyticsHelper.DISCOVER, "主播榜");
                break;

            case R.id.discover_videobillboard:
                ActivityManager.startBillboardActivity(getActivity(), BillboardActivity.TYPE_VIDEO);
                UmengAnalyticsHelper.onEvent(getActivity(), UmengAnalyticsHelper.DISCOVER, "视频榜");
                break;

            case R.id.discover_dynamic:
                startDynamicActivity();
                break;
        }
    }

    /**
     * 回调：抽奖状态
     */
    public void onEventMainThread(SweepstakeStatusEntity event) {
        if (event!=null && event.isResult()){
            if (event.getStatus().equals("1")) {//抽奖开启
                draw.setVisibility(View.VISIBLE);
            }
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
                go.setVisibility(View.GONE);
            } else {
                count.setVisibility(View.GONE);
                go.setVisibility(View.VISIBLE);
            }
        }
    }

    /**
     * 回调：玩家广场动态更新红点
     */
    public void onEventMainThread(SquareDotEntity event) {
        if (event != null && event.isResult() && mSquareUnRead != null) {
            //玩家广场动态有更新
            if (event.getData().isHasNew()) {
                mSquareUnRead.setVisibility(View.VISIBLE);
            } else {
                mSquareUnRead.setVisibility(View.GONE);
            }
        }
    }
}
