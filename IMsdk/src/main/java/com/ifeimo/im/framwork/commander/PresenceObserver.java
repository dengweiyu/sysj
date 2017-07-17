package com.ifeimo.im.framwork.commander;

import org.jivesoftware.smack.packet.Presence;

/**
 * Created by lpds on 2017/6/15.
 */
public interface PresenceObserver extends HandlerMessageLeader{

    void onMessage(Presence stanza);

}
