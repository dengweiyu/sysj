package com.li.videoapplication.data.model.response;

import com.li.videoapplication.framework.BaseResponse2Entity;
import com.li.videoapplication.framework.BaseResponseEntity;


@SuppressWarnings("serial")
public class IsRepeatEntity extends BaseResponseEntity {

    public Data data;

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public class Data extends BaseResponse2Entity {

        public boolean isRepeat;

        public String info;

        public boolean isRepeat() {
            return isRepeat;
        }

        public void setRepeat(boolean repeat) {
            isRepeat = repeat;
        }

        public String getInfo() {
            return info;
        }

        public void setInfo(String info) {
            this.info = info;
        }
    }
}
