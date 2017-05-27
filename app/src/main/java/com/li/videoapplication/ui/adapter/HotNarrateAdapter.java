package com.li.videoapplication.ui.adapter;

import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.li.videoapplication.R;
import com.li.videoapplication.data.model.entity.Member;
import com.li.videoapplication.framework.BaseArrayAdapter;
import com.li.videoapplication.tools.UmengAnalyticsHelper;
import com.li.videoapplication.ui.ActivityManager;
import com.li.videoapplication.ui.activity.RecommendActivity;
import com.li.videoapplication.utils.ScreenUtil;
import com.li.videoapplication.utils.StringUtil;

/**
 * 适配器：金牌主播
 */
@SuppressLint("InflateParams")
public class HotNarrateAdapter extends BaseArrayAdapter<Member> {

    private Activity activity;

    /**
     * 跳转：玩家动态
     */
    private void startPlayerDynamicActivity(Member member) {
        if (StringUtil.isNull(member.getId())) {
            member.setId(member.getMember_id());
        }
        ActivityManager.startPlayerDynamicActivity(getContext(), member);
    }

    public HotNarrateAdapter(Context context, List<Member> data) {
        super(context, R.layout.adapter_home_hotnarrate, data);

        try {
            activity = (Activity) context;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {

        final Member record = getItem(position);
        ViewHolder holder = null;
        if (view == null) {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.adapter_home_hotnarrate, null);
            holder.name = (TextView) view.findViewById(R.id.hotnarrate_name);
            holder.pic = (ImageView) view.findViewById(R.id.hotnarrate_pic);
            holder.status = (ImageView) view.findViewById(R.id.hotnarrate_status);
            holder.root = (View) view.findViewById(R.id.root);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        setLayoutParams(holder.root);

        setTextViewText(holder.name, record.getNickname());
        setImageViewImageNet(holder.pic, record.getAvatar());
        setSatus(holder.status, record);

        view.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                startPlayerDynamicActivity(record);
                if (activity instanceof RecommendActivity) {
                    UmengAnalyticsHelper.onEvent(getContext(), UmengAnalyticsHelper.DISCOVER, "精彩推荐-金牌主播");
                } else {
                    UmengAnalyticsHelper.onEvent(getContext(), UmengAnalyticsHelper.MAIN, "热门主播");
                }
            }
        });

        return view;
    }

    private void setSatus(ImageView view, final Member record) {
        view.setVisibility(View.VISIBLE);
        if (record.getSex() == 1) {// 男
            setImageViewImageRes(view, R.drawable.home_v_red);
        } else {// 女
            setImageViewImageRes(view, R.drawable.home_v_red);
        }
    }

    private void setLayoutParams(View view) {
        // 70/72
        int w = ScreenUtil.dp2px(72);
        int h = ScreenUtil.dp2px(72);
        setLinearLayoutLayoutParams(view, w, h);
    }


    private static class ViewHolder {

        TextView name;
        ImageView pic;
        ImageView status;
        View root;
    }
}
