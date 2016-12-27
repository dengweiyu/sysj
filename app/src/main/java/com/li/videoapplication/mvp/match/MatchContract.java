package com.li.videoapplication.mvp.match;

import com.li.videoapplication.data.model.entity.Game;
import com.li.videoapplication.data.model.entity.Match;
import com.li.videoapplication.data.model.response.EventsList214Entity;
import com.li.videoapplication.data.model.response.EventsPKListEntity;
import com.li.videoapplication.data.model.response.MatchRewardBillboardEntity;
import com.li.videoapplication.data.model.response.SignScheduleEntity;
import com.li.videoapplication.data.model.response.ServiceNameEntity;
import com.li.videoapplication.framework.BaseHttpResult;
import com.li.videoapplication.mvp.OnLoadDataListener;

import java.util.List;

/**
 * 接口存放类：赛事
 * 放置 Model View Presenter 的接口，将大大减少类文件
 */
public class MatchContract {

    /**
     * Model层接口: 赛事
     */
    public interface IMatchModel {
        //赛事列表
        void getEventsList(int page, int format_type, String game_id, final OnLoadDataListener<EventsList214Entity> listener);
        //赛事列表游戏类型筛选
        void getGameCate(final OnLoadDataListener<List<Game>> listener);
        //我的赛事列表
        void getMyEventsList(int page,String member_id, final OnLoadDataListener<EventsList214Entity> listener);
        //赛事结果
        void getEventResult(String event_id, final OnLoadDataListener<MatchRewardBillboardEntity> listener);
        //圈子赛事列表
        void getGroupEventsList(int page, String game_id, final OnLoadDataListener<EventsList214Entity> listener);
        //赛事详情
        void getEventsInfo(String event_id, String member_id, final OnLoadDataListener<Match> listener);
        //赛事签到
        void signSchedule(String member_id, String schedule_id, final OnLoadDataListener<SignScheduleEntity> listener);
        //加入群聊
        void groupJoin(String member_id, String chatroom_group_id, final OnLoadDataListener<BaseHttpResult> listener);
        //客服名称
        void getServiceName(String member_id, final OnLoadDataListener<ServiceNameEntity> listener);
        //赛事赛程
        void getEventsSchedule(String event_id, final OnLoadDataListener<List<Match>> listener);
        //赛事赛程pk列表
        void getEventsPKList(String schedule_id, int page, String member_id,final OnLoadDataListener<EventsPKListEntity> listener);
    }

    /**
     * View层接口: 赛事
     */
    public interface IMatchListView {
        //关闭加载页
        void hideProgress();

        //数据加载成功
        void refreshMatchListData(EventsList214Entity data);
    }

    /**
     * View层接口: 我的赛事
     */
    public interface IMyMatchListView {
        //关闭加载页
        void hideProgress();

        //数据加载成功
        void refreshMyMatchListData(EventsList214Entity data);
    }

    /**
     * View层接口: 赛事筛选
     */
    public interface IMatchListFliterView {
        //数据加载成功
        void refreshGameCateData(List<Game> data);
    }

    /**
     * View层接口: 赛事结果
     */
    public interface IMatchResultView {
        //数据加载成功
        void refreshData(MatchRewardBillboardEntity data);
    }

    /**
     * View层接口: 圈子赛事列表
     */
    public interface IGroupMatchListView {
        void hideProgress();
        //数据加载成功
        void refreshGroupMatchListData(EventsList214Entity data);
    }

    /**
     * View层接口: 赛事详情
     */
    public interface IMatchDetailView {
        //赛事详情加载成功
        void refreshMatchDetailData(Match data);
        //赛事签到
        void refreshSignScheduleData(SignScheduleEntity data);
        //加入群聊
        void refreshGroupJoin(BaseHttpResult data);
        //客服名称
        void refreshServiceName(ServiceNameEntity data);
    }

    /**
     * View层接口: 赛程
     */
    public interface IMatchProcessView {
        void hideProgress();
        void refreshEventsScheduleData(List<Match> data);
        void refreshEventsPKListData(EventsPKListEntity data);
    }

    /**
     * Presenter接口: 赛事
     */
    public interface IMatchPresenter {
        void setMatchListView(IMatchListView matchListView);
        void setMatchListFliterView(IMatchListFliterView matchListFliterView);
        void setMyMatchListView(IMyMatchListView myMatchListView);
        void setMatchResultView(IMatchResultView matchResultView);
        void setGroupMatchListView(IGroupMatchListView groupMatchListView);
        void setMatchDetailView(IMatchDetailView matchDetailView);
        void setMatchProcessView(IMatchProcessView matchProcessView);


        void getEventsList(int page, int format_type, String game_id);
        void getGameCate();
        void getMyEventsList(int page, String member_id);
        void getEventResult(String event_id);
        void getGroupEventsList(int page, String game_id);
        void getEventsInfo(String event_id, String member_id);
        void signSchedule(String member_id, String schedule_id);
        void groupJoin(String member_id, String chatroom_group_id);
        void getServiceName(String member_id);
        void getEventsSchedule(String event_id);
        void getEventsPKList(String schedule_id, int page, String member_id);
    }
}
