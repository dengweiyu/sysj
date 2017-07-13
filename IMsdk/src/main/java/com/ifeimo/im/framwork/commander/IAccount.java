package com.ifeimo.im.framwork.commander;

import com.ifeimo.im.IEmployee;
import com.ifeimo.im.OnUpdate;
import com.ifeimo.im.common.bean.AccountBean;

import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.roster.RosterEntry;
import org.jivesoftware.smack.roster.RosterListener;
import org.jivesoftware.smack.roster.RosterLoadedListener;

import java.util.Set;

/**
 * 用户管理接口
 * Created by lpds on 2017/1/17.
 */
public interface IAccount extends OnUpdate, IEmployee,RosterLoadedListener,RosterListener{

    /**
     * 获取所有好友
     * @return
     */
    Set<RosterEntry> getAllFriend();

    /**
     * 添加好友
     * @param accountBean
     */
    void addFriend(AccountBean accountBean);

    /**
     * 删除好友
     * @param rosterEntry
     */
    void deleteFriend(RosterEntry rosterEntry);

    void setAccountState(Presence.Mode m);

    RosterEntry getFriend(String memberid);
}
