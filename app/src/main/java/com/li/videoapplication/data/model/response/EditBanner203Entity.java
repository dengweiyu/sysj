package com.li.videoapplication.data.model.response;

import com.li.videoapplication.data.model.entity.Banner;
import com.li.videoapplication.framework.BaseResponse2Entity;
import com.li.videoapplication.framework.BaseResponseEntity;

import java.util.List;


@SuppressWarnings("serial")
public class EditBanner203Entity extends BaseResponseEntity {

    private Data data;

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public class Data extends BaseResponse2Entity {

        private List<Banner> list;

        public List<Banner> getList() {
            return list;
        }

        public void setList(List<Banner> list) {
            this.list = list;
        }
    }
}
