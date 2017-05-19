package com.li.videoapplication.mvp.mall;

import com.li.videoapplication.data.model.entity.Currency;
import com.li.videoapplication.data.model.entity.PaymentList;
import com.li.videoapplication.data.model.entity.TopUp;
import com.li.videoapplication.data.model.response.PaymentEntity;
import com.li.videoapplication.data.model.response.TopUpOptionEntity;
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

        //充值
        void getRechargeRule(final OnLoadDataListener<TopUpOptionEntity> listener);

        //支付
        void payment(int use,String member_id,int level, String currency_num, int pay_type, int ingress, final OnLoadDataListener<PaymentEntity> listener);

        //充值记录
        void getTopUpRecordList(String member_id, final OnLoadDataListener<List<TopUp>> listener);
        //获取支付分类选择
        void getPaymentList(String  target,final OnLoadDataListener<PaymentList> listener);
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
     * View层接口: 充值
     */
    public interface ITopUpView {
        //回调：商城兑换详情
        void refreshTopUpOptionData(TopUpOptionEntity data);
    }

    /**
     * View层接口: 充值记录
     */
    public interface ITopUpRecordView {
        //回调：商城兑换详情
        void refreshTopUpRecordData(List<TopUp> data);
    }

    /**
     * View层接口：支付类型列表
     */
    public interface IPaymentListView{

        void refreshPaymentList(PaymentList list);

        //回调：获取支付订单信息
        void refreshOrderInfoData(PaymentEntity entity);
        //订单获取失败
        void refreshFault();
    }

    /**
     * Presenter接口: 商城
     */
    public interface IMallPresenter {
        void setExchangeRecordView(IExchangeRecordView exchangeRecordView);

        void setExchangeRecordDetailView(IExchangeRecordDetailView exchangeRecordDetailView);

        void setTopUpView(ITopUpView topUpView);
        void setTopUpRecordView(ITopUpRecordView topUpRecordView);

        void getOrderList(String member_id);

        void getMemberAward(String member_id);

        void orderDetail(String member_id, String order_id);

        void getMemberAwardDetail(String member_id, String id);

        void getRechargeRule();

        void payment(int use,String member_id,int level, String currency_num, int pay_type, int ingress);

        void getTopUpRecordList(String member_id);

        void getPaymentList(String target);

        void setPaymentLisView(IPaymentListView paymentLisView);
    }
}
