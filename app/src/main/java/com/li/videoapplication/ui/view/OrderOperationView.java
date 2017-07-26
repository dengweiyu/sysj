package com.li.videoapplication.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.li.videoapplication.R;
import com.li.videoapplication.data.model.response.PlayWithOrderDetailEntity;
import com.li.videoapplication.impl.OrderContext;
import com.li.videoapplication.interfaces.IShowDialogListener;

/**
 *订单操作
 */
public class OrderOperationView extends LinearLayout implements View.OnClickListener {
    private OrderContext mOrderContext = new OrderContext() ;
    private TextView mLeftButton;
    private TextView mRightButton;
    public OrderOperationView(Context context) {
        super(context);
        init();
    }

    public OrderOperationView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public OrderOperationView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init(){
        View view = LayoutInflater.from(getContext()).inflate(R.layout.layout_coach_operation,this);

        View root  = view.findViewById(R.id.ll_coach_operation);
        root.setVisibility(VISIBLE);
        mLeftButton = (TextView)view.findViewById(R.id.tv_chat_with_coach);
        mRightButton = (TextView)view.findViewById(R.id.tv_coach_selected);

        mLeftButton.setVisibility(GONE);
        mRightButton.setVisibility(GONE);
        mLeftButton.setOnClickListener(this);
        mRightButton.setOnClickListener(this);
    }


    public void refreshOrder(PlayWithOrderDetailEntity entity,int role){
        mOrderContext.setOrderState(entity,role);


        mOrderContext.renderLeftButton(mLeftButton);
        mOrderContext.renderRightButton(mRightButton);
    }

    @Override
    public void onClick(View v) {
        if (v == mLeftButton){
            mOrderContext.onClickLeftButton();
        }else if (v == mRightButton){
            mOrderContext.onClickRightButton();
        }
    }



    public void setListener(IShowDialogListener listener) {
        mOrderContext.setShowDialogListener(listener);
    }
}
