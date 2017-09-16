package com.li.videoapplication.mvp.mall.view;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.IPullToRefresh;
import com.li.videoapplication.R;
import com.li.videoapplication.data.model.response.BillEntity;
import com.li.videoapplication.framework.TBaseFragment;
import com.li.videoapplication.mvp.adapter.BillAdapter;
import com.li.videoapplication.mvp.mall.MallContract;
import com.li.videoapplication.mvp.mall.presenter.MallPresenter;

import java.util.ArrayList;
import java.util.List;

/**
 * 魔豆账单
 */

public class MyBeansBillFragment extends TBaseFragment implements MallContract.IBillListView {
    private MallContract.IMallPresenter presenter;
    private RecyclerView mRecyclerView;
    private BillAdapter mAdapter;
    private List<BillEntity.SectionBill> mData;

    private TextView mEmpty;
    @Override
    protected int getCreateView() {
        return R.layout.fragment_my_beans_bill;
    }

    @Override
    protected void initContentView(View view) {
        mRecyclerView = (RecyclerView)view.findViewById(R.id.rv_my_bill_list);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mData = new ArrayList<>();
        mAdapter = new BillAdapter(mData,getMember_id());
        mRecyclerView.setAdapter(mAdapter);
        presenter = new MallPresenter();
        presenter.setBillListView(this);
        presenter.getBillList(getMember_id(),1);

        mEmpty = (TextView)view.findViewById(R.id.tv_coin_bill_empty);
    }

    @Override
    protected IPullToRefresh getPullToRefresh() {
        return null;
    }

    @Override
    public void refreshBillList(BillEntity entity) {
        if (entity != null){
            List<BillEntity.DataBean.BillDatasBean> datas = entity.getData().getBillDatas();
            for (int i = 0; i < datas.size(); i++) {
                BillEntity.DataBean.BillDatasBean  billDatasbean =  datas.get(i);
                BillEntity.SectionBill section = new BillEntity.SectionBill(true,billDatasbean.getTitle());
                mData.add(section);
                for (int j = 0; j < billDatasbean.getList().size(); j++) {
                    section = new BillEntity.SectionBill(billDatasbean.getList().get(j));
                    mData.add(section);
                }
            }
            mAdapter.setNewData(mData);

            if (datas.size() == 0){
                mEmpty.setVisibility(View.VISIBLE);
            }else {
                mEmpty.setVisibility(View.GONE);
            }
        }else {
            mEmpty.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void refreshFault() {

    }
}
