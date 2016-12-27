package com.li.videoapplication.data.model.response;


import com.li.videoapplication.data.model.entity.Member;
import com.li.videoapplication.framework.BaseResponseEntity;

import java.util.List;

@SuppressWarnings("serial")
public class PlayerRankingCurrencyEntity extends BaseResponseEntity {

    private String myRanking;
    private List<Member> data;

    public String getMyRanking() {
        return myRanking;
    }

    public void setMyRanking(String myRanking) {
        this.myRanking = myRanking;
    }

    public List<Member> getData() {
        return data;
    }

    public void setData(List<Member> data) {
        this.data = data;
    }
}
