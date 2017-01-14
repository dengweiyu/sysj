package com.li.videoapplication.mvp.adapter;

import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.li.videoapplication.R;
import com.li.videoapplication.data.model.entity.Gift;
import com.li.videoapplication.framework.BaseArrayAdapter;
import com.li.videoapplication.tools.TextImageHelper;
import com.li.videoapplication.tools.UmengAnalyticsHelper;
import com.li.videoapplication.ui.ActivityManeger;

/**
 * 适配器：我的礼包
 */
@SuppressLint("InflateParams")
public class MyGiftAdapter extends BaseQuickAdapter<Gift,BaseViewHolder> {

    private final TextImageHelper helper;

    public MyGiftAdapter(List<Gift> data) {
        super(R.layout.adapter_mygift, data);
        helper = new TextImageHelper();
    }

    @Override
    protected void convert(BaseViewHolder holder, Gift gift) {
        holder.setText(R.id.mygift_title,gift.getTitle())
                .setText(R.id.mygift_obj,gift.getContent());

        TextView count = holder.getView(R.id.mygift_count);
        setCount(count, gift);

        ImageView pic = holder.getView(R.id.mygift_pic);
        helper.setImageViewImageNet(pic,gift.getFlag());
    }

    /**
     * 领取礼包的进度条
     *
     * @return 剩余数量：75%
     */
    private void setCount(TextView view, Gift record) {

        int max = Integer.parseInt(record.getNum());
        int progress = Integer.parseInt(record.getCount());
        view.setText("剩余数量：" + (int) (((float) progress / max) * 100) + "%");
    }
}
