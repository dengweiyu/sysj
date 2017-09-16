package com.li.videoapplication.ui.activity;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.li.videoapplication.R;
import com.li.videoapplication.data.DataManager;
import com.li.videoapplication.data.network.UITask;
import com.li.videoapplication.framework.TBaseAppCompatActivity;

import com.li.videoapplication.mvp.mall.view.MyVipInfoFragment;
import com.li.videoapplication.tools.TabLayoutHelper;
import com.li.videoapplication.ui.fragment.MyBeansFragment;
import com.li.videoapplication.ui.fragment.MyWalletTaskFragment;
import com.li.videoapplication.ui.pageradapter.ViewPagerAdapter;
import com.li.videoapplication.utils.ScreenUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 钱包
 */

public class MyWalletActivity extends TBaseAppCompatActivity implements View.OnClickListener {
    public static int PAGE_BEANS = 0;
    public static int PAGE_CURRENCY = 1;
    public static int PAGE_TASK = 2;

    private List<Fragment> mFragments;
    private ViewPager mPager;
    private int page;
    @Override
    protected int getContentView() {
        return R.layout.activity_my_wallet;
    }

    @Override
    public void refreshIntent() {
        super.refreshIntent();

        page = getIntent().getIntExtra("page",0);

    }

    @Override
    public void initView() {
        findViewById(R.id.tb_back).setOnClickListener(this);
        TextView title = (TextView)findViewById(R.id.tb_title);
        title.setText("我的钱包");
        ImageView question = (ImageView)findViewById(R.id.iv_tb_record);
        question.setImageResource(R.drawable.mywallet_question);
        question.setVisibility(View.VISIBLE);
        question.setOnClickListener(this);
        question.setBackgroundResource(R.drawable.button_white);
        mPager = (ViewPager)findViewById(R.id.vp_my_wallet);
        mFragments = new ArrayList<>();

        mFragments.add(MyBeansFragment.newInsatnce(MyBeansFragment.MY_BEANS));              //魔豆
        mFragments.add(MyBeansFragment.newInsatnce(MyBeansFragment.MY_CURRENCY));           //魔币
        mFragments.add(new MyVipInfoFragment());                                            //VIP详情
        mFragments.add(new MyWalletTaskFragment());
        mPager.setAdapter(new ViewPagerAdapter(getSupportFragmentManager(),mFragments,new String[]{"魔豆","魔币","VIP","任务"}));
        mPager.setOffscreenPageLimit(3);
        if (page >= mFragments.size()){
            page = mFragments.size() - 1;
        }
        if (page < 0){
            page = 0;
        }
        mPager.setCurrentItem(page);

        final TabLayout tabLayout = (TabLayout)findViewById(R.id.tab_my_wallet) ;
        tabLayout.setupWithViewPager(mPager);

        UITask.postDelayed(new Runnable() {
            @Override
            public void run() {
                int margin = ScreenUtil.dp2px(20);
                TabLayoutHelper.setIndicator(tabLayout,margin,margin);
            }
        },100);

        DataManager.getMemberTask(getMember_id());

    }



    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_tb_record:
                //钱包说明
                WebActivityJS.startWebActivityJS(this,"http://apps.ifeimo.com/Sysj221/WalletInstructions/index","我的钱包说明","");
                break;
            case R.id.tb_back:
                finish();
                break;
        }
    }


}
