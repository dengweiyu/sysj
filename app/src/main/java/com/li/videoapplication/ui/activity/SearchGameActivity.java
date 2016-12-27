package com.li.videoapplication.ui.activity;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.TextView;

import com.li.videoapplication.R;
import com.li.videoapplication.data.DataManager;
import com.li.videoapplication.data.model.entity.Associate;
import com.li.videoapplication.data.preferences.PreferencesHepler;
import com.li.videoapplication.framework.TBaseAppCompatActivity;
import com.li.videoapplication.ui.adapter.SearchGameAdapter;
import com.li.videoapplication.ui.fragment.ChooseGameFragment;
import com.li.videoapplication.ui.pageradapter.ViewPagerAdapter;
import com.li.videoapplication.utils.InputUtil;
import com.li.videoapplication.utils.StringUtil;

import me.everything.android.ui.overscroll.HorizontalOverScrollBounceEffectDecorator;
import me.everything.android.ui.overscroll.adapters.ViewPagerOverScrollDecorAdapter;

/**
 * 活动：查找游戏
 */
@SuppressLint("HandlerLeak")
public class SearchGameActivity extends TBaseAppCompatActivity implements TextWatcher, OnClickListener {

    private List<Fragment> fragments;
    private AutoCompleteTextView searchEdit;
    private ImageView delete, search;
    public ViewPager viewpager;
    private ChooseGameFragment hotGameFragment;

    private String getAbSearchEdit() {
        return searchEdit.getText().toString().trim();
    }

    // 搜索结果
    private SearchGameAdapter adapter;


    // 搜索类型(手机游戏，精彩生活)
    private String type;

    @Override
    public void refreshIntent() {
        super.refreshIntent();

        try {
            type = getIntent().getStringExtra("type");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getContentView() {
        return R.layout.activity_searchgame;
    }

    @Override
    public void afterOnCreate() {
        super.afterOnCreate();
        setSystemBarBackgroundColor(Color.WHITE);
    }

    @Override
    public void initView() {
        super.initView();
        initToolbar();

        initViewPager();
    }

    private void initToolbar() {
        findViewById(R.id.tb_back).setOnClickListener(this);
        TextView tb_title = (TextView) findViewById(R.id.tb_title);
        tb_title.setText("选择游戏");

        searchEdit = (AutoCompleteTextView) findViewById(R.id.search_edit);
        delete = (ImageView) findViewById(R.id.search_delete);
        search = (ImageView) findViewById(R.id.search);

        delete.setVisibility(View.GONE);
        search.setEnabled(false);
        delete.setOnClickListener(this);
        //设置输入多少字符后提示，默认值为1
        searchEdit.setThreshold(1);
        searchEdit.addTextChangedListener(this);
    }

    private void initViewPager() {
        if (fragments == null) {
            fragments = new ArrayList<>();

            fragments.add(ChooseGameFragment.newInstance(ChooseGameFragment.ChooseGame_HISTORY));
            hotGameFragment = ChooseGameFragment.newInstance(ChooseGameFragment.ChooseGame_HOT);
            fragments.add(hotGameFragment);
        }

        final String[] tabTitle = {"最近游戏", "热门游戏"};
        ViewPagerAdapter viewpagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), fragments, tabTitle);
        viewpager = (ViewPager) findViewById(R.id.search_viewpager);
        viewpager.setAdapter(viewpagerAdapter);
        new HorizontalOverScrollBounceEffectDecorator(new ViewPagerOverScrollDecorAdapter(viewpager));

        TabLayout tabLayout = (TabLayout) findViewById(R.id.search_tab);
        tabLayout.setupWithViewPager(viewpager);

        List<Associate> list = PreferencesHepler.getInstance().getAssociate201List();
        if (list.size() <= 0) {
            viewpager.setCurrentItem(1);
        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.tb_back:
                finish();
                break;
            case R.id.search_delete:
                searchEdit.setText("");
                InputUtil.showKeyboard(searchEdit);
                hotGameFragment.loadData();
                break;
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
    }

    @Override
    public void afterTextChanged(Editable s) {

        if (!StringUtil.isNull(getAbSearchEdit())) {
            // 搜索联想词201
            DataManager.associate210(type, getAbSearchEdit());
        }

        if (viewpager.getCurrentItem() == 0 && !StringUtil.isNull(getAbSearchEdit())) {
            viewpager.setCurrentItem(1);
        }

        if (StringUtil.isNull(getAbSearchEdit())) {
            delete.setVisibility(View.GONE);
            hotGameFragment.loadData();
        } else {
            delete.setVisibility(View.VISIBLE);
        }
    }
}
