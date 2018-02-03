package com.li.videoapplication.data.model.response;

import com.li.videoapplication.data.model.entity.GroupVideoReplyMessage;
import com.li.videoapplication.framework.BaseResponseEntity;

import java.util.List;

/**
 * Created by cx on 2018/1/29.
 */

public class GameGroupReplyMessageEntity extends BaseResponseEntity{

    private DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean{
        private int page_count;
        private List<GroupVideoReplyMessage> list;

        public int getPage_count() {
            return page_count;
        }

        public void setPage_count(int page_count) {
            this.page_count = page_count;
        }

        public List<GroupVideoReplyMessage> getList() {
            return list;
        }

        public void setList(List<GroupVideoReplyMessage> list) {
            this.list = list;
        }
    }
}
