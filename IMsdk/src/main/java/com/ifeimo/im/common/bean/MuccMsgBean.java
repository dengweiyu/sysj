package com.ifeimo.im.common.bean;

import android.database.Cursor;

import com.ifeimo.im.framwork.database.Fields;
import com.ifeimo.im.provider.BaseProvider;
import com.ifeimo.im.provider.MuccProvider;

import org.jivesoftware.smack.packet.DefaultExtensionElement;
import org.jivesoftware.smack.packet.ExtensionElement;

import java.util.List;

/**
 * Created by lpds on 2017/1/11.
 * 群消息
 */
public class MuccMsgBean extends MsgBean {

    private String rooomID;
    private String formatTime;

    private MuccMsgBean(int id, String memberId,String member, String content, String createTime, String sendType, String rooomID) throws Exception {
        throw new Exception();
    }


    public MuccMsgBean(int id, String msgId,String memberId, String memberNickName, String memberAvatarUrl,String content, String createTime, int sendType, String rooomID) {
        super(id, msgId,memberId, memberNickName, memberAvatarUrl, null, null, null, content, createTime, sendType);
        this.rooomID = rooomID;
    }

    public MuccMsgBean() {
    }

    @Override
    public String getReceiverId(){
//        throw new Exception("此消息是群聊");
        return null;
    }

    @Override
    public void setReceiverId(String receiverId){
//        throw new Exception("此消息是群聊");
    }

    public String getRooomID() {
        return rooomID;
    }

    public void setRooomID(String rooomID) {
        this.rooomID = rooomID;
    }

    public  static MuccMsgBean createLineByCursor(Cursor cursor){
        MuccMsgBean muccMsgBean = new MuccMsgBean();
        muccMsgBean.setId(cursor.getInt(cursor.getColumnIndex(Fields.GroupChatFields.ID)));
        muccMsgBean.setSendType(cursor.getInt(cursor.getColumnIndex(Fields.GroupChatFields.SEND_TYPE)));
        muccMsgBean.setMemberId(cursor.getString(cursor.getColumnIndex(Fields.GroupChatFields.MEMBER_ID)));
        muccMsgBean.setCreateTime(cursor.getString(cursor.getColumnIndex(Fields.GroupChatFields.CREATE_TIME)));
        muccMsgBean.setRooomID(cursor.getString(cursor.getColumnIndex(Fields.GroupChatFields.ROOM_ID)));
        muccMsgBean.setContent(cursor.getString(cursor.getColumnIndex(Fields.GroupChatFields.CONTENT)));
        muccMsgBean.setMemberNickName(cursor.getString(cursor.getColumnIndex(Fields.AccounFields.MEMBER_NICKNAME)));
        muccMsgBean.setMemberAvatarUrl(cursor.getString(cursor.getColumnIndex(Fields.AccounFields.MEMBER_AVATARURL)));
        muccMsgBean.setMsgId(cursor.getString(cursor.getColumnIndex(Fields.GroupChatFields.MSG_ID)));
        return muccMsgBean;

    }

    public String getFormatTime() {
        return formatTime;
    }

    public void setFormatTime(String formatTime) {
        this.formatTime = formatTime;
    }

    @Override
    public String toString() {
        return "MuccMsgBean{" +
                "rooomID='" + rooomID + '\'' +
                '}';
    }

    public static MuccMsgBean buildMuccBean(org.jivesoftware.smack.packet.Message message) {

        if (message.getBody() != null && !message.getBody().equals("")) {
            MuccMsgBean muccMsgBean = new MuccMsgBean();
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
            muccMsgBean.setRooomID(roomID);
            muccMsgBean.setMsgId(msgid);
            return muccMsgBean;
        } else {
            return null;
        }
    }

}
