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
import android.graphics.Color;
import android.view.View;
import android.widget.TextView;

import com.li.videoapplication.R;
import com.li.videoapplication.data.network.RequestConstant;
import com.li.videoapplication.framework.BaseBottomDialog;

/**
 * 弹框：游戏分类
 */
@SuppressLint("CutPasteId")
public class ClassifiedGameDialog extends BaseBottomDialog implements View.OnClickListener {

    private TextView hotGame, newGame;

    private View.OnClickListener hotClickListener, newClickListener;

    public ClassifiedGameDialog(Context context, String sort,
                                View.OnClickListener hotClickListener, View.OnClickListener newClickListener) {
        super(context);
        this.hotClickListener = hotClickListener;
        this.newClickListener = newClickListener;

        if (sort.equals(RequestConstant.GAMELIST_SORT_TIME)) {
            newGame.setTextColor(Color.parseColor("#2d9fe4"));//blue
            hotGame.setTextColor(Color.parseColor("#686868"));//black
        } else if (sort.equals(RequestConstant.GAMELIST_SORT_HOT)) {
            newGame.setTextColor(Color.parseColor("#686868"));
            hotGame.setTextColor(Color.parseColor("#2d9fe4"));
        }
    }

    @Override
    protected int getContentView() {
        return R.layout.dialog_classifiedgame;
    }

    @Override
    protected void afterContentView(Context context) {
        super.afterContentView(context);

        hotGame = (TextView) findViewById(R.id.classifiedgame_hot);
        newGame = (TextView) findViewById(R.id.classifiedgame_new);

        hotGame.setOnClickListener(this);
        newGame.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.classifiedgame_hot:
                if (hotClickListener != null) {
                    hotClickListener.onClick(v);
                }
                cancel();
                break;

            case R.id.classifiedgame_new:
                if (newClickListener != null) {
                    newClickListener.onClick(v);
                }
                cancel();
                break;
        }
    }
}
