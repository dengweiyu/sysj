package com.ifeimo.im.common.bean.model;

import com.ifeimo.im.common.util.StringUtil;
import com.ifeimo.im.framwork.Proxy;

import org.jivesoftware.smack.packet.DefaultExtensionElement;
import org.jivesoftware.smack.packet.ExtensionElement;
import org.jivesoftware.smack.packet.Message;

import java.util.List;

/**
 * Created by linhui on 2017/8/8.
 */
public class HeadLineModel extends Model<HeadLineModel> implements Msg{

    private long id;

    private String msgId;

    private String createTime;

    private String body;

    private String mode;

    private String extras;

    private String opposite_memberId;

    private String memberId;

    private int send_type;

    private String  imageUrl;

    private String subject;

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public HeadLineModel() {
        memberId = Proxy.getAccountManger().getUserMemberId();
    }

    public String getMemberId() {
        return memberId;
    }


    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public String getExtras() {
        return extras;
    }

    public void setExtras(String extras) {
        this.extras = extras;
    }

    public void setContent(String body) {
        this.body = body;
    }

    public String getOpposite_memberId() {
        return opposite_memberId;
    }

    public void setOpposite_memberId(String opposite_memberId) {
        this.opposite_memberId = opposite_memberId;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    @Override
    public String getTableName() {
        return null;
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
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }


    public static final HeadLineModel buildHeadLineModel(Message message){

        String body = message.getBody();
        String Opposite_memberId = message.getFrom().split("@")[0];//发送者
        String msgid = message.getStanzaId();
        String subject = message.getSubject();
        HeadLineModel headLineModel = new HeadLineModel();
        headLineModel.setBody(body);
        headLineModel.setOpposite_memberId(Opposite_memberId);
        headLineModel.setMsgId(msgid);
        headLineModel.setSubject(subject);
        List<ExtensionElement> list = message.getExtensions();
        headLineModel.setCreateTime(System.currentTimeMillis() + "");
        for (ExtensionElement extensionElement : list) {
            if(extensionElement instanceof DefaultExtensionElement) {
                DefaultExtensionElement defaultExtensionElement = (DefaultExtensionElement)extensionElement;
                String time = defaultExtensionElement.getValue("time");
                String mode = defaultExtensionElement.getValue("mode");
                String extras = defaultExtensionElement.getValue("extras");
                String imageUrl = defaultExtensionElement.getValue("imageUrl");
                if (!StringUtil.isNull(time)) {
                    headLineModel.setCreateTime(time);
                }
                if (!StringUtil.isNull(mode)) {
                    headLineModel.setMode(mode);
                }
                if (!StringUtil.isNull(extras)) {
                    headLineModel.setExtras(extras);
                }

                if (!StringUtil.isNull(imageUrl)){
                    headLineModel.setImageUrl(imageUrl);
                }

            }
        }
        return headLineModel;
    }


    @Override
    public String toString() {
        return "HeadLineModel{" +
                "id=" + id +
                ", createTime='" + createTime + '\'' +
                ", body='" + body + '\'' +
                ", mode='" + mode + '\'' +
                ", extras='" + extras + '\'' +
                ", opposite_memberId='" + opposite_memberId + '\'' +
                ", memberId='" + memberId + '\'' +
                '}';
    }

    @Override
    @Deprecated
    public String getContent() {
        return body;
    }

    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }

    @Override
    public String getMsgId() {
        return msgId;
    }

    @Override
    public int getSendType() {
        return send_type;
    }

    @Override
    public void setSendType(int i) {
        this.send_type = i;
    }
}
