package com.li.videoapplication.ui.dialog;

import android.content.Context;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.li.videoapplication.R;
import com.li.videoapplication.data.DataManager;
import com.li.videoapplication.data.model.entity.Member;
import com.li.videoapplication.data.model.response.UserProfileFinishMemberInfoEntity;
import com.li.videoapplication.framework.BaseDialog;
import com.li.videoapplication.tools.ToastHelper;

import io.rong.eventbus.EventBus;

/**
 * 弹框：编辑性别
 */
public class EditSexDialog extends BaseDialog {

    private Member newMember;

    public EditSexDialog(Context context) {
        super(context);
        newMember = (Member) getUser().clone();
        RadioButton female = (RadioButton) findViewById(R.id.female);
        if (newMember.getSex() == 0){
            female.setChecked(true);
        }
        RadioGroup radioGroup = (RadioGroup) findViewById(R.id.radiogroup);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.male:
                        newMember.setSex(1);
                        break;
                    case R.id.female:
                        newMember.setSex(0);
                        break;
                }
                // 编辑个人资料
                DataManager.userProfileFinishMemberInfo(newMember);
            }
        });
    }

    @Override
    protected int getContentView() {
        return R.layout.dialog_editsex;
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
