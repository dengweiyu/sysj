package com.li.videoapplication.ui.adapter;


import android.widget.ImageView;
import com.chad.library.adapter.base.BaseSectionQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;
import com.google.common.collect.PeekingIterator;
import com.li.videoapplication.R;
import com.li.videoapplication.data.image.GlideHelper;
import com.li.videoapplication.data.model.response.MyGiftBillEntity;
import java.util.List;

/**
 * 礼物适配器
 */

public class MyGiftBillAdapter extends BaseSectionQuickAdapter<MyGiftBillEntity.SectionBill,BaseViewHolder> {

    public MyGiftBillAdapter( List<MyGiftBillEntity.SectionBill> data) {
        super(R.layout.my_gift_bill_list_item, R.layout.bill_list_item_header, data);
    }

    @Override
    protected void convertHead(BaseViewHolder baseViewHolder, MyGiftBillEntity.SectionBill sectionBill) {
        baseViewHolder.setText(R.id.iv_bill_list_title,sectionBill.header);
    }

    @Override
    protected void convert(BaseViewHolder holder, MyGiftBillEntity.SectionBill sectionBill) {
        MyGiftBillEntity.DataBean.ListBean data = sectionBill.t;
        GlideHelper.displayImage(mContext,data.getGift_icon(),(ImageView)holder.getView(R.id.iv_my_gift_icon));
        holder.setText(R.id.tv_my_gift_name,data.getGift_name())
                .setText(R.id.tv_my_gift_number,"x"+data.getNumber())
                .setText(R.id.tv_my_gift_time,data.getTime())
                .setText(R.id.tv_my_gift_price,data.getReward_price())
                .setText(R.id.tv_my_gift_nick_name,data.getNickname())
                .setText(R.id.tv_my_gift_video_name,data.getVideo_name());

        holder.addOnClickListener(R.id.tv_my_gift_nick_name)
                .addOnClickListener(R.id.tv_my_gift_video_name);


    }

    @Override
    public void setNewData(final List<MyGiftBillEntity.SectionBill> data) {
        //no child view, remove title
        super.setNewData(Lists.newArrayList(Iterables.filter(data, new Predicate<MyGiftBillEntity.SectionBill>() {
            PeekingIterator iterator = Iterators.peekingIterator(data.iterator());
            @Override
            public boolean apply(MyGiftBillEntity.SectionBill input) {
                if (iterator.hasNext()){
                    iterator.next();
                }
                if (input.isHeader){
                    if (iterator.hasNext()){
                        if (((MyGiftBillEntity.SectionBill)iterator.peek()).isHeader){
                            return false;
                        }
                    }else {
                        return false;
                    }
                }
                return true;
            }
        })));
    }



}
