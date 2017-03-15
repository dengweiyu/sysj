package com.ifeimo.im.framwork.interface_im;

import android.support.v7.widget.RecyclerView;
import android.widget.AbsListView;

import com.ifeimo.im.common.bean.chat.BaseChatBean;
import com.ifeimo.im.framwork.message.IInformation;
import com.ifeimo.im.framwork.notification.INotification;

/**
 * Created by lpds on 2017/1/10.
 */
public interface IMWindow extends IMMain,INotification,IInformation{

    //单聊
    int CHAT_TYPE = 2;
    //群聊
    int MUCCHAT_TYPE = 4;
    //无
    int NULL_TYPE = -1;

    /**
     * 关闭IM窗口
     */
    void close();

    /**
     * 是否已关闭
     * @return
     */
    boolean isFinish();

    /**
     * 当前窗口模式
     * @return
     */
    int getType();

    /**
     * 登陆成功
     */
    void loginSucceed();

    void finishing();

    /**
     * 获取聊天工具实体
     * @return
     */
    @Deprecated
    BaseChatBean getBean();

    String getKey();

    /**
     * 当前聊天房间，null为单聊
     * @return
     */
    String getRoomId();

    /**
     * 获取接收者，null为群聊
     * @return
     */
    String getReceiver();

    /**
     * 显示提示dialog
     * @param s
     */
    void showTipToast(String s);

    void loadCaheUser();

    void loadChatData(int id);

    void send(String content);

    RecyclerView getMsgListView();


}
