package com.li.videoapplication.ui.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.li.videoapplication.R;
import com.li.videoapplication.data.model.entity.GroupType;
import com.li.videoapplication.framework.BaseArrayAdapter;
import com.li.videoapplication.tools.UmengAnalyticsHelper;
import com.li.videoapplication.ui.ActivityManager;
import com.li.videoapplication.utils.StringUtil;

import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;

import java.util.List;

/**
 * 适配器：游戏分类
 */
@SuppressLint("InflateParams")
public class ClassifiedAdapter extends BaseArrayAdapter<GroupType>   {

    /**
     * 页面跳转：圈子列表
     */
    private void startGroupListActivity(GroupType item) {
        ActivityManager.startGroupListFragment(getContext(), item);

    }


    public ClassifiedAdapter(Context context, List<GroupType> data) {
        super(context, R.layout.adapter_classified, data);

        inflater = LayoutInflater.from(context);
        resources = context.getResources();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final GroupType record = getItem(position);
        View view = convertView;
        ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            LayoutInflater li = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = li.inflate(R.layout.adapter_classified, parent, false);
            holder.icon = (ImageView) view.findViewById(R.id.classified_icon);
            holder.text = (TextView) view.findViewById(R.id.classified_text);
            holder.root = view.findViewById(R.id.root);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        setTextViewText(holder.text, record.getGroup_type_name());
        setImageViewImageNet(holder.icon, record.getFlag());

        view.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                startGroupListActivity(record);
                if (!StringUtil.isNull(record.getGroup_type_id())){
                    UmengAnalyticsHelper.onGameEvent(getContext(), record.getGroup_type_id());
                }
            }
        });

        return view;
    }


    private class ViewHolder {

        ImageView icon;
        TextView text;
        View root;
    }
}
