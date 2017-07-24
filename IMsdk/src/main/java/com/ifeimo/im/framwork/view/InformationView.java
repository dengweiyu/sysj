package com.ifeimo.im.framwork.view;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.ifeimo.im.OnInitialization;
import com.ifeimo.im.R;
import com.ifeimo.im.common.adapter.InformationAdapter;
import com.ifeimo.im.common.adapter.OnAdapterItemOnClickListener;
import com.ifeimo.im.common.adapter.base.RecyclerViewCursorAdapter;
import com.ifeimo.im.common.adapter.holder.InformationHolder;
import com.ifeimo.im.common.util.ThreadUtil;
import com.ifeimo.im.framwork.Proxy;
import com.ifeimo.im.framwork.database.Fields;
import com.ifeimo.im.provider.InformationProvide;

/**
 * Created by lpds on 2017/2/24.
 */
public class InformationView extends FrameLayout implements OnInitialization,LoaderManager.LoaderCallbacks<Cursor>{

    public static final int AUTO_MODE = 1000;
    public static final int GROUP_MODE = 1001;
    public static final int CHAT_MODE = 1002;

    private boolean isInit = false;
    private ViewGroup contentView;
    private RecyclerView informationRecycleView;
    private RecyclerViewCursorAdapter<InformationHolder> adapter;
    private Loader loader;
    private Support support;

    public InformationView(Context context) {
        super(context);
    }

    public InformationView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public InformationView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public synchronized void init() {
        if(!isInit) {

            contentView = (ViewGroup) LayoutInflater.from(getContext()).inflate(R.layout.information_layout, null);
            informationRecycleView = (RecyclerView) contentView.findViewById(R.id.com_im_id_main_list);
            informationRecycleView.setLayoutManager(new LinearLayoutManager(getContext()));
            informationRecycleView.setItemAnimator(new DefaultItemAnimator());
            informationRecycleView.setAdapter(adapter == null ? adapter = new InformationAdapter(getContext()) : adapter);
            addView(contentView);
            instancesLoad(1);
            isInit = true;
        }
    }

    private void instancesLoad(final int i) {
        stopLoader();
        if(!ThreadUtil.isMainThread()) {
            post(new Runnable() {
                @Override
                public void run() {
                    loader = ((Activity) getContext()).getLoaderManager().initLoader(i, null, InformationView.this);
                }
            });
        }else{
            loader = ((Activity) getContext()).getLoaderManager().initLoader(i, null, this);
        }
    }

    private void stopLoader(){
        if(loader != null){
            loader.stopLoading();
            loader.cancelLoad();
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        stopLoader();
        super.onDetachedFromWindow();
    }

    @Override
    public boolean isInitialized() {
        return isInit;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(getContext(), InformationProvide.CONTENT_URI,null,
                String.format("%s = ?", Fields.InformationFields.MEMBER_ID),new String[]{Proxy.getAccountManger().getUserMemberId()},null);
    }

    @Override
    public void onLoadFinished(android.content.Loader<Cursor> loader, Cursor cursor){
        adapter.changeCursor(cursor);
        if(support != null){
            support.messageCount(adapter.getItemCount());
        }
    }

    @Override
    public void onLoaderReset(android.content.Loader<Cursor> loader) {
        adapter.changeCursor(null);
    }

    /**
     * 没初始化无法此方法无效
     * @param onAdapterItemOnClickListener
     */
    public void setOnAdapterItemOnClickListener(OnAdapterItemOnClickListener onAdapterItemOnClickListener){
        if(isInit){
            RecyclerView.Adapter adapter = informationRecycleView.getAdapter();
            if(adapter instanceof InformationAdapter){
                ((InformationAdapter)adapter).setOnAdapterItemOnClickListener(onAdapterItemOnClickListener);
            }
        }

    }

    public void setSupport(Support support) {
        this.support = support;
    }

    public interface Support{
        void messageCount(int count);
    }

}
