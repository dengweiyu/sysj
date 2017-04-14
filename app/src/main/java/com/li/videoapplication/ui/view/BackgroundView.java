package com.li.videoapplication.ui.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.li.videoapplication.R;


/**
 * 视图：视频剪辑背景图
 */
public class BackgroundView extends FrameLayout {

    public final String action = this.getClass().getName();
    public final String tag = this.getClass().getSimpleName();

    private ImageView firstImage;
    private ImageView secondImage;
    private ImageView thirdImage;
    private ImageView fourthImage;
    private LinearLayout container;

    private LayoutInflater inflater;

    public BackgroundView(Context context) {
        this(context, null);
    }

    public BackgroundView(Context context, AttributeSet attrs) {
        super(context, attrs);
        inflater = LayoutInflater.from(getContext());
        inflater.inflate(R.layout.view_background, this);

        container = (LinearLayout) findViewById(R.id.container);
        firstImage = (ImageView) findViewById(R.id.background_image_first);
        secondImage = (ImageView) findViewById(R.id.background_image_second);
        thirdImage = (ImageView) findViewById(R.id.background_image_third);
        fourthImage = (ImageView) findViewById(R.id.background_image_fourth);
    }

    public void displayImage(final Bitmap firstBitmap,
                             final Bitmap secondBitmap,
                             final Bitmap thirdBitmap,
                             final Bitmap fourthBitmap) {

        displayImage(firstBitmap, firstImage);
        displayImage(secondBitmap, secondImage);
        displayImage(thirdBitmap, thirdImage);
        displayImage(fourthBitmap, fourthImage);
    }

    private void displayImage(Bitmap bitmap, ImageView imageView) {
        if (bitmap != null && !bitmap.isRecycled() && imageView != null)
            imageView.setImageBitmap(bitmap);
    }
}
