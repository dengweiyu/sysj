package com.li.videoapplication.data.model.response;

import com.li.videoapplication.framework.BaseResponse2Entity;
import com.li.videoapplication.framework.BaseResponseEntity;


@SuppressWarnings("serial")
public class BaseInfoEntity extends BaseResponseEntity {

    public Data data;

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public class Data extends BaseResponse2Entity {

        public boolean hasBad;

        public boolean isHasBad() {
            return hasBad;
        }

        public void setHasBad(boolean hasBad) {
            this.hasBad = hasBad;
        }
    }
}
