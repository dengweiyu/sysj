package com.li.videoapplication.ui.fragment;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.IPullToRefresh;
import com.li.videoapplication.R;
import com.li.videoapplication.data.DataManager;
import com.li.videoapplication.data.image.GlideHelper;
import com.li.videoapplication.framework.TBaseFragment;
import com.li.videoapplication.mvp.Constant;
import com.li.videoapplication.tools.UmengAnalyticsHelper;
import com.li.videoapplication.ui.activity.GroupDetailActivity;
import com.li.videoapplication.utils.StringUtil;
import com.li.videoapplication.utils.URLUtil;

/**
 * 碎片：游戏介绍
 */
public class GroupdetailIntroduceFragment extends TBaseFragment{

    private GroupDetailActivity activity;

    private TextView introduce;
    private ImageView mIcon;
    private TextView mType;
    private TextView mDownloadNum;
    private TextView mName;
    private TextView mOpen;
    private View mSubTitle;
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            this.activity = (GroupDetailActivity) activity;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        loadData();
    }

    @Override
    protected int getCreateView() {
        return R.layout.pager_groupdetail_introduce;
    }

    @Override
    protected void initContentView(View view) {
        introduce = (TextView) view.findViewById(R.id.groupdetail_introduce);
        mIcon = (ImageView)view.findViewById(R.id.iv_group_detail_icon);
        mName = (TextView)view.findViewById(R.id.tv_group_detail_name);
        mType = (TextView)view.findViewById(R.id.tv_group_detail_type);
        mDownloadNum = (TextView)view.findViewById(R.id.tv_group_detail_num);
        mOpen = (TextView)view.findViewById(R.id.tv_group_detail_open);
        mSubTitle = view.findViewById(R.id.rl_introduce_subtitle);
        mOpen.setOnClickListener(mListener);
    }

    public void loadData() {
        if (activity.game != null){
            mSubTitle.setVisibility(View.VISIBLE);
            setTextViewText(introduce, activity.game.getGame_description());
            setTextViewText(mName, activity.game.getGroup_name());
            setTextViewText(mType, "类型："+activity.game.getType_name());
            String downloadNum = activity.game.getDownload_num();
            if (StringUtil.isNull(downloadNum)){
                downloadNum = "0";
            }
            setTextViewText(mDownloadNum, "下载："+ StringUtil.formatNum(downloadNum));
            GlideHelper.displayImage(getActivity(),activity.game.getFlag(),mIcon);
        }

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        //该fragment处于最前台交互状态
        if (isVisibleToUser) {
            if (null != activity &&activity.isSingleEvent){
                UmengAnalyticsHelper.onEvent(getActivity(), UmengAnalyticsHelper.GAME, activity.game.getGroup_name()+"-"+ "游戏圈-游戏介绍");
            }else {
                UmengAnalyticsHelper.onEvent(getActivity(), UmengAnalyticsHelper.GAME, "游戏圈-游戏介绍");
            }

        }
    }

    @Override
    protected IPullToRefresh getPullToRefresh() {
        return null;
    }


    final View.OnClickListener mListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.tv_group_detail_open:
                    install();
                    break;
            }
        }
    };


    /**
     * 跳转：安装应用
     */
    private void install() {
        if (activity.game != null &&activity.game.getA_download() != null) {
            if (URLUtil.isURL(activity.game.getA_download())) {
                Uri uri = Uri.parse(activity.game.getA_download());
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);

                // 游戏下载数+1
                DataManager.downloadClick217(activity.game.getGame_id(), getMember_id(),
                        Constant.DOWNLOAD_LOCATION_GROUP, activity.group_id);
            }
        }
    }
}
