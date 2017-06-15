package com.li.videoapplication.ui.dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.ifeimo.im.framwork.Proxy;
import com.li.videoapplication.R;
import com.li.videoapplication.data.model.response.VideoPlayGiftEntity;
import com.li.videoapplication.framework.BaseOverShootDialog;
import com.li.videoapplication.ui.activity.MainActivity;
import com.li.videoapplication.ui.activity.VideoPlayActivity;
import com.li.videoapplication.ui.adapter.GiftRankAdapter;
import com.li.videoapplication.ui.view.SpanItemDecoration;
import com.li.videoapplication.utils.ScreenUtil;
import com.li.videoapplication.utils.TextUtil;

import java.util.List;

/**
 * 礼物排名
 */

public class GiftRankDialog extends BaseOverShootDialog implements View.OnClickListener {

    private RecyclerView mRank;
    private GiftRankAdapter mAdapter;
    private TextView mDescription;
    private ImageView mGoPlayGift;
    private VideoPlayActivity mActivity;
    public GiftRankDialog(Context context, List<VideoPlayGiftEntity.DataBean.IncludesBean> data,String memberId) {
        super(context);
        initList(data,memberId);
        if (context instanceof VideoPlayActivity){
            mActivity = (VideoPlayActivity)context;
        }
    }

    private void initList(List<VideoPlayGiftEntity.DataBean.IncludesBean> datas,String memberId){
        mRank = (RecyclerView) findViewById(R.id.rv_rank);
        mDescription = (TextView)findViewById(R.id.tv_my_rank_description);
        mGoPlayGift = (ImageView)findViewById(R.id.iv_go_play_gift);
        mAdapter = new GiftRankAdapter(datas,memberId);
        mRank.setLayoutManager(new LinearLayoutManager(mContext));
        mRank.addItemDecoration(new SpanItemDecoration(ScreenUtil.dp2px(mContext,10f),false,false,true,false));
        mRank.setAdapter(mAdapter);

        mGoPlayGift.setOnClickListener(this);
        setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                if (mAdapter != null){
                    mAdapter.setFirstShow(false);
                }
            }
        });

        if (memberId != null){
            boolean isInRank = false;
            int distance = 0;
            int mRank = 0;
            for (int i = 0; i < datas.size(); i++) {
                if (memberId.equals(datas.get(i).getMember_id())){
                    //上榜
                    isInRank = true;
                    mRank = i+1;
                    if (i >= 1){
                        try {
                            distance = Integer.parseInt(datas.get(i-1).getCoin_sum()) - Integer.parseInt(datas.get(i).getCoin_sum());
                        } catch (NumberFormatException e) {
                            e.printStackTrace();
                        }
                        break;
                    }
                }
            }
            if (isInRank){
                if (mRank == 1){
                    mDescription.setText(Html.fromHtml("我的排名："+ TextUtil.toColor(mRank+"","#fffa00")+"，您已经是榜首，继续加油哦~"));
                }else {
                    mDescription.setText(Html.fromHtml("我的排名："+ TextUtil.toColor(mRank+"","#fffa00")+"，距离上一排名还差"+TextUtil.toColor(distance+"","#fffa00")+"魔币"));
                }
                mGoPlayGift.setVisibility(View.GONE);
            }else {
                mDescription.setText(Html.fromHtml("我的排名："+ TextUtil.toColor("未打赏","#fffa00")));
                mGoPlayGift.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    protected int getContentView() {
        return R.layout.dialog_gift_rank;
    }

    @Override
    public void show() {
        super.show();
        if (mAdapter != null){
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void afterContentView(Context context) {
        super.afterContentView(context);
        Window window = getWindow();

        if (window != null) {
            WindowManager.LayoutParams params = window.getAttributes();
            // 根据x，y坐标设置窗口需要显示的位置
            params.x = 0; // x小于0左移，大于0右移
            params.y = 0; // y小于0上移，大于0下移
            params.gravity = Gravity.CENTER;
            params.width = (int) (0.85* ScreenUtil.getScreenWidth(window.getWindowManager()));
            params.height = ScreenUtil.getScreenHeight(window.getWindowManager());
            window.setAttributes(params);
        }
        setCancelable(true);
        setCanceledOnTouchOutside(true);
        findViewById(R.id.iv_rank_close).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_rank_close:
                dismiss();
                break;
            case R.id.iv_go_play_gift:
                dismiss();
                if (mActivity != null){
                    mActivity.setGiftFragmentState(true);
                }
                break;
        }
    }
}
