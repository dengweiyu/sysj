package com.li.videoapplication.mvp.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.li.videoapplication.R;
import com.li.videoapplication.data.model.entity.Match;
import com.li.videoapplication.tools.TextImageHelper;
import com.li.videoapplication.tools.TimeHelper;
import com.li.videoapplication.views.CircleImageView;

import java.util.List;

/**
 * 适配器：历史战绩
 */
public class MatchRecordAdapter extends BaseQuickAdapter<Match, BaseViewHolder> {


    private TextImageHelper helper;

    public MatchRecordAdapter(List<Match> data) {
        super(R.layout.adapter_matchrecord, data);
        helper = new TextImageHelper();
    }

    @Override
    protected void convert(BaseViewHolder holder, Match match) {

        holder.setText(R.id.matchrecord_title, match.getTitle())
                .setText(R.id.matchrecord_name, match.getNickname());
        try {
            holder.setText(R.id.matchrecord_time, TimeHelper.getWholeTimeFormat(match.getUptime()));
        } catch (Exception e) {
            e.printStackTrace();
        }

        //1为赢，-1为输
        //本来应该1对应赢的图标，-1对应输的，但是接口返回的是反的。
        if (match.getIs_win() == 1)
            holder.setImageResource(R.id.matchrecord_resultimg, R.drawable.matchresult_lose_signet);
        else if (match.getIs_win() == -1)
            holder.setImageResource(R.id.matchrecord_resultimg, R.drawable.matchresult_win_signet);

        CircleImageView head = holder.getView(R.id.matchrecord_head);
        helper.setImageViewImageNet(head, match.getAvatar());
    }
}
