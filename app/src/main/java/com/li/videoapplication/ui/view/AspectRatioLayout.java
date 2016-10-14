package com.li.videoapplication.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

public final class AspectRatioLayout extends RelativeLayout {

    private static final float MAX_ASPECT_RATIO_DEFORMATION_FRACTION = 0.01F;
    private float videoAspectRatio;

    public AspectRatioLayout(Context context) {
        super(context);
    }

    public AspectRatioLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setAspectRatio(float widthHeightRatio) {
        if(this.videoAspectRatio != widthHeightRatio) {
            this.videoAspectRatio = widthHeightRatio;
            this.requestLayout();
        }
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if(this.videoAspectRatio != 0.0F) {
            int width = this.getMeasuredWidth();
            int height = this.getMeasuredHeight();
            float viewAspectRatio = (float)width / (float)height;
            float aspectDeformation = this.videoAspectRatio / viewAspectRatio - 1.0F;
            if(Math.abs(aspectDeformation) > 0.01F) {
                if(aspectDeformation > 0.0F) {
                    height = (int)((float)width / this.videoAspectRatio);
                } else {
                    width = (int)((float)height * this.videoAspectRatio);
                }
                super.onMeasure(MeasureSpec.makeMeasureSpec(width, 1073741824),
                        MeasureSpec.makeMeasureSpec(height, 1073741824));
            }
        }
    }
}
