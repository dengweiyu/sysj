package com.li.videoapplication.mvp.adapter;

import android.annotation.SuppressLint;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.li.videoapplication.R;
import com.li.videoapplication.data.model.entity.Match;
import com.li.videoapplication.data.preferences.PreferencesHepler;
import com.li.videoapplication.tools.TextImageHelper;
import com.li.videoapplication.mvp.match.view.GameMatchProcessFragment;
import com.li.videoapplication.utils.TextUtil;

import java.util.List;

/**
 * 适配器：赛程
 */
@SuppressLint("InflateParams")
public class MatchProcessAdapter extends BaseQuickAdapter<Match, BaseViewHolder> {
    private static final String TAG = MatchProcessAdapter.class.getSimpleName();

    private String member_id;
    private TextImageHelper textImageHelper;
    private GameMatchProcessFragment fragment;

    public MatchProcessAdapter(GameMatchProcessFragment fragment, List<Match> data) {
        super(R.layout.adapter_matchprocess, data);
        this.fragment = fragment;
        textImageHelper = new TextImageHelper();
        if (PreferencesHepler.getInstance().isLogin()) {
            member_id = PreferencesHepler.getInstance().getMember_id();
        }
    }

    @Override
    protected void convert(BaseViewHolder holder, Match record) {
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
            //视频 a_video_id b_video_id
            temp = record.getB_video_id();
            record.setB_video_id(record.getA_video_id());
            record.setA_video_id(temp);
        }

        holder.setVisible(R.id.adapter_root, true)
                .setVisible(R.id.pk_video_red, !record.getA_video_id().equals("0"))
                .setVisible(R.id.pk_video_blue, !record.getB_video_id().equals("0"))
                .setText(R.id.match_status_text, record.getStatus())
                .addOnClickListener(R.id.red_icon)
                .addOnClickListener(R.id.blue_icon)
                .addOnClickListener(R.id.pk_video_red)
                .addOnClickListener(R.id.pk_video_blue);

        // 头像右上角聊天角标
        boolean isShowChatIcon = record.getA_member_id().equals(member_id)
                && !record.getB_member_id().equals("0");
        holder.setVisible(R.id.blue_contact, isShowChatIcon);

        ImageView match_status_image = holder.getView(R.id.match_status_image);
        TextView match_result = holder.getView(R.id.match_result);

        if (fragment.is_last == 1) {//冠季军赛
            holder.setVisible(R.id.champion_image, true)
                    .setVisible(R.id.match_result, true);

            ImageView champion_image = holder.getView(R.id.champion_image);
            if (holder.getLayoutPosition() == 0) {
                textImageHelper.setImageViewImageRes(champion_image, R.drawable.champion);
            } else if (holder.getLayoutPosition() == 1) {
                textImageHelper.setImageViewImageRes(champion_image, R.drawable.thirdwinner);
            }
            setChampion(match_status_image, match_result, record, holder.getLayoutPosition());
        } else {//非决赛
            holder.setVisible(R.id.champion_image, false);
            setWinner(match_status_image, match_result, record);
        }

        if (!record.getPk_a().equals("0")) {
            holder.setText(R.id.red_name, record.getA_name());
            ImageView red_icon = holder.getView(R.id.red_icon);
            textImageHelper.setImageViewImageNet(red_icon, record.getA_avatar());
        }

        ImageView blue_icon = holder.getView(R.id.blue_icon);
        //is_mate : 判断b队是轮空或匹配中（1正常,2轮空,3匹配中）
        switch (record.getIs_mate()) {
            case "1":
                setBlueName(holder, R.color.match_process_blue, record.getB_name());
                textImageHelper.setImageViewImageNet(blue_icon, record.getB_avatar());
                break;
            case "2": //B轮空
                setBlueName(holder, R.color.match_process_gray, "本局轮空");
                textImageHelper.setImageViewImageRes(blue_icon, R.drawable.matchprocess_noenemy);
                break;
            case "3": //匹配中
                setBlueName(holder, R.color.match_process_gray, "匹配中");
                textImageHelper.setImageViewImageRes(blue_icon, R.drawable.ongoing_nullicon);
                break;
        }
    }

    private void setBlueName(BaseViewHolder holder, int color, String b_name) {
        holder.setTextColor(R.id.blue_name, mContext.getResources().getColor(color))
                .setText(R.id.blue_name, b_name);
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
}
