package com.li.videoapplication.ui.activity;

import android.content.Intent;
import android.media.Image;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.google.gson.Gson;
import com.li.videoapplication.R;
import com.li.videoapplication.data.DataManager;
import com.li.videoapplication.data.image.GlideHelper;
import com.li.videoapplication.data.model.entity.OrderResultEntity;
import com.li.videoapplication.data.model.event.RefreshOrderDetailEvent;
import com.li.videoapplication.data.model.response.OrderResultCommitEntity;
import com.li.videoapplication.data.model.response.UploadKeyEntity;
import com.li.videoapplication.data.network.UITask;
import com.li.videoapplication.data.qiniu.http.ResponseInfo;
import com.li.videoapplication.data.qiniu.storage.UpCancellationSignal;
import com.li.videoapplication.data.qiniu.storage.UpCompletionHandler;
import com.li.videoapplication.data.qiniu.storage.UpProgressHandler;
import com.li.videoapplication.data.qiniu.storage.UploadManager;
import com.li.videoapplication.data.qiniu.storage.UploadOptions;
import com.li.videoapplication.framework.AsyncTask;
import com.li.videoapplication.framework.TBaseAppCompatActivity;
import com.li.videoapplication.tools.ToastHelper;
import com.li.videoapplication.ui.adapter.CoachShowImageAdapter;
import com.li.videoapplication.ui.adapter.OrderResultListAdapter;
import com.li.videoapplication.ui.dialog.ConfirmDoneDialog;
import com.li.videoapplication.ui.dialog.LoadingDialog;
import com.li.videoapplication.ui.view.SpanItemDecoration;
import com.li.videoapplication.utils.ExpandUtil;
import com.li.videoapplication.utils.ScreenUtil;
import com.li.videoapplication.utils.StringUtil;

import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import id.zelory.compressor.Compressor;
import io.rong.eventbus.EventBus;

/**
 * 确认订单完成
 */

public class ConfirmOrderDoneActivity extends TBaseAppCompatActivity implements View.OnClickListener,CoachShowImageAdapter.OnOperation  {
    public static int MAX_IMAGE_SIZE = 3;
    private String mAvatar;
    private String mNickName;
    private String mOrderId;
    private String mOrderCount;
    private int mCount = 0;
    private int mImageSize = 0;
    private int mChoiceMaxSize = MAX_IMAGE_SIZE ;
    private ExpandUtil mExpandUtil = new ExpandUtil();
    private RecyclerView mResultList;
    private RecyclerView mResultImage;
    private OrderResultListAdapter mAdapter;
    private CoachShowImageAdapter mImageAdapter;
    private List<String> mImages = new ArrayList<>();
    private LoadingDialog mLoadingDialog;
    @Override
    public void refreshIntent() {
        super.refreshIntent();
        Intent intent = getIntent();
        mOrderId = intent.getStringExtra("order_id");
        mAvatar = intent.getStringExtra("avatar");
        mNickName = intent.getStringExtra("nick_name");
        String count = intent.getStringExtra("count");
        try {
            mCount = Integer.parseInt(count);
        } catch (Exception e) {
            e.printStackTrace();
        }

        mChoiceMaxSize = mCount >=4 ? mCount:MAX_IMAGE_SIZE;
        mOrderCount = intent.getStringExtra("order_count");
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_confirm_order_done;
    }

    @Override
    public void afterOnCreate() {
        super.afterOnCreate();
        initToolbar();
        findViewById(R.id.tv_commit_order_result).setOnClickListener(this);
        mResultImage = (RecyclerView)findViewById(R.id.rv_order_result_image);
        mResultList = (RecyclerView)findViewById(R.id.rv_order_result);

        mResultList.setLayoutManager(new GridLayoutManager(this,4));
        mResultList.addItemDecoration(new SpanItemDecoration(ScreenUtil.dp2px(8),false,false,true,true));
        mAdapter = new OrderResultListAdapter(this,mCount);
        mResultList.setAdapter(mAdapter);

        mImages = new ArrayList<>();
        mImages.add(R.drawable.add_big+"");
        mImageAdapter = new CoachShowImageAdapter(this,mImages,mChoiceMaxSize);
        mResultImage.setLayoutManager(new GridLayoutManager(this,4));
        mResultImage.setAdapter(mImageAdapter);
        mResultImage.addItemDecoration(new SpanItemDecoration(ScreenUtil.dp2px(10),true,false,false,true));

        mLoadingDialog = new LoadingDialog(this);
        mLoadingDialog.setProgressText("提交中...");

        ((TextView)findViewById(R.id.tv_user_nick_name)).setText(mNickName);
        ((TextView)findViewById(R.id.tv_user_place_order_num)).setText("下单数："+mOrderCount);

        final ImageView icon = (ImageView)findViewById(R.id.civ_user_icon);
        GlideHelper.displayImageWhite(this,mAvatar,icon);
    }


    private void initToolbar(){
        findViewById(R.id.tb_back).setOnClickListener(this);
        ((TextView)findViewById(R.id.tb_title)).setText("订单信息");
        setSupportActionBar(((Toolbar)findViewById(R.id.toolbar)));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        String path = mExpandUtil.getImagePath(this,requestCode,resultCode,data);      //获取图片路径
        if (StringUtil.isNull(path)){
            return;
        }

        mImageSize++;
        mImages.add(0,path);
        //不用使用notify
        mImageAdapter.notifyDataSetChanged();
    }

    @Override
    public void removeImage(int index) {
        if (mImageSize > 0){
            mImageSize--;
        }

        mImages.remove(index);
        mImageAdapter.setNewData(mImages);
    }

