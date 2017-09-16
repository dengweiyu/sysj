package com.li.videoapplication.ui.adapter;

import java.lang.reflect.Field;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.li.videoapplication.R;
import com.li.videoapplication.data.DataManager;
import com.li.videoapplication.data.model.entity.Comment;
import com.li.videoapplication.data.model.entity.Member;
import com.li.videoapplication.data.model.entity.VideoImage;
import com.li.videoapplication.framework.BaseArrayAdapter;
import com.li.videoapplication.tools.TimeHelper;
import com.li.videoapplication.tools.UmengAnalyticsHelper;
import com.li.videoapplication.ui.ActivityManager;
import com.li.videoapplication.ui.activity.VideoPlayActivity;
import com.li.videoapplication.utils.PatternUtil;
import com.li.videoapplication.utils.StringUtil;
import com.li.videoapplication.views.CircleImageView;



/**
 * 适配器：视频玩家评论
 */
@SuppressLint("InflateParams")
public class VideoPlayCommentAdapter extends BaseArrayAdapter<Comment> {

    /**
     * 跳转：玩家动态
     */
    private void startPlayerDynamicActivity(Member member) {
        ActivityManager.startPlayerDynamicActivity(getContext(), member);
    }

    private String[] expressionArray;
    private String[] expressionCnArray;

    private VideoImage item;

    private List<Comment> data;

    public void setVideoImage(VideoImage item) {
        this.item = item;
    }

    public VideoPlayCommentAdapter(Context context, List<Comment> data) {
        super(context, R.layout.adapter_videoplay_comment, data);
        this.data = data;
     //   Emojix.wrap(context);
        expressionArray = context.getResources().getStringArray(R.array.expressionArray);
        expressionCnArray = context.getResources().getStringArray(R.array.expressionCnArray);
    }

