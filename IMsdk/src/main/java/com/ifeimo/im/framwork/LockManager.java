package com.ifeimo.im.framwork;

import com.ifeimo.im.IEmployee;
import com.ifeimo.im.framwork.interface_im.ILock;
import com.ifeimo.im.framwork.interface_im.ILockManager;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;

/**
 * Created by lpds on 2017/6/5.
 */
class LockManager implements ILockManager,IEmployee{


    private LockManager(){
        init();
    }

    private static LockManager lockManager;
    static {
        lockManager = new LockManager();
    }

    private Map<Integer,ILock> locks;

    public static ILockManager getInstances() {
        return lockManager;
    }

    @Override
    public void saveLock(int key, ILock o) {
        locks.put(key,o);
    }

    @Override
    public ILock getLock(int key) {
        return  locks.get(key);
    }

    @Override
    public Object getLockByType(int type) {
        return null;
    }

    private void init() {

        locks = new HashMap<>();
        ILock userLock = new ILock() {
            @Override
            public int getLockType() {
                return ILock.USER_LOCK;
            }
        };
        ILock messageLock = new ILock() {
            @Override
            public int getLockType() {
                return ILock.MESSAGE_LOCK;
            }
        };
        locks.put(messageLock.getLockType(),messageLock);
        locks.put(userLock.getLockType(),userLock);
    }

    @Override
    public boolean isInitialized() {
        return true;
    }
}
