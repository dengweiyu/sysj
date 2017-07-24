package com.li.videoapplication.ui.dialog;

import android.content.Context;
import android.os.Bundle;
import android.provider.Contacts;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.widget.NestedScrollView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.li.videoapplication.R;
import com.li.videoapplication.data.network.UITask;
import com.li.videoapplication.ui.activity.CreatePlayWithOrderActivity;
import com.li.videoapplication.ui.adapter.SimpleChoiceAdapter;
import com.li.videoapplication.ui.view.WheelRecyclerView;

import java.util.List;

/**
 *
 * 双滚轮选择器
 */

public class SimpleDoubleChoiceDialog extends WheelBottomDialog {
    private SimpleChoiceAdapter mAdapterHour;

    private SimpleChoiceAdapter mAdapterMinute;

    private List<String> mDataHour;

    private List<String> mDataMinute;

    private WheelRecyclerView mHour;
    private WheelRecyclerView mMinute;

    private int mHourPosition;
    protected int mMinutePosition;

    private SimpleChoiceDialog.OnSelectedListener mListener;

    private CreatePlayWithOrderActivity mActivity;
    public SimpleDoubleChoiceDialog(@NonNull Context context, List<String> firstColumn, List<String> secondColumn) {
        super(context);

        if (context instanceof  CreatePlayWithOrderActivity){
            mActivity = (CreatePlayWithOrderActivity)context;
        }
        mDataHour = firstColumn;
        mDataMinute = secondColumn;
    }

    @Override
    public void show() {
        super.show();
        //回到当前选中的值
        if (mAdapterHour != null){
            //应该加上 占位
            mAdapterHour.smoothScrollByPosition(mHourPosition + mHour.getItemHolder(),true);

            //更新值
            mHour.setSelectPosition(mHourPosition);

        }

        if (mAdapterMinute != null){
            //应该加上 占位
            mAdapterMinute.smoothScrollByPosition(mMinutePosition + mMinute.getItemHolder(),true);
            //更新值
            mMinute.setSelectPosition(mMinutePosition);

        }
    }


    public void setHourPosition(int hourPosition) {
        mHourPosition = hourPosition;
        //更新值
        if(mHour != null){
            mHour.setSelectPosition(hourPosition);
        }

    }


    public void setMinutePosition(int minutePosition) {
        mMinutePosition = minutePosition;
        //更新值
        if (mMinute != null){
            mMinute.setSelectPosition(mMinutePosition);
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    @Override
    public void onChildView(View child) {
        super.onChildView(child);
        //重新绘制 : 位置
        View view = findViewById(R.id.tv_divide_char);
        if (view != null){
            int paddingTop = child.getMeasuredHeight() * (mList.getItemHolder())+child.getMeasuredHeight()/2-view.getMeasuredHeight()/2;
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)view.getLayoutParams();
            params.setMargins(0,paddingTop,0,0);
            view.setLayoutParams(params);
            view.invalidate();
        }
    }

    private void init(){
        setContentView(R.layout.dialog_double_choice);

        //启用阴影跟随
        resetCallback();

        //BottomSheetBehavior  findScrollingChild()可以知道 只会find第一个ScrollingChild 这样会导致 只有第一个列表能滚动 因此直接在外面套一层NestedScrollView并且屏蔽它的滚动 保证每个列表都能滚动
        final NestedScrollView root  = (NestedScrollView) findViewById(R.id.nsv_choice_root);
        mHour = (WheelRecyclerView)findViewById(R.id.wrv_hour);
        mMinute = (WheelRecyclerView)findViewById(R.id.wrv_minute);

        root.setNestedScrollingEnabled(false);
        //设置属性
        mList = mHour;

        if (mDataHour != null && mDataHour.size() > 0 ){
            mAdapterHour = new SimpleChoiceAdapter(getContext(),mHour,mDataHour);
            //启用分割线
            mHour.setMeasureChild(this);

            mHour.setAdapter(mAdapterHour);
        }

        //滚动发生改变
        mHour.setOnCurrentChangeListener(new WheelRecyclerView.onCurrentChangeListener() {
            @Override
            public void onChange(int position) {
                if (mActivity != null){
                    mActivity.onCurrentHourChange(position);
                }
            }
        });

        if (mDataMinute != null && mDataMinute.size() > 0 ){
            mAdapterMinute = new SimpleChoiceAdapter(getContext(),mMinute,mDataMinute);
            mMinute.setAdapter(mAdapterMinute);
        }

        TextView title = (TextView) findViewById(R.id.tv_choice_left);

        title.setText("开始时间");

        findViewById(R.id.tv_choice_right).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                mHourPosition = mHour.getSelectPosition();
                mMinutePosition = mMinute.getSelectPosition();
                if (mListener != null){
                    mListener.onSelected(SimpleChoiceDialog.TYPE_CHOICE_HOUR,mHourPosition);
                    mListener.onSelected(SimpleChoiceDialog.TYPE_CHOICE_MINUTE,mMinutePosition);
                }
            }
        });
    }

    public void notifyDataSetChange(){
        mAdapterHour = new SimpleChoiceAdapter(getContext(),mHour,mDataHour);
        mHour.setAdapter(mAdapterHour);
        notifyMinuteDataSetChange();


    }

    public void notifyMinuteDataSetChange(){
        mAdapterMinute = new SimpleChoiceAdapter(getContext(),mMinute,mDataMinute);
        mMinute.setAdapter(mAdapterMinute);

        mMinute.setSelectPosition(0);
    }

    public SimpleChoiceDialog.OnSelectedListener getListener() {
        return mListener;
    }

    public void setListener(SimpleChoiceDialog.OnSelectedListener listener) {
        mListener = listener;
    }
}
