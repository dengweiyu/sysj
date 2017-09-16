package com.li.videoapplication.mvp.adapter;


import android.graphics.Color;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseSectionQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.li.videoapplication.R;
import com.li.videoapplication.data.image.GlideHelper;
import com.li.videoapplication.data.model.entity.Member;
import com.li.videoapplication.data.model.entity.VideoImage;
import com.li.videoapplication.data.model.response.BillEntity;
import com.li.videoapplication.tools.TimeHelper;
import com.li.videoapplication.ui.ActivityManager;
import com.li.videoapplication.utils.StringUtil;

import java.util.List;


/**
 * 账单
 */

public class BillAdapter extends BaseSectionQuickAdapter<BillEntity.SectionBill,BaseViewHolder> {

    private String mMemberId;
    public BillAdapter(List<BillEntity.SectionBill> data,String memberId) {
        super(R.layout.bill_list_item, R.layout.bill_list_item_header, data);
        mMemberId = memberId;
    }

    @Override
    protected void convertHead(BaseViewHolder baseViewHolder, BillEntity.SectionBill sectionBill) {
        baseViewHolder.setText(R.id.iv_bill_list_title,sectionBill.header);
    }

    @Override
    protected void convert(BaseViewHolder holder, final BillEntity.SectionBill sectionBill) {
        final BillEntity.DataBean.BillDatasBean.ListBean data = sectionBill.t;
        if (data == null){
            return;
        }
        try {
            holder.setText(R.id.tv_bill_record_day, TimeHelper.getWeek(data.getTime()))
                    .setText(R.id.tv_bill_record_time,TimeHelper.getTime2HmFormat(data.getTime()))
                    .setText(R.id.tv_bill_amount,"余额:"+StringUtil.formatNum(data.getAmount()));

            String description = data.getTitle();

            String memberName = data.getMember_name();
            String videoName = data.getVideo_name();
            if (!StringUtil.isNull(memberName)){
                description = description.replace("##",memberName);
            }

            if (!StringUtil.isNull(videoName)){
                description = description.replace("$$",videoName);
            }

            SpannableString spannable = new SpannableString(description);

            if (!StringUtil.isNull(memberName)){
                int start = description.indexOf(memberName);
                int end  = start+memberName.length();
                spannable.setSpan(new Clickable(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        notifyDataSetChanged();         //必要的  LinkMovementMethod.getInstance() 会将被点击的部分 移动 显示出来 ，因此重新渲染  还要求后台现在长度 保证在能在屏幕中显示完全
                        if (mMemberId != null){
                            if (!mMemberId.equals(data.getMember_id())){
                                Member member = new Member();
                                member.setMember_id(data.getMember_id());
                                ActivityManager.startPlayerDynamicActivity(mContext,member);
                            }
                        }

                    }
                }),start,end, Spanned.SPAN_MARK_MARK);
            }


            if (!StringUtil.isNull(videoName)){
                int start = description.indexOf(videoName);
                int end  = start+videoName.length();
                spannable.setSpan(new Clickable(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        notifyDataSetChanged();
                        VideoImage video = new VideoImage();
                        video.setVideo_id(data.getVideo_id());
                        ActivityManager.startVideoPlayActivity(mContext,video);
                    }
                }),start,end, Spanned.SPAN_MARK_MARK);
            }

            TextView text = holder.getView(R.id.tv_bill_description);
            text.setMovementMethod(LinkMovementMethod.getInstance());
            holder.setText(R.id.tv_bill_description, spannable);

            if (data.getOperation().equals("1")){
                holder.setText(R.id.tv_bill_number,"+"+data.getNum());
            }else {
                holder.setText(R.id.tv_bill_number,"-"+data.getNum());
            }

            GlideHelper.displayImage(mContext,data.getIco(),(ImageView) holder.getView(R.id.iv_bill_record_icon));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

   public static class Clickable extends ClickableSpan{
       private View.OnClickListener mListener;
       public Clickable(View.OnClickListener listener){
           mListener = listener;
       }

       @Override
       public void onClick(View v) {
            mListener.onClick(v);
       }

       @Override
       public void updateDrawState(TextPaint ds) {
           super.updateDrawState(ds);

           ds.setColor(Color.parseColor("#40a7ff"));
           ds.setUnderlineText(false);
       }
   }
}
