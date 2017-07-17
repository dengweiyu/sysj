package com.ifeimo.im.framwork.commander;

import android.content.Context;

/**
 * Created by lpds on 2017/1/24.
 */
public interface IMMain {

    /**
     * 当前上下文
     * @return
     */
    Context getContext();

    IMWindow getIMWindow();

    void runOnUiThread(Runnable runnable);

}
