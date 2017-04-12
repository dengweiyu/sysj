package com.ifeimo.im.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.AbsListView;

import com.ifeimo.im.common.bean.chat.BaseChatBean;
import com.ifeimo.im.framwork.Proxy;
import com.ifeimo.im.framwork.interface_im.IMMain;
import com.ifeimo.im.framwork.interface_im.IMWindow;

/**
 * Created by lpds on 2017/2/6.
 */
@Deprecated
public abstract class BaseActivity extends AppCompatActivity implements IMMain{

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Proxy.getManagerList().onCreate(this);
    }

    @Override
    public Context getContext() {
        return this;
    }
}
