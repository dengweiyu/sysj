package com.li.videoapplication.mvp.mall.view;

import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Spannable;
import android.text.SpannableString;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.IPullToRefresh;
import com.li.videoapplication.R;
import com.li.videoapplication.data.DataManager;
import com.li.videoapplication.data.VipManager;
import com.li.videoapplication.data.image.GlideHelper;
import com.li.videoapplication.data.model.response.VipRecharge2Entity;
import com.li.videoapplication.data.preferences.PreferencesHepler;
import com.li.videoapplication.framework.CommonAdapter;
import com.li.videoapplication.framework.TBaseFragment;
import com.li.videoapplication.framework.ViewHolder;
import com.li.videoapplication.mvp.mall.model.MallModel;
import com.li.videoapplication.tools.UmengAnalyticsHelper;
import com.li.videoapplication.tools.UmengAnalyticsHelper2;
import com.li.videoapplication.ui.ActivityManager;
import com.li.videoapplication.utils.StringUtil;
import com.li.videoapplication.utils.TextUtil;


import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import butterknife.BindView;
import butterknife.BindViews;


/**
 * Created by y on 2017/5/16.
 * vip 充值
 */
public class RechargeVIP2Fragment extends TBaseFragment implements View.OnClickListener {
    @BindView(R.id.recharge_vip_ListViewY1)ListView recharge_vip_ListViewY1;

    @BindView(R.id.recharge_vip_money)
    TextView recharge_vip_money;
    @BindView(R.id.recharge_vip_pay_now)
    TextView recharge_vip_pay_now;
    //    @Bind(R.id.id_recharge_old_vip_money)
//    TextView id_recharge_old_vip_money;
    //    @Bind(R.id.id_recharge_old_vip_money_layout)
//    View id_recharge_old_vip_money_layout;
    @BindView(R.id.vip_level_layout_1)
    View vip_level_layout_1;
    @BindView(R.id.vip_level_layout_2)
    View vip_level_layout_2;
    @BindView(R.id.vip_level_layout_3)
    View vip_level_layout_3;
    @BindViews({R.id.id_vip_level_name_1, R.id.id_vip_level_name_2, R.id.id_vip_level_name_3})
    TextView[] id_vip_level_names;
    @BindViews({R.id.id_vip_level_month_money_1, R.id.id_vip_level_month_money_2, R.id.id_vip_level_month_money_3})
    TextView[] id_vip_level_month_moneys;
    @BindView(R.id.migu_vip_parent_layout)
    View migu_vip_parent_layout;
    @BindView(R.id.id_sale_layout)
    View id_sale_layout;
    @BindView(R.id.recharge_sale_vip_money)
    TextView recharge_sale_vip_money;

    private VipListDelegate vipListDelegate;

    /**
     * 月份按钮
     */
    private TextView[] buyMonthViews = new TextView[4];
    private Drawable[] monthSelectDrawables = new Drawable[2];
    //当前月份下标
    private int thisMonthPosition = -1;
    private int vipIndex = -1;
    private List<VipRecharge2Entity.PackageMemuBean> packageMemuBeen = new ArrayList<>();
    private int chooseLevel;
    private float chooseMoney;
    private float chooseRealityMoney;
    private float chooseSaleRealityMoney;
    private List<VipRecharge2Entity.Data> data = new ArrayList<>();
    private final List<VipRecharge2Entity.VipAllInfo> vipAllInfos = new ArrayList<>();

    private String keepSale = "1";

