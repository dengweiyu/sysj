package com.ifeimo.im.framwork.connect;

/**
 * Created by lpds on 2017/6/19.
 * 此观察者会在用户提交用户信息大到im之后回调
 */
public interface MemberInfoObserver {

    void onSucceed();

    void onError(Exception e);

}
