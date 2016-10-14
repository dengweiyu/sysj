package com.li.videoapplication.framework;

public interface IBaseActivity {

    void beforeOnCreate();

    void afterOnCreate();

    void initView();

    void loadData();

    void refreshIntent();
}
