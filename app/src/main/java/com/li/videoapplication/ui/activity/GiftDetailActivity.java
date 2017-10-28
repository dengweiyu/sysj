
package com.li.videoapplication.ui.activity;

import android.content.ClipboardManager;
import android.content.Context;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.li.videoapplication.R;
import com.li.videoapplication.data.DataManager;
import com.li.videoapplication.data.model.entity.Gift;
import com.li.videoapplication.data.model.response.ClaimPackageEntity;
import com.li.videoapplication.data.model.response.PackageInfo203Entity;
import com.li.videoapplication.framework.TBaseActivity;
import com.li.videoapplication.tools.TimeHelper;
import com.li.videoapplication.tools.UmengAnalyticsHelper;
import com.li.videoapplication.ui.ActivityManager;
import com.li.videoapplication.utils.StringUtil;
import com.li.videoapplication.utils.TextUtil;
import com.umeng.analytics.AnalyticsConfig;

/**
 * 活动：礼包详情
 */
public class GiftDetailActivity extends TBaseActivity implements OnClickListener {

    private Gift item;
    private String id;

    @Override
    public void refreshIntent() {
        super.refreshIntent();

        id = getIntent().getStringExtra("id");
        if (StringUtil.isNull(id)) {
            finish();
        }
    }

    private ImageView pic;
    private TextView title, remaining, valid, content, usage;
    private TextView receive, copy, code;
    private ImageView ios, android;

    @Override
    public int getContentView() {
        return R.layout.activity_giftdetail;
    }

    public int inflateActionBar() {
        return R.layout.actionbar_second;
    }

    @Override
    public void afterOnCreate() {
        super.afterOnCreate();

        setSystemBarBackgroundWhite();
        setAbTitle(R.string.giftdetail_title);
    }

    @Override
    public void initView() {
        super.initView();

        initContentView();
    }

    @Override
    public void loadData() {
        super.loadData();

        // 礼包详情
        DataManager.packageInfo203(getMember_id(), id);
    }

    private void initContentView() {

        View download = findViewById(R.id.tb_iv_download_blue);

        pic = (ImageView) findViewById(R.id.giftdetail_pic);
        title = (TextView) findViewById(R.id.giftdetail_title);
        remaining = (TextView) findViewById(R.id.giftdetail_remaining);
        valid = (TextView) findViewById(R.id.giftdetail_valid);
        content = (TextView) findViewById(R.id.giftdetail_content);
        usage = (TextView) findViewById(R.id.giftdetail_usage);

        receive = (TextView) findViewById(R.id.giftdetail_receive);

        copy = (TextView) findViewById(R.id.giftdetail_copy);
        code = (TextView) findViewById(R.id.giftdetail_code);

        ios = (ImageView) findViewById(R.id.giftdetail_ios);
        android = (ImageView) findViewById(R.id.giftdetail_android);

        usage.setAutoLinkMask(Linkify.ALL);
        usage.setMovementMethod(LinkMovementMethod.getInstance());

        receive.setOnClickListener(this);
        copy.setOnClickListener(this);

        receive.setVisibility(View.GONE);
        copy.setVisibility(View.GONE);
        code.setVisibility(View.GONE);

        download.setOnClickListener(this);

        if (isNeedHideDownload(this)){
            download.setVisibility(View.GONE);
        }else {
            download.setVisibility(View.VISIBLE);
        }

    }


    /**
     *
     */
    private boolean isNeedHideDownload(Context context){
        String channel = AnalyticsConfig.getChannel(context);
        if ("huawei".equals(channel)|| "xiaomi".equalsIgnoreCase(channel) || "anzhi".equalsIgnoreCase(channel)){
            return true;
        }
        return false;
    }

    private void refreshContentView(Gift item) {
        if (item != null) {
            setImageViewImageNet(pic, item.getFlag());
            setTextViewText(title, item.getTitle());
            setRemaining(remaining, item);
            setValid(valid, item);
            setTextViewText(content, item.getContent());
            setTextViewText(usage, item.getTrade_type());
            setPlatform(ios, android, item);
        }
    }

