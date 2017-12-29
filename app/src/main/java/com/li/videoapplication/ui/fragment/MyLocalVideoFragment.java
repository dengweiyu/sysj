package com.li.videoapplication.ui.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.IPullToRefresh;
import com.li.videoapplication.R;
import com.li.videoapplication.data.DataManager;
import com.li.videoapplication.data.database.VideoCaptureEntity;
import com.li.videoapplication.data.database.VideoCaptureManager;
import com.li.videoapplication.data.database.VideoCaptureResponseObject;
import com.li.videoapplication.data.model.event.VideoCutEvent;
import com.li.videoapplication.data.model.event.VideoEditor2VideoManagerEvent;
import com.li.videoapplication.data.model.response.PaymentEntity;
import com.li.videoapplication.data.preferences.PreferencesHepler;
import com.li.videoapplication.data.preferences.VideoPreferences;
import com.li.videoapplication.data.upload.VideoShareTask208;
import com.li.videoapplication.framework.TBaseFragment;
import com.li.videoapplication.tools.UmengAnalyticsHelper;
import com.li.videoapplication.ui.ActivityManager;
import com.li.videoapplication.ui.DialogManager;
import com.li.videoapplication.ui.activity.VideoMangerActivity;
import com.li.videoapplication.ui.adapter.MyLocalVideoAdapter;
import com.li.videoapplication.tools.ToastHelper;
import com.li.videoapplication.utils.StringUtil;
import com.li.videoapplication.utils.TextUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import me.everything.android.ui.overscroll.VerticalOverScrollBounceEffectDecorator;
import me.everything.android.ui.overscroll.adapters.AbsListViewOverScrollDecorAdapter;

/**
 * 碎片：本地视频
 */
@SuppressLint("SdCardPath")
public class MyLocalVideoFragment extends TBaseFragment {

    public TextView storgeText;
    private LinearLayout storgeRoot;

    @BindView(R.id.ab_videomanager_import)
    public View tvImport;

    /**
     * 已选择，将要导入的文件列表
     */
    public List<VideoCaptureEntity> myImportData = new ArrayList<>();

    public ListView listView;
    public MyLocalVideoAdapter adapter;
    public List<VideoCaptureEntity> data = new ArrayList<>();

