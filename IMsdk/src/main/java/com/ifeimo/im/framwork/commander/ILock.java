package com.ifeimo.im.framwork.commander;

/**
 * Created by lpds on 2017/6/5.
 */
public interface ILock {

    int USER_LOCK = 1024;
    int ACCESS_LOCK = 16;
    int MESSAGE_LOCK = 8;
    int MANGER_LIST_LOCK = 4;
    int getLockType();
}
