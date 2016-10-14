package com.li.videoapplication.data.model.response;

import com.li.videoapplication.data.model.entity.Match;
import com.li.videoapplication.framework.BaseResponse2Entity;
import com.li.videoapplication.framework.BaseResponseEntity;

import java.util.List;

@SuppressWarnings("serial")
public class EventsList204Entity extends BaseResponseEntity {
    private Data data;

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public class Data extends BaseResponse2Entity {

        private List<Match> list;

        public List<Match> getList() {
            return list;
        }

        public void setList(List<Match> list) {
            this.list = list;
        }
    }
}
