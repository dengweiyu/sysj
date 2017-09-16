package com.li.videoapplication.ui.fragment;

import android.view.View;

import com.handmark.pulltorefresh.library.IPullToRefresh;
import com.li.videoapplication.R;
import com.li.videoapplication.data.DataManager;
import com.li.videoapplication.data.model.response.LoginEntity;
import com.li.videoapplication.framework.TBaseFragment;
import com.li.videoapplication.tools.ShareSDKLoginHelper;
import com.li.videoapplication.ui.ActivityManager;

/**
 *
 */

public class WelcomeChoiceLoginFragment extends TBaseFragment implements View.OnClickListener {
    private ShareSDKLoginHelper loginHelper = new ShareSDKLoginHelper();
    @Override
    protected int getCreateView() {
        return R.layout.activity_choice_login_channel;
    }

    @Override
    protected void initContentView(View view) {

        view.findViewById(R.id.iv_login_phone).setOnClickListener(this);

        view.findViewById(R.id.iv_login_qq).setOnClickListener(this);

        view.findViewById(R.id.iv_login_wechat).setOnClickListener(this);

        view.findViewById(R.id.iv_login_weibo).setOnClickListener(this);
        view.findViewById(R.id.iv_go_to).setOnClickListener(this);
        loginHelper.initSDK(getActivity());
    }

    @Override
    protected IPullToRefresh getPullToRefresh() {
        return null;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_login_phone:
                ActivityManager.startLoginActivity(getActivity());
                break;
            case R.id.iv_login_qq:
                loginHelper.qq();
                break;
            case R.id.iv_login_wechat:
                loginHelper.wx();
                break;
            case R.id.iv_login_weibo:
                loginHelper.wb();
                break;
            case R.id.iv_go_to:
                ActivityManager.startMainActivity(getActivity());
                getActivity().finish();
                break;
        }
    }

    /**
     * 回调:登录
     */
    public void onEventMainThread(LoginEntity event) {
        if (event != null) {
            dismissProgressDialog();
            if (event.isResult()) {
                showToastShort("登录成功");
                DataManager.userProfilePersonalInformation(getMember_id(), getMember_id());
                ActivityManager.startMainActivity(getActivity());
                getActivity().finish();
            } else {
                showToastShort(event.getMsg());
            }
        }
    }
}
