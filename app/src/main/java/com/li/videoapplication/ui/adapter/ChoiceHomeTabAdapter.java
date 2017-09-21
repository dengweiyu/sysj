package com.li.videoapplication.ui.adapter;

import com.chad.library.adapter.base.BaseItemDraggableAdapter;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseSectionQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.entity.SectionEntity;
import com.li.videoapplication.R;
import com.li.videoapplication.data.image.GlideHelper;
import com.li.videoapplication.ui.activity.ChoiceHomeTabActivity;

import java.util.List;

/**
 *
 */

public class ChoiceHomeTabAdapter extends BaseMultiItemQuickAdapter<ChoiceHomeTabActivity.MyMultiItemEntity,BaseViewHolder> {
  /*  public ChoiceHomeTabAdapter( List<SectionEntity> data) {
        super(R.layout.adapter_choice_home_tab, R.layout.adapter_choice_home_tab_header, data);
    }*/

    public ChoiceHomeTabAdapter(List<ChoiceHomeTabActivity.MyMultiItemEntity> data) {
        super(data);
        addItemType(ChoiceHomeTabActivity.MyMultiItemEntity.TYPE_TITLE,R.layout.adapter_choice_home_tab_header);
        addItemType(ChoiceHomeTabActivity.MyMultiItemEntity.TYPE_LIST,R.layout.adapter_choice_home_tab);


    }

    @Override
    protected void convert(BaseViewHolder holder, ChoiceHomeTabActivity.MyMultiItemEntity myMultiItemEntity) {

    }
}
