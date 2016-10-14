package com.li.videoapplication.ui.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.GridView;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.li.videoapplication.R;
import com.li.videoapplication.data.DataManager;
import com.li.videoapplication.data.EventManager;
import com.li.videoapplication.data.local.ImageDirectoryEntity;
import com.li.videoapplication.data.local.ImageDirectoryResponseObject;
import com.li.videoapplication.framework.TBaseActivity;
import com.li.videoapplication.ui.ActivityManeger;
import com.li.videoapplication.ui.adapter.ImageViewAdapter;
import com.li.videoapplication.ui.popupwindows.ImageDirectoryPopupWindow;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * 活动：图片选择
 */
@SuppressLint("HandlerLeak")
public class ImageViewActivity extends TBaseActivity implements OnClickListener {

    /** 图文选中的图片 */
    public static Set<String> imageViewDeleteData = new LinkedHashSet<>();
	
	private GridView gridView;
	private ImageViewAdapter adapter;
	private List<ImageDirectoryEntity> data = new LinkedList<>();
	
	private RelativeLayout bottom;
	private TextView name;
	private TextView count;

	private String directoryPath;

    @Override
	public int getContentView() {
		return R.layout.activity_imageview;
	}

	@Override
    public int inflateActionBar() {
		return R.layout.actionbar_second;
	}

    @Override
    public void afterOnCreate() {
        super.afterOnCreate();

        setSystemBarBackgroundWhite();
        setAbTitle(R.string.imageview_title);

    }

    @Override
    public void initView() {
        super.initView();

        gridView = (GridView) findViewById(R.id.gridview);
        bottom = (RelativeLayout) findViewById(R.id.bottom);
        name = (TextView) findViewById(R.id.imageview_name);
        count = (TextView) findViewById(R.id.imageview_count);
        name.setText("");
        count.setText("");

        abImageViewDone.setVisibility(View.VISIBLE);
        abImageViewDone.setText("完成(" + imageViewDeleteData.size() + "/9)");

        bottom.setOnClickListener(this);
        abImageViewDone.setOnClickListener(this);
    }

    @Override
    public void loadData() {
        super.loadData();

        // 加载图片文件夹
        DataManager.LOCAL.loadImageDirectorys();
    }

    @Override
    public void finish() {
        super.finish();

        EventManager.postImageView2ImageShareEvent();
    }

    @Override
	public void onClick(View v) {
		if (v == bottom) {
            if (data.size() > 0) {
                showPopupWindow();
            } else {
                showToastShort("未扫描到图片");
            }
			lightOff();
		} else if (v == abImageViewDone) {
            finish();
		}
	}

    /**
     * 图片文件夹
     */
    private ImageDirectoryPopupWindow popupWindow;

    private void showPopupWindow() {
        dismissPopupWindow();
        popupWindow = new ImageDirectoryPopupWindow(this, data, directoryPath);
        popupWindow.setOnDismissListener(new OnDismissListener() {

            @Override
            public void onDismiss() {
                lightOn();
            }
        });
        popupWindow.showAtLocation(getWindow().getDecorView(), Gravity.BOTTOM, 0, 0);
    }

    private void dismissPopupWindow(){
        if (popupWindow != null && popupWindow.isShowing())
            popupWindow.dismiss();
    }

    /**
     * 内容区域变亮
     */
    private void lightOn() {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = 1.0f;
        getWindow().setAttributes(lp);
    }

    /**
     * 内容区域变淡
     */
    private void lightOff() {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = 0.3f;
        getWindow().setAttributes(lp);
    }

    public void refreshActionBar(int size) {
        abImageViewDone.setText("完成(" + size + "/9)");
    }

    /**
     * 选择图片文件夹
     */
    public void refreshContentView(ImageDirectoryEntity entity) {
        directoryPath = entity.getPath();
        List<String> filePaths = entity.getFileNames();
        if (filePaths == null || filePaths.size() == 0) {
            showToastShort("该文件夹下没有图片文件");
        } else {
            adapter = new ImageViewAdapter(ImageViewActivity.this, filePaths, directoryPath);
            gridView.setAdapter(adapter);

            count.setText(filePaths.size() + "");
            name.setText(entity.getName());
        }
    }

    /**
     * 回调：加载图片文件夹
     */
    public void onEventMainThread(ImageDirectoryResponseObject event) {

        if (!event.isResult()) {
            if (event.getMsg() != null)
                showToastShort(event.getMsg());
            else
                showToastShort("未扫描到图片");
            return;
        }
        if (event.getDirectorys() != null && event.getDirectorys().size() > 0) {
            data.addAll(event.getDirectorys());
            ImageDirectoryEntity entity = event.getDirectorys().get(0);
            refreshContentView(entity);
        } else {
            showToastShort("未扫描到图片");
        }
    }
}
