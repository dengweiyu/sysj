package com.li.videoapplication.ui.adapter;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.callback.ItemDragAndSwipeCallback;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;
import com.li.videoapplication.R;
import com.li.videoapplication.data.model.entity.HomeGameSelectEntity;
import com.li.videoapplication.mvp.adapter.ChoiceHomeTabGameAdapter;
import com.li.videoapplication.ui.activity.ChoiceHomeTabActivity;

import java.util.List;

/**
 *首页选择Tab
 */

public class ChoiceHomeTabAdapter extends BaseMultiItemQuickAdapter<ChoiceHomeTabActivity.MyMultiItemEntity,BaseViewHolder> {
    private final int LINE_COUNT = 4;
    private List<HomeGameSelectEntity.ADataBean.MyGameBean> myGameList;
    private List<HomeGameSelectEntity.ADataBean.HotGameBean> hotGameList;
    private ChoiceHomeTabGameAdapter mMyGameAdapter;
    private ChoiceHomeTabHotGameAdapter mHotGameAdapter;

    private ItemTouchHelper mMyGameTouchHelper;
    private ItemTouchHelper mHotGameTouchHelper;

    public ChoiceHomeTabAdapter(List<ChoiceHomeTabActivity.MyMultiItemEntity> data,List<HomeGameSelectEntity.ADataBean.MyGameBean> myGameList,List<HomeGameSelectEntity.ADataBean.HotGameBean> hotGameList) {
        super(data);
        addItemType(ChoiceHomeTabActivity.MyMultiItemEntity.TYPE_TITLE_MYGAME,R.layout.adapter_choice_home_tab_header);
        addItemType(ChoiceHomeTabActivity.MyMultiItemEntity.TYPE_LIST_MYGAME,R.layout.adapter_choice_home_tab);
        addItemType(ChoiceHomeTabActivity.MyMultiItemEntity.TYPE_VIEW,R.layout.adapter_choice_view);
        addItemType(ChoiceHomeTabActivity.MyMultiItemEntity.TYPE_TITLE_HOTGAME,R.layout.adapter_choice_title_hot_game);
        addItemType(ChoiceHomeTabActivity.MyMultiItemEntity.TYPE_LIST_HOTGAME,R.layout.adapter_choice_home_tab);
        this.myGameList=myGameList;
        this.hotGameList=hotGameList;
        mMyGameAdapter = new ChoiceHomeTabGameAdapter(myGameList);
        mHotGameAdapter = new ChoiceHomeTabHotGameAdapter(hotGameList);
    }

    public void setEditState(boolean isEditState) {
        if (isEditState) {
            mMyGameAdapter.enableDragItem(mMyGameTouchHelper);
//            mHotGameAdapter.enableDragItem(mHotGameTouchHelper);
        } else {
            mMyGameAdapter.disableDragItem();
//            mHotGameAdapter.disableDragItem();
        }
        mMyGameAdapter.setEditState(isEditState);
        mHotGameAdapter.setEditState(isEditState);
        mMyGameAdapter.notifyDataSetChanged();
        mHotGameAdapter.notifyDataSetChanged();
    }

    @Override
    protected void convert(BaseViewHolder holder, ChoiceHomeTabActivity.MyMultiItemEntity myMultiItemEntity) {

        switch (myMultiItemEntity.getItemType()){
            case ChoiceHomeTabActivity.MyMultiItemEntity.TYPE_TITLE_MYGAME:
                break;
            case ChoiceHomeTabActivity.MyMultiItemEntity.TYPE_TITLE_HOTGAME:
                break;
            case ChoiceHomeTabActivity.MyMultiItemEntity.TYPE_VIEW:
                break;
            case ChoiceHomeTabActivity.MyMultiItemEntity.TYPE_LIST_MYGAME://我的游戏
                //我的游戏
                RecyclerView gameList = holder.getView(R.id.rv_game_list);
                gameList.setLayoutManager(new GridLayoutManager(mContext,LINE_COUNT));
                //这里传入newArrayList就能显示了
                ItemDragAndSwipeCallback callback =  new ItemDragAndSwipeCallback(mMyGameAdapter);
                mMyGameTouchHelper =  new ItemTouchHelper(callback);
                mMyGameTouchHelper.attachToRecyclerView(gameList);
                mMyGameAdapter.enableDragItem(mMyGameTouchHelper);
                mMyGameAdapter.disableDragItem(); //默认不能拉动
                gameList.setAdapter(mMyGameAdapter);
                gameList.addOnItemTouchListener(new OnItemChildClickListener() {
                    @Override
                    public void SimpleOnItemChildClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                        HomeGameSelectEntity.ADataBean.MyGameBean myGameBean = myGameList.get(i);
                        myGameList.remove(i);
                        HomeGameSelectEntity.ADataBean.HotGameBean hotGameBean
                                = new HomeGameSelectEntity.ADataBean.HotGameBean();
                        hotGameBean.setColumn_id(myGameBean.getColumn_id());
                        hotGameBean.setIco_pic(myGameBean.getIco_pic());
                        hotGameBean.setName(myGameBean.getName());
                        hotGameBean.setType(myGameBean.getType());
                        hotGameList.add(hotGameBean);
                        Log.i("ChoiceHomeTabAdapter", hotGameList.get(hotGameList.size()-1).getName());
                        mHotGameAdapter.notifyDataSetChanged();
                        mMyGameAdapter.notifyDataSetChanged();
                    }
                });

                break;
            case ChoiceHomeTabActivity.MyMultiItemEntity.TYPE_LIST_HOTGAME://热门游戏
                RecyclerView homeGameList = holder.getView(R.id.rv_game_list);
                homeGameList.setLayoutManager(new GridLayoutManager(mContext,LINE_COUNT)); //hotGameList.size()
                ItemDragAndSwipeCallback hotCallback =  new ItemDragAndSwipeCallback(mHotGameAdapter);
                mHotGameTouchHelper =  new ItemTouchHelper(hotCallback);
                mHotGameTouchHelper.attachToRecyclerView(homeGameList);
                mHotGameAdapter.enableDragItem(mHotGameTouchHelper);
                mHotGameAdapter.disableDragItem(); //热门游戏先不拖动
                homeGameList.setAdapter(mHotGameAdapter);
                homeGameList.addOnItemTouchListener(new OnItemChildClickListener() {
                    @Override
                    public void SimpleOnItemChildClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                        HomeGameSelectEntity.ADataBean.HotGameBean hotGameBean = hotGameList.get(i);
                        hotGameList.remove(i);
                        HomeGameSelectEntity.ADataBean.MyGameBean myGameBean
                                = new HomeGameSelectEntity.ADataBean.MyGameBean();
                        myGameBean.setColumn_id(hotGameBean.getColumn_id());
                        myGameBean.setIco_pic(hotGameBean.getIco_pic());
                        myGameBean.setName(hotGameBean.getName());
                        myGameBean.setType(hotGameBean.getType());
                        myGameList.add(myGameBean);
                        mHotGameAdapter.notifyDataSetChanged();
                        mMyGameAdapter.notifyDataSetChanged();

                    }
                });

                break;
        }
    }

}
