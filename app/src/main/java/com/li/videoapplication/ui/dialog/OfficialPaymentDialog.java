package com.li.videoapplication.ui.dialog;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.li.videoapplication.R;
import com.li.videoapplication.data.DataManager;
import com.li.videoapplication.data.model.entity.Recommend;
import com.li.videoapplication.data.model.response.BaseInfo4OfficialDialogEntity;
import com.li.videoapplication.data.model.response.RecommendedLocationEntity;
import com.li.videoapplication.data.preferences.PreferencesHepler;
import com.li.videoapplication.framework.BaseDialog;
import com.li.videoapplication.framework.TBaseActivity;
import com.li.videoapplication.mvp.Constant;
import com.li.videoapplication.mvp.adapter.OfficialPaymentAdapter;
import com.li.videoapplication.tools.ToastHelper;
import com.li.videoapplication.ui.ActivityManager;
import com.li.videoapplication.ui.activity.VideoMangerActivity;
import com.li.videoapplication.ui.activity.VideoShareActivity;
import com.li.videoapplication.utils.InputUtil;
import com.li.videoapplication.utils.StringUtil;

import io.rong.eventbus.EventBus;

/**
 * 弹框：申请官方推荐支付
 */
@SuppressLint("CutPasteId")
public class OfficialPaymentDialog extends BaseDialog implements View.OnClickListener {

    private final String video_id;
    private TBaseActivity activity;
    private TextView currency, price, confirm, topUpNow;
    private RecommendedLocationEntity event;
    private boolean haveEnoughCurrency;
    private RecyclerView recyclerView;
    private OfficialPaymentAdapter adapter;
    private SparseBooleanArray mCheckStates = new SparseBooleanArray();

    /**
     * 跳转：充值
     */
    private void startTopUpActivity() {
        ActivityManager.startTopUpActivity(getContext(),Constant.TOPUP_ENTRY_RECOMMEND,0);
    }

    public OfficialPaymentDialog(Context context, RecommendedLocationEntity event) {
        this(context, event, "");
    }

    public OfficialPaymentDialog(Context context, RecommendedLocationEntity event, String video_id) {
        super(context);
        this.event = event;
        this.video_id = video_id;
        try {
            activity = (TBaseActivity) context;
        } catch (Exception e) {
            e.printStackTrace();
        }

        haveEnoughCurrency = Integer.valueOf(event.getMember_currency()) >=
                Integer.valueOf(event.getGoods().get(0).getCurrency_num());

        initView();
        initAdapter();
        addOnClickListener();
    }

    @Override
    protected int getContentView() {
        return R.layout.dialog_officialpayment;
    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        EventBus.getDefault().unregister(this);
    }

    private void initAdapter() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setItemAnimator(null);
        for (int i = 0; i < event.getGoods().size(); i++) {
            if (i == 0) {
                mCheckStates.put(i, true);
            } else {
                mCheckStates.put(i, false);
            }
        }
        adapter = new OfficialPaymentAdapter(event.getGoods(), mCheckStates);
        recyclerView.setAdapter(adapter);