    public static RechargeVIP2Fragment newInstance(){
        RechargeVIP2Fragment fragment = new RechargeVIP2Fragment();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected int getCreateView() {
        return R.layout.fragment_vip_detail;
    }


    @Override
    protected IPullToRefresh getPullToRefresh() {
        return null;
    }

    protected int[] levelPositions = new int[3];

    @Override
    public void initContentView(View view) {

        UmengAnalyticsHelper2.onEvent(UmengAnalyticsHelper2.VIP_ID, UmengAnalyticsHelper2.VIP_PAGE_SHOW_COUNT_TYPE);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        vipListDelegate = new VipListDelegate();
//        recharge_vip_ListViewY1 = (ListView) view.findViewById(R.id.recharge_vip_ListViewY1);
        vipListDelegate.init();
//        recharge_vip_ListViewY1.setAdapter(new CommonAdapter<VipRecharge2Entity.VipAllInfo>(getActivity(), vipAllInfos, R.layout.adapter_vip_detail) {
//            @Override
//            public void convert(ViewHolder holder, VipRecharge2Entity.VipAllInfo vipAllInfo, int position) {
//                holder.setText(R.id.id_vip_detail_tv, vipAllInfo.getName());
//                if (vipAllInfo.isClick()) {
//                    holder.setTextColor(R.id.id_vip_detail_tv, ActivityCompat.getColor(getActivity(), R.color.menu_help_blue_1));
//                    ImageHelper.displayImage(getActivity(), vipAllInfo.getSelectedCoin(), (ImageView) holder.getView(R.id.id_main_iv));
//                    holder.setImageView(R.id.choose_iv, R.drawable.pay_checkbox_select_blue);
//                } else {
//                    holder.setTextColor(R.id.id_vip_detail_tv, ActivityCompat.getColor(getActivity(), R.color.color_666666));
//                    ImageHelper.displayImage(getActivity(), vipAllInfo.getNotSelectedCoin(), (ImageView) holder.getView(R.id.id_main_iv));
//                    holder.setImageView(R.id.choose_iv, R.drawable.loginphone_deal_gray_);
//                }
//            }
//        });
//        recharge_vip_ListViewY1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
//                ThreadUtil.start(new Runnable() {
//                    @Override
//                    public void run() {
//                        onVipItemClickListener(position);
//                    }
//                });
//            }
//        });

        monthSelectDrawables[0] = getActivity().getResources().getDrawable(R.drawable.shape_gray);
        monthSelectDrawables[1] = getActivity().getResources().getDrawable(R.drawable.shape_main_bule_solid);
        buyMonthViews[0] = (TextView) view.findViewById(R.id.id_one_month_btn);

        buyMonthViews[1] = (TextView) view.findViewById(R.id.id_three_month_btn);
        buyMonthViews[1].setTag(view.findViewById(R.id.id_three_month_discount_iv));

        buyMonthViews[2] = (TextView) view.findViewById(R.id.id_six_month_btn);
        buyMonthViews[2].setTag(view.findViewById(R.id.id_six_month_discount_iv));

        buyMonthViews[3] = (TextView) view.findViewById(R.id.id_twelve_month_btn);
        buyMonthViews[3].setTag(view.findViewById(R.id.id_twelve_month_discount_iv));

        for (View v : buyMonthViews) {
            v.setOnClickListener(this);
        }
        recharge_vip_pay_now.setOnClickListener(this);
        vip_level_layout_1.setOnClickListener(this);
        vip_level_layout_2.setOnClickListener(this);
        vip_level_layout_3.setOnClickListener(this);

        //咪咕
//        if (UmengAnalyticsHelper.isMiguVipShowByChannel()) {
//            migu_vip_parent_layout.setVisibility(View.VISIBLE);
//        } else {
//            migu_vip_parent_layout.setVisibility(View.GONE);
//        }

        migu_vip_parent_layout.setOnClickListener(this);

        loadData();
    }

//    @Deprecated
//    private synchronized void onVipItemClickListener(final int position) {
//        if (vipAllInfos.size() > position) {
//            VipRecharge2Entity.VipAllInfo vipAllInfo = vipAllInfos.get(position);
//            for (int i = 0; i <= position; i++) {
//                vipAllInfos.get(i).setClick(true);
//            }
//            for (int i = position; i < vipAllInfos.size(); i++) {
//
//                if (vipAllInfo.getLevel() >= vipAllInfos.get(i).getLevel()) {
//                    vipAllInfos.get(i).setClick(true);
//                } else {
//                    vipAllInfos.get(i).setClick(false);
//                }
//            }
//            getActivity().runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    changeChooseVip(vipAllInfos.get(position).getLevel() - 1, false);
//                    ((BaseAdapter) recharge_vip_ListViewY1.getAdapter()).notifyDataSetChanged();
//                }
//            });
//        }
//    }

    private synchronized void changeMonth(final int newMonthPosition) {

        if (thisMonthPosition == -1) {
            buyMonthViews[0].setBackground(monthSelectDrawables[1]);
            buyMonthViews[0].setTextColor(Color.WHITE);
            thisMonthPosition = 0;
        } else {
            if (thisMonthPosition == newMonthPosition) {
                return;
            }
            buyMonthViews[thisMonthPosition].setBackground(monthSelectDrawables[0]);
            buyMonthViews[thisMonthPosition].setTextColor(Color.parseColor("#999999"));
            if (thisMonthPosition != 0) {
                ((ImageView) buyMonthViews[thisMonthPosition].getTag()).
                        setImageBitmap(BitmapFactory.decodeResource(getActivity().getResources(), R.drawable.no_discount));
            }
            buyMonthViews[newMonthPosition].setBackground(monthSelectDrawables[1]);
            buyMonthViews[newMonthPosition].setTextColor(Color.WHITE);
            if (newMonthPosition != 0) {
                ((ImageView) buyMonthViews[newMonthPosition].getTag()).
                        setImageBitmap(BitmapFactory.decodeResource(getActivity().getResources(), R.drawable.discount));
            }
            thisMonthPosition = newMonthPosition;
        }
        setMoney(data);
    }

    private void loadData() {
        DataManager.getVIPRechargeInfo2();
    }

    /**
     * 设置金额
     *
     * @param data
     */
    private void setMoney(List<VipRecharge2Entity.Data> data) {
        if (data.size() == 0) {
            return;
        }
        chooseLevel = data.get(vipIndex).getLevel();
        int money = data.get(vipIndex).getPrice();
//        id_recharge_old_vip_money_layout.setVisibility(View.VISIBLE);

        switch (thisMonthPosition) {
            case 0: //1个月
                chooseMoney = money;
                chooseRealityMoney = money * getDiscount(0);
//                id_recharge_old_vip_money_layout.setVisibility(View.GONE);
                break;
            case 1: //3个月
                chooseMoney = money * 3;
                chooseRealityMoney = money * 3 * getDiscount(1);
                break;
            case 2: //6个月
                chooseMoney = money * 6;
                chooseRealityMoney = money * 6 * getDiscount(2);
                break;
            case 3: //12个月
                chooseMoney = money * 12;
                chooseRealityMoney = money * 12 * getDiscount(3);
                break;
        }
        handlerMoneyShow();
        handlerSale();
    }

    /**
     * 处理普通折扣
     */
    private void handlerMoneyShow() {
        String strPay = "支付：" + StringUtil.formatNum2(chooseRealityMoney) + " 元";
        if (thisMonthPosition != -1 && getDiscount(thisMonthPosition) == 1) {
            Spannable s = new SpannableString(strPay);
            TextUtil.setForegroundColorText(s, 3, strPay.length(), ActivityCompat.getColor(getActivity(), R.color.menu_help_blue_1));
            recharge_vip_money.setText(s);
            recharge_vip_money.setTag(s);
        } else {
            String oldMoney = "  原价" + chooseMoney;
            Spannable s = new SpannableString(strPay + oldMoney);
            TextUtil.setForegroundColorText(s, 3, strPay.length(), ActivityCompat.getColor(getActivity(), R.color.menu_help_blue_1));
            TextUtil.setForegroundColorText(s, strPay.length(), s.length(), ActivityCompat.getColor(getActivity(), R.color.color_999999));
            TextUtil.setStrikethroughSpan(s, strPay.length(), s.length());
            recharge_vip_money.setText(s);
            recharge_vip_money.setTag(s);
        }
    }

    /**
     * 处理续费折扣
     */
    private void handlerSale() {

        chooseSaleRealityMoney = chooseRealityMoney;
        if (VipManager.getInstance().isSaleVipUser(chooseLevel + "")) {
            id_sale_layout.setVisibility(View.VISIBLE);
            try {
                chooseSaleRealityMoney = chooseRealityMoney * Float.parseFloat(keepSale);
            } catch (Exception ex) {

                ex.printStackTrace();
            }
            Spannable s = new SpannableString("续费：" + StringUtil.formatNum2(chooseSaleRealityMoney) + " 元（续费优惠）");
            TextUtil.setForegroundColorText(s, 3, s.length() - 6, ActivityCompat.getColor(getActivity(), R.color.menu_help_blue_1));
            TextUtil.setSizeText(s, s.length() - 6, s.length(), 12);
            TextUtil.setForegroundColorText(s, s.length() - 6, s.length(), ActivityCompat.getColor(getActivity(), R.color.color_999999));
            recharge_sale_vip_money.setText(s);
            if (recharge_vip_money.getTag() != null && recharge_vip_money.getTag() instanceof SpannableString) {
                Spannable oldSpannable = (Spannable) recharge_vip_money.getTag();
                TextUtil.setStrikethroughSpan(oldSpannable, 0, oldSpannable.length());
                recharge_vip_money.setText(oldSpannable);
            } else {
                id_sale_layout.setVisibility(View.GONE);
            }
        } else {
            id_sale_layout.setVisibility(View.GONE);
        }

    }

    private void setMonthText() {
        if (packageMemuBeen != null) {
            for (int i = 0; i < buyMonthViews.length; i++) {
                buyMonthViews[i].setText(packageMemuBeen.get(i).getText());
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.recharge_vip_pay_now:
                onEventMessage();
                UmengAnalyticsHelper2.onEvent(UmengAnalyticsHelper2.VIP_ID, UmengAnalyticsHelper2.VIP_BUY_TYPE);
                if (!PreferencesHepler.getInstance().isLogin()) {
                    ActivityManager.startLoginActivity(getActivity());
                    return;
                }
                if (chooseMoney <= 0) {
                    showToastShort("请输入您要购买VIP的月份！");
                } else {
                    int mum = getMonthCount();
                    Log.d(tag, "onClick: 等级 = " + chooseLevel + " , 待支付金额 = " + chooseMoney + " , 月份 = " + mum);
                    if (mum != -1) {
                        TopUpActivity activity = (TopUpActivity) getActivity();
                        startPaymentWayActivity(chooseSaleRealityMoney, activity.entry, chooseLevel, mum);
                    }
                }
                break;
            case R.id.id_one_month_btn:
                changeMonth(0);
                break;
            case R.id.id_three_month_btn:
                changeMonth(1);
                break;
            case R.id.id_six_month_btn:
                changeMonth(2);
                break;
            case R.id.id_twelve_month_btn:
                changeMonth(3);
                break;
//            case R.id.vip_level_layout_1:
//                changeChooseVip(0);
//                break;
//            case R.id.vip_level_layout_2:
//                changeChooseVip(1);
//                break;
//            case R.id.vip_level_layout_3:
//                changeChooseVip(2);
//                break;
//            case R.id.migu_vip_parent_layout:
//                ActivityManager.startMiguPayActivity(getActivity());
//                break;
        }
    }

    /**
     * 友盟统计
     */
    private void onEventMessage() {


        switch (chooseLevel) {
            case 1:
                switch (thisMonthPosition) {
                    case 0:
                        UmengAnalyticsHelper2.onEvent(UmengAnalyticsHelper2.VIP_ID, UmengAnalyticsHelper2.VIP_V1_CHOOSE_MONTH_1_TYPE);
                        //return 1;
                        break;
                    case 1:
                        UmengAnalyticsHelper2.onEvent(UmengAnalyticsHelper2.VIP_ID, UmengAnalyticsHelper2.VIP_V1_CHOOSE_MONTH_3_TYPE);
                        //return 3;
                        break;
                    case 2:
                        UmengAnalyticsHelper2.onEvent(UmengAnalyticsHelper2.VIP_ID, UmengAnalyticsHelper2.VIP_V1_CHOOSE_MONTH_6_TYPE);
                        //return 6;
                        break;
                    case 3:
                        UmengAnalyticsHelper2.onEvent(UmengAnalyticsHelper2.VIP_ID, UmengAnalyticsHelper2.VIP_V1_CHOOSE_MONTH_12_TYPE);
                        // return 12;
                        break;
                }
                break;
            case 2:
                switch (thisMonthPosition) {
                    case 0:
                        UmengAnalyticsHelper2.onEvent(UmengAnalyticsHelper2.VIP_ID, UmengAnalyticsHelper2.VIP_V2_CHOOSE_MONTH_1_TYPE);
                        //return 1;
                        break;
                    case 1:
                        UmengAnalyticsHelper2.onEvent(UmengAnalyticsHelper2.VIP_ID, UmengAnalyticsHelper2.VIP_V2_CHOOSE_MONTH_3_TYPE);
                        //return 3;
                        break;
                    case 2:
                        UmengAnalyticsHelper2.onEvent(UmengAnalyticsHelper2.VIP_ID, UmengAnalyticsHelper2.VIP_V2_CHOOSE_MONTH_6_TYPE);
                        //return 6;
                        break;
                    case 3:
                        UmengAnalyticsHelper2.onEvent(UmengAnalyticsHelper2.VIP_ID, UmengAnalyticsHelper2.VIP_V2_CHOOSE_MONTH_12_TYPE);
                        // return 12;
                        break;
                }
                break;
            case 3:
                switch (thisMonthPosition) {
                    case 0:
                        UmengAnalyticsHelper2.onEvent(UmengAnalyticsHelper2.VIP_ID, UmengAnalyticsHelper2.VIP_V3_CHOOSE_MONTH_1_TYPE);
                        //return 1;
                        break;
                    case 1:
                        UmengAnalyticsHelper2.onEvent(UmengAnalyticsHelper2.VIP_ID, UmengAnalyticsHelper2.VIP_V3_CHOOSE_MONTH_3_TYPE);
                        //return 3;
                        break;
                    case 2:
                        UmengAnalyticsHelper2.onEvent(UmengAnalyticsHelper2.VIP_ID, UmengAnalyticsHelper2.VIP_V3_CHOOSE_MONTH_6_TYPE);
                        //return 6;
                        break;
                    case 3:
                        UmengAnalyticsHelper2.onEvent(UmengAnalyticsHelper2.VIP_ID, UmengAnalyticsHelper2.VIP_V3_CHOOSE_MONTH_12_TYPE);
                        // return 12;
                        break;
                }
                break;
        }


    }

    private void changeChooseVip(int i) {
        changeChooseVip(i, true);
    }

    private void changeChooseVip(int i, boolean isperformItemClick) {
        if (vipIndex != -1) {
            id_vip_level_names[vipIndex].setTextColor(Color.parseColor("#999999"));
            id_vip_level_month_moneys[vipIndex].setTextColor(Color.parseColor("#999999"));
        }
        id_vip_level_names[i].setTextColor(ActivityCompat.getColor(getActivity(), R.color.menu_help_blue_1));
        id_vip_level_month_moneys[i].setTextColor(ActivityCompat.getColor(getActivity(), R.color.menu_help_blue_1));
        vipIndex = i;
        setMoney(data);
        if (isperformItemClick) {
            recharge_vip_ListViewY1.performItemClick(null, levelPositions[i], 1);
        }
    }

    private void initVipDetail(List<VipRecharge2Entity.VipAllInfo> vipAllInfo) {
        vipAllInfos.clear();
        vipAllInfos.addAll(vipAllInfo);

        int i = 0;
        int last = -1;
        for (int p = 0; p < vipAllInfos.size(); p++) {
            if (last != vipAllInfos.get(p).getLevel()) {
                levelPositions[i] = p;
                last = vipAllInfos.get(p).getLevel();
                i++;
                if (levelPositions.length == i) {
                    return;
                }
            }
        }

    }

    /**
     * 获取月份
     *
     * @return
     */
    private int getMonthCount() {
        switch (thisMonthPosition) {
            case 0:
                return 1;
            case 1:
                return 3;
            case 2:
                return 6;
            case 3:
                return 12;
        }
        showToastShort("请输入正确的月份格式");
        return -1;
    }

//    /**
//     * 获取会员信息回调
//     */
//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void onMessage(VipRecharge2Entity entity) {
//        if (entity != null && entity.isResult()) {
//            keepSale = entity.getRenewalDiscount();
//            packageMemuBeen.clear();
//            packageMemuBeen.addAll(entity.getPackageMemu());
//            data.clear();
//            data.addAll(entity.getData());
//            initVipDetail(entity.getVipAllInfo());
//            setMonthText();
//            setVipLevelInfo();
//            vipListDelegate.refresh();
//            changeChooseVip(0);
//            buyMonthViews[0].performClick();
//
//        }
//    }

    /**
     * 获取会员信息回调
     */
    public void onEventMainThread(VipRecharge2Entity entity) {
        if (entity != null && entity.isResult()) {
            keepSale = entity.getRenewalDiscount();
            packageMemuBeen.clear();
            packageMemuBeen.addAll(entity.getPackageMemu());
            data.clear();
            data.addAll(entity.getData());
            initVipDetail(entity.getVipAllInfo());
            setMonthText();
            setVipLevelInfo();
            vipListDelegate.refresh();
            changeChooseVip(0);
            buyMonthViews[0].performClick();

        }
    }

    private void setVipLevelInfo() {

        for (VipRecharge2Entity.Data d : data) {
            try {
                id_vip_level_names[d.getLevel() - 1].setText(d.getName());
                id_vip_level_month_moneys[d.getLevel() - 1].setText(String.format("%d元/月", d.getPrice()));
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    /**
     * 获得折扣率
     *
     * @param i 按钮下标
     * @return
     */
    public float getDiscount(int i) {
        if (packageMemuBeen.size() < 1) {
            return 1;
        }
        float c = 0;
        try {
            c = packageMemuBeen.get(i).getDiscount();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return c;
    }

    private class VipListDelegate {

        final List<VipDelegateInfo> vipDelegateInfoList = new ArrayList<>();

        AtomicInteger choosePosition = new AtomicInteger(1);

        void init() {
            recharge_vip_ListViewY1.setAdapter(new CommonAdapter<VipDelegateInfo>(
                    getActivity(), vipDelegateInfoList, R.layout.adapter_vip_new2) {

                @Override
                public void convert(ViewHolder holder, VipDelegateInfo vipDelegateInfo, int position) {
                    ImageView id_vip_ic = holder.getView(R.id.id_vip_ic);
                    id_vip_ic.setVisibility(View.VISIBLE);
                    holder.setImageView(R.id.id_vip_check, R.drawable.pay_checkbox_select_blue);
                    GlideHelper.displayImageEmpty(getActivity(), vipDelegateInfo.getChooseVipImage(), id_vip_ic);
                    holder.getView(R.id.id_lineView).setVisibility(View.VISIBLE);

                    boolean enableTextColor = true;
                    if (position == vipDelegateInfoList.size() - 1) {
                        holder.getView(R.id.id_lineView).setVisibility(View.GONE);

                    }
                    if (choosePosition.get() > vipDelegateInfo.getViplevel()) {
                        id_vip_ic.setVisibility(View.GONE);
                        holder.getView(R.id.id_lineView).setVisibility(View.GONE);
                    } else if (choosePosition.get() < vipDelegateInfo.getViplevel()) {
                        holder.setImageView(R.id.id_vip_check, R.drawable.loginphone_deal_gray_);
                        GlideHelper.displayImageEmpty(getActivity(), vipDelegateInfo.getNoChooseVipImage(), (ImageView) holder.getView(R.id.id_vip_ic));
                        enableTextColor = false;
                    }


                    {
                        ViewGroup id_vip_detail_layout = holder.getView(R.id.id_vip_detail_layout);
                        id_vip_detail_layout.removeAllViews();
                        List<View> vs = new ArrayList<>();
                        for (int i = 0; i < vipDelegateInfo.getVipDetails().size(); i++) {
                            vs.add(setDetailVip(vipDelegateInfo.getVipDetails().get(i), id_vip_detail_layout, enableTextColor));
                        }
                        for (View v : vs) {
                            id_vip_detail_layout.addView(v);
                        }
                    }


                }
            });


            recharge_vip_ListViewY1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                    chooseLevel = vipDelegateInfoList.get(position).getViplevel();
                    switch (chooseLevel) {
                        case 2:
                            UmengAnalyticsHelper2.onEvent(UmengAnalyticsHelper2.VIP_ID, UmengAnalyticsHelper2.VIP_V2_CHOOSE_TYPE);
                            break;
                        case 3:
                            UmengAnalyticsHelper2.onEvent(UmengAnalyticsHelper2.VIP_ID, UmengAnalyticsHelper2.VIP_V3_CHOOSE_TYPE);
                            break;
                    }

                    choosePosition.set(chooseLevel);
                    changeChooseVip(chooseLevel - 1, false);
                    notifyAdapter();
                }
            });

        }


        void refresh() {
            vipDelegateInfoList.clear();
            for (VipRecharge2Entity.Data d : data) {
                VipDelegateInfo delegateInfo = new VipDelegateInfo();
                delegateInfo.chooseVipImage = d.getSelectedIcon();
                delegateInfo.noChooseVipImage = d.getNoSelectedIcon();
                delegateInfo.viplevel = d.getLevel();
                for (VipRecharge2Entity.VipAllInfo vipAllInfo : vipAllInfos) {

                    if (vipAllInfo.getLevel() == d.getLevel()) {
                        ViewDetailInfo viewDetailInfo = new ViewDetailInfo();
                        viewDetailInfo.string = vipAllInfo.getName();
                        viewDetailInfo.chooseImage = vipAllInfo.getSelectedCoin();
                        viewDetailInfo.noChooseImage = vipAllInfo.getNotSelectedCoin();
                        viewDetailInfo.level = d.getLevel();
                        delegateInfo.vipDetails.add(viewDetailInfo);
                    }


                }
                vipDelegateInfoList.add(delegateInfo);
            }
            notifyAdapter();


        }

        private void notifyAdapter() {
            if (recharge_vip_ListViewY1.getAdapter() != null) {
                ((BaseAdapter) recharge_vip_ListViewY1.getAdapter()).notifyDataSetChanged();
            }
        }

        private View setDetailVip(ViewDetailInfo viewDetailInfo, ViewGroup v, boolean colorFlag) {
            View vipStringView = LayoutInflater.from(getActivity()).inflate(R.layout.item_vip_detail, v, false);
            TextView vipString = (TextView) vipStringView.findViewById(R.id.id_vip_detail_tv);
            vipString.setText(viewDetailInfo.getString());
            if (colorFlag) {
                vipString.setTextColor(ActivityCompat.getColor(getActivity(), R.color.menu_help_blue_1));
            } else {
                vipString.setTextColor(ActivityCompat.getColor(getActivity(), R.color.color_666666));
            }
            if (choosePosition.get() >= viewDetailInfo.getLevel()) {
                GlideHelper.displayImageEmpty(getActivity(), viewDetailInfo.getchooseImage(),
                        (ImageView) vipStringView.findViewById(R.id.id_main_iv));
            } else {
                GlideHelper.displayImageEmpty(getActivity(), viewDetailInfo.getNoChooseImage(),
                        (ImageView) vipStringView.findViewById(R.id.id_main_iv));
            }
            return vipStringView;
        }


    }


    private static class VipDelegateInfo {

        private String chooseVipImage;
        private int viplevel;
        private String noChooseVipImage;
        private List<ViewDetailInfo> vipDetails = new ArrayList<>();

        public String getChooseVipImage() {
            return chooseVipImage;
        }

        public int getViplevel() {
            return viplevel;
        }

        public String getNoChooseVipImage() {
            return noChooseVipImage;
        }

        public List<ViewDetailInfo> getVipDetails() {
            return vipDetails;
        }
    }

    private static class ViewDetailInfo {

        private String string;
        private String chooseImage;
        private String noChooseImage;
        private int level;

        public int getLevel() {
            return level;
        }

        public String getString() {
            return string;
        }

        public String getchooseImage() {
            return chooseImage;
        }

        public String getNoChooseImage() {
            return noChooseImage;
        }
    }

    /**
     * 跳转：支付方式选择
     */
    private void startPaymentWayActivity(float money,int entry,int level,int key) {
        ActivityManager.startPaymentWayActivity(getActivity(),money,0,entry, MallModel.USE_RECHARGE_VIP,level,key);
    }
}
