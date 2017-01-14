package com.li.videoapplication.mvp.activity_gift;

import com.li.videoapplication.data.model.entity.Match;
import com.li.videoapplication.data.model.response.MyMatchListEntity;
import com.li.videoapplication.data.model.response.MyPackageEntity;
import com.li.videoapplication.framework.BaseHttpResult;
import com.li.videoapplication.mvp.OnLoadDataListener;

/**
 * 接口存放类：活动，礼包
 */
public class ActivityGiftContract {
    /**
     * Model层接口: 活动
     */
    public interface IActivityModel {
        //我的活动
        void getMyMatchList(String member_id, int page, final OnLoadDataListener<MyMatchListEntity> listener);
        //活动详情
        void getMatchInfo(String match_id, final OnLoadDataListener<Match> listener);
        //活动流水点击
        void competitionRecordClick(String competition_id, int c_record_status, final OnLoadDataListener<BaseHttpResult> listener);
    }

    /**
     * Model层接口: 礼包
     */
    public interface IGiftModel {
        //我的活动
        void getMyGiftList(String member_id, int page, final OnLoadDataListener<MyPackageEntity> listener);

    }

    /**
     * View层接口: 活动详情
     */
    public interface IActivityDetailView {
        void refreshActivityDetailData(Match data);
    }

    /**
     * View层接口: 我的活动
     */
    public interface IMyActivityView {
        void hideProgress();

        void refreshMyMatchListData(MyMatchListEntity data);
    }

    /**
     * View层接口: 我的礼包
     */
    public interface IMyGiftView {
        void hideProgress();

        void refreshMyGiftListData(MyPackageEntity data);
    }

    /**
     * Presenter接口: 活动
     */
    public interface IActivityPresenter {
        void setMyActivityView(IMyActivityView myActivityView);
        void setActivityDetailView(IActivityDetailView activityDetailView);

        void getMyMatchList(String member_id, int page);
        void getMatchInfo(String match_id);

        void competitionRecordClick(String competition_id, int c_record_status);
    }

    /**
     * Presenter接口: 礼包
     */
    public interface IGiftPresenter {
        void setMyGiftView(IMyGiftView myGiftView);

        void getMyGiftList(String member_id, int page);
    }
}
