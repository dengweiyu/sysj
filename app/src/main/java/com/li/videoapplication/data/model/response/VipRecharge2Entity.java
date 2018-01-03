package com.li.videoapplication.data.model.response;


import com.li.videoapplication.framework.BaseResponseEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by linhui on 2017/11/28.
 */
public class VipRecharge2Entity extends BaseResponseEntity {
    private List<Data> data = new ArrayList<>();
    private List<PackageMemuBean> packageMemu = new ArrayList<>();
    private List<VipAllInfo> vipAllInfo = new ArrayList<>();
    private String renewalDiscount;

    public String getRenewalDiscount() {
        return renewalDiscount;
    }

    public void setRenewalDiscount(String renewalDiscount) {
        this.renewalDiscount = renewalDiscount;
    }

    public List<VipAllInfo> getVipAllInfo() {
        return vipAllInfo;
    }

    public void setVipAllInfo(List<VipAllInfo> vipAllInfo) {
        this.vipAllInfo = vipAllInfo;
    }

    public void setData(List<Data> data) {
        this.data = data;
    }

    public List<Data> getData() {
        return data;
    }

    public List<PackageMemuBean> getPackageMemu() {
        return packageMemu;
    }

    public void setPackageMemu(List<PackageMemuBean> packageMemu) {
        this.packageMemu = packageMemu;
    }

    public static class PackageMemuBean {
        /**
         * key : 1
         * text : 一个月
         * discount : 1
         */

        private int key;
        private String text;
        private float discount;

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

    public static class Data {
        private int level;
        private String name;
        private String icon;
        private int price;
        private int color;
        private String selectedIcon;
        private String noSelectedIcon;
        private List<String> description;

        public String getSelectedIcon() {
            return selectedIcon;
        }

        public void setSelectedIcon(String selectedIcon) {
            this.selectedIcon = selectedIcon;
        }

        public String getNoSelectedIcon() {
            return noSelectedIcon;
        }

        public void setNoSelectedIcon(String noSelectedIcon) {
            this.noSelectedIcon = noSelectedIcon;
        }

        public List<String> getDescription() {
            return description;
        }

        public void setDescription(List<String> description) {
            this.description = description;
        }

        public int getColor() {
            return color;
        }

        public void setColor(int color) {
            this.color = color;
        }

        public void setLevel(int level) {
            this.level = level;
        }

        public int getLevel() {
            return level;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }

        public String getIcon() {
            return icon;
        }

        public void setPrice(int price) {
            this.price = price;
        }

        public int getPrice() {
            return price;
        }
    }


    public static class VipAllInfo {

        private String name;

        private String selectedCoin;

        private String notSelectedCoin;

        private int level;

        boolean isClick;

        public boolean isClick() {
            return isClick;
        }

        public void setClick(boolean click) {
            isClick = click;
        }

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

        public String getSelectedCoin() {
            return selectedCoin;
        }

        public void setSelectedCoin(String selectedCoin) {
            this.selectedCoin = selectedCoin;
        }

        public String getNotSelectedCoin() {
            return notSelectedCoin;
        }

        public void setNotSelectedCoin(String notSelectedCoin) {
            this.notSelectedCoin = notSelectedCoin;
        }
    }
}
