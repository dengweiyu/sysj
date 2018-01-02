/*
 *  Copyright (c) 2013 The CCP project authors. All Rights Reserved.
 *
 *  Use of this source code is governed by a Beijing Speedtong Information Technology Co.,Ltd license
 *  that can be found in the LICENSE file in the root of the web site.
 *
 *   http://www.yuntongxun.com
 *
 *  An additional intellectual property rights grant can be found
 *  in the file PATENTS.  All contributing project authors may
 *  be found in the AUTHORS file in the root of the source tree.
 */
package com.li.videoapplication.ui.dialog;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.li.videoapplication.R;
import com.li.videoapplication.data.EventManager;
import com.li.videoapplication.data.model.entity.Game;
import com.li.videoapplication.data.model.response.GameCateEntity;
import com.li.videoapplication.framework.BaseEmptyRightDialog;
import com.li.videoapplication.mvp.adapter.MatchListFliterAdapter;
import com.li.videoapplication.tools.ToastHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * 弹框：赛事筛选
 */
@SuppressLint("CutPasteId")
public class MatchFliterDialog extends BaseEmptyRightDialog implements View.OnClickListener {

    private RecyclerView gameRecy, eventRecy;
    private TextView tv_topbar_left;
    private TextView tv_topbar_mid;

    private MatchListFliterAdapter adapter_game, adapter_event;

    private List<Game> gameData = new ArrayList<>();
    private List<Game> eventData = new ArrayList<>();

    //选中游戏类型
    private static SparseBooleanArray gameSelectedPos = new SparseBooleanArray();
    private static SparseBooleanArray eventSelectedPos = new SparseBooleanArray();

    private int gameSelectedCount = 0;
    private int eventSelectedCount = 0;

    @Override
    protected int getContentView() {
        return R.layout.fragment_matchlist_fliter;
    }

    public MatchFliterDialog(Context context, GameCateEntity data) {
        super(context);
        gameData = data.getGameData();
        eventData = data.getEventsData();

        initView();

        initAdapter();

        addOnClickListener();
    }

    private void initView() {
        gameRecy = (RecyclerView) findViewById(R.id.fliter_recyclerview_game);
        eventRecy = (RecyclerView) findViewById(R.id.fliter_recyclerview_event);

        tv_topbar_left = (TextView) findViewById(R.id.fliter_topbar_left);
        tv_topbar_mid = (TextView) findViewById(R.id.fliter_topbar_middle);

        refreshTopBar(true);
    }

    private void initAdapter() {
        gameRecy.setLayoutManager(new GridLayoutManager(getContext(), 3));
        eventRecy.setLayoutManager(new GridLayoutManager(getContext(), 3));

        adapter_game = new MatchListFliterAdapter(gameData, gameSelectedPos);
        adapter_event = new MatchListFliterAdapter(eventData, eventSelectedPos);

        gameRecy.setAdapter(adapter_game);
        eventRecy.setAdapter(adapter_event);

        resetGameType();
        resetEventType();
    }

