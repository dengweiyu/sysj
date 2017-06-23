package com.li.videoapplication.ui.dialog;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.li.videoapplication.R;
import com.li.videoapplication.data.EventManager;
import com.li.videoapplication.data.image.GlideHelper;
import com.li.videoapplication.data.local.ScreenShotEntity;
import com.li.videoapplication.data.model.event.ImageView2ImageShareEvent;
import com.li.videoapplication.framework.BaseOverShootDialog;
import com.li.videoapplication.ui.ActivityManager;
import com.li.videoapplication.ui.activity.ImageViewActivity;

import java.util.ArrayList;
import java.util.List;

import io.rong.eventbus.EventBus;

/**
 * 弹框：赛事上传截图
 */
public class UploadPicDialog extends BaseOverShootDialog implements View.OnClickListener {

    private TextView confirm, text;
    private List<ScreenShotEntity> data = new ArrayList<>();
    private ImageView img1, img2, img3;
    private View imgsContainer;

    public UploadPicDialog(Context context) {
        super(context, R.style.MyDialog);
        setCanceledOnTouchOutside(false);
    }

    @Override
    protected int getContentView() {
        return R.layout.dialog_uploadpic;
    }

    @Override
    protected void afterContentView(Context context) {
        super.afterContentView(context);

        findViewById(R.id.uploadpic_choose).setOnClickListener(this);

        text = (TextView) findViewById(R.id.uploadpic_text);

        imgsContainer = findViewById(R.id.uploadpic_imgs_container);
        img1 = (ImageView) findViewById(R.id.uploadpic_img1);
        img2 = (ImageView) findViewById(R.id.uploadpic_img2);
        img3 = (ImageView) findViewById(R.id.uploadpic_img3);

        confirm = (TextView) findViewById(R.id.uploadpic_confirm);
        confirm.setOnClickListener(this);
        confirm.setFocusable(false);
        confirm.setClickable(false);
    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.uploadpic_choose:
                ActivityManager.startImageViewActivity(getContext());
                break;
            case R.id.uploadpic_confirm:
                if (ImageViewActivity.imageViewDeleteData.size() < 1) {
                    showToastShort("请选择要上传的图片");
                    return;
                }
                dismiss();
                EventManager.postUploadMatchPicEvent(data);
                break;
        }
    }

    /**
     * 组件间的通讯：图片选择
     */
    public void onEventMainThread(ImageView2ImageShareEvent event) {

        List<ScreenShotEntity> list = new ArrayList<>();
        for (String imageUrl : ImageViewActivity.imageViewDeleteData) {
            ScreenShotEntity imageInfo = new ScreenShotEntity();
            imageInfo.setPath(imageUrl);
            list.add(imageInfo);
        }
        ScreenShotEntity entity = new ScreenShotEntity();
        list.add(entity);

        data.clear();
        data.addAll(list);

        refreshView();
    }

    private void refreshView() {
        if (data.size() == 1) {//无图
            imgsContainer.setVisibility(View.GONE);
            confirm.setFocusable(false);
            confirm.setClickable(false);
            confirm.setBackgroundResource(R.drawable.button_gray);
            text.setText("上传截图");
        } else if (data.size() == 2) {//1张图
            imgsContainer.setVisibility(View.VISIBLE);
            img1.setVisibility(View.GONE);
            img2.setVisibility(View.VISIBLE);
            img3.setVisibility(View.GONE);
            GlideHelper.displayImageWhite(getContext(), data.get(0).getPath(), img2);//2是居中那张
        } else if (data.size() == 3) {//2张图
            imgsContainer.setVisibility(View.VISIBLE);
            img1.setVisibility(View.VISIBLE);
            img2.setVisibility(View.VISIBLE);
            img3.setVisibility(View.GONE);
            GlideHelper.displayImageWhite(getContext(), data.get(0).getPath(), img1);
            GlideHelper.displayImageWhite(getContext(), data.get(1).getPath(), img2);
        } else {//3+张图
            imgsContainer.setVisibility(View.VISIBLE);
            img1.setVisibility(View.VISIBLE);
            img2.setVisibility(View.VISIBLE);
            img3.setVisibility(View.VISIBLE);
            GlideHelper.displayImageWhite(getContext(), data.get(0).getPath(), img1);
            GlideHelper.displayImageWhite(getContext(), data.get(1).getPath(), img2);
            GlideHelper.displayImageWhite(getContext(), data.get(2).getPath(), img3);
        }

        if (data.size() > 1) {
            confirm.setFocusable(true);
            confirm.setClickable(true);
            confirm.setBackgroundResource(R.drawable.button_red);
            text.setText("继续上传");
        }
    }
}
