package com.li.videoapplication.views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

@SuppressLint("DrawAllocation")
public class ApplyView extends View {

	public ApplyView(Context context) {
		this(context, null);
    }

	public ApplyView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		Paint paint = new Paint();
		paint.setAntiAlias(true);
		paint.setStyle(Paint.Style.FILL);
	    paint.setColor(0xddffffff);

		/* 画一个实心三角形 */
		Path path = new Path();

        path.moveTo(0, 0);
        path.lineTo(getWidth(), 0);
        path.lineTo(getWidth()/2, getHeight());
		path.close();

		canvas.drawPath(path, paint);
	}
}
