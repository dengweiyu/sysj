package com.ifeimo.im.activity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ifeimo.im.R;
import com.ifeimo.im.activity.fragment.ContractFragment;
import com.ifeimo.im.activity.fragment.FriendFragment;
import com.ifeimo.im.framwork.Proxy;
import com.ifeimo.im.framwork.interface_im.IMWindow;

import java.util.ArrayList;
import java.util.List;

/**
 * 联系人
 */
@Deprecated
public class ContactListActivity extends BaseActivity implements
        View.OnClickListener, OnPageChangeListener {


    private ViewPager viewpage;
    private ImageView id_talk_iv;
    private ImageView id_friend_iv;
    private FragmentPagerAdapter pagerAdapter;
    private List<Fragment> mList = new ArrayList<>();

    private TextView id_top_right_tv;
    private ImageView id_top_right_iv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        clearImWindow();
        setContentView(R.layout.activity_contract_list);
        initView();
    }

    private void initView() {

        id_top_right_tv = (TextView) findViewById(R.id.id_top_right_tv);
        id_top_right_iv = (ImageView) findViewById(R.id.id_top_right_iv);
        id_top_right_tv.setVisibility(View.GONE);

        id_talk_iv = (ImageView) findViewById(R.id.id_talk_iv);
        id_friend_iv = (ImageView) findViewById(R.id.id_friend_iv);
        id_talk_iv.setOnClickListener(this);
        id_friend_iv.setOnClickListener(this);
        mList.add(new ContractFragment());
        mList.add(new FriendFragment());

        viewpage = (ViewPager) findViewById(R.id.viewpage);
        pagerAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return mList.get(position);
            }

            @Override
            public int getCount() {
                return mList.size();
            }
        };
        viewpage.setAdapter(pagerAdapter);
        viewpage.addOnPageChangeListener(this);
    }

    private void clearImWindow() {
        Proxy.getIMWindowManager().releaseAll();
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.id_talk_iv) {
            id_talk_iv.setImageResource(R.drawable.a5d);
            id_friend_iv.setImageResource(R.drawable.tc);
            viewpage.setCurrentItem(0);
        } else {
            id_talk_iv.setImageResource(R.drawable.a5c);
            id_friend_iv.setImageResource(R.drawable.t3);
            viewpage.setCurrentItem(1);
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        if (position == 0) {
            onClick(id_talk_iv);
        } else {
            onClick(id_friend_iv);
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    public void doBack(View v){
        finish();
    }

    @Override
    public IMWindow getIMWindow() {
        return null;
    }
}
