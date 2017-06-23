package com.li.videoapplication.ui.view;

import android.animation.ObjectAnimator;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;

/**
 * 时间线动画  在平滑移动的过程中增加透明度变化
 */

public class GiftTimeLineAnimator extends DefaultItemAnimator {

    @Override
    public boolean animateMove(RecyclerView.ViewHolder holder, int fromX, int fromY, int toX, int toY) {
        addAlphaAnimator(holder,500);
        return super.animateMove(holder, fromX, fromY, toX, toY);
    }

    private void addAlphaAnimator(RecyclerView.ViewHolder holder,int duration){
        int position = holder.getLayoutPosition();
        System.out.println("position:"+position);
        float scaleFrom = 1.0F;
        float scaleTo = 0.8F;

        if (position == 0){
            scaleFrom = 0.8F;
            scaleTo = 0.6F;
        }
        ObjectAnimator.ofFloat(holder.itemView, "alpha", new float[]{scaleFrom, scaleTo}).setDuration(duration).start();
    }
}
