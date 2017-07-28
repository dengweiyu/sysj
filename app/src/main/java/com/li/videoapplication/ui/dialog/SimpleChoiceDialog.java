package com.li.videoapplication.ui.dialog;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.view.KeyboardShortcutGroup;
import android.view.Menu;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.TextView;

import com.li.videoapplication.R;
import com.li.videoapplication.data.network.UITask;
import com.li.videoapplication.ui.adapter.SimpleChoiceAdapter;
import com.li.videoapplication.ui.view.WheelRecyclerView;

import java.util.List;

/**
 * 选择游戏大区
 */

public class SimpleChoiceDialog extends WheelBottomDialog {
    public final static int TYPE_CHOICE_SERVER = 1;
    public final static int TYPE_CHOICE_MODE = 2;
    public final static int TYPE_CHOICE_RANK = 3;
    public final static int TYPE_CHOICE_HOUR = 4;
    public final static int TYPE_CHOICE_MINUTE = 5;
    public final static int TYPE_CHOICE_COUNT = 6;
    private List<String> mData;

    private OnSelectedListener mListener;

    private SimpleChoiceAdapter mAdapter;

    private int mSelectPosition;
    private int mType;
    public SimpleChoiceDialog(@NonNull Context context, List<String> data,int type) {
        super(context);
        mData = data;
        mType = type;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    @Override
    public void show() {
        super.show();
        scrollByPosition();
    }

    private void  init(){
        setContentView(R.layout.dialog_choice_game_server);
        //启用阴影跟随
        resetCallback();

        mList = (WheelRecyclerView)findViewById(R.id.wrv_server_list);


        if (mData != null && mData.size() > 0){
            mAdapter = new SimpleChoiceAdapter(getContext(),mList, mData);
            mList.setAdapter(mAdapter);
            //启用分割线
            mList.setMeasureChild(this);
            mList.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    //滚动到默认项
                    UITask.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            scrollByPosition();
                        }
                    },100);
                    mList.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
            });

        }
        TextView left = (TextView) findViewById(R.id.tv_choice_left);
        String title = "";
        switch (mType){
            case TYPE_CHOICE_SERVER:
                title = "选择大区";
                break;
            case TYPE_CHOICE_MODE:
                title = "游戏模式";
                break;
            case TYPE_CHOICE_RANK:
                title = "陪练段位";
                break;
            case TYPE_CHOICE_COUNT:
                title = "选择局数";
                break;
        }
        left.setText(title);

        findViewById(R.id.tv_choice_right).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mBehavior != null){
                    mBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                }

                if (mListener != null){
                    mListener.onSelected(mType,mList.getSelectPosition());
                    mSelectPosition = mList.getSelectPosition();
                }
            }
        });
    }

    public void notifyDataSetChanged(){
        if (mAdapter != null){
            mAdapter.notifyDataSetChanged();
        }
    }

    private void scrollByPosition(){
        //回到当前选中的值
        if (mAdapter != null){
            //应该加上 占位
            mAdapter.smoothScrollByPosition(mSelectPosition + mList.getItemHolder(),true);
            //还原一下值
            mList.setSelectPosition(mSelectPosition);
            //还原一下颜色
           // mAdapter.notifyDataSetChanged();
        }
    }

    public int getSelectPosition() {
        return mSelectPosition;
    }

    public void setSelectPosition(int selectPosition) {
        mSelectPosition = selectPosition;
        scrollByPosition();
    }

    public OnSelectedListener getListener() {
        return mListener;
    }

    public void setListener(OnSelectedListener listener) {
        mListener = listener;
    }

    public interface OnSelectedListener{
        void onSelected(int type,int position);
    }
}
