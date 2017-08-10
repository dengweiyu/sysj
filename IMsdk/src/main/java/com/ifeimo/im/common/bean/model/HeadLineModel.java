package com.ifeimo.im.common.bean.model;

import android.content.Intent;

import com.ifeimo.im.framwork.Proxy;

import org.jivesoftware.smack.packet.DefaultExtensionElement;
import org.jivesoftware.smack.packet.ExtensionElement;
import org.jivesoftware.smack.packet.Message;

import java.util.List;
import java.util.Set;

/**
 * Created by linhui on 2017/8/8.
 */
public class HeadLineModel extends Model<HeadLineModel> implements Msg{

    private long id;

    private String msgId;

    private String createTime;

    private String body;

    private String open_type;

    private String open_id;

    private String opposite_memberId;

    private String memberId;

    private int send_type;


    public HeadLineModel() {
        memberId = Proxy.getAccountManger().getUserMemberId();
    }

    public String getMemberId() {
        return memberId;
    }


    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public String getOpen_id() {
        return open_id;
    }

    public void setOpen_id(String open_id) {
        this.open_id = open_id;
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

    public String getOpen_type() {
        return open_type;
    }

    public void setOpen_type(String open_type) {
        this.open_type = open_type;
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

        String content = message.getBody();
        String Opposite_memberId = message.getFrom().split("@")[0];//发送者

        HeadLineModel headLineModel = new HeadLineModel();
        headLineModel.setContent(content);
        headLineModel.setOpposite_memberId(Opposite_memberId);

        List<ExtensionElement> list = message.getExtensions();
        headLineModel.setCreateTime(System.currentTimeMillis() + "");
        for (ExtensionElement d : list) {
            if(d instanceof DefaultExtensionElement) {
                String time = ((DefaultExtensionElement) d).getValue("time");
                if (time != null) {
                    headLineModel.setCreateTime(time);
                    break;
                }
            }
        }

        ExtensionElement extensionElement = message.getExtension("msginfo:extend ");
        if(extensionElement instanceof DefaultExtensionElement){
            DefaultExtensionElement defaultExtensionElement = ((DefaultExtensionElement)extensionElement);
            headLineModel.setOpen_type(defaultExtensionElement.getValue("open_type"));
            headLineModel.setOpen_id(defaultExtensionElement.getValue("open_id"));
        }

        return headLineModel;
    }


    @Override
    public String toString() {
        return "HeadLineModel{" +
                "id=" + id +
                ", createTime='" + createTime + '\'' +
                ", body='" + body + '\'' +
                ", open_type='" + open_type + '\'' +
                ", open_id='" + open_id + '\'' +
                ", opposite_memberId='" + opposite_memberId + '\'' +
                ", memberId='" + memberId + '\'' +
                '}';
    }

    @Override
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
