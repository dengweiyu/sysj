package com.ifeimo.im.common.bean.model;

import com.ifeimo.im.framwork.database.Fields;

import org.jivesoftware.smack.packet.Message;

import y.com.sqlitesdk.framework.annotation.TBColumn;
import y.com.sqlitesdk.framework.annotation.TBPrimarykey;

/**
 * Created by lpds on 2017/4/19.
 * 单聊模型
 */
public class ChatMsgModel extends Model<ChatMsgModel> implements IChatMsg {

    public static final String TB_NAME = Fields.ChatFields.TB_NAME;

    @TBPrimarykey
    private long id;

    @TBColumn
    private int send_type;

    @TBColumn
    private String memberId;

    @TBColumn
    private String receiverId;

    @TBColumn
    private String create_time;

    @TBColumn
    private String content;

    @TBColumn
    private String msgId;

    private String member_nick_name;

    private String avatarUrl;

    @Override
    public String getMemberAvatarUrl() {
        return avatarUrl;
    }

    @Override
    public void setMemberAvatarUrl(String picurl) {
        avatarUrl = picurl;
    }

    @Override
    public String getMemberNickName() {
        return member_nick_name;
    }

    @Override
    public void setMemberNickName(String nickname) {
        member_nick_name = nickname;
    }

    public int getSendType() {
        return send_type;
    }

    public void setSendType(int send_type) {
        this.send_type = send_type;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public String getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(String receiverId) {
        this.receiverId = receiverId;
    }


    public void setCreateTime(String create_time) {
        this.create_time = create_time;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getMsgId() {
        return msgId;
    }

    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }

    @Override
    public String getTableName() {
        return TB_NAME;
    }

    @Override
    public long getId() {
        return id;
    }

    @Override
    public void setId(long id) {
        this.id = id;
    }

    @Override
    public String getCreateTime() {
        return create_time;
    }

    public static ChatMsgModel buildChatModel(Message message) {
        if (message.getBody() != null && !message.getBody().equals("")) {
            ChatMsgModel msgBean = new ChatMsgModel();
            String content = message.getBody();
            String memberID = message.getFrom().split("@")[0];//发送者
            String receiverId = message.getTo().split("@")[0];//接收者

            msgBean.setMemberId(memberID);
            msgBean.setReceiverId(receiverId);
            msgBean.setContent(content);
            msgBean.setSendType(Fields.ChatFields.SEND_FINISH);
            msgBean.setCreateTime(System.currentTimeMillis()+"");
            msgBean.setMsgId(message.getPacketID());
            return msgBean;
        } else {
            return null;
        }

    }

    @Override
    public String toString() {
        return "ChatMsgModel{" +
                "id=" + id +
                ", send_type=" + send_type +
                ", memberId='" + memberId + '\'' +
                ", receiverId='" + receiverId + '\'' +
                ", create_time='" + create_time + '\'' +
                ", content='" + content + '\'' +
                ", msgId='" + msgId + '\'' +
                ", member_nick_name='" + member_nick_name + '\'' +
                ", avatarUrl='" + avatarUrl + '\'' +
                '}';
    }
}
