package com.ifeimo.im.framwork.notification;

import android.content.Intent;

import java.io.Serializable;

/**
 * Created by lpds on 2017/1/16.
 */
public class NotifyBean implements NotificationItem,Serializable {

    private int id;
    private Intent intent;
    private String title;
    private String text;
    private String content;

    public void setId(int id) {
        this.id = id;
    }

    public void setIntent(Intent intent) {
        this.intent = intent;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Intent getIntent() {
        return intent;
    }

    public String getTitle() {
        return title;
    }

    public String getText() {
        return text;
    }

    public int getId() {
        return id;
    }

    public String getContent() {
        return content;
    }

}
