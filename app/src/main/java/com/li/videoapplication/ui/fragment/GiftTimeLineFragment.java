package com.li.videoapplication.ui.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.handmark.pulltorefresh.library.IPullToRefresh;
import com.li.videoapplication.R;
import com.li.videoapplication.data.DataManager;
import com.li.videoapplication.data.model.event.ResetTimeLineEvent;
import com.li.videoapplication.data.model.response.ServiceTimeEntity;
import com.li.videoapplication.data.model.response.TimeLineGiftEntity;
import com.li.videoapplication.framework.TBaseFragment;
import com.li.videoapplication.ui.adapter.GiftTimeLineAdapter;
import com.li.videoapplication.ui.view.GiftTimeLineAnimator;
import com.li.videoapplication.ui.view.SpanItemDecoration;
import com.li.videoapplication.utils.ScreenUtil;
import com.li.videoapplication.ui.view.VideoPlayView;
import com.li.videoapplication.views.SlideLiftAnimation;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * 礼物时间轴
 */

public class GiftTimeLineFragment extends TBaseFragment implements View.OnClickListener,VideoPlayView.OnProgressListener {
    private final  int mDelay = 2000;
    private String mVideoId;
    private RecyclerView mTimeLine;
    private static List<TimeLineGiftEntity.DataBean> sData;                     //所有数据
    private static List<TimeLineGiftEntity.DataBean> sSaveData;                 //RecyclerView数据
    private GiftTimeLineAdapter mAdapter;

    public static GiftTimeLineFragment newInstance(String videoId){
        GiftTimeLineFragment fragment = new GiftTimeLineFragment();
        Bundle bundle = new Bundle();
        bundle.putString("video_id",videoId);
        fragment.setArguments(bundle);
        return fragment;
    }
    @Override
    protected int getCreateView() {
        return R.layout.fragment_gift_time_line;
    }

    @Override
    protected void initContentView(View view) {
        Bundle bundle = getArguments();
        if (bundle != null){
            mVideoId = bundle.getString("video_id","");
        }


        mTimeLine = (RecyclerView)view.findViewById(R.id.rv_time_line);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext()){
            @Override
            public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
                try {
                    super.onLayoutChildren(recycler, state);
                } catch (Exception e) {
                    e.printStackTrace();
                    mAdapter.notifyItemRangeInserted(0,3);
                }
            }
        };
        mTimeLine.setLayoutManager(layoutManager);

        mTimeLine.addItemDecoration(new SpanItemDecoration(ScreenUtil.dp2px(getContext(),12),false,false,true,false));
        mTimeLine.setItemAnimator(new GiftTimeLineAnimator());

        if (sSaveData  == null){
            sSaveData = new ArrayList<>();
        }
        if (sData ==  null){
            sData = new ArrayList<>();
        }
        mAdapter = new GiftTimeLineAdapter(sSaveData);
        mAdapter.openLoadAnimation(new SlideLiftAnimation());
        mTimeLine.setAdapter(mAdapter);
        loadData();
        mHandler.post(mConsumer);
    }

    @Override
    public void onDestroy() {
        if (mHandler != null){
            mHandler.removeCallbacks(mConsumer);
        }
        sSaveData = mAdapter.getData();
        super.onDestroy();
    }

    public void resetData(){
        if (sData != null){
            for (int i = 0; i < sData.size(); i++) {
                sData.get(i).setShowed(false);
            }
        }
    }

    /**
     * 回收数据缓存
     */
    public void recyclerData(){
        sData = null;
        sSaveData = null;
        if (sQueue != null){
            sQueue.clear();
            sQueue = null;
        }
    }

    private void loadData(){
        if (sData == null || sData.size() == 0){
            DataManager.getGiftTimeLineList(mVideoId);
        }
    }
    //队列消费者
    private Handler mHandler = new Handler();
    private Runnable mConsumer = new Runnable() {
        @Override
        public void run() {
            if (sQueue != null){
                int showSize = mAdapter.getData().size();
                if (showSize == 3 && sQueue.size() > 0){
                    remove();
                    showSize--;
                }else {
                    if (sQueue.size() == 0){
                        remove();
                    }
                    if (showSize == 0){
                        mAdapter.setNewData(new ArrayList<TimeLineGiftEntity.DataBean>());
                    }
                }

                int queueSize = sQueue.size();
                for (int i = 0; i < (3 - showSize > queueSize?queueSize : 3 - showSize); i++) {
                    add(deQueue());
                }
            }else {
                sQueue = new ArrayBlockingQueue<>(128);
            }
            mHandler.postDelayed(mConsumer,mDelay);
        }
    };

    //时间轴礼物队列
    private static BlockingQueue<TimeLineGiftEntity.DataBean> sQueue ;
    private void enQueue(TimeLineGiftEntity.DataBean data){
        if (sQueue != null && data != null){
            try {
                sQueue.offer(data);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private TimeLineGiftEntity.DataBean deQueue(){
        return sQueue.poll();
    }

    /**
     *
     */
    private void remove(){
        if (mAdapter != null && mAdapter.getData().size() > 0){
            mAdapter.remove(0);
        }
    }

    private void add(TimeLineGiftEntity.DataBean data){
        if (mAdapter != null && data != null){
            mAdapter.addData(data);
        }
    }

    @Override
    public void onProgress(final long p) {
         System.out.println("Duration:"+p);
        final String node = String.valueOf((int) (p/1000));
        System.out.println("Node:"+node);
        if (sData != null){
            for (TimeLineGiftEntity.DataBean data:
                 sData) {
                if (node.equals(data.getVideo_node())  && !data.isShowed()){
                    data.setShowed(true);
                    enQueue(data);
                }
            }
        }
    }


    @Override
    protected IPullToRefresh getPullToRefresh() {
        return null;
    }

    @Override
    public void onClick(View v) {

    }

    /**
     * 插入一条打赏记录
     */
    public void onEventMainThread(TimeLineGiftEntity.DataBean data){
        if (mAdapter  == null){
            return;
        }
        if (data == null){
            return;
        }

        data.setShowed(true);
        //加入数据集合
        sData.add(data);
        if (mAdapter.getData() != null){
            //加入队列 等待消费
            enQueue(data);
        }
    }

    /**
     * 礼物时间轴
     */
    public void onEventMainThread(TimeLineGiftEntity entity){
        if (entity != null && entity.getData() != null && entity.getData().size() > 0){
             sData = entity.getData();
        }
    }


    /**
     * 重置礼物时间轴
     */
    public void onEventMainThread( ResetTimeLineEvent event){
       if (sData != null){
           for (int i = 0; i < sData.size(); i++) {
               sData.get(i).setShowed(false);
           }
       }
    }
}
