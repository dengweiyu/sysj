package com.li.videoapplication.ui.activity;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.li.videoapplication.R;
import com.li.videoapplication.data.DataManager;
import com.li.videoapplication.data.model.response.VipRechargeEntity;
import com.li.videoapplication.framework.TBaseAppCompatActivity;


/**
 * 会员中心
 */

public class VipCenterActivity extends TBaseAppCompatActivity implements View.OnClickListener {

    private RecyclerView mOptions;

    @Override
    public int getContentView() {
        return R.layout.activity_vip_center;
    }

    @Override
    public void afterOnCreate() {
        super.afterOnCreate();
    }

    @Override
    public void initView() {
        super.initView();
        initToolbar();

        mOptions  = (RecyclerView)findViewById(R.id.rv_vip_center_option);


    }


    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        TextView tb_title = (TextView) findViewById(R.id.tb_title);
        tb_title.setText("充值");
        findViewById(R.id.tb_back).setOnClickListener(this);
    }



    @Override
    public void loadData() {
        super.loadData();


        DataManager.vipInfo();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tb_back:
                finish();
                break;

        }
    }

    /**
     * 回调
     */
    public void onEventMainThread(VipRechargeEntity entity){
        if (entity != null && entity.isResult()){

        }
    }
}
