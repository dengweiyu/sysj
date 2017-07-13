package com.ifeimo.im.framwork.view;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import com.ifeimo.im.R;
import com.ifeimo.im.common.adapter.base.CommonAdapter;
import com.ifeimo.im.common.adapter.base.ViewHolder;
import com.ifeimo.im.common.util.ScreenUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by lpds on 2017/7/12.
 */
public class FashReplyListView extends ListView {
    private List<String> datas = new ArrayList<>();
    private CommonAdapter<String> adapter;
    private EditText editText;
    private List<OnItemClickListener> itemListeners = new ArrayList<>();
    private boolean isAnim = true;
    private int dHeight;
    private OnItemClickListener onItemClickListener = new OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
            if (editText != null) {
                editText.setText(adapter.getItem(position));
            }
            for (OnItemClickListener onItemClickListener : itemListeners) {
                onItemClickListener.onItemClick(adapterView, view, position, id);
            }
        }
    };

    public FashReplyListView(Context context) {
        super(context);
        init();
    }

    public FashReplyListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public FashReplyListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        valueAnimator.setDuration(300).setFloatValues(0, 1);
        datas.addAll(Arrays.asList(getContext().getResources().getStringArray(R.array.fast_reply_string)));
        mesureChildHeight();
        adapter = new CommonAdapter<String>(getContext(), R.layout.item_fast_reply_layout, datas) {
            @Override
            protected void convert(ViewHolder viewHolder, String item, int position) {
                viewHolder.setText(R.id.id_fast_reply_tv, item);
            }
        };
        setAdapter(adapter);
        setOnItemClickListener(onItemClickListener);
    }

    private void mesureChildHeight() {
        View v = LayoutInflater.from(getContext()).inflate(R.layout.item_fast_reply_layout, null);

        ViewGroup.LayoutParams lp = v.getLayoutParams();
        if (lp == null) {
            lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            int height = MeasureSpec.makeMeasureSpec(lp.height, MeasureSpec.UNSPECIFIED);
            int width = MeasureSpec.makeMeasureSpec(lp.width, MeasureSpec.UNSPECIFIED);
            v.measure(width, height);
            int w = v.getMeasuredWidth();
            int h = v.getMeasuredHeight();
            Log.i("ViewGroup", "mesureChildHeight: " + w + " " + h);
            dHeight = h * (datas.size() > 4 ? 4 : datas.size())+2;
        }
    }

    @Override
    public void setOnItemClickListener(OnItemClickListener listener) {
        if (onItemClickListener != listener) {
            itemListeners.add(listener);
        } else {
            super.setOnItemClickListener(onItemClickListener);
        }
    }

    public void setEditText(EditText editText) {
        this.editText = editText;
    }

    ValueAnimator valueAnimator = new ValueAnimator();

    @Override
    public void setVisibility(int visibility) {
        if (isAnim) {
            if (visibility == GONE) {
                valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        Float aFloat = (Float) valueAnimator.getAnimatedValue();
                        aFloat = dHeight * (1 - aFloat);
                        getLayoutParams().height = (int) +aFloat;
                        requestLayout();
                    }
                });
                valueAnimator.start();
            } else {
                valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator valueAnimator) {
                        Float aFloat = (Float) valueAnimator.getAnimatedValue();
                        aFloat = dHeight * aFloat;
                        getLayoutParams().height = (int) +aFloat;
                        requestLayout();
                    }
                });
                valueAnimator.start();
            }
        } else {
            super.setVisibility(visibility);
        }
    }

}
