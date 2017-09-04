package com.li.videoapplication.mvp.mall.view;

import android.graphics.Color;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.IPullToRefresh;
import com.li.videoapplication.R;
import com.li.videoapplication.data.DataManager;
import com.li.videoapplication.data.model.response.UserVipInfoEntity;
import com.li.videoapplication.data.model.response.VipRechargeEntity;
import com.li.videoapplication.framework.TBaseFragment;
import com.li.videoapplication.mvp.Constant;
import com.li.videoapplication.tools.TimeHelper;
import com.li.videoapplication.ui.ActivityManager;

/**
 * 我的VIP 详情
 */

public class MyVipInfoFragment extends TBaseFragment implements View.OnClickListener {
    private View mRoot;

    private ImageView mVipIcon;

    private TextView mVipText;

    private TextView mOverdueTime;

    private View mEmptyView;

    private VipSelectDialog mSelectDialog;

    private int mVipLevel = 0;
    @Override
    protected int getCreateView() {
        return R.layout.fragment_my_vip_info;
    }

    @Override
    protected void initContentView(View view) {
        mRoot = view;
        mRoot.setVisibility(View.INVISIBLE);
        mVipIcon = (ImageView)mRoot.findViewById(R.id.iv_vip_level_icon);
        mVipText = (TextView)mRoot.findViewById(R.id.tv_vip_level);
        mOverdueTime = (TextView)mRoot.findViewById(R.id.tv_overdue_time);
        mEmptyView = mRoot.findViewById(R.id.ll_no_vip_info);

        mRoot.findViewById(R.id.tv_show_vip_detail).setOnClickListener(this);
        mRoot.findViewById(R.id.tv_recharge_vip_now).setOnClickListener(this);
        mRoot.findViewById(R.id.tv_payment_vip_now).setOnClickListener(this);
        loadData();
    }

    private void loadData(){
        //当前账号VIP
        DataManager.getUserVipInfo(getMember_id());
        //
        DataManager.vipInfo();
    }

    @Override
    protected IPullToRefresh getPullToRefresh() {
        return null;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_show_vip_detail:
                if (mSelectDialog != null){
                    mSelectDialog.show(mVipLevel);
                }else {
                    DataManager.vipInfo();
                }
                break;
            case R.id.tv_recharge_vip_now:
            case R.id.tv_payment_vip_now:
                //开通VIP
                ActivityManager.startTopUpActivity(getActivity(), Constant.TOPUP_ENTRY_MYWALLEY,2);
                break;
        }
    }


    public void onEventMainThread(UserVipInfoEntity entity){
        mRoot.setVisibility(View.VISIBLE);
        if (entity.isResult() && entity.getData() != null && entity.getData().getValid() != null){

            mRoot.findViewById(R.id.ll_my_vip_info).setVisibility(View.VISIBLE);
            mRoot.findViewById(R.id.tv_payment_vip_now).setVisibility(View.VISIBLE);
            mEmptyView.setVisibility(View.GONE);

            try {
                mVipLevel = Integer.parseInt(entity.getData().getLevel());
            } catch (Exception e){
                e.printStackTrace();
            }

            mVipText.setText("VIP  "+entity.getData().getLevel());
            try {
                mOverdueTime.setText(TimeHelper.getTimeFormat(entity.getData().getEnd_time())+"  到期");
            } catch (Exception e) {
                e.printStackTrace();
            }

            String color ="#fc3c2e";
            int resLevel = 0;

            if ("1".equals(entity.getData().getValid())){
                switch (entity.getData().getLevel()){
                    case  "1":
                        resLevel = R.drawable.vip_level_1;
                        color="#609a8d";
                        break;
                    case  "2":
                        resLevel = R.drawable.vip_level_2;
                        color="#f89312";
                        break;
                    case  "3":
                        resLevel = R.drawable.vip_level_3;
                        color="#ff3995";
                        break;
                }
            }else {
                switch (entity.getData().getLevel()){
                    case  "1":
                        resLevel = R.drawable.vip_level_gray_1;
                        color = "#b8b8b8";
                        break;
                    case  "2":
                        resLevel = R.drawable.vip_level_gray_2;
                        color = "#b8b8b8";
                        break;
                    case  "3":
                        resLevel = R.drawable.vip_level_gray_3;
                        color = "#b8b8b8";
                        break;
                }
                ((TextView)mRoot.findViewById(R.id.tv_show_vip_detail)).setTextColor(Color.parseColor(color));
            }
            mVipIcon.setImageResource(resLevel);
            mVipText.setTextColor(Color.parseColor(color));

        }else {
            //show empty view
            mRoot.findViewById(R.id.tv_payment_vip_now).setVisibility(View.GONE);
            mRoot.findViewById(R.id.ll_my_vip_info).setVisibility(View.INVISIBLE);
            mEmptyView.setVisibility(View.VISIBLE);
        }
    }

    public void  onEventMainThread(VipRechargeEntity entity){
        if (entity.isResult() && entity.getData() != null){
            mSelectDialog = new VipSelectDialog(getActivity(),entity.getData());
        }
    }
}
