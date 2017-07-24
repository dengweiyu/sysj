package com.ifeimo.im.framwork;

import android.util.Log;

import com.ifeimo.im.common.bean.AccountBean;
import com.ifeimo.im.common.bean.model.AccountModel;
import com.ifeimo.im.common.util.Jid;
import com.ifeimo.im.common.util.StringUtil;
import com.ifeimo.im.framwork.commander.IAccount;
import com.ifeimo.im.framwork.message.PresenceMessageManager;
import com.ifeimo.im.framwork.message.PresenceOperate;

import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.roster.Roster;
import org.jivesoftware.smack.roster.RosterEntries;
import org.jivesoftware.smack.roster.RosterEntry;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by lpds on 2017/1/17.
 * <p>
 * 此类管理个人的一些状态
 * 接收好友的状态
 */
final class IMAccountManager implements IAccount {
    public static final String TAG = "XMPP_IMAccountManager";
    private static IMAccountManager accountManager;
    private boolean isinit = false;
    private Roster roster;
    private String MYFRIEND = "我的好友";

    private final AccountModel ACCOUNT = new AccountModel();
    private List<AccountBean> listBeans;

    static {
        accountManager = new IMAccountManager();
    }

    private IMAccountManager() {
        listBeans = new ArrayList<>();
        ManagerList.getInstances().addManager(this);
    }


    public static IMAccountManager getInstances() {
        return accountManager;
    }

    private void init() {
        if (roster != null) {
            roster.removeRosterListener(this);
            roster.removeRosterLoadedListener(this);
        }
        roster = Roster.getInstanceFor(IMConnectManager.getInstances().getConnection());
        roster.addRosterLoadedListener(this);
        roster.addRosterListener(this);
        if (roster.getGroupCount() < 1) {
            roster.createGroup(MYFRIEND);
        }
        isinit = true;
    }

    @Override
    public Set<RosterEntry> getAllFriend() {
        Set<RosterEntry> set = new HashSet<RosterEntry>();
        set.addAll(roster.getEntries());
        itemType(set);
        return set;
    }

    private void itemType(Set<RosterEntry> set) {
        for (RosterEntry rosterEntry : set) {
            Log.d(TAG, "\nSMACK ***********************************");
            Log.d(TAG, "SMACK User = " + rosterEntry.getUser() + " name = " + rosterEntry.getName());
            if (rosterEntry.getStatus() != null) {
                Log.d(TAG, "SMACK ItemStatus , name = " + rosterEntry.getStatus().name() + " ordinal = " + rosterEntry.getStatus().ordinal());
            }
            if (rosterEntry.getType() != null) {
                switch (rosterEntry.getType()) {
                    case none:
                        Log.d(TAG, "SMACK ItemType , name = " + rosterEntry.getType().name() + "(被取消订阅,不存在关系) ， ordinal = " + rosterEntry.getType().ordinal());
                        break;
                    case to:
                        Log.d(TAG, "SMACK ItemType , name = " + rosterEntry.getType().name() + "(订阅了好友，但是好友为订阅你) ， 1 ordinal = " + rosterEntry.getType().ordinal());
                        break;
                    case from:
                        Log.d(TAG, "SMACK ItemType , name = " + rosterEntry.getType().name() + "(好友订阅了你，你还未订阅好友) ， ordinal = " + rosterEntry.getType().ordinal());
                        break;
                    case remove:
                        Log.d(TAG, "SMACK ItemType , name = " + rosterEntry.getType().name() + "(不在给好友订阅的状态信息) ， ordinal = " + rosterEntry.getType().ordinal());
                        break;
                    case both:
                        Log.d(TAG, "SMACK ItemType , name = " + rosterEntry.getType().name() + "(相互订阅) ， ordinal = " + rosterEntry.getType().ordinal());
                        break;
                }

            }
            Log.d(TAG, "SMACK ***********************************\n");
        }
    }

    /**
     * 添加好友
     * @param accountBean
     */
    public void addFriend(AccountBean accountBean) {
        try {
            roster.createEntry(
                    Jid.getJid(IMSdk.CONTEXT, accountBean.getMemeberid()),
                    accountBean.getNickName(),
                    new String[]{MYFRIEND}
            );

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteFriend(RosterEntry rosterEntry) {

        try {
            roster.removeEntry(rosterEntry);
            Log.i(TAG, "deleteFriend: 删除用户 " + rosterEntry.getName() + "  memebrid = " + rosterEntry.getUser());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public boolean isInitialized() {
        return isinit;
    }

    @Override
    public void update() {
        init();
    }

    /**
     * 加添好友成功   memberid+@op.17sysj.com;
     *
     * @param addresses
     */
    @Override
    public void entriesAdded(Collection<String> addresses) {
        for (String s : addresses) {
            Log.i(TAG, "entriesAdded: " + s);
        }
    }

    /**
     * 好友列表更新（新增的时候）  memberid+@op.17sysj.com;
     *
     * @param addresses
     */
    @Override
    public void entriesUpdated(Collection<String> addresses) {
        for (String s : addresses) {
            Log.i(TAG, "entriesUpdated: " + s);
        }
    }

    /**
     * 删除好友 memberid+@op.17sysj.com;
     *
     * @param addresses
     */
    @Override
    public void entriesDeleted(Collection<String> addresses) {
        for (String s : addresses) {
            Log.i(TAG, "entriesDeleted: " + s);
        }
    }

    @Override
    public void presenceChanged(Presence presence) {
        Log.i(TAG, "presenceChanged: " + presence);
    }

    @Override
    public void onRosterLoaded(Roster roster) {
        Log.i(TAG, "onRosterLoaded: " + roster);
    }


//    public void get

    @Override
    public void setAccountState(Presence.Mode m) {
        PresenceMessageManager.getInstances().modiPresenceStatus(m);
    }

    @Override
    public AccountModel getAccount(boolean isClone) {
        synchronized (ACCOUNT) {
            if (isClone) {
                return ACCOUNT.clone();
            } else {
                return ACCOUNT;
            }
        }
    }

    @Override
    public void setAccount(AccountModel.Build build) {
        synchronized (ACCOUNT) {
            ACCOUNT.setBuild(build);
        }
    }

    @Override
    public String getUserMemberId() {
        synchronized (ACCOUNT) {
            return ACCOUNT.getMemberId();
        }
    }

    @Override
    public boolean isUserNull() {
        synchronized (ACCOUNT) {
            return StringUtil.isNull(ACCOUNT.getMemberId());
        }
    }

    @Override
    public void clearUserSelf() {
        AccountModel.Build build = new AccountModel.Build();
        this.setAccount(build);
    }

    @Override
    public RosterEntry getFriend(String memberid) {

        for (RosterEntry r : roster.getEntries()) {

            r.getUser().contains(memberid);

            return r;
        }

        return null;
    }

}
