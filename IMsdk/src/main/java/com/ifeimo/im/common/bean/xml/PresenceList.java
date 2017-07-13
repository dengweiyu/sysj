package com.ifeimo.im.common.bean.xml;

//import android.util.Log;
//
//import com.google.gson.annotations.SerializedName;
//import com.ifeimo.im.common.util.StringUtil;
//import com.thoughtworks.xstream.annotations.XStreamAlias;
//import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
//import com.thoughtworks.xstream.annotations.XStreamImplicit;
//import com.thoughtworks.xstream.annotations.XStreamOmitField;
//
//import org.jivesoftware.smack.packet.Presence;

import org.jivesoftware.smack.packet.Presence;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lpds on 2017/6/22.
 */


//@XStreamAlias("root")
public class PresenceList {
//    @XStreamImplicit(itemFieldName = "presence")
    List<Presence> presences = new ArrayList<>();

    public List<Presence> getPresences() {
        return presences;
    }

    public void setPresences(List<Presence> presences) {
        this.presences = presences;
    }

    public static class Presence {
        private String id;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getTo() {
            return to;
        }

        public void setTo(String to) {
            this.to = to;
        }

        private String to;
//        @XStreamAsAttribute
        private org.jivesoftware.smack.packet.Presence.Type type;
//        @XStreamAsAttribute
        private String from;
        private String status;
        private String priority;
        private String simpleFrom;
        private org.jivesoftware.smack.packet.Presence.Mode show;
//        @XStreamOmitField
        private String c;

        public org.jivesoftware.smack.packet.Presence.Mode getShow() {
            return show;
        }

        public void setShow(org.jivesoftware.smack.packet.Presence.Mode show) {
            this.show = show;
        }

        public org.jivesoftware.smack.packet.Presence.Type getType() {
            return type;
        }

        public void setType(org.jivesoftware.smack.packet.Presence.Type type) {
            this.type = type;
            show = org.jivesoftware.smack.packet.Presence.Mode.away;
        }

        public String getFrom() {
//            Log.i("PresenceList", "from");
            if(from != null && from.contains("@")) {
                return from.substring(0, from.indexOf("@"));
            }else{
                return from;
            }
        }

        public String getFullFrom(){
            return from;
        }

        public void setFrom(String from) {
            this.from = from;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getPriority() {
            return priority;
        }

        public void setPriority(String priority) {
            this.priority = priority;
        }
    }


}
