package com.li.videoapplication.data.model.response;

import com.li.videoapplication.framework.BaseResponseEntity;


public class GetTeamMemberNumEntity extends BaseResponseEntity {

    private int teamMemberNum;

    public int getTeamMemberNum() {
        return teamMemberNum;
    }

    public void setTeamMemberNum(int teamMemberNum) {
        this.teamMemberNum = teamMemberNum;
    }
}
