package com.li.videoapplication.mvp.home;

import com.li.videoapplication.data.model.entity.Download;
import com.li.videoapplication.data.model.response.ChangeGuessEntity;
import com.li.videoapplication.data.model.response.UnfinishedTaskEntity;
import com.li.videoapplication.data.model.response.AdvertisementDto;
import com.li.videoapplication.data.model.entity.HomeDto;
import com.li.videoapplication.framework.BaseHttpResult;
import com.li.videoapplication.mvp.OnLoadDataListener;

import java.util.List;

/**
 * 接口存放类：首页
 * 放置 Model View Presenter 的接口，将大大减少类文件
 */
public class HomeContract {

    /**
     * Model层接口: 首页
     */
    public interface IHomeModel {

        void loadHomeData(int page, boolean isLoad, final onloadHomeDataListener listener);

        void unfinishedTask(String member_id, boolean update, final onloadHomeDataListener listener);

        void changeGuess(String group_ids, final onloadHomeDataListener listener);

        void changeGuessSecond(String video_ids, final onloadHomeDataListener listener);

        //启动图广告218
        void adverImage(int localtion_id, final OnLoadDataListener<AdvertisementDto> listener);

        //获取启动图中游戏下载详情
        void getDownloadOther(String game_id, final OnLoadDataListener<Download> listener);

        //通栏广告
        void adImage208(int localtion_id, boolean isLoad, final onloadHomeDataListener listener);

        void adClick(long ad_id, int ad_click_state, String hardwarecode, final onloadHomeDataListener listener);

        void adImageDownload(List<String> downloadList, final OnLoadDataListener<Boolean> listener);
    }

    /**
     * View层接口: 首页
     */
    public interface IHomeView {

        //关闭加载页
        void hideProgress();

        //回调：首页数据
        void refreshHomeData(HomeDto data);

        //回调：首页数据加载失败
        void refreshHomeDataFault(Throwable t);

        //回调：首页每日任务
        void refreshUnFinishTaskView(UnfinishedTaskEntity data);

        //回调：首页猜你喜欢
        void refreshChangeGuessView(ChangeGuessEntity data);

        //回调：首页广告
        void refreshAdvertisementView(AdvertisementDto data);
    }

    /**
     * Presenter接口: 首页
     */
    public interface IHomePresenter {
        void setHomeView(IHomeView homeView);

        void loadHomeData(int page, boolean isLoad);
        void unfinishedTask(String member_id, boolean update);

        void changeGuess(String group_ids);

        void changeGuessSecond(String video_ids);

        //启动图广告218
        void adverImage(int localtion_id);

        //通栏广告
        void adImage208(int localtion_id, boolean isLoad);

        void adClick(long ad_id, int ad_click_state, String hardwarecode);

    }

    /**
     * M层 与 P层回调接口
     */
    public interface onloadHomeDataListener {
        void onLoadHomeSuccess(HomeDto data);

        void onLoadHomeFault(Throwable t);

        void onLoadUnFinishTaskSuccess(UnfinishedTaskEntity data);

        void onLoadChangeGuessSuccess(ChangeGuessEntity data);

        void onLoadAdvertisementSuccess(AdvertisementDto data);

        void onLoadAdClickSuccess(BaseHttpResult data);

        void  onFailure(Throwable t);
    }
}
