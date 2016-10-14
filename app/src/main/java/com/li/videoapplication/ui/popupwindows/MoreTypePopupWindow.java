package com.li.videoapplication.ui.popupwindows;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.li.videoapplication.R;
import com.li.videoapplication.tools.UmengAnalyticsHelper;
import com.li.videoapplication.ui.ActivityManeger;
import com.li.videoapplication.utils.ScreenUtil;

/**
 * 弹框：游戏类型
 */
public class MoreTypePopupWindow extends PopupWindow implements OnClickListener {

    private View conentView;
    private LinearLayout game, life;
    private Activity activity;
    /**
     * 是否隐藏泡泡窗口生活状态栏
     */
    private boolean isHideLift;
    private LayoutInflater inflater;

    private ImageView typeArrow;

    /**
     * 显示向右箭头
     */
    private void turnArrowRight() {
        if (typeArrow != null)
            typeArrow.setImageResource(R.drawable.moretype_triangle_right);
    }

    /**
     * 显示向下箭头
     */
    private void turnArrowDown() {
        if (typeArrow != null)
            typeArrow.setImageResource(R.drawable.moretype_triangle_down);
    }

    @SuppressWarnings("deprecation")
    public MoreTypePopupWindow(final Activity activity, final ImageView typeArrow, boolean isHideLift) {
        this.activity = activity;
        this.typeArrow = typeArrow;
        this.isHideLift = isHideLift;

        inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        conentView = inflater.inflate(R.layout.popup_moretype, null);
        game = (LinearLayout) conentView.findViewById(R.id.moretype_game);
        life = (LinearLayout) conentView.findViewById(R.id.moretype_life);
        int authorH = 0;
        if (this.isHideLift) {
            life.setVisibility(View.GONE);
            authorH = ScreenUtil.dp2px(40);
        } else {
            life.setVisibility(View.VISIBLE);
            authorH = ScreenUtil.dp2px(80);
        }
        int h = activity.getWindowManager().getDefaultDisplay().getHeight();
        int w = activity.getWindowManager().getDefaultDisplay().getWidth();

        // 设置SelectPicPopupWindow的View
        this.setContentView(conentView);
        // 设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(w);
        // 设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(authorH);
        // 设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        this.setOutsideTouchable(true);
        // 刷新状态
        this.update();
        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0000000000);
        this.setBackgroundDrawable(dw);
        // 消失监听
        this.setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss() {
                turnArrowRight();
            }
        });
        this.setAnimationStyle(R.style.scaleTopAnim);

        game.setOnClickListener(this);
        life.setOnClickListener(this);
    }

    /**
     * 显示窗口
     */
    public void showPopupWindow(View anchor) {
        if (!isShowing()) {
            showAsDropDown(anchor);
            turnArrowDown();
            setBackgroundAlpha(0.8f);
        } else {
            dismiss();
        }
    }

    @Override
    public void dismiss() {
        super.dismiss();
        setBackgroundAlpha(1f);
    }

    @Override
    public void onClick(View v) {
        if (v == game) {// 手机游戏
            ActivityManeger.startSearchGameActivity(activity);
            UmengAnalyticsHelper.onEvent(activity, UmengAnalyticsHelper.MACROSCOPIC_DATA, "手机游戏");
            this.dismiss();
        } else if (v == life) {// 精彩生活
            ActivityManeger.startSearchLifeActivity(activity);
            UmengAnalyticsHelper.onEvent(activity, UmengAnalyticsHelper.MACROSCOPIC_DATA, "精彩生活");
            this.dismiss();
        }
        // 其他区域
        this.dismiss();
    }

    /**
     * 设置添加屏幕的背景透明度
     */
    public void setBackgroundAlpha(float alpha) {
        Window window = activity.getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        params.alpha = alpha;
        window.setAttributes(params);
    }
}