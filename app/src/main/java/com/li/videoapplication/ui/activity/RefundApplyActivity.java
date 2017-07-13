package com.li.videoapplication.ui.activity;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.li.videoapplication.R;
import com.li.videoapplication.data.DataManager;
import com.li.videoapplication.data.model.response.PlayWithOrderDetailEntity;
import com.li.videoapplication.data.model.response.RefundApplyEntity;
import com.li.videoapplication.framework.TBaseAppCompatActivity;
import com.li.videoapplication.tools.ToastHelper;
import com.li.videoapplication.ui.adapter.RefundReasonAdapter;
import com.li.videoapplication.ui.dialog.LoadingDialog;
import com.li.videoapplication.ui.fragment.PlayWithOrderDetailFragment;
import com.li.videoapplication.utils.StringUtil;

import io.rong.imlib.filetransfer.Call;

/**
 * 退款
 */

public class RefundApplyActivity extends TBaseAppCompatActivity implements View.OnClickListener {
    private int mRole;

    private PlayWithOrderDetailFragment mFragment;

    private PlayWithOrderDetailEntity mOrderDetail;

    private String mOrderId;

    private RefundReasonAdapter mAdapter;

    private RecyclerView mReasonList;

    private EditText mInput;

    private TextView mTip;

    private TextView mCommit;

    private View mInputLayout;
    private View mDivider;
    private LoadingDialog mLoadingDialog;
    @Override
    public void refreshIntent() {
        super.refreshIntent();


        setOrderDetail((PlayWithOrderDetailEntity)getIntent().getSerializableExtra("order_detail"));

        if (mOrderDetail == null){
            mOrderId = getIntent().getStringExtra("order_id");
            if (!StringUtil.isNull(mOrderId)){
                DataManager.getPlayWithOrderDetail(getMember_id(),mOrderId);
            }
        }else {
            mOrderId = mOrderDetail.getData().getOrder_id();
        }
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_refund_apply;
    }

    @Override
    public void afterOnCreate() {
        super.afterOnCreate();
        initToolbar();

        showOrderFragment(mOrderDetail);

        findViewById(R.id.ll_coach_operation).setVisibility(View.VISIBLE);
        findViewById(R.id.tv_chat_with_coach).setVisibility(View.GONE);

        mInputLayout = findViewById(R.id.cv_input_reason);
        mCommit = (TextView) findViewById(R.id.tv_coach_selected);
        mCommit.setOnClickListener(this);
        mCommit.setText("提交");
        mInput = (EditText)findViewById(R.id.et_refund_reason_input) ;
        mTip = (TextView)findViewById(R.id.tv_refund_reason);
        mDivider = findViewById(R.id.v_divider_top);

        mReasonList = (RecyclerView)findViewById(R.id.rv_refund_reason);
        mReasonList.setLayoutManager(new LinearLayoutManager(this));

        refreshContent();

        mLoadingDialog = new LoadingDialog(this);
        mLoadingDialog.setProgressText("申请中..");
        if (mOrderDetail == null){
            mLoadingDialog.show();
        }

    }

    private void  initToolbar(){
        findViewById(R.id.tb_back).setOnClickListener(this);
        ((TextView)findViewById(R.id.tb_title)).setText("退款申请");
        setSupportActionBar(((Toolbar)findViewById(R.id.toolbar)));
    }

    private void showOrderFragment(PlayWithOrderDetailEntity entity){
        if (entity == null){
            return;
        }
        if (mFragment == null ){
            mFragment = PlayWithOrderDetailFragment.newInstance(entity,true);
        }
        mFragment.addViewGone(R.id.ll_order_status_root);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.rl_play_with_detail_fragment,mFragment)
                .commitAllowingStateLoss();

    }

    private void refreshContent(){
        if (mOrderDetail == null){
            return;
        }
        mAdapter = new RefundReasonAdapter(mOrderDetail.getDefaultReason());
        mAdapter.setDefaultReason(mOrderDetail.getData().getDefault_refund_reason());

        mReasonList.setAdapter(mAdapter);
        mReasonList.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void SimpleOnItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {

                mAdapter.setSelectPosition(i);
            }
        });


        switch (mOrderDetail.getData().getStatusX()){
            case "10":
                if (mRole == PlayWithOrderDetailActivity.ROLE_COACH){
                    mAdapter.setApplyDone(true);
                    mInput.setVisibility(View.GONE);
                    mTip.setText(mOrderDetail.getData().getRefund_reason());
                    mTip.setVisibility(View.VISIBLE);
                    mCommit.setVisibility(View.VISIBLE);
                    mDivider.setVisibility(View.VISIBLE);
                }
                mInputLayout.setVisibility(View.GONE);

                break;
            case "11":
                mAdapter.setApplyDone(true);
                mInput.setVisibility(View.GONE);
                mTip.setText(mOrderDetail.getData().getRefund_reason());
                mTip.setVisibility(View.VISIBLE);
                mCommit.setVisibility(View.GONE);
                mInputLayout.setVisibility(View.GONE);
                if(mRole == PlayWithOrderDetailActivity.ROLE_COACH){
                    mDivider.setVisibility(View.VISIBLE);
                }
                break;

            default:
                mAdapter.setApplyDone(false);
                mInput.setVisibility(View.VISIBLE);
                mTip.setText(mOrderDetail.getData().getRefund_reason());
                mTip.setVisibility(View.GONE);
                mCommit.setVisibility(View.VISIBLE);

                break;
        }

    }

    @Override
    public void loadData() {
        super.loadData();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tb_back:
                finish();
                break;
            case R.id.tv_coach_selected:

                //是否选择默认理由
                if (mAdapter.getSelectPosition() == -1){
                    ToastHelper.s("请选择退款原因哦~");
                    return;
                }
                //是否输入原因
                String reason = mInput.getEditableText().toString();
                if (StringUtil.isNull(reason)){
                    ToastHelper.s("请输入具体原因哦~");
                    return;
                }

                mLoadingDialog.show();
                DataManager.refundApply(getMember_id(),mOrderDetail.getData().getOrder_id(),mOrderDetail.getDefaultReason().get(mAdapter.getSelectPosition()),reason);
                break;
        }
    }

    private void setOrderDetail(PlayWithOrderDetailEntity entity){
        if (entity != null){
            mOrderDetail = entity;
            mRole = PlayWithOrderDetailActivity.getRole(getMember_id(),entity.getUser().getMember_id(),entity.getCoach().getMember_id());
        }
    }

    /**
     *
     */
    public void onEventMainThread(RefundApplyEntity entity){
        if (entity != null && entity.isResult()){
            ToastHelper.s("您已成功申请退款，金额将会在七个工作日内原路退还");

            /*mOrderDetail.getData().setDefault_refund_reason(mOrderDetail.getDefaultReason().get(mAdapter.getSelectPosition()));
            mOrderDetail.getData().setRefund_reason(mInput.getEditableText().toString());
            mOrderDetail.getData().setStatusX("10");
            refreshContent();*/
            DataManager.getPlayWithOrderDetail(getMember_id(),mOrderId);
        }else {
            if (entity != null){
                if (entity.getCode() == 20022){
                    ToastHelper.s("您已申请成功，无需重复申请");
                }
            }
        }
    }

    /**
     *订单详情
     */
    public void onEventMainThread(PlayWithOrderDetailEntity entity){
        if (entity != null && entity.isResult()){
            setOrderDetail(entity);
            //更新页面
            showOrderFragment(mOrderDetail);

            refreshContent();
        }

        mLoadingDialog.dismiss();
    }
}