    private void addOnClickListener() {
        findViewById(R.id.fliter_default).setOnClickListener(this);
        findViewById(R.id.fliter_confirm).setOnClickListener(this);
        findViewById(R.id.fliter_topbar).setOnClickListener(this);

        gameRecy.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                Game game = (Game) adapter.getItem(position);

                if (isGameSelected(position)) {
                    gameSelectedPos.put(position, false);
                } else {
                    gameSelectedPos.put(position, true);
                }
                adapter.notifyItemChanged(position);//选中或取消点击的游戏

                if (game.getId().equals("0")) { //点击的是[全部游戏]
                    resetGameType();//重置所有选中，即单选[全部游戏]
                } else { //点击的是其他游戏
                    if (isGameSelected(0)) { //将[全部游戏]置为未选中
                        gameSelectedPos.put(0, false);
                        adapter.notifyItemChanged(0);
                    }
                }

                refreshTopBar(false);
            }

        });

        eventRecy.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (isEventSelected(position)) {
                    eventSelectedPos.put(position, false);
                } else {
                    eventSelectedPos.put(position, true);
                }
                adapter.notifyItemChanged(position);//选中或取消点击的游戏
                refreshTopBar(false);
            }

        });
    }

    private void refreshTopBar(boolean isDefault) {
        if (isDefault) {
            // 全部游戏 | 官方赛, 邀请赛
            tv_topbar_left.setText("默认：");
            tv_topbar_mid.setText(gameData.get(0).getName()
                    + " | " + eventData.get(0).getName()
                    + ", " + eventData.get(1).getName());
        } else {
            tv_topbar_left.setText("已选择：");
            String s = getGameSelectedNames() + getMatchTypeSelectedNames();
            tv_topbar_mid.setText(s);
        }
    }

    private boolean isGameSelected(int pos) {
        return gameSelectedPos.get(pos);
    }

    private boolean isEventSelected(int pos) {
        return eventSelectedPos.get(pos);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fliter_default:
                resetGameType();
                resetEventType();
                refreshTopBar(true);
                break;
            case R.id.fliter_confirm:
                if (getGameSelectedCount() <= 0) {
                    ToastHelper.s("请选择游戏类型");
                    return;
                }
                if (getEventSelectedCount() <= 0) {
                    ToastHelper.s("请选择赛事类型");
                    return;
                }
                hide();
                EventManager.postMatchListFliterEvent(getGameSelectedId(), getGameSelectedNames(),
                        getMatchTypeSelectedId(), getMatchTypeSelectedNames());
                gameSelectedCount = 0;
                eventSelectedCount = 0;
                break;
            case R.id.fliter_topbar:
                hide();
                break;
        }
    }

    private int getGameSelectedCount() {
        for (int i = 0; i < gameSelectedPos.size(); i++) {
            if (gameSelectedPos.valueAt(i)) {
                gameSelectedCount++;
            }
        }
        Log.d(tag, "getGameSelectedCount: " + gameSelectedCount);
        return gameSelectedCount;
    }

    private int getEventSelectedCount() {
        for (int i = 0; i < eventSelectedPos.size(); i++) {
            if (eventSelectedPos.valueAt(i)) {
                eventSelectedCount++;
            }
        }
        Log.d(tag, "getEventSelectedCount: " + eventSelectedCount);
        return eventSelectedCount;
    }

    //获得游戏选中id
    private String getGameSelectedId() {
        StringBuilder gameIds = new StringBuilder();
        for (int i = 0; i < gameData.size(); i++) {
            if (isGameSelected(i)) {
                gameIds.append(gameData.get(i).getId());
                gameIds.append(",");
            }
        }
        if (gameIds.length() == 0) { //全部没选
            gameSelectedPos.put(0, true);//选默认
            adapter_game.notifyItemChanged(0);
            refreshTopBar(false);
            return gameData.get(0).getId();
        } else {
            gameIds.deleteCharAt(gameIds.length() - 1);//删除最后多余的逗号
            return gameIds.toString();
        }
    }

    //获得游戏选中name
    private String getGameSelectedNames() {
        StringBuilder selectedGamesName = new StringBuilder();
        for (int i = 0; i < gameData.size(); i++) {
            if (isGameSelected(i)) {
                selectedGamesName.append(gameData.get(i).getName());
                selectedGamesName.append(", ");
            }
        }
        if (selectedGamesName.length() == 0) { //全部没选
            gameSelectedPos.put(0, true);//选默认
            adapter_game.notifyItemChanged(0);
            refreshTopBar(false);
            return gameData.get(0).getName() + " | ";
        } else {
            Log.d(tag, "selectedGamesName.length(): " + selectedGamesName.length());
            if (selectedGamesName.length() < 12) {//xxxx, xxxx,
                selectedGamesName.delete(selectedGamesName.length() - 2, selectedGamesName.length());//删除多余逗号
                selectedGamesName.append(" | ");
                return selectedGamesName.toString();
            } else {//xxxx, xxxx, xxxx
                return selectedGamesName.substring(0, 10) + "... | "; //xxxx, xxxx... |
            }
        }
    }

    //获得赛事类型选中条目的结果id
    private String getMatchTypeSelectedId() {
        StringBuilder enetIds = new StringBuilder();
        for (int i = 0; i < eventData.size(); i++) {
            if (isEventSelected(i)) {
                enetIds.append(eventData.get(i).getId());
                enetIds.append(",");
            }
        }
        if (enetIds.length() == 0) { //全部没选
            eventSelectedPos.put(0, true);//选默认
            adapter_event.notifyItemChanged(0);
            refreshTopBar(false);
            return eventData.get(0).getId();
        } else {
            enetIds.deleteCharAt(enetIds.length() - 1);//删除最后多余的逗号
            return enetIds.toString();
        }
    }

    //获得赛事类型选中条目的结果name
    private String getMatchTypeSelectedNames() {
        StringBuilder selectedEventsName = new StringBuilder();
        for (int i = 0; i < eventData.size(); i++) {
            if (isEventSelected(i)) {
                selectedEventsName.append(eventData.get(i).getName());
                selectedEventsName.append(", ");
            }
        }
        if (selectedEventsName.length() == 0) { //全部没选
            eventSelectedPos.put(0, true);//选默认
            adapter_event.notifyItemChanged(0);
            refreshTopBar(false);
            return eventData.get(0).getName();
        } else {
            //删除最后多余的 ", "
            selectedEventsName.delete(selectedEventsName.length() - 2, selectedEventsName.length());
            return selectedEventsName.toString();
        }
    }

    //默认选中【0:全部游戏】
    private void resetGameType() {
        for (int i = 0; i < gameData.size(); i++) {
            if (gameData.get(i).getId().equals("0")) {
                gameSelectedPos.put(i, true);
            } else {
                gameSelectedPos.put(i, false);
            }
        }
        adapter_game.setNewData(gameData);
    }

    //默认选中【1:官方赛,2:邀请赛】
    private void resetEventType() {
        for (int i = 0; i < eventData.size(); i++) {
            if (eventData.get(i).getId().equals("1") ||
                    eventData.get(i).getId().equals("2")) {
                eventSelectedPos.put(i, true);
            } else {
                eventSelectedPos.put(i, false);
            }
        }
        adapter_event.setNewData(eventData);
    }
}
