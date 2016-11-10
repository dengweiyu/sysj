package com.li.videoapplication.data.image;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;

import com.li.videoapplication.R;
import com.li.videoapplication.framework.AppManager;

public class Constant {

    public static final Drawable PICTRUE_DEFAULT_TRANSPARENT = new ColorDrawable(Color.TRANSPARENT);

    public static final Drawable PICTRUE_DEFAULT_VIDEO = AppManager.getInstance().getContext().getResources().getDrawable(R.drawable.default_video_211);
}
