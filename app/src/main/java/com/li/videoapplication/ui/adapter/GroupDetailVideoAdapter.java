package com.li.videoapplication.ui.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.li.videoapplication.R;
import com.li.videoapplication.data.DataManager;
import com.li.videoapplication.data.model.entity.Member;
import com.li.videoapplication.data.model.entity.VideoImage;
import com.li.videoapplication.data.model.event.GoodAndStartEvent;
import com.li.videoapplication.data.model.response.GoodsDetailEntity;
import com.li.videoapplication.framework.BaseArrayAdapter;
import com.li.videoapplication.mvp.Constant;
import com.li.videoapplication.tools.TimeHelper;
import com.li.videoapplication.tools.ToastHelper;
import com.li.videoapplication.tools.UmengAnalyticsHelper;
import com.li.videoapplication.ui.ActivityManager;
import com.li.videoapplication.ui.DialogManager;
import com.li.videoapplication.ui.activity.HomeMoreActivity;
import com.li.videoapplication.ui.activity.MyDynamicActivity;
import com.li.videoapplication.ui.activity.SearchActivity;
import com.li.videoapplication.ui.activity.SquareActivity;
import com.li.videoapplication.utils.StringUtil;
import com.li.videoapplication.utils.TextUtil;
import com.li.videoapplication.views.CircleImageView;
import com.li.videoapplication.views.GridViewY1;

import java.util.List;

import io.rong.eventbus.EventBus;

/**
 * 适配器：广场（最新，最热）；首页更多（最新，最热）; 动态视频 ; 视频搜索结果
 * <p>
 * 风云榜视频(已改成新的 VideoBillboardAdapter)
 */
@SuppressLint("InflateParams")
public class GroupDetailVideoAdapter extends BaseArrayAdapter<VideoImage> {

    private Activity activity;
    private int tab = 0;
    private String more_mark;
    private boolean isHomeMoreNew;
    private  List<VideoImage> mData;

    /**
     * 跳转：视频播放
     */
    private void startVideoPlayActivity(VideoImage videoImage) {
        ActivityManager.startVideoPlayActivity(getContext(), videoImage);
        if (activity instanceof SquareActivity) {
            UmengAnalyticsHelper.onEvent(getContext(), UmengAnalyticsHelper.DISCOVER, "广场-视频");
        } else if (activity instanceof MyDynamicActivity) {
            UmengAnalyticsHelper.onEvent(getContext(), UmengAnalyticsHelper.DISCOVER, "动态-视频");
        } else if (activity instanceof SearchActivity) {
            UmengAnalyticsHelper.onEvent(getContext(), UmengAnalyticsHelper.MAIN, "搜索-相关视频-点击相关视频内任意视频次数");
        } else if (activity instanceof HomeMoreActivity) {
            UmengAnalyticsHelper.onMainMoreEvent(getContext(), more_mark, isHomeMoreNew);
        }
    }

    /**
     * 跳转：玩家动态
     */
    private void startPlayerDynamicActivity(Member member) {
        ActivityManager.startPlayerDynamicActivity(getContext(), member);
        if (activity instanceof MyDynamicActivity) {
            UmengAnalyticsHelper.onEvent(getContext(), UmengAnalyticsHelper.DISCOVER, "动态-他人");
        } else if (activity instanceof SquareActivity) {
            UmengAnalyticsHelper.onEvent(getContext(), UmengAnalyticsHelper.DISCOVER, "广场-头像");
        }
    }

    public GroupDetailVideoAdapter(Context context, List<VideoImage> data) {
        super(context, R.layout.adapter_groupdetail_video, data);
        try {
            mData = data;
            activity = (Activity) context;
        } catch (Exception e) {
            e.printStackTrace();
        }

        EventBus.getDefault().register(this);
    }

    public void setHomeMoreLocation(String more_mark, boolean isHomeMoreNew) {
        this.more_mark = more_mark;
        this.isHomeMoreNew = isHomeMoreNew;
    }

