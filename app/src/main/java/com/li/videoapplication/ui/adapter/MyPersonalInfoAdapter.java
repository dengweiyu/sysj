package com.li.videoapplication.ui.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.li.videoapplication.R;
import com.li.videoapplication.data.model.entity.GroupType;
import com.li.videoapplication.framework.BaseArrayAdapter;

import java.util.List;


@SuppressLint({"InflateParams", "ViewHolder"})
public class MyPersonalInfoAdapter extends BaseArrayAdapter<GroupType> {

    public MyPersonalInfoAdapter(Context context, List<GroupType> data) {
        super(context, R.layout.adapter_mypersonainfo, data);
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {

        final GroupType recored = getItem(position);
        view = inflater.inflate(R.layout.adapter_mypersonainfo, null);
        ViewHolder holder = new ViewHolder();
        holder.pic = (ImageView) view.findViewById(R.id.mypersonalinfo_pic);
        holder.name = (TextView) view.findViewById(R.id.mypersonalinfo_name);

        if (recored != null) {
            setTextViewText(holder.name, recored.getGroup_type_name());
            setImageViewImageNet(holder.pic, recored.getFlag());
        }

        setAdapterViewLayoutParams(view, dp2px(90), dp2px(40));
        return view;
    }

    private static class ViewHolder {
        ImageView pic;
        TextView name;
    }
}
