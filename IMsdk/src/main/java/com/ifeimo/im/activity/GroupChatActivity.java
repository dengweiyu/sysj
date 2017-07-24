package com.ifeimo.im.activity;

import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatButton;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.ifeimo.im.R;
import com.ifeimo.im.common.adapter.MuccChatReAdapter;
import com.ifeimo.im.common.bean.model.GroupChatModel;
import com.ifeimo.im.common.bean.model.InformationModel;
import com.ifeimo.im.common.bean.model.SubscriptionModel;
import com.ifeimo.im.common.util.IMWindosThreadUtil;
import com.ifeimo.im.common.util.PManager;
import com.ifeimo.im.common.util.StringUtil;
import com.ifeimo.im.framwork.IMSdk;
import com.ifeimo.im.framwork.Proxy;
import com.ifeimo.im.framwork.database.Fields;
import com.ifeimo.im.framwork.commander.IMWindow;
import com.ifeimo.im.provider.InformationProvide;
import com.ifeimo.im.provider.business.GroupChatBusiness;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import y.com.sqlitesdk.framework.business.Business;
import y.com.sqlitesdk.framework.db.Access;
import y.com.sqlitesdk.framework.sqliteinterface.Execute;


/**
 * Created by admin on 2016/12/19.
 */
public class GroupChatActivity extends BaseIMCompatActivity<GroupChatModel,MuccChatReAdapter> {
    private String roomJID;
    private String roomName;
    private String roomPicurl;
    //发送的队列
    private List<GroupChatModel> sendMuccMsgBean;

