package com.li.videoapplication.views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

import com.li.videoapplication.R;

@SuppressLint("DrawAllocation")
public class ReportView extends View {

	public ReportView(Context context) {
		this(context, null);
    }

	public ReportView(Context context, AttributeSet attrs) {
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

        path.moveTo(0, 0);
        path.lineTo(getWidth(), getHeight()/2);
        path.lineTo(0, getHeight());
		path.close();

		canvas.drawPath(path, paint);
	}
}