    private VideoMangerActivity activity;


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            this.activity = (VideoMangerActivity) activity;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            UmengAnalyticsHelper.onEvent(getActivity(), UmengAnalyticsHelper.SLIDER, "我的视频-本地视频");
        }
    }

    @Override
    protected IPullToRefresh getPullToRefresh() {
        return null;
    }

    @Override
    protected int getCreateView() {
        return R.layout.fragment_mylocalvideo;
    }

    @Override
    protected void initContentView(View view) {

        initContentView(view, null);
        // 验证并加载本地视频
        DataManager.LOCAL.checkVideoCaptures();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        VideoShareTask208.removeCallbacks(adapter);
    }

    /**
     * 初始化控件
     */
    private void initContentView(View view, Object o) {
        tvImport.setVisibility(View.VISIBLE);
        listView = (ListView) view.findViewById(R.id.listview);
        new VerticalOverScrollBounceEffectDecorator(new AbsListViewOverScrollDecorAdapter(listView));

        TextView emptyText = (TextView) view.findViewById(R.id.mylocalvideo_empty);
        listView.setEmptyView(emptyText);

        storgeText = (TextView) view.findViewById(R.id.videomanager_text);
        storgeRoot = (LinearLayout) view.findViewById(R.id.videomanager_root);
        storgeRoot.setVisibility(View.GONE);
        storgeText.setText("");

        adapter = new MyLocalVideoAdapter(getActivity(), data, listView, (VideoMangerActivity) getActivity());
        VideoShareTask208.addCallbacks(adapter);

        listView.setAdapter(adapter);
        listView.setOnItemClickListener(mListener);
    }

    @OnClick(R.id.ab_videomanager_import)
    public void importVideo(){
        if (activity != null) {
            UmengAnalyticsHelper.onEvent(activity, UmengAnalyticsHelper.MAIN, "发布-视频-点击视频页面的导入次数");
            activity.setImporting(true);
            // 导入外部视频
            DataManager.LOCAL.importVideoCaptures();
        }
    }

    public void refreshStorge() {
        if (storgeRoot == null || storgeText == null)
            return;

        if (activity != null &&
                !StringUtil.isNull(activity.inSDCardTotalSize) &&
                !StringUtil.isNull(activity.inSDCardUsedSize) &&
                !StringUtil.isNull(activity.inSDCardAppSize) &&
                !StringUtil.isNull(activity.inSDCardLeftSize)) {// 显示
            storgeRoot.setVisibility(View.VISIBLE);
            storgeText.setText(Html.fromHtml(
                    "共" + toBlueColor(activity.myLocalVideoSize + "") + "个视频，" +
                            toBlueColor(activity.myScreenShotSize + "") + "张图片，" +
                            "剩余" + toBlueColor(activity.inSDCardLeftSize) + "/"
                            + toBlueColor(activity.inSDCardTotalSize)));
        } else {
            storgeRoot.setVisibility(View.GONE);
            storgeText.setText("");
        }
    }

    private String toBlueColor(String text) {
        return TextUtil.toColor(text, "#40a7ff");
    }


    /**
     * Item 的点击事件
     */
    final AdapterView.OnItemClickListener mListener = new AdapterView.OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            final VideoCaptureEntity record = adapter.getItem(position);

            if (!PreferencesHepler.getInstance().isLogin()) {// 检查用户是否已登陆
                ToastHelper.s("请先登录");
            }

            if (VideoShareTask208.isUploading()) {
                ToastHelper.s("当前有视频在上传");
                return;
            }

            if (record.getVideo_station().equals(VideoCaptureEntity.VIDEO_STATION_UPLOADING) ||
                    record.getVideo_station().equals(VideoCaptureEntity.VIDEO_STATION_PAUSE)) {
                ToastHelper.s("该视频正在上传");
                return;
            }

            if (!adapter.edit(record.getVideo_path())) {
                return;
            }

            ActivityManager.startVideoEditorActivity(getActivity(), record);
            UmengAnalyticsHelper.onEvent(getActivity(), UmengAnalyticsHelper.SLIDER, "本地视频-编辑");
        }
    };

    /**
     * 导入视频对话框
     */
    private void showImportDialog(List<VideoCaptureEntity> list) {
        myImportData.clear();

        // 导入外部视频
        DialogManager.showVideoManagerImportDialog(getActivity(), list, this,
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Log.i(tag, "import=" + myImportData);
                        for (int i = 0; i < myImportData.size(); i++) {
                            Log.i(tag, "import=" + 1);
                            VideoCaptureManager.save(myImportData.get(i).getVideo_path(),
                                    VideoCaptureEntity.VIDEO_SOURCE_EXT,
                                    VideoCaptureEntity.VIDEO_STATION_LOCAL);
                            // 用来记录视频是否已经导入
                            VideoPreferences.getInstance().putBoolean(myImportData.get(i).getVideo_path(), true);
                        }
                        // 验证并加载本地视频
                        DataManager.LOCAL.checkVideoCaptures();
                    }
                });
    }

    /**
     * 回调:兑换
     */
    public void onEventMainThread(PaymentEntity event) {

        if (event != null) {
            if (event.isResult()) {// 支付视频推荐位成功
                ToastHelper.s("申请视频推荐成功，请到兑换记录里查看详情");
                DataManager.userProfilePersonalInformation(getMember_id(), getMember_id());
            } else {
                ToastHelper.s(event.getMsg());
            }
        }
    }

    /**
     * 回调:视频剪辑完成
     */
    public void onEventMainThread(VideoCutEvent event) {
        // 验证并加载本地视频
        DataManager.LOCAL.checkVideoCaptures();
    }

    /**
     * 回调：加载本地视频
     */
    public void onEventMainThread(VideoCaptureResponseObject event) {

        if (event.getResultCode() == VideoCaptureResponseObject.RESULT_CODE_LOADING
                || event.getResultCode() == VideoCaptureResponseObject.RESULT_CODE_CHECKING) {

            if (event.getData() != null) {
                data.clear();
                data.addAll(event.getData());
                Log.d(tag, "data: == " + data);
                activity.setMyLocalVideoSize(data.size());
                adapter.notifyDataSetChanged();
            }
        } else if (event.getResultCode() == VideoCaptureResponseObject.RESULT_CODE_IMPORTING) {
            if (event.getData() != null) {
                if (event.getData().size() > 0) {
                    List<VideoCaptureEntity> list = event.getData();
                    showImportDialog(list);
                } else {
                    showToastShort("本地没有相关视频");
                }
            }
            activity.setImporting(false);
        }
    }

    /**
     * 回调：本地视频
     */
    public void onEventMainThread(VideoEditor2VideoManagerEvent event) {
        Log.d(tag, "onMessage: VideoEditor2VideoManagerEvent");

        if (adapter != null) {
            // 视频编辑
            if (event.getIndex() == 1) {
                Log.d(tag, "onMessage: 1");
                adapter.notifyDataSetChanged();
            }
            // 视频分享
            if (event.getIndex() == 2) {
                Log.d(tag, "onMessage: 2");
                adapter.updataRecordsAndViews(event.getVideo_paths());
            }
        }
    }
}
