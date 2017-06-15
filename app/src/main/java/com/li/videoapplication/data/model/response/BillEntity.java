package com.li.videoapplication.data.model.response;

import com.chad.library.adapter.base.entity.SectionEntity;
import com.li.videoapplication.framework.BaseResponseEntity;

import java.util.List;


/**
 * 账单实体
 */

public class BillEntity extends BaseResponseEntity {

    /**
     * data : {"billsHead":"飞魔豆账单","billDatas":[{"title":"本月","list":[{"num":"2","description":"观看视频","amount":"652572","operation":"1","time":"1496721216","title":"完成观看视频奖励","ico":"http://apps.ifeimo.com/Public/Uploads/CurrencyTask/Flag/58a3b5f8a27a0.png","member_name":"","video_name":""},{"num":"20","description":"每日登陆","amount":"652570","operation":"1","time":"1496716407","title":"完成每日登陆奖励","ico":"http://apps.ifeimo.com/Public/Uploads/CurrencyTask/Flag/58a122bbc25f9.png","member_name":"","video_name":""},{"num":"20","description":"每日登陆","amount":"652550","operation":"1","time":"1496642253","title":"完成每日登陆奖励","ico":"http://apps.ifeimo.com/Public/Uploads/CurrencyTask/Flag/58a122bbc25f9.png","member_name":"","video_name":""}]},{"title":"05月","list":[{"num":"2","description":"观看视频","amount":"652530","operation":"1","time":"1495984511","title":"完成观看视频奖励","ico":"http://apps.ifeimo.com/Public/Uploads/CurrencyTask/Flag/58a3b5f8a27a0.png","member_name":"","video_name":""},{"num":"20","description":"每日登陆","amount":"652528","operation":"1","time":"1495961710","title":"完成每日登陆奖励","ico":"http://apps.ifeimo.com/Public/Uploads/CurrencyTask/Flag/58a122bbc25f9.png","member_name":"","video_name":""},{"num":"20","description":"每日登陆","amount":"652508","operation":"1","time":"1495865165","title":"完成每日登陆奖励","ico":"http://apps.ifeimo.com/Public/Uploads/CurrencyTask/Flag/58a122bbc25f9.png","member_name":"","video_name":""},{"num":"20","description":"每日登陆","amount":"652488","operation":"1","time":"1495793445","title":"完成每日登陆奖励","ico":"http://apps.ifeimo.com/Public/Uploads/CurrencyTask/Flag/58a122bbc25f9.png","member_name":"","video_name":""},{"num":"20","description":"每日登陆","amount":"652468","operation":"1","time":"1495281231","title":"完成每日登陆奖励","ico":"http://apps.ifeimo.com/Public/Uploads/CurrencyTask/Flag/58a122bbc25f9.png","member_name":"","video_name":""},{"num":"20","description":"分享至手游视界 广场","amount":"652448","operation":"1","time":"1495181545","title":"完成分享至手游视界 广场奖励","ico":"http://apps.ifeimo.com/Public/Uploads/CurrencyTask/Flag/58d0e3879bf26.png","member_name":"","video_name":""},{"num":"2","description":"观看视频","amount":"652428","operation":"1","time":"1495180072","title":"完成观看视频奖励","ico":"http://apps.ifeimo.com/Public/Uploads/CurrencyTask/Flag/58a3b5f8a27a0.png","member_name":"","video_name":""},{"num":"20","description":"分享至手游视界 广场","amount":"652426","operation":"1","time":"1495178382","title":"完成分享至手游视界 广场奖励","ico":"http://apps.ifeimo.com/Public/Uploads/CurrencyTask/Flag/58d0e3879bf26.png","member_name":"","video_name":""},{"num":"20","description":"每日登陆","amount":"652406","operation":"1","time":"1495178353","title":"完成每日登陆奖励","ico":"http://apps.ifeimo.com/Public/Uploads/CurrencyTask/Flag/58a122bbc25f9.png","member_name":"","video_name":""},{"num":"10","description":"上传手游视频","amount":"652386","operation":"1","time":"1495178086","title":"完成上传手游视频奖励","ico":"http://apps.ifeimo.com/Public/Uploads/CurrencyTask/Flag/5811b27d7fbd1.png","member_name":"","video_name":""},{"num":"20","description":"分享至手游视界 广场","amount":"652376","operation":"1","time":"1495178073","title":"完成分享至手游视界 广场奖励","ico":"http://apps.ifeimo.com/Public/Uploads/CurrencyTask/Flag/58d0e3879bf26.png","member_name":"","video_name":""},{"num":"2","description":"观看视频","amount":"652356","operation":"1","time":"1494827075","title":"完成观看视频奖励","ico":"http://apps.ifeimo.com/Public/Uploads/CurrencyTask/Flag/58a3b5f8a27a0.png","member_name":"","video_name":""},{"num":"2","description":"观看视频","amount":"652354","operation":"1","time":"1494826755","title":"完成观看视频奖励","ico":"http://apps.ifeimo.com/Public/Uploads/CurrencyTask/Flag/58a3b5f8a27a0.png","member_name":"","video_name":""},{"num":"2","description":"观看视频","amount":"652352","operation":"1","time":"1494821045","title":"完成观看视频奖励","ico":"http://apps.ifeimo.com/Public/Uploads/CurrencyTask/Flag/58a3b5f8a27a0.png","member_name":"","video_name":""},{"num":"10","description":"每日登陆","amount":"652350","operation":"1","time":"1494820752","title":"完成每日登陆奖励","ico":"http://apps.ifeimo.com/Public/Uploads/CurrencyTask/Flag/58a122bbc25f9.png","member_name":"","video_name":""},{"num":"10","description":"每日登陆","amount":"652340","operation":"1","time":"1494309873","title":"完成每日登陆奖励","ico":"http://apps.ifeimo.com/Public/Uploads/CurrencyTask/Flag/58a122bbc25f9.png","member_name":"","video_name":""},{"num":"100","description":"用户充值","amount":"652330","operation":"1","time":"1493979399","title":"充值飞磨豆","ico":"http://apps.ifeimo.com/Public/Uploads/CurrencyRecharge/recharge.png","member_name":"","video_name":""},{"num":"100","description":"用户充值","amount":"652230","operation":"1","time":"1493975622","title":"充值飞磨豆","ico":"http://apps.ifeimo.com/Public/Uploads/CurrencyRecharge/recharge.png","member_name":"","video_name":""},{"num":"100","description":"用户充值","amount":"652130","operation":"1","time":"1493975444","title":"充值飞磨豆","ico":"http://apps.ifeimo.com/Public/Uploads/CurrencyRecharge/recharge.png","member_name":"","video_name":""},{"num":"10","description":"用户充值","amount":"652030","operation":"1","time":"1493963723","title":"充值飞磨豆","ico":"http://apps.ifeimo.com/Public/Uploads/CurrencyRecharge/recharge.png","member_name":"","video_name":""}]}]}
     */

