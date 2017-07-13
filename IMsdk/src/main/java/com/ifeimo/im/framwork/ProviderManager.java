package com.ifeimo.im.framwork;

import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;

import com.ifeimo.im.common.bean.model.InformationModel;
import com.ifeimo.im.framwork.commander.IProvider;
import com.ifeimo.im.provider.business.ChatBusiness;
import com.ifeimo.im.provider.business.GroupChatBusiness;
import com.ifeimo.im.provider.business.IInfortmationBusiness;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import y.com.sqlitesdk.framework.db.Access;
import y.com.sqlitesdk.framework.entity.respone.QueryRespone;
import y.com.sqlitesdk.framework.sqliteinterface.Execute;

/**
 * Created by lpds on 2017/1/24.
 *此类作用-------------
 * 处理一些 其他的数据的手尾。
 */
final class ProviderManager implements IProvider,Runnable{
    private final String TAG = "XMPP_IProvider";
    private final static ProviderManager providerManager;
    private final List<InformationModel> requestConnectInformationModel = new ArrayList<InformationModel>();
    private HandlerThread handlerThread = new HandlerThread("ProviderManager");
    private Handler executor = null;

    static {
        providerManager = new ProviderManager();
    }

    private ProviderManager() {
        handlerThread.start();
        executor = new Handler(handlerThread.getLooper());
        EventBus.getDefault().register(this);
        ManagerList.getInstances().addManager(this);
        IMConnectManager.getInstances().addMemberInfoObserver(this);
    }

    public static IProvider getInstances() {
        return providerManager;
    }


    @Override
    public boolean isInitialized() {
        return true;
    }

    @Override
    public void update() {
        action1();
        ChatBusiness.getInstances().modifyErrorSituation();
        GroupChatBusiness.getInstances().modifyErrorSituation();
    }

    @Override
    public void leaveIM() {
        shutdown();
    }

    @Override
    public void leaveErrorIM() {
        shutdown();
    }

    private void shutdown(){
        Log.i(TAG, "shutdown: IProvider out ");
        executor.removeCallbacks(ProviderManager.this);
        requestConnectInformationModel.clear();
        Proxy.getMessageManager().releaseAllChat();

    }

    /**
     * 获取所有房间进入
     */
    private void action1() {

        Access.run(new Execute() {
            @Override
            public void onExecute(SQLiteDatabase sqLiteDatabase) throws Exception {
                List<InformationModel> informationModels =
                        GroupChatBusiness.getInstances().
                                getAllSubscriptionByType(sqLiteDatabase, InformationModel.ROOM);
                QueryRespone<InformationModel> queryRespone = new QueryRespone(informationModels, true, null);
                Log.i(TAG, "onExecute: User will join room count is  "+queryRespone.getData().size());
                EventBus.getDefault().post(queryRespone);
            }

            @Override
            public void onExternalError() {

            }
        });

    }

    @Subscribe(threadMode = org.greenrobot.eventbus.ThreadMode.BACKGROUND)
    public void onMessage(QueryRespone<InformationModel> queryRespone) {
        if (queryRespone.getData() != null) {
            synchronized (providerManager) {
                requestConnectInformationModel.clear();
                requestConnectInformationModel.addAll(queryRespone.getData());
            }
            joinRoom();
        }
    }

    private void joinRoom() {
        if (requestConnectInformationModel.size() == 0) {
            return;
        }
        Log.i(TAG, "joinRoom: 进入房间");
        executor.post(ProviderManager.this);
    }

    @Override
    public void run() {
        synchronized (providerManager) {
            for (InformationModel informationModel : requestConnectInformationModel) {
                MessageManager.getInstances().createGruopChat(informationModel.getOppositeId());
            }
        }
    }

    @Override
    public void onSucceed() {
        Log.i(TAG, "------------ sysj member info had Submitted successfully ------------");
    }

    @Override
    public void onError(Exception e) {
        Log.i(TAG, "------------ sysj member info had Submitted error ------------");
    }
}
