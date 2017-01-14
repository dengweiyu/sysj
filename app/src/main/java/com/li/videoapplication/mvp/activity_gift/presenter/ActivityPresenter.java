package com.li.videoapplication.mvp.activity_gift.presenter;

import android.util.Log;

import com.li.videoapplication.data.model.entity.Match;
import com.li.videoapplication.data.model.response.MyMatchListEntity;
import com.li.videoapplication.framework.BaseHttpResult;
import com.li.videoapplication.mvp.OnLoadDataListener;
import com.li.videoapplication.mvp.activity_gift.ActivityGiftContract.IActivityDetailView;
import com.li.videoapplication.mvp.activity_gift.ActivityGiftContract.IActivityModel;
import com.li.videoapplication.mvp.activity_gift.ActivityGiftContract.IActivityPresenter;
import com.li.videoapplication.mvp.activity_gift.ActivityGiftContract.IMyActivityView;
import com.li.videoapplication.mvp.activity_gift.model.ActivityModel;

/**
 * Presenter实现类: 活动
 */
public class ActivityPresenter implements IActivityPresenter{
    private static final String TAG = ActivityPresenter.class.getSimpleName();

    private IActivityModel activityModel;
    private static ActivityPresenter activityPresenter;

    private IMyActivityView myActivityView;
    private IActivityDetailView activityDetailView;


    private ActivityPresenter() {
        activityModel = ActivityModel.getInstance();
    }

    public static synchronized ActivityPresenter getInstance() {
        if (activityPresenter == null) {
            activityPresenter = new ActivityPresenter();
        }
        return activityPresenter;
    }


    @Override
    public void setMyActivityView(IMyActivityView myActivityView) {
        this.myActivityView = myActivityView;
    }

    @Override
    public void setActivityDetailView(IActivityDetailView activityDetailView) {
        this.activityDetailView = activityDetailView;
    }

    @Override
    public void getMyMatchList(String member_id, int page) {
        activityModel.getMyMatchList(member_id, page, new OnLoadDataListener<MyMatchListEntity>() {
            @Override
            public void onSuccess(MyMatchListEntity data) {
                myActivityView.hideProgress();
                myActivityView.refreshMyMatchListData(data);
            }

            @Override
            public void onFailure(Throwable e) {

            }
        });
    }

    @Override
    public void getMatchInfo(String match_id) {
        activityModel.getMatchInfo(match_id, new OnLoadDataListener<Match>() {
            @Override
            public void onSuccess(Match data) {
                activityDetailView.refreshActivityDetailData(data);
            }

            @Override
            public void onFailure(Throwable e) {

            }
        });
    }

    @Override
    public void competitionRecordClick(String competition_id, int c_record_status) {
        activityModel.competitionRecordClick(competition_id, c_record_status, new OnLoadDataListener<BaseHttpResult>() {
            @Override
            public void onSuccess(BaseHttpResult data) {
                Log.d(TAG, "competitionRecordClick: "+data.getMsg());
            }

            @Override
            public void onFailure(Throwable e) {
                Log.d(TAG, "competitionRecordClick: onFailure");
            }
        });
    }
}
