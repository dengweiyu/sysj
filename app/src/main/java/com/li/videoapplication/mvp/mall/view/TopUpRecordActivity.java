package com.li.videoapplication.mvp.mall.view;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.li.videoapplication.R;
import com.li.videoapplication.data.model.entity.Currency;
import com.li.videoapplication.data.model.entity.TopUp;
import com.li.videoapplication.framework.TBaseAppCompatActivity;
import com.li.videoapplication.mvp.adapter.TopUpRecordAdapter;
import com.li.videoapplication.mvp.mall.MallContract;
import com.li.videoapplication.mvp.mall.presenter.MallPresenter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import me.everything.android.ui.overscroll.OverScrollDecoratorHelper;

/**
 * 活动：充值记录
 */
public class TopUpRecordActivity extends TBaseAppCompatActivity implements MallContract.ITopUpRecordView,
        View.OnClickListener{

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    private TopUpRecordAdapter adapter;

    @Override
    protected int getContentView() {
        return R.layout.activity_topuprecord;
    }

    @Override
    public void afterOnCreate() {
        super.afterOnCreate();
        setSystemBarBackgroundWhite();
    }

    @Override
    public void initView() {
        super.initView();
        initToolbar();
        initAdapter();
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        TextView tb_title = (TextView) findViewById(R.id.tb_title);
        tb_title.setText("充值记录");

        findViewById(R.id.tb_back).setOnClickListener(this);
    }

    private void initAdapter() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//        OverScrollDecoratorHelper.setUpOverScroll(recyclerView, OverScrollDecoratorHelper.ORIENTATION_VERTICAL);
        List<TopUp> data = new ArrayList<>();
        adapter = new TopUpRecordAdapter(data);

        View emptyView = getLayoutInflater().inflate(R.layout.emptyview,
                (ViewGroup) recyclerView.getParent(), false);
        TextView emptyViewText = (TextView) emptyView.findViewById(R.id.emptyview_text);
        emptyViewText.setText("您还没有充值记录喔~");

        adapter.setEmptyView(emptyView);

        recyclerView.setAdapter(adapter);
    }

    @Override
    public void loadData() {
        super.loadData();
        MallContract.IMallPresenter presenter = new MallPresenter();
        presenter.setTopUpRecordView(this);
//        presenter.getTopUpRecordList("1201867");
        presenter.getTopUpRecordList(getMember_id());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tb_back:
                finish();
                break;

        }
    }

    /**
     * 回调：充值记录
     */
    @Override
    public void refreshTopUpRecordData(List<TopUp> data) {
        adapter.setNewData(data);
    }
}
