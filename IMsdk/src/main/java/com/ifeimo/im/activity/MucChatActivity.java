package com.ifeimo.im.activity;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;

import com.ifeimo.im.common.adapter.MuccChatReAdapter;
import com.ifeimo.im.common.bean.InformationBean;
import com.ifeimo.im.common.bean.msg.MuccMsgBean;
import com.ifeimo.im.common.bean.UserBean;
import com.ifeimo.im.common.util.IMWindosThreadUtil;
import com.ifeimo.im.common.util.PManager;
import com.ifeimo.im.common.util.StringUtil;
import com.ifeimo.im.common.util.ThreadUtil;
import com.ifeimo.im.framwork.Proxy;
import com.ifeimo.im.framwork.database.Fields;
import com.ifeimo.im.framwork.database.business.Business;
import com.ifeimo.im.framwork.interface_im.IMWindow;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by admin on 2016/12/19.
 */
public class MucChatActivity extends BaseCompatActivity<MuccChatReAdapter> {
    private String roomJID;
    private String roomName;
    private String roomPicurl;
//    private MuccBean muccBean;
    //发送的队列
    private List<MuccMsgBean> sendMuccMsgBean;
    @Override
    protected void init(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            PManager.getCacheUser(getContext());
            roomJID = savedInstanceState.getString("roomJID");
            roomName = savedInstanceState.getString("roomName");
            roomPicurl = savedInstanceState.getString("roomPicurl");
            sendMuccMsgBean = (List<MuccMsgBean>) savedInstanceState.get("sendMuccMsgBean");
        } else {
            roomJID = getIntent().getStringExtra("roomJID");
            roomName = getIntent().getStringExtra("roomName");
            roomPicurl = getIntent().getStringExtra("roomPicurl");
            sendMuccMsgBean = new ArrayList<>();
        }
        title.setText(roomName);
        getMsgListView().setVisibility(View.INVISIBLE);
        Proxy.getMessageManager().createMucc(getKey());
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                getMsgListView().setVisibility(View.VISIBLE);
            }
        }, 800);

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString("roomJID", roomJID);
        outState.putString("roomName", roomName);
        outState.putSerializable("sendMuccMsgBean", (Serializable) sendMuccMsgBean);
        super.onSaveInstanceState(outState);
        log("------- MUCChatActivity.Class 群聊被回收 数据已缓存 --------- ");
    }

    public void sendOnclick(View v) {
        IMWindosThreadUtil.getInstances().run(getKey(),new Runnable() {
            @Override
            public void run() {
                String sendMsg = editeMsg.getText().toString().trim();
                if (StringUtil.isNull(sendMsg)) {
                    return;
                }
                MuccMsgBean msgBean = createBean(sendMsg);
                sendMuccMsgBean.add(msgBean);
                Proxy.getMessageManager().sendMuccMsg(MucChatActivity.this.getKey(), msgBean);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        editeMsg.setText("");
                    }
                });
            }
        });
    }

    private MuccMsgBean createBean(String sendMsg) {
        MuccMsgBean msgBean = new MuccMsgBean();
        msgBean.setRooomID(roomJID);
        msgBean.setMemberId(UserBean.getMemberID());
        msgBean.setMemberAvatarUrl(UserBean.getAvatarUrl());
        msgBean.setMemberNickName(UserBean.getNickName());
        msgBean.setCreateTime(System.currentTimeMillis() + "");
        msgBean.setContent(sendMsg);
        return msgBean;
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Proxy.getMessageManager().createMucc(getKey());
    }

    @Override
    public void close() {
    }

    @Override
    public String getKey() {
        return roomJID;
    }

    @Override
    public void loginSucceed() {
        Proxy.getMessageManager().createMucc(getKey());
    }

    @Override
    public int getType() {
        return IMWindow.MUCCHAT_TYPE;
    }

//    @Override
//    public String getRoomId() {
//        return roomJID;
//    }

    @Override
    public void send(String content) {
        MuccMsgBean msgBean = createBean(content);
        sendMuccMsgBean.add(msgBean);
        Proxy.getMessageManager().sendMuccMsg(MucChatActivity.this.getKey(), msgBean);
    }

    @Override
    protected void getMaxMsgCount(final Runnable runnable) {
        ThreadUtil.getInstances().createThreadStartToCachedThreadPool(new Runnable() {
            @Override
            public void run() {
                try {
                    getAdapter().setMaxCount(Business.getInstances().queryMaxCountByTableName(Fields.GroupChatFields.TB_NAME, " roomid = " + roomJID));
                    log(" ------ 最大的消息量为 maxCount = " + getAdapter().getMaxCount() + " -------");
                    if(!isFinish()) {
                        runnable.run();
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }
    @Override
    public void onFirstDataChange(Cursor cursor) {
        loginSucceed();
    }

    @Override
    protected void onBeforeLoad(final Runnable runnable) {
        IMWindosThreadUtil.getInstances().run(getKey(),new Runnable() {
            @Override
            public void run() {
                if(Business.getInstances().insertSubscription(UserBean.getMemberID(),roomJID,roomName,roomPicurl) > 0){
                    log("------ 查询 subscription 完毕 type = room ------");
                    if(!isFinish()){
                        runnable.run();
                    }
                }
            }
        });
    }


    @Override
    public void cancelInformation() {
        IMWindosThreadUtil.getInstances().run(getKey(),new Runnable() {
            @Override
            public void run() {
                Business.getInstances().cancelInformation(UserBean.getMemberID(),getKey(), InformationBean.ROOM);
            }
        });
    }
}
