package com.ifeimo.im.framwork.commander;

/**
 *
 * 生命周期管理接口
 * Created by lpds on 2017/1/19.
 */
public interface ILife {

    void onCreate(IMMain imMain);
    void onResume(IMMain imMain);
    void onDestroy(IMMain imMain);
    void onPause(IMMain imMain);
    void onStop(IMMain imMain);

}
