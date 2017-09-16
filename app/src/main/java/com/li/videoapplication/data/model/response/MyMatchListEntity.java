package com.li.videoapplication.data.model.response;

import java.util.List;

import com.li.videoapplication.data.model.entity.Match;
import com.li.videoapplication.framework.BaseResponse2Entity;

@SuppressWarnings("serial")
public class MyMatchListEntity extends BaseResponse2Entity {

    private List<Match> list;

    public List<Match> getList() {
        return list;
    }

    public void setList(List<Match> list) {
        this.list = list;
    }

}
