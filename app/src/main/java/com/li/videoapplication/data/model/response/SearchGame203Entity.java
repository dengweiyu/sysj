package com.li.videoapplication.data.model.response;

import com.li.videoapplication.data.model.entity.Game;
import com.li.videoapplication.framework.BaseResponse2Entity;
import com.li.videoapplication.framework.BaseResponseEntity;

import java.util.List;


@SuppressWarnings("serial")
public class SearchGame203Entity extends BaseResponseEntity {

    private Data data;

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public class Data extends BaseResponse2Entity {

        private List<Game> list;

        public List<Game> getList() {
            return list;
        }

        public void setList(List<Game> list) {
            this.list = list;
        }
    }
	
}
