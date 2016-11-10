package com.li.videoapplication.data.model.response;

import com.li.videoapplication.data.model.entity.Currency;
import com.li.videoapplication.framework.BaseResponseEntity;

import java.util.List;

public class MemberTaskEntity extends BaseResponseEntity {

    private String member_currency;
    private List<Currency> type_menu;
    private List<Currency> daily;
    private List<Currency> novice;
    private List<Currency> events;
    private List<Currency> honor;

    public List<Currency> getType_menu() {
        return type_menu;
    }

    public void setType_menu(List<Currency> type_menu) {
        this.type_menu = type_menu;
    }

    public String getMember_currency() {
        return member_currency;
    }

    public void setMember_currency(String member_currency) {
        this.member_currency = member_currency;
    }

    public List<Currency> getDaily() {
        return daily;
    }

    public void setDaily(List<Currency> daily) {
        this.daily = daily;
    }

    public List<Currency> getNovice() {
        return novice;
    }

    public void setNovice(List<Currency> novice) {
        this.novice = novice;
    }

    public List<Currency> getEvents() {
        return events;
    }

    public void setEvents(List<Currency> events) {
        this.events = events;
    }

    public List<Currency> getHonor() {
        return honor;
    }

    public void setHonor(List<Currency> honor) {
        this.honor = honor;
    }
}
