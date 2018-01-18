package com.li.videoapplication.ui.adapter;

import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.li.videoapplication.R;
import com.li.videoapplication.data.model.entity.GroupType;
import com.li.videoapplication.data.model.response.SelectMyGameEntity;
import com.li.videoapplication.tools.ToastHelper;
import com.li.videoapplication.views.SmoothCheckBox;

import java.util.ArrayList;
import java.util.List;

/**
 * 适配器：我的个人资料喜欢的游戏类型
 */
public class EditMyGameAdapter extends BaseQuickAdapter<SelectMyGameEntity.Bean, BaseViewHolder> {
    private static final String TAG = EditMyGameAdapter.class.getSimpleName();

    private List<String> group_id = new ArrayList<>();

    public EditMyGameAdapter(List<SelectMyGameEntity.Bean> data) {
        super(R.layout.adapter_editgametype, data);
    }

    @Override
    protected void convert(final BaseViewHolder holder, final SelectMyGameEntity.Bean bean) {

        holder.setText(R.id.editgame_name, bean.getGroup_name());

        final SmoothCheckBox checkBox = holder.getView(R.id.editgame_check);
        checkBox.setClickable(false);
        checkBox.setChecked(bean.getIs_attention() == 1);
//        checkBox.setOnCheckedChangeListener(new SmoothCheckBox.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(SmoothCheckBox checkBox, boolean isChecked) {
//                if (isChecked) {
//                    bean.setIs_attention(1);
//                } else bean.setIs_attention(0);
//            }
//        });

        View root = holder.getView(R.id.root);
        root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkBox.performClick();
                if (checkBox.isChecked()) {
                    bean.setIs_attention(1);
                } else bean.setIs_attention(0);
            }
        });
    }

    public List<String> getGroup_id() {
        group_id.clear();
        for (SelectMyGameEntity.Bean bean : getData()) {
            if (bean.getIs_attention() == 1) {
                group_id.add(bean.getGroup_id());
            }
        }
        return group_id;
    }
}
