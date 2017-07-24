package com.ifeimo.im.framwork.commander;

import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.packet.Stanza;

/**
 * Created by lpds on 2017/6/15.
 *
 * 继承此类的子类，再被managerList 添加之后
 *
 */
public interface HandlerMessageLeader<T extends Stanza>{
    void onMessage(T stanza);
}
