package com.ifeimo.im.common.popupwindow;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.ifeimo.im.R;
import com.ifeimo.im.common.util.WindowUtil;

import java.io.PipedOutputStream;

/**
 * Created by lpds on 2017/1/19.
 */
@Deprecated
public class AddFriendPopupWindow  {

    PopupWindow popupWindow;

    Context context;

    View.OnClickListener onClickListener;

    View contentView;
    View addView;

    int height;
    int width;

    public AddFriendPopupWindow(Context context){
        this.context = context;
    }

    public PopupWindow init(){
        if(popupWindow == null){
            contentView = LayoutInflater.from(context).inflate(R.layout.popup_add,null);
            addView = contentView.findViewById(R.id.addFrientTv);
            addView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(onClickListener!=null){
                        onClickListener.onClick(view);
                    }
                }
            });
            popupWindow = new PopupWindow(contentView,
                    ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT)
            {
                @Override
                public void showAsDropDown(View anchor, int xoff, int yoff) {
                    super.showAsDropDown(anchor, xoff, yoff);
                    WindowUtil.addMark((Activity) context);
                }

                @Override
                public void dismiss() {
                    super.dismiss();
                    WindowUtil.removeMark((Activity) context);
                }
            };
            popupWindow.setBackgroundDrawable(new ColorDrawable(0x000000));
            popupWindow.setFocusable(true);
            popupWindow.setOutsideTouchable(true);

            contentView.measure(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
            height = contentView.getMeasuredHeight();
            width = contentView.getMeasuredWidth();
        }
        return popupWindow;
    }

    public AddFriendPopupWindow setOnClickListener(View.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
        return this;
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }
}
