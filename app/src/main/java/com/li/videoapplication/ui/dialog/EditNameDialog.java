package com.li.videoapplication.ui.dialog;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.widget.EditText;

import com.li.videoapplication.R;
import com.li.videoapplication.data.DataManager;
import com.li.videoapplication.data.model.response.IsRepeatEntity;
import com.li.videoapplication.framework.BaseDialog;
import com.li.videoapplication.tools.ToastHelper;
import com.li.videoapplication.utils.InputUtil;
import com.li.videoapplication.utils.StringUtil;

import io.rong.eventbus.EventBus;

/**
 * 弹框：编辑昵称
 */
public class EditNameDialog extends BaseDialog implements View.OnClickListener {

    private Activity activity;
    private NameCallback nameCallback;
    private EditText name;

    public EditNameDialog(Context context, String oldName, NameCallback nameCallback) {
        super(context);

        this.nameCallback = nameCallback;
        try {
            activity = (Activity) context;
        } catch (Exception e) {
            e.printStackTrace();
        }

        name = (EditText) findViewById(R.id.dialog_editname_name);

        findViewById(R.id.dialog_editname_no).setOnClickListener(this);
        findViewById(R.id.dialog_editname_yes).setOnClickListener(this);

        if (!StringUtil.isNull(oldName))
            name.setText(oldName);

        setCanceledOnTouchOutside(false);
    }

    @Override
    protected int getContentView() {
        return R.layout.dialog_editname;
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

    private String getName() {
        if (name.getText() != null)
            return name.getText().toString();
        return "";
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.dialog_editname_yes:
                //检测用户昵称重复
                DataManager.isRepeat(getName());
                break;
            case R.id.dialog_editname_no:
                dismiss();
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
    }

    public interface NameCallback {
        void onNameCallback(DialogInterface dialog, String newName);
    }

    /**
     * 回调：检测用户昵称重复接口
     */
    public void onEventMainThread(IsRepeatEntity event) {
        if (event != null && event.isResult()) {
            if (event.getData().isRepeat()) {
                ToastHelper.s(event.getData().getInfo());
            } else {
                if (nameCallback != null)
                    nameCallback.onNameCallback(this, getName());
            }
        }
    }
}
