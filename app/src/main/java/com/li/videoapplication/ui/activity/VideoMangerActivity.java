package com.li.videoapplication.ui.activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Matrix;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.Html;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.li.videoapplication.R;
import com.li.videoapplication.data.DataManager;
import com.li.videoapplication.data.database.VideoCaptureEntity;
import com.li.videoapplication.data.database.VideoCaptureManager;
import com.li.videoapplication.data.local.Contants;
import com.li.videoapplication.data.local.ScreenShotEntity;
import com.li.videoapplication.data.local.StorageUtil;
import com.li.videoapplication.data.model.entity.Game;
import com.li.videoapplication.data.model.entity.Match;
import com.li.videoapplication.data.model.entity.VideoImage;
import com.li.videoapplication.data.model.event.SearchGame2VideoShareEvent;
import com.li.videoapplication.data.model.event.SharedSuccessEvent;
import com.li.videoapplication.data.model.response.VideoDisplayVideoEntity;
import com.li.videoapplication.data.network.LightTask;
import com.li.videoapplication.data.preferences.VideoPreferences;
import com.li.videoapplication.data.upload.VideoShareTask208;
import com.li.videoapplication.framework.AppManager;
import com.li.videoapplication.framework.AsyncTask;
import com.li.videoapplication.framework.TBaseActivity;
import com.li.videoapplication.tools.UmengAnalyticsHelper;
import com.li.videoapplication.ui.dialog.LoadingDialog;
import com.li.videoapplication.ui.dialog.SharedSuccessDialog;
import com.li.videoapplication.ui.fragment.MyCloudVideoFragment;
import com.li.videoapplication.ui.fragment.MyLocalVideoFragment;
import com.li.videoapplication.ui.fragment.MyScreenShotFragment;
import com.li.videoapplication.tools.ToastHelper;
import com.li.videoapplication.utils.StringUtil;
import com.li.videoapplication.utils.TextUtil;
import com.li.videoapplication.views.CustomViewPager;
import io.rong.eventbus.EventBus;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import me.everything.android.ui.overscroll.HorizontalOverScrollBounceEffectDecorator;
import me.everything.android.ui.overscroll.adapters.ViewPagerOverScrollDecorAdapter;

/**
 * 碎片：视频管理
 */
