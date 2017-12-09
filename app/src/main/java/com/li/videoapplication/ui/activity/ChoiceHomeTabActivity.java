package com.li.videoapplication.ui.activity;

import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.li.videoapplication.R;
import com.li.videoapplication.data.DataManager;
import com.li.videoapplication.data.model.entity.HomeGameSelectEntity;
import com.li.videoapplication.framework.AppConstant;
import com.li.videoapplication.framework.TBaseAppCompatActivity;
import com.li.videoapplication.tools.ToastHelper;
import com.li.videoapplication.ui.adapter.ChoiceHomeTabAdapter;

import java.util.ArrayList;
import java.util.List;

import io.rong.eventbus.EventBus;

/**
 * 选择首页 tab
 */

public class ChoiceHomeTabActivity extends TBaseAppCompatActivity {


    private RecyclerView mGameList;
    private TextView mTvSaveEdit;

    private List<MyMultiItemEntity> mData;

    private ChoiceHomeTabAdapter mAdapter;
    private List<HomeGameSelectEntity.ADataBean.MyGameBean> myGameList;
    private List<HomeGameSelectEntity.ADataBean.HotGameBean> hotGameList;

    private boolean tvSaveEditState = false; //true:save||false:edit

    @Override
    protected int getContentView() {
        return R.layout.activity_choice_home_tab;
    }

    @Override
    public void afterOnCreate() {
        super.afterOnCreate();

        myGameList = new ArrayList<>();
        hotGameList = new ArrayList<>();
        initToolBar();
        getData();
        mTvSaveEdit = (TextView) findViewById(R.id.tv_save_edit);
        mTvSaveEdit.setVisibility(View.VISIBLE);
        mGameList = (RecyclerView) findViewById(R.id.rv_choice_game_list);
        mGameList.setLayoutManager(new LinearLayoutManager(this));
        mData = new ArrayList<>();

        MyMultiItemEntity entity1 = new MyMultiItemEntity(MyMultiItemEntity.TYPE_TITLE_MYGAME);
        MyMultiItemEntity entity2 = new MyMultiItemEntity(MyMultiItemEntity.TYPE_LIST_MYGAME);
        MyMultiItemEntity entity3 = new MyMultiItemEntity(MyMultiItemEntity.TYPE_VIEW);
        MyMultiItemEntity entity4 = new MyMultiItemEntity(MyMultiItemEntity.TYPE_TITLE_HOTGAME);
        MyMultiItemEntity entity5 = new MyMultiItemEntity(MyMultiItemEntity.TYPE_LIST_HOTGAME);
        mData.add(entity1);
        mData.add(entity2);
        mData.add(entity3);
        mData.add(entity4);
        mData.add(entity5);

        mTvSaveEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isLogin()) {
                    ToastHelper.s("请先登录");
                    setTvSaveEditState(); //登录搞好后删除该行
                } else {
                    if (tvSaveEditState) {
                        StringBuffer sb = new StringBuffer();
                        for (int i = 0; i < myGameList.size(); i++) {
                            HomeGameSelectEntity.ADataBean.MyGameBean myGameBean = myGameList.get(i);
                            Log.i(tag, myGameBean.getName());
                            sb.append(myGameBean.getColumn_id());
                            if (i < (myGameList.size() - 1)) {
                                sb.append(",");
                            }
                        }
                        Log.w(tag, sb.toString());
                        DataManager.saveMyGameList(getMember_id(), sb.toString());
                        EventBus.getDefault().post(myGameList);
                    }
                    setTvSaveEditState();
                }
            }
        });
    }

    public void setTvSaveEditState() {

        if (tvSaveEditState) {
            mTvSaveEdit.setText(getApplicationContext().getString(R.string.mypersonalinfo_edit));
            mTvSaveEdit.setTextColor(ContextCompat.getColor(this, R.color.menu_main_red));
            mTvSaveEdit.setBackground(ContextCompat.getDrawable(this, R.drawable.button_edit));
            tvSaveEditState = false;
            mAdapter.setEditState(false);  //按钮为编辑时，里面item为非编辑状态
        } else {
            mTvSaveEdit.setText(getApplicationContext().getString(R.string.rc_confirm));
            mTvSaveEdit.setTextColor(ContextCompat.getColor(this, R.color.white));
            mTvSaveEdit.setBackground(ContextCompat.getDrawable(this, R.drawable.button_save));
            tvSaveEditState = true;
            mAdapter.setEditState(true);
        }
    }

    public void getData() {
        String member_id = getMember_id();
        DataManager.getGameToSelect(member_id);
    }

    public void onEventMainThread(HomeGameSelectEntity entity) {
        hotGameList = entity.getAData().getHot_game();
        myGameList = entity.getAData().getMy_game();
        mAdapter = new ChoiceHomeTabAdapter(mData, myGameList, hotGameList);
        mGameList.setAdapter(mAdapter);
        for (int i = 0; i < myGameList.size(); i++) {
            HomeGameSelectEntity.ADataBean.MyGameBean myGameBean = myGameList.get(i);
            Log.i(tag, myGameBean.getName());
        }

    }

    public static class MyMultiItemEntity implements MultiItemEntity {
        public final static int TYPE_TITLE_MYGAME = 0;
        public final static int TYPE_LIST_MYGAME = 1;
        public final static int TYPE_TITLE_HOTGAME = 4;
        public final static int TYPE_LIST_HOTGAME = 5;
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

    public void initToolBar() {
        TextView tv = (TextView) findViewById(R.id.tb_title);
        tv.setText("选择游戏");
        View view = findViewById(R.id.tb_back);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void onDestroy() {

        super.onDestroy();
    }
}
