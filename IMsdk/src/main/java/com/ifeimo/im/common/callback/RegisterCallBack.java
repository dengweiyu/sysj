package com.ifeimo.im.common.callback;

/**
 * Created by lpds on 2017/1/6.
 */
@Deprecated
public interface RegisterCallBack {

    void registerSuccess();
    void registerFail();
    void hadAccount(String id);

}
