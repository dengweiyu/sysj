package com.li.videoapplication.ui.activity;

import android.view.View;
import android.widget.GridView;


import com.li.videoapplication.R;
import com.li.videoapplication.data.EventManager;
import com.li.videoapplication.data.model.entity.Tag;
import com.li.videoapplication.framework.TBaseActivity;
import com.li.videoapplication.ui.adapter.TagAdapter;

import java.util.ArrayList;
import java.util.List;


/**
 * 碎片：标签
 */
public class TagActivity extends TBaseActivity implements View.OnClickListener {

    public List<Tag> data = new ArrayList<>();
    private TagAdapter adapter;

    @Override
    public int getContentView() {
        return R.layout.activity_tag;
    }

    @Override
    public int inflateActionBar() {
        return R.layout.actionbar_second;
    }

    @Override
    public void beforeOnCreate() {
        super.beforeOnCreate();
        setSystemBar(true);
    }

    @Override
    public void afterOnCreate() {
        super.afterOnCreate();

        setAbTitle(R.string.tag_title);
        setSystemBarBackgroundWhite();
        abTagConfirm.setVisibility(View.VISIBLE);
        abTagConfirm.setOnClickListener(this);
    }

    @Override
    public void initView() {
        super.initView();
        GridView gridView = (GridView) findViewById(R.id.gridView);
        adapter = new TagAdapter(this, VideoShareActivity210.TAGS);
        gridView.setAdapter(adapter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        EventManager.postTag2VideoShareEvent();
    }

    @Override
    public void onClick(View view) {

        if (view == abTagConfirm) {
            finish();
        }
    }
}
