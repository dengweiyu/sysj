package com.li.videoapplication.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.view.View.MeasureSpec;
import android.widget.ImageView;

import com.li.videoapplication.framework.CommonAdapter;
import com.li.videoapplication.framework.ViewHolder;
import com.li.videoapplication.R;
import com.li.videoapplication.data.local.ScreenShotEntity;
import com.li.videoapplication.data.image.ImageLoaderHelper;
import com.li.videoapplication.tools.LayoutParamsHelper;
import com.li.videoapplication.ui.ActivityManeger;
import com.li.videoapplication.utils.ScreenUtil;

import java.io.File;
import java.util.List;

/**
 * 适配器：图文分享
 */
public class ImageShareAdapter extends CommonAdapter<ScreenShotEntity> {

	protected LayoutParamsHelper helper;

	public ImageShareAdapter(Context context, List<ScreenShotEntity> data, int layoutId) {
		super(context, data, layoutId);
		helper = new LayoutParamsHelper();
	}

	@Override
	public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
		super.onMeasure(widthMeasureSpec, expandSpec);
	}

	@Override
	public void convert(ViewHolder holder, ScreenShotEntity t, final int position) {

        final ScreenShotEntity record = getItem(position);
		final ImageView pic = holder.getView(R.id.imageshare_pic);
		setLayoutParams(pic);
		if (position == getCount() - 1) {// 最后一张显示加号图片
            pic.setImageResource(R.drawable.imageshare_add);
		} else {
            ImageLoaderHelper.displayImageWhite4Local(record.getPath(), pic);
		}

        holder.getConvertView().setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {

                if (position == getCount() - 1) {// 点击加号图片
                    ActivityManeger.startImageViewActivity(context);
                } else {// 预览图片
                    statAcionView(position);
                }
            }
        });
	}

    private void statAcionView(int position) {
        File file = new File(data.get(position).getPath());
        Intent intent = new Intent(Intent.ACTION_VIEW);
        Uri uri = Uri.fromFile(file);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setDataAndType(uri, "image/*");
        context.startActivity(intent);
    }

    private void setLayoutParams(View view) {
		// 13 + 10*3 + 13 = 56
		int w = (srceenWidth - ScreenUtil.dp2px(56))/4;
		helper.setLinearLayout(view, w, w);
	}
}
