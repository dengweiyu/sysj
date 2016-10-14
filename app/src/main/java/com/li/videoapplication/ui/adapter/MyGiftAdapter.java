package com.li.videoapplication.ui.adapter;

import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.li.videoapplication.R;
import com.li.videoapplication.data.model.entity.Gift;
import com.li.videoapplication.framework.BaseArrayAdapter;
import com.li.videoapplication.tools.UmengAnalyticsHelper;
import com.li.videoapplication.ui.ActivityManeger;

/**
 * 适配器：我的礼包
 */
@SuppressLint("InflateParams")
public class MyGiftAdapter extends BaseArrayAdapter<Gift> {

    /**
     * 跳转：礼包详情
     */
    private void startGiftDetailActivity(Gift item) {
        ActivityManeger.startGiftDetailActivity(getContext(), item.getId());
        UmengAnalyticsHelper.onEvent(getContext(), UmengAnalyticsHelper.SLIDER, "我的福利-礼包");
    }

    public MyGiftAdapter(Context context, List<Gift> data) {
        super(context, R.layout.adapter_mygift, data);
    }

    @Override
    public View getView(final int position, View view, ViewGroup parent) {

        final Gift record = getItem(position);
        final ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.adapter_mygift, null);
            holder.pic = (ImageView) view.findViewById(R.id.mygift_pic);
            holder.title = (TextView) view.findViewById(R.id.mygift_title);
            holder.count = (TextView) view.findViewById(R.id.mygift_count);
            holder.obj = (TextView) view.findViewById(R.id.mygift_obj);
            holder.button = (LinearLayout) view.findViewById(R.id.button);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        setTextViewText(holder.title, record.getTitle());
        setCount(holder.count, record);
        setObj(holder.obj, record);
        setImageViewImageNet(holder.pic, record.getFlag());

        holder.button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                startGiftDetailActivity(record);
            }
        });

        return view;
    }

    private class ViewHolder {

        ImageView pic;
        TextView title;
        TextView count;// 剩余数量：75%
        TextView obj;
        LinearLayout button;
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

    /**
     * 礼包内容
     */
    private void setObj(TextView view, Gift record) {
        setTextViewText(view, record.getContent());
    }
}