    @Override
    public View getView(final int position, View view, ViewGroup parent) {

        final Comment record = getItem(position);
        ViewHolder holder = null;
        if (view == null) {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.adapter_videoplay_comment, null);
            holder.head = (CircleImageView) view.findViewById(R.id.videoplay_comment_head);
            holder.isV = (ImageView) view.findViewById(R.id.videoplay_comment_v);
            holder.name = (TextView) view.findViewById(R.id.videoplay_comment_name);
            holder.content = (TextView) view.findViewById(R.id.videoplay_comment_content);
            holder.identity = (TextView) view.findViewById(R.id.videoplay_comment_identity);
            holder.time = (TextView) view.findViewById(R.id.videoplay_comment_time);
            holder.good = (ImageView) view.findViewById(R.id.videoplay_comment_good);
            holder.goodCount = (TextView) view.findViewById(R.id.videoplay_comment_goodCount);
            holder.comment = (ImageView) view.findViewById(R.id.videoplay_comment_comment);
            holder.delete = (ImageView) view.findViewById(R.id.videoplay_comment_del);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        setImageViewImageNet(holder.head, record.getAvatar());
        setTextViewText(holder.name, record.getNickname());
        setTextViewText(holder.goodCount, record.getLikeNum());
        setTime(record, holder.time);
        setContent(record, holder.content);
        setIdentity(record, holder.identity);
        setGood(record, holder.good);

        if (record.isV()) {
            holder.isV.setVisibility(View.VISIBLE);
        } else {
            holder.isV.setVisibility(View.INVISIBLE);
        }

        if (record.isCanBeDel() && !StringUtil.isNull(getMember_id())) {
            holder.delete.setVisibility(View.VISIBLE);
        } else {
            holder.delete.setVisibility(View.GONE);
        }

        holder.head.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Member member = gson.fromJson(record.toJSON(), Member.class);
                startPlayerDynamicActivity(member);
            }
        });

        holder.comment.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (getContext() instanceof VideoPlayActivity) {
                    VideoPlayActivity activity = (VideoPlayActivity) getContext();
                    activity.commentView.replyComment(record);
                    UmengAnalyticsHelper.onEvent(getContext(), UmengAnalyticsHelper.VIDEOPLAY, "视频播放-回复");
                }
            }
        });

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DataManager.commentDel(getMember_id(), record.getComment_id());
                data.remove(position);
                notifyDataSetInvalidated();
            }
        });

        return view;
    }

    /**
     * 时间
     */
    private void setTime(final Comment record, TextView view) {

        try {
            setTextViewText(view, TimeHelper.getVideoImageUpTime(record.getTime()));
        } catch (Exception e) {
            e.printStackTrace();
            setTextViewText(view, "");
        }

    }

    /**
     * 点赞
     */
    public void setGood(final Comment record, ImageView view) {
        if (record != null) {
            if (record.getLike_tick() == 1) {// 已点赞状态
                view.setImageResource(R.drawable.videoplay_good_red_205);
            } else {// 未点赞状态
                view.setImageResource(R.drawable.videoplay_good_gray_205);
            }
        }
        view.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (!isLogin()) {
                    showToastShort("请先登录！");
                    return;
                }
                boolean flag;
                if (record != null) {
                    if (record.getLike_tick() == 1) {// 已点赞状态
                        flag = false;
                        record.setLikeNum(Integer.valueOf(record.getLikeNum()) - 1 + "");
                        record.setLike_tick(0);
                        UmengAnalyticsHelper.onEvent(getContext(), UmengAnalyticsHelper.VIDEOPLAY, "评论-取消赞");
                    } else {// 未点赞状态
                        flag = true;
                        record.setLikeNum(Integer.valueOf(record.getLikeNum()) + 1 + "");
                        record.setLike_tick(1);
                        UmengAnalyticsHelper.onEvent(getContext(), UmengAnalyticsHelper.VIDEOPLAY, "评论-点赞");
                    }
                    // 视频评论点赞
                    DataManager.videoCommentLike2(record.getComment_id(), getMember_id());
                    notifyDataSetChanged();
                }
            }
        });
    }

    /**
     * 身份
     */
    private void setIdentity(final Comment record, TextView view) {

        if (item != null && !StringUtil.isNull(item.getMember_id())) {
            if (item.getMember_id().equals(record.getMember_id())) {
                view.setVisibility(View.VISIBLE);
                return;
            }
        }
        view.setVisibility(View.GONE);
    }

    /**
     * 评论内容，如果是表情串码则替换显示相应表情
     * ToDo 处理表情
     */
    private void setContent(final Comment record, TextView view) {
        if (record == null || view == null) {
            setTextViewText(view, "");
            return;
        }
        String content = record.getContent();
        if (StringUtil.isNull(content)) {
            setTextViewText(view, "");
            return;
        }

        String json = record.toJSON();
        if (PatternUtil.isContainUnicode(json)) {
            String jsonNew = json.replace("\\\\", "\\");// \\ud83d\\ude24 --> \ud83d\ude24
            Log.d(tag, "jsonNew == "+jsonNew);
            Comment recordNew = gson.fromJson(jsonNew, Comment.class);
            content = recordNew.getContent();
        }

        //处理显示表情
        int len = 0;
        int starts = 0;
        int end = 0;
        SpannableString spannableString = new SpannableString(content);
        while (len < content.length()) {
            if (content.indexOf("[", starts) != -1 && content.indexOf("]", end) != -1) {
                starts = content.indexOf("[", starts);
                end = content.indexOf("]", end);
                String face = content.substring(starts + 1, end);
                for (int i = 0; i < expressionCnArray.length; i++) {
                    if (face.equals(expressionCnArray[i])) {
                        face = expressionArray[i];
                        break;
                    }
                }
                try {
                    Field f = null;
                    try {
                        f = R.drawable.class.getDeclaredField(face);
                    } catch (NoSuchFieldException e) {
                        e.printStackTrace();
                    }
                    if (f != null){
                        int i = f.getInt(R.drawable.class);
                        Drawable drawable = resources.getDrawable(i);
                        if (drawable != null) {
                            drawable.setBounds(0, 0, drawable.getIntrinsicWidth() / 2, drawable.getIntrinsicHeight() / 2);
                            ImageSpan span = new ImageSpan(drawable, ImageSpan.ALIGN_BOTTOM);
                            spannableString.setSpan(span, starts, end + 1, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                        }
                    }else {
                        //[送礼]
                        if ("送礼".equals(face)){
                            ForegroundColorSpan span = new ForegroundColorSpan(Color.RED);
                            spannableString.setSpan(span, starts, end + 1, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                        }
                    }

                } catch (SecurityException | IllegalArgumentException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {

                }
                starts = end;
                len = end;
                end++;
            } else {
                starts++;
                end++;
                len = end;
            }
        }


        view.setText(spannableString);
    }

    private static class ViewHolder {

        CircleImageView head;
        TextView name;
        TextView content;
        TextView identity;
        TextView time;
        ImageView good;
        TextView goodCount;
        ImageView comment;
        ImageView isV;
        ImageView delete;
    }
}
