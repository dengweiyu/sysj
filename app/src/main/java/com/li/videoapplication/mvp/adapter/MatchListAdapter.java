package com.li.videoapplication.mvp.adapter;

import android.graphics.Paint;
import android.text.Html;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.li.videoapplication.R;
import com.li.videoapplication.data.model.entity.Match;
import com.li.videoapplication.tools.TextImageHelper;
import com.li.videoapplication.tools.TimeHelper;
import com.li.videoapplication.utils.StringUtil;
import com.li.videoapplication.utils.URLUtil;

import java.util.List;

/**
 * 适配器：主页-赛事列表
 */
public class MatchListAdapter extends BaseQuickAdapter<Match, BaseViewHolder> {

    private TextImageHelper helper;

    public MatchListAdapter(List<Match> data) {
        super(R.layout.adapter_matchlist, data);
        helper = new TextImageHelper();
    }

    @Override
    protected void convert(BaseViewHolder holder, Match match) {
        holder.setText(R.id.match_title, match.getTitle())
                .setText(R.id.match_type, match.getEvent_format())
                .setText(R.id.match_reward, match.getEvent_award());

        ImageView pic = holder.getView(R.id.match_pic);
        helper.setImageViewImageNet(pic, match.getNew_cover());

        setStatus(holder, match);
        setResult(holder, match);
        setTime(holder, match);

    }

    private void setTime(BaseViewHolder holder, Match record) {
        try {
            String string = TimeHelper.getMMddTimeFormat(record.getStarttime()) +
                    "~" + TimeHelper.getMMddTimeFormat(record.getEndtime());

            holder.setText(R.id.match_time, Html.fromHtml(string));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setResult(BaseViewHolder holder, Match match) {
        boolean isResult = match.getResult_status().equals("1");//状态值：0=》为未发布，1=>为发布(才显示赛事结果)

        holder.setVisible(R.id.match_result, isResult)
                .addOnClickListener(R.id.match_result);

        TextView result = holder.getView(R.id.match_result);
        result.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线
        result.getPaint().setAntiAlias(true);//抗锯齿
    }

    private void setStatus(BaseViewHolder holder, Match match) {
        holder.setText(R.id.match_status, match.getStatus());
        switch (match.getStatus()) {
            case "进行中":
                setStatusBgColor(holder, R.color.activity_red);
                break;
            case "已结束":
                setStatusBgColor(holder, R.color.activity_gray);
                break;
            case "报名中":
                setStatusBgColor(holder, R.color.match_green);
                break;
            default:
                holder.setVisible(R.id.match_status, false);
                break;
        }
    }

    private void setStatusBgColor(BaseViewHolder holder, int color) {
        holder.setBackgroundColor(R.id.match_status,
                mContext.getResources().getColor(color));
    }
}
