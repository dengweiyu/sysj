package com.li.videoapplication.ui.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;
import com.li.videoapplication.R;
import com.li.videoapplication.data.DataManager;
import com.li.videoapplication.data.model.entity.NetworkError;
import com.li.videoapplication.data.model.response.CoachEditSignEntity;
import com.li.videoapplication.data.model.response.CommitSignEntity;
import com.li.videoapplication.data.model.response.UploadKeyEntity;
import com.li.videoapplication.data.network.RequestUrl;
import com.li.videoapplication.data.qiniu.http.ResponseInfo;
import com.li.videoapplication.data.qiniu.storage.UpCancellationSignal;
import com.li.videoapplication.data.qiniu.storage.UpCompletionHandler;
import com.li.videoapplication.data.qiniu.storage.UpProgressHandler;
import com.li.videoapplication.data.qiniu.storage.UploadManager;
import com.li.videoapplication.data.qiniu.storage.UploadOptions;
import com.li.videoapplication.framework.AsyncTask;
import com.li.videoapplication.framework.TBaseAppCompatActivity;
import com.li.videoapplication.tools.ToastHelper;
import com.li.videoapplication.ui.ActivityManager;
import com.li.videoapplication.ui.adapter.CoachShowImageAdapter;
import com.li.videoapplication.ui.dialog.LoadingDialog;
import com.li.videoapplication.utils.ExpandUtil;
import com.li.videoapplication.utils.ScreenUtil;
import com.li.videoapplication.utils.StringUtil;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import id.zelory.compressor.Compressor;

/**
 * 编辑陪练信息
 */

public class CoachInfoEditActivity extends TBaseAppCompatActivity implements View.OnClickListener,CoachShowImageAdapter.OnOperation {
    private RecyclerView mImageList;
    public static int MAX_IMAGE_SIZE = 3;
    private static int MAX_INTRODUCE_LENGTH = 60;
    private List<String> mImages = new ArrayList<>();
    private ExpandUtil mExpandUtil = new ExpandUtil();
    private CoachShowImageAdapter mAdapter;
    private int mImageSize = 0;
    private EditText mInput;
    private TextView mInputNumber;
    private View mRoot;
    private CoachEditSignEntity mSignEntity;
    private LoadingDialog mLoadingDialog;
    @Override
    protected int getContentView() {
        return R.layout.activity_edit_coach_info;
    }

    @Override
    public void afterOnCreate() {
        super.afterOnCreate();

        initToolbar();
        mLoadingDialog = new LoadingDialog(this);
        mLoadingDialog.setProgressText("加载中..");

        mRoot = findViewById(R.id.ll_edit_coach_info_root);
        mRoot.setVisibility(View.GONE);

        mInput = (EditText)findViewById(R.id.et_input_introduce);

        mInput.addTextChangedListener(mTextWatcher);
        mInputNumber = (TextView)findViewById(R.id.tv_input_length);
        mInputNumber.setText(MAX_INTRODUCE_LENGTH +"");
        mImageList = (RecyclerView)findViewById(R.id.rv_coach_show_image);

        mImageList.setLayoutManager(new GridLayoutManager(this,MAX_IMAGE_SIZE));
        mImageList.setItemAnimator(new DefaultItemAnimator());

        mAdapter = new CoachShowImageAdapter(this,mImages,MAX_IMAGE_SIZE);
        mImageList.setAdapter(mAdapter);

    }

    @Override
    public void loadData() {
        super.loadData();
        mLoadingDialog.show();
        DataManager.getCoachSign(getMember_id());
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
        mAdapter.notifyDataSetChanged();
    }


    @Override
    public void removeImage(int index){
        if (mImageSize > 0){
            mImageSize--;
        }

        String path = mImages.get(index);
        mImages.remove(index);

        //上次编辑的图片
        if (path.indexOf(mSignEntity.getOData().getImgUrl()) >= 0){
            mSignEntity.getOData().getPic().remove(index);
        }

        //不要使用mAdapter.notifyDataSetChanged();
        mAdapter.setNewData(mImages);

    }

