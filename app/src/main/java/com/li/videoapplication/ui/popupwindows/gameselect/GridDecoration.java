package com.li.videoapplication.ui.popupwindows.gameselect;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.li.videoapplication.R;
import com.li.videoapplication.framework.AppManager;


/**
 * Created by linhui on 2017/10/19.
 */
public class GridDecoration extends RecyclerView.ItemDecoration {

    Paint paint = new Paint();

    public GridDecoration() {
        this.paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(ActivityCompat.getColor(AppManager.getInstance().getContext(), R.color.color_dfdfdf));
    }

    /**
     * 对RecyclerView网格布局分割线
     * @param c
     * @param parent
     * @param state
     */
    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        int childCount = parent.getChildCount();
        int count = childCount % 2 == 0 ? childCount / 2 : (childCount + 1) / 2;
        int height = 0;

        int chilcHeight = -1;

        for (int i = 1; i <= childCount; i++) {
            View view = parent.getChildAt(i - 1);
            if(chilcHeight == -1){
                chilcHeight =  view.getHeight();
            }
            height = view.getTop();
            /**
             * 画每一行
             */
            if (i % 2 == 0) {
                c.drawLine(parent.getWidth() / 2, height, parent.getWidth(), height, paint);
            } else {
                c.drawLine(0, height, parent.getWidth() / 2, height, paint);
            }

        }
        /**
         * 如果是基数补上
         */
        if (childCount % 2 != 0) {
            c.drawLine(parent.getWidth() / 2, height, parent.getWidth(), height, paint);
        }

        /**
         * 画中间分割线
         */
        c.drawLine(parent.getWidth() / 2, 0, parent.getWidth() / 2, chilcHeight * count , paint);

        /**
         * 补上item最后一行
         */
        c.drawLine(0, chilcHeight * count, parent.getWidth(), chilcHeight * count , paint);

    }
}
