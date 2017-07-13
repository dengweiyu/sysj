package com.ifeimo.im.framwork.message;

import org.jivesoftware.smack.packet.IQ;

import java.util.Set;

/**
 * Created by lpds on 2017/6/22.
 */
public interface IQEnforce {

    int TIME_OUT = 5 * 1000;

    Set<IQAffirm> getAllAffirms();

    interface IQAffirm {
        void onAffirm(IQ iq);
    }
}
