package com.li.videoapplication.data.model.response;

import java.util.List;

import com.li.videoapplication.data.model.entity.Member;
import com.li.videoapplication.framework.BaseEntity;

@SuppressWarnings("serial")
public class PlayerRankingEntity extends BaseEntity{

    private String myRanking;

    private List<Member> list;

    public List<Member> getList() {
        return list;
    }

    public void setList(List<Member> list) {
        this.list = list;
    }

    public String getMyRanking() {
        return myRanking;
    }

    public void setMyRanking(String myRanking) {
        this.myRanking = myRanking;
    }

}