    @Override
    protected void init(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            PManager.getCacheUser(getContext());
            roomJID = savedInstanceState.getString("roomJID");
            roomName = savedInstanceState.getString("roomName");
            roomPicurl = savedInstanceState.getString("roomPicurl");
            sendMuccMsgBean = (List<GroupChatModel>) savedInstanceState.get("sendMuccMsgBean");
        } else {
            roomJID = getIntent().getStringExtra("roomJID");
            roomName = getIntent().getStringExtra("roomName");
            roomPicurl = getIntent().getStringExtra("roomPicurl");
            sendMuccMsgBean = new ArrayList<>();
        }
        title.setText(roomName);
        getMsgListView().setVisibility(View.INVISIBLE);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                getMsgListView().setVisibility(View.VISIBLE);
            }
        }, 800);

    }

    @Override
    protected void debug() {

        AppCompatButton b = new AppCompatButton(this);
        b.setText("Leave Room "+getKey());
        if(Build.VERSION.SDK_INT > 20) {
            b.setElevation(10);
        }
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(v.getContext()).setMessage("离开房间 "+getKey()).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Proxy.getMessageManager().leaveMuccRoom(getKey());
                    }
                }).show();

            }
        });
        FrameLayout.LayoutParams l = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        l.gravity = Gravity.TOP|Gravity.RIGHT;
        ((ViewGroup)getWindow().getDecorView()).addView(b,l);

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
        IMWindosThreadUtil.getInstances().run(getKey(), new Runnable() {
            @Override
            public void run() {
                String sendMsg = editeMsg.getText().toString().trim();
                if (StringUtil.isNull(sendMsg)) {
                    return;
                }
                GroupChatModel msgBean = createBean(sendMsg);
                sendMuccMsgBean.add(msgBean);
                Proxy.getMessageManager().sendMuccMsg(GroupChatActivity.this.getKey(), msgBean);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        editeMsg.setText("");
                    }
                });
            }
        });
    }

    private GroupChatModel createBean(String sendMsg) {
        GroupChatModel msgBean = new GroupChatModel();
        msgBean.setRoomid(roomJID);
        msgBean.setMemberId(Proxy.getAccountManger().getUserMemberId());
        msgBean.setMemberAvatarUrl(Proxy.getAccountManger().getAccount(true).getAvatarUrl());
        msgBean.setMemberNickName(Proxy.getAccountManger().getAccount(true).getNickName());
        msgBean.setCreateTime(System.currentTimeMillis() + "");
        msgBean.setContent(sendMsg);
        return msgBean;
    }

    @Override
    protected void onRestart() {
        super.onRestart();
//        Proxy.getMessageManager().createGruopChat(getKey());
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
//        Proxy.getMessageManager().createGruopChat(getKey());
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
        GroupChatModel msgBean = createBean(content);
        sendMuccMsgBean.add(msgBean);
        Proxy.getMessageManager().sendMuccMsg(GroupChatActivity.this.getKey(), msgBean);
    }

    @Override
    protected void getMaxMsgCount(final Runnable runnable) {
        IMWindosThreadUtil.getInstances().run(getKey(), new Runnable() {
            @Override
            public void run() {
                Access.runCustomThread(new Execute() {
                    @Override
                    public void onExecute(SQLiteDatabase sqLiteDatabase) throws Exception {
                        long maxCount = y.com.sqlitesdk.framework.business.
                                Business.getInstances().
                                getTableMaxCount(sqLiteDatabase,
                                        GroupChatModel.class, " roomid = '" + getKey()+"'");
                        getAdapter().setMaxCount(new Integer(maxCount + ""));
                        if(!isFinishing()) {
                            runnable.run();
                        }
                    }

                    @Override
                    public void onExternalError() {

                    }
                });


            }
        });
    }

    @Override
    public void onFirstDataChange(Cursor cursor) {
        loginSucceed();
    }

    @Override
    protected void onBeforeLoad(final Runnable runnable) {
        IMWindosThreadUtil.getInstances().run(getKey(), new Runnable() {
            @Override
            public void run() {
                Proxy.getMessageManager().createGruopChat(getKey());
                Access.runCustomThread(new Execute() {
                    @Override
                    public void onExecute(SQLiteDatabase sqLiteDatabase) throws Exception {

                        SubscriptionModel subscriptionModel = new SubscriptionModel();
                        subscriptionModel.setSubscription_id(roomJID);
                        subscriptionModel.setSubscription_name(roomName);
                        subscriptionModel.setSubscription_pic_url(roomPicurl);
                        subscriptionModel.setSubscription_type(InformationModel.ROOM);

                        if (GroupChatBusiness.getInstances().insertSubscriptionDetails(sqLiteDatabase, subscriptionModel) > 0) {
                            log("------ 新增或者修改 subscription 完毕 type = room  roomid = "+roomJID+"------");
                        }
                        if(GroupChatBusiness.getInstances().insertAccount2Subscription(sqLiteDatabase
                                ,subscriptionModel.getId(),false).getId() > 0) {
                            if(!isFinishing()) {
                                runnable.run();
                            }
                        }
                    }

                    @Override
                    public void onExternalError() {

                    }
                });
            }
        });
    }


    @Override
    public void cancelInformation() {
        IMWindosThreadUtil.getInstances().run(getKey(), new Runnable() {
            @Override
            public void run() {
                Access.run(new Execute() {
                    @Override
                    public void onExecute(SQLiteDatabase sqLiteDatabase) throws Exception {
                        String sqlStr = String.format("%s = ? AND %s = ? AND %s = ?",
                                Fields.InformationFields.MEMBER_ID,
                                Fields.InformationFields.OPPOSITE_ID,
                                Fields.InformationFields.TYPE);
                        InformationModel informationModel =
                                Business.getInstances().queryLineByWhere(
                                        sqLiteDatabase,
                                        InformationModel.class,
                                        sqlStr,
                                        new String[]{Proxy.getAccountManger().getUserMemberId(), getKey(), InformationModel.ROOM + ""});
                        if (informationModel != null) {
                            informationModel.setUnread(0);
                            if (Business.getInstances().modify(
                                    sqLiteDatabase,
                                    informationModel) > 0) {
                                IMSdk.CONTEXT.getContentResolver().notifyChange(InformationProvide.CONTENT_URI, null);
                            }
                        }
                    }

                    @Override
                    public void onExternalError() {

                    }
                });

            }
        });
    }

    @Override
    protected int getContentViewByInt() {
        return R.layout.activity_group_chat_recycler;
    }
}
