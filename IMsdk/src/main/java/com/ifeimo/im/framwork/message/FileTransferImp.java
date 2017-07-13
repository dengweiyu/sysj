package com.ifeimo.im.framwork.message;

import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;

import com.ifeimo.im.IEmployee;
import com.ifeimo.im.OnOutIM;
import com.ifeimo.im.OnUpdate;
import com.ifeimo.im.common.bean.message.FileTransferBean;
import com.ifeimo.im.framwork.Proxy;
import com.ifeimo.im.framwork.commander.IFileTransfer;

import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.roster.RosterEntry;
import org.jivesoftware.smackx.filetransfer.FileTransfer;
import org.jivesoftware.smackx.filetransfer.FileTransferListener;
import org.jivesoftware.smackx.filetransfer.FileTransferManager;
import org.jivesoftware.smackx.filetransfer.FileTransferRequest;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * 文件发送，提供类
 * Created by lpds on 2017/6/20.
 */
public class FileTransferImp implements OnUpdate, IFileTransfer, OnOutIM, FileTransferListener, IEmployee {
    private static FileTransferImp fileTransferImp;

    private static final String TAG = "XMPP_FileTransferImp";


    static {
        fileTransferImp = new FileTransferImp();
    }

    private FileTransferManager fileTransferManager;
    /**
     * 房间的发送集合
     */
    private Map<String, FileTransferBean> roomFileFolders;
    /**
     * 单聊的发送集合
     */
    private Map<String, FileTransferBean> chatFileFolders;
    /**
     * 线程轮训
     */
    private HandlerThread handlerThread;
    private Handler handler;

    private FileTransferImp() {
        handlerThread = new HandlerThread("FileTransferImp");
        handlerThread.start();
        handler = new Handler(handlerThread.getLooper());
        roomFileFolders = new HashMap<>();
        chatFileFolders = new HashMap<>();
        Proxy.getManagerList().addManager(this);
    }

    public static IFileTransfer getInstances() {
        return fileTransferImp;
    }

    /**
     * 向房间发送文件
     * @param rosterEntry
     * @param path
     * @param fileObserver
     */
    @Override
    public void sendFileRoom(RosterEntry rosterEntry, String path, FileObserver fileObserver) {

    }

    /**
     * 向个人发送图片
     * @param rosterEntry
     * @param path
     * @param fileObserver
     */
    @Override
    public void sendFileChat(final RosterEntry rosterEntry, final String path, final FileObserver fileObserver) {
        final File file = new File(path);
        if (!file.exists()) {
            return;
        }
        fileObserver.onStart(file);
        final FileTransferBean fileTransferBean = createByChat(rosterEntry,file);
        final Presence presence = new Presence(Presence.Type.available);
        final FileTransferBean.Tiny tiny = new FileTransferBean.Tiny(null, presence.getStanzaId(), file.getAbsolutePath());
        try {
            fileTransferBean.getOutgoingFileTransfer().sendFile(file, presence.getStanzaId());
            tiny.setRunnable1(new Runnable() {
                @Override
                public void run() {
                    while (!fileTransferBean.getOutgoingFileTransfer().isDone()) {
                        if (fileTransferBean.getOutgoingFileTransfer().getStatus().equals(FileTransfer.Status.error)) {
                            Log.i(TAG, "---------------- " + "error!!!" + fileTransferBean.getOutgoingFileTransfer().getError() + " -----------------");
                        } else {
                            double progress = fileTransferBean.getOutgoingFileTransfer().getProgress();
                            if (progress > 0.0 && tiny.getStartTime() == -1) {
                                tiny.setStartTime(System.currentTimeMillis());
                            }
                            fileObserver.progress(progress *= 100f);
                            Log.i(TAG, "---------------- progress = " + progress + " ------------------\n" +
                                    "status=" + fileTransferBean.getOutgoingFileTransfer().getStatus());
                        }
                    }
                    fileObserver.complete();
                }

            });
        } catch (SmackException e) {
            e.printStackTrace();
            fileObserver.onError(e);
        }

        fileTransferBean.getFolder().put(presence.getStanzaId(), tiny);
        handler.post(tiny.getRunnable1());
    }

    /**
     * 创建一个 接收人员文件实体类
     * @param rosterEntry
     * @param file
     * @return
     */
    private FileTransferBean createByChat(RosterEntry rosterEntry,File file) {
        if (chatFileFolders.containsKey(rosterEntry.getUser())) {
            return chatFileFolders.get(rosterEntry.getUser());
        } else {
            FileTransferBean fileTransferBean = new FileTransferBean(fileTransferManager.createOutgoingFileTransfer(rosterEntry.getUser()+"/"+file.getName()));
            chatFileFolders.put(rosterEntry.getUser(), fileTransferBean);
            return fileTransferBean;
        }
    }


    @Override
    public void update() {
        fileTransferManager = FileTransferManager.getInstanceFor(Proxy.getConnectManager().getConnection());
        fileTransferManager.addFileTransferListener(this);
    }

    @Override
    public void setMaxFileCount(int i) {

    }


    @Override
    public void leaveIM() {
        handler.removeCallbacksAndMessages(null);
        chatFileFolders.clear();
        fileTransferManager = null;
    }

    @Override
    public void leaveErrorIM() {
        handler.removeCallbacksAndMessages(null);
        chatFileFolders.clear();
        fileTransferManager = null;
    }

    @Override
    public void fileTransferRequest(FileTransferRequest request) {

        String filename = request.getFileName();
        long fileleng = request.getFileSize();
        Log.i(TAG, "---------------  Receive File ---------------\n fileName = "+filename+"  fileLeng = "+fileleng);


        try {
            request.reject();
        } catch (SmackException.NotConnectedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean isInitialized() {
        return true;
    }
}
