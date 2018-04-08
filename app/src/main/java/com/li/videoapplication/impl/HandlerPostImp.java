package com.li.videoapplication.impl;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.li.videoapplication.tools.StatisticsOpen;
import com.li.videoapplication.interfaces.ICell;
import com.li.videoapplication.interfaces.IHandlerPost;

/**
 * Created by linhui on 2018/4/2.
 */
class HandlerPostImp implements IHandlerPost {

    private Handler handler = null;

    HandlerPostImp() {

        new Thread() {
            @Override
            public void run() {
                Looper.prepare();
                handler = new Handler();
                Looper.loop();
            }
        }.start();

    }


    @Override
    public void handlerSum(final ICell cell) {
        if (handler != null) {
            final long sum = cell.getSumCount();
            handler.post(new Runnable() {
                @Override
                public void run() {
                    /**
                     * 处理数据库插入
                     */
                    Log.i(StatisticsOpen.TAG, "run: " + sum );
                }
            });
        }
    }

    @Override
    public Handler getHandler() {
        return handler;
    }


}
