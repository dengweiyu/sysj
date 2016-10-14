package com.li.videoapplication.ui.adapter;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.li.videoapplication.R;
import com.li.videoapplication.data.model.entity.Match;
import com.li.videoapplication.tools.TextImageHelper;
import com.li.videoapplication.tools.TimeHelper;
import com.li.videoapplication.utils.StringUtil;
import com.li.videoapplication.utils.TextUtil;

import java.util.List;

/**
 * 适配器：我的赛程（已结束fragment）
 */
public class MyFinishedMatchProcessAdapter extends RecyclerView.Adapter<MyFinishedMatchProcessAdapter.ViewHolder> {

    private List<Match> data;
    private TextImageHelper helper;

    public MyFinishedMatchProcessAdapter(List<Match> data) {
        this.data = data;
        helper = new TextImageHelper();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder holder = new ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_myfinishedmatch, parent, false));
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Match record = data.get(position);

        //ff3d2e--red
        holder.process.setText(TextUtil.numberAtRed(record.getName(), "#ff3d2e"));
        setMyResultText(holder.mybgline, holder.myresult_text, holder.myresult_image, record.getTeam_a());
        helper.setTextViewText(holder.myname, record.getTeam_a().getLeader_game_role());

        if (!StringUtil.isNull(record.getTeam_a().getTeam_name())) {
            holder.myteamname.setVisibility(View.VISIBLE);
            helper.setTextViewText(holder.myteamname, "战队名：" + record.getTeam_a().getTeam_name());
        } else {
            holder.myteamname.setVisibility(View.GONE);
        }

        setEnemyResultText(holder.enemybgline, holder.enemyresult_text, record.getTeam_b());

        if (!StringUtil.isNull(record.getTeam_b().getLeader_id())) {
            helper.setTextViewText(holder.enemyname, record.getTeam_b().getLeader_game_role());
        } else {
            helper.setTextViewText(holder.enemyname, "本局轮空");
        }

        if (!StringUtil.isNull(record.getTeam_b().getTeam_name())) {
            holder.enemyteamname.setVisibility(View.VISIBLE);
            helper.setTextViewText(holder.enemyteamname, "战队名：" + record.getTeam_b().getTeam_name());
        } else {
            holder.enemyteamname.setVisibility(View.GONE);
        }

        setTime(holder.time, record);

        helper.setImageViewImageNet(holder.myicon, record.getTeam_a().getAvatar());

        if (!StringUtil.isNull(record.getTeam_b().getAvatar())) {
            helper.setImageViewImageNet(holder.enemyicon, record.getTeam_b().getAvatar());
        } else {
            helper.setImageViewImageRes(holder.enemyicon, R.drawable.matchprocess_noenemy_large);
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView myresult_image, myicon, enemyicon;
        TextView process, time;
        TextView myresult_text, myname, myteamname, enemyresult_text, enemyname, enemyteamname;
        View mybgline, enemybgline;

        public ViewHolder(View itemView) {
            super(itemView);
            process = (TextView) itemView.findViewById(R.id.process);
            time = (TextView) itemView.findViewById(R.id.time);
            myresult_image = (ImageView) itemView.findViewById(R.id.myresult_image);
            myicon = (ImageView) itemView.findViewById(R.id.myicon);
            myresult_text = (TextView) itemView.findViewById(R.id.myresult_text);
            myname = (TextView) itemView.findViewById(R.id.myname);
            myteamname = (TextView) itemView.findViewById(R.id.myteamname);
            enemyicon = (ImageView) itemView.findViewById(R.id.enemyicon);
            enemyresult_text = (TextView) itemView.findViewById(R.id.enemyresult_text);
            enemyname = (TextView) itemView.findViewById(R.id.enemyname);
            enemyteamname = (TextView) itemView.findViewById(R.id.enemyteamname);
            mybgline = itemView.findViewById(R.id.mybgline);
            enemybgline = itemView.findViewById(R.id.enemybgline);
        }
    }

    /**
     * 对战时间
     */
    private void setTime(TextView view, Match record) {
        try {
            String string = "对战时间：" + TimeHelper.getWholeTimeFormat(record.getSchedule_starttime())
                    + "~" + TimeHelper.getTime2HmFormat(record.getSchedule_endtime());
            view.setText(string);
        } catch (Exception e) {
            e.printStackTrace();
            view.setText("");
        }
    }

    private void setMyResultText(View bg, TextView tv, ImageView image, Match record) {
        int is_win = record.getIs_win();
        switch (is_win) {
            case 0://输
                tv.setBackgroundColor(Color.parseColor("#ababab"));
                bg.setBackgroundResource(R.drawable.match_userinfo_lose);
                tv.setText("我方败");
                helper.setImageViewImageRes(image, R.drawable.matchresult_lose_signet);
                break;
            case 1://赢
                tv.setBackgroundResource(R.color.menu_main_red);
                bg.setBackgroundResource(R.drawable.match_userinfo);
                tv.setText("我方胜");
                helper.setImageViewImageRes(image, R.drawable.matchresult_win_signet);
                break;
        }
    }

    private void setEnemyResultText(View bg, TextView tv, Match record) {
        int is_win = record.getIs_win();
        switch (is_win) {
            case 0://输
                tv.setBackgroundColor(Color.parseColor("#ababab"));
                bg.setBackgroundResource(R.drawable.match_userinfo_lose);
                tv.setText("敌方败");
                break;
            case 1://赢
//                tv.setBackgroundResource(R.color.ab_backdround_blue);//赢了是蓝色
//                bg.setBackgroundResource(R.drawable.match_userinfo_blue);//赢了是蓝色
                tv.setBackgroundColor(Color.parseColor("#ababab"));//赢了也是灰色
                bg.setBackgroundResource(R.drawable.match_userinfo_lose);//赢了也是灰色
                tv.setText("敌方胜");
                break;
        }
    }
}
