package com.li.videoapplication.data.model.response;

import com.li.videoapplication.framework.BaseResponseEntity;

import java.util.List;

/**
 * 会员中心 开通VIP
 */

public class VipRechargeEntity extends BaseResponseEntity {


    private List<DataBean> data;

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * level : 1
         * name : VIP1
         * icon :
         * price : 1
         * description : ["录制时长60分钟"]
         */

        private int level;
        private String name;
        private String icon;
        private float price;
        private List<String> description;

        private boolean isChoice;    //自定义字段  是否在列表中选中c

        public int getLevel() {
            return level;
        }

        public void setLevel(int level) {
            this.level = level;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getIcon() {
            return icon;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }

        public float getPrice() {
            return price;
        }

        public void setPrice(float price) {
            this.price = price;
        }

        public List<String> getDescription() {
            return description;
        }

        public void setDescription(List<String> description) {
            this.description = description;
        }

        public boolean isChoice() {
            return isChoice;
        }

        public void setChoice(boolean choice) {
            isChoice = choice;
        }
    }
}
