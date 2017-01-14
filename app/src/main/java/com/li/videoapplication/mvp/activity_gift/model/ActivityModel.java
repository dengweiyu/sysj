package com.li.videoapplication.mvp.activity_gift.model;

import com.li.videoapplication.data.HttpManager;
import com.li.videoapplication.data.model.entity.Match;
import com.li.videoapplication.data.model.response.MyMatchListEntity;
import com.li.videoapplication.framework.BaseHttpResult;
import com.li.videoapplication.mvp.OnLoadDataListener;
import com.li.videoapplication.mvp.activity_gift.ActivityGiftContract.IActivityModel;

import rx.Observer;

/**
 * Model层: 活动
 */
public class ActivityModel implements IActivityModel {

    private static ActivityModel activityModel;

    public static synchronized IActivityModel getInstance() {
        if (activityModel == null) {
            activityModel = new ActivityModel();
        }
        return activityModel;
    }

    @Override
    public void getMyMatchList(String member_id, int page,final OnLoadDataListener<MyMatchListEntity> listener) {
        HttpManager.getInstance().getMyMatchList(member_id, page, new Observer<MyMatchListEntity>() {

            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
                listener.onFailure(e);
            }

            @Override
            public void onNext(MyMatchListEntity entity) {
                if (entity != null)
                    listener.onSuccess(entity);
            }
        });
    }

    @Override
    public void getMatchInfo(String match_id,final OnLoadDataListener<Match> listener) {
        HttpManager.getInstance().getMatchInfo(match_id, new Observer<Match>() {

            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
                listener.onFailure(e);
            }

            @Override
            public void onNext(Match entity) {
                if (entity != null)
                    listener.onSuccess(entity);
            }
        });
    }

    @Override
    public void competitionRecordClick(String competition_id, int c_record_status,final OnLoadDataListener<BaseHttpResult> listener) {
        HttpManager.getInstance().competitionRecordClick(competition_id, c_record_status, new Observer<BaseHttpResult>() {

            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
                listener.onFailure(e);
            }

            @Override
            public void onNext(BaseHttpResult entity) {
                if (entity != null)
                    listener.onSuccess(entity);
            }
        });
    }
}
