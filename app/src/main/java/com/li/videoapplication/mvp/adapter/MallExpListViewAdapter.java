package com.li.videoapplication.mvp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.li.videoapplication.R;
import com.li.videoapplication.data.model.entity.Currency;
import com.li.videoapplication.data.preferences.PreferencesHepler;
import com.li.videoapplication.tools.TextImageHelper;
import com.li.videoapplication.ui.ActivityManeger;
import com.li.videoapplication.ui.DialogManager;
import com.li.videoapplication.ui.activity.WebActivity;
import com.li.videoapplication.tools.ToastHelper;
import com.li.videoapplication.utils.StringUtil;

import java.util.List;

/**
 * 适配器：商品列表
 */

public class MallExpListViewAdapter extends BaseExpandableListAdapter {

    private static final String TAG = MallExpListViewAdapter.class.getSimpleName();
    private Context context;
    private List<Currency> mDatas;
    private TextImageHelper helper;

    /**
     * 跳转：商品详情
     */
    private void startProductsDetailActivity(Currency childList) {
            ActivityManeger.startProductsDetailActivity(context, childList.getId(),childList.getEvents());
    }

    public MallExpListViewAdapter(Context context, List<Currency> mDatas) {
        this.context = context;
        this.mDatas = mDatas;
        helper = new TextImageHelper();
    }

    @Override
    public int getGroupCount() {
        return mDatas.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return mDatas.get(groupPosition).getList().size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return mDatas.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return mDatas.get(groupPosition).getList().get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            convertView = inflater.inflate(R.layout.adapter_mall_header, null);
        }
        convertView.setTag(R.layout.adapter_mall_header, groupPosition);
        convertView.setTag(R.layout.adapter_mall_item, -1);

        TextView categoryname = (TextView) convertView.findViewById(R.id.mall_header_categoryname);
        helper.setTextViewText(categoryname, mDatas.get(groupPosition).getCategory_name());
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        final Currency childList = mDatas.get(groupPosition).getList().get(childPosition);
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            convertView = inflater.inflate(R.layout.adapter_mall_item, parent, false);
        }
        convertView.setTag(R.layout.adapter_mall_header, groupPosition);
        convertView.setTag(R.layout.adapter_mall_item, -1);

        TextView name = (TextView) convertView.findViewById(R.id.mall_item_name);
        TextView description = (TextView) convertView.findViewById(R.id.mall_item_description);
        View beanView = convertView.findViewById(R.id.mall_item_beanview);
        TextView beanNum = (TextView) convertView.findViewById(R.id.mall_item_beannum);
        TextView overplus = (TextView) convertView.findViewById(R.id.mall_item_overplus);
        TextView exchange = (TextView) convertView.findViewById(R.id.mall_item_exchange);
        final ImageView pic = (ImageView) convertView.findViewById(R.id.mall_item_pic);

        if (childList.getExchange_way().equals("0") || //周边商品
                childList.getExchange_way().equals("1")) {//首页推荐位
            description.setVisibility(View.VISIBLE);
            beanView.setVisibility(View.GONE);
            helper.setTextViewText(description, childList.getDescription());
            helper.setTextViewText(exchange, "详情");
        } else {
            description.setVisibility(View.GONE);
            beanView.setVisibility(View.VISIBLE);
            helper.setTextViewText(beanNum, StringUtil.formatNum(childList.getCurrency_num()));
            helper.setTextViewText(overplus, childList.getInventory());
            helper.setTextViewText(exchange, "兑换");
        }
        helper.setTextViewText(name, childList.getName());
        helper.setImageViewImageNet(pic, childList.getCover());

        exchange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!StringUtil.isNull(childList.getExchange_way())) {
                    switch (childList.getExchange_way()) {
                        case "0"://周边商品
                            WebActivity.startWebActivity(context, childList.getUrl());
                            break;
                        case "1"://推荐位
                        case "5"://冠名位
                            startProductsDetailActivity(childList);
                            break;
                        case "2"://话费流量类
                        case "3"://Q币类
                        case "4"://京东卡类
                        case "6"://战网兑换类
                            if (Integer.valueOf(childList.getInventory()) > 0) {
                                if (PreferencesHepler.getInstance().isLogin()){
                                    int myCurrency = Integer.valueOf(PreferencesHepler.getInstance()
                                            .getUserProfilePersonalInformation().getCurrency());

                                    if (myCurrency >= Integer.valueOf(childList.getCurrency_num())) {
                                        DialogManager.showPaymentDialog(context, childList);
                                    } else {
                                        ToastHelper.s("飞磨豆不足");
                                    }
                                }else {
                                    DialogManager.showLogInDialog(context);
                                }
                            } else {
                                ToastHelper.s("商品已售罄");
                            }
                            break;
                    }
                }
            }
        });

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (childList.getExchange_way().equals("0")) {
                    WebActivity.startWebActivity(context, childList.getUrl());
                } else {
                    startProductsDetailActivity(childList);
                }
            }
        });

        return convertView;
    }

    //  子项是否可选中，如果需要设置子项的点击事件，需要返回true
    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

}
