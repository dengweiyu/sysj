package com.li.videoapplication.data.model.response;

import com.li.videoapplication.data.model.entity.Match;
import com.li.videoapplication.framework.BaseHttpResult;
import com.li.videoapplication.framework.BaseResponse2Entity;
import com.li.videoapplication.framework.BaseResponseEntity;


@SuppressWarnings("serial")
public class SignScheduleEntity extends BaseResponseEntity {
    private Data data;

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public class Data extends BaseResponse2Entity {

        private Match list;

        public Match getList() {
            return list;
        }

        public void setList(Match list) {
            this.list = list;
        }
    }

}