    public void setTab(int tab) {
        this.tab = tab;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {

        final VideoImage record = getItem(position);
        final ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.adapter_groupdetail_video, null);
            holder.head = (CircleImageView) view.findViewById(R.id.groupdetail_head);
            holder.head_v = (ImageView) view.findViewById(R.id.groupdetail_v);
            holder.name = (TextView) view.findViewById(R.id.groupdetail_name);
            holder.isRecommend = (ImageView) view.findViewById(R.id.iv_video_recommed);
            holder.time = (TextView) view.findViewById(R.id.groupdetail_time);
            holder.content = (TextView) view.findViewById(R.id.groupdetail_content);
            holder.cover = (ImageView) view.findViewById(R.id.groupdetail_cover);
            holder.centerPlay = (ImageView) view.findViewById(R.id.groupdetail_centerPlay);
            holder.play = (ImageView) view.findViewById(R.id.groupdetail_play);
            holder.allTime = (TextView) view.findViewById(R.id.groupdetail_allTime);
            holder.playCount = (TextView) view.findViewById(R.id.groupdetail_playCount);
            holder.like = (ImageView) view.findViewById(R.id.groupdetail_like);
            holder.likeCount = (TextView) view.findViewById(R.id.groupdetail_likeCount);
            holder.star = (ImageView) view.findViewById(R.id.groupdetail_star);
            holder.starCount = (TextView) view.findViewById(R.id.groupdetail_starCount);
            holder.comment = (ImageView) view.findViewById(R.id.groupdetail_comment);
            holder.commentCount = (TextView) view.findViewById(R.id.groupdetail_commentCount);
            holder.image = (RelativeLayout) view.findViewById(R.id.groupdetail_image);
            holder.video = (RelativeLayout) view.findViewById(R.id.groupdetail_video);
            holder.grid = (GridViewY1) view.findViewById(R.id.gridview);
            holder.isPrivate = view.findViewById(R.id.ll_is_private);
            holder.upLoading = view.findViewById(R.id.tv_video_uploading);
            holder.root = view.findViewById(R.id.root);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        holder.head_v.setVisibility(record.isV() ? View.VISIBLE : View.INVISIBLE);

        boolean isRecommed = !StringUtil.isNull(record.getIsRecommend()) &&
                !record.getIsRecommend().equals("0");// 0：普通视频；>0：推荐视频，值为推荐位id
        holder.isRecommend.setVisibility(isRecommed ? View.VISIBLE : View.INVISIBLE);

        setImageViewImageNet(holder.head, record.getAvatar());
        setTextViewText(holder.name, record.getNickname());
        setTextViewText(holder.content, record.getTitle());

      /*  if (tab != 0) {
            if (tab == 1) {
                holder.likeCount.setText(Html.fromHtml(TextUtil.toColor(record.getFlower_count(), "#ff3d2e")));
                setTextViewText(holder.commentCount, record.getComment_count());
            } else if (tab == 2) {
                setTextViewText(holder.likeCount, record.getFlower_count());
                holder.commentCount.setText(Html.fromHtml(TextUtil.toColor(record.getComment_count(), "#ff3d2e")));
            } else {

            }

            setTextViewText(holder.likeCount, record.getFlower_count());
            setTextViewText(holder.commentCount, record.getComment_count());
        } else {*/


        setTextViewText(holder.commentCount, record.getComment_count());

        if (record.getFlower_tick() == 1) { // 已点赞状态
            holder.likeCount.setText(Html.fromHtml(TextUtil.toColor(record.getFlower_count(), "#ff3d2e")));
        } else {                            // 未点赞状态
            setTextViewText(holder.likeCount, record.getFlower_count());
        }

        if (record.getCollection_tick() == 1) {// 已收藏状态
            holder.starCount.setText(Html.fromHtml(TextUtil.toColor(record.collection_count, "#ff3d2e")));

        } else {// 未收藏状态
            setTextViewText(holder.starCount, record.getCollection_count());
        }

       // }


        setTime(record, holder.time);

        if (isVideo(record)) {// 视频
            holder.video.setVisibility(View.VISIBLE);
            holder.image.setVisibility(View.GONE);

            setTimeLength(holder.allTime, record);

           /* if (tab != 0 && tab == 3) {
                holder.playCount.setText(Html.fromHtml(TextUtil.toColor(StringUtil.toUnitW(record.getClick_count()), "#ff3d2e")));
            } else {

            }
*/
            setTextViewText(holder.playCount, StringUtil.toUnitW(record.getClick_count()));// 785

            if (!StringUtil.isNull(record.getVideo_flag())) {
                setImageViewImageNet(holder.cover, record.getVideo_flag());
            } else if (!StringUtil.isNull(record.getFlag())) {
                setImageViewImageNet(holder.cover, record.getFlag());
            }
        }
        if (isImage(record)) { // 图文
            holder.video.setVisibility(View.GONE);
            holder.image.setVisibility(View.VISIBLE);
            List<String> data = record.getPic_urls();
            GroupDetailImageAdapter dapter = new GroupDetailImageAdapter(getContext(), data, record);
            holder.grid.setAdapter(dapter);
            dapter.notifyDataSetChanged();
        }
        if (StringUtil.isNull(record.getPic_id()) && StringUtil.isNull(record.getVideo_id()) && !StringUtil.isNull(record.getId())) { // 搜索视频
            holder.video.setVisibility(View.VISIBLE);
            holder.image.setVisibility(View.GONE);

            setTimeLength(holder.allTime, record);
            setImageViewImageNet(holder.cover, record.getFlagPath());
            setTextViewText(holder.name, record.getUserName());
            setTextViewText(holder.content, record.getName());
        }
        // 点赞设置
        setLike(record, holder.like);

        // 收藏设置
        setStar(record, holder.star);

        // 评论
        setComment(record, holder.comment);

        //判断是否是自己现在上传转码中的视频
        String isPrivate = record.getPersonal_private();
        if (!StringUtil.isNull(isPrivate)){
            if (isPrivate.equals("0")){
                holder.upLoading.setVisibility(View.GONE);
                holder.isPrivate.setVisibility(View.GONE);
                holder.like.setVisibility(View.VISIBLE);
                holder.likeCount.setVisibility(View.VISIBLE);
                holder.star.setVisibility(View.VISIBLE);
                holder.starCount.setVisibility(View.VISIBLE);
                holder.comment.setVisibility(View.VISIBLE);
                holder.commentCount.setVisibility(View.VISIBLE);
                holder.centerPlay.setVisibility(View.VISIBLE);
            }else {
                holder.upLoading.setVisibility(View.VISIBLE);
                holder.isPrivate.setVisibility(View.VISIBLE);
                holder.like.setVisibility(View.GONE);
                holder.likeCount.setVisibility(View.GONE);
                holder.star.setVisibility(View.GONE);
                holder.starCount.setVisibility(View.GONE);
                holder.comment.setVisibility(View.GONE);
                holder.commentCount.setVisibility(View.GONE);
                holder.centerPlay.setVisibility(View.GONE);
            }
        }


        holder.head.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Member member = gson.fromJson(record.toJSON(), Member.class);
                startPlayerDynamicActivity(member);
            }
        });

        holder.video.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (!StringUtil.isNull(record.getPersonal_private())){
                    if (record.getPersonal_private().equals("1")){      //视频上传中仅自己可见
                        ToastHelper.s("该视频正在上传中，一会再来查看哦~");
                        return;
                    }
                }
                if (record.getVideo_id() != null && !record.getVideo_id().equals("0")) {// 视频
                    startVideoPlayActivity(record);
                }
            }
        });

        holder.root.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!StringUtil.isNull(record.getPersonal_private())){
                    if (record.getPersonal_private().equals("1")){      //视频上传中仅自己可见
                        ToastHelper.s("该视频正在上传中，一会再来查看哦~");
                        return;
                    }
                }
                if (record.getVideo_id() != null && !record.getVideo_id().equals("0")) {// 视频
                    startVideoPlayActivity(record);
                }
            }
        });

        holder.isRecommend.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityManager.startProductsDetailActivity(getContext(), record.getIsRecommend(),
                        Constant.PRODUCTSDETAIL_RICHTEXT_WITHBTN);
            }
        });

        return view;
    }

    /**
     * 发布时间
     */
    private void setTime(final VideoImage record, TextView view) {
        // 1小时前
        if (view != null && record != null) {

            if (activity instanceof SearchActivity) {
                try {
                    setTextViewText(view, TimeHelper.getVideoImageUpTime(record.getUpload_time()));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                try {
                    setTextViewText(view, TimeHelper.getVideoImageUpTime(record.getUptime()));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 点赞
     */
    public void setLike(final VideoImage record, ImageView view) {

        if (StringUtil.isNull(record.getPic_id()) && StringUtil.isNull(record.getVideo_id()) && !StringUtil.isNull(record.getId())) { // 搜索视频
            return;
        }
        if (record.getFlower_tick() == 1) {// 已点赞状态
            view.setImageResource(R.drawable.videoplay_good_red_205);
        } else {// 未点赞状态
            view.setImageResource(R.drawable.videoplay_good_gray_205);
        }

        view.setVisibility(View.VISIBLE);
        view.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                if (!isLogin()) {
                    DialogManager.showLogInDialog(getContext());
                    return;
                }
                if (StringUtil.isNull(record.getFlower_count())) {
                    record.setFlower_count("0");
                }
                if (isVideo(record)) {// 视频
                    if (record.getFlower_tick() == 1) {// 已点赞状态
                        record.setFlower_count(Integer.valueOf(record.getFlower_count()) - 1 + "");
                        record.setFlower_tick(0);
                    } else {// 未点赞状态
                        record.setFlower_count(Integer.valueOf(record.getFlower_count()) + 1 + "");
                        record.setFlower_tick(1);
                    }
                    // 视频点赞
                    DataManager.videoFlower2(record.getVideo_id(), getMember_id());
                }
                if (isImage(record)) { // 图文
                    int flag = record.getFlower_tick();
                    if (flag == 1) {// 已点赞状态
                        record.setFlower_count(Integer.valueOf(record.getFlower_count()) - 1 + "");
                        record.setFlower_tick(0);
                    } else {// 未点赞状态
                        record.setFlower_count(Integer.valueOf(record.getFlower_count()) + 1 + "");
                        record.setFlower_tick(1);
                    }
                    // 献花/取消献花用户
                    DataManager.photoFlower(record.getPic_id(), getMember_id(), flag);

                }
                notifyDataSetChanged();
            }
        });
    }

    /**
     * 收藏
     */
    public void setStar(final VideoImage record, ImageView view) {

        if (StringUtil.isNull(record.getPic_id()) && StringUtil.isNull(record.getVideo_id()) && !StringUtil.isNull(record.getId())) { // 搜索视频

            return;
        }
        if (record.getCollection_tick() == 1) {// 已收藏状态
            view.setImageResource(R.drawable.videoplay_star_red_205);
        } else {// 未收藏状态
            view.setImageResource(R.drawable.videoplay_star_gray_205);
        }

        view.setVisibility(View.VISIBLE);
        view.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                if (!isLogin()) {
                    DialogManager.showLogInDialog(getContext());
                    return;
                }
                if (StringUtil.isNull(record.getCollection_count())) {
                    record.setCollection_count("0");
                }
                if (isVideo(record)) {// 视频
                    if (record.getCollection_tick() == 1) {// 已收藏状态
                        record.setCollection_count(Integer.valueOf(record.getCollection_count()) - 1 + "");
                        record.setCollection_tick(0);
                    } else {// 未收藏状态
                        record.setCollection_count(Integer.valueOf(record.getCollection_count()) + 1 + "");
                        record.setCollection_tick(1);
                    }
                    // 视频收藏
                    DataManager.videoCollect2(record.getVideo_id(), getMember_id());
                }
                if (isImage(record)) { // 图文
                    int flag = record.getCollection_tick();

                    if (flag == 1) {// 已收藏状态
                        record.setCollection_count(Integer.valueOf(record.getCollection_count()) - 1 + "");
                        record.setCollection_tick(0);
                    } else {// 未收藏状态
                        record.setCollection_count(Integer.valueOf(record.getCollection_count()) + 1 + "");
                        record.setCollection_tick(1);
                    }
                    // 收藏/取收藏花用户
                    DataManager.photoCollection(record.getPic_id(), getMember_id(), flag);
                }
                notifyDataSetChanged();
            }
        });
    }

    /**
     * 评论
     */
    private void setComment(final VideoImage record, ImageView view) {

        if (StringUtil.isNull(record.getPic_id())
                && StringUtil.isNull(record.getVideo_id())
                && !StringUtil.isNull(record.getId())) { // 搜索视频
            return;
        }

        view.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                startVideoPlayActivity(record);
            }
        });
    }

    /**
     * 视频时间长度
     */
    private void setTimeLength(TextView view, VideoImage record) {
        // 17:00
        try {
            setTextViewText(view, TimeHelper.getVideoPlayTime(record.getTime_length()));
        } catch (Exception e) {
            e.printStackTrace();
            setTextViewText(view, "");
        }
    }

    /**
     * 是否是视频
     */
    private boolean isVideo(final VideoImage record) {
        // 视频
        return !StringUtil.isNull(record.getVideo_id()) && !record.getVideo_id().equals("0");
    }

    /**
     * 是否是图文
     */
    private boolean isImage(final VideoImage record) {
        // 图文
        return !StringUtil.isNull(record.getPic_id()) && !record.getPic_id().equals("0");
    }

    /**
     * 点赞 收藏事件
     */
    public void onEventMainThread(GoodAndStartEvent event){
        if (StringUtil.isNull(event.getVideoId())){
            return;
        }
        if (mData == null){
            return;
        }
        for (VideoImage v:
             mData) {
            if (event.getVideoId().equals(v.getVideo_id())){
                if (event.getType() == GoodAndStartEvent.TYPE_GOOD){
                    if (event.isPositive()){
                        v.flower_tick = 1;
                        v.flower_count =( Integer.parseInt(v.flower_count) + 1)+"";
                    }else {
                        int count = Integer.parseInt(v.flower_count);
                        if (count < 1){
                            return;
                        }
                        v.flower_tick = 0;
                        v.flower_count =(count - 1)+"";
                    }
                    notifyDataSetChanged();
                }else if (event.getType() == GoodAndStartEvent.TYPE_START){
                    if (event.isPositive()){
                        v.collection_tick = 1;
                        v.collection_count = ( Integer.parseInt(v.collection_count) + 1)+"";
                    }else {
                        v.collection_tick = 0;

                        int count = Integer.parseInt(v.collection_count);
                        if (count < 1){
                            return;
                        }
                        v.collection_count = ( count - 1)+"";
                    }
                    notifyDataSetChanged();
                }
                break;
            }
        }
    }

    private static class ViewHolder {

        CircleImageView head;
        ImageView head_v;
        TextView name;
        ImageView isRecommend;
        TextView time;

        TextView content;
        ImageView cover;
        TextView allTime;
        TextView playCount;
        ImageView centerPlay;
        ImageView play;

        ImageView like;
        TextView likeCount;
        ImageView star;
        TextView starCount;
        ImageView comment;
        TextView commentCount;

        RelativeLayout video;
        RelativeLayout image;
        GridViewY1 grid;

        View isPrivate;
        View upLoading;
        View root;
    }
}
