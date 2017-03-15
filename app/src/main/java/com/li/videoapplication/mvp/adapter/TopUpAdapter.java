package com.li.videoapplication.mvp.adapter;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.li.videoapplication.R;
import com.li.videoapplication.data.model.entity.TopUp;
import com.li.videoapplication.mvp.mall.view.TopUpActivity;
import com.li.videoapplication.tools.ToastHelper;
import com.li.videoapplication.utils.StringUtil;

import java.util.List;

/**
 * 适配器：充值选项
 */
public class TopUpAdapter extends BaseQuickAdapter<TopUp, BaseViewHolder> {
    private static final String TAG = TopUpAdapter.class.getSimpleName();
    private TopUpActivity activity;
    private int customInt;
    int lastestIndex = -1;

    public TopUpAdapter(List<TopUp> data, TopUpActivity activity) {
        super(R.layout.adapter_topup, data);
        this.activity = activity;
    }

    @Override
    protected void convert(final BaseViewHolder holder, final TopUp item) {

        holder.setText(R.id.topup_option, StringUtil.formatNum(item.getOption()) + " 飞磨豆")
                .setVisible(R.id.topup_option, !isCustomItem(item))
                .setVisible(R.id.topup_custom, isCustomItem(item))
                .setVisible(R.id.topup_check_img, item.isSelected());

        if (item.isSelected()) {
            holder.setBackgroundRes(R.id.topup_check, R.drawable.stroke_red_4px);
        } else {
            holder.setBackgroundRes(R.id.topup_check, R.drawable.stroke_gray_2px);
        }

        int pos = holder.getAdapterPosition();
        EditText ev_custom = holder.getView(R.id.topup_custom);
        if (isCustomItem(item)) {
            addEditTextListener(pos, ev_custom);
        }

        ev_custom.clearFocus();
        if (pos == lastestIndex && getData().get(pos).isSelected()) {
            // 如果该项中的EditText是要获取焦点的
            ev_custom.requestFocus();
        }
    }

    private boolean isCustomItem(TopUp item) {
        return item.getOption().equals("-1");
    }

    private void addEditTextListener(final int pos, final EditText ev_custom) {
        ev_custom.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                Log.d(TAG, "onFocusChange: " + hasFocus);
                if (hasFocus) {
                    ev_custom.addTextChangedListener(textWatch);
                    if (getCustomInt() > 0)
                        activity.setPrice(getData().size() - 1);
                } else {
                    ev_custom.removeTextChangedListener(textWatch);
                }
            }
        });

        // 设置触摸事件（别想着用OnClickListener）
        ev_custom.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    Log.d(TAG, "onTouch: pos = " + pos);
                    lastestIndex = pos;
                }
                return false;
            }
        });
    }

    private TextWatcher textWatch = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            Log.d(TAG, "-------afterTextChanged: --------");
            try {
                setCustomInt(Integer.valueOf(s.toString()));
                activity.setPrice(getData().size() - 1);
            } catch (NumberFormatException e) {
                ToastHelper.s("充值飞磨豆数量不在允许范围");
            }
        }
    };

    public int getCustomInt() {
        return customInt;
    }

    public void setCustomInt(int customInt) {
        Log.d(TAG, "setCustomInt: " + customInt);
        this.customInt = customInt;
    }
}
