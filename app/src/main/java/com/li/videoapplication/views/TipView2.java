package com.li.videoapplication.views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Path.Direction;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

@SuppressLint("DrawAllocation")
public class TipView2 extends View {

	private final  Paint paint;
	private final Context context;

	private final int radiu;
	private final int padding;
	private final int color;

	public TipView2(Context context) {
		this(context, null);
	}

	public TipView2(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		this.paint = new Paint();
		this.paint.setAntiAlias(true); //消除锯齿
		this.radiu = dip2px(6);
		this.padding = dip2px(0);
//		this.color = getContext().getResources().getColor(R.color.tip_bg);
		this.color = Color.parseColor("#88000000");
	}

	@Override
	protected void onDraw(Canvas canvas) {
		
		int w = getWidth();
		int h = getHeight();
		int a;
		if (w > h) {
			a = h/2;
		} else {
			a = w/2;
		}
		int cx = w/2;
		int cy = h/2;
		int l = cx - a;
		int t = cy - a;
		int r = cx + a;
		int b = cy + a;
//		int l = 0;
//		int t = 0;
//		int r = w;
//		int b = h;
		
		/*//绘制圆角矩形
		this.paint.setColor(color);
		this.paint.setStyle(Paint.Style.STROKE);
		this.paint.setStrokeWidth(padding);
		RectF f = new RectF(l, t, r, b);
		canvas.drawRoundRect(f, radiu, radiu, paint);*/
		
		//绘制环形矩形
		this.paint.setColor(color);
		this.paint.setStyle(Paint.Style.FILL);
		Path path = new Path();
		RectF inner = new RectF(0, 0, w, h);
		RectF outer = new RectF(0, 0, w, h);
		path.addRoundRect(inner, radiu, radiu, Direction.CCW);
		path.addRect(outer, Direction.CW);
		path.close();
		canvas.drawPath(path, paint);
		
		super.onDraw(canvas);
	}
	
	
	/**
	 * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
	 */
	public int dip2px(float dpValue) {
		final float scale = getContext().getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}
}

