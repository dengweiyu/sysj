package com.ifeimo.im.activity;


import android.app.LoaderManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Loader;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ifeimo.im.R;
import com.ifeimo.im.common.adapter.BaseChatReCursorAdapter;
import com.ifeimo.im.common.adapter.ChatReAdapter;
import com.ifeimo.im.common.adapter.MuccChatReAdapter;
import com.ifeimo.im.common.adapter.holder.Holder;
import com.ifeimo.im.common.bean.model.AccountModel;
import com.ifeimo.im.common.util.IMWindosThreadUtil;
import com.ifeimo.im.common.util.PManager;
import com.ifeimo.im.common.util.ScreenUtil;
import com.ifeimo.im.common.util.StatusBarBlackTextHelper;
import com.ifeimo.im.common.util.WindowUtil;
import com.ifeimo.im.framwork.Proxy;
import com.ifeimo.im.framwork.database.iduq.OnCursorDataChange;
import com.ifeimo.im.framwork.commander.IMWindow;
import com.ifeimo.im.framwork.view.IMRecycleView;
import com.ifeimo.im.framwork.view.OnItemShowListener;
import com.ifeimo.im.provider.ChatProvider;
import com.ifeimo.im.provider.GroupChatProvider;


import org.greenrobot.eventbus.Subscribe;

import y.com.sqlitesdk.framework.interface_model.IModel;

/**
 * Created by lpds on 2017/1/9.
 */
