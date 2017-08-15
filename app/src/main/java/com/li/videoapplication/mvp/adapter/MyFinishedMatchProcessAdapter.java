package com.li.videoapplication.mvp.adapter;

import android.app.Activity;
import android.graphics.Color;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.li.videoapplication.R;
import com.li.videoapplication.data.model.entity.Match;
import com.li.videoapplication.framework.AppManager;
import com.li.videoapplication.tools.TextImageHelper;
import com.li.videoapplication.tools.TimeHelper;
import com.li.videoapplication.ui.ActivityManager;
import com.li.videoapplication.utils.StringUtil;
import com.li.videoapplication.utils.TextUtil;

import java.util.List;

/**
 * 适配器：我的赛程（已结束fragment）
 */
public class MyFinishedMatchProcessAdapter extends BaseQuickAdapter<Match, BaseViewHolder> {

    private TextImageHelper helper;
    private String event_id;

    public MyFinishedMatchProcessAdapter(List<Match> data) {
        super(R.layout.adapter_myfinishedmatch, data);
        helper = new TextImageHelper();
    }

    @Override
    protected void convert(BaseViewHolder holder, Match record) {
        holder.setText(R.id.process, TextUtil.numberAtRed(record.getName(), "#ff3d2e"))//ff3d2e--red
                .setText(R.id.myname, record.getTeam_a().getLeader_game_role());
//                .addOnClickListener(R.id.clickarea);

        View mybgline = holder.getView(R.id.mybgline);
        TextView myresult_text = holder.getView(R.id.myresult_text);
        ImageView myresult_image = holder.getView(R.id.myresult_image);
        setMyResultText(mybgline, myresult_text, myresult_image, record.getTeam_a());

        if (!StringUtil.isNull(record.getTeam_a().getTeam_name())) {
            holder.setVisible(R.id.myteamname, true)
                    .setText(R.id.myteamname, "战队名：" + record.getTeam_a().getTeam_name());
        } else {
            holder.setVisible(R.id.myteamname, false);
        }

        View enemybgline = holder.getView(R.id.enemybgline);
        TextView enemyresult_text = holder.getView(R.id.enemyresult_text);
        setEnemyResultText(enemybgline, enemyresult_text, record.getTeam_b());

        if (!StringUtil.isNull(record.getTeam_b().getLeader_id())) {
            holder.setText(R.id.enemyname, record.getTeam_b().getLeader_game_role());
        } else {
            holder.setText(R.id.enemyname, "本局轮空");
        }

        if (!StringUtil.isNull(record.getTeam_b().getTeam_name())) {
            holder.setVisible(R.id.enemyteamname, true)
                    .setText(R.id.enemyteamname, "战队名：" + record.getTeam_b().getTeam_name());
        } else {
            holder.setVisible(R.id.enemyteamname, false);
        }

        setTime((TextView) holder.getView(R.id.time), record);
        helper.setImageViewImageNet((ImageView) holder.getView(R.id.myicon), record.getTeam_a().getAvatar());

        ImageView enemyicon = holder.getView(R.id.enemyicon);
        if (!StringUtil.isNull(record.getTeam_b().getAvatar())) {
            helper.setImageViewImageNet(enemyicon, record.getTeam_b().getAvatar());
        } else {
            helper.setImageViewImageRes(enemyicon, R.drawable.matchprocess_noenemy_large);
        }

        addOnClickListener(holder, record);
    }

    private void addOnClickListener(BaseViewHolder holder, final Match record) {
        View tranView = holder.getView(R.id.transition_area);
        final View tranView2 = holder.getView(R.id.myresult_image);
        tranView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!StringUtil.isNull(event_id)) {
                    Activity activity = AppManager.getInstance().currentActivity();
                    ActivityManager.startMyMatchBettleActivity(activity,
                            event_id, record.getSchedule_id(), v, "finishmatch",
                            tranView2, "signet");
                }
            }
        });
    }

    public void setEventID(String event_id) {
        this.event_id = event_id;
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
