package com.li.videoapplication.ui.activity;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.li.videoapplication.R;
import com.li.videoapplication.data.DataManager;
import com.li.videoapplication.data.model.entity.HomeGameSelectEntity;
import com.li.videoapplication.framework.TBaseAppCompatActivity;
import com.li.videoapplication.ui.adapter.ChoiceHomeTabAdapter;
import java.util.ArrayList;
import java.util.List;

/**
 *选择首页 tab
 */

public class ChoiceHomeTabActivity extends TBaseAppCompatActivity  {


    private RecyclerView mGameList;

    private List<MyMultiItemEntity> mData;

    private ChoiceHomeTabAdapter mAdapter;
    private List<HomeGameSelectEntity.ADataBean.MyGameBean> myGameList;
    private List<HomeGameSelectEntity.ADataBean.HotGameBean> hotGameList;

    @Override
    protected int getContentView() {
        return R.layout.activity_choice_home_tab;
    }

    @Override
    public void afterOnCreate() {
        super.afterOnCreate();

        myGameList=new ArrayList<>();
        hotGameList=new ArrayList<>();
        initToolBar();
        getData();
        mGameList = (RecyclerView) findViewById(R.id.rv_choice_game_list);
        mGameList.setLayoutManager( new LinearLayoutManager(this));
        mData = new ArrayList<>();

        MyMultiItemEntity entity1=new MyMultiItemEntity(MyMultiItemEntity.TYPE_TITLE_MYGAME);
        MyMultiItemEntity entity2=new MyMultiItemEntity(MyMultiItemEntity.TYPE_LIST_MYGAME);
        MyMultiItemEntity entity3=new MyMultiItemEntity(MyMultiItemEntity.TYPE_VIEW);
        MyMultiItemEntity entity4=new MyMultiItemEntity(MyMultiItemEntity.TYPE_TITLE_HOTGAME);
        MyMultiItemEntity entity5=new MyMultiItemEntity(MyMultiItemEntity.TYPE_LIST_HOTGAME);
        mData.add(entity1);mData.add(entity2);mData.add(entity3);mData.add(entity4);mData.add(entity5);

    }


    public void getData(){
        String member_id =getMember_id();
        DataManager.getGameToSelect(member_id);
    }
    public void onEventMainThread(HomeGameSelectEntity entity){
        hotGameList=entity.getAData().getHot_game();
        myGameList=entity.getAData().getMy_game();
        mAdapter = new ChoiceHomeTabAdapter(mData,myGameList,hotGameList);
        mGameList.setAdapter(mAdapter);

    }
    public static class MyMultiItemEntity implements MultiItemEntity {
        public final static int TYPE_TITLE_MYGAME = 0;
        public final static int TYPE_LIST_MYGAME = 1;
        public final static int TYPE_TITLE_HOTGAME = 4;
        public final static int TYPE_LIST_HOTGAME = 5 ;
        public final static int TYPE_VIEW = 3;

        private int mType;

        public MyMultiItemEntity(int type) {
            mType = type;
        }

        @Override
        public int getItemType() {
            return mType;
        }
    }
    public  void initToolBar(){
        TextView tv = (TextView) findViewById(R.id.tb_title);
        tv.setText("选择游戏");
        View view=findViewById(R.id.tb_back);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

}