    @Override
    public void onClickImage(View source,int index){
        if (mImageSize == MAX_IMAGE_SIZE){
            //show detail
            ImageDetailSceneActivity.startImageDetailActivity(CoachInfoEditActivity.this,source,0,new String[]{mImages.get(index)},true);
        }else {
            if (mImages.size()-1 == index ){
                //pick image
                mExpandUtil.openPhotoPick(CoachInfoEditActivity.this);
            }else {
                //show detail
                ImageDetailSceneActivity.startImageDetailActivity(CoachInfoEditActivity.this,source,0,new String[]{mImages.get(index)},true);
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tb_back:
                finish();
                break;
            case R.id.tb_topup_record:          //保存
                if (checkInput()){
                    mLoadingDialog.show();

                    if (mImageSize == 0){
                        if (mSignEntity != null && mSignEntity.getOData().getPic() != null && mSignEntity.getOData().getPic().size() > 0){
                            //不需要重新上次本地图片
                            commitSign();
                        }else {
                            ToastHelper.s("至少选择一张图片哦~");
                            mLoadingDialog.dismiss();
                        }
                    }else {
                        DataManager.getUploadKey(getMember_id(),mImageSize);
                    }

                }
                break;
        }
    }

    private void initToolbar(){
        findViewById(R.id.tb_back).setOnClickListener(this);
        ((TextView)findViewById(R.id.tb_title)).setText("陪练信息");
        TextView right = (TextView)findViewById(R.id.tb_topup_record);
        right.setVisibility(View.VISIBLE);
        right.setText("保存");
        right.setBackgroundColor(getResources().getColor(R.color.ab_backdround_red));
        right.setTextColor(Color.WHITE);
        right.setPadding(ScreenUtil.dp2px(this,8),right.getTop(),ScreenUtil.dp2px(this,8),right.getBottom());
        right.setOnClickListener(this);
        right.setVisibility(View.GONE);
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)right.getLayoutParams();
        params.setMargins(params.leftMargin,ScreenUtil.dp2px(8),ScreenUtil.dp2px(16),ScreenUtil.dp2px(8));
        right.setLayoutParams(params);
        setSupportActionBar(((Toolbar)findViewById(R.id.toolbar)));
    }

    private boolean checkInput(){
        if (mInput.getText().length() == 0){
            ToastHelper.s("请输入个性签名哦~");
        }else {
            if (mImageSize == 0 && mImages.size() == 0){
                ToastHelper.s("至少选择一张图片哦~");
            }else {
                return true;
            }
        }
        return false;
    }


    private TextWatcher mTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void afterTextChanged(Editable s) {
           if (s.length() == MAX_INTRODUCE_LENGTH){
               ToastHelper.s("最多输入60字");
           }
           mInputNumber.setText((MAX_INTRODUCE_LENGTH - s.length())+"");
        }
    };

    private UploadKeyEntity mUploadEntity;
    public void onEventMainThread(UploadKeyEntity entity){
        if (entity.isResult()){
            mUploadEntity = entity;
            mSuccessUpload = 0;
            //
            for (int i = 0; i < mImageSize; i++) {
                //本地图片
                if (mImages.get(i).indexOf(mSignEntity.getOData().getImgUrl()) < 0){
                    new UploadTask().execute(i);
                }
            }

        }else {
            ToastHelper.s("图片上传失败请稍后重试~");
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


    public  UpCompletionHandler mUploadHandler =  new UpCompletionHandler() {
        @Override
        public void complete(String key, ResponseInfo info, JSONObject response) {
            if(info != null && info.isOK()){
                successUpload();
            }else {
                failedUpload();
            }
        }
    };


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


    private int mSuccessUpload = 0;
    public synchronized void successUpload(){
        mSuccessUpload++;

        if (mSuccessUpload == mImageSize){
            commitSign();
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
     * 提交签名
     */
    private void commitSign(){
        List<String> newKey = Lists.newArrayList();

        if (mUploadEntity != null && mUploadEntity.getAData() != null){
            newKey.addAll(mUploadEntity.getAData());
        }

        if (mSignEntity.getOData().getPic() != null){
            newKey.addAll(mSignEntity.getOData().getPic());
        }
        DataManager.commitSign(mInput.getText().toString(),getMember_id(),arrayKey2Str(newKey));
    }

    /**
     *获取陪练信息
     */
    public void onEventMainThread(CoachEditSignEntity entity){
        if (entity.isResult()){
            mSignEntity = entity;
            mRoot.setVisibility(View.VISIBLE);
            findViewById(R.id.tb_topup_record).setVisibility(View.VISIBLE);
            mInput.setText(entity.getOData().getIndividuality_signature());
            if (entity.getOData().getPic() != null){
                for (String key:
                        entity.getOData().getPic()) {
                    mImages.add(entity.getOData().getImgUrl()+key);
                }
            }

            mImages.add(R.drawable.add_big+"");
            mAdapter.setNewData(mImages);
        }else {
            ToastHelper.s("无法获取陪练信息，请稍后重试~");
        }

        mLoadingDialog.dismiss();
    }

    /**
     *签名提交结果
     */
    public void onEventMainThread(CommitSignEntity entity){
        if (entity.isResult()){
            ToastHelper.s("编辑成功");
            finish();
        }else {
            ToastHelper.s("编辑失败");
        }
        mLoadingDialog.dismiss();
    }

    /**
     *
     */
    public void onEventMainThread(NetworkError error){
        if (RequestUrl.getInstance().commitSign().equals(error.getUrl())){
            mLoadingDialog.dismiss();
            ToastHelper.s("当前网络不可用，请检查后重试");
        }
    }
}
