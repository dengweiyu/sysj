package com.li.videoapplication.data.model.response;

import com.li.videoapplication.data.model.entity.Gift;
import com.li.videoapplication.framework.BaseResponse2Entity;
import com.li.videoapplication.framework.BaseResponseEntity;

import java.util.List;


@SuppressWarnings("serial")
public class SearchPackage203Entity extends BaseResponseEntity {

    private Data data;

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public class Data extends BaseResponse2Entity {

        private List<Gift> list;

        public List<Gift> getList() {
            return list;
        }

        public void setList(List<Gift> list) {
            this.list = list;
        }
    }
	
}
