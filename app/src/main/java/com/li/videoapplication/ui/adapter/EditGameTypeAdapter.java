package com.li.videoapplication.ui.adapter;

import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.li.videoapplication.R;
import com.li.videoapplication.data.model.entity.GroupType;
import com.li.videoapplication.tools.ToastHelper;
import com.li.videoapplication.views.SmoothCheckBox;

import java.util.ArrayList;
import java.util.List;

/**
 * 适配器：我的个人资料喜欢的游戏类型
 */
public class EditGameTypeAdapter extends BaseQuickAdapter<GroupType, BaseViewHolder> {
    private static final String TAG = EditGameTypeAdapter.class.getSimpleName();

    private List<String> group_type_id = new ArrayList<>();

    public EditGameTypeAdapter(List<GroupType> data) {
        super(R.layout.adapter_editgametype, data);
    }

    @Override
    protected void convert(final BaseViewHolder holder, final GroupType groupType) {

        holder.setText(R.id.editgame_name, groupType.getGroup_type_name());

        final SmoothCheckBox checkBox = holder.getView(R.id.editgame_check);
        checkBox.setChecked(groupType.isSelected());
        checkBox.setOnCheckedChangeListener(new SmoothCheckBox.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SmoothCheckBox checkBox, boolean isChecked) {
                if (canSeleceed()) {
                    groupType.setSelected(isChecked);
                } else {
                    if (isChecked) {// 選中狀態
                        checkBox.setChecked(false);
                        ToastHelper.s("只能选择5个喜欢的游戏类型");
                    } else {
                        groupType.setSelected(false);
                    }
                }
            }
        });

        View root = holder.getView(R.id.root);
        root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkBox.performClick();
            }
        });
    }

    private boolean canSeleceed() {
        getGroup_type_id();
        return group_type_id.size() < 5;
    }

    public List<String> getGroup_type_id() {
        group_type_id.clear();
        for (GroupType record : getData()) {
            if (record.isSelected()) {
                group_type_id.add(record.getGroup_type_id());
            }
        }
        return group_type_id;
    }
}
