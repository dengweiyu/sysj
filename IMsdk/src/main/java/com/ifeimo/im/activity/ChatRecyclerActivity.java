package com.ifeimo.im.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.ifeimo.im.common.MD5;
import com.ifeimo.im.common.adapter.ChatReAdapter;
import com.ifeimo.im.common.bean.AccountBean;
import com.ifeimo.im.common.bean.InformationBean;
import com.ifeimo.im.common.bean.msg.MsgBean;
import com.ifeimo.im.common.bean.UserBean;
import com.ifeimo.im.common.util.ConnectUtil;
import com.ifeimo.im.common.util.IMWindosThreadUtil;
import com.ifeimo.im.common.util.PManager;
import com.ifeimo.im.common.util.StringUtil;
import com.ifeimo.im.framwork.Proxy;
import com.ifeimo.im.framwork.database.Fields;
import com.ifeimo.im.framwork.database.business.Business;
import com.ifeimo.im.framwork.request.Account;

import org.json.JSONObject;

import okhttp3.Response;

public class ChatRecyclerActivity extends BaseCompatActivity<ChatReAdapter> implements View.OnClickListener {

    private AccountBean receiverBean = new AccountBean();

    @Override
    protected void init(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            PManager.getCacheUser(getContext());
            receiverBean = (AccountBean) savedInstanceState.getSerializable("receiver");
        } else {
            receiverBean.setMemeberid(getIntent().getStringExtra("receiverID"));
            receiverBean.setNickName(getIntent().getStringExtra("receiverNickName"));
            receiverBean.setAvatarUrl(getIntent().getStringExtra("receiverAvatarUrl"));
        }
        if (!StringUtil.isNull(receiverBean.getMemeberid())) {
            instances();
        }
        log("------ 对方用户 " + receiverBean.getMemeberid() + "------ ");
        getAdapter().setReceiverBean(receiverBean);
        checkReceiver();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putSerializable("receiver",receiverBean);
        super.onSaveInstanceState(outState);
        log("------- ChatActivity.Class 单聊被回收 数据已缓存 --------- ");
    }

    private void instances() {
        title.setText(receiverBean.getNickName());
        IMWindosThreadUtil.getInstances().run(getKey(),new Runnable() {
            @Override
            public void run() {
                initChat();
            }
        });
    }

    @Override
    protected void onNewIntent(Intent intent) {
        if(!MD5.getMD5(getKey()).equals(MD5.getMD5(intent.getStringExtra("receiverID")))) {
            close();
            receiverBean.setMemeberid(intent.getStringExtra("receiverID"));
            receiverBean.setNickName(intent.getStringExtra("receiverNickName"));
            receiverBean.setAvatarUrl(intent.getStringExtra("receiverAvatarUrl"));
            instances();
            laterInit();
        }
        super.onNewIntent(intent);
    }

    private MsgBean initMsgBean() {
        MsgBean msgBean = null;
        msgBean = new MsgBean();
        msgBean.setMemberId(UserBean.getMemberID());
        msgBean.setReceiverId(receiverBean.getMemeberid());
        return msgBean;
    }

    /**
     * 初始化 chat单聊
     */
    private void initChat() {
        Proxy.getMessageManager().createChat(receiverBean.getMemeberid(), UserBean.getMemberID());
    }

    public void sendOnclick(View v) {

        new Thread() {
            @Override
            public void run() {
                String content = editeMsg.getText().toString();
                if (!content.equals("")) {
                    MsgBean msgBean = initMsgBean();
                    msgBean.setContent(content);
                    msgBean.setCreateTime(System.currentTimeMillis() + "");
                    Proxy.getMessageManager().sendChatMsg(getKey(), msgBean);
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            editeMsg.setText("");
                        }
                    });
                }
            }
        }.start();
    }

    @Override
    public String getReceiver() {
        return receiverBean.getMemeberid();
    }

    @Override
    public void send(String content) {
        MsgBean msgBean = initMsgBean();
        msgBean.setContent(content);
        msgBean.setCreateTime(System.currentTimeMillis() + "");
        Proxy.getMessageManager().sendChatMsg(this.getKey(), msgBean);
    }

    @Override
    public String getKey() {
        return UserBean.getMemberID() + receiverBean.getMemeberid();
    }

    @Override
    public void close() {
        Proxy.getMessageManager().leaveChat(getKey());
    }

    @Override
    public int getType() {
        return CHAT_TYPE;
    }

    @Override
    public void loginSucceed(){
        initChat();
    }

    @Override
    public void onClick(View view) {startActivity(new Intent(this, ContactListActivity.class));}

    private void checkReceiver() {
        if (StringUtil.isNull(receiverBean.getNickName()) || StringUtil.isNull(receiverBean.getAvatarUrl())) {
            if(!ConnectUtil.isConnect(this)){
                return;
            }
            IMWindosThreadUtil.getInstances().run(getKey(),new Runnable() {
                @Override
                public void run() {
                    try {
                        Response response = Account.getMemberInfo(ChatRecyclerActivity.this,receiverBean.getMemeberid());

                        JSONObject jsonObject = new JSONObject(response.body().string());
                        if(jsonObject.getInt("code") == 200){
                            receiverBean.setAvatarUrl(jsonObject.getString("avatarUrl"));
                            receiverBean.setNickName(jsonObject.getString("nickname"));
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    getAdapter().notifyDataSetChanged();
                                }
                            });
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    @Override
    protected void getMaxMsgCount(final Runnable runnable) {
        IMWindosThreadUtil.getInstances().run(getKey(),new Runnable() {
            @Override
            public void run() {
                getAdapter().setMaxCount(Business.getInstances().queryMaxCountByTableName(Fields.ChatFields.TB_NAME, " (receiverId = '" + receiverBean.getMemeberid() + "' and memberId = '" + UserBean.getMemberID() + "') " +
                        "or (receiverId = '" + UserBean.getMemberID() + "' and memberId = '" + receiverBean.getMemeberid() + "')"));
                log(" ------ 最大的消息量为 maxCount = " + getAdapter().getMaxCount() + " -------");
                runnable.run();
            }
        });
    }

    @Override
    protected void onBeforeLoad(Runnable runnable) {
        runnable.run();
    }


    @Override
    public void cancelInformation() {
        IMWindosThreadUtil.getInstances().run(getKey(),new Runnable() {
            @Override
            public void run() {
                Business.getInstances().cancelInformation(UserBean.getMemberID(),getReceiver(), InformationBean.CHAT);
            }
        });
    }
}
