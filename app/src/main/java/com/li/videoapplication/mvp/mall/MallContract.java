package com.li.videoapplication.mvp.mall;

import com.li.videoapplication.data.model.entity.Currency;
import com.li.videoapplication.mvp.OnLoadDataListener;

import java.util.List;

/**
 * 接口存放类：商城
 * 放置 Model View Presenter 的接口
 */
public class MallContract {

    /**
     * Model层接口: 商城
     */
    public interface IMallModel {
        //兑换记录
        void getOrderList(String member_id, final OnLoadDataListener<List<Currency>> listener);
        //中奖纪录
        void getMemberAward(String member_id, final OnLoadDataListener<List<Currency>> listener);
        //兑换记录详情
        void orderDetail(String member_id, String order_id, final OnLoadDataListener<Currency> listener);
        //抽奖记录详情
        void getMemberAwardDetail(String member_id, String id, final OnLoadDataListener<Currency> listener);
    }

    /**
     * View层接口: 商城--兑换记录
     */
    public interface IExchangeRecordView {
        //回调：商城兑换
        void refreshOrderListData(List<Currency> data);
        //回调：抽奖记录
        void refreshRewardListData(List<Currency> data);
    }

    /**
     * View层接口: 商城--兑换记录--详情
     */
    public interface IExchangeRecordDetailView {
        //回调：商城兑换详情
        void refreshOrderDetailData(Currency data);
        //回调：抽奖记录详情
        void refreshRewardDetailData(Currency data);
    }

    /**
     * Presenter接口: 商城
     */
    public interface IMallPresenter {
        void setExchangeRecordView(IExchangeRecordView exchangeRecordView);
        void setExchangeRecordDetailView(IExchangeRecordDetailView exchangeRecordDetailView);

        void getOrderList(String member_id);
        void getMemberAward(String member_id);
        void orderDetail(String member_id, String order_id);
        void getMemberAwardDetail(String member_id, String id);
    }
}
