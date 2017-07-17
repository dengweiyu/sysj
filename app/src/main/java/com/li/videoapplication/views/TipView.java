package com.li.videoapplication.views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Path.Direction;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.li.videoapplication.R;

@SuppressLint("DrawAllocation")
public class TipView extends View {

	private final  Paint paint;
	private final Context context;

	private final int radiu;
	private final int padding;
	private final int color;

	private boolean isRound;
	private float marginBottom = 0;
	public TipView(Context context) {
		this(context, null);
	}

	public TipView(Context context, AttributeSet attrs) {
		super(context, attrs);
		TypedArray array = context.obtainStyledAttributes(attrs,R.styleable.TipView);
		isRound = array.getBoolean(R.styleable.TipView_isRound,false);
		marginBottom = array.getDimension(R.styleable.TipView_marginBottom,0f);
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
		if (isRound){
			l = 0;
			t = 0;
			r = w>h?w:h;
			b = w>h?w:h;

			RectF f = new RectF(l+marginBottom, t+marginBottom, r-marginBottom, b-marginBottom);
			//开新图层
			int v = canvas.saveLayer(l,t,r,b,new Paint(),Canvas.ALL_SAVE_FLAG);
			//画阴影背景
			canvas.drawColor(color);
			//高亮区域
			Bitmap light = Bitmap.createBitmap(getMeasuredWidth(),getMeasuredWidth(), Bitmap.Config.ARGB_8888);
			light.eraseColor(Color.TRANSPARENT);

			Canvas canvasLight = new Canvas(light);

			//绘制圆形
			this.paint.setFlags(Paint.ANTI_ALIAS_FLAG);
			this.paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));

			Paint p = new Paint();
			p.setAntiAlias(true);
			canvasLight.drawRoundRect(f, r/2, r/2, p);

			canvas.drawBitmap(light,0,0,paint);
			canvas.restoreToCount(v);
		}else {
			//绘制环形矩形
			this.paint.setColor(color);
			this.paint.setStyle(Paint.Style.FILL);
			Path path = new Path();
			RectF inner = new RectF(l, t, r, b);
			RectF outer = new RectF(0, 0, w, h);
			path.addRoundRect(inner, radiu, radiu, Direction.CCW);
			path.addRect(outer, Direction.CW);
			path.close();
			canvas.drawPath(path, paint);
		}

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

