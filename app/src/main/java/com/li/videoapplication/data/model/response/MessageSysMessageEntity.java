package com.li.videoapplication.data.model.response;

import java.util.List;

import com.li.videoapplication.data.model.entity.SysMessage;
import com.li.videoapplication.framework.BaseResponseEntity;
import com.li.videoapplication.framework.BaseResponse2Entity;


@SuppressWarnings("serial")
public class MessageSysMessageEntity extends BaseResponseEntity {

    private Data data;

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public class Data extends BaseResponse2Entity {

        private List<SysMessage> list;

        public List<SysMessage> getList() {
            return list;
        }

        public void setList(List<SysMessage> list) {
            this.list = list;
        }
    }

}