public class VideoMangerActivity extends TBaseActivity implements
        OnClickListener {

    public Game game;
    public Match match;

    @Override
    public void refreshIntent() {
        try {
            game = (Game) getIntent().getSerializableExtra("game");
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            match = (Match) getIntent().getSerializableExtra("match");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 选择要删除的文件列表选
     */
    public static List<Boolean> myLocalVideoDeleteData = new ArrayList<>();
    public static List<Boolean> myCloudVideoDeleteData = new ArrayList<>();
    public static List<Boolean> myScreenShotDeleteData = new ArrayList<>();

    /**
     * 已选择，将要操作的列表
     */
    public List<VideoCaptureEntity> myLocalData = new ArrayList<>();
    public List<VideoImage> myCloudData = new ArrayList<>();
    public List<ScreenShotEntity> myScreenData = new ArrayList<>();

    /**
     * 是否处于批量删除状态
     */
    public static boolean isDeleteMode = false;

    /**
     * SD卡剩余空间，总空间
     */
    public String inSDCardUsedSize;
    public String inSDCardTotalSize;
    public String inSDCardLeftSize;
    /**
     * 录屏大师占用空间
     */
    public String inSDCardAppSize;
    // 本地视频数
    public int myLocalVideoSize;
    // 本地截图数
    public int myScreenShotSize;

    public void setMyLocalVideoSize(int myLocalVideoSize) {
        this.myLocalVideoSize = myLocalVideoSize;
        loadSDCardSize();
    }

    public void setMyScreenShotSize(int myScreenShotSize) {
        this.myScreenShotSize = myScreenShotSize;
        loadSDCardSize();
    }

    // 卡片界面
    private CustomViewPager viewPager;
    //动画图片
    private ImageView cursor;
    private TextView first, second, third;
    private List<Fragment> views;// Tab页面列表
    private int offset = 0;// 动画图片偏移量
    private int bmpW;// 动画图片宽度
    /**
     * 处在哪一个页面，0为本地视频页，1为云端视频页，2为截图页
     */
    private int currIndex = 0, inWitchPage = 0;
    //各个页卡
    public MyLocalVideoFragment myLocalVideoFragment;
    private MyCloudVideoFragment myCloudVideoFragment;
    private MyScreenShotFragment myScreenShotFragment;

    // 底部栏
    private TextView delete, allSelected;
    private LinearLayout root;

    /**
     * 增加本地视频
     */
    public static void addMyLocalData(VideoCaptureEntity item) {
        VideoMangerActivity activity = (VideoMangerActivity) AppManager.getInstance().getActivity(VideoMangerActivity.class);
        if (activity != null && item != null)
            activity.myLocalData.add(item);
    }

    /**
     * 删除本地视频
     */
    public static void removeMyLocalData(VideoCaptureEntity item) {
        VideoMangerActivity activity = (VideoMangerActivity) AppManager.getInstance().getActivity(VideoMangerActivity.class);
        if (activity != null && item != null) {
            activity.myLocalData.remove(item);
            for (VideoImage v : activity.myCloudData) {
                Log.d("", "removeMyLocalData: v=" + v);
            }
        }
    }

    /**
     * 增加云端视频
     */
    public static void addMyCloudData(VideoImage item) {
        VideoMangerActivity activity = (VideoMangerActivity) AppManager.getInstance().getActivity(VideoMangerActivity.class);
        if (activity != null && item != null) {
            activity.myCloudData.add(item);
            for (VideoImage v : activity.myCloudData) {
                Log.d("", "addMyCloudData: v=" + v);
            }
        }
    }

    /**
     * 删除云端视频
     */
    public static void removeMyScreenData(ScreenShotEntity item) {
        VideoMangerActivity activity = (VideoMangerActivity) AppManager.getInstance().getActivity(VideoMangerActivity.class);
        if (activity != null && item != null)
            activity.myScreenData.remove(item);
    }

    /**
     * 增加本地图片
     */
    public static void addMyScreenData(ScreenShotEntity item) {
        VideoMangerActivity activity = (VideoMangerActivity) AppManager.getInstance().getActivity(VideoMangerActivity.class);
        if (activity != null && item != null)
            activity.myScreenData.add(item);
    }

    /**
     * 删除本地图片
     */
    public static void removeMyCloudData(VideoImage item) {
        VideoMangerActivity activity = (VideoMangerActivity) AppManager.getInstance().getActivity(VideoMangerActivity.class);
        if (activity != null && item != null)
            activity.myCloudData.remove(item);
    }

    /**
     * 虚拟点击编辑按钮
     */
    public static void performClick2() {
        VideoMangerActivity activity = (VideoMangerActivity) AppManager.getInstance().getActivity(VideoMangerActivity.class);
        if (activity != null) {
            activity.performClick();
        }
    }

    /**
     * 更新标题栏
     */
    public static void refreshAbTitle2() {
        VideoMangerActivity activity = (VideoMangerActivity) AppManager.getInstance().getActivity(VideoMangerActivity.class);
        if (activity != null) {
            activity.refreshAbTitle();
        }
    }

    /**
     * 虚拟点击编辑按钮
     */
    private void performClick() {
        abVideoManagerSelected.performClick();
    }

    /**
     * 更新标题栏
     */
    private void refreshAbTitle() {

        if (inWitchPage == 0) {
            abTitle.setText(
                    Html.fromHtml("选中了<font color=\"#15b4eb\"> "
                            + myLocalData.size()
                            + " </font> 个视频"));
        } else if (inWitchPage == 1) {
            abTitle.setText(
                    Html.fromHtml("选中了<font color=\"#15b4eb\"> "
                            + myCloudData.size() + " </font> 个视频"));
        } else if (inWitchPage == 2) {
            abTitle.setText(Html
                    .fromHtml("选中了<font color=\"#15b4eb\"> "
                            + myScreenData.size()
                            + " </font> 张图片"));
        }
    }

    @Override
    public int getContentView() {
        return R.layout.activity_videomanager;
    }

    public int inflateActionBar() {
        return R.layout.actionbar_second;
    }

    @Override
    public void afterOnCreate() {
        super.afterOnCreate();

        setSystemBarBackgroundWhite();
        setAbTitle(R.string.videomanager_title);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void initView() {
        super.initView();

        initContentView();
        initViewPager();

        refreshBottomView(true);
    }

    @Override
    public void loadData() {
        super.loadData();

        isDeleteMode = false;
        myScreenData.clear();
        myLocalData.clear();
    }

    @Override
    public void doBack(View view) {
        if (VideoMangerActivity.isDeleteMode) {
            abVideoManagerCancel.performClick();
        } else {
            super.doBack(view);
        }
    }

    @Override
    public void onBackPressed() {

        if (VideoMangerActivity.isDeleteMode) {
            abVideoManagerCancel.performClick();
        } else {
            super.onBackPressed();
        }
    }

    private void initContentView() {

        abVideoManagerImport.setVisibility(View.VISIBLE);
        abVideoManagerSelected.setVisibility(View.VISIBLE);
        abVideoManagerCancel.setVisibility(View.GONE);

        abVideoManagerImport.setOnClickListener(this);
        abVideoManagerSelected.setOnClickListener(this);
        abVideoManagerCancel.setOnClickListener(this);

        delete = (TextView) findViewById(R.id.videomanager_delete);
        allSelected = (TextView) findViewById(R.id.videomanager_allSelected);
        root = (LinearLayout) findViewById(R.id.root);

        delete.setOnClickListener(this);
        allSelected.setOnClickListener(this);
    }

    /**
     * 底部编辑状态
     *
     * @param flag true 非编辑状态 false 编辑状态
     */
    private void refreshBottomView(boolean flag) {
        if (flag) {
            abVideoManagerCancel.setVisibility(View.GONE);
            abVideoManagerSelected.setVisibility(View.VISIBLE);
            root.setVisibility(View.GONE);
            viewPager.setScanScroll(true);
            isDeleteMode = false;
            allSelected.setText("全选");
            if (inWitchPage == 0) {
                abVideoManagerImport.setVisibility(View.VISIBLE);
            }
        } else {
            abVideoManagerCancel.setVisibility(View.VISIBLE);
            abVideoManagerSelected.setVisibility(View.GONE);
            root.setVisibility(View.VISIBLE);
            viewPager.setScanScroll(false);
            isDeleteMode = true;
            allSelected.setText("全选");
            if (inWitchPage == 0) {
                abVideoManagerImport.setVisibility(View.GONE);
            }
        }
        if (inWitchPage == 1) {
            delete.setText("清除记录");
        } else {
            delete.setText("删除");
        }
        first.setClickable(flag);
        second.setClickable(flag);
        third.setClickable(flag);
    }

    @Override
    public void onClick(View view) {

        /**标题栏导入*/
        if (view == abVideoManagerImport) {
            UmengAnalyticsHelper.onEvent(this, UmengAnalyticsHelper.MAIN, "发布-视频-点击视频页面的导入次数");
            setImporting(true);
            // 导入外部视频
            DataManager.LOCAL.importVideoCaptures();
        }

        /**标题栏取消*/
        if (view == abVideoManagerCancel) {
            setAbTitle(R.string.videomanager_title);
            abVideoManagerSelected.setVisibility(View.VISIBLE);
            abVideoManagerCancel.setVisibility(View.GONE);
            refreshBottomView(true);

            if (inWitchPage == 0) {
                myLocalVideoFragment.adapter.notifyDataSetChanged();
            } else if (inWitchPage == 1) {// 云端视频
                // 允许下拉上拉刷新数据
                myCloudVideoFragment.pullToRefreshListView.setMode(PullToRefreshBase.Mode.BOTH);
                myCloudVideoFragment.adapter.notifyDataSetChanged();
            } else if (inWitchPage == 2) {// 本地截图
                myScreenShotFragment.adapter.notifyDataSetChanged();
            }
        }

        /**标题栏选择*/
        if (view == abVideoManagerSelected) {
            if (inWitchPage == 0 && VideoShareTask208.isUploading()) {
                ToastHelper.s("当前列表有视频在上传视频");
                return;
            }
            if (inWitchPage == 0 && myLocalVideoFragment.data.size() == 0) {
                ToastHelper.s("当前列表没有视频");
                return;
            }
            if (inWitchPage == 1 && myCloudVideoFragment.data.size() == 0) {
                ToastHelper.s("当前列表没有视频");
                return;
            }
            if (inWitchPage == 2 && myScreenShotFragment.data.size() == 0) {
                ToastHelper.s("当前列表没有图片");
                return;
            }

            /**本地视频*/
            if (inWitchPage == 0) {
                myLocalData.clear();
                /**云端视频*/
            } else if (inWitchPage == 1) {
                myCloudVideoFragment.pullToRefreshListView.setMode(PullToRefreshBase.Mode.DISABLED);
                myCloudData.clear();
                /**本地截图*/
            } else if (inWitchPage == 2) {
                myScreenData.clear();
            }

            untraverseData();

            VideoMangerActivity.refreshAbTitle2();
            abVideoManagerSelected.setVisibility(View.GONE);
            abVideoManagerCancel.setVisibility(View.VISIBLE);
            refreshBottomView(false);
        }

        /**底部删除*/
        if (view == delete) {
            setAbTitle(R.string.videomanager_title);
            if (inWitchPage == 0) {
                if (myLocalData.size() > 0) {
                    showDeleteDialog(myLocalData.size());
                } else {
                    ToastHelper.s("请先选择要删除的视频");
                }
            } else if (inWitchPage == 1) {
                if (myCloudData.size() > 0) {
                    showDeleteDialog(myCloudData.size());
                } else {
                    ToastHelper.s("请先选择要删除的视频");
                }
            } else if (inWitchPage == 2) {
                if (myScreenData.size() > 0) {
                    showDeleteDialog(myScreenData.size());
                } else {
                    ToastHelper.s("请先选择要删除的图片");
                }
            }
        }

        /**底部全选*/
        if (view == allSelected) {
            if (allSelected.getText().equals("全选")) {
                traverseData();
                VideoMangerActivity.refreshAbTitle2();
            } else {
                untraverseData();
                VideoMangerActivity.refreshAbTitle2();
                allSelected.setText("全选");
            }
        }
    }

    /**
     * 初始化叶卡
     */
    private void initViewPager() {

        cursor = (ImageView) findViewById(R.id.cursor);
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int screenWeight = dm.widthPixels;
        // 将游标的尺寸宽度设为屏幕的三分之一
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(screenWeight / 3, 3);
        cursor.setLayoutParams(params);

        offset = (screenWeight / 3) / 2;// 计算偏移量
        Matrix matrix = new Matrix();
        matrix.postTranslate(0, 0);
        cursor.setImageMatrix(matrix);// 设置动画初始位置

        first = (TextView) findViewById(R.id.top_first);
        second = (TextView) findViewById(R.id.top_second);
        third = (TextView) findViewById(R.id.top_third);

        first.setOnClickListener(new ClickListener(0));
        second.setOnClickListener(new ClickListener(1));
        third.setOnClickListener(new ClickListener(2));

        viewPager = (CustomViewPager) findViewById(R.id.viewpager);
        new HorizontalOverScrollBounceEffectDecorator(new ViewPagerOverScrollDecorAdapter(viewPager));

        views = new ArrayList<>();

        myLocalVideoFragment = new MyLocalVideoFragment();
        myCloudVideoFragment = new MyCloudVideoFragment();
        myScreenShotFragment = new MyScreenShotFragment();

        views.add(myLocalVideoFragment);
        views.add(myCloudVideoFragment);
        views.add(myScreenShotFragment);

        viewPager.setAdapter(new Adapter(getSupportFragmentManager()));
        viewPager.setOffscreenPageLimit(2);
        viewPager.addOnPageChangeListener(new PagerChangeListener());
        viewPager.setCurrentItem(0);
    }

    public void setImporting(boolean importing){
        if (importing){
            showProgressDialog(LoadingDialog.LOADING);
        }else {
            dismissProgressDialog();
        }
    }

    private class ClickListener implements OnClickListener {

        private int index = 0;

        public ClickListener(int i) {
            index = i;
        }

        public void onClick(View v) {
            viewPager.setCurrentItem(index);
        }
    }

    public class Adapter extends FragmentPagerAdapter {

        public Adapter(android.support.v4.app.FragmentManager fm) {
            super(fm);
        }

        @Override
        public android.support.v4.app.Fragment getItem(int arg0) {
            return views.get(arg0);
        }

        @Override
        public int getCount() {
            return views.size();
        }
    }

    @SuppressLint("ResourceAsColor")
    public class PagerChangeListener implements OnPageChangeListener {

        private int one = offset * 2 + bmpW;// 页卡1 -> 页卡2 偏移量
        private int two = one * 2;// 页卡1 -> 页卡3 偏移量

        public void onPageScrollStateChanged(int arg0) {
            if (arg0 == 0) {// 静止

            } else if (arg0 == 1) { // 正在滑动 滑动时，编辑，导入按钮不能点击

            } else if (arg0 == 2) {// 滑屏结束

            }
        }

        // 滑屏中
        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }

        public void onPageSelected(int arg0) {

            switchCursor(arg0);
            switchMenu(arg0);
            switchContent(arg0);

            currIndex = arg0;
        }

        /**
         * 移动游标
         */
        private void switchCursor(int arg0) {
            Animation animation = new TranslateAnimation(one * currIndex, one * arg0, 0, 0);
            animation.setFillAfter(true);// True:图片停在动画结束位置
            animation.setDuration(300);
            cursor.startAnimation(animation);
        }
    }

    /**
     * 选择内容
     */
    private void switchContent(int arg0) {

        abVideoManagerSelected.setVisibility(View.VISIBLE);
        if (arg0 == 0) {
            inWitchPage = 0;
            abVideoManagerImport.setVisibility(View.VISIBLE);
        } else if (arg0 == 1) {
            inWitchPage = 1;
            abVideoManagerImport.setVisibility(View.GONE);
        } else if (arg0 == 2) {
            inWitchPage = 2;
            abVideoManagerImport.setVisibility(View.GONE);
        }
    }

    /**
     * 设置字体颜色
     */
    private void switchMenu(int i) {
        if (i == 0) {
            first.setTextColor(getResources().getColorStateList(R.color.lpds_blue));
            second.setTextColor(getResources().getColorStateList(R.color.menu_videomanager_gray));
            third.setTextColor(getResources().getColorStateList(R.color.menu_videomanager_gray));
        } else if (i == 1) {
            first.setTextColor(getResources().getColorStateList(R.color.menu_videomanager_gray));
            second.setTextColor(getResources().getColorStateList(R.color.lpds_blue));
            third.setTextColor(getResources().getColorStateList(R.color.menu_videomanager_gray));
        } else if (i == 2) {
            first.setTextColor(getResources().getColorStateList(R.color.menu_videomanager_gray));
            second.setTextColor(getResources().getColorStateList(R.color.menu_videomanager_gray));
            third.setTextColor(getResources().getColorStateList(R.color.lpds_blue));
        }
    }

    /**
     * 反相遍历数据
     */
    private void untraverseData() {
        if (inWitchPage == 0) {
            for (int i = 0; i < myLocalVideoFragment.data.size(); i++) {
                VideoMangerActivity.myLocalVideoDeleteData.set(i, false);
            }
            myLocalData.clear();
            myLocalVideoFragment.adapter.notifyDataSetChanged();
        } else if (inWitchPage == 1) {
            for (int i = 0; i < myCloudVideoFragment.data.size(); i++) {
                VideoMangerActivity.myCloudVideoDeleteData.set(i, false);
            }
            myCloudData.clear();
            myCloudVideoFragment.adapter.notifyDataSetChanged();
        } else if (inWitchPage == 2) {
            for (int i = 0; i < myScreenShotFragment.data.size(); i++) {
                VideoMangerActivity.myScreenShotDeleteData.set(i, false);
            }
            myScreenData.clear();
            myScreenShotFragment.adapter.notifyDataSetChanged();
        }
    }

    /***
     * 遍历并选择数据
     */
    private void traverseData() {

        if (inWitchPage == 0) {// 本地视频
            myLocalData.clear();
            for (int i = 0; i < myLocalVideoFragment.data.size(); i++) {
                VideoMangerActivity.myLocalVideoDeleteData.set(i, true);
                myLocalData.add(myLocalVideoFragment.data.get(i));
            }
            myLocalVideoFragment.adapter.notifyDataSetChanged();
            allSelected.setText("取消全选");
        } else if (inWitchPage == 1) {// 云端视频
            myCloudData.clear();
            for (int i = 0; i < myCloudVideoFragment.data.size(); i++) {
                VideoMangerActivity.myCloudVideoDeleteData.set(i, true);
                myCloudData.add(myCloudVideoFragment.data.get(i));
            }
            myCloudVideoFragment.adapter.notifyDataSetChanged();
            allSelected.setText("取消全选");
        } else if (inWitchPage == 2) {// 本地图片
            myScreenData.clear();
            for (int i = 0; i < myScreenShotFragment.data.size(); i++) {
                VideoMangerActivity.myScreenShotDeleteData.set(i, true);
                myScreenData.add(myScreenShotFragment.data.get(i));
            }
            myScreenShotFragment.adapter.notifyDataSetChanged();
            allSelected.setText("取消全选");
        }
    }

    /**
     * 删除文件对话框
     */
    protected void showDeleteDialog(int size) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
        builder.setTitle("删除文件");
        builder.setMessage(Html.fromHtml("确认删除这" + TextUtil.toColor(size + "", "#fc3c2d") + "个文件?"));
        DialogInterface.OnClickListener positive = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                refreshBottomView(true);
                deleteData();
            }
        };
        builder.setPositiveButton(Html.fromHtml(TextUtil.toColor("确认", "#15b4eb")), positive);
        builder.setNegativeButton("取消", null);
        builder.create().show();
    }

    /**
     * 删除视频图片
     */
    private void deleteData() {
        DeleteDataTask task = new DeleteDataTask();
        task.execute();
    }

    /**
     * 删除视频图片异步类
     */
    private class DeleteDataTask extends AsyncTask<Void, Integer, Integer> {

        /**
         * 删除数据的总数量
         */
        private int myCloudVideoDeleteNumber = 0;
        private int myLocalVideoDeleteNumber = 0;
        private int myScreenShotDeleteNumber = 0;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgressDialog("删除中");
        }

        protected Integer doInBackground(Void... params) {
            /**删除本地视频*/
            if (inWitchPage == 0) {
                File file;
                for (VideoCaptureEntity entity : myLocalData) {
                    // 取消外部视频导入标志
                    VideoPreferences.getInstance().putBoolean(entity.getVideo_path(), false);
                    file = new File(entity.getVideo_path());
                    if (entity.getVideo_path().contains(Contants.SYSJ)) {
                        if (file.exists()) {
                            file.delete();
                        }
                    }
                    VideoCaptureManager.deleteById(entity.getId());
                    ++myLocalVideoDeleteNumber;
                }
                /**删除云端视频*/
            } else if (inWitchPage == 1) {
                for (VideoImage videoImage : myCloudData) {
                    String uri;
                    // 隐藏云端视频
                    VideoDisplayVideoEntity entity;
                    boolean flag;
                    if (!StringUtil.isNull(videoImage.getQn_key())) {// 七牛视频
                        uri = videoImage.getQn_key();
                        entity = DataManager.videoDisplayVideoQnKey(uri);
                        flag = true;
                    } else {
                        flag = false;
                        uri = videoImage.getYk_url();
                        entity = DataManager.videoDisplayVideoUrl(uri);
                    }
                    if (entity != null && entity.isResult()) {
                        ++myCloudVideoDeleteNumber;
                        if (flag) {
                            // 保存上传状态
                            VideoCaptureManager.updateStationByQnkey(uri, VideoCaptureEntity.VIDEO_STATION_HIDE);
                        }
                    }
                }
                /**删除截图*/
            } else if (inWitchPage == 2) {
                File f;
                for (ScreenShotEntity x : myScreenData) {
                    f = new File(x.getPath());
                    f.delete();
                    ++myScreenShotDeleteNumber;
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Integer result) {
            super.onPostExecute(result);

            dismissProgressDialog();

            ToastHelper.s("删除成功");
            if (inWitchPage == 0) {
                //MyLocalVideoFragment.loadDataFromDatabase2();
                // 加载本地视频
                DataManager.LOCAL.loadVideoCaptures();
                ToastHelper.s("一共删除" + myLocalVideoDeleteNumber + "个本地视频");
            } else if (inWitchPage == 1) {
                // 开放上拉下拉
                myCloudVideoFragment.pullToRefreshListView.setMode(PullToRefreshBase.Mode.BOTH);
                ToastHelper.s("一共删除" + myCloudVideoDeleteNumber + "个云端视频");
                myCloudVideoFragment.onPullDownToRefresh(null);
            } else if (inWitchPage == 2) {
                ToastHelper.s("一共删除" + myScreenShotDeleteNumber + "张截图");
                // 加载截图
                DataManager.LOCAL.loadScreenShots();
            }
        }
    }

    /**
     * 计算内外SD卡空间，剩余空间，应用视频截屏所占空间
     */
    public void loadSDCardSize() {

        Runnable r = new Runnable() {
            @Override
            public void run() {
                inSDCardAppSize = StorageUtil.getSysjSize();
                inSDCardUsedSize = StorageUtil.getSysjAvailableSize();
                inSDCardTotalSize = StorageUtil.getSysjTotalSize();
                inSDCardLeftSize = StorageUtil.getAvailMemory();
                post(new Runnable() {
                    @Override
                    public void run() {

                        myLocalVideoFragment.refreshStorge();
                        myScreenShotFragment.refreshStorge();
                    }
                });
            }
        };
        LightTask.post(r);
    }

    private String gameId ;

    /**
     * 分享成功
     */
    public void onEventMainThread(SharedSuccessEvent event){
        String title;
        switch (event.getChannel()){
            case "SYSJ":
                title = "你的视频已经被分享到";
                break;
            default:
                title = "你的视频已经同步分享到玩家广场，让更多人围观!";
                break;
        }
        new SharedSuccessDialog(this,title,gameId).show();
    }



    /**
     *选择了游戏的分类
     */
    public void onEventMainThread(SearchGame2VideoShareEvent event) {
        if (event != null && event.getAssociate() != null){
             gameId = event.getAssociate().getGame_id();
        }
    }
}
