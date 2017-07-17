package com.ifeimo.im.framwork.commander;

/**
 * Created by lpds on 2017/6/5.
 */
public interface ILockManager {

    void saveLock(int key,ILock o);

    ILock getLock(int key);

    Object getLockByType(int type);
}
