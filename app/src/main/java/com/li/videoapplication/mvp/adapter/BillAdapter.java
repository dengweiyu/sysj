package com.li.videoapplication.mvp.adapter;


import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseSectionQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.entity.SectionEntity;
import com.li.videoapplication.R;
import com.li.videoapplication.data.image.GlideHelper;
import com.li.videoapplication.data.model.response.BillEntity;
import com.li.videoapplication.tools.TimeHelper;
import com.li.videoapplication.utils.StringUtil;
import com.li.videoapplication.utils.TextUtil;

import java.util.List;


/**
 * 账单
 */

public class BillAdapter extends BaseSectionQuickAdapter<BillEntity.SectionBill,BaseViewHolder> {

    public BillAdapter(List<BillEntity.SectionBill> data) {
        super(R.layout.bill_list_item, R.layout.bill_list_item_header, data);
    }

    @Override
    protected void convertHead(BaseViewHolder baseViewHolder, BillEntity.SectionBill sectionBill) {
        baseViewHolder.setText(R.id.iv_bill_list_title,sectionBill.header);
    }

    @Override
    protected void convert(BaseViewHolder holder, BillEntity.SectionBill sectionBill) {
        BillEntity.DataBean.BillDatasBean.ListBean data = sectionBill.t;
        if (data == null){
            return;
        }
        try {
            holder.setText(R.id.tv_bill_record_day, TimeHelper.getWeek(data.getTime()))
                    .setText(R.id.tv_bill_record_time,TimeHelper.getTime2HmFormat(data.getTime()))
                    .setText(R.id.tv_bill_amount,"余额:"+StringUtil.formatNum(data.getAmount()));

            String description = data.getTitle();
            if (!StringUtil.isNull(data.getMember_name())){
                String member = TextUtil.toColor(data.getMember_name(), "#40a7ff");
                description = description.replace("##",member);
            }

            if (!StringUtil.isNull(data.getVideo_name())){
                String video = TextUtil.toColor(data.getVideo_name(), "#40a7ff");
                description = description.replace("$$",video);
            }

            holder.setText(R.id.tv_bill_description, Html.fromHtml(description));

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
}
