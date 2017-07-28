package com.ifeimo.im.activity;


import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.ifeimo.im.R;
import com.ifeimo.im.common.MD5;
import com.ifeimo.im.common.adapter.ChatReAdapter;
import com.ifeimo.im.common.bean.AccountBean;
import com.ifeimo.im.common.bean.eventbus.ChatWindowEntity;
import com.ifeimo.im.common.bean.model.ChatMsgModel;
import com.ifeimo.im.common.bean.model.InformationModel;
import com.ifeimo.im.common.bean.response.MemberInfoRespones;
import com.ifeimo.im.common.bean.xml.PresenceList;
import com.ifeimo.im.common.util.ConnectUtil;
import com.ifeimo.im.common.util.IMWindosThreadUtil;
import com.ifeimo.im.common.util.PManager;
import com.ifeimo.im.common.util.StringUtil;
import com.ifeimo.im.framwork.IMSdk;
import com.ifeimo.im.framwork.Proxy;
import com.ifeimo.im.framwork.database.Fields;
import com.ifeimo.im.framwork.request.Account;
import com.ifeimo.im.framwork.view.FlashReplyListView;
import com.ifeimo.im.provider.InformationProvide;

import org.greenrobot.eventbus.EventBus;

import y.com.sqlitesdk.framework.business.Business;
import y.com.sqlitesdk.framework.db.Access;
import y.com.sqlitesdk.framework.sqliteinterface.Execute;

public class ChatActivity extends BaseIMCompatActivity<ChatMsgModel, ChatReAdapter> {

    public static final int SHOW_FAST_REPLY = 1000;
    public static final int SHOW_EFAULT = -200;
    private AccountBean receiverBean = new AccountBean();
    private PopupWindow addFriendPopupWindow;

    private ImageView id_fast_reply_iv;
    private TextView id_qq_tv;
    private TextView id_qq_tip_tv;
    private FlashReplyListView id_fast_reply_lv;
    private boolean isShow = false;

    private int show;
    private String showQQ;

    @Override
    protected void init(Bundle savedInstanceState) {

        id_qq_tv = (TextView) findViewById(R.id.id_qq_tv);
        id_fast_reply_iv = (ImageView) findViewById(R.id.id_fast_reply_iv);
        id_fast_reply_lv = (FlashReplyListView) findViewById(R.id.id_fast_reply_lv);
        id_qq_tip_tv = (TextView) findViewById(R.id.id_qq_tip_tv);

        id_qq_tip_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 直接打开qq某个人（可以是陌生人）
                String qqUrl = "mqqwpa://im/chat?chat_type=wpa&uin=" + showQQ+ "&version=1";
                try {
                    ChatActivity.this.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(qqUrl)));
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(ChatActivity.this,"未安装手Q或安装的版本不支持",Toast.LENGTH_SHORT).show();
                }
            }
        });

        findViewById(R.id.btn_chat_send_txt).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendOnclick(v);
            }
        });
        checkShowFast(getIntent());
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
        setTopRightBar();
