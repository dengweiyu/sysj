package com.li.videoapplication.ui.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.li.videoapplication.R;
import com.li.videoapplication.data.DataManager;
import com.li.videoapplication.data.model.entity.Member;
import com.li.videoapplication.data.model.entity.VideoImage;
import com.li.videoapplication.framework.BaseArrayAdapter;
import com.li.videoapplication.tools.UmengAnalyticsHelper;
import com.li.videoapplication.ui.ActivityManeger;
import com.li.videoapplication.utils.StringUtil;
import com.li.videoapplication.views.CircleImageView;

import java.util.List;

/**
 * 适配器：玩家精选
 */
@SuppressLint("InflateParams")
public class RecommendAdapter extends BaseArrayAdapter<VideoImage> {

    /**
     * 跳转：视频播放
     */
    private void startVideoPlayActivity(VideoImage videoImage) {
        ActivityManeger.startVideoPlayActivity(getContext(), videoImage);
        UmengAnalyticsHelper.onEvent(getContext(), UmengAnalyticsHelper.DISCOVER, "精彩推荐-推荐视频");
    }

    /**
     * 跳转：玩家动态
     */
    private void startPlayerDynamicActivity(Member member) {
        ActivityManeger.startPlayerDynamicActivity(getContext(), member);
    }

    public RecommendAdapter(Context context, List<VideoImage> data) {
        super(context, R.layout.adapter_recommend, data);
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {

        final VideoImage record = getItem(position);
        final ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.adapter_recommend, null);
            holder.head = (CircleImageView) view.findViewById(R.id.recommend_head);
            holder.name = (TextView) view.findViewById(R.id.recommend_name);
            holder.cover = (ImageView) view.findViewById(R.id.recommend_cover);
            holder.title = (TextView) view.findViewById(R.id.recommend_title);
            holder.playCount = (TextView)view.findViewById(R.id.tv_recommend_play_count);
            holder.recommend = (TextView)view.findViewById(R.id.tv_recommend);
            holder.memberV = (ImageView)view.findViewById(R.id.iv_recommend_member_v);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        boolean isRecommed = !StringUtil.isNull(record.getIsRecommend()) &&
                !record.getIsRecommend().equals("0");// 0：普通视频；>0：推荐视频，值为推荐位id

        holder.recommend.setVisibility(isRecommed ? View.VISIBLE : View.INVISIBLE);

        if (record.isV()){
            holder.memberV.setVisibility(View.VISIBLE);
        }else {
            holder.memberV.setVisibility(View.INVISIBLE);
        }

        setImageViewImageNet(holder.head, record.getAvatar());
        setTextViewText(holder.name, record.getNickname());
        setImageViewImageNet(holder.cover, record.getFlag());
        setTextViewText(holder.title, record.getTitle());

        setTextViewText(holder.playCount, StringUtil.formatNum(record.getView_count()));
        holder.head.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Member member = gson.fromJson(record.toJSON(), Member.class);
                startPlayerDynamicActivity(member);
            }
        });

        view.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                startVideoPlayActivity(record);
            }
        });

        setListViewLayoutParams(view, 86);

        return view;
    }

    /**
     * 点赞
     */
    public void setGood(final VideoImage record, ImageView view) {
        if (record != null) {
            if (record.getFlower_tick() == 1) {// 已点赞状态
                view.setImageResource(R.drawable.videoplay_good_red_205);
            } else {// 未点赞状态
                view.setImageResource(R.drawable.videoplay_good_gray_205);
            }
        }

        view.setVisibility(View.VISIBLE);
        view.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (!isLogin()) {
                    showToastShort("请先登录！");
                    return;
                }
                if (StringUtil.isNull(record.getFlower_count())) {
                    record.setFlower_count("0");
                }
                if (record.getFlower_tick() == 1) {// 已点赞状态
                    record.setFlower_count(Integer.valueOf(record.getFlower_count()) - 1 + "");
                    record.setFlower_tick(0);
                } else {// 未点赞状态
                    record.setFlower_count(Integer.valueOf(record.getFlower_count()) + 1 + "");
                    record.setFlower_tick(1);
                }
                // 视频点赞
                DataManager.videoFlower2(record.getVideo_id(), getMember_id());

                notifyDataSetChanged();
            }
        });
    }

    /**
     * 收藏
     */
    public void setStar(final VideoImage record, ImageView view) {

        if (record != null) {
            if (record.getCollection_tick() == 1) {// 已收藏状态
                view.setImageResource(R.drawable.videoplay_star_red);
            } else {// 未收藏状态
                view.setImageResource(R.drawable.videoplay_star_gray);
            }
        }

        view.setVisibility(View.VISIBLE);
        view.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (!isLogin()) {
                    showToastShort("请先登录！");
                    return;
                }
                if (StringUtil.isNull(record.getCollection_count())) {
                    record.setCollection_count("0");
                }
                if (record.getCollection_tick() == 1) {// 已收藏状态
                    record.setCollection_count(Integer.valueOf(record.getCollection_count()) - 1 + "");
                    record.setCollection_tick(0);
                } else {// 未收藏状态
                    record.setCollection_count(Integer.valueOf(record.getCollection_count()) + 1 + "");
                    record.setCollection_tick(1);
                }
                // 视频收藏
                DataManager.videoCollect2(record.getVideo_id(), getMember_id());
                notifyDataSetChanged();
            }
        });
    }


    private static class ViewHolder {

        ImageView cover;
        TextView title;

        CircleImageView head;
        TextView name;

        TextView playCount;
        TextView recommend;

        ImageView memberV;
    }
}
