package com.li.videoapplication.mvp.match.presenter;

import android.util.Log;

import com.li.videoapplication.data.model.entity.Game;
import com.li.videoapplication.data.model.entity.Match;
import com.li.videoapplication.data.model.event.MatchListFliterEvent;
import com.li.videoapplication.data.model.response.EventsList214Entity;
import com.li.videoapplication.data.model.response.EventsPKListEntity;
import com.li.videoapplication.data.model.response.GameCateEntity;
import com.li.videoapplication.data.model.response.MatchRecordEntity;
import com.li.videoapplication.data.model.response.MatchRewardBillboardEntity;
import com.li.videoapplication.data.model.response.ServiceNameEntity;
import com.li.videoapplication.data.model.response.SignScheduleEntity;
import com.li.videoapplication.framework.BaseHttpResult;
import com.li.videoapplication.mvp.OnLoadDataListener;
import com.li.videoapplication.mvp.match.MatchContract;
import com.li.videoapplication.mvp.match.MatchContract.IMatchRecordView;
import com.li.videoapplication.mvp.match.MatchContract.IMatchProcessView;
import com.li.videoapplication.mvp.match.MatchContract.IMatchDetailView;
import com.li.videoapplication.mvp.match.MatchContract.IGroupMatchListView;
import com.li.videoapplication.mvp.match.MatchContract.IMatchResultView;
import com.li.videoapplication.mvp.match.MatchContract.IMyMatchListView;
import com.li.videoapplication.mvp.match.MatchContract.IMatchListView;
import com.li.videoapplication.mvp.match.MatchContract.IMatchPresenter;
import com.li.videoapplication.mvp.match.MatchContract.IMatchModel;
import com.li.videoapplication.mvp.match.model.MatchModel;

import java.util.List;


/**
 * Presenter实现类: 赛事
 */
public class MatchPresenter implements IMatchPresenter {

    private static final String TAG = MatchPresenter.class.getSimpleName();

    private IMatchListView matchListView;
    private IMyMatchListView myMatchListView;
    private IMatchResultView matchResultView;
    private IGroupMatchListView groupMatchListView;
    private IMatchDetailView matchDetailView;
    private IMatchProcessView matchProcessView;
    private IMatchRecordView matchRecordView;

    private IMatchModel matchModel;
    private static MatchPresenter matchPresenter;

    private MatchPresenter() {
        matchModel = MatchModel.getInstance();
    }

    public static synchronized MatchPresenter getInstance() {
        if (matchPresenter == null) {
            matchPresenter = new MatchPresenter();
        }
        return matchPresenter;
    }

    //赛事列表view
    @Override
    public void setMatchListView(IMatchListView matchListView) {
        this.matchListView = matchListView;
    }

    @Override
    public void setMyMatchListView(IMyMatchListView myMatchListView) {
        this.myMatchListView = myMatchListView;
    }

    @Override
    public void setMatchResultView(IMatchResultView matchResultView) {
        this.matchResultView = matchResultView;
    }

    @Override
    public void setGroupMatchListView(IGroupMatchListView groupMatchListView) {
        this.groupMatchListView = groupMatchListView;
    }

    @Override
    public void setMatchDetailView(IMatchDetailView matchDetailView) {
        this.matchDetailView = matchDetailView;
    }

    @Override
    public void setMatchProcessView(IMatchProcessView matchProcessView) {
        this.matchProcessView = matchProcessView;
    }

    @Override
    public void setMatchRecordView(IMatchRecordView matchRecordView) {
        this.matchRecordView = matchRecordView;
    }


    @Override
    public void getEventsList(int page, String format_type, String game_id) {
        matchModel.getEventsList(page, format_type, game_id, new OnLoadDataListener<EventsList214Entity>() {
            @Override
            public void onSuccess(EventsList214Entity data) {
                matchListView.hideProgress();
                matchListView.refreshMatchListData(data);
            }

            @Override
            public void onFailure(Throwable e) {

            }
        });
    }

    @Override
    public void getGameCate() {
        matchModel.getGameCate(new OnLoadDataListener<GameCateEntity>() {
            @Override
            public void onSuccess(GameCateEntity data) {
                matchListView.refreshGameCateData(data);
            }

            @Override
            public void onFailure(Throwable e) {

            }
        });
    }

