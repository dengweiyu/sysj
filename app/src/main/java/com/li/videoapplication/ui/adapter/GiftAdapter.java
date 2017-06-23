package com.li.videoapplication.ui.adapter;

import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.li.videoapplication.R;
import com.li.videoapplication.data.model.entity.Gift;
import com.li.videoapplication.framework.BaseArrayAdapter;
import com.li.videoapplication.tools.UmengAnalyticsHelper;
import com.li.videoapplication.ui.ActivityManager;
import com.li.videoapplication.ui.DialogManager;
import com.li.videoapplication.ui.activity.GiftListActivity;
import com.li.videoapplication.ui.activity.GroupGiftActivity;
import com.li.videoapplication.ui.activity.SearchActivity;
import com.li.videoapplication.utils.StringUtil;
import com.li.videoapplication.utils.TextUtil;
import com.li.videoapplication.views.RoundedImageView;

/**
 * 适配器：礼包
 */
@SuppressLint("InflateParams")
public class GiftAdapter extends BaseArrayAdapter<Gift> {

    private Activity activity;

    /**
     * 跳转：礼包详情
     */
    private void startGiftDetailActivity(Gift item) {
        ActivityManager.startGiftDetailActivity(getContext(), item.getId());
        if (activity instanceof GiftListActivity)
            UmengAnalyticsHelper.onEvent(getContext(), UmengAnalyticsHelper.DISCOVER, "热门礼包-有效");
        else if (activity instanceof GroupGiftActivity)
            UmengAnalyticsHelper.onEvent(getContext(), UmengAnalyticsHelper.DISCOVER, "游戏圈-礼包-有效");
        else if (activity instanceof SearchActivity)
            UmengAnalyticsHelper.onEvent(getContext(), UmengAnalyticsHelper.MAIN, "搜索-相关礼包-点击相关礼包内任意礼包进入礼包详情页次数");
    }

    public GiftAdapter(Context context, List<Gift> data) {
        super(context, R.layout.adapter_gift, data);
        try {
            activity = (Activity) context;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public View getView(final int position, View view, ViewGroup parent) {

        final Gift record = getItem(position);
        final ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.adapter_gift, null);
            holder.pic = (RoundedImageView) view.findViewById(R.id.gift_pic);
            holder.status = (ImageView) view.findViewById(R.id.gift_status);
            holder.title = (TextView) view.findViewById(R.id.gift_title);
            holder.count = (TextView) view.findViewById(R.id.gift_count);
            holder.obj = (TextView) view.findViewById(R.id.gift_obj);
            holder.button = (RelativeLayout) view.findViewById(R.id.button);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        setTextViewText(holder.title, record.getTitle());
        setCount(holder.count, record);
        setObj(holder.obj, record);
        setImageViewImageNet(holder.pic, record.getFlag());
        setStatus(holder.status, record);

        holder.button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (StringUtil.isNull(record.getId())) {
                    return;
                }
                startGiftDetailActivity(record);
            }
        });

        return view;
    }

    private class ViewHolder {

        RoundedImageView pic;
        ImageView status;
        TextView title;
        TextView count;// 剩余数量：75%
        TextView obj;
        RelativeLayout button;
    }

    /**
     * 领取礼包的进度条
     *
     * @return 剩余数量：75%
     */
    private void setCount(TextView view, Gift record) {
        // #ff5b4f
        int max = Integer.parseInt(record.getNum());
        int progress = Integer.parseInt(record.getCount());
        String string = String.valueOf((int) (((float) progress / max) * 100) + "%");
        view.setText(Html.fromHtml("剩余数量：" + TextUtil.toColor(string, "#ff5b4f")));
    }

    /**
     * 礼包内容
     */
    private void setObj(TextView view, Gift record) {
        setTextViewText(view, record.getContent());
    }

    /**
     * 状态
     */
    private void setStatus(final ImageView view, final Gift record) {
        final String id = record.getId();
        final String status = record.getStatus();
        final String count = record.getCount();
        //根据礼包码字段设置礼包界面领取按钮状态
        switch (status) {
            case Gift.STATUS_GETTED: // 已领取
                setImageViewImageRes(view, R.drawable.gift_gray);
                break;
            case Gift.STATUS_GET: // 领取
                setImageViewImageRes(view, R.drawable.gift_yellow);
                break;
            case Gift.STATUS_BEGIN: // 即将开始
                setImageViewImageRes(view, R.drawable.gift_blue);
                break;
            case Gift.STATUS_FINISHED: // 已结束
                setImageViewImageRes(view, R.drawable.gift_end);
                break;
            default:
                setImageViewImageRes(view, R.drawable.gift_end);
                break;
        }

        if (count != null && count.equals("0")) {//礼包数量为0
            setImageViewImageRes(view,R.drawable.gift_gray_zero);
        }

        view.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (!isLogin()) {
                    DialogManager.showLogInDialog(getContext());
                    return;
                }
                if (StringUtil.isNull(record.getId())) {
                    return;
                }
                startGiftDetailActivity(record);
            }
        });
    }
}
