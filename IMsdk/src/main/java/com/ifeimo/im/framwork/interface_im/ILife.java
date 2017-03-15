package com.ifeimo.im.framwork.interface_im;

/**
 * Created by lpds on 2017/1/19.
 */
public interface ILife {

    void onCreate(IMMain imMain);
    void onResume(IMMain imMain);
    void onDestroy(IMMain imMain);
    void onPause(IMMain imMain);
    void onStop(IMMain imMain);

}