abstract class BaseIMCompatActivity
        <E extends IModel<E>,T extends BaseChatReCursorAdapter<Holder,E>>
        extends SuperActivity
        implements  IMWindow, OnCursorDataChange, View.OnLayoutChangeListener,
                    LoaderManager.LoaderCallbacks<Cursor>, OnItemShowListener {
    protected static final String TGA = "XMPP_Activity";
    private final String tip1 = this.getClass().getSimpleName() + " 登陆成功 ";
    /**
     * 文字编辑框
     */
    protected EditText editeMsg;
    /**
     * 头文字
     */
    protected TextView title;
    protected TextView id_top_right_tv;
    protected ImageView id_top_right_iv;
    protected ViewGroup id_top_right_layout;
    protected TextView sendBtn;
    protected com.ifeimo.im.framwork.view.RecyclerViewHeader RecyclerViewHeader;
    protected View doBack;
    protected View id_more_ProgressBar;
    protected Handler handler = new Handler();
    protected Loader loader;


    protected boolean DEBUG = false;

    /**
     * 界面的父级view
     */
    protected ViewGroup parentLayout;
    /**
     * 判断是否第一次加载消息
     */
    private boolean isFirstLoadDate = true;
    /**
     * 当前的行数
     */
    private int lastCount = -1;
    private BaseChatReCursorAdapter adapter;
    private IMRecycleView recyclerViewParentView;
    private Toast toast;
    private int loadId = 1;
    private int tempCount = -1;

    @Override
    protected final void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarBlackTextHelper.initStatusBarTextColor(getWindow(),true);
        loadCaheUser();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        setContentView(getContentViewByInt());
        init(savedInstanceState);
        onBeforeLoad(new Runnable() {
            @Override
            public void run() {
                laterInit();
            }
        });
        Proxy.getManagerList().onCreate(this);
        if(DEBUG) {
            debug();
        }
    }

    /**
     * 此方法只会在debug标志下控制执行
     */
    protected void debug() {}

    protected void laterInit() {
        if (loader != null) {
            loader.stopLoading();
            loader.cancelLoad();
            loader = null;
        }
        getMaxMsgCount(new Runnable() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        loadChatData(loadId++);
                    }
                });
            }
        });
    }

    private void loadMaxCount() {
        getMaxMsgCount(new Runnable() {
            @Override
            public void run() {
                log(" ---- 最大页数 " + adapter.getMaxPage() + " -----");
            }
        });
    }

    protected abstract void init(Bundle savedInstanceState);

    @Override
    public final void setContentView(View view) {
        super.setContentView(view);
    }

    @Override
    public final void setContentView(View view, ViewGroup.LayoutParams params) {
        super.setContentView(view, params);
    }

    @Override
    public final void setContentView(@LayoutRes int layoutResID) {
        super.setContentView(layoutResID);
        parentLayout = (ViewGroup) findViewById(R.id.parentLayout);
        if (parentLayout != null) {
            listenSoft(parentLayout);
        }
        RecyclerViewHeader = (com.ifeimo.im.framwork.view.RecyclerViewHeader) findViewById(R.id.id_RecyclerViewHeader);
        id_more_ProgressBar = findViewById(R.id.id_more_ProgressBar);
        recyclerViewParentView = (IMRecycleView) findViewById(R.id.id_RecyclerView);
        recyclerViewParentView.init(getAdapter(),id_more_ProgressBar);
        RecyclerViewHeader.attachTo(recyclerViewParentView);
        getAdapter().setHeadView(id_more_ProgressBar);
        recyclerViewParentView.setOnItemShowListener(this);
        editeMsg = (EditText) findViewById(R.id.et_chat_message);
        sendBtn = (TextView) findViewById(R.id.btn_chat_send_txt);
        title = (TextView) findViewById(R.id.conversation_title);
        doBack = findViewById(R.id.id_back);
        id_top_right_tv = (TextView) findViewById(R.id.id_top_right_tv);
        id_top_right_iv = (ImageView) findViewById(R.id.id_top_right_iv);
        id_top_right_layout = (ViewGroup) findViewById(R.id.id_top_right_layout);
        doBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    @Override
    public RecyclerView getMsgListView() {
        return (RecyclerView) recyclerViewParentView;
    }

    @Override
    protected void onResume() {
        super.onResume();
//        loaderResume();
        Proxy.getManagerList().onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
//        loaderPause();
        Proxy.getManagerList().onPause(this);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        loadCaheUser();
    }

    @Override
    protected void onStop() {
        super.onStop();
        Proxy.getManagerList().onStop(this);
    }

    @Subscribe
    public void nullE(AccountModel userBean){

    }

    @Override
    protected void onDestroy() {

        IMWindosThreadUtil.getInstances().leaveThreadPool(getKey(),false);
        RecyclerViewHeader.detach();
        Proxy.getManagerList().onDestroy(this);
        super.onDestroy();
    }

    @Override
    public void finish() {
        WindowUtil.setHideSoft(this);
        super.finish();
    }

    @Override
    public void showTipToast(String msg) {
        if (toast == null) {
            toast = Toast.makeText(this, msg, Toast.LENGTH_SHORT);
        }
        toast.setText(msg);
        toast.show();
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public boolean isFinish() {
        return isFinishing();
    }

    @Override
    public int getType() {
        return NULL_TYPE;
    }

    @Override
    public void loginSucceed() {
        log(tip1);
    }

    @Override
    public void finishing() {
        finish();
    }
//
//    @Override
//    public String getRoomId() {
//        return null;
//    }

    @Override
    public String getReceiver() {
        return null;
    }

    protected boolean isHadIMWindow() {
        for (IMWindow windows : Proxy.getIMWindowManager().getAllIMWindows()) {

            if (this == windows) {
                return true;
            }

        }
        return false;
    }

    @Override
    public void loadCaheUser() {
        if (Proxy.getAccountManger().isUserNull()) {
            PManager.getCacheUser(getContext());
        }
    }

    public void log(String msg) {
        Log.i(TGA, msg);
    }

    @Override
    public void clearAllNotify() {
        Proxy.getIMNotificationManager().canClearNotifications();
    }

    public void loadChatData(int id) {
        if(!isFinishing()) {
            loader = getLoaderManager().initLoader(id, null, this);
        }
    }

    private void onChangeDate(Cursor cursor) {
        if (isFirstLoadDate) {
            isFirstLoadDate = !isFirstLoadDate;
            onFirstDataChange(cursor);
        }
        onCursorDataChange(cursor);
    }


    protected void listenSoft(View view) {
        view.addOnLayoutChangeListener(this);
    }

    @Override
    public void onLayoutChange(View view, int left, int top, int right, int bottom,
                               int oldLeft, int oldTop, int oldRight, int oldBottom) {
        Log.i("onLayoutChange", " bottom = " + bottom + "    oldBottom = " + oldBottom);
        if (bottom == oldBottom) {
            return;
        }
        if (bottom - oldBottom < ScreenUtil.getScreenHeight(this) / 3) {
            //弹起
            if (getMsgListView() != null) {
                RecyclerView.Adapter baseAdapter = getMsgListView().getAdapter();
                if (baseAdapter != null) {
                    recyclerViewParentView.scrollToPosition();
                }
            }

        }

    }

    protected T getAdapter() {
        if (adapter != null) {
            return (T) adapter;
        }
        switch (getType()) {
            case IMWindow.CHAT_TYPE:
                adapter = new ChatReAdapter(this, null, 1);
                break;
            case IMWindow.MUCCHAT_TYPE:
                adapter = new MuccChatReAdapter(this, null, 1);
                break;
            default:
                return null;
        }
        return (T) adapter;


    }

    protected abstract void getMaxMsgCount(Runnable runnable);

    @Override
    public Loader onCreateLoader(int i, Bundle bundle) {
        Log.i(TGA, " ------- 加载消息 --------");
        switch (getType()) {
            case IMWindow.CHAT_TYPE:
                return new CursorLoader(this, ChatProvider.CONTENT_URI, null, adapter.getPage() + "",
                        new String[]{Proxy.getAccountManger().getUserMemberId(), getReceiver()
                        }, null);
            case IMWindow.MUCCHAT_TYPE:
                return new CursorLoader(this, GroupChatProvider.CONTENT_URI, null, adapter.getPage() + "",
                        new String[]{getKey()}, null);
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader loader, final Cursor cursor) {
        Log.i(TGA, " ------- 消息数量 " + cursor.getCount() + " --------");
        onChangeDate(cursor);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                adapter.changeCursor(cursor);
                final int temp = cursor.getCount();
                lastCount = temp;
                //如果有滚动位置 tempcount 不超过当前的数据量
                if (tempCount > 0 && tempCount <= adapter.getCursor().getCount()) {
                    //设置滚动位置
                    recyclerViewParentView.freedomScrollToPosition(tempCount - 1);
                    tempCount = -1;
                } else {
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            recyclerViewParentView.scrollToPosition(adapter.getItemCount() - 1);
                        }
                    }, 50);
                }
            }
        });
    }

    @Override
    public void onLoaderReset(Loader loader) {
        Log.i(TGA, " ------- 重置 --------");
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                adapter.changeCursor(null);
            }
        });
    }

    @Override
    public void onFirstDataChange(Cursor cursor) {

    }

    @Override
    public void onCursorDataChange(Cursor cursor) {
        if (adapter.getCursor() != null && adapter.isRefreshNow()) {
            tempCount = cursor.getCount() - adapter.getCursor().getCount() + recyclerViewParentView.getLinearLayoutManager().findLastCompletelyVisibleItemPosition();
            Log.e("1333", "cursor.getCount() = " + cursor.getCount() + "  adapter.getCursor().getCount() = " + adapter.getCursor().getCount());
            if (tempCount == 0) {
                tempCount = -1;
            }
        }
        loadMaxCount();
    }

    /**
     * 可以加载更多页的回调
     */
    @Override
    public void onFirstCompletelyVisibleItemPosition0() {
        if (adapter.getPage() <= adapter.getMaxPage() && adapter.getMaxPage() > 1) {
            adapter.setPage(adapter.getPage() + 1);
            loadChatData(++loadId);
        }
    }

    /**
     * 加载数据之前
     */
    protected abstract void onBeforeLoad(Runnable runnable);


    @Override
    public IMWindow getIMWindow() {
        return this;
    }
}
