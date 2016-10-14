package com.li.videoapplication.ui.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.li.videoapplication.R;
import com.li.videoapplication.data.model.entity.Match;
import com.li.videoapplication.framework.TBaseFragment;
import com.li.videoapplication.tools.TimeHelper;
import com.li.videoapplication.ui.fragment.GameMatchProcessFragment;
import com.li.videoapplication.utils.TextUtil;

import java.util.List;

/**
 * 适配器：赛程 头布局
 */
@SuppressLint("InflateParams")
public class MatchProcessHeaderAdapter extends RecyclerView.Adapter<MatchProcessHeaderAdapter.ViewHolder> {

    private final List<Match> data;
    private Context context;
    private GameMatchProcessFragment processFragment;

    public MatchProcessHeaderAdapter(Context context, List<Match> data) {
        this.context=  context;
        this.data = data;
    }

    public TBaseFragment receiveFragment(GameMatchProcessFragment fragment) {
        processFragment = fragment;
        return processFragment;
    }

    private void setTextColorRed(ViewHolder holder) {
        holder.bottomLine.setVisibility(View.VISIBLE);
        holder.process_header.setTextColor(Color.parseColor("#282828"));//black
        holder.day_header.setTextColor(Color.parseColor("#ff4b4b"));//red
        holder.time_header.setTextColor(Color.parseColor("#ff4b4b"));//red
    }

    private void setTextColorGray(ViewHolder holder) {
        holder.bottomLine.setVisibility(View.GONE);
        holder.process_header.setTextColor(Color.parseColor("#757575"));//gray
        holder.day_header.setTextColor(Color.parseColor("#757575"));//gray
        holder.time_header.setTextColor(Color.parseColor("#757575"));//gray
    }

    private void setTime(int position, String format, TextView view, Match record) {
        try {
            String string = "";
            if (format.equals("MM-dd")) {
                string = TimeHelper.getTime2MdFormat(record.getSchedule_time());
            }
            if (format.equals("HH:mm")) {
                string = TimeHelper.getTime2HmFormat(record.getSchedule_time());
            }
            if (position == 0) {
                TextUtil.toColor(string, "#ff3d2e");//红色
                view.setText(Html.fromHtml(string));
            } else {
                view.setText(string);
            }
        } catch (Exception e) {
            e.printStackTrace();
            view.setText("");
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_matchprocessheader, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Match record = data.get(position);

        holder.process_header.setText(record.getName());
        setTime(position, "MM-dd", holder.day_header, record);
        setTime(position, "HH:mm", holder.time_header, record);

        if (processFragment.currentHeaderPos != -1) {//有点击
            if (position == processFragment.currentHeaderPos) {
                setTextColorRed(holder);
            } else {
                setTextColorGray(holder);
            }
        } else {//没点击
            if (position == data.size() - 1) {//数据最后一项
                setTextColorRed(holder);
            } else {
                setTextColorGray(holder);
            }
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        View bottomLine;
        TextView process_header, day_header, time_header;

        public ViewHolder(View itemView) {
            super(itemView);
            process_header = (TextView) itemView.findViewById(R.id.process_header);
            day_header = (TextView) itemView.findViewById(R.id.day_header);
            time_header = (TextView) itemView.findViewById(R.id.time_header);
            bottomLine = itemView.findViewById(R.id.header_bottomline);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    processFragment.currentHeaderPos = getAdapterPosition();
                    processFragment.setHeaderClick();
                }
            });
        }
    }
}
