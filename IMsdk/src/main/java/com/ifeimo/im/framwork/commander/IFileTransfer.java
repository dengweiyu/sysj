package com.ifeimo.im.framwork.commander;

import org.jivesoftware.smack.roster.RosterEntry;

import java.io.File;

/**
 * Created by lpds on 2017/6/20.
 */
public interface IFileTransfer {

    int COUNT_SLOW = 3;
    int COUNT_DEFAULT = 6;
    int COUNT_QUICK = 12;

    void setMaxFileCount(int i);

    void sendFileRoom(RosterEntry rosterEntry,String path,FileObserver fileObserver);
    void sendFileChat(RosterEntry rosterEntry,String path,FileObserver fileObserver);
    interface FileObserver {
        void onStart(File file);

        void  progress(Double d);

        void complete();

        void onError(Exception e);
    }
}
