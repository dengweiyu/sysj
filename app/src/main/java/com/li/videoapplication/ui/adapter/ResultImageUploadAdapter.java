package com.li.videoapplication.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.li.videoapplication.R;
import com.li.videoapplication.data.image.ImageLoaderHelper;
import com.li.videoapplication.data.local.ScreenShotEntity;
import com.li.videoapplication.framework.CommonAdapter;
import com.li.videoapplication.framework.ViewHolder;
import com.li.videoapplication.tools.LayoutParamsHelper;
import com.li.videoapplication.ui.ActivityManeger;
import com.li.videoapplication.ui.activity.ImageViewActivity;
import com.li.videoapplication.utils.ScreenUtil;

import java.io.File;
import java.util.List;

/**
 * 适配器：比赛结果图片上传
 */
public class ResultImageUploadAdapter extends CommonAdapter<ScreenShotEntity> {

	protected LayoutParamsHelper helper;

	public ResultImageUploadAdapter(Context context, List<ScreenShotEntity> data, int layoutId) {
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
		final ImageView del = holder.getView(R.id.image_del);
		setLayoutParams(pic);
		if (position == getCount() - 1) {// 最后一张显示加号图片
            pic.setImageResource(R.drawable.imageshare_add);
            pic.setScaleType(ImageView.ScaleType.FIT_XY);
            del.setVisibility(View.GONE);
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

        del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageViewActivity.imageViewDeleteData.remove(data.get(position).getPath());
                data.remove(position);
                notifyDataSetChanged();
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
		// 8+5 + 3*8 +8+8 = 53
		int w = (srceenWidth - ScreenUtil.dp2px(53))/4;
		helper.setViewGroupLayout(view, w, w);
    }
}
