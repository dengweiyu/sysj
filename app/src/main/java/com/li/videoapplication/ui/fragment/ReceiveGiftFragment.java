package com.li.videoapplication.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;
import com.google.common.collect.PeekingIterator;
import com.handmark.pulltorefresh.library.IPullToRefresh;
import com.li.videoapplication.R;
import com.li.videoapplication.data.DataManager;
import com.li.videoapplication.data.model.entity.Member;
import com.li.videoapplication.data.model.entity.VideoImage;
import com.li.videoapplication.data.model.response.MyGiftBillEntity;
import com.li.videoapplication.framework.TBaseFragment;
import com.li.videoapplication.ui.ActivityManager;
import com.li.videoapplication.ui.activity.MyGiftActivity;
import com.li.videoapplication.ui.adapter.MyGiftBillAdapter;
import com.li.videoapplication.ui.view.SimpleItemDecoration;
import com.li.videoapplication.utils.StringUtil;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * 收到的礼物
 */

public class ReceiveGiftFragment extends TBaseFragment implements View.OnClickListener {
    public static int MODE_RECEIVE = 1;
    public static int MODE_SEND = 2;
    private RecyclerView mRecyclerView;
    private MyGiftBillAdapter mAdapter;
    private List<MyGiftBillEntity.SectionBill> mData;
    private int mMode ;
    private String mUserId;
    private int mRed;
    private int mGray;
    private TextView mAllGift;
    private TextView mBeansGift;
    private TextView mCoinGift;

