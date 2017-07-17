package com.ifeimo.im.framwork.message;

import com.ifeimo.im.common.bean.xml.PresenceList;
import com.ifeimo.im.framwork.commander.PresenceObserver;

import org.jivesoftware.smack.packet.Presence;

/**
 * Created by lpds on 2017/6/16.
 */
public interface PresenceOperate {

    void modiPresenceStatus(Presence.Mode t);

    PresenceList.Presence getSeftPresence();


    PresenceMessageManager.Config getConfig();

}
