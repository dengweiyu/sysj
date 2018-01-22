package com.li.videoapplication.mvp.mall.view;

import android.graphics.Color;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.IPullToRefresh;
import com.li.videoapplication.R;
import com.li.videoapplication.data.DataManager;
import com.li.videoapplication.data.VipManager;
import com.li.videoapplication.data.model.response.UserVipInfoEntity;
import com.li.videoapplication.data.model.response.VipRecharge2Entity;
import com.li.videoapplication.data.model.response.VipRechargeEntity;
import com.li.videoapplication.framework.TBaseFragment;
import com.li.videoapplication.mvp.Constant;
import com.li.videoapplication.tools.TimeHelper;
import com.li.videoapplication.ui.ActivityManager;
import com.li.videoapplication.ui.dialog.VipSelectDialog;
import com.li.videoapplication.utils.AppUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

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

    @BindView(R.id.ll_vip3_server)
    LinearLayout llVip3Server;
    @BindView(R.id.id_vip3_server_tv)
    TextView id_vip3_server_tv;
    @BindView(R.id.id_vip3_server_iv)
    ImageView id_vip3_server_iv;

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
        llVip3Server.setOnClickListener(this);
        loadData();
    }

    private void loadData(){
        //当前账号VIP
        DataManager.getUserVipInfo(getMember_id());
        //
        DataManager.getVIPRechargeInfo2();
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
                    DataManager.getVIPRechargeInfo2();
                }
                break;
            case R.id.tv_recharge_vip_now:
            case R.id.tv_payment_vip_now:
                //开通VIP
                ActivityManager.startTopUpActivity(getActivity(), Constant.TOPUP_ENTRY_MYWALLEY,2);
                break;
            case R.id.ll_vip3_server:
                if(VipManager.getInstance().isLevel3()){
                    AppUtil.startQQChat(getActivity(),"526619379");
                }else{
                    showToastLong(R.string.vip3_no_server_tip);
                }
//            case R.id.id_vip3_server_tv:
//            case R.id.id_vip3_server_iv:
//                if(VipManager.getInstance().isLevel3()){
//                    AppUtil.startQQChat(getActivity(),"526619379");
//                }else{
//                    showToastLong(R.string.vip3_no_server_tip);
//                }
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
                        vie3Server(false);
                        break;
                    case  "2":
                        resLevel = R.drawable.vip_level_2;
                        color="#f89312";
                        vie3Server(false);
                        break;
                    case  "3":
                        resLevel = R.drawable.vip_level_3;
                        color="#ff3995";
                        vie3Server(true);
                        break;
                }
            }else {
                switch (entity.getData().getLevel()){
                    case  "1":
                        resLevel = R.drawable.vip_level_gray_1;
                        color = "#b8b8b8";
                        vie3Server(false);
                        break;
                    case  "2":
                        resLevel = R.drawable.vip_level_gray_2;
                        color = "#b8b8b8";
                        vie3Server(false);
                        break;
                    case  "3":
                        resLevel = R.drawable.vip_level_gray_3;
                        color = "#b8b8b8";
                        vie3Server(false);
                        break;
                }
                ((TextView)mRoot.findViewById(R.id.tv_show_vip_detail)).setTextColor(Color.parseColor(color));
            }
            mVipIcon.setImageResource(resLevel);
            mVipText.setTextColor(Color.parseColor(color));

        }else {
            //show empty view
            mRoot.findViewById(R.id.tv_payment_vip_now).setVisibility(View.GONE);
            llVip3Server.setVisibility(View.GONE);
            mRoot.findViewById(R.id.ll_my_vip_info).setVisibility(View.INVISIBLE);
            mEmptyView.setVisibility(View.VISIBLE);
        }
    }

//    public void  onEventMainThread(VipRechargeEntity entity){
//        if (entity.isResult() && entity.getData() != null){
//            mSelectDialog = new VipSelectDialog(getActivity(),entity.getData());
//        }
//    }

    public void onEventMainThread(VipRecharge2Entity entity) {
        if (entity.isResult() && entity.getData() != null){
            List<VipRechargeEntity.DataBean> data = change2VipRechargeEntityData(entity);
            mSelectDialog = new VipSelectDialog(getActivity(), data);
        }
    }

    private void vie3Server(boolean enable){
        if(enable){
            id_vip3_server_tv.setTextColor(ActivityCompat.getColor(getActivity(),R.color.vip_color_yellow));
            id_vip3_server_iv.setImageResource(R.drawable.vip3_server);
        }else{
            id_vip3_server_tv.setTextColor(ActivityCompat.getColor(getActivity(),R.color.color_999999));
            id_vip3_server_iv.setImageResource(R.drawable.vip3_no_server);
        }
    }

    private List<VipRechargeEntity.DataBean> change2VipRechargeEntityData(VipRecharge2Entity entity){
        List<VipRechargeEntity.DataBean> data = new ArrayList<>();
        for (int i = 0; i < entity.getData().size(); i++) {
            VipRechargeEntity.DataBean dataBean = new VipRechargeEntity.DataBean();
            dataBean.setName(entity.getData().get(i).getName());
            dataBean.setLevel(entity.getData().get(i).getLevel());
            dataBean.setPrice(entity.getData().get(i).getPrice());
            dataBean.setIcon(entity.getData().get(i).getSelectedIcon());
            List<String> desc = new ArrayList<>();
            for (VipRecharge2Entity.VipAllInfo vipAllInfo : entity.getVipAllInfo()) {
                if (dataBean.getLevel() == vipAllInfo.getLevel()) {
                    desc.add(vipAllInfo.getName());
                }
            }
            dataBean.setDescription(desc);
            data.add(dataBean);
        }

        return data;
    }
}
