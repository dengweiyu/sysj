package com.li.videoapplication.data.model.event;

import com.li.videoapplication.framework.BaseEntity;


public class MatchListFliterEvent extends BaseEntity {

    private String gameIds;
    private String gameNames;
    private int match_type;
    private String match_type_names;

    public MatchListFliterEvent(String gameIds, String gameNames, int match_type, String match_type_names) {
        this.gameIds = gameIds;
        this.gameNames = gameNames;
        this.match_type = match_type;
        this.match_type_names = match_type_names;
    }

    public String getGameNames() {
        return gameNames;
    }

    public void setGameNames(String gameNames) {
        this.gameNames = gameNames;
    }

    public String getMatch_type_names() {
        return match_type_names;
    }

    public void setMatch_type_names(String match_type_names) {
        this.match_type_names = match_type_names;
    }

    public int getMatch_type() {
        return match_type;
    }

    public void setMatch_type(int match_type) {
        this.match_type = match_type;
    }

    public String getGameIds() {
        return gameIds;
    }

    public void setGameIds(String gameIds) {
        this.gameIds = gameIds;
    }
}
