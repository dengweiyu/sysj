package com.li.videoapplication.mvp.home;

import com.li.videoapplication.data.model.response.ChangeGuessEntity;
import com.li.videoapplication.data.model.response.UnfinishedTaskEntity;
import com.li.videoapplication.data.model.entity.AdvertisementDto;
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

        //首页数据加载成功
        void refreshHomeData(HomeDto data);

        //首页每日任务加载成功
        void refreshUnFinishTaskView(UnfinishedTaskEntity data);

        //首页猜你喜欢加载成功
        void refreshChangeGuessView(ChangeGuessEntity data);

        //首页广告加载成功
        void refreshAdvertisementView(AdvertisementDto data);
    }

    /**
     * Presenter接口: 首页
     */
    public interface IHomePresenter {
        void setHomeView(IHomeView homeView);

        //调用 M层相应方法加载数据
        void loadHomeData(int page, boolean isLoad);

        void unfinishedTask(String member_id, boolean update);

        void changeGuess(String group_ids);

        void changeGuessSecond(String video_ids);

        void adImage208(int localtion_id, boolean isLoad);

        void adClick(long ad_id, int ad_click_state, String hardwarecode);
    }

    /**
     * M层 与 P层回调接口
     */
    public interface onloadHomeDataListener {
        void onLoadHomeSuccess(HomeDto data);

        void onLoadUnFinishTaskSuccess(UnfinishedTaskEntity data);

        void onLoadChangeGuessSuccess(ChangeGuessEntity data);

        void onLoadAdvertisementSuccess(AdvertisementDto data);

        void onLoadAdClickSuccess(BaseHttpResult data);

        void onFailure(Throwable e);
    }
}
