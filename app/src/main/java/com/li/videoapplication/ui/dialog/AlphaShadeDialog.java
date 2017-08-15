package com.li.videoapplication.ui.dialog;


import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;

import com.li.videoapplication.R;
import com.li.videoapplication.data.network.UITask;
import com.li.videoapplication.ui.activity.PlayWithOrderDetailActivity;
import com.li.videoapplication.utils.ScreenUtil;

import java.lang.reflect.Field;
import java.text.DecimalFormat;

/**
 * Created by liuwei on 2017/6/27.
 *
 * 阴影渐变Dialog
 */

public class AlphaShadeDialog extends BottomSheetDialog{
    protected BottomSheetBehavior<FrameLayout> mBehavior;
    private int mBehaviorState;

    protected Activity mActivity;
    public AlphaShadeDialog(@NonNull Context context) {
        super(context);
        init(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final Window window = getWindow();
        final WindowManager.LayoutParams params = window.getAttributes();
        params.width = ScreenUtil.getScreenWidth();
        window.setAttributes(params);
    }

    @Override
    public void show() {
        super.show();
        if (mBehavior != null){

            final Window window = getWindow();
            final WindowManager.LayoutParams params = window.getAttributes();
            params.dimAmount = 0.5f;
            params.width = ScreenUtil.getScreenWidth();
            window.setAttributes(params);


            if ( mBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED){
                mBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            }
        }

    }


    /**
     * must behind in setContentView invoke
     */
    protected void resetCallback(){
        if (mBehavior != null){
            return;
        }
        final Window window = getWindow();
        final WindowManager.LayoutParams params = window.getAttributes();
        params.width = ScreenUtil.getScreenWidth();
        window.setAttributes(params);

        Class parent = getClass();
        for (;;){
            if (parent.isAssignableFrom(BottomSheetDialog.class)){
                break;
            }
            if (parent == null){
                return;
            }
            parent = parent.getSuperclass();
        }
        try {
            if(parent == null){
                return;
            }
            Field behaviorField =  parent.getDeclaredField("mBehavior");
            behaviorField.setAccessible(true);
            mBehavior = (BottomSheetBehavior<FrameLayout>) behaviorField.get(this);
            if (mBehavior != null){
                mBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
                    @Override
                    public void onStateChanged(@NonNull View bottomSheet, int newState) {
                        if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                            cancel();

                        }else if (newState == BottomSheetBehavior.STATE_SETTLING){

                        }else if (newState == BottomSheetBehavior.STATE_EXPANDED){
                            params.dimAmount = 0.5f;
                            window.setAttributes(params);

                        }
                        mBehaviorState = newState;
                    }

                    @Override
                    public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                        // NaN do nothing
                        if (Double.isNaN(slideOffset)){
                            return;
                        }

                        if (mBehaviorState == BottomSheetBehavior.STATE_SETTLING && params.dimAmount == 0.5f){
                            return;
                        }

                        //slideOffset -1=>0

                        //0.5 is default value
                       float scale = (float) (0.5f+0.7*(slideOffset));
                        DecimalFormat decimalFormat=new DecimalFormat("##0.0");
                        scale = Float.parseFloat(decimalFormat.format(scale));
                        if (scale < 0){
                            scale = 0;
                        }

                        if (scale > 0.5f){
                            scale = 0.5f;
                        }

                        params.dimAmount = scale;
                        window.setAttributes(params);

                    }
                });
            }
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }catch(IllegalAccessException e){
            e.printStackTrace();
        }
    }

    private void init(Context context){
        final Window window = getWindow();

        //支持阴影
        window.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
    }
}