    @Override
    public void getMyEventsList(int page, String member_id) {
        matchModel.getMyEventsList(page, member_id, new OnLoadDataListener<EventsList214Entity>() {
            @Override
            public void onSuccess(EventsList214Entity data) {
                myMatchListView.hideProgress();
                myMatchListView.refreshMyMatchListData(data);
            }

            @Override
            public void onFailure(Throwable e) {

            }
        });
    }

    @Override
    public void getEventResult(String event_id) {
        matchModel.getEventResult(event_id, new OnLoadDataListener<MatchRewardBillboardEntity>() {
            @Override
            public void onSuccess(MatchRewardBillboardEntity data) {
                matchResultView.refreshData(data);
            }

            @Override
            public void onFailure(Throwable e) {

            }
        });
    }

    @Override
    public void getGroupEventsList(int page, String game_id) {
        matchModel.getGroupEventsList(page, game_id, new OnLoadDataListener<EventsList214Entity>() {
            @Override
            public void onSuccess(EventsList214Entity data) {
                groupMatchListView.hideProgress();
                groupMatchListView.refreshGroupMatchListData(data);
            }

            @Override
            public void onFailure(Throwable e) {

            }
        });
    }

    @Override
    public void getEventsInfo(String event_id, String member_id) {
        matchModel.getEventsInfo(event_id, member_id, new OnLoadDataListener<Match>() {
            @Override
            public void onSuccess(Match data) {
                matchDetailView.refreshMatchDetailData(data);
            }

            @Override
            public void onFailure(Throwable e) {

            }
        });
    }

    @Override
    public void signSchedule(String member_id, String schedule_id,String event_id) {
        matchModel.signSchedule(member_id, schedule_id,event_id, new OnLoadDataListener<SignScheduleEntity>() {
            @Override
            public void onSuccess(SignScheduleEntity data) {
                matchDetailView.refreshSignScheduleData(data);
            }

            @Override
            public void onFailure(Throwable e) {

            }
        });
    }

    @Override
    public void groupJoin(String member_id, String chatroom_group_id) {
        matchModel.groupJoin(member_id, chatroom_group_id, new OnLoadDataListener<BaseHttpResult>() {
            @Override
            public void onSuccess(BaseHttpResult data) {
                matchDetailView.refreshGroupJoin(data);
            }

            @Override
            public void onFailure(Throwable e) {

            }
        });
    }

    @Override
    public void getServiceName(String member_id) {
        matchModel.getServiceName(member_id, new OnLoadDataListener<ServiceNameEntity>() {
            @Override
            public void onSuccess(ServiceNameEntity data) {
                matchDetailView.refreshServiceName(data);
            }

            @Override
            public void onFailure(Throwable e) {

            }
        });
    }

    @Override
    public void getEventsSchedule(String event_id) {
        matchModel.getEventsSchedule(event_id, new OnLoadDataListener<List<Match>>() {
            @Override
            public void onSuccess(List<Match> data) {
                matchProcessView.refreshEventsScheduleData(data);
            }

            @Override
            public void onFailure(Throwable e) {

            }
        });
    }

    @Override
    public void getEventsPKList(String schedule_id, int page, String member_id) {
        matchModel.getEventsPKList(schedule_id, page, member_id, new OnLoadDataListener<EventsPKListEntity>() {
            @Override
            public void onSuccess(EventsPKListEntity data) {
                matchProcessView.hideProgress();
                matchProcessView.refreshEventsPKListData(data);
            }

            @Override
            public void onFailure(Throwable e) {

            }
        });
    }

    @Override
    public void getHistoricalRecord(String memberId, int page) {
        matchModel.getHistoricalRecord(memberId, page, new OnLoadDataListener<MatchRecordEntity>() {
            @Override
            public void onSuccess(MatchRecordEntity data) {
                matchRecordView.hideProgress();
                matchRecordView.refreshMatchRecordData(data);
            }

            @Override
            public void onFailure(Throwable e) {

            }
        });
    }

    @Override
    public void eventsRecordClick(String event_id, int e_record_status) {
        matchModel.eventsRecordClick(event_id, e_record_status, new OnLoadDataListener<BaseHttpResult>() {
            @Override
            public void onSuccess(BaseHttpResult data) {
                Log.d(TAG, "eventsRecordClick: "+data.getMsg());
            }

            @Override
            public void onFailure(Throwable e) {
                Log.d(TAG, "eventsRecordClick onFailure: ");
            }
        });
    }
}
