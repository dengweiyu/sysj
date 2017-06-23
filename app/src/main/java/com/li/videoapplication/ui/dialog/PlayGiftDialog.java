package com.li.videoapplication.ui.dialog;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import com.google.common.collect.Maps;
import com.li.videoapplication.R;
import com.li.videoapplication.data.image.GlideHelper;
import com.li.videoapplication.data.network.UITask;
import com.li.videoapplication.framework.BaseDialog;
import com.li.videoapplication.tools.TextImageHelper;

import java.util.Map;

/**
 * 赞赏
 */

public class PlayGiftDialog extends BaseDialog {

    private String mUrl;
    private ImageView mGiftIcon;
    private TextView mCountText;

    private Animation.AnimationListener mTimeListener;
    Animation.AnimationListener mGiftListener;
    private TextImageHelper mHelper;
    private int mCount;

    private static Map<String,Integer> sCountIcon;

    static {
        if(sCountIcon == null){
            sCountIcon = Maps.newHashMap();
            sCountIcon.put("X",R.drawable.play_gift_time_close);
            sCountIcon.put("0",R.drawable.play_gift_time_0);
            sCountIcon.put("1",R.drawable.play_gift_time_1);
            sCountIcon.put("2",R.drawable.play_gift_time_2);
            sCountIcon.put("3",R.drawable.play_gift_time_3);
            sCountIcon.put("4",R.drawable.play_gift_time_4);
            sCountIcon.put("5",R.drawable.play_gift_time_5);
            sCountIcon.put("6",R.drawable.play_gift_time_6);
            sCountIcon.put("7",R.drawable.play_gift_time_7);
            sCountIcon.put("8",R.drawable.play_gift_time_8);
            sCountIcon.put("9",R.drawable.play_gift_time_9);
        }
    }
    public PlayGiftDialog(Context context,String url,int count) {
        super(context,R.style.custom_dialog);
        this.mCount = count;
        mUrl = url;
        if (mGiftIcon != null){
            GlideHelper.displayImageWhite(context,mUrl,mGiftIcon);
        }
    }

    @Override
    public void show() {
        super.show();
        final Animation giftAnimation = AnimationUtils.loadAnimation(getContext(),R.anim.play_gift_scale_enter);
        giftAnimation.setAnimationListener(mGiftListener);

        final Animation timeAnimation = AnimationUtils.loadAnimation(getContext(),R.anim.play_gift_time_rotate);
        timeAnimation.setAnimationListener(mTimeListener);

        //
        UITask.postDelayed(new Runnable() {
            @Override
            public void run() {
                mGiftIcon.startAnimation(giftAnimation);
            }
        },50);

        //
        UITask.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mCount <= 0){
                    mCount = 1;
                }
                replaceIcon("X"+mCount,getContext(),mCountText);
                mCountText.startAnimation(timeAnimation);
            }
        },400);
    }

    @Override
    public void onBackPressed() {
       // nothing to do
    }

    @Override
    protected int getContentView() {
        return R.layout.dialog_play_gift;
    }

    @Override
    protected void afterContentView(Context context) {
        super.afterContentView(context);

        Window window = getWindow();

        if (window != null) {
            window.setBackgroundDrawableResource(android.R.color.transparent); // 设置对话框背景为透明
            window.clearFlags( WindowManager.LayoutParams.FLAG_DIM_BEHIND);
            WindowManager.LayoutParams params = window.getAttributes();
            // 根据x，y坐标设置窗口需要显示的位置
            params.x = 0; // x小于0左移，大于0右移
            params.y = 0; // y小于0上移，大于0下移
            params.gravity = Gravity.CENTER;
            window.setAttributes(params);
        }

        setCancelable(false);
        mCountText = (TextView)findViewById(R.id.tv_play_time);
        mGiftIcon = (ImageView) findViewById(R.id.iv_gift_icon);


        initListener();

    }

    private void initListener(){

        mTimeListener = new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                mCountText.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {

                mCountText.setVisibility(View.GONE);

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        };

        mGiftListener = new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mGiftIcon.setVisibility(View.GONE);
                dismiss();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        };
    }


    /**
     * text replace to drawable
     * @param s
     */
    public static void replaceIcon(String s,Context context,TextView countText){
        char array[] = s.toCharArray();
        SpannableString spannableString = new SpannableString(s);
        for (int i = 0 ;i < array.length;i++) {
            try {
                int resId = sCountIcon.get(new String(new char[]{array[i]}));
                if (resId >= 0){
                    Drawable drawable = context.getResources().getDrawable(resId);
                    drawable.setBounds(0, 0, (int)countText.getTextSize(), (int)countText.getTextSize());
                    ImageSpan span = new ImageSpan(drawable, ImageSpan.ALIGN_BOTTOM);
                    spannableString.setSpan(span,i,i+1, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                    Log.d("ResId",resId+"");
                }
            } catch (Resources.NotFoundException e) {
                e.printStackTrace();
            }
        }

        countText.setText(spannableString);
    }
}