    @Override
    public void onClickImage(View view, int index) {
        if (mImageSize == mChoiceMaxSize){
            //show detail
            ImageDetailSceneActivity.startImageDetailActivity(this,view,0,new String[]{mImages.get(index)},true);
        }else {
            if (mImages.size()-1 == index ){
                //pick image
                mExpandUtil.openPhotoPick(this);
            }else {
                //show detail
                ImageDetailSceneActivity.startImageDetailActivity(this,view,0,new String[]{mImages.get(index)},true);
            }
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
            case R.id.tv_commit_order_result:
                List<Integer> result = mAdapter.getOrderResult();
                for (int i = 0; i < result.size(); i++) {
                    if (result.get(i) == -1){
                        ToastHelper.l("第"+ StringUtil.convert2Chinese(i+1)+"局未选择游戏结果哦~");
                        return;
                    }
                }

                if (mImageSize <= 0){
                    ToastHelper.s("至少选择一张图片哦~");
                    return;
                }

                if (!mLoadingDialog.isShowing()){
                    mLoadingDialog.show();
                }
                DataManager.getUploadKey(getMember_id(),mImageSize);
                break;
        }
    }

    private String arrayKey2Str(List<String> arrayKey){
        String key = null;
        for (String k:
                arrayKey) {
            if (key == null){
                key = k;
            }else {
                key +="|" + k;
            }
        }
        return key;
    }


    public UpCompletionHandler mUploadHandler =  new UpCompletionHandler() {
        @Override
        public void complete(String key, ResponseInfo info, JSONObject response) {
            if(info != null && info.isOK()){
                successUpload();
            }else {
                failedUpload();
            }
        }
    };

    private void commitResult(){
        List<Integer> result = mAdapter.getOrderResult();
        List<OrderResultEntity.DataBean> data = new ArrayList<>();
        //转换成 后台需要的index
        for (int i = 0; i < result.size(); i++) {
            switch (result.get(i) % 4){
                case 1:
                    data.add(new OrderResultEntity.DataBean(i+1,1));
                    break;
                case 2:
                    data.add(new OrderResultEntity.DataBean(i+1,0));
                    break;
                case 3:
                    data.add(new OrderResultEntity.DataBean(i+1,-1));
                    break;
            }
        }


        String dataStr = "";
        Gson gson = new Gson();
        dataStr = gson.toJson(data);
        if (mUploadEntity != null){
            DataManager.confirmOrderResult(getMember_id(),mOrderId,dataStr,arrayKey2Str(mUploadEntity.getAData()));
        }
    }


    private int mSuccessUpload = 0;
    public synchronized void successUpload(){
        mSuccessUpload++;

        if (mSuccessUpload == mImageSize){
            commitResult();
        }
    }

    public synchronized  void failedUpload(){
        if (mSuccessUpload != -1){
            ToastHelper.s("图片上传失败");
            mLoadingDialog.dismiss();
            mSuccessUpload = -1;
        }
    }

    /**
     *图片key获取成功
     */
    private UploadKeyEntity mUploadEntity;
    public void onEventMainThread(UploadKeyEntity entity){
        if (entity.isResult()){
            mUploadEntity = entity;
            mSuccessUpload = 0;
            //
            for (int i = 0; i < mImageSize; i++) {
                new UploadTask().execute(i);
            }

        }else {
            ToastHelper.s("图片上传失败请稍后重试~");
        }

    }

    /**
     *
     */
    public void onEventMainThread(OrderResultCommitEntity entity){
        if (mLoadingDialog != null && mLoadingDialog.isShowing()){
            mLoadingDialog.dismiss();
        }
        if (entity != null && entity.isResult()){
            //不可编辑图片
            mImageAdapter.setUploadDone(true);
            mImages.remove(mImages.size()-1);
            mImageAdapter.notifyDataSetChanged();

            //不可编辑结果
            mAdapter.setChoiceDone(true);
            mAdapter.notifyDataSetChanged();

            //显示确认对话框
            new ConfirmDoneDialog(this).show();

            //修改提示语
            ((TextView)findViewById(R.id.tv_upload_image_tip)).setText("游戏结果");

            //隐藏提交按钮
            findViewById(R.id.tv_commit_order_result).setVisibility(View.GONE);

            ToastHelper.l("提交成功~");
            EventBus.getDefault().post(new RefreshOrderDetailEvent(
                    mOrderId,
                    "",
                    ""));


        }else {
            ToastHelper.l("提交失败~");
        }
    }


    /**
     * 压缩图片
     * @param path
     * @return
     */
    public File compressedImage(String path){
        if (path == null){
            return null;
        }
        try {
            File file = new File(path);
            return  new Compressor.Builder(this).build().compressToFile(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public class  UploadTask extends AsyncTask<Integer,Integer,File> {
        private UploadManager mUploadManager = new UploadManager();
        private int mIndex;
        @Override
        protected File doInBackground(Integer... params) {
            mIndex = params[0];
            return  compressedImage(mImages.get(mIndex));
        }

        @Override
        protected void onPostExecute(File file) {
            super.onPostExecute(file);
            try {
                if (file != null){
                    mUploadManager.put(file, mUploadEntity.getAData().get(mIndex), mUploadEntity.getToken(),mUploadHandler,options);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        UploadOptions options = new UploadOptions(null, null, false, new UpProgressHandler() {
            @Override
            public void progress(String key, double percent) {
                // 上传进度
                // Log.i("UploadTask", "number=" + "/percent=" + percent);
            }
        }, new UpCancellationSignal() {

            @Override
            public boolean isCancelled() {
                return false;
            }
        });
    }
}