    private void refreshButton(Gift item) {
        code.setText("");
        if (!StringUtil.isNull(item.getActivity_code())) {// 复制激活码
            code.setText(item.getActivity_code());
            copy.setVisibility(View.VISIBLE);
            code.setVisibility(View.VISIBLE);
            receive.setVisibility(View.GONE);
        } else {// 领取礼包
            receive.setVisibility(View.VISIBLE);
            if (item.getCount() != null && item.getCount().equals("0")) {
                receive.setText(R.string.giftdetail_receive_gray);
                receive.setBackgroundResource(R.drawable.giftdetail_receive_gray);
            } else {
                receive.setText(R.string.giftdetail_receive);
                receive.setBackgroundResource(R.drawable.giftdetail_receive);
            }
        }
    }

    /**
     * 剩余数量
     */
    private void setRemaining(TextView view, Gift item) {
        // 剩余数量：26%
        if (item != null) {
            int max = Integer.parseInt(item.getNum());
            int progress = Integer.parseInt(item.getCount());
            String s = (int) (((float) progress / max) * 100) + "%";
            view.setText(Html.fromHtml("剩余数量：" + TextUtil.toColor(s, "#2c93f7")));
        }
    }

    /**
     * 有效期
     */
    private void setValid(TextView view, Gift item) {
        // 有效期：2015-12-06 至 2016-01-10
        if (item != null) {
            try {
                view.setText("有效期：" + TimeHelper.getSysMessageTime(item.getStarttime()) +
                        "至" + TimeHelper.getSysMessageTime(item.getEndtime()));
            } catch (Exception e) {
                e.printStackTrace();
                view.setText("");
            }
        }
    }

    /**
     * 适用平台
     */
    private void setPlatform(ImageView ios, ImageView android, Gift item) {
        if (item != null && android != null && ios != null) {
            if (item.getScope().equals(Gift.SCOPE_IOS)) {
                ios.setImageResource(R.drawable.gift_ios_green);
                android.setImageResource(R.drawable.gift_android_gray);
            } else if (item.getScope().equals(Gift.SCOPE_ANDROID)) {
                ios.setImageResource(R.drawable.gift_ios_gray);
                android.setImageResource(R.drawable.gift_android_green);
            } else if (item.getScope().equals(Gift.SCOPE_BOTH)) {
                ios.setImageResource(R.drawable.gift_ios_green);
                android.setImageResource(R.drawable.gift_android_green);
            } else {
                ios.setImageResource(R.drawable.gift_ios_gray);
                android.setImageResource(R.drawable.gift_android_gray);
            }
        }
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.giftdetail_receive:// 领取
                if (!isLogin()) {
                    showToastLogin();
                    return;
                }
                // 领取礼包
                DataManager.claimPackage(getMember_id(), item.getId());
                break;

            case R.id.giftdetail_copy:// 复制激活码
                ClipboardManager cmb = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                cmb.setText(code.getText().toString());
                showToastShort("已复制到剪贴板");
                break;
            case R.id.tb_iv_download_blue:
                UmengAnalyticsHelper.onEvent(this, UmengAnalyticsHelper.DISCOVER, "礼包详情-游戏下载");
                if (item == null || StringUtil.isNull(item.getGame_id())){
                    showToastShort("暂无此游戏哦~");
                    return;
                }
                ActivityManager.startDownloadManagerActivity(this,item.getGame_id(),"6",item.getGame_id());
                break;
        }
    }

    /**
     * 回调：礼包详情
     */
    public void onEventMainThread(PackageInfo203Entity event) {

        if (event != null) {
            if (event.isResult()) {
                if (event.getData() != null) {
                    item = event.getData();
                    refreshContentView(item);
                    refreshButton(item);
                }
            }
        }
    }

    /**
     * 回调：领取礼包
     */
    public void onEventMainThread(ClaimPackageEntity event) {

        if (event.isResult()) {
            showToastShort("领取礼包成功");
            // 礼包详情
            DataManager.packageInfo203(getMember_id(), item.getId());
            UmengAnalyticsHelper.onEvent(this, UmengAnalyticsHelper.DISCOVER, "热门礼包-领取成功");
        } else {
            showToastShort(event.getMsg());
        }
    }
}
