package com.li.videoapplication.ui.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.Selection;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;

import com.li.videoapplication.R;
import com.li.videoapplication.data.DataManager;
import com.li.videoapplication.data.model.entity.Associate;
import com.li.videoapplication.data.model.entity.Keyword;
import com.li.videoapplication.data.model.response.AssociateEntity;
import com.li.videoapplication.data.model.response.KeyWordListNewEntity;
import com.li.videoapplication.data.preferences.PreferencesHepler;
import com.li.videoapplication.framework.TBaseActivity;
import com.li.videoapplication.tools.UmengAnalyticsHelper;
import com.li.videoapplication.ui.adapter.SearchAssociateAdapter;
import com.li.videoapplication.ui.adapter.SearchHistoryAdapter;
import com.li.videoapplication.ui.adapter.SearchHotAdapter;
import com.li.videoapplication.ui.dialog.LoadingDialog;
import com.li.videoapplication.ui.fragment.SearchResultFragment;
import com.li.videoapplication.utils.InputUtil;
import com.li.videoapplication.utils.StringUtil;
import com.li.videoapplication.views.GridViewY1;
import com.li.videoapplication.views.ListViewY1;

import java.util.ArrayList;
import java.util.List;

/**
 * 活动：搜索
 */
public class SearchActivity extends TBaseActivity implements
        OnClickListener,
        TextWatcher,
        OnItemClickListener,
        View.OnFocusChangeListener {

    private SearchResultFragment searchResultFragment;

    /**
     * 跳转：搜索结果
     */
    public void startSearchResultFragment(String content) {
        setLoading(true);
        PreferencesHepler.getInstance().addSearchHistory(content);

        if (searchResultFragment == null) {
            searchResultFragment = new SearchResultFragment();
            Bundle bundle = new Bundle();
            bundle.putString("content", content);
            searchResultFragment.setArguments(bundle);

            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.setCustomAnimations(R.anim.activity_slide_in_right, R.anim.activity_disappear);
            ft.add(R.id.frag_container, searchResultFragment).commit();
        } else {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.show(searchResultFragment).commit();
            searchResultFragment.refreshSearchResult(content);
        }
    }

    public void hideSearchResultFragment() {
        if (searchResultFragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
            ft.hide(searchResultFragment).commit();
        }
    }

    private View contentContainer;

    // 搜索历史
    private View historyContainer;
    private GridViewY1 historyGridView;
    private List<String> historyData = new ArrayList<>();
    private SearchHistoryAdapter historyAdapter;

    // 热门搜索
    private View hotContainer;
    private GridViewY1 hotGridView;
    private List<Keyword> hotData = new ArrayList<>();
    private SearchHotAdapter hotAdapter;

    private TextView clear;

    // 搜索匹配
    private View associateContainer;
    private ListViewY1 associateListView;
    private List<Associate> associateData = new ArrayList<>();
    private SearchAssociateAdapter associateAdapter;

    public String getAbSearchEdit() {
        if (abSearchEdit.getText() == null)
            return "";
        return abSearchEdit.getText().toString().trim();
    }

    @Override
    public int getContentView() {
        return R.layout.activity_search;
    }

    public int inflateActionBar() {
        return R.layout.actionbar_second;
    }

    @Override
    public void afterOnCreate() {
        super.afterOnCreate();

        setSystemBarBackgroundWhite();
    }

    @Override
    public void initView() {
        super.initView();

        initContentView();
        refreshContentView(isAssociateVisible);
    }

    private void initContentView() {
        contentContainer = findViewById(R.id.search_content);

        historyContainer = findViewById(R.id.search_history);
        hotContainer = findViewById(R.id.search_hot);
        associateContainer = findViewById(R.id.search_associate);
        historyContainer.setVisibility(View.GONE);
        hotContainer.setVisibility(View.GONE);
        associateContainer.setVisibility(View.GONE);

        clear = (TextView) findViewById(R.id.search_clear);

        clear = (TextView) findViewById(R.id.search_clear);
        clear.setOnClickListener(this);

        abSearchContainer.setVisibility(View.VISIBLE);
        abSearchDelete.setVisibility(View.GONE);
        abSearchSubmit.setOnClickListener(this);
        abSearchDelete.setOnClickListener(this);
        //设置输入多少字符后提示，默认值为1
        abSearchEdit.setThreshold(1);
        abSearchEdit.addTextChangedListener(this);
        abSearchEdit.setOnFocusChangeListener(this);
        abSearchEdit.requestFocus();

        historyGridView = (GridViewY1) findViewById(R.id.search_history_grid);
        historyAdapter = new SearchHistoryAdapter(this, historyData);
        historyGridView.setAdapter(historyAdapter);
        historyGridView.setOnItemClickListener(this);

        hotGridView = (GridViewY1) findViewById(R.id.search_hot_grid);
        hotAdapter = new SearchHotAdapter(this, hotData);
        hotGridView.setAdapter(hotAdapter);
        hotGridView.setOnItemClickListener(this);

        associateListView = (ListViewY1) findViewById(R.id.search_associate_list);
        associateAdapter = new SearchAssociateAdapter(this, associateData);
        associateListView.setAdapter(associateAdapter);
        associateListView.setOnItemClickListener(this);

        abSearchEdit.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                        (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                    abSearchSubmit.performClick();
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    public void loadData() {
        super.loadData();

        // 搜索关键字(v1.1.5接口)
        DataManager.keyWordListNew();
    }

    public void setLoading(boolean isLoading){
        if (isLoading){
            showProgressDialog(LoadingDialog.LOADING);
        }else {
            dismissProgressDialog();
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

        // 搜索联想词
        DataManager.associate(getAbSearchEdit());

        isAssociateVisible = getAbSearchEdit().length() > 0;
        refreshContentView(isAssociateVisible);
    }

    @Override
    public void afterTextChanged(Editable s) {
        if (StringUtil.isNull(getAbSearchEdit())) {
            abSearchDelete.setVisibility(View.GONE);
        } else {
            hideSearchResultFragment();
            abSearchDelete.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.search_clear:
                PreferencesHepler.getInstance().removeSearchHistory();
                refreshContentView(isAssociateVisible);
                break;

            case R.id.ab_search:
                if (StringUtil.isNull(getAbSearchEdit())) {
                    showToastShort("请输入搜索内容");
                    return;
                }
                try {
                    InputUtil.closeKeyboard(this);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                startSearchResultFragment(getAbSearchEdit());
                break;

            case R.id.ab_search_delete:
                hideSearchResultFragment();
                abSearchEdit.setText("");
                InputUtil.showKeyboard(abSearchEdit);
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        if (parent.getAdapter() == historyAdapter) {
            UmengAnalyticsHelper.onEvent(this, UmengAnalyticsHelper.MAIN, "搜索-搜索记录-点击搜索记录内任何记录");
            String record = (String) parent.getAdapter().getItem(position);
            if (!StringUtil.isNull(record)) {
                abSearchEdit.setText(record);
                try {
                    InputUtil.closeKeyboard(this);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                startSearchResultFragment(record);
            }
        }

        if (parent.getAdapter() == hotAdapter) {
            UmengAnalyticsHelper.onEvent(this, UmengAnalyticsHelper.MAIN, "搜索-热门搜索-点击搜索框下面的任何热门游戏");
            Keyword record = (Keyword) parent.getAdapter().getItem(position);
            if (!StringUtil.isNull(record.getName())) {
                abSearchEdit.setText(record.getName());
                try {
                    InputUtil.closeKeyboard(this);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                startSearchResultFragment(record.getName());
            }
        }

        if (parent.getAdapter() == associateAdapter) {
            Associate record = (Associate) parent.getAdapter().getItem(position);
            if (!StringUtil.isNull(record.getName())) {
                abSearchEdit.setText(record.getName());
                try {
                    InputUtil.closeKeyboard(this);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                startSearchResultFragment(record.getName());
            }
        }

        Editable e = abSearchEdit.getText();
        Selection.setSelection(e, e.length());
    }

    private boolean isAssociateVisible = false;

    private void refreshContentView(boolean isAssociateVisible) {

        if (isAssociateVisible) {
            associateContainer.setVisibility(View.VISIBLE);
            contentContainer.setVisibility(View.GONE);
        } else {
            associateContainer.setVisibility(View.GONE);
            contentContainer.setVisibility(View.VISIBLE);
        }

        List<String> list = PreferencesHepler.getInstance().getSearchHistory();
        historyData.clear();
        historyData.addAll(list);
        historyAdapter.notifyDataSetChanged();
        if (historyData.size() > 0) {
            historyContainer.setVisibility(View.VISIBLE);
        } else {
            historyContainer.setVisibility(View.GONE);
        }

        hotAdapter.notifyDataSetChanged();
        if (hotData.size() > 0) {
            hotContainer.setVisibility(View.VISIBLE);
        } else {
            hotContainer.setVisibility(View.GONE);
        }

        associateAdapter.notifyDataSetChanged();
    }

    /**
     * 回调：搜索关键字(v1.1.5接口)
     */
    public void onEventMainThread(KeyWordListNewEntity event) {

        if (event.isResult()) {
            if (event.getData().getList().size() > 0) {
                hotData.clear();
                hotData.addAll(event.getData().getList());
            }
        }
        refreshContentView(isAssociateVisible);
    }

    /**
     * 回调：搜索联想词
     */
    public void onEventMainThread(AssociateEntity event) {

        if (event.isResult()) {
            if (event.getData() != null && event.getData().size() > 0) {
                associateData.clear();
                associateData.addAll(event.getData());
            }
        }
        if (isAssociateVisible)
            associateAdapter.setKeyWord(getAbSearchEdit());
        refreshContentView(isAssociateVisible);
    }
}
