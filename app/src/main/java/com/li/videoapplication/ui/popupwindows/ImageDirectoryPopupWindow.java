package com.li.videoapplication.ui.popupwindows;

import java.util.List;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.li.videoapplication.data.local.ImageDirectoryEntity;
import com.li.videoapplication.R;
import com.li.videoapplication.ui.activity.ImageViewActivity;
import com.li.videoapplication.ui.adapter.ImageDirectoryAdapter;

/**
 * 弹框：图片文件夹
 */
public class ImageDirectoryPopupWindow extends PopupWindow implements OnClickListener {

	private int width;
	private int height;
	private View view;
	private ListView listView;
	private List<ImageDirectoryEntity> data;
    private ImageDirectoryAdapter adapter;
    private String directory;
    private LayoutInflater inflater;
    private ImageViewActivity activity;

	public ImageDirectoryPopupWindow(ImageViewActivity activity, List<ImageDirectoryEntity> data, String directory) {
        this.data = data;
        this.directory = directory;
        this.activity = activity;

        inflater = LayoutInflater.from(activity);
        view = inflater.inflate(R.layout.popup_imagedirectory, null);

        WindowManager windowManager = (WindowManager) activity.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(outMetrics);
        width = outMetrics.widthPixels;
        height = (int) (outMetrics.heightPixels * 0.7);

		setContentView(view);
		setWidth(width);
		setHeight(height);

		// 设置可触摸
		setFocusable(true);
		setTouchable(true);
		// 点击外部消失
		setOutsideTouchable(true);

		setBackgroundDrawable(new BitmapDrawable());
		setTouchInterceptor(new OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
                    dismiss();
                    return true;
                }
                return false;
            }
        });
        this.setAnimationStyle(R.style.slideBottomAlphaAnim);

        listView = (ListView) view.findViewById(R.id.listview);
        adapter = new ImageDirectoryAdapter(activity, this, data, directory);
        listView.setAdapter(adapter);

        view.findViewById(R.id.imagedirectory_cancel).setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		
		switch (v.getId()) {
		
		case R.id.imagedirectory_cancel:
			dismiss();
			break;
		}
	}
}
