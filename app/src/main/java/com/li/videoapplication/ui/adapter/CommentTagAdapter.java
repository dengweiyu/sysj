package com.li.videoapplication.ui.adapter;

import android.view.ViewGroup;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.li.videoapplication.R;
import com.li.videoapplication.data.model.response.CommentTagEntity;

import java.util.List;

/**
 * 评论标签
 */

public class CommentTagAdapter extends BaseQuickAdapter<CommentTagEntity.DataBean,BaseViewHolder> {
    private int mSelected;
    public CommentTagAdapter(List<CommentTagEntity.DataBean> data,int selected) {
        super(R.layout.comment_tag_item,data);
        mSelected = selected;
    }


    @Override
    protected void convert(BaseViewHolder holder, CommentTagEntity.DataBean dataBean) {
        TextView content = (TextView)holder.getView(R.id.tv_tag_content) ;
        content.setText(dataBean.getTitle());
        if(holder.getAdapterPosition() == mSelected){
            content.setBackgroundResource(R.drawable.red_border);
            content.setTextColor(mContext.getResources().getColor(R.color.ab_backdround_red));
        }else {
            content.setBackgroundResource(R.drawable.gray_border);
            content.setTextColor(mContext.getResources().getColor(R.color.textcolor_french_gray));
        }
    }

    public int getSelected() {
        return mSelected;
    }

    public void setSelected(int selected) {
        mSelected = selected;
        notifyDataSetChanged();
    }
}
