package com.ifeimo.im.common.postentity;

/**
 * Created by lpds on 2017/4/11.
 */
@Deprecated
public class IMTextHtmlEntity extends BaseEntity {

    private String sendMemberId;
    private String defaultStr;
    private String[] matchStr;
    private boolean isMe;

    public String getSendMemberId() {
        return sendMemberId;
    }

    public void setSendMemberId(String sendMemberId) {
        this.sendMemberId = sendMemberId;
    }

    public String getDefaultStr() {
        return defaultStr;
    }

    public void setDefaultStr(String defaultStr) {
        this.defaultStr = defaultStr;
    }

    public String[] getMatchStr() {
        return matchStr;
    }

    public void setMatchStr(String[] matchStr) {
        this.matchStr = matchStr;
    }

    public boolean isMe() {
        return isMe;
    }

    public void setMe(boolean me) {
        isMe = me;
    }
}
