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

    private List<PackageMemuBean> packageMemu;

    public List<PackageMemuBean> getPackageMemu() {
        return packageMemu;
    }

    public void setPackageMemu(List<PackageMemuBean> packageMemu) {
        this.packageMemu = packageMemu;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * level : 1
         * name :
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


    public static class PackageMemuBean {
            /**
             * key : 1
             * text : 一个月
             * discount : 1
             */
            private boolean isChoice;    //自定义字段  是否在列表中选中c
            private int key;
            private String text;
            private float discount;

        public boolean isChoice() {
            return isChoice;
        }

        public void setChoice(boolean choice) {
            isChoice = choice;
        }

        public int getKey() {
                return key;
            }

            public void setKey(int key) {
                this.key = key;
            }

            public String getText() {
                return text;
            }

            public void setText(String text) {
                this.text = text;
            }

            public float getDiscount() {
                return discount;
            }

            public void setDiscount(float discount) {
                this.discount = discount;
            }
        }
}
