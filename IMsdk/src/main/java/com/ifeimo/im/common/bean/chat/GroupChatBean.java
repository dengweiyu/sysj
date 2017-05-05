package com.ifeimo.im.common.bean.chat;

import org.jivesoftware.smackx.muc.MultiUserChat;

import java.util.List;

/**
 * Created by lpds on 2017/1/11.
 * 群聊房间信息
 */
public class GroupChatBean extends BaseChatBean {
    private List<String> accounts;
    private MultiUserChat multiUserChat;
    private String roomID;

    public GroupChatBean(String usert, String roomID, List<String> accounts, MultiUserChat multiUserChat) {
        super(usert);
        this.accounts = accounts;
        this.multiUserChat = multiUserChat;
        this.roomID = roomID;
    }

    public List<String> getAccounts() {
        return accounts;
    }

    public void setAccounts(List<String> accounts) {
        this.accounts = accounts;
    }

    public MultiUserChat getMultiUserChat() {
        return multiUserChat;
    }

    public void setMultiUserChat(MultiUserChat multiUserChat) {
        this.multiUserChat = multiUserChat;
    }

    public String getRoomID() {
        return roomID;
    }

    public void setRoomID(String roomID) {
        this.roomID = roomID;
    }
}
