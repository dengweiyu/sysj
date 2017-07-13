package com.ifeimo.im.framwork.message;

import android.util.Log;

import com.ifeimo.im.framwork.commander.IQObserver;

import org.jivesoftware.smack.packet.IQ;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by lpds on 2017/6/15.
 */
public final class IQMessageManager implements IQObserver{

    private static final String TAG = "XMPP_IQ";
    static IQMessageManager iqMessageManager;
    static {
        iqMessageManager = new IQMessageManager();
    }
    private IQEnforceImp iqEnforceImpl;

    private IQMessageManager(){
        iqEnforceImpl = new IQEnforceImp();
    }



    public static IQObserver getInstances(){
        return iqMessageManager;
    }

    @Override
    public void onMessage(IQ stanza) {
        Log.i(TAG, "onMessage: "+stanza);
        iqEnforceImpl.onMessage(stanza);
    }


}
