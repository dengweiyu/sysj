package com.li.videoapplication.ui.dialog;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.UpdateAppearance;
import android.view.Display;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.li.videoapplication.R;
import com.li.videoapplication.data.model.response.Vip3AndAuthoryEntity;
import com.li.videoapplication.framework.BaseDialog;
import com.li.videoapplication.mvp.Constant;
import com.li.videoapplication.mvp.mall.model.MallModel;
import com.li.videoapplication.tools.UmengAnalyticsHelper2;
import com.li.videoapplication.ui.ActivityManager;
import com.li.videoapplication.utils.ScreenUtil;
import com.li.videoapplication.utils.TextUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cx on 2018/1/24.
 */

public class Vip3ShowDialog extends BaseDialog implements OnClickListener, Vip3ShowDialog.INotifyChange {

    private RecyclerView id_vip_recyclerView;
    private TextView id_one_month_btn;
    private TextView id_three_month_btn;
    private ImageView id_three_month_iv;
    private TextView id_six_month_btn;
    private ImageView id_six_month_iv;
    private TextView id_twelve_month_btn;
    private ImageView id_twelve_month_iv;
    private TextView pay_text;
    private TextView sale_pay_text;
    private View recharge_vip_pay_now;

    private Drawable defualtDrawable;
    private Drawable selectDrawable;

    private int month = 1;
    private int money;
    private float moneyRate;
    private Vip3AndAuthoryEntity vip3AndAuthoryEntity;

    private final List<Vip3AndAuthoryEntity.DiamondVipDataBean.DiamondVipTextBean> diamondVipTextBeans = new ArrayList<>();

    public Vip3ShowDialog(Context context, Vip3AndAuthoryEntity vip3AndAuthoryEntity) {
        super(context);
        this.vip3AndAuthoryEntity = vip3AndAuthoryEntity;
        defualtDrawable = ActivityCompat.getDrawable(context, R.drawable.shape_gray);
        selectDrawable = ActivityCompat.getDrawable(context, R.drawable.shape_main_bule);
//        notifyChange();
    }

