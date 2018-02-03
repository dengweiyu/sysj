package com.li.videoapplication.data.model.response;

import com.li.videoapplication.data.model.entity.MessageReplyComment;
import com.li.videoapplication.framework.BaseResponseEntity;

import java.util.List;

/**
 * Created by cx on 2018/1/29.
 */

public class MsgReplyCommentEntity extends BaseResponseEntity {

    private MsgReplyCommentEntity.DataBean AData;

    public DataBean getAData() {
        return AData;
    }

    public void setAData(DataBean AData) {
        this.AData = AData;
    }

    public static class DataBean{
        private MessageReplyComment parentData;
        private List<MessageReplyComment> childList;
        private int countNum;
        private int page_count;

        public MessageReplyComment getParentData() {
            return parentData;
        }

        public void setParentData(MessageReplyComment parentData) {
            this.parentData = parentData;
        }

        public List<MessageReplyComment> getChildList() {
            return childList;
        }

        public void setChildList(List<MessageReplyComment> childList) {
            this.childList = childList;
        }

        public int getCountNum() {
            return countNum;
        }

        public void setCountNum(int countNum) {
            this.countNum = countNum;
        }

        public int getPage_count() {
            return page_count;
        }

        public void setPage_count(int page_count) {
            this.page_count = page_count;
        }
    }
}
