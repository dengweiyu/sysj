package com.ifeimo.im.framwork.interface_im;

import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;

/**
 * Created by lpds on 2017/1/25.
 */
public interface Join {

    void join();

    void leave();

    void reJoin();

}
