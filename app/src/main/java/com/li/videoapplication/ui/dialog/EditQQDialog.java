package com.li.videoapplication.ui.dialog;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.li.videoapplication.R;
import com.li.videoapplication.data.DataManager;
import com.li.videoapplication.data.model.entity.Member;
import com.li.videoapplication.data.model.response.UserProfileFinishMemberInfoEntity;
import com.li.videoapplication.framework.BaseDialog;
import com.li.videoapplication.tools.AnimationHelper;
import com.li.videoapplication.tools.ToastHelper;
import com.li.videoapplication.utils.InputUtil;
import com.li.videoapplication.utils.StringUtil;

import io.rong.eventbus.EventBus;

/**
 * 弹框：编辑qq
 */
public class EditQQDialog extends BaseDialog implements View.OnClickListener {

    private AnimationHelper animationHelper;
    private Activity activity;
    private EditText qq;
    private Member newMember;

    public EditQQDialog(Context context) {
        super(context);
        animationHelper = new AnimationHelper();
        try {
            activity = (Activity) context;
        } catch (Exception e) {
            e.printStackTrace();
        }
        newMember = (Member) getUser().clone();

        qq = (EditText) findViewById(R.id.editqq_qq);

        if (!StringUtil.isNull(newMember.getQq())) {
            TextView title = (TextView) findViewById(R.id.editqq_title);
            title.setText("更改QQ");
        }

        findViewById(R.id.editqq_confirm).setOnClickListener(this);

        showKeyboard();
    }

    @Override
    protected int getContentView() {
        return R.layout.dialog_editqq;
    }

    public void showKeyboard() {
        if (qq != null) {
            InputUtil.showKeyboard(qq);
        }
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

    private String getQQText() {
        if (qq.getText() != null)
            return qq.getText().toString();
        return "";
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.editqq_confirm:
                if (StringUtil.isNull(getQQText())) {
                    showToastShort("qq号码不能为空");
                    animationHelper.startAnimationShake(qq);
                    return;
                }
                newMember.setQq(getQQText());
                // 编辑个人资料
                DataManager.userProfileFinishMemberInfo(newMember);
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

    /**
     * 回调:编辑个人资料
     */
    public void onEventMainThread(UserProfileFinishMemberInfoEntity event) {
        if (event != null) {
            ToastHelper.s(event.getMsg());
            if (event.isResult()) dismiss();
        }
    }
}
