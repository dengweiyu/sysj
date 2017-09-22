package com.li.videoapplication.ui.activity;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.li.videoapplication.R;
import com.li.videoapplication.framework.TBaseAppCompatActivity;
import com.li.videoapplication.ui.adapter.ChoiceHomeTabAdapter;
import java.util.ArrayList;
import java.util.List;

/**
 *选择首页 tab
 */

public class ChoiceHomeTabActivity extends TBaseAppCompatActivity {


    private RecyclerView mGameList;

    private List<MyMultiItemEntity> mData;

    private ChoiceHomeTabAdapter mAdapter;
    @Override
    protected int getContentView() {
        return R.layout.activity_choice_home_tab;
    }


    @Override
    public void afterOnCreate() {
        super.afterOnCreate();
        mGameList = (RecyclerView) findViewById(R.id.rv_choice_game_list);

        mGameList.setLayoutManager( new LinearLayoutManager(this));
        mData = new ArrayList<>();

        for (int i = 0; i < 4; i++) {
            MyMultiItemEntity entity ;
            if (i== 0 || i == 2){
                entity = new MyMultiItemEntity(MyMultiItemEntity.TYPE_TITLE);
            }else {
                entity = new MyMultiItemEntity(MyMultiItemEntity.TYPE_LIST);
            }
            mData.add(entity);
        }

        mAdapter = new ChoiceHomeTabAdapter(mData);

        mGameList.setAdapter(mAdapter);
    }


   public static class MyMultiItemEntity implements MultiItemEntity {
        public final static int TYPE_TITLE = 1;
        public final static int TYPE_LIST = 2;

        private int mType;

        public MyMultiItemEntity(int type) {
            mType = type;
        }

        @Override
        public int getItemType() {
            return mType;
        }
    }
}
