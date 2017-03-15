package com.li.videoapplication.views;

import android.content.Context;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.EditText;

/**
 * 限制输入数字的范围的EditText
 */
public class RegionNumberEditText extends EditText {

    private Context context;
    private int max;
    private int min;

    public RegionNumberEditText(Context context) {
        super(context);
        this.context = context;
    }

    public RegionNumberEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    public RegionNumberEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
    }

    /**
     * 设置输入数字的范围
     *
     * @param maxNum 最大数
     * @param minNum 最小数
     */
    public void setRegion(int minNum, int maxNum) {
        setInputType(InputType.TYPE_CLASS_NUMBER);
        this.max = maxNum;
        this.min = minNum;
    }

    public void setTextWatcher(final IRegionNumWatcher watcher) {
        addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (min != -1 && max != -1) {
                    try {
                        int num = Integer.parseInt(s.toString());
                        //判断当前edittext中的数字(可能一开始Edittext中有数字)是否大于max
                        if (num > max) {
//                                s = String.valueOf(max);//如果大于max，则内容为max
//                                setText(s);
                            watcher.overMax();

                        } else if (num < min) {
//                                s = String.valueOf(min);//如果小于min,则内容为min
//                                setText(s);
                            watcher.lowerMin();
                        }
                    } catch (NumberFormatException e) {
                        Log.e("ontextchanged", "==" + e.toString());
                    }
                    //edittext中的数字在max和min之间，则不做处理，正常显示即可。
                }
            }
        });
    }

    public interface IRegionNumWatcher {
        void overMax();

        void lowerMin();
    }
}
