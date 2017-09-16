package com.li.videoapplication.ui.activity;

import android.graphics.Canvas;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.li.videoapplication.R;
import com.li.videoapplication.data.model.entity.SquareGameEntity;
import com.li.videoapplication.data.model.event.SquareFilterEvent;
import com.li.videoapplication.framework.TBaseActivity;
import com.li.videoapplication.mvp.adapter.SquareGameChoiceAdapter;
import com.li.videoapplication.ui.view.SimpleItemDecoration;

import io.rong.eventbus.EventBus;

/**
 * 玩家广场 游戏选择
 */

public class SquareGameChoiceActivity extends TBaseActivity implements View.OnClickListener {

    private SquareGameEntity mGame;

    private RecyclerView mList;

    private SquareGameChoiceAdapter mAdapter;

    @Override
    public void initView() {
        super.initView();
        initToolbar();

        mList = (RecyclerView)findViewById(R.id.rv_square_choice_list);

        mAdapter = new SquareGameChoiceAdapter(this,mGame.getData());
        mList.setLayoutManager(new GridLayoutManager(this,4));
        mList.setAdapter(mAdapter);

        mList.addItemDecoration(new SimpleItemDecoration(this,false,false,false,false){
            @Override
            public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
                int childCount = parent.getChildCount();
                for (int i = 0; i < childCount; i++) {
                    int width = 1;

                    if (mIsTop){
                        drawTop(c,parent.getChildAt(i),parent,width);
                    }
                    if (mIsBottom){
                        drawBottom(c,parent.getChildAt(i),parent,width);
                    }
                    if (mIsLeft){
                        drawLeft(c,parent.getChildAt(i),parent,width);
                    }
                    if (mIsRight){
                        drawRight(c,parent.getChildAt(i),parent,width);
                    }
                }
            }
        });
    }

    private void initToolbar(){
        setSystemBarBackgroundWhite();
        setTextViewText((TextView)findViewById(R.id.tb_title),"选择游戏");

        View close  = findViewById(R.id.tb_close);
        close.setOnClickListener(this);
        close.setVisibility(View.VISIBLE);

        findViewById(R.id.tb_back).setVisibility(View.GONE);
    }

    @Override
    public int getContentView() {
        return R.layout.activity_square_game_choice;
    }

    @Override
    public void afterOnCreate() {
        super.afterOnCreate();

        try {
            mGame = (SquareGameEntity) getIntent().getSerializableExtra("game");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        EventBus.getDefault().post(new SquareFilterEvent(mAdapter.getLastChoice()));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tb_close:
                finish();
                break;
        }
    }
}
