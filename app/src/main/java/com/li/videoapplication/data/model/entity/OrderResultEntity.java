package com.li.videoapplication.data.model.entity;

import com.li.videoapplication.framework.BaseEntity;

import java.util.List;

/**
 * 订单结果
 */

public class OrderResultEntity extends BaseEntity {

    private List<DataBean> data;

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static final class DataBean{
        private int index;
        private int result;

        public DataBean(int index, int result) {
            this.index = index;
            this.result = result;
        }

        public int getIndex() {
            return index;
        }

        public void setIndex(int index) {
            this.index = index;
        }

        public int getResult() {
            return result;
        }

        public void setResult(int result) {
            this.result = result;
        }
    }
}