        setTextViewText(price, StringUtil.formatNum(event.getGoods().get(0).getCurrency_num()));
        toogleCurrencyView();
    }

    private void initView() {
        currency = (TextView) findViewById(R.id.official_currency);
        price = (TextView) findViewById(R.id.official_price);
        confirm = (TextView) findViewById(R.id.official_confirm);
        topUpNow = (TextView) findViewById(R.id.official_topup);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        findViewById(R.id.official_detail).setOnClickListener(this);
        confirm.setOnClickListener(this);
        topUpNow.setOnClickListener(this);
    }

    private void refreshCurrencyView(String priceNum) {
        Log.d(tag, "refreshCurrencyView: priceNum = " + priceNum);
        setTextViewText(price, StringUtil.formatNum(priceNum));
        haveEnoughCurrency = Integer.valueOf(event.getMember_currency()) >=
                Integer.valueOf(priceNum);
        toogleCurrencyView();
    }

    private void toogleCurrencyView() {
        if (haveEnoughCurrency) {
            currency.setTextColor(getContext().getResources().getColor(R.color.title_bg_color));
            setTextViewText(currency, "余额：" + StringUtil.formatNum(event.getMember_currency()));
            setTextViewText(confirm, "确认支付");
            topUpNow.setVisibility(View.VISIBLE);
        } else {
            currency.setTextColor(getContext().getResources().getColor(R.color.currency_red));
            setTextViewText(currency, "余额不足：" + StringUtil.formatNum(event.getMember_currency()));
            setTextViewText(confirm, "立即充值");
            topUpNow.setVisibility(View.INVISIBLE);
        }
    }

    private int selectedPos;

    private void addOnClickListener() {
        //recyclerview item点击事件处理
        recyclerView.addOnItemTouchListener(new OnItemClickListener() {

            @Override
            public void SimpleOnItemClick(BaseQuickAdapter adapter, View view, int pos) {
                Recommend record = (Recommend) adapter.getItem(pos);
                if (!mCheckStates.valueAt(pos)) {
                    if (selectedPos != pos) {
                        //先取消上个item的勾选状态
                        mCheckStates.put(selectedPos, false);
                        OfficialPaymentDialog.this.adapter.notifyItemChanged(selectedPos);
                        //设置新Item的勾选状态
                        selectedPos = pos;
                        mCheckStates.put(selectedPos, true);
                        OfficialPaymentDialog.this.adapter.notifyItemChanged(selectedPos);
                        refreshCurrencyView(record.getCurrency_num());

                        if (activity instanceof VideoShareActivity) {
                            VideoShareActivity shareActivity = (VideoShareActivity) activity;
                            shareActivity.setGoods_id(getSelectedItemID());
                        }
                    }
                }
            }
        });
    }

    private String getSelectedItemID() {
        for (int i = 0; i < mCheckStates.size(); i++) {
            if (mCheckStates.valueAt(i)) {
                return event.getGoods().get(i).getId();
            }
        }
        return event.getGoods().get(0).getId();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.official_confirm:
                if (haveEnoughCurrency) {
                    //云端视频申请推荐位
                    if (activity instanceof VideoMangerActivity && !StringUtil.isNull(video_id)) {
                        String mobile = PreferencesHepler.getInstance().getUserProfilePersonalInformation().getMobile();
                        String member_id = PreferencesHepler.getInstance().getMember_id();
                        // 商品兑换
                        DataManager.payment(member_id, getSelectedItemID(), mobile, video_id);
                        dismiss();
                    } else {
                        VideoShareActivity videoShareActivity = (VideoShareActivity) activity;
                        videoShareActivity.setGoods_id(getSelectedItemID());
                        //敏感词过滤
                        DataManager.baseInfo(videoShareActivity.getDescription(), new BaseInfo4OfficialDialogEntity());
                    }
                } else {
                    dismiss();
                    startTopUpActivity();
                }
                break;

            case R.id.official_detail:
                if (event != null)
                    ActivityManager.startProductsDetailActivity(getContext(), event.getDetailId(),
                            Constant.PRODUCTSDETAIL_RICHTEXT_WITHBTN);
                break;
            case R.id.official_topup:
                startTopUpActivity();
                break;
        }
    }

    /**
     * 回调：敏感词过滤
     */
    public void onEventMainThread(BaseInfo4OfficialDialogEntity event) {
        if (event != null && event.isResult()) {
            if (event.getData().isHasBad()) {
                ToastHelper.s("请勿使用敏感词汇");
            } else {
                dismiss();
                VideoShareActivity.isPayed = true;
                ActivityManager.startShareActivity4MyLocalVideo(activity,false);
            }
        }
    }

    @Override
    public void dismiss() {
        try {
            InputUtil.closeKeyboard(activity);
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.dismiss();
    }
}
