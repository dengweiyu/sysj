package com.li.videoapplication.ui.activity;

import android.content.Intent;
import android.media.Image;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.google.gson.Gson;
import com.li.videoapplication.R;
import com.li.videoapplication.data.DataManager;
import com.li.videoapplication.data.image.GlideHelper;
import com.li.videoapplication.data.model.entity.OrderResultEntity;
import com.li.videoapplication.data.model.event.RefreshOrderDetailEvent;
import com.li.videoapplication.data.model.response.OrderResultCommitEntity;
import com.li.videoapplication.data.network.UITask;
import com.li.videoapplication.framework.TBaseAppCompatActivity;
import com.li.videoapplication.tools.ToastHelper;
import com.li.videoapplication.ui.adapter.OrderResultListAdapter;
import com.li.videoapplication.ui.dialog.LoadingDialog;
import com.li.videoapplication.ui.view.SpanItemDecoration;
import com.li.videoapplication.utils.ScreenUtil;
import com.li.videoapplication.utils.StringUtil;

import java.util.ArrayList;
import java.util.List;

import io.rong.eventbus.EventBus;

/**
 * 确认订单完成
 */

public class ConfirmOrderDoneActivity extends TBaseAppCompatActivity implements View.OnClickListener {

    private String mAvatar;
    private String mNickName;
    private String mOrderId;
    private String mOrderCount;
    private int mCount = 0;

    private RecyclerView mResultList;
    private OrderResultListAdapter mAdapter;

    private LoadingDialog mLoadingDialog;
    @Override
    public void refreshIntent() {
        super.refreshIntent();
        Intent intent = getIntent();
        mOrderId = intent.getStringExtra("order_id");
        mAvatar = intent.getStringExtra("avatar");
        mNickName = intent.getStringExtra("nick_name");
        String count = intent.getStringExtra("count");
        try {
            mCount = Integer.parseInt(count);
        } catch (Exception e) {
            e.printStackTrace();
        }
        mOrderCount = intent.getStringExtra("order_count");
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_confirm_order_done;
    }

    @Override
    public void afterOnCreate() {
        super.afterOnCreate();
        initToolbar();
        findViewById(R.id.tv_commit_order_result).setOnClickListener(this);
        mResultList = (RecyclerView)findViewById(R.id.rv_order_result);
        mResultList.setLayoutManager(new GridLayoutManager(this,4));
        mResultList.addItemDecoration(new SpanItemDecoration(ScreenUtil.dp2px(8),false,false,true,true));
        mAdapter = new OrderResultListAdapter(this,mCount);
        mResultList.setAdapter(mAdapter);

        mLoadingDialog = new LoadingDialog(this);
        mLoadingDialog.setProgressText("提交中...");

        ((TextView)findViewById(R.id.tv_user_nick_name)).setText(mNickName);
        ((TextView)findViewById(R.id.tv_user_place_order_num)).setText("下单数："+mOrderCount);

        final ImageView icon = (ImageView)findViewById(R.id.civ_user_icon);
        GlideHelper.displayImage(this,mAvatar,icon);
        UITask.postDelayed(new Runnable() {
            @Override
            public void run() {
                GlideHelper.displayImage(ConfirmOrderDoneActivity.this,mAvatar,icon);
            }
        },800);
    }


    private void initToolbar(){
        findViewById(R.id.tb_back).setOnClickListener(this);
        ((TextView)findViewById(R.id.tb_title)).setText("订单信息");
        setSupportActionBar(((Toolbar)findViewById(R.id.toolbar)));

    }

    @Override
    public void loadData() {
        super.loadData();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tb_back:
                finish();
                break;
            case R.id.tv_commit_order_result:
                List<Integer> result = mAdapter.getOrderResult();
                for (int i = 0; i < result.size(); i++) {
                    if (result.get(i) == -1){
                        ToastHelper.l("第"+ StringUtil.convert2Chinese(i+1)+"局未选择游戏结果哦~");
                        return;
                    }
                }
                List<OrderResultEntity.DataBean> data = new ArrayList<>();
                //转换成 后台需要的index
                for (int i = 0; i < result.size(); i++) {
                    switch (result.get(i) % 4){
                        case 1:
                            data.add(new OrderResultEntity.DataBean(i+1,1));
                            break;
                        case 2:
                            data.add(new OrderResultEntity.DataBean(i+1,0));
                            break;
                        case 3:
                            data.add(new OrderResultEntity.DataBean(i+1,-1));
                            break;
                    }
                }


                if (!mLoadingDialog.isShowing()){
                    mLoadingDialog.show();
                }

                String dataStr = "";
                Gson gson = new Gson();
                dataStr = gson.toJson(data);
                DataManager.confirmOrderResult(getMember_id(),mOrderId,dataStr);
                break;
        }
    }


    /**
     *
     */
    public void onEventMainThread(OrderResultCommitEntity entity){
        if (mLoadingDialog != null && mLoadingDialog.isShowing()){
            mLoadingDialog.dismiss();
        }
        if (entity != null && entity.isResult()){
            ToastHelper.l("提交成功~");
            finish();

            EventBus.getDefault().post(new RefreshOrderDetailEvent(
                    mOrderId,
                    "",
                    ""));
        }else {
            ToastHelper.l("提交失败~");
        }
    }
}
