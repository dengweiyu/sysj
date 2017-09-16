package com.li.videoapplication.ui.fragment;

import android.graphics.Color;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.chad.library.adapter.base.listener.SimpleClickListener;
import com.handmark.pulltorefresh.library.IPullToRefresh;
import com.li.videoapplication.R;
import com.li.videoapplication.data.DataManager;
import com.li.videoapplication.data.model.event.WelcomeScrollEvent;
import com.li.videoapplication.data.model.response.FocusGameListEntity;
import com.li.videoapplication.data.preferences.Constants;
import com.li.videoapplication.data.preferences.UserPreferences;
import com.li.videoapplication.framework.TBaseFragment;
import com.li.videoapplication.ui.ActivityManager;

import com.li.videoapplication.ui.adapter.ChoiceFocusGameAdapter;
import com.li.videoapplication.utils.StringUtil;

import io.rong.eventbus.EventBus;

/**
 *
 */

public class WelcomeChoiceGameFragment extends TBaseFragment implements View.OnClickListener {

    private RecyclerView mGameList;

    private FocusGameListEntity mFocusGameList;

    private ChoiceFocusGameAdapter mAdapter;
    private TextView mChoiceDone;
    @Override
    protected int getCreateView() {
        return R.layout.activity_choice_focus_game;
    }

    @Override
    protected void initContentView(View view) {
        initToolbar(view);
        mChoiceDone = (TextView) view.findViewById(R.id.tv_choice_done);
        mChoiceDone.setOnClickListener(this);
        mGameList = (RecyclerView)view.findViewById(R.id.rv_choice_game_list);
        mGameList.setLayoutManager(new GridLayoutManager(getActivity(),3));

        DataManager.focusGameList();

        mGameList.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void SimpleOnItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {

                if (mFocusGameList != null){
                    boolean isChoice =  !mAdapter.getData().get(i).isChoice();
                    mAdapter.getData().get(i).setChoice(isChoice);
                    if (mAdapter != null){
                        mAdapter.setNewData(mAdapter.getData());
                    }

                    mChoiceCount = isChoice?mChoiceCount+1:mChoiceCount-1;
                    refreshButton();
                }
            }
        });


    }

    private void initToolbar(View view){
        View root = view.findViewById(R.id.rl_toolbar);
        root.setBackgroundColor(Color.parseColor("#ff3f3f"));
        view.findViewById(R.id.tb_back).setVisibility(View.GONE);
        TextView title = (TextView)view.findViewById(R.id.tb_title);
        title.setText("关注游戏");
        title.setTextColor(Color.WHITE);

        TextView go = (TextView)view.findViewById(R.id.tb_topup_record);
        go.setTextColor(Color.WHITE);
        go.setText("跳过");
        go.setVisibility(View.VISIBLE);
        go.setOnClickListener(this);
    }

    private void refreshButton(){
        if (mChoiceCount > 0){
            mChoiceDone.setEnabled(true);
            mChoiceDone.setBackgroundResource(R.drawable.welcome_gointo);
        }else {
            mChoiceDone.setEnabled(false);
            mChoiceDone.setBackgroundResource(R.drawable.welcome_unselect);
        }
    }

    @Override
    protected IPullToRefresh getPullToRefresh() {
        return null;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tb_topup_record:
                EventBus.getDefault().post(new WelcomeScrollEvent(2));
                break;
            case R.id.tv_choice_done:
                //保存问卷
                String ids = getGroupIds();
                if (!StringUtil.isNull(ids)){
                    UserPreferences.getInstance().putString(Constants.GROUP_IDS_NEW, ids);
                }
                EventBus.getDefault().post(new WelcomeScrollEvent(2));
                break;
        }
    }

    private String getGroupIds(){
        String ids = "";
        if (mFocusGameList != null){
            for (int i = 0; i < mFocusGameList.getAData().size(); i++) {
                if (mFocusGameList.getAData().get(i).isChoice()){
                    ids += mFocusGameList.getAData().get(i).getGroup_id();
                    if (i != mFocusGameList.getAData().size() - 1){
                        ids += ",";
                    }
                }
            }
        }
        return ids;
    }

    private int mChoiceCount = 0;
    public void onEventMainThread(FocusGameListEntity entity){
        if(entity.isResult() && entity.getAData() != null && entity.getAData().size() > 0){
            mFocusGameList = entity;
            mChoiceCount = 0;
            if (mFocusGameList != null){
                for (int i = 0; i < mFocusGameList.getAData().size(); i++) {
                    if (i < 2){
                        mFocusGameList.getAData().get(i).setChoice(true);
                        mChoiceCount++;
                    }else {
                        break;
                    }
                }
            }
            refreshButton();
            mAdapter = new ChoiceFocusGameAdapter(mFocusGameList.getAData());
            mGameList.setAdapter(mAdapter);
        }
    }
}
