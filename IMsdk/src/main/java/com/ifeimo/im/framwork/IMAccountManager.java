package com.ifeimo.im.framwork;

import com.ifeimo.im.common.bean.AccountBean;
import com.ifeimo.im.common.util.Jid;
import com.ifeimo.im.framwork.interface_im.IAccount;

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
 */
final class IMAccountManager implements IAccount {

    private static IMAccountManager accountManager;
    private boolean isinit = false;
    private Roster roster;
    private RosterEntries rosterEntries;
    private Set<RosterEntry> mSetEntry;

    public String MYFRIEND = "我的好友";

    static {
        accountManager = new IMAccountManager();
    }

    private IMAccountManager() {
        listBeans = new ArrayList<>();
        ManagerList.getInstances().addManager(this);
    }

    List<AccountBean> listBeans;

    public static IMAccountManager getInstances() {
        return accountManager;
    }

    private void init() {
        roster = Roster.getInstanceFor(IMConnectManager.getInstances().getConnection());
        if (mSetEntry == null) {
            mSetEntry = new HashSet<>();
        } else {
            mSetEntry.clear();
        }
        mSetEntry.addAll(roster.getEntries());
        isinit = true;
    }

    public Set<RosterEntry> getAllFriend() {
        Set set = new HashSet();
        set.addAll(mSetEntry);
        return set;
    }

    public void addFriend(AccountBean accountBean) {
        try {


            roster.createEntry(
                    Jid.getJid(IMSdk.CONTEXT, accountBean.getMemeberid()),
                    accountBean.getNickName(),
                    null
            );


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

    @Override
    public void entriesAdded(Collection<String> addresses) {

    }

    @Override
    public void entriesUpdated(Collection<String> addresses) {

    }

    @Override
    public void entriesDeleted(Collection<String> addresses) {

    }

    @Override
    public void presenceChanged(Presence presence) {

    }

    @Override
    public void onRosterLoaded(Roster roster) {

    }
}
