package com.li.videoapplication.ui.adapter;

import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.li.videoapplication.R;
import com.li.videoapplication.data.model.entity.Game;
import com.li.videoapplication.framework.BaseArrayAdapter;
import com.li.videoapplication.tools.UmengAnalyticsHelper;
import com.li.videoapplication.ui.ActivityManeger;
import com.li.videoapplication.utils.ScreenUtil;
import com.li.videoapplication.views.RoundedImageView;

/**
 * 适配器：首页热门游戏
 */
@SuppressLint("InflateParams")
public class HotGameAdapter extends BaseArrayAdapter<Game> {

    /**
     * 跳转：圈子详情
     */
    private void startGameDetailActivity(Game item) {
        ActivityManeger.startGroupDetailActivity(getContext(), item.getGroup_id());
    }

    public HotGameAdapter(Context context, List<Game> data) {
        super(context, R.layout.adapter_home_hotgame, data);
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {

        final Game record = getItem(position);
        ViewHolder holder = null;
        if (view == null) {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.adapter_home_hotgame, null);
            holder.name = (TextView) view.findViewById(R.id.hotgame_name);
            holder.pic = (RoundedImageView) view.findViewById(R.id.hotgame_pic);
            holder.status = (ImageView) view.findViewById(R.id.hotgame_status);
            holder.root = view.findViewById(R.id.root);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        setLayoutParams(holder.root);

        setImageViewImageNet(holder.pic, record.getFlag());
        setTextViewText(holder.name, record.getGroup_name());
        setSatus(holder.status, record);

        view.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                startGameDetailActivity(record);
                UmengAnalyticsHelper.onEvent(getContext(), UmengAnalyticsHelper.MAIN, "热门游戏");
            }
        });

        return view;
    }

    private void setSatus(ImageView view, final Game record) {

        view.setVisibility(View.GONE);
    }

    private void setLayoutParams(View view) {
        // 72/72
        int w = ScreenUtil.dp2px(70);
        int h = ScreenUtil.dp2px(72);
        setLinearLayoutLayoutParams(view, w, h);
    }

    private static class ViewHolder {
        TextView name;
        RoundedImageView pic;
        ImageView status;
        View root;
    }
}
