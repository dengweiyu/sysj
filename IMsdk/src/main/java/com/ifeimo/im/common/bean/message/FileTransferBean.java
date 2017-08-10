package com.ifeimo.im.common.bean.message;

import org.jivesoftware.smackx.filetransfer.OutgoingFileTransfer;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by lpds on 2017/6/20.
 */
public class FileTransferBean {

    public static final int TYPE_PIC = 0x2;
    public static final int TYPE_OTHER = 0x4;
    public static final int TYPE_MUSIC = 0x8;
    public static final int TYPE_VIDEO = 0x10;


    protected OutgoingFileTransfer outgoingFileTransfer;
    protected Map<String, Tiny> folder;

    public OutgoingFileTransfer getOutgoingFileTransfer() {
        return outgoingFileTransfer;
    }

    public FileTransferBean(OutgoingFileTransfer outgoingFileTransfer) {
        this.folder = new HashMap<>();
        this.outgoingFileTransfer = outgoingFileTransfer;
    }

    public Map<String, Tiny> getFolder() {
        return folder;
    }

    public Tiny getTiny(String messageid){
        return folder.get(messageid);
    }

    public static final class Tiny{
        Runnable runnable1;
        String id;
        String path;
        long startTime = -1;
        public Tiny(Runnable runnable1, String id, String path) {
            this.runnable1 = runnable1;
            this.id = id;
            this.path = path;
        }

        public long getStartTime() {
            return startTime;
        }

        public void setStartTime(long startTime) {
            this.startTime = startTime;
        }

        public Runnable getRunnable1() {
            return runnable1;
        }

        public void setRunnable1(Runnable runnable1) {
            this.runnable1 = runnable1;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }
    }

}
