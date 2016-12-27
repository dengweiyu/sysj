package com.li.videoapplication.ui.dialog;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.li.videoapplication.R;
import com.li.videoapplication.data.database.VideoCaptureEntity;
import com.li.videoapplication.data.image.VideoCover;
import com.li.videoapplication.data.local.SYSJStorageUtil;
import com.li.videoapplication.data.model.response.RecommendedLocationEntity;
import com.li.videoapplication.framework.BaseDialog;
import com.li.videoapplication.ui.ActivityManeger;
import com.li.videoapplication.ui.activity.VideoShareActivity210;
import com.li.videoapplication.utils.BitmapUtil;
import com.li.videoapplication.utils.InputUtil;

/**
 * 弹框：申请官方推荐支付
 */
@SuppressLint("CutPasteId")
public class OfficialPaymentDialog extends BaseDialog implements View.OnClickListener {

    private VideoShareActivity210 activity;
    private VideoCaptureEntity entity;
    private ImageView pic;
    private TextView title, currency, price, confirm;
    private RecommendedLocationEntity event;
    private Bitmap bitmap;
    private boolean haveEnoughCurrency;

    public OfficialPaymentDialog(Context context, VideoCaptureEntity entity, RecommendedLocationEntity event) {
        super(context);
        this.entity = entity;
        this.event = event;

        if (entity == null) dismiss();

        try {
            activity = (VideoShareActivity210) context;
        } catch (Exception e) {
            e.printStackTrace();
        }

        haveEnoughCurrency = Integer.valueOf(event.getMember_currency()) >=
                Integer.valueOf(event.getGoods().getCurrency_num());

        initView();

        initData();
    }

    @Override
    protected int getContentView() {
        return R.layout.dialog_officialpayment;
    }

    private void initData() {
        try {
            bitmap = BitmapUtil.readLocalBitmapQuarter(SYSJStorageUtil.createCoverPath(entity.getVideo_path()).getPath());
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (bitmap == null) {
            try {
                bitmap = VideoCover.generateBitmap(entity.getVideo_path());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (bitmap != null)
            pic.setImageBitmap(bitmap);

        setTextViewText(title, entity.getVideo_name());
        setTextViewText(price, event.getGoods().getCurrency_num());

        if (haveEnoughCurrency) {
            currency.setTextColor(getContext().getResources().getColor(R.color.title_bg_color));
            setTextViewText(currency, "余额：" + event.getMember_currency());
            confirm.setBackgroundResource(R.drawable.button_blue);
            setTextViewText(confirm, "确认支付");
        } else {
            currency.setTextColor(getContext().getResources().getColor(R.color.currency_red));
            setTextViewText(currency, "余额不足：" + event.getMember_currency());
            confirm.setBackgroundResource(R.drawable.button_gray);
            setTextViewText(confirm, "返回");
        }
    }

    private void initView() {
        pic = (ImageView) findViewById(R.id.official_pic);
        title = (TextView) findViewById(R.id.official_title);
        currency = (TextView) findViewById(R.id.official_currency);
        price = (TextView) findViewById(R.id.official_price);
        confirm = (TextView) findViewById(R.id.official_confirm);

        findViewById(R.id.official_detail).setOnClickListener(this);
        confirm.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.official_confirm:

                dismiss();
                if (haveEnoughCurrency) {
                    ActivityManeger.startShareActivity4MyLocalVideo(activity);
                } else {
                    activity.apply.setChecked(false);
                }
                break;

            case R.id.official_detail:
                if (event != null)
                    ActivityManeger.startProductsDetailActivity(getContext(), event.getGoods().getId());
                break;
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
        BitmapUtil.recycleBitmap(bitmap);
    }
}