    private View mEmptyLayout;
    private ImageView mEmptyIcon;
    private TextView mEmptyDescription;
    private MyGiftActivity mActivity;
    public static ReceiveGiftFragment newInstance(int mode,String userId){
        ReceiveGiftFragment fragment = new ReceiveGiftFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("mode",mode);
        bundle.putString("user_id",userId);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof MyGiftActivity){
            mActivity = (MyGiftActivity) context;
        }
    }

    @Override
    protected int getCreateView() {
        return R.layout.fragment_receive_gift;
    }

    @Override
    protected void initContentView(View view) {
        Bundle bundle = getArguments();
        if (bundle != null){
            mMode = bundle.getInt("mode");
            mUserId = bundle.getString("user_id","");
        }

        mEmptyLayout = view.findViewById(R.id.ll_no_gift);
        mEmptyIcon = (ImageView) view.findViewById(R.id.iv_no_gift);
        mEmptyDescription = (TextView) view.findViewById(R.id.tv_no_gift);

        mAllGift = (TextView)view.findViewById(R.id.tv_subtitle_all);
        mBeansGift = (TextView)view.findViewById(R.id.tv_subtitle_beans);
        mCoinGift = (TextView)view.findViewById(R.id.tv_subtitle_coin);

        mAllGift.setOnClickListener(this);
        mBeansGift.setOnClickListener(this);
        mCoinGift.setOnClickListener(this);

        mRecyclerView = (RecyclerView)view.findViewById(R.id.rv_my_bill_recycler_view);
        mData = new ArrayList<>();
        mAdapter = new MyGiftBillAdapter(mData);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.addItemDecoration(new SimpleItemDecoration(getActivity(),false,false,false,true));
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addOnItemTouchListener(new OnItemChildClickListener() {
            @Override
            public void SimpleOnItemChildClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                MyGiftBillEntity.SectionBill data = mAdapter.getData().get(i);
                switch (view.getId()){
                    case R.id.tv_my_gift_nick_name:
                        if (!mUserId.equals(data.t.getMember_id())){
                            Member member = new Member();
                            member.setMember_id(data.t.getMember_id());
                            ActivityManager.startPlayerDynamicActivity(getContext(),member);
                        }
                        break;
                    case R.id.tv_my_gift_video_name:
                        VideoImage video = new VideoImage();
                        video.setVideo_id(data.t.getVideo_id());
                        ActivityManager.startVideoPlayActivity(getActivity(),video);
                        break;
                }
            }
        });



        mRed = getResources().getColor(R.color.currency_red);
        mGray = getResources().getColor(R.color.activity_gray);
    }


    @Override
    protected IPullToRefresh getPullToRefresh() {
        return null;
    }


    public void onEventMainThread(MyGiftBillEntity entity){
        if (entity != null){
            List<MyGiftBillEntity.DataBean> dataBeen = entity.getData();
            if (dataBeen != null){
                for (int i = 0; i < dataBeen.size(); i++) {
                    MyGiftBillEntity.SectionBill section = new MyGiftBillEntity.SectionBill(true,dataBeen.get(i).getTitle());
                    mData.add(section);
                    int size = mData.size();
                    for (int j = 0; j < dataBeen.get(i).getList().size(); j++) {
                        MyGiftBillEntity.DataBean.ListBean listData =  dataBeen.get(i).getList().get(j);
                        if (mMode == MODE_RECEIVE){
                            if ("received".equals(listData.getTab())){
                                section = new MyGiftBillEntity.SectionBill(listData);
                                mData.add(section);
                            }
                        }else {
                            if ("send".equals(listData.getTab())){
                                section = new MyGiftBillEntity.SectionBill(listData);
                                mData.add(section);
                            }
                        }
                    }
                    if (size == mData.size()){
                        mData.remove(size -1);      //no child view,remove title
                    }
                }
            }
            mAdapter.setNewData(mData);
        }

        if (mData.size() == 0){
            showEmptyView();
        }else {
            hideEmptyView();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_subtitle_all:
                mAllGift.setTextColor(mRed);
                mCoinGift.setTextColor(mGray);
                mBeansGift.setTextColor(mGray);
                resetData(0);
                break;

            case R.id.tv_subtitle_coin:
                mAllGift.setTextColor(mGray);
                mCoinGift.setTextColor(mRed);
                mBeansGift.setTextColor(mGray);
                resetData(1);
                break;

            case R.id.tv_subtitle_beans:
                mAllGift.setTextColor(mGray);
                mCoinGift.setTextColor(mGray);
                mBeansGift.setTextColor(mRed);
                resetData(2);
                break;
        }
    }

    /**
     *
     * @param mode
     *              0 显示全部礼物
     *              1 显示魔币礼物
     *              2 显示魔豆礼物
     */
    private void resetData(int mode){
            if (mode == 0){
                mAdapter.setNewData(mData);
            }else if(mode == 1){            //魔币礼物
                mAdapter.setNewData(Lists.newArrayList(Iterables.filter(mData, new Predicate<MyGiftBillEntity.SectionBill>() {
                    @Override
                    public boolean apply(MyGiftBillEntity.SectionBill input) {
                        if (input.t == null){       //标题
                            return true;
                        }
                        String type = input.t.getType();
                        if (!StringUtil.isNull(type)){
                            if ("coin".equals(type)){
                                return true;
                            }
                        }
                        return false;
                    }
                })));
            }else if(mode == 2){            //魔豆礼物
                mAdapter.setNewData(Lists.newArrayList(Iterables.filter(mData, new Predicate<MyGiftBillEntity.SectionBill>() {
                    @Override
                    public boolean apply(MyGiftBillEntity.SectionBill input) {
                        if (input.t == null){       //标题
                            return true;
                        }
                        String type = input.t.getType();
                        if (!StringUtil.isNull(type)){
                            if ("currency".equals(type)){
                                return true;
                            }
                        }
                        return false;
                    }
                })));
            }
    }

    public void showEmptyView(){
        if (mMode == MODE_RECEIVE){
            mEmptyLayout.setVisibility(View.VISIBLE);
            mEmptyIcon.setImageResource(R.drawable.no_receive_gift);
            mEmptyDescription.setText(R.string.no_receive_gift_description);
        }else {
            mEmptyLayout.setVisibility(View.VISIBLE);
            mEmptyIcon.setImageResource(R.drawable.no_send_gift);
            mEmptyDescription.setText(R.string.no_send_gift_description);
        }
    }

    public void hideEmptyView(){
        mEmptyLayout.setVisibility(View.GONE);
    }
}
