package com.li.videoapplication.ui.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.ImageSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.google.gson.Gson;
import com.li.videoapplication.R;
import com.li.videoapplication.data.model.entity.Comment;
import com.li.videoapplication.data.model.entity.MessageReplyComment;
import com.li.videoapplication.tools.TextImageHelper;
import com.li.videoapplication.tools.TimeHelper;
import com.li.videoapplication.utils.PatternUtil;

import java.lang.reflect.Field;
import java.util.List;

/**
 * Created by cx on 2018/1/29.
 */

public class MessageReplyCommentAdapter extends BaseQuickAdapter<MessageReplyComment, BaseViewHolder> {

    private final String TAG = MessageReplyCommentAdapter.class.getSimpleName();
    private Gson gson;

    private Context mContext;

    private TextImageHelper textImageHelper = new TextImageHelper();

    private String[] expressionArray;
    private String[] expressionCnArray;

    public MessageReplyCommentAdapter(Context context, @Nullable List<MessageReplyComment> data) {
        super(R.layout.adapter_msg_reply_comment, data);
        expressionArray = context.getResources().getStringArray(R.array.expressionArray);
        expressionCnArray = context.getResources().getStringArray(R.array.expressionCnArray);
        mContext = context;
        gson = new Gson();
    }

    @Override
    protected void convert(BaseViewHolder helper, MessageReplyComment item) {
        ImageView ivAvatar = helper.getView(R.id.civ_avatar);
        TextView tvContent = helper.getView(R.id.tv_content);
        TextView tvTime = helper.getView(R.id.tv_time);
        ImageView tvComment = helper.getView(R.id.iv_comment);

        textImageHelper.setImageViewImageNet(mContext, ivAvatar, item.getReplyMemberIcon());

        String content = item.getContent();

        //文字表情处理
        String json = item.toJSON();
        if (PatternUtil.isContainUnicode(json)) {
            String jsonNew = json.replace("\\\\", "\\");// \\ud83d\\ude24 --> \ud83d\ude24
            Log.d(TAG, "jsonNew == "+jsonNew);
            MessageReplyComment c = gson.fromJson(jsonNew, MessageReplyComment.class);
            content = c.getContent();
            Log.i(TAG, content);
        }

        setContent(tvContent, item.getReplyMemberName(), item.getReplyedUserName(), content);
        try {
            textImageHelper.setTextViewText(tvTime, TimeHelper.getVideoImageUpTime(item.getAdd_time()));
        } catch (Exception e) {
            e.printStackTrace();
        }



    }

    private void setContent(TextView view, String replyMember, String replyedUser, String content) {
        SpannableStringBuilder ssb = new SpannableStringBuilder();
        StyleSpan styleSpan = new StyleSpan(Typeface.BOLD);
        StyleSpan styleSpan2 = new StyleSpan(Typeface.BOLD);
        ssb.append(replyMember);
        ssb.setSpan(styleSpan, 0, replyMember.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        ssb.setSpan(new RelativeSizeSpan(1.1f), 0, replyMember.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        ssb.append(" 回复 ");
        ssb.append(replyedUser);
        ssb.setSpan(styleSpan2, replyMember.length() + 4,
                replyMember.length() + 4 + replyedUser.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ssb.setSpan(new RelativeSizeSpan(1.1f), replyMember.length() + 4,
                replyMember.length() + 4 + replyedUser.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ssb.append("：");
        ssb.append(content);

        content = ssb.toString();

        //处理显示表情
        int len = 0;
        int start = 0;
        int end = 0;
        while (len < content.length()) {
            if (content.indexOf("[", start) != -1 && content.indexOf("]", end) != -1) {
                start = content.indexOf("[", start);
                end = content.indexOf("]", end);
                String face = content.substring(start + 1, end);
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
                    if (f != null) {
                        int i = f.getInt(R.drawable.class);
                        Drawable drawable = ContextCompat.getDrawable(mContext, i);
                        if (drawable != null) {
                            drawable.setBounds(0, 0, drawable.getIntrinsicWidth() / 2, drawable.getIntrinsicHeight() / 2);
                            ImageSpan span = new ImageSpan(drawable, ImageSpan.ALIGN_BOTTOM);
                            ssb.setSpan(span, start, end + 1, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                        }
                    }

                } catch (SecurityException | IllegalArgumentException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {

                }
                start = end;
                len = end;
                end++;
            } else {
                start++;
                end++;
                len = end;
            }

            view.setText(ssb);
        }


    }


}
