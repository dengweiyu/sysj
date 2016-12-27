package com.li.videoapplication.mvp.match.model;

import com.li.videoapplication.data.HttpManager;
import com.li.videoapplication.data.model.entity.Game;
import com.li.videoapplication.data.model.entity.Match;
import com.li.videoapplication.data.model.event.MatchListFliterEvent;
import com.li.videoapplication.data.model.response.EventsList214Entity;
import com.li.videoapplication.data.model.response.EventsPKListEntity;
import com.li.videoapplication.data.model.response.MatchRewardBillboardEntity;
import com.li.videoapplication.data.model.response.ServiceNameEntity;
import com.li.videoapplication.data.model.response.SignScheduleEntity;
import com.li.videoapplication.framework.BaseHttpResult;
import com.li.videoapplication.framework.RxBus;
import com.li.videoapplication.mvp.OnLoadDataListener;
import com.li.videoapplication.mvp.match.MatchContract.IMatchModel;

import java.util.List;

import rx.Observer;
import rx.Subscription;
import rx.functions.Action1;

/**
 * Model层: 赛事
 */
public class MatchModel implements IMatchModel {

    private static MatchModel matchModel;

    public static synchronized IMatchModel getInstance() {
        if (matchModel == null) {
            matchModel = new MatchModel();
        }
        return matchModel;
    }

    @Override
    public void getEventsList(int page, int format_type, String game_id, final OnLoadDataListener<EventsList214Entity> listener) {
        HttpManager.getInstance().getEventsList(page, format_type, game_id, new Observer<EventsList214Entity>() {

            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
                listener.onFailure(e);
            }

            @Override
            public void onNext(EventsList214Entity entity) {
                if (entity != null)
                    listener.onSuccess(entity);
            }
        });
    }

    @Override
    public void getGameCate(final OnLoadDataListener<List<Game>> listener) {
        HttpManager.getInstance().getGameCate(new Observer<List<Game>>() {

            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
                listener.onFailure(e);
            }

            @Override
            public void onNext(List<Game> entity) {
                if (entity != null)
                    listener.onSuccess(entity);
            }
        });
    }

    @Override
    public void getMyEventsList(int page, String member_id, final OnLoadDataListener<EventsList214Entity> listener) {
        HttpManager.getInstance().getMyEventsList(page, member_id, new Observer<EventsList214Entity>() {

            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
                listener.onFailure(e);
            }

            @Override
            public void onNext(EventsList214Entity entity) {
                if (entity != null)
                    listener.onSuccess(entity);
            }
        });
    }

    @Override
    public void getEventResult(String event_id, final OnLoadDataListener<MatchRewardBillboardEntity> listener) {
        HttpManager.getInstance().getEventResult(event_id, new Observer<MatchRewardBillboardEntity>() {

            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
                listener.onFailure(e);
            }

            @Override
            public void onNext(MatchRewardBillboardEntity entity) {
                if (entity != null)
                    listener.onSuccess(entity);
            }
        });
    }

    @Override
    public void getGroupEventsList(int page, String game_id, final OnLoadDataListener<EventsList214Entity> listener) {
        HttpManager.getInstance().getGroupEventsList(page, game_id, new Observer<EventsList214Entity>() {

            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
                listener.onFailure(e);
            }

            @Override
            public void onNext(EventsList214Entity entity) {
                if (entity != null)
                    listener.onSuccess(entity);
            }
        });
    }

    @Override
    public void getEventsInfo(String event_id, String member_id, final OnLoadDataListener<Match> listener) {
        HttpManager.getInstance().getEventsInfo(event_id, member_id, new Observer<Match>() {

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
    public void signSchedule(String member_id, String schedule_id, final OnLoadDataListener<SignScheduleEntity> listener) {
        HttpManager.getInstance().signSchedule(member_id, schedule_id, new Observer<SignScheduleEntity>() {

            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
                listener.onFailure(e);
            }

            @Override
            public void onNext(SignScheduleEntity entity) {
                if (entity != null)
                    listener.onSuccess(entity);
            }
        });
    }

    @Override
    public void groupJoin(String member_id, String chatroom_group_id, final OnLoadDataListener<BaseHttpResult> listener) {
        HttpManager.getInstance().groupJoin(member_id, chatroom_group_id, new Observer<BaseHttpResult>() {

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

    @Override
    public void getServiceName(String member_id, final OnLoadDataListener<ServiceNameEntity> listener) {
        HttpManager.getInstance().getServiceName(member_id, new Observer<ServiceNameEntity>() {

            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
                listener.onFailure(e);
            }

            @Override
            public void onNext(ServiceNameEntity entity) {
                if (entity != null)
                    listener.onSuccess(entity);
            }
        });
    }

    @Override
    public void getEventsSchedule(String event_id, final OnLoadDataListener<List<Match>> listener) {
        HttpManager.getInstance().getEventsSchedule(event_id, new Observer<List<Match>>() {

            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
                listener.onFailure(e);
            }

            @Override
            public void onNext(List<Match> entity) {
                if (entity != null)
                    listener.onSuccess(entity);
            }
        });
    }

    @Override
    public void getEventsPKList(String schedule_id, int page, String member_id, final OnLoadDataListener<EventsPKListEntity> listener) {
        HttpManager.getInstance().getEventsPKList(schedule_id, page, member_id, new Observer<EventsPKListEntity>() {

            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
                listener.onFailure(e);
            }

            @Override
            public void onNext(EventsPKListEntity entity) {
                if (entity != null)
                    listener.onSuccess(entity);
            }
        });
    }
}
