package com.li.videoapplication.ui.activity;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toolbar;

import com.li.videoapplication.R;
import com.li.videoapplication.data.DataManager;
import com.li.videoapplication.data.model.entity.HomeDto;
import com.li.videoapplication.data.network.UITask;
import com.li.videoapplication.framework.TBaseAppCompatActivity;
import com.li.videoapplication.tools.TabLayoutHelper;
import com.li.videoapplication.ui.fragment.ReceiveGiftFragment;
import com.li.videoapplication.ui.pageradapter.ViewPagerAdapter;
import com.li.videoapplication.utils.ScreenUtil;
import com.li.videoapplication.utils.StringUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 我的礼物
 */

public class MyGiftActivity extends TBaseAppCompatActivity implements View.OnClickListener {
    private List<Fragment> fragments;

    private String mUserId;


    @Override
    public void refreshIntent() {
        super.refreshIntent();
        Intent intent = getIntent();
        mUserId = intent.getStringExtra("user_id");

    }

    @Override
    protected int getContentView() {
        return R.layout.activity_my_gift;
    }


    @Override
    public void initView() {
        findViewById(R.id.tb_back).setOnClickListener(this);
        TextView title = ((TextView)findViewById(R.id.tb_title));
        if (mUserId.equals(getMember_id())){
            title.setText("我的礼物");
        }else {
            title.setText("TA的礼物");
        }


        fragments = new ArrayList<>();
        fragments.add(ReceiveGiftFragment.newInstance(ReceiveGiftFragment.MODE_RECEIVE,mUserId));
        fragments.add(ReceiveGiftFragment.newInstance(ReceiveGiftFragment.MODE_SEND,mUserId));
        ViewPager pager = (ViewPager)findViewById(R.id.vp_my_gift_bill);
        ViewPagerAdapter adapter = new ViewPagerAdapter(manager, fragments,new String[]{"收到的礼物","送出的礼物"});
        pager.setAdapter(adapter);

        final TabLayout tabLayout = (TabLayout)findViewById(R.id.tab_my_gift_bill) ;
        tabLayout.setupWithViewPager(pager);

        UITask.postDelayed(new Runnable() {
            @Override
            public void run() {
                int margin = ScreenUtil.dp2px(50);
                TabLayoutHelper.setIndicator(tabLayout,margin,margin);
            }
        },100);

    }

    @Override
    public void loadData() {
        super.loadData();

        DataManager.getGiftBill(getMember_id(),mUserId);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tb_back:
                finish(); break;
        }
    }


}