    private DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }
    //封装分组数据的实体类
    public static class SectionBill extends SectionEntity<DataBean.BillDatasBean.ListBean>{
        public SectionBill(boolean isHeader, String header) {
            super(isHeader, header);
        }

        public SectionBill(DataBean.BillDatasBean.ListBean listBean) {
            super(listBean);
        }
    }



    public static class DataBean {
        /**
         * billsHead : 飞魔豆账单
         * billDatas : [{"title":"本月","list":[{"num":"2","description":"观看视频","amount":"652572","operation":"1","time":"1496721216","title":"完成观看视频奖励","ico":"http://apps.ifeimo.com/Public/Uploads/CurrencyTask/Flag/58a3b5f8a27a0.png","member_name":"","video_name":""},{"num":"20","description":"每日登陆","amount":"652570","operation":"1","time":"1496716407","title":"完成每日登陆奖励","ico":"http://apps.ifeimo.com/Public/Uploads/CurrencyTask/Flag/58a122bbc25f9.png","member_name":"","video_name":""},{"num":"20","description":"每日登陆","amount":"652550","operation":"1","time":"1496642253","title":"完成每日登陆奖励","ico":"http://apps.ifeimo.com/Public/Uploads/CurrencyTask/Flag/58a122bbc25f9.png","member_name":"","video_name":""}]},{"title":"05月","list":[{"num":"2","description":"观看视频","amount":"652530","operation":"1","time":"1495984511","title":"完成观看视频奖励","ico":"http://apps.ifeimo.com/Public/Uploads/CurrencyTask/Flag/58a3b5f8a27a0.png","member_name":"","video_name":""},{"num":"20","description":"每日登陆","amount":"652528","operation":"1","time":"1495961710","title":"完成每日登陆奖励","ico":"http://apps.ifeimo.com/Public/Uploads/CurrencyTask/Flag/58a122bbc25f9.png","member_name":"","video_name":""},{"num":"20","description":"每日登陆","amount":"652508","operation":"1","time":"1495865165","title":"完成每日登陆奖励","ico":"http://apps.ifeimo.com/Public/Uploads/CurrencyTask/Flag/58a122bbc25f9.png","member_name":"","video_name":""},{"num":"20","description":"每日登陆","amount":"652488","operation":"1","time":"1495793445","title":"完成每日登陆奖励","ico":"http://apps.ifeimo.com/Public/Uploads/CurrencyTask/Flag/58a122bbc25f9.png","member_name":"","video_name":""},{"num":"20","description":"每日登陆","amount":"652468","operation":"1","time":"1495281231","title":"完成每日登陆奖励","ico":"http://apps.ifeimo.com/Public/Uploads/CurrencyTask/Flag/58a122bbc25f9.png","member_name":"","video_name":""},{"num":"20","description":"分享至手游视界 广场","amount":"652448","operation":"1","time":"1495181545","title":"完成分享至手游视界 广场奖励","ico":"http://apps.ifeimo.com/Public/Uploads/CurrencyTask/Flag/58d0e3879bf26.png","member_name":"","video_name":""},{"num":"2","description":"观看视频","amount":"652428","operation":"1","time":"1495180072","title":"完成观看视频奖励","ico":"http://apps.ifeimo.com/Public/Uploads/CurrencyTask/Flag/58a3b5f8a27a0.png","member_name":"","video_name":""},{"num":"20","description":"分享至手游视界 广场","amount":"652426","operation":"1","time":"1495178382","title":"完成分享至手游视界 广场奖励","ico":"http://apps.ifeimo.com/Public/Uploads/CurrencyTask/Flag/58d0e3879bf26.png","member_name":"","video_name":""},{"num":"20","description":"每日登陆","amount":"652406","operation":"1","time":"1495178353","title":"完成每日登陆奖励","ico":"http://apps.ifeimo.com/Public/Uploads/CurrencyTask/Flag/58a122bbc25f9.png","member_name":"","video_name":""},{"num":"10","description":"上传手游视频","amount":"652386","operation":"1","time":"1495178086","title":"完成上传手游视频奖励","ico":"http://apps.ifeimo.com/Public/Uploads/CurrencyTask/Flag/5811b27d7fbd1.png","member_name":"","video_name":""},{"num":"20","description":"分享至手游视界 广场","amount":"652376","operation":"1","time":"1495178073","title":"完成分享至手游视界 广场奖励","ico":"http://apps.ifeimo.com/Public/Uploads/CurrencyTask/Flag/58d0e3879bf26.png","member_name":"","video_name":""},{"num":"2","description":"观看视频","amount":"652356","operation":"1","time":"1494827075","title":"完成观看视频奖励","ico":"http://apps.ifeimo.com/Public/Uploads/CurrencyTask/Flag/58a3b5f8a27a0.png","member_name":"","video_name":""},{"num":"2","description":"观看视频","amount":"652354","operation":"1","time":"1494826755","title":"完成观看视频奖励","ico":"http://apps.ifeimo.com/Public/Uploads/CurrencyTask/Flag/58a3b5f8a27a0.png","member_name":"","video_name":""},{"num":"2","description":"观看视频","amount":"652352","operation":"1","time":"1494821045","title":"完成观看视频奖励","ico":"http://apps.ifeimo.com/Public/Uploads/CurrencyTask/Flag/58a3b5f8a27a0.png","member_name":"","video_name":""},{"num":"10","description":"每日登陆","amount":"652350","operation":"1","time":"1494820752","title":"完成每日登陆奖励","ico":"http://apps.ifeimo.com/Public/Uploads/CurrencyTask/Flag/58a122bbc25f9.png","member_name":"","video_name":""},{"num":"10","description":"每日登陆","amount":"652340","operation":"1","time":"1494309873","title":"完成每日登陆奖励","ico":"http://apps.ifeimo.com/Public/Uploads/CurrencyTask/Flag/58a122bbc25f9.png","member_name":"","video_name":""},{"num":"100","description":"用户充值","amount":"652330","operation":"1","time":"1493979399","title":"充值飞磨豆","ico":"http://apps.ifeimo.com/Public/Uploads/CurrencyRecharge/recharge.png","member_name":"","video_name":""},{"num":"100","description":"用户充值","amount":"652230","operation":"1","time":"1493975622","title":"充值飞磨豆","ico":"http://apps.ifeimo.com/Public/Uploads/CurrencyRecharge/recharge.png","member_name":"","video_name":""},{"num":"100","description":"用户充值","amount":"652130","operation":"1","time":"1493975444","title":"充值飞磨豆","ico":"http://apps.ifeimo.com/Public/Uploads/CurrencyRecharge/recharge.png","member_name":"","video_name":""},{"num":"10","description":"用户充值","amount":"652030","operation":"1","time":"1493963723","title":"充值飞磨豆","ico":"http://apps.ifeimo.com/Public/Uploads/CurrencyRecharge/recharge.png","member_name":"","video_name":""}]}]
         */

        private String billsHead;
        private List<BillDatasBean> billDatas;

        public String getBillsHead() {
            return billsHead;
        }

        public void setBillsHead(String billsHead) {
            this.billsHead = billsHead;
        }

        public List<BillDatasBean> getBillDatas() {
            return billDatas;
        }

        public void setBillDatas(List<BillDatasBean> billDatas) {
            this.billDatas = billDatas;
        }

        public static class BillDatasBean {
            /**
             * title : 本月
             * list : [{"num":"2","description":"观看视频","amount":"652572","operation":"1","time":"1496721216","title":"完成观看视频奖励","ico":"http://apps.ifeimo.com/Public/Uploads/CurrencyTask/Flag/58a3b5f8a27a0.png","member_name":"","video_name":""},{"num":"20","description":"每日登陆","amount":"652570","operation":"1","time":"1496716407","title":"完成每日登陆奖励","ico":"http://apps.ifeimo.com/Public/Uploads/CurrencyTask/Flag/58a122bbc25f9.png","member_name":"","video_name":""},{"num":"20","description":"每日登陆","amount":"652550","operation":"1","time":"1496642253","title":"完成每日登陆奖励","ico":"http://apps.ifeimo.com/Public/Uploads/CurrencyTask/Flag/58a122bbc25f9.png","member_name":"","video_name":""}]
             */

            private String title;
            private List<ListBean> list;

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public List<ListBean> getList() {
                return list;
            }

            public void setList(List<ListBean> list) {
                this.list = list;
            }

            public static class ListBean {
                /**
                 * num : 2
                 * description : 观看视频
                 * amount : 652572
                 * operation : 1
                 * time : 1496721216
                 * title : 完成观看视频奖励
                 * ico : http://apps.ifeimo.com/Public/Uploads/CurrencyTask/Flag/58a3b5f8a27a0.png
                 * member_name :
                 * video_name :
                 */

                private String num;
                private String description;
                private String amount;
                private String operation;
                private String time;
                private String title;
                private String ico;
                private String member_name;
                private String video_name;

                public String getNum() {
                    return num;
                }

                public void setNum(String num) {
                    this.num = num;
                }

                public String getDescription() {
                    return description;
                }

                public void setDescription(String description) {
                    this.description = description;
                }

                public String getAmount() {
                    return amount;
                }

                public void setAmount(String amount) {
                    this.amount = amount;
                }

                public String getOperation() {
                    return operation;
                }

                public void setOperation(String operation) {
                    this.operation = operation;
                }

                public String getTime() {
                    return time;
                }

                public void setTime(String time) {
                    this.time = time;
                }

                public String getTitle() {
                    return title;
                }

                public void setTitle(String title) {
                    this.title = title;
                }

                public String getIco() {
                    return ico;
                }

                public void setIco(String ico) {
                    this.ico = ico;
                }

                public String getMember_name() {
                    return member_name;
                }

                public void setMember_name(String member_name) {
                    this.member_name = member_name;
                }

                public String getVideo_name() {
                    return video_name;
                }

                public void setVideo_name(String video_name) {
                    this.video_name = video_name;
                }
            }
        }
    }
}
