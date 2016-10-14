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
public class TriangleView extends View {

	public static final int LEFTTOP = 0x0;
	
	public static final int RIGHTTOP = 0x1;
	
	public static final int RIGHTBOTTOM = 0x2;
	
	public static final int LEFTBOTTOM = 0x3;
	
	private int solidLocationDefault = LEFTTOP;

    private int solidColorDefault = 0xffff0000;
	
	private int solidColor = solidColorDefault;

    private int solidLocation = solidLocationDefault;

	public TriangleView(Context context) {
		this(context, null);
    }

	public TriangleView(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		init(context, attrs);
	}

	private void init(Context context, AttributeSet attrs) {

        if (attrs != null) {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.TriangleView);
            if (a.hasValue(R.styleable.TriangleView_SolidColor))
                solidColor = a.getColor(R.styleable.TriangleView_SolidColor, 0);
            if (a.hasValue(R.styleable.TriangleView_SolidLocation))
                solidLocation = a.getColor(R.styleable.TriangleView_SolidColor, 0);
            else
                solidLocation = solidLocationDefault;
            a.recycle();
        }
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		
		/* 设置背景为白色 */
		canvas.drawColor(Color.TRANSPARENT);

		Paint paint = new Paint();
		paint.setAntiAlias(true);
		paint.setStyle(Paint.Style.FILL);
	    paint.setColor(solidColor); 

		/* 画一个实心三角形 */
		Path path = new Path();
		
		switch (solidLocation) {
		
		case LEFTTOP:
			path.moveTo(0, 0);
			path.lineTo(getWidth(), 0);
			path.lineTo(0, getHeight());
			break;
			
		case RIGHTTOP:
			path.moveTo(getWidth(), 0);
			path.lineTo(getWidth(), getHeight());
			path.lineTo(0, 0);
			break;
			
		case RIGHTBOTTOM:
			path.moveTo(getWidth(), getHeight());
			path.lineTo(0, getHeight());
			path.lineTo(getWidth(), 0);
			break;
			
		case LEFTBOTTOM:
			path.moveTo(0, getHeight());
			path.lineTo(0, 0);
			path.lineTo(getWidth(), getHeight());
			break;
		}
		path.close();
		canvas.drawPath(path, paint);
	}
}
