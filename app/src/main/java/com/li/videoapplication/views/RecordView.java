package com.li.videoapplication.views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

@SuppressLint("DrawAllocation")
public class RecordView extends View {

	public RecordView(Context context) {
		this(context, null);
    }

	public RecordView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		Paint paint = new Paint();
		paint.setAntiAlias(true);
		paint.setStyle(Paint.Style.FILL);
	    paint.setColor(0xffffffff);

		/* 画一个实心三角形 */
		Path path = new Path();

        path.moveTo(getWidth()/2, 0);
        path.lineTo(getWidth(), getHeight());
        path.lineTo(0, getHeight());
		path.close();

		canvas.drawPath(path, paint);
	}
}
