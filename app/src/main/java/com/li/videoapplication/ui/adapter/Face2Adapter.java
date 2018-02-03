package com.li.videoapplication.ui.adapter;

import android.support.annotation.Nullable;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.li.videoapplication.R;

import java.util.List;

/**
 * Created by cx on 2018/1/30.
 */

public class Face2Adapter extends BaseQuickAdapter<Integer, BaseViewHolder> {

    public Face2Adapter(@Nullable List<Integer> data) {
        super(R.layout.adapter_face2, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, Integer item) {
        helper.addOnClickListener(R.id.face_icon)
                .getView(R.id.face_icon).setBackgroundResource(item);

    }
}