//        DEBUG = true;
    }

    /**
     * 设置右上方图片
     */
    private void setTopRightBar() {
        super.id_top_right_iv.setImageResource(R.drawable.user_msg);
        id_top_right_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChatWindowEntity c = new ChatWindowEntity(receiverBean.getMemeberid(), v.getId());
                EventBus.getDefault().post(c);
            }
        });
    }

    /**
     * 是否显示快捷回复
     *
     * @param intent
     */
    private void checkShowFast(Intent intent) {
        show = intent.getIntExtra("show", SHOW_EFAULT);
        if (show == SHOW_FAST_REPLY) {
            id_fast_reply_lv.setEditText(editeMsg);
            id_fast_reply_iv.setVisibility(View.VISIBLE);
            id_fast_reply_iv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isShow) {
                        id_fast_reply_lv.setVisibility(View.GONE);
                    } else {
                        id_fast_reply_lv.setVisibility(View.VISIBLE);
                    }
                    isShow = !isShow;
                }
            });
        } else {
            id_fast_reply_iv.setVisibility(View.GONE);
        }
        checkQQShow(intent);
    }

    private void checkQQShow(Intent intent) {
        showQQ = intent.getStringExtra("showQQ");
        if (StringUtil.isNull(showQQ)) {
//            id_qq_tv.setVisibility(View.GONE);
            id_qq_tip_tv.setVisibility(View.GONE);
        } else {
//            id_qq_tv.setVisibility(View.VISIBLE);
            id_qq_tip_tv.setVisibility(View.VISIBLE);
            id_qq_tip_tv.setText(Html.fromHtml("如陪练不回复，可QQ联系,对方QQ：<font color=#10a3e2>" + showQQ + "</font>"));
//            id_qq_tv.setText(Html.fromHtml("QQ: <font color=#10a3e2>" + showQQ + "</font>"));
        }
    }

    @Override
    protected void debug() {
        IMWindosThreadUtil.getInstances().run(getKey(), new Runnable() {
            @Override
            public void run() {
                PresenceList presenceList = null;
                if (null != (presenceList = Account.getCoachPresence())) {
                    for (PresenceList.Presence presence : presenceList.getPresences()) {
                        System.out.print("id = " + presence.getId());
                        System.out.print(" ,from = " + presence.getFrom());
                        System.out.print(" ,show = " + presence.getShow());
                        System.out.print(" ,status = " + presence.getStatus());
                        System.out.print(" ,type = " + presence.getType());
                        System.out.println();

                    }
                }
            }
        });

//        addFriendPopupWindow = new AddFriendPopupWindow(this).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                showTipToast(receiverBean.getNickName()+"  "+receiverBean.getMemeberid());
//                Proxy.getAccountManger().addFriend(receiverBean);
//                addFriendPopupWindow.dismiss();
//            }
//        }).init();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putSerializable("receiver", receiverBean);
        super.onSaveInstanceState(outState);
        log("------- ChatActivity.Class  数据已缓存 --------- ");
    }

    private void instances() {
        title.setText(receiverBean.getNickName());
        IMWindosThreadUtil.getInstances().run(getKey(), new Runnable() {
            @Override
            public void run() {
                initChat();
            }
        });
    }

    @Override
    protected void onNewIntent(Intent intent) {
        if (!MD5.getMD5(getKey()).equals(MD5.getMD5(intent.getStringExtra("receiverID")))) {
            close();
            receiverBean.setMemeberid(intent.getStringExtra("receiverID"));
            receiverBean.setNickName(intent.getStringExtra("receiverNickName"));
            receiverBean.setAvatarUrl(intent.getStringExtra("receiverAvatarUrl"));
            checkShowFast(intent);
            instances();
            laterInit();
        }
        setTopRightBar();
        super.onNewIntent(intent);
    }

    private ChatMsgModel initMsgBean() {
        ChatMsgModel chatMsgModel = new ChatMsgModel();
        chatMsgModel.setMemberId(Proxy.getAccountManger().getUserMemberId());
        chatMsgModel.setReceiverId(receiverBean.getMemeberid());
        return chatMsgModel;
    }

    /**
     * 初始化 chat单聊
     */
    private void initChat() {
        Proxy.getMessageManager().createChat(receiverBean.getMemeberid(), Proxy.getAccountManger().getUserMemberId());
    }

    public void sendOnclick(View v) {
        new Thread() {
            @Override
            public void run() {
                String content = editeMsg.getText().toString();
                if (!content.equals("")) {
                    ChatMsgModel msgBean = initMsgBean();
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
        ChatMsgModel msgBean = initMsgBean();
        msgBean.setContent(content);
        msgBean.setCreateTime(System.currentTimeMillis() + "");
        Proxy.getMessageManager().sendChatMsg(this.getKey(), msgBean);
    }

    @Override
    public String getKey() {
        return Proxy.getAccountManger().getUserMemberId() + receiverBean.getMemeberid();
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
    public void loginSucceed() {
        initChat();
    }

    private void checkReceiver() {
        if (StringUtil.isNull(receiverBean.getNickName()) || StringUtil.isNull(receiverBean.getAvatarUrl())) {
            if (!ConnectUtil.isConnect(this)) {
                return;
            }
            IMWindosThreadUtil.getInstances().run(getKey(), new Runnable() {
                @Override
                public void run() {
                    try {
                        MemberInfoRespones response = Account.getMemberInfo(ChatActivity.this, receiverBean.getMemeberid());
                        if (response.getCode() == 200) {
                            receiverBean.setAvatarUrl(response.getAvatarUrl());
                            receiverBean.setNickName(response.getNickname());
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
        IMWindosThreadUtil.getInstances().run(getKey(), new Runnable() {
            @Override
            public void run() {
                Access.runCustomThread(new Execute() {
                    @Override
                    public void onExecute(SQLiteDatabase sqLiteDatabase) throws Exception {
                        long maxCount = Business.getInstances().getTableMaxCount(sqLiteDatabase, ChatMsgModel.class,
                                " (receiverId = '" + receiverBean.getMemeberid() + "' and memberId = '" + Proxy.getAccountManger().getUserMemberId() + "') " +
                                        "or (receiverId = '" + Proxy.getAccountManger().getUserMemberId() + "' and memberId = '" + receiverBean.getMemeberid() + "')");
                        getAdapter().setMaxCount(Integer.parseInt(maxCount + ""));
                        if (!isFinishing()) {
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
    protected void onBeforeLoad(Runnable runnable) {
        runnable.run();
    }

    @Override
    public void cancelInformation() {
        IMWindosThreadUtil.getInstances().run(getKey(), new Runnable() {
            @Override
            public void run() {
                Access.runCustomThread(new Execute() {
                    @Override
                    public void onExecute(SQLiteDatabase sqLiteDatabase) throws Exception {
                        InformationModel informationModel = Business.getInstances().queryLineByWhere(sqLiteDatabase,
                                InformationModel.class,
                                String.format("%s = ? AND %s = ? AND %s = ?",
                                        Fields.InformationFields.MEMBER_ID,
                                        Fields.InformationFields.OPPOSITE_ID,
                                        Fields.InformationFields.TYPE),
                                new String[]{Proxy.getAccountManger().getUserMemberId(), getReceiver(), InformationModel.CHAT + ""});
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

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if(resultCode == RESULT_OK){
//            switch (requestCode){
//                case 100:
//                    Uri selectedImage = data.getData();
//                    String[] filePathColumn = { MediaStore.Images.Media.DATA };
//                    Cursor cursor = getContentResolver().query(selectedImage,
//                            filePathColumn, null, null, null);
//                    cursor.moveToFirst();
//                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
//                    final String picturePath = cursor.getString(columnIndex);
//                    cursor.close();
//                    showTipToast(picturePath);
//                    RosterEntry r = Proxy.getAccountManger().getFriend(receiverBean.getMemeberid());
//                    if(r == null){
//                        showTipToast("你们还不是好友关系");
//                        return;
//                    }
//                    FileTransferImp.getInstances().sendFileChat(r, picturePath, new IFileTransfer.FileObserver() {
//                        @Override
//                        public void onStart(File file) {
//                            log("-------------file = "+file.getAbsolutePath());
//                            showTipToast(" 准备发送  "+file.getAbsolutePath());
//                        }

//
//                        @Override
//                        public void progress(final Double d) {
//                            runOnUiThread(new Runnable() {
//                                @Override
//                                public void run() {
//                                    showTipToast(" 已发送 "+d);
//                                }
//                            });
//                        }
//
//                        @Override
//                        public void complete() {
//                            showTipToast(" 发送完毕 ");
//                        }
//
//                        @Override
//                        public void onError(Exception e) {
//                            showTipToast(e.getMessage());
//                        }
//                    });
////                    IMWindosThreadUtil.getInstances().run(getKey(), new Runnable() {
////                        @Override
////                        public void run() {
////
////                        }
////                    });
//
//                    break;
//            }
//
//        }
//    }

    @Override
    protected int getContentViewByInt() {
        return R.layout.activity_chat_recycler;
    }
}
