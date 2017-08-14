package com.ifeimo.im.common.bean.model;

import com.ifeimo.im.common.util.StringUtil;
import com.ifeimo.im.framwork.database.Fields;

import org.jivesoftware.smack.packet.DefaultExtensionElement;
import org.jivesoftware.smack.packet.ExtensionElement;
import org.jivesoftware.smack.packet.Message;

import java.util.List;

import y.com.sqlitesdk.framework.annotation.TBColumn;
import y.com.sqlitesdk.framework.annotation.TBPrimarykey;

/**
 * Created by lpds on 2017/4/20.
 */
public class GroupChatModel extends Model<GroupChatModel> implements IChatMsg {

    public static final String TB_NAME = Fields.GroupChatFields.TB_NAME;

    @TBPrimarykey
    private long id;

    @TBColumn
    private int send_type;

    @TBColumn
    private String msgId;

    @TBColumn
    private String memberId;

    @TBColumn
    private String create_time;

    @TBColumn
    private String roomid;

    @TBColumn
    private String content;

    private String roomName;

    private String roompic;

    private String member_nick_name;

    private String avatarUrl;


    public String getMemberAvatarUrl() {
        return avatarUrl;
    }

    public void setMemberAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getMemberNickName() {
        return member_nick_name;
    }

    public void setMemberNickName(String member_nick_name) {
        this.member_nick_name = member_nick_name;
    }

    public int getSendType() {
        return send_type;
    }

    public void setSendType(int send_type) {
        this.send_type = send_type;
    }

    public String getMsgId() {
        return msgId;
    }

    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public void setCreateTime(String create_time) {
        this.create_time = create_time;
    }

    public String getRoomid() {
        return roomid;
    }

    public void setRoomid(String roomid) {
        this.roomid = roomid;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
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

    public static GroupChatModel buildMuccModel(Message message) {
        if (!StringUtil.isNull(message.getBody())) {
            GroupChatModel muccMsgBean = new GroupChatModel();
            String msgid = message.getStanzaId();
            String content = message.getBody();
            String roomID = message.getFrom().contains("conference") ? message.getFrom().split("@")[0] : null;
            if (message.hasExtension("memberInfo", "jabber:fm:memberinfo")) {
                DefaultExtensionElement defaultExtensionElement = message.getExtension("memberInfo", "jabber:fm:memberinfo");
                String nickName = defaultExtensionElement.getValue("nickname");
                String avatarUrl = defaultExtensionElement.getValue("avatarUrl");
                String memberId = defaultExtensionElement.getValue("memberId");
                muccMsgBean.setMemberId(memberId);
                muccMsgBean.setMemberNickName(nickName);
                muccMsgBean.setMemberAvatarUrl(avatarUrl);

                List<ExtensionElement> list = message.getExtensions();
                muccMsgBean.setCreateTime(System.currentTimeMillis() + "");
                for (ExtensionElement d : list) {
                    if(d instanceof  DefaultExtensionElement) {
                        String time = ((DefaultExtensionElement) d).getValue("time");
                        if (time != null) {
                            muccMsgBean.setCreateTime(time);
                            break;
                        }
                    }
                }
            }
            muccMsgBean.setContent(content);
            muccMsgBean.setSendType(Fields.GroupChatFields.SEND_FINISH);
            muccMsgBean.setRoomid(roomID);
            muccMsgBean.setMsgId(msgid);
            return muccMsgBean;
        } else {
            return null;
        }
    }


    @Override
    public String toString() {
        return "GroupChatModel{" +
                "id=" + id +
                ", send_type=" + send_type +
                ", msgId='" + msgId + '\'' +
                ", memberId='" + memberId + '\'' +
                ", create_time='" + create_time + '\'' +
                ", roomid='" + roomid + '\'' +
                ", content='" + content + '\'' +
                ", member_nick_name='" + member_nick_name + '\'' +
                ", avatarUrl='" + avatarUrl + '\'' +
                '}';
    }
}
