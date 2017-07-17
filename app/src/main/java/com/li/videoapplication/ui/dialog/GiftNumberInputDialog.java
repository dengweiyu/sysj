package com.li.videoapplication.ui.dialog;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.SimpleAdapter;

import com.li.videoapplication.R;
import com.li.videoapplication.data.model.event.InputNumberEvent;
import com.li.videoapplication.data.model.response.PlayGiftTypeEntity;
import com.li.videoapplication.data.network.UITask;
import com.li.videoapplication.ui.adapter.GiftNumberInputAdapter;
import com.li.videoapplication.utils.ScreenUtil;
import com.li.videoapplication.utils.StringUtil;
import io.rong.eventbus.EventBus;

import java.util.List;

import io.rong.imkit.model.Event;


/**
 * 选择赠送礼物的数量
 */

public class GiftNumberInputDialog extends PopupWindow implements View.OnClickListener ,GiftNumberInputAdapter.SizeListener {
    private View mRootView;
    private View mAnchor;
    private ListView mList;
    private Context mContext;
    private boolean isShowing = false;
    private List<PlayGiftTypeEntity.NumberSenseBean> mData;
    public GiftNumberInputDialog(Context context, View anchor, List<PlayGiftTypeEntity.NumberSenseBean> data) {
        super(context);
        mAnchor = anchor;
        mContext = context;
        mData = data;
        init(context,data);
    }

    private void init(Context context, final List<PlayGiftTypeEntity.NumberSenseBean> data){
        mRootView = LayoutInflater.from(context).inflate(R.layout.dialog_gift_number_input,null);
        mList = (ListView) mRootView.findViewById(R.id.lv_gift_input_number);
        if (data != null){
            mList.setAdapter(new GiftNumberInputAdapter(context,this,data));
        }

        //给一个初始化不然不会渲染视图
        setWidth(1);
        setHeight(1);
        setTouchable(true);
        setOutsideTouchable(true);
        setFocusable(true);
        setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        setContentView(mRootView);

        mList.setOnItemClickListener(mItemClickListener);
    }

    AdapterView.OnItemClickListener mItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            int number = 1;
            String numberStr = ((PlayGiftTypeEntity.NumberSenseBean)mList.getAdapter().getItem(position)).getNumber();
            if (!StringUtil.isNull(numberStr)){
                try {
                    number = Integer.parseInt(numberStr);
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            }
            io.rong.eventbus.EventBus.getDefault().post(new InputNumberEvent(number));
            dismiss();
        }
    };

    private int mWidth = 0;

    @Override
    public void onListener(int h, int w) {
        //更新大小
        setWidth(w);
        setHeight(h);
        mList.getLayoutParams().height = h;

        mWidth = w;

        //触发重绘
        dismiss();
        show();
    }

    public void showOrHide(){
        if (isShowing){
            dismiss();
        }else {
            show();
        }
    }

    public void setNewData(List<PlayGiftTypeEntity.NumberSenseBean> data){
        if (data != null && data.size() > 0){
            mList.setAdapter(new GiftNumberInputAdapter(mContext,this,data));
        }
    }

    private void show(){
        int[] location  = new int[2];
        isShowing = true;
        mAnchor.getLocationOnScreen(location);

        int offsetX = -mAnchor.getWidth();
        if (mWidth != 0){
            //保证参考view在中间
            offsetX = -(mWidth/2 - mAnchor.getWidth()/2);
        }

        //Y偏移
        int offsetY = ScreenUtil.dp2px(10);
        showAsDropDown(mAnchor,offsetX,offsetY,Gravity.TOP);
    }

    @Override
    public void dismiss() {
        super.dismiss();
        isShowing = false;
    }

    @Override
    public void onClick(View v) {
    }
}
