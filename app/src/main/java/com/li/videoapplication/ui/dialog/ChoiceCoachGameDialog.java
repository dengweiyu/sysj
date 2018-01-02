package com.li.videoapplication.ui.dialog;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.OvershootInterpolator;
import android.view.animation.ScaleAnimation;
import android.view.animation.Transformation;
import android.widget.AdapterView;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.li.videoapplication.R;
import com.li.videoapplication.data.model.event.SwitchChoiceGameEvent;
import com.li.videoapplication.ui.activity.MainActivity;
import com.li.videoapplication.ui.adapter.ChoiceCoachGameListAdapter;
import com.li.videoapplication.utils.ScreenUtil;

import java.util.ArrayList;
import java.util.List;

import io.rong.eventbus.EventBus;

/**
 * 选择陪练游戏
 */

public class ChoiceCoachGameDialog extends PopupWindow{

    private View mRootView;
    private ListView mGameList;
    private Activity mContext;
    private View mAnchor;
    private int mTargetHeight;

    private List<String > mData;

    private ChoiceCoachGameListAdapter mAdapter;
    public ChoiceCoachGameDialog(Activity context, View anchor){
        super();
        mContext = context;
        mAnchor = anchor;
        init();
    }

    private void init(){
        mRootView = LayoutInflater.from(mContext).inflate(R.layout.dialog_choice_coach_game,null);
        mGameList = (ListView) mRootView.findViewById(R.id.lv_game_list);

        //屏蔽默认动画效果
        setAnimationStyle(R.style.ChoiceGamePopupAnimation);
        setTouchable(true);
        setOutsideTouchable(true);
        setFocusable(true);
        setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        mGameList.setOnItemClickListener(mOnItemClickListener);

        int width = mAnchor.getMeasuredWidth();
        setWidth(width);
        setContentView(mRootView);


    }

    public void show(List<String> data){
        if (data == null || data.size() == 0){
            return;
        }

        mIsDismiss = false;

        mData = data;

        //加上分割线高度
        final int height = mAnchor.getMeasuredHeight();

        mTargetHeight = height * mData.size() + mData.size();
        setHeight(mTargetHeight);

        final LinearLayout.LayoutParams params = (LinearLayout.LayoutParams)mGameList.getLayoutParams();
        params.width = getWidth();
        params.height = height;
        mGameList.setLayoutParams(params);


        mAdapter = new ChoiceCoachGameListAdapter(mContext,mData,mAnchor.getMeasuredWidth());
        mGameList.setAdapter(mAdapter);

        int offsetX = 0;
        //Y偏移
        int offsetY = - height;
        showAsDropDown(mAnchor, offsetX, offsetY,Gravity.BOTTOM);

        ValueAnimator animator = ValueAnimator.ofFloat(0.333f,1f);
        animator.setInterpolator(new AccelerateDecelerateInterpolator());
        animator.setDuration(300);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (float)animation.getAnimatedValue();
                params.height = (int) (mTargetHeight * value);
                mGameList.setLayoutParams(params);

                float scale = 1- ((value - 0.333f) / (0.667f))*0.5f;
                System.out.println("scale:"+scale);
                if (mContext instanceof MainActivity && scale >= 0.5f && !mIsDismiss){
                    MainActivity activity = (MainActivity)mContext;
                    WindowManager.LayoutParams params = activity.getWindow().getAttributes();
                    params.alpha = scale;
                    activity.getWindow().setAttributes(params);
                }
            }
        });
        animator.start();
    }

    final AdapterView.OnItemClickListener mOnItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            if (position == 0){
                return;
            }

            mData.set(0,mData.get(position));
            mAdapter.notifyDataSetChanged();
            EventBus.getDefault().post(new SwitchChoiceGameEvent(position - 1));

            final LinearLayout.LayoutParams params = (LinearLayout.LayoutParams)mGameList.getLayoutParams();
            ValueAnimator animator = ValueAnimator.ofFloat(1f,0.333f);
            animator.setInterpolator(new AccelerateDecelerateInterpolator());
            animator.setDuration(300);
            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    float value = (float)animation.getAnimatedValue();
                    params.height = (int) (mTargetHeight*value);
                    mGameList.setLayoutParams(params);

                    if (value <= 0.333f){
                        dismiss();
                    }

                    float scale = 0.5f + ((1f - value) / (0.667f))*0.5f;
                    if (mContext instanceof MainActivity && scale >= 0.5f && !mIsDismiss){
                        MainActivity activity = (MainActivity)mContext;
                        WindowManager.LayoutParams params = activity.getWindow().getAttributes();
                        params.alpha = scale;
                        activity.getWindow().setAttributes(params);
                    }
                }
            });
            animator.start();
        }
    };

    @Override
    public void setOnDismissListener(OnDismissListener onDismissListener) {

        super.setOnDismissListener(mDismissListenerProxy);
        mDismissListener = onDismissListener;
    }

    private OnDismissListener mDismissListener;
    private boolean mIsDismiss;

    final  OnDismissListener mDismissListenerProxy = new OnDismissListener() {
        @Override
        public void onDismiss() {
            if (mDismissListener != null){
                mDismissListener.onDismiss();
            }
            mIsDismiss = true;
        }
    };
}
