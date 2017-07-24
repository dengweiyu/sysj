package com.li.videoapplication.ui.activity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

import com.alibaba.sdk.android.feedback.impl.FeedbackAPI;
import com.li.videoapplication.R;
import com.li.videoapplication.framework.TBaseActivity;
import com.li.videoapplication.framework.TBaseAppCompatActivity;

import java.util.concurrent.Callable;

/**
 * Created by y on 2017/1/5.
 */

public class FeedbackActivity extends TBaseAppCompatActivity {

    @Override
    public void afterOnCreate() {
        super.afterOnCreate();

        setSystemBarBackgroundWhite();
    }

    @Override
    public void beforeOnCreate() {
        super.beforeOnCreate();
        setSystemBar(true);
    }

    @Override
    public int getContentView() {
        return R.layout.activity_feedback;
    }

    @Override
    public void initView() {
        super.initView();

        final FragmentTransaction transaction = manager.beginTransaction();
        final Fragment feedback = FeedbackAPI.getFeedbackFragment();
        transaction.replace(R.id.fl_ali_feedback, feedback);
        transaction.commit();


    }

    @Override
    public void loadData() {
        super.loadData();
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        FeedbackAPI.cleanFeedbackFragment();
        FeedbackAPI.cleanActivity();
    }
}
