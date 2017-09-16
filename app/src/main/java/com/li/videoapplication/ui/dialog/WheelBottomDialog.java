package com.li.videoapplication.ui.dialog;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.RelativeLayout;

import com.li.videoapplication.R;
import com.li.videoapplication.ui.view.WheelRecyclerView;

/**
 * Created by liuwei on 2017/6/28.
 * 底部滚轮选择器
 */

public class WheelBottomDialog extends AlphaShadeDialog implements WheelRecyclerView.OnMeasureChild {
    protected WheelRecyclerView mList;

    public WheelBottomDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    public void onChildView(View child) {
        //绘制需要的分割线
        View top = findViewById(R.id.v_divider_top);
        View bottom = findViewById(R.id.v_divider_bottom);

        if (top == null || bottom == null){
            return;
        }

        int paddingTop = child.getMeasuredHeight() * (mList.getItemHolder()) ;

        RelativeLayout.LayoutParams topParams = (RelativeLayout.LayoutParams)top.getLayoutParams();
        topParams.setMargins(top.getLeft(),paddingTop,top.getRight(),top.getBottom());


        RelativeLayout.LayoutParams bottomParams = (RelativeLayout.LayoutParams)bottom.getLayoutParams();
        topParams.setMargins(0,paddingTop,0,0);

        bottomParams.setMargins(0,paddingTop+child.getMeasuredHeight(),0,0);


        top.setLayoutParams(topParams);
        bottom.setLayoutParams(bottomParams);

        top.invalidate();
        bottom.invalidate();


    }
}
