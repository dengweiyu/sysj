package com.li.videoapplication.ui.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.li.videoapplication.R;
import com.li.videoapplication.data.image.GlideHelper;
import com.li.videoapplication.ui.activity.CoachInfoEditActivity;
import com.li.videoapplication.utils.ScreenUtil;

import java.util.List;

/**
 *
 */

public class CoachShowImageAdapter extends BaseQuickAdapter<String,BaseViewHolder> {
    private OnOperation mOperation;
    private int mMaxSize;
    private boolean isUploadDone;
    private List<String> mData;
    public CoachShowImageAdapter(OnOperation operation,List<String> data,int maxSize) {
        super(R.layout.adapter_coach_show_image,data);
        mOperation = operation;
        mMaxSize = maxSize;
        mData = data;
    }

    @Override
    protected void convert(final BaseViewHolder holder, String s) {
        ImageView icon = holder.getView(R.id.iv_coach_show);
        icon.setVisibility(View.VISIBLE);
        View delete = holder.getView(R.id.iv_delete_image);
        delete.setVisibility(View.VISIBLE);

        try {
            if (holder.getAdapterPosition() == mData.size()-1){
                int id = Integer.parseInt(s);
                icon.setImageResource(id);
                icon.setBackgroundResource(R.drawable.ab_add);
                int pad = ScreenUtil.dp2px(mContext,16);
                icon.setPadding(pad,pad,pad,pad);
                icon.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                delete.setVisibility(View.GONE);
            }else {
                GlideHelper.displayImage(mContext,s,icon);
                icon.setBackgroundResource(0);
                icon.setPadding(0,0,0,0);
                icon.setScaleType(ImageView.ScaleType.FIT_XY);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (holder.getAdapterPosition() == mMaxSize){
            icon.setVisibility(View.GONE);
        }


        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOperation != null){
                    mOperation .removeImage(holder.getAdapterPosition());
                }

            }
        });

        icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOperation != null){
                    mOperation.onClickImage(v,holder.getAdapterPosition());
                }
            }
        });

        if (isUploadDone){
            delete.setVisibility(View.GONE);
            delete.setOnClickListener(null);
        }
    }

    public boolean isUploadDone() {
        return isUploadDone;
    }

    public void setUploadDone(boolean uploadDone) {
        isUploadDone = uploadDone;
    }

    public interface OnOperation{
        void removeImage(int position);
        void onClickImage(View view,int position);
    }
}
