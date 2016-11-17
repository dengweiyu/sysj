package com.li.videoapplication.ui.adapter;

import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.li.videoapplication.R;
import com.li.videoapplication.data.model.entity.Currency;
import com.li.videoapplication.data.model.entity.Match;
import com.li.videoapplication.tools.TextImageHelper;
import com.li.videoapplication.tools.TimeHelper;
import com.li.videoapplication.utils.ScreenUtil;

import java.util.List;

/**
 * 适配器：圈子-赛事列表
 */
public class GroupMatchListAdapter extends BaseQuickAdapter<Match> {

    private TextImageHelper helper;

    public GroupMatchListAdapter(List<Match> data) {
        super(R.layout.adapter_gamematch, data);
        helper = new TextImageHelper();
    }

    @Override
    protected void convert(BaseViewHolder holder, Match item) {
        ImageView pic= holder.getView(R.id.activity_pic);
        helper.setImageViewImageNet(pic, item.getCover());
        setPicLayoutParams(pic);

        holder.setText(R.id.activity_title, item.getTitle())
                .setText(R.id.activity_joined, item.getType_name());
        try {
            String string = "起止时间：<font color=\"#ff641f\">" + TimeHelper.getTimeFormat(item.getStarttime()) +
                    "~" + TimeHelper.getTimeFormat(item.getEndtime()) + "</font>";

            holder.setText(R.id.activity_time, Html.fromHtml(string));
        } catch (Exception e) {
            e.printStackTrace();
        }

        switch (item.getStatus()) {
            case "火热":
                holder.setImageResource(R.id.activity_bg, R.drawable.match_hot);
                break;
            case "进行中":
                holder.setImageResource(R.id.activity_bg, R.drawable.activity_yellow);
                break;
            case "已结束":
                holder.setImageResource(R.id.activity_bg, R.drawable.activity_gray);
                break;
            case "报名中":
                holder.setImageResource(R.id.activity_bg, R.drawable.match_signingup);
                break;
            default:
                holder.setVisible(R.id.activity_bg,false);
                break;
        }
    }

    private void setPicLayoutParams(ImageView view) {
        // 9 + 9 + 9 + 9 = 36
        int w = ScreenUtil.getScreenWidth() - ScreenUtil.dp2px(36);
        int h = w * 94 / 292;
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) view.getLayoutParams();
        params.width = w;
        params.height = h;
        view.setLayoutParams(params);
    }
}