    @Override
    protected void afterContentView(Context context) {

        Window window = getWindow();
        WindowManager manager = ((Activity) context).getWindowManager();
        Display display = manager.getDefaultDisplay();
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = (int) (display.getWidth() * 0.9f);
        window.setAttributes(params);


        pay_text = (TextView) findViewById(R.id.pay_text);
        sale_pay_text = (TextView) findViewById(R.id.sale_pay_text);
        id_vip_recyclerView = (RecyclerView) findViewById(R.id.id_vip_recyclerView);
        id_vip_recyclerView.setItemAnimator(new DefaultItemAnimator());
        id_vip_recyclerView.setLayoutManager(new LinearLayoutManager(super.context));

        id_one_month_btn = (TextView) findViewById(R.id.id_one_month_btn);
        id_three_month_btn = (TextView) findViewById(R.id.id_three_month_btn);
        id_six_month_btn = (TextView) findViewById(R.id.id_six_month_btn);
        id_twelve_month_btn = (TextView) findViewById(R.id.id_twelve_month_btn);
        id_three_month_iv = (ImageView) findViewById(R.id.id_three_month_iv);
        id_six_month_iv = (ImageView) findViewById(R.id.id_six_month_iv);
        id_twelve_month_iv = (ImageView) findViewById(R.id.id_twelve_month_iv);
        recharge_vip_pay_now = findViewById(R.id.recharge_vip_pay_now);
        id_one_month_btn.setOnClickListener(this);
        id_three_month_btn.setOnClickListener(this);
        id_six_month_btn.setOnClickListener(this);
        id_twelve_month_btn.setOnClickListener(this);
        recharge_vip_pay_now.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                UmengAnalyticsHelper2.onEvent(UmengAnalyticsHelper2.FEN_XIANG_ID,
                        UmengAnalyticsHelper2.SHARE_PRIVILEGE_PAY_TYPE);
                ActivityManager.startPaymentWayActivity(v.getContext(),
                        moneyRate, 0, Constant.TOPUP_ENTRY_VIDEO_MANAGER, MallModel.USE_RECHARGE_VIP,
                        3, month
                );
            }
        });


    }

    @Override
    public void show() {
        super.show();
        handlerData();
        if (id_vip_recyclerView.getAdapter() == null) {
            id_vip_recyclerView.setAdapter(new BaseQuickAdapter<Vip3AndAuthoryEntity.
                    DiamondVipDataBean.DiamondVipTextBean, BaseViewHolder>(R.layout.adapter_vip3_tip, diamondVipTextBeans) {
                @Override
                protected void convert(BaseViewHolder baseViewHolder,
                                       Vip3AndAuthoryEntity.DiamondVipDataBean.DiamondVipTextBean diamondVipTextBean) {

                    StringBuilder stringBuilder = new StringBuilder();
                    List<TextEntity> updateAppearances = new ArrayList<>();

                    if (diamondVipTextBean.getMainText() != null) {

                        stringBuilder.append(diamondVipTextBean.getMainText().getTitle());
                        TextEntity mTextEntity = new TextEntity();
                        mTextEntity.appearance = new ForegroundColorSpan(Color.parseColor(diamondVipTextBean.getMainText().getColor()));
                        mTextEntity.start = 0;
                        mTextEntity.end = stringBuilder.length();
                        updateAppearances.add(mTextEntity);

                        mTextEntity = new TextEntity();
                        mTextEntity.appearance = new AbsoluteSizeSpan(
                                ScreenUtil.dp2px(Float.parseFloat(diamondVipTextBean.getMainText().getFontSize())));
                        mTextEntity.start = 0;
                        mTextEntity.end = stringBuilder.length();
                        updateAppearances.add(mTextEntity);

                    }
                    if (diamondVipTextBean.getSecondText() != null) {
                        int leng = stringBuilder.length();
                        stringBuilder.append(diamondVipTextBean.getSecondText().getTitle());
                        TextEntity mTextEntity = new TextEntity();
                        mTextEntity.appearance = new ForegroundColorSpan(Color.parseColor(diamondVipTextBean.getSecondText().getColor()));
                        mTextEntity.start = leng;
                        mTextEntity.end = stringBuilder.length();
                        updateAppearances.add(mTextEntity);

                        mTextEntity = new TextEntity();
                        mTextEntity.appearance = new AbsoluteSizeSpan(ScreenUtil.dp2px(diamondVipTextBean.getSecondText().getFontSize()));
                        mTextEntity.start = leng;
                        mTextEntity.end = stringBuilder.length();
                        updateAppearances.add(mTextEntity);

                    }
                    if (diamondVipTextBean.getThirdText() != null) {
                        int leng = stringBuilder.length();
                        stringBuilder.append(diamondVipTextBean.getThirdText().getTitle());
                        TextEntity mTextEntity = new TextEntity();
                        mTextEntity.appearance = new ForegroundColorSpan(Color.parseColor(diamondVipTextBean.getThirdText().getColor()));
                        mTextEntity.start = leng;
                        mTextEntity.end = stringBuilder.length();
                        updateAppearances.add(mTextEntity);

                        mTextEntity = new TextEntity();
                        mTextEntity.appearance = new AbsoluteSizeSpan(ScreenUtil.dp2px(diamondVipTextBean.getThirdText().getFontSize()));
                        mTextEntity.start = leng;
                        mTextEntity.end = stringBuilder.length();
                        updateAppearances.add(mTextEntity);
                    }
                    Spannable spannable = new SpannableString(stringBuilder.toString());
                    for (TextEntity textEntity : updateAppearances) {
                        spannable.setSpan(textEntity.appearance, textEntity.start, textEntity.end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    }

                    baseViewHolder.setText(R.id.id_text, spannable);
                }
            });
            id_one_month_btn.post(new Runnable() {
                @Override
                public void run() {
                    id_one_month_btn.performClick();
                }
            });
        }
    }

    @Override
    protected int getContentView() {
        return R.layout.dialog_vip3_tip;
    }


    public void notifyChange() {
        handlerData();
    }

    @Override
    public void onClick(View v) {
        id_one_month_btn.setBackgroundResource(R.drawable.shape_gray);
        id_three_month_btn.setBackgroundResource(R.drawable.shape_gray);
        id_six_month_btn.setBackgroundResource(R.drawable.shape_gray);
        id_twelve_month_btn.setBackgroundResource(R.drawable.shape_gray);
        id_one_month_btn.setTextColor(ActivityCompat.getColor(v.getContext(), R.color.color_999999));
        id_three_month_btn.setTextColor(ActivityCompat.getColor(v.getContext(), R.color.color_999999));
        id_six_month_btn.setTextColor(ActivityCompat.getColor(v.getContext(), R.color.color_999999));
        id_twelve_month_btn.setTextColor(ActivityCompat.getColor(v.getContext(), R.color.color_999999));
        id_three_month_iv.setImageResource(R.drawable.ic_sale_vip3_no_choose);
        id_six_month_iv.setImageResource(R.drawable.ic_sale_vip3_no_choose);
        id_twelve_month_iv.setImageResource(R.drawable.ic_sale_vip3_no_choose);
        switch (v.getId()) {
            case R.id.id_one_month_btn:
                month = 1;
                id_one_month_btn.setBackground(selectDrawable);
                id_one_month_btn.setTextColor(ActivityCompat.getColor(v.getContext(), R.color.menu_help_blue_1));
                sendToMoneyChange();
                break;
            case R.id.id_three_month_btn:
                month = 3;
                id_three_month_btn.setBackground(selectDrawable);
                id_three_month_btn.setTextColor(ActivityCompat.getColor(v.getContext(), R.color.menu_help_blue_1));
                id_three_month_iv.setImageResource(R.drawable.ic_sale_vip3_choose);
                sendToMoneyChange();
                UmengAnalyticsHelper2.onEvent(UmengAnalyticsHelper2.FEN_XIANG_ID, UmengAnalyticsHelper2.SHARE_THREE_MONTH_PRIVILEGE_TYPE);
                break;
            case R.id.id_six_month_btn:
                month = 6;
                id_six_month_btn.setBackground(selectDrawable);
                id_six_month_btn.setTextColor(ActivityCompat.getColor(v.getContext(), R.color.menu_help_blue_1));
                id_six_month_iv.setImageResource(R.drawable.ic_sale_vip3_choose);
                sendToMoneyChange();
                UmengAnalyticsHelper2.onEvent(UmengAnalyticsHelper2.FEN_XIANG_ID, UmengAnalyticsHelper2.SHARE_SIX_MONTH_PRIVILEGE_TYPE);
                break;
            case R.id.id_twelve_month_btn:
                month = 12;
                id_twelve_month_btn.setBackground(selectDrawable);
                id_twelve_month_btn.setTextColor(ActivityCompat.getColor(v.getContext(), R.color.menu_help_blue_1));
                id_twelve_month_iv.setImageResource(R.drawable.ic_sale_vip3_choose);
                sendToMoneyChange();
                UmengAnalyticsHelper2.onEvent(UmengAnalyticsHelper2.FEN_XIANG_ID, UmengAnalyticsHelper2.SHARE_TWELVE_MONTH_PRIVILEGE_TYPE);
                break;
        }

    }

    public void handlerData() {
        id_one_month_btn.setText(vip3AndAuthoryEntity.getDiamondVipData().getPackageMemu().get(0).getText());
        id_three_month_btn.setText(vip3AndAuthoryEntity.getDiamondVipData().getPackageMemu().get(1).getText());
        id_six_month_btn.setText(vip3AndAuthoryEntity.getDiamondVipData().getPackageMemu().get(2).getText());
        id_twelve_month_btn.setText(vip3AndAuthoryEntity.getDiamondVipData().getPackageMemu().get(3).getText());
        id_one_month_btn.setEnabled(true);
        id_one_month_btn.setEnabled(true);
        id_one_month_btn.setEnabled(true);
        id_one_month_btn.setEnabled(true);

        diamondVipTextBeans.clear();
        diamondVipTextBeans.addAll(vip3AndAuthoryEntity.getDiamondVipData().getDiamondVipText());
        if (id_vip_recyclerView.getAdapter() != null) {
            id_vip_recyclerView.getAdapter().notifyDataSetChanged();
        }

    }

    private void sendToMoneyChange() {
        switch (month) {
            case 1:
                money = vip3AndAuthoryEntity.getDiamondVipData().getDiamondVipPrice();
                moneyRate = money * vip3AndAuthoryEntity.getDiamondVipData().getPackageMemu().get(0).getDiscount();
                break;
            case 3:
                money = vip3AndAuthoryEntity.getDiamondVipData().getDiamondVipPrice() * 3;
                moneyRate = money * vip3AndAuthoryEntity.getDiamondVipData().getPackageMemu().get(1).getDiscount();
                break;
            case 6:
                money = vip3AndAuthoryEntity.getDiamondVipData().getDiamondVipPrice() * 6;
                moneyRate = money * vip3AndAuthoryEntity.getDiamondVipData().getPackageMemu().get(2).getDiscount();
                break;
            case 12:
                money = vip3AndAuthoryEntity.getDiamondVipData().getDiamondVipPrice() * 12;
                moneyRate = money * vip3AndAuthoryEntity.getDiamondVipData().getPackageMemu().get(3).getDiscount();
                break;
        }
        pay_text.setText(Html.fromHtml("支付：<font color=#ff3d2e>" + moneyRate * 1.0f + "</font> 元"));
        if (money == moneyRate) {
            sale_pay_text.setVisibility(View.INVISIBLE);
        } else {
            sale_pay_text.setVisibility(View.VISIBLE);
        }
        final String deleteStr = "原价：" + money * 1.0f;
        sale_pay_text.setText(TextUtil.setStrikethroughSpan(
                new SpannableString(deleteStr), 0, deleteStr.length()));
    }

    private static final class TextEntity {

        UpdateAppearance appearance;
        int start;
        int end;

    }


    public interface INotifyChange {

        void notifyChange();

    }

}
