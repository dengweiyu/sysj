package com.li.videoapplication.data.model.response;

import com.li.videoapplication.data.model.entity.Member;
import com.li.videoapplication.framework.BaseResponse2Entity;
import com.li.videoapplication.framework.BaseResponseEntity;

import java.util.List;


@SuppressWarnings("serial")
public class EditGoldMember203Entity extends BaseResponseEntity {

    private Data data;

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public class Data extends BaseResponse2Entity {

        private String title;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        private List<Member> list;

        public List<Member> getList() {
            return list;
        }

        public void setList(List<Member> list) {
            this.list = list;
        }
    }
}
