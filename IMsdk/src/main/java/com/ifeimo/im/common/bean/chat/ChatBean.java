package com.ifeimo.im.common.bean.chat;


import com.ifeimo.im.framwork.Proxy;

import org.jivesoftware.smack.chat.Chat;

/**
 * Created by lpds on 2017/1/11.
 * 单聊信息
 */
public class ChatBean extends BaseChatBean {
    private int id;
    private String account;
    private Chat chat;
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            releaseChat();
            Proxy.getMessageManager().removeChatSet(usert+account);
        }
    };

    public ChatBean(String account, String usert, Chat chat) {
        super(usert);
        this.id = (int) (Math.random()*10000000);
        this.account = account;
        this.usert = usert;
        this.chat = chat;
    }

    public String getAccount() {
        return account;
    }
    public void setAccount(String account) {
        this.account = account;
    }
    public Chat getChat() {
        return chat;
    }

    public void setChat(Chat chat) {
        this.chat = chat;
    }

    private void releaseChat(){
        if(chat != null){
            chat.close();
            chat = null;
        }
    }

    public Runnable getRunnable() {
        return runnable;
    }

    public int getId() {
        return id;
    }

    @Override
    public Object clone(){
        ChatBean chatBean = new ChatBean(account,usert,chat);
        return chatBean;
    }
}
