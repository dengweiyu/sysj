package com.li.videoapplication.ui.adapter;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.li.videoapplication.R;
import com.li.videoapplication.data.model.entity.Match;
import com.li.videoapplication.data.preferences.PreferencesHepler;
import com.li.videoapplication.tools.TextImageHelper;
import com.li.videoapplication.tools.UmengAnalyticsHelper;
import com.li.videoapplication.ui.ActivityManeger;
import com.li.videoapplication.ui.fragment.GameMatchProcessFragment;
import com.li.videoapplication.utils.StringUtil;
import com.li.videoapplication.utils.TextUtil;

import java.util.List;

import io.rong.imkit.RongIM;

/**
 * 适配器：赛程
 */
@SuppressLint("InflateParams")
public class MatchProcessAdapter extends RecyclerView.Adapter<MatchProcessAdapter.PKViewHolder> {
    private static final String TAG = MatchProcessAdapter.class.getSimpleName();
    private String member_id;
    private TextImageHelper textImageHelper;
    private List<Match> data;
    private GameMatchProcessFragment fragment;

    public MatchProcessAdapter(GameMatchProcessFragment fragment, List<Match> data) {
        this.fragment = fragment;
        this.data = data;
        textImageHelper = new TextImageHelper();
        if (PreferencesHepler.getInstance().isLogin()) {
            member_id = PreferencesHepler.getInstance().getMember_id();
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @Override
    public PKViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new PKViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_matchprocess, parent, false));
    }

    @Override
    public void onBindViewHolder(PKViewHolder holder, int position) {
        holder.root.setVisibility(View.VISIBLE);
        final Match record = data.get(position);

        if (fragment.is_last == 1 && record.getB_member_id().equals(member_id)) {//冠季军赛时，我出现在右边情况
            //置换两边数据，保证我在左边
            String temp;
            //id
            temp = record.getB_member_id();
            record.setB_member_id(record.getA_member_id());
            record.setA_member_id(temp);
            //头像
            temp = record.getB_avatar();
            record.setB_avatar(record.getA_avatar());
            record.setA_avatar(temp);
            //名字
            temp = record.getB_name();
            record.setB_name(record.getA_name());
            record.setA_name(temp);
            //qq
            temp = record.getB_qq();
            record.setB_qq(record.getA_qq());
            record.setA_qq(temp);
            //pk_a pk_b
            temp = record.getPk_b();
            record.setPk_b(record.getPk_a());
            record.setPk_a(temp);
        }

        // 头像右上角聊天角标
        if (record.getA_member_id().equals(member_id) &&
                !record.getB_member_id().equals("0")) {
            holder.blue_contact.setVisibility(View.VISIBLE);
        } else {
            holder.blue_contact.setVisibility(View.GONE);
        }

        if (fragment.is_last == 1) {//冠季军赛
            holder.champion_image.setVisibility(View.VISIBLE);
            holder.match_result.setVisibility(View.VISIBLE);
            if (position == 0) {
                textImageHelper.setImageViewImageRes(holder.champion_image, R.drawable.champion);
            } else if (position == 1) {
                textImageHelper.setImageViewImageRes(holder.champion_image, R.drawable.thirdwinner);
            }
            setChampion(holder.match_status_image, holder.match_result, record, position);
        } else {//非决赛
            holder.champion_image.setVisibility(View.GONE);
            setWinner(holder.match_status_image, holder.match_result, record);
        }

        if (!record.getPk_a().equals("0")) {
            textImageHelper.setTextViewText(holder.red_name, record.getA_name());
            textImageHelper.setImageViewImageNet(holder.red_icon, record.getA_avatar());
        }

        //is_mate : 判断b队是轮空或匹配中（1正常,2轮空,3匹配中）
        switch (record.getIs_mate()) {
            case "1":
                holder.blue_name.setTextColor(Color.parseColor("#48c5ff"));//blue
                textImageHelper.setTextViewText(holder.blue_name, record.getB_name());
                textImageHelper.setImageViewImageNet(holder.blue_icon, record.getB_avatar());
                break;
            case "2": //B轮空
                textImageHelper.setTextViewText(holder.blue_name, "本局轮空");
                holder.blue_name.setTextColor(Color.parseColor("#acacac"));//gray
                textImageHelper.setImageViewImageRes(holder.blue_icon, R.drawable.matchprocess_noenemy);
                break;
            case "3": //匹配中
                textImageHelper.setTextViewText(holder.blue_name, "匹配中");
                holder.blue_name.setTextColor(Color.parseColor("#acacac"));//gray
                textImageHelper.setImageViewImageRes(holder.blue_icon, R.drawable.ongoing_nullicon);
                break;
        }

        textImageHelper.setTextViewText(holder.match_status_text, record.getStatus());
    }

    private void setChampion(ImageView vsIcon, TextView pkResult, Match record, int pos) {
        String pk_a = record.getPk_a();
        String pk_b = record.getPk_b();
        String winner = record.getWinner();

        //冠亚军胜负状态
        if (pos == 0) {
            if (winner.equals(pk_a)) {
                //红色：#ff3d2e
                pkResult.setText(Html.fromHtml(TextUtil.toColor("冠军", "#ff3d2e") + ":亚军"));
            } else if (winner.equals(pk_b)) {
                //蓝色：#48c5ff
                pkResult.setText(Html.fromHtml("亚军:" + TextUtil.toColor("冠军", "#48c5ff")));
            } else if (record.getIs_mate().equals("2")) { //B轮空
                pkResult.setText(Html.fromHtml(TextUtil.toColor("冠军", "#ff3d2e") + ":亚军"));
            }
        }
        //季殿军胜负状态
        if (pos == 1) {
            if (winner.equals(pk_a)) {
                //红色：#ff3d2e
                pkResult.setText(Html.fromHtml(TextUtil.toColor("季军", "#ff3d2e") + ":殿军"));
            } else if (winner.equals(pk_b)) {
                //蓝色：#48c5ff
                pkResult.setText(Html.fromHtml("殿军:" + TextUtil.toColor("季军", "#48c5ff")));
            } else if (record.getIs_mate().equals("2")) { //B轮空
                pkResult.setText(Html.fromHtml(TextUtil.toColor("季军", "#ff3d2e") + ":殿军"));
            }
        }
        if (record.getStatus().equals("进行中") && winner.equals("0") ||
                record.getIs_mate().equals("3") && winner.equals("0")) {
            textImageHelper.setImageViewImageRes(vsIcon, R.drawable.myongoingmatch_vs);
            vsIcon.setVisibility(View.VISIBLE);
            pkResult.setVisibility(View.GONE);
        } else {
            pkResult.setVisibility(View.VISIBLE);
            vsIcon.setVisibility(View.INVISIBLE);//保留位置
        }
        if (record.getStatus().equals("已结束") && winner.equals("0")) {//败败
            textImageHelper.setTextViewText(pkResult, "败 : 败");
        }
    }

    private void setWinner(ImageView imageView, TextView textView, Match record) {
        String pk_a = record.getPk_a();
        String pk_b = record.getPk_b();
        String winner = record.getWinner();

        if (winner.equals(pk_a)) {
            //红色：#ff3d2e
            textView.setText(Html.fromHtml(TextUtil.toColor("胜", "#ff3d2e") + " : 败"));
        }
        if (winner.equals(pk_b)) {
            //蓝色：#48c5ff
            textView.setText(Html.fromHtml("败 : " + TextUtil.toColor("胜", "#48c5ff")));
        }
        if (record.getStatus().equals("进行中") && winner.equals("0") ||
                record.getIs_mate().equals("3") && winner.equals("0")) {
            textImageHelper.setImageViewImageRes(imageView, R.drawable.myongoingmatch_vs);
            imageView.setVisibility(View.VISIBLE);
            textView.setVisibility(View.GONE);
        } else {
            textView.setVisibility(View.VISIBLE);
            imageView.setVisibility(View.INVISIBLE);//保留位置
        }
        if (record.getStatus().equals("已结束") && winner.equals("0")) {//败败
            textImageHelper.setTextViewText(textView, "败 : 败");
        }
        //B位轮空。暂时目前轮空位置只会出现在B位
        if (record.getIs_mate().equals("2")) { //B轮空
            textView.setText(Html.fromHtml(TextUtil.toColor("胜", "#ff3d2e") + " : 败"));
        }
    }

    public class PKViewHolder extends RecyclerView.ViewHolder {
        ImageView red_icon, blue_icon, match_status_image, champion_image;
        TextView red_name, blue_name, match_status_text, match_result;
        View root, blue_contact;

        public PKViewHolder(View itemView) {
            super(itemView);

            red_icon = (ImageView) itemView.findViewById(R.id.red_icon);
            blue_icon = (ImageView) itemView.findViewById(R.id.blue_icon);
            match_status_image = (ImageView) itemView.findViewById(R.id.match_status_image);
            red_name = (TextView) itemView.findViewById(R.id.red_name);
            blue_name = (TextView) itemView.findViewById(R.id.blue_name);
            match_status_text = (TextView) itemView.findViewById(R.id.match_status_text);
            root = itemView.findViewById(R.id.adapter_root);
            blue_contact = itemView.findViewById(R.id.blue_contact);
            champion_image = (ImageView) itemView.findViewById(R.id.champion_image);
            match_result = (TextView) itemView.findViewById(R.id.match_result);

            red_icon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (RongIM.getInstance() != null &&
                            !StringUtil.isNull(data.get(getAdapterPosition()).getA_member_id()) &&
                            !StringUtil.isNull(data.get(getAdapterPosition()).getA_name()) &&
                            !data.get(getAdapterPosition()).getA_member_id().equals(member_id)) {

                        ActivityManeger.startConversationActivity(fragment.getActivity(),
                                data.get(getAdapterPosition()).getA_member_id(),
                                data.get(getAdapterPosition()).getA_name(), false);
                        UmengAnalyticsHelper.onEvent(fragment.getActivity(), UmengAnalyticsHelper.MATCH, "对战表-头像");
                    }
                }
            });

            blue_icon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (RongIM.getInstance() != null &&
                            !StringUtil.isNull(data.get(getAdapterPosition()).getB_member_id()) &&
                            !StringUtil.isNull(data.get(getAdapterPosition()).getB_name())) {

                        if (data.get(getAdapterPosition()).getA_member_id()
                                .equals(member_id)) {

                            ActivityManeger.startConversationActivity(fragment.getActivity(),
                                    data.get(getAdapterPosition()).getB_member_id(),
                                    data.get(getAdapterPosition()).getB_name(), true,
                                    data.get(getAdapterPosition()).getB_qq());
                        } else {
                            ActivityManeger.startConversationActivity(fragment.getActivity(),
                                    data.get(getAdapterPosition()).getB_member_id(),
                                    data.get(getAdapterPosition()).getB_name(), false);
                        }
                        UmengAnalyticsHelper.onEvent(fragment.getActivity(), UmengAnalyticsHelper.MATCH, "对战表-头像");
                    }
                }
            });
        }
    }
}
