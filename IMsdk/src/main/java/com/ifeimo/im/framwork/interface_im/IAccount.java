package com.ifeimo.im.framwork.interface_im;

import com.ifeimo.im.IEmployee;
import com.ifeimo.im.OnUpdate;

import org.jivesoftware.smack.roster.RosterEntry;
import org.jivesoftware.smack.roster.RosterListener;
import org.jivesoftware.smack.roster.RosterLoadedListener;

import java.util.Set;

/**
 * 用户管理接口
 * Created by lpds on 2017/1/17.
 */
public interface IAccount extends OnUpdate,
        IEmployee,RosterLoadedListener,RosterListener{


    Set<RosterEntry> getAllFriend();



}
