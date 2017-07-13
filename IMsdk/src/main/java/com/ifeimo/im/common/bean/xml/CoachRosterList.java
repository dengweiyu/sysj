package com.ifeimo.im.common.bean.xml;

import java.util.List;

//import com.thoughtworks.xstream.annotations.XStreamAlias;
//import com.thoughtworks.xstream.annotations.XStreamImplicit;
//import com.thoughtworks.xstream.annotations.XStreamOmitField;
//
import java.util.List;
//
///**
// * Created by lpds on 2017/6/22.
// */
//@XStreamAlias("roster")
public class CoachRosterList {

//    @XStreamImplicit(itemFieldName="rosterItem")
    List<RosterItem> rosterItemList;

    public List<RosterItem> getRosterItemList() {
        return rosterItemList;
    }

    public void setRosterItemList(List<RosterItem> rosterItemList) {
        this.rosterItemList = rosterItemList;
    }

    public class RosterItem{
        private String jid;
        private String subscriptionType;
        private String nickname;
//        @XStreamOmitField
        private String groups;


        public String getJid() {
            return jid;
        }

        public void setJid(String jid) {
            this.jid = jid;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public String getSubscriptionType() {
            return subscriptionType;
        }

        public void setSubscriptionType(String subscriptionType) {
            this.subscriptionType = subscriptionType;
        }
    }



}
