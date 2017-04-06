package com.li.videoapplication.data.model.entity;

import com.li.videoapplication.framework.BaseEntity;

import java.util.List;


/**
 * Created by liuwei on 2017/4/5.
 */

public class PaymentList extends BaseEntity {


    /**
     * result : true
     * msg : 请求成功
     * data : [{"pay_type":"1","pay_name":"支付宝支付","note":"推荐安装微信5.0及以上版本使用","icon":"http://apps.ifeimo.com/Public/Sysj217/images/alipay.png"},{"pay_type":"2","pay_name":"微信支付","note":"推荐有支付宝账号的用户使用","icon":"http://apps.ifeimo.com/Public/Sysj217/images/wxpay.png"},{"pay_type":"3","pay_name":"QQ支付","note":"推荐开通QQ支付的用户使用","icon":"http://apps.ifeimo.com/Public/Sysj217/images/qqpay.png"}]
     */

    private boolean result;
    private String msg;
    private List<DataBean> data;

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * pay_type : 1
         * pay_name : 支付宝支付
         * note : 推荐安装微信5.0及以上版本使用
         * icon : http://apps.ifeimo.com/Public/Sysj217/images/alipay.png
         */

        private String pay_type;
        private String pay_name;
        private String note;
        private String icon;

        public String getPay_type() {
            return pay_type;
        }

        public void setPay_type(String pay_type) {
            this.pay_type = pay_type;
        }

        public String getPay_name() {
            return pay_name;
        }

        public void setPay_name(String pay_name) {
            this.pay_name = pay_name;
        }

        public String getNote() {
            return note;
        }

        public void setNote(String note) {
            this.note = note;
        }

        public String getIcon() {
            return icon;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }
    }
}
